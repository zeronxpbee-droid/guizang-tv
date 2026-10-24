# Tasks

## Active Goal

- Status: In Progress
- Goal: Add a separately opt-in, recoverable standby-wake mode for the user's `MiTV-MFTP0`, so a remote-control wake can enter GuiZang after Xiaomi finishes waking without replacing the system Home.
- Allowed scope: Read-only wake diagnostics; an MFTP0-only startup policy and settings choice; bounded same-boot wake debouncing; unit tests and documentation; Debug APK build and same-signature ADB upgrade; temporary restoration of `start_3rd_app` to the isolated package; and controlled standby/wake verification with the user's remote.
- Forbidden scope: Changing the default Home app, disabling or clearing Xiaomi packages, Root, accessibility or overlay services, uninstalling apps, clearing preferences, removing the existing full-boot guard, storing raw personal logs, claiming to suppress system-level boot advertising, publishing a Release, or merging a PR.
- Definition of Done: The settings UI offers Off, full reboot only, and full reboot plus standby wake; existing enabled installs migrate to full reboot only; wake mode is limited to verified MFTP0 firmware behavior and debounced; Home and the stock launcher remain available; the prior empty `start_3rd_app` value and an in-app Off switch provide rollback; build/tests/lint pass; same-signature upgrade preserves existing preferences; and at least one remote standby/wake plus rollback-path check is recorded, or any unverified step is stated explicitly.

## Execution Loop

- Type: Development and real-device wake verification loop.
- Roles: Codex plans, implements and records; automated tests and read-only device state evaluate; the user supplies any unavoidable television confirmation or private input.
- Evidence: Git diff and tests, ADB settings/preferences baselines, BootStart diagnostics, foreground activity, one remote standby/wake observation, Home-key behavior, and rollback-state checks.
- Safe restart boundary: Feature branch plus independently installable Debug package; original settings are captured before writes and restored on failure.
- Human intervention: Required for the physical remote standby/wake action, an ADB authorization prompt, or recovery if network debugging does not return after wake.

## Next Task

Implement and verify the separately opt-in MFTP0 standby-wake mode, then record the exact wake, Home-key and rollback outcomes before marking this task complete.
