# Bay Area Sync

Bay Area Sync is a CalDAV/CardDAV sync app for the mail platform of Bay Area Affiliates, based on
[DAVx⁵ OSE](https://github.com/bitfireAT/davx5-ose) (GPLv3). Users only enter their email address, password and a
description – all calendars and address books (own ones, shared ones and the global address lists of their company)
are synchronized without further interaction.

Privacy policy: [PRIVACY.md](PRIVACY.md)

## Differences to DAVx⁵

Functionality (`core`, marked with "Bay Area Sync" comments):

- All detected collections are preselected for synchronization (`DefaultsProvider`).
- Email login looks for the DAV server at `dav.<domain>` first (`DavResourceFinder`).
- The collection list is refreshed on every periodic sync, and newly found collections are synchronized at once
  (`PeriodicSyncWorker`, `RefreshCollectionsWorker`).
- The calendar owner account is the email address (user name), not the account name, which is a description
  (`LocalCalendarStore`).
- Own HTTP User-Agent and PRODID (`ProductIds`).

App variant (`app-ose`):

- `applicationId` `com.bayareaaffiliates.sync`, own account types, version (`app-ose/build.gradle.kts`).
- Login starts with the email login (`BayAreaLoginTypesProvider`), shorter intro, slim navigation drawer, colors.
- Name: `scripts/bayarea-branding-strings.py` generates overrides for all strings that contain "DAVx⁵" (re-run after
  updating the `l10n` submodule).
- Icons: `scripts/bayarea-icons.py` generates the launcher icons and the Play icon from `app-ose/branding/icon.png`.
- No location permissions (the "WiFi name" restriction is hidden).

## Building

Requirements: Android SDK (`local.properties` with `sdk.dir`), JDK 21 for the Gradle toolchain, submodules
(`git submodule update --init --recursive`).

```sh
./gradlew :app-ose:assembleOseDebug     # debug APK
./gradlew :app-ose:bundleOseRelease     # release bundle (.aab) for Google Play
```

## Release

1. Increase `versionCode` and `versionName` in `app-ose/build.gradle.kts` (scheme `MmmppIIII`, e.g. 5.0.1 →
   `500010000`).
2. Release builds are signed with the **upload key**. Key and passwords are *not* in the repository but in
   `~/.android-keys/bayareasync-upload.properties` (or the file given in `BAYAREA_SIGNING_PROPERTIES`):

   ```properties
   storeFile=/path/to/bayareasync-upload.jks
   storePassword=…
   keyAlias=upload
   keyPassword=…
   ```

   Keep a backup of both files. Google Play keeps the actual app signing key (Play App Signing); a lost upload key can
   be reset via the Play Console.
3. Upload `app-ose/build/outputs/bundle/oseRelease/*.aab` to the Play Console.

## Updating from DAVx⁵

```sh
git remote add upstream https://github.com/bitfireAT/davx5-ose.git   # once
git fetch upstream && git merge upstream/main
git submodule update --init --recursive
python3 scripts/bayarea-branding-strings.py
```
