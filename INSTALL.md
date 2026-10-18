# Installing Bay Area Sync on Android

Bay Area Sync puts your company calendars and contacts on your Android phone: your own calendars, calendars shared by
colleagues and the company address books. It is set up once with your email address and password.

Until the app is available on Google Play, it is installed directly from our download page.

<img src="docs/download-qr.png" alt="QR code for the download" width="200">

**Download:** <https://github.com/aknuth/davx5-bayarea/releases/latest/download/BayAreaSync.apk>

## 1. Install the app

1. **Samsung phones only:** open *Settings → Security and privacy → Auto Blocker* and switch it **off** for now. You
   can switch it on again after step 1.
2. On your phone, scan the QR code with the camera (or open the download link in Chrome).
3. Open the downloaded file *BayAreaSync.apk* (tap the download notification, or find it under *Files → Downloads*).
4. Android asks whether Chrome may install apps: tap **Settings**, switch on **Allow from this source**, go back.
5. Tap **Install**. If Google Play Protect warns about an unknown app, choose **More details → Install anyway**.

## 2. Set up your account

1. Open **Bay Area Sync** and follow the welcome screens:
   - Permissions: switch on **All of the below** and allow access to calendar, contacts and notifications.
   - Regular sync intervals: tap the switch and **allow** the app to run in the background. Without this, Android may
     stop synchronizing.
2. Tap **+** (Add account), enter your **email address** and **password**, tap **Login**.
3. Enter an account name, for example *Work*, and tap **Add account**.

That's it. After a minute, your calendars appear in the calendar app (Samsung Calendar or Google Calendar) and your
contacts in the contacts app. New calendars shared with you later appear automatically.

> If a calendar is missing in the calendar app, check that it is switched on in the calendar app's settings (list of
> calendars).

## Updates

Install a new version the same way, from the same download link: your account and data are kept. If you want updates
to be installed automatically, you can use the free app [Obtainium](https://obtainium.imranr.dev/) and add
`https://github.com/aknuth/davx5-bayarea` as source.

## Security note

The app is signed by Bay Area Affiliates. Certificate SHA-256 fingerprint:

```
4F:1F:44:AB:88:B5:57:D2:E1:5E:83:18:D4:87:7C:8D:8A:6A:92:FF:A2:B4:D2:58:E5:5C:B2:CF:FE:35:9F:59
```

Privacy policy: [PRIVACY.md](PRIVACY.md)
