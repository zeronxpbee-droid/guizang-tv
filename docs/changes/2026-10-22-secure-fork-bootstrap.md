# Secure fork bootstrap

- Change-Id: `mitv-ui-20261022-secure-fork-bootstrap`
- Executor: Codex
- Authorization: The user requested creation of their own GitHub fork and secure Git exclusions for keys and related private material.
- Baseline: `312c01119a327f402e68818670a6542f1e643e56`

## Scope

Create the user's fork and local checkout, preserve the upstream remote, and harden repository exclusions for Android signing material, Codex companion pairing state, environment files, ADB/SSH credentials, certificates, private configuration, and raw diagnostics.

## Boundaries

This change does not create or move real credentials, build an APK, modify television settings, adapt device behavior, publish a release, or merge a pull request. Real credentials remain outside the repository, preferably under `~/LocalSecrets/MITV-UI/`.

## Verification

Verification must confirm Git remotes, branch state, representative ignore rules, absence of tracked secret-like paths, and a clean secret-pattern scan that reports paths only.
