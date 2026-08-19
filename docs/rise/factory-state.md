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
| 1 | scope | 🔄 | Ledger drafted + 3-lens reviewed (2 P0s found + fixed); card mockup published; awaiting human sign-off on C7/C8/C9/C11/C12 | `docs/rise/SCOPING.md` |
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

1. **C7** — Build against Rise.ai V2 docs, or confirm with Rise.ai partnerships first? — *open*
2. **C9** — Static API key vs. OAuth partnership; who owns key rotation/revocation? — *open*
3. **C8** — Ship v1 with only a per-transaction cap, or require a cumulative/velocity cap too? — *open*
4. **C11** — Accept ledger-line loyalty treatment for v1, or hold for a loyalty-forward card? — *open*
5. **C12** — Refund action moved to deferred (not in v1) — recommend accepting. — *open*

## Remaining open items
- Signal freshness: AG Jeans citation is ~18mo old, post-close — confirm pain point still live (optional, non-blocking).
- Broader Slack-based demand search once Slack MCP is authorized (optional, non-blocking).
- Pre-build sandbox verification (§7 items 1–7 in SCOPING.md) — none arranged yet; needed before stage 4 (validate), and specifically before stage 3→4 for the `issue_store_credit` cap (item 4, wallet/gift-card double-count).
- V1/V2 docs ambiguity and OAuth-vs-key auth model must resolve before build starts in earnest.
