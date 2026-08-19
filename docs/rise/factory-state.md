---
vendor: rise
current_stage: 1
updated: 2026-08-19
---

# Rise — App Platform factory state

Cache of pipeline progress, maintained by `/app-platform-factory rise`. **Not the source
of truth** — the command derives the current stage by detecting artifacts on disk and
reconciles this file to match (detection wins on conflict). Safe to delete; it rebuilds.

| # | Stage | Status | Proof | Artifact |
|---|-------|--------|-------|----------|
| 1 | scope | ✅ | Ledger drafted + 3-lens reviewed (2 P0s found + fixed); card mockup published; signed off 2026-08-19 (Matthew Albert) | `docs/rise/BUILD-SCOPE.md` |
| 2 | plan | ⬜ | — | `docs/plans/<date>-NNN-feat-rise-*-plan.md` |
| 3 | build | ⬜ | — | `apps/rise/app/manifest.json` |
| 4 | validate | ⬜ | — | live-data / `appcfg run data-graphql` result |
| 5 | UAT | ⬜ | — | `docs/rise/<date>-rise-uat-edge-case-loop.md` |
| 6 | ship | ⬜ | — | `docs/rise/<date>-rise-launch-plan.md` |
| 7 | document | ⬜ | — | `docs/rise/rise-on-app-platform-help-center.docx` |

Status: ⬜ not started · 🔄 in progress · ✅ complete (proof recorded).

Build repo: `~/rise` · remote: not yet created (local-only; no push has occurred).

Proof surfaces (record which are met, keep separate): local · pushed · ported · merged ·
deployed · live. Ship-stage surfaces that aren't met yet are handoff items, not done.

## Sign-off decisions
Track every human decision the scope/plan stages surfaced — number them, record the answer +
date when resolved.

1. **C7** — Build against Rise.ai V2 docs. — ✅ resolved 2026-08-19
2. **C9** — Static per-merchant API key (not OAuth). Rotation/revocation ownership unassigned. — ✅ auth model resolved 2026-08-19; ownership gap carried forward, see below
3. **C8** — v1 ships per-transaction cap only; no cumulative/velocity limit yet. — ✅ resolved 2026-08-19
4. **C11** — Ledger-line loyalty treatment accepted for v1. — ✅ resolved 2026-08-19
5. **C12** — Refund action confirmed deferred, not in v1. — ✅ resolved 2026-08-19

## Remaining open items
- **Key rotation/revocation ownership unassigned** (C9) — needs an owner before go-live, not before build; carry into stage 2 (plan) so it doesn't get lost.
- Cumulative/velocity cap (C8) — named residual, not blocking v1, revisit if usage warrants it.
- Signal freshness: AG Jeans citation is ~18mo old, post-close — confirm pain point still live (optional, non-blocking).
- Broader Slack-based demand search once Slack MCP is authorized (optional, non-blocking).
- Pre-build sandbox verification (`BUILD-SCOPE.md` gate, `SCOPING.md` §7 — 12 items) — none arranged yet; needed before stage 4 (validate), and specifically before stage 3→4 for the `issue_store_credit` cap (item 4, wallet/gift-card double-count).
- Refund mechanism (C12) unresolved — revisit via sandbox before Phase 2 scoping, not a v1 concern.
