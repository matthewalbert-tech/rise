---
vendor: rise
current_stage: 3
updated: 2026-08-20
---

# Rise — App Platform factory state

Cache of pipeline progress, maintained by `/app-platform-factory rise`. **Not the source
of truth** — the command derives the current stage by detecting artifacts on disk and
reconciles this file to match (detection wins on conflict). Safe to delete; it rebuilds.

| # | Stage | Status | Proof | Artifact |
|---|-------|--------|-------|----------|
| 1 | scope | ✅ | Ledger drafted + 3-lens reviewed (2 P0s found + fixed); card mockup published; signed off 2026-08-19 (Matthew Albert) | `docs/rise/BUILD-SCOPE.md` |
| 2 | plan | ✅ | Implementation-ready plan, doc-reviewed (5 findings, 7 auto-fixed) | `docs/plans/2026-08-19-001-feat-rise-wallet-app-plan.md` |
| 3 | build | 🔄 | U1-U8 built and green (`make all` passing); one critical fix (int64-overflow cap bypass, code review) and two same-day architecture corrections (gift card moved to a lookup action, KTD11; then partially restored onto the card once a real wallet proved it embeds gift card info, KTD12) applied mid-build | `apps/rise/app/manifest.json` |
| 4 | validate | 🔄 | Auth scheme, wallet query format, 404 shape, wallet response envelope, and gift-card-in-wallet shape all confirmed live (2026-08-20) against a real test wallet ($10, matthew.albert@gladly.com). `balance` precision beyond whole-dollar and `loyaltyCardNumber`'s source remain unconfirmed | live-data / `appcfg run data-graphql` result |
| 5 | UAT | ⬜ | — | `docs/rise/<date>-rise-uat-edge-case-loop.md` |
| 6 | ship | ⬜ | — | `docs/rise/<date>-rise-launch-plan.md` |
| 7 | document | ⬜ | — | `docs/rise/rise-on-app-platform-help-center.docx` |

Status: ⬜ not started · 🔄 in progress · ✅ complete (proof recorded).

Build repo: `~/rise` · remote: `matthewalbert-tech/rise` (private) · branch `feat/rise-wallet-app`.

Proof surfaces (record which are met, keep separate): local (✅, `make all` green) · pushed (✅) ·
ported · merged · deployed · live. Ship-stage surfaces that aren't met yet are handoff items, not
done.

## Sign-off decisions
Track every human decision the scope/plan stages surfaced — number them, record the answer +
date when resolved.

1. **C7** — Build against Rise.ai V2 docs. — ✅ resolved 2026-08-19
2. **C9** — Static per-merchant API key (not OAuth). Rotation/revocation ownership unassigned. — ✅ auth model resolved 2026-08-19; ownership gap carried forward, see below
3. **C8** — v1 ships per-transaction cap only; no cumulative/velocity limit yet. — ✅ resolved 2026-08-19
4. **C11** — Ledger-line loyalty treatment accepted for v1. — ✅ resolved 2026-08-19
5. **C12** — Refund action confirmed deferred, not in v1. — ✅ resolved 2026-08-19
6. **KTD11** — Gift card moved from a nested wallet field to an agent-invoked lookup action (`lookupGiftCard`) after live testing found no confirmed email-based wallet↔gift-card link. Same data, different surface. — ✅ resolved 2026-08-20 (approved live, mid-build)
7. **KTD12** — Partial reversal of KTD11 same day: a real wallet (customer with an actual store-credit balance, not just a bare gift card) embeds its gift card in the `GET /v1/rise/wallets` response. `RiseWallet.giftCardCode` restored on U4's card; U8's lookup action kept for bare-gift-card customers. Both mechanisms are real, for different customer states. — ✅ resolved 2026-08-20

## Remaining open items
- **Key rotation/revocation ownership unassigned** (C9) — needs an owner before go-live, not before build.
- Cumulative/velocity cap (C8) — named residual, not blocking v1, revisit if usage warrants it.
- Signal freshness: AG Jeans citation is ~18mo old, post-close — confirm pain point still live (optional, non-blocking).
- **Wallet success-body shape mostly confirmed** (2026-08-20) — real shape seen: `{"wallet": {id, revision, giftCardId, customerReferences, createdDate, updatedDate, giftCardInfo: {code, balance, currency, codeSuffix}, primaryEmail, ...}}`. Still open: exact `balance` decimal precision beyond a whole-dollar example (`"10"`), and whether `loyaltyCardNumber` exists anywhere in this object (absent from every live response so far — may not exist as a wallet-level concept at all).
- Refund mechanism (C12) unresolved — revisit via sandbox before Phase 2 scoping, not a v1 concern.
- `wallet_actions/query` parent-id filter support (needed for U3's transaction chaining) — not yet live-verified.
- Rise.ai's `POST /v1/rise/wallets/query_by_contact` endpoint (which returns wallets with embedded gift card info) has a broken/non-representative docs example — four filter-shape guesses were rejected as `UNSUPPORTED_FILTER`. If a future phase wants the fully-automatic unified card back, this needs a real example from Rise.ai support, not further guessing.
