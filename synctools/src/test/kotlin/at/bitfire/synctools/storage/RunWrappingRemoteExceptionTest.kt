/*
 * Copyright © All Contributors. See LICENSE and AUTHORS in the root directory for details.
 */

package at.bitfire.synctools.storage

import android.os.RemoteException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class RunWrappingRemoteExceptionTest {

    @Test
    fun `firstOrNull() on flow that emits inside the block`() = runTest {
        val flow = flow {
            runWrappingRemoteException {
                emit(1)
                emit(2)
            }
        }

        // firstOrNull() aborts the flow with an AbortFlowException (a CancellationException) after the first element
        assertEquals(1, flow.firstOrNull())
        assertEquals(1, flow.first())
    }

    @Test(expected = CancellationException::class)
    fun `CancellationException is passed through`() {
        runWrappingRemoteException { throw CancellationException() }
    }

    @Test(expected = LocalStorageException::class)
    fun `RemoteException is wrapped`() {
        runWrappingRemoteException { throw RemoteException() }
    }

    @Test(expected = LocalStorageException::class)
    fun `RuntimeException is wrapped`() {
        runWrappingRemoteException { throw IllegalStateException() }
    }

}
