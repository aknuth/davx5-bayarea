/*
 * Copyright © All Contributors. See LICENSE and AUTHORS in the root directory for details.
 */

package at.bitfire.davdroid.sync.worker

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.hilt.work.HiltWorker
import androidx.work.WorkManager
import androidx.work.await
import androidx.work.WorkerParameters
import at.bitfire.davdroid.accounts.AccountId
import at.bitfire.davdroid.accounts.DbAccountId
import at.bitfire.davdroid.accounts.LegacyAccount
import at.bitfire.davdroid.db.Service
import at.bitfire.davdroid.repository.DavServiceRepository
import at.bitfire.davdroid.servicedetection.RefreshCollectionsWorker
import at.bitfire.davdroid.sync.SyncDataType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

/**
 * Handles scheduled sync requests.
 *
 * The different periodic sync workers each carry a unique work name composed of the account and
 * authority which they are responsible for. For each account there will be multiple dedicated periodic
 * sync workers for each authority. See [PeriodicSyncWorker.workerName] for more information.
 *
 * Deferrable: yes (periodic)
 *
 * Expedited: no (→ no [getForegroundInfo])
 *
 * Long-running: no
 *
 * **Important:** If this class is renamed (or its package is changed), already enqueued workers won't
 * run anymore because WorkManager references the work by the full class name.
 */
@HiltWorker
class PeriodicSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : BaseSyncWorker(appContext, workerParams) {

    @AssistedFactory
    @VisibleForTesting
    interface Factory {
        fun create(appContext: Context, workerParams: WorkerParameters): PeriodicSyncWorker
    }

    @Inject
    lateinit var serviceRepository: DavServiceRepository

    /**
     * Bay Area Sync: refreshes the collection list on every periodic sync, so that collections which have
     * been added on the server (for instance shared calendars) show up without user interaction.
     *
     * Waits for the refresh (with timeout), so that newly found collections are synchronized by this sync.
     * Otherwise the one-time sync that the refresh enqueues for them would be skipped while this sync runs.
     */
    override suspend fun beforeSync(accountId: AccountId, dataType: SyncDataType) {
        val serviceType = when (dataType) {
            SyncDataType.CONTACTS -> Service.TYPE_CARDDAV
            SyncDataType.EVENTS -> Service.TYPE_CALDAV
            SyncDataType.TASKS -> return       // CalDAV service is already refreshed by EVENTS sync
        }
        val service = serviceRepository.getByAccountIdAndType(accountId, serviceType) ?: return
        val (workerName, enqueued) = RefreshCollectionsWorker.enqueue(applicationContext, service.id, background = true)
        enqueued.await()    // otherwise the flow could still report the previous (finished) refresh
        val finished = withTimeoutOrNull(REFRESH_TIMEOUT) {
            WorkManager.getInstance(applicationContext)
                .getWorkInfosForUniqueWorkFlow(workerName)
                .first { infos -> infos.all { it.state.isFinished } }
        }
        if (finished == null)
            logger.warning("Collection refresh didn't finish in time, continuing with sync")
    }

    companion object {

        /** how long to wait for the collection refresh before a periodic sync */
        private val REFRESH_TIMEOUT = 1.minutes

        /**
         * Unique work name of this worker. Can also be used as tag.
         *
         * Mainly used to query [WorkManager] for work state (by unique work name or tag).
         *
         * @param accountId [AccountId] of the account this worker is running for
         * @param dataType  data type to be synchronized
         *
         * @return Name of this worker composed as "periodic-sync $authority ${account.type}/${account.name}"
         */
        fun workerName(accountId: AccountId, dataType: SyncDataType): String {
            return when (accountId) {
                is LegacyAccount -> {
                    val account = accountId.androidAccount
                    "periodic-sync $dataType ${account.type}/${account.name}"
                }
                is DbAccountId -> TODO("Operation not yet implemented")
            }
        }

    }

}