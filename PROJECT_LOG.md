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

## 2026-10-22 — MiTV-MFTP0 full feature validation started

- User explicitly authorized adapting and validating HDMI, the system input panel, opt-in Codex and boot auto-entry on the real television.
- Captured the pre-change baseline: `MiTV-MFTP0` / `alita`, Android 9 / API 28, firmware `23.2.10.6129`, `start_3rd_app` empty, `boot_auto_play=0`, boot count 71, and the compatibility package absent.
- The firmware exposes HDMI 1–3 in its actual input panel. Direct system tests mapped source 23/24/25 to TV Input HW5/HW6/HW7; source 26 created no session and is excluded. HDMI 1 had a connected Fire TV Stick and active CEC route.
- The explicit input-popup receiver path did not display a window. A package-scoped implicit broadcast with background delivery did display the Xiaomi `settings_window`; the compatibility profile now uses that path only on `MiTV-MFTP0`.
- Direct `MainActivityUserMode` launch was rejected by Xiaomi's `HOME_STATE` signature permission. The public `com.mitv.tvhome.MainActivity` alias resolved and opened the original Home, so the profile now uses the public alias.
- Code build, app-driven device checks, Codex pairing and full reboot are pending. No package has been installed and no persistent setting has been changed yet.
- First CI attempt `39755127080` failed at Java compilation because Android SDK stubs do not expose the framework's `FLAG_RECEIVER_INCLUDE_BACKGROUND` symbol. The device test had proven the flag behavior, so the implementation now uses the documented framework bit value `0x01000000` behind a named local constant; no APK from the failed run was installed.
- Second CI attempt `39755562286` compiled and packaged the APK but stopped after 1 of 25 Java tests failed: the clone-protection test still expected Bilibili at index 0 after HDMI 1 became the device-specific first item. The assertion was corrected to the new intended default; lint and Python tests did not run after the unit-test failure.
