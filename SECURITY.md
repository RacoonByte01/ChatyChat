# Security Policy

## Reporting a Vulnerability

Please report security vulnerabilities through the **private** [GitHub Security Advisory](https://github.com/RacoonByte01/ChatyChat/security/advisories/new) for this repository. Do not open a public issue.

Include in the report:

- Affected version
- Steps to reproduce (minimal example)
- Impact assessment if known
- Optional proof of concept

You will receive an acknowledgement within 48 hours, and we will work to confirm, reproduce, and fix the issue before public disclosure.

## Supported Versions

Only the latest release is supported. Older versions will not receive security fixes; update to the current release.

| Version | Supported          |
| ------- | ------------------ |
| 1.0.0   | :white_check_mark: |
| < 1.0.0 | :x:                |

## Responsible Disclosure

Security issues are disclosed publicly only after a fix is released, allowing affected users time to upgrade. We ask that you do not share details publicly before then.

## Current Security Posture

ChatyChat is a self-hosted service; hardening is the operator's responsibility. Known limitations of the current implementation:

- **No TLS by default.** Run the app behind an HTTPS reverse proxy (Caddy, nginx, ...). Without HTTPS, credentials and tokens travel in plaintext.
- **Passwords** are stored as salted-less SHA-512 hashes (`Hash.get_SHA512`). A password hashing scheme such as bcrypt or argon2 is planned.
- **Tokens** are 32 random bytes (Base64url) with **no expiration**, stored in plaintext in `data/users.json`. Revoke them explicitly with `/logout` or `/token`.
- **Authentication failures** (missing/unknown user or insufficient permissions) return HTTP 200 with a `null`/empty body instead of `403`, so responses do not distinguish "not found" from "not allowed".
- **Flat-file persistence** (`data/*.json`) without locking — no protection against concurrent writes.
- **Message validation** only checks for a PGP armor marker; message encryption is the client's responsibility (the server never encrypts).
- **No rate limiting** on `/login` — exposed to brute force.
- **Orphans on deletion:** deleting a group or leaving it does not remove `data/groups/<id>.json`; the last member can leave, orphaning the group.
