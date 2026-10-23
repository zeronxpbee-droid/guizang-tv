# Tasks

## Active Goal

- Status: Complete
- Goal: Adapt and verify HDMI shortcuts, the Xiaomi input-source panel, opt-in Codex text chat, and experimental boot auto-entry on the user's `MiTV-MFTP0` without replacing the system Home.
- Allowed scope: Read-only device discovery; model-specific profiles and tests; Debug APK builds; side-by-side ADB installation of `io.github.zeronxpbee.guizangtv.mftp0`; explicit activity, HDMI and input-panel launches; application UI configuration; a repository-external private Codex companion directory; temporary and recoverable `start_3rd_app` configuration; and one controlled full reboot after all non-reboot checks pass.
- Forbidden scope: Changing the default Home app, disabling or clearing Xiaomi system packages, uninstalling existing apps, overwriting an unknown boot target without recording it, enabling Codex by editing preferences behind the UI, storing secrets or raw personal logs in the repository, exposing the companion service publicly, publishing a Release, or merging PR #2.
- Definition of Done: HDMI 1/2/3 mappings and the input panel are identified from this firmware and verified through the adapted app; Codex remains opt-in and completes at least two visible text turns after a trusted local pairing; boot auto-entry completes one full-reboot test with the original system setting recoverable; stock Home, Home key, Xiaomi packages and failure fallbacks remain available; build/tests/lint pass; exact passed, failed and unverified items are recorded.

## Execution Loop

- Type: Development and real-device verification loop.
- Roles: Codex plans, implements and records; automated tests and read-only device state evaluate; the user supplies any unavoidable television confirmation or private input.
- Evidence: Git diff and tests, ADB component/settings snapshots, foreground activity and UI hierarchy or screenshot checks, Codex visible message state, boot diagnostics, and restored-state checks.
- Safe restart boundary: Feature branch plus independently installable Debug package; original settings are captured before writes and restored on failure.
- Human intervention: Required for an ADB authorization prompt, unavailable HDMI equipment, private pairing entry that cannot be completed safely, or recovery if network debugging does not return after reboot.

## Next Task

No implementation task is active. Await the user's decision on whether standby wake should remain on the Xiaomi Home (the verified safe behavior) or become a separately designed opt-in feature; do not treat that as part of full-reboot auto-entry.
