# Tasks

## Active Goal

- Status: In Progress
- Goal: Build a safe `MiTV-MFTP0` compatibility variant from the original repository's latest `main`, preserving the opt-in Codex experiment and existing `MiTV-MTSU0` behavior.
- Allowed scope: Device-profile code, the verified Xiaomi media-explorer album entry, an independent application ID, focused unit tests, build configuration, compatibility documentation, and a local debug APK build.
- Forbidden scope: Enabling unverified HDMI mappings, enabling boot auto-entry for `MiTV-MFTP0`, changing the default Home app, disabling Xiaomi system packages, installing an APK, writing TV settings, creating real signing keys, publishing a release, or merging the compatibility PR.
- Definition of Done: The custom variant builds from upstream `main`; `MiTV-MFTP0` receives the verified album entry while risky Xiaomi capabilities remain gated; existing `MiTV-MTSU0` behavior and Codex opt-in remain intact; relevant tests and static checks pass; results and unverified boundaries are recorded.

## Next Task

Inspect current tests and build tooling, implement the smallest model-specific profile, then build and verify without installing it on the television.
