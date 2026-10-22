# Project Log

## 2026-10-22 — Secure fork bootstrap

- Created the public GitHub fork `zeronxpbee-droid/guizang-tv` from `op7418/guizang-tv`.
- Cloned it into `/Users/poon/workspace/projects/MITV-UI`.
- Confirmed `origin` points to the user's fork and `upstream` points to the original repository.
- Started branch `codex/secure-fork-bootstrap`; no direct change was made to `main`.
- Expanded `.gitignore` for signing keys, companion pairing state, environment files, ADB/SSH keys, credentials, private directories, and raw diagnostics.
- No real secret values were created, read, copied, or committed.

## 2026-10-22 — MiTV-MFTP0 compatibility work started

- User authorized PR #1; GitHub merged it as `c4cc14d572eb8ef8328f18ca5636d2a83febd239`.
- Confirmed the original repository's latest `main` was still `312c01119a327f402e68818670a6542f1e643e56`; the fork was two commits ahead and zero behind after the merge.
- Started `codex/mitv-mftp0-compat` from the updated fork `main`.
- Added a model-specific safe profile, the verified Xiaomi Media Explorer album action, an independently installable application ID, and focused policy tests.
- Kept `MiTV-MFTP0` HDMI switching, input popup and boot auto-entry disabled because their actual behavior is not verified on this firmware.
- GitHub Actions run `39749608158` first built the code commit successfully. Final verification run `39750226976` then built branch commit `1ea8932154c569a0f9c0d6351e472546b3cc2059`; `assembleDebug`, Java unit tests, Android lint and the Python wallpaper tests all passed.
- Downloaded the final 1.9 MB Debug artifact to `artifacts/run-39750226976/app-debug.apk`; SHA-256 is `027c005fe5357ee53cc0d81a7aee3edc5f5bb27cd8f7f088d02a4140e6737d16`. ZIP integrity validation passed.
- A direct local Python test attempt failed before execution because the system Python lacked Pillow. The isolated CI environment installed its declared Pillow dependency and passed the same tests; this was an environment dependency failure, not a passing local test.
- Opened PR #2 for review and CI only. It remains unmerged.
- No APK has been installed and no television setting has been changed. Real-device launch behavior remains unverified.
