<!-- INTERNAL SCOPE DOC — rise.ai only. Do not generalize to other apps. -->

# Rise.ai — App Platform Scoping Ledger

Draft v0.2 (post-review) · 2026-08-19 · Author: Claude (app-platform-scoping-ledger) for Matthew Albert

**Status: NOT locked.** §5 review surfaced two independent P0s that changed scope (refund action,
wallet/gift-card double-count). Revised below. Needs your sign-off on the decisions flagged
`needs-human` in §4/§7 before this becomes `BUILD-SCOPE.md`.

## §0 Baseline

Greenfield build — no Rise.ai App Platform app exists on main. No pre-existing actions, pulls,
forms, or auth to preserve.

**Architecture note (confirmed by Jeremy, first-party, 2026-08-19):** Rise.ai is distributed as
a Shopify app but runs as its own standalone platform with its own API (`platform.rise.ai`).
Gladly's native Shopify integration does **not** surface Rise.ai data — gift card and store
credit balances live entirely on Rise's backend. This is a fully separate vendor integration,
not an extension of the Shopify data pull. (Confirmed independently by the AG Jeans call below:
"Gladly can create discount codes within the platform, but cannot directly integrate with Rise
AI for gift cards/store credits.")

## §1 Signal mining

Slack MCP unavailable in this session (not authorized); Gong archive (`archive_search`) was
available and searched. Per the documented non-interactive-session fallback, demand is scoped
from internal call archive + web-verified competitive parity, with the citation bar carried
forward transparently rather than blocking.

**Named-merchant citations — bar mechanically cleared (≥2), but read the caveat below:**

| # | Merchant | Source | Date | Quote |
|---|---|---|---|---|
| 1 | AG Jeans (closed-won customer, 5 CX seats) | Gong call `981154465339032967`, "Gladly x AG Jeans" | 2025-02-18 | "Gladly can create discount codes within the platform, but cannot directly integrate with Rise AI for gift cards/store credits." Rep confirms merchant manages store credit "through rise" today, entirely off-platform, and asks whether a "rise and loop integration" could automate credit issuance from returns. |
| 2 | Tula (active opportunity, PNG/beauty) | Gong call `5076719016537214670`, "Tula + Gladly Intro Call" | 2025-12-02 | Next-step action item: "Steph will look into RISE.AI's open API accessibility" — evaluating Rise.ai as a data layer under Gorgias/standalone Gladly. |

**Caveat, added post-adversarial-review:** both citations are *pipeline* artifacts (Gladly's own
sales calls), not independent voice-of-customer research — neither is a merchant proactively
requesting this to Gladly's CS/product org. AG Jeans' quote is ~18 months old relative to today
and describes a workaround-era pain point from a deal that **already closed**; there's no cited
renewal/expansion signal confirming the pain is still live. Tula's citation is an SC's diligence
action item, not a customer statement of need. **Read this as "weak-but-cleared," not "strong
demand confirmed."** `needs-human`: if a fresher/independent signal is wanted before final
sign-off, someone with Slack access should search `#ama-solutions-engineering` /
`#project-critical-integrations`, and/or CS should confirm AG Jeans' pain point is still live.

**Corroborating (non-named) web signal:**

- Shopify App Store review, Calicoastal Boutique (Jul 2026): *"I can't even see how many
  outstanding credits there are"* — https://apps.shopify.com/gift-card-loyalty-program/reviews
  (weak signal — general product complaint, not Gladly-specific).
- Rise.ai's own "Compensation" solutions page markets directly at CS teams —
  https://rise.ai/solutions/compensation/ (vendor marketing, directional only).

## §2b Competitive-parity matrix (the scope anchor)

| Capability | Gorgias (official, first-party) | Zendesk | Kustomer / Freshdesk / Re:amaze | Gladly-deliverable? |
|---|---|---|---|---|
| Store credit balance display | ✅ sidebar widget | ❌ Zapier only, no card | ❌ none found | ✅ data pull |
| Gift card balance + code display | ✅ | ❌ | ❌ | ✅ data pull |
| Expiration date display | ✅ | ❌ | ❌ | ✅ data pull |
| Loyalty card number display | ✅ | ❌ | ❌ | ✅ data pull (see C11 — not a separate points balance) |
| Issue store credit (Gorgias: preset-amount buttons) | ✅ | ❌ | ❌ | ✅ action, GUARDED — **interaction design diverges from Gorgias's preset buttons, see C16** |
| Process refund (order-linked) | ✅ | ❌ | ❌ | ⛔ **moved to deferred post-review — see C12/§6** |
| Real-time wallet update after action | ✅ | ❌ | ❌ | ✅ standard action → timeline pattern |
| Referral data display | not shown in Gorgias | ❌ | ❌ | ⛔ out-of-scope — no parity precedent, no citation |

**Parity is the scope target, not a ceiling on interaction design.** Gorgias is the only genuine
helpdesk precedent for *what data/actions* to expose. It is **not** binding on *how* — Gladly's
own action-form idiom (free-text amount + cap, not preset buttons) is used instead; see C16. This
distinction was blurred in the v0.1 draft (adversarial review finding) and is now explicit.

## §2c Gladly capability pre-check

| Ask | Platform primitive check | Verdict |
|---|---|---|
| Wallet/gift-card balance card | Standard `data_schema.graphql` Data Type + `flexible.card`, matched by customer email (same idiom as Stay/Recharge) | ✅ deliverable |
| Issue store credit from customer profile | Action + agent form (compose "+" menu), GUARDED confirmation copy | ✅ deliverable, cap logic conditional on C10 (see below) |
| **Distinct "loyalty points" balance** | Rise.ai has **no separate loyalty/points endpoint** — rewards/cashback post as a `WalletAction` of type `REWARD` into the *same* store-credit wallet. | ⛔ not deliverable as a separate balance (vendor data-model reality). **Display treatment for loyalty-motivated merchants is `contested`, not settled — see C11.** |
| Refund-to-credit tied to a specific order | Rise's refund-eligibility endpoint takes a `transactionId`; no POST "execute refund" endpoint was found in docs. | 🔎 **Existence of this as a distinct action is unconfirmed — see C12. Do not assume deliverable until sandbox-verified.** |
| Admin toggle for destructive actions | Forms cannot read `integration.configuration`; toggle must be enforced in the action template itself (rivo `enableCancelMembership` pattern) | ✅ deliverable if a destructive action enters scope — none does in v1 |

## §3 Per-item API research

All facts sourced from `dev.rise.ai` (V2 docs, current as of 2026-08-19) via WebFetch by the
research agent; legacy V1 docs at `api.rise.ai` still listed alongside V2 with no deprecation
notice (see C7). Base URL: `https://platform.rise.ai/v1/`. Auth headers required on every call:
`Authorization`, `Content-Type: application/json`, `rise-account-id: <ACCOUNT_ID>`.

### 3a. Wallet balance data pull

- **User story:** Agent opens a customer profile and sees the customer's Rise.ai store-credit
  balance, linked gift card(s), expiration, and loyalty card number without leaving Gladly.
- **Endpoint:** `GET /v1/rise/wallets` — query by `wallet_id`, `email`, or
  `customer_reference_source`.
- **Response shape:** wallet object with `balance` (typed `DECIMAL_VALUE` — **string, not a
  JSON number**), currency, linked gift card summary, loyalty card number.
- **Status:** `resolved` (documented) / auth header scheme `contested` (C4) / error-on-empty
  `needs-sandbox` / **multi-wallet-per-customer possibility `needs-sandbox`** (feasibility review:
  "matched by email, same as Stay" is asserted more confidently than evidenced — confirm a
  customer can't have >1 wallet/currency, which would break the 1-card-per-customer assumption).

### 3b. Gift card lookup + balance

- **Endpoint:** `GET /v1/rise/gift-cards/{giftCardId}`, plus `POST /v1/rise/gift-cards/query`.
- **Response:** gift card object incl. code, balance (`DECIMAL_VALUE` string), status, expiry.
- **Status:** `resolved` — 404 `GIFT_CARD_NOT_FOUND` is documented (unlike wallets, this endpoint
  does have a real error path on record).

### 3c. Transaction / ledger history — **now its own chained Data Type (see C10 correction)**

- **User story:** Agent sees recent activity (including reward-type entries, C11) and, for a
  refund-shaped request, finds the specific transaction id to act on.
- **Endpoints:** `POST /v1/rise/gift-cards/transactions/query`, `POST
  /v1/rise/wallet_actions/query`, `POST /v1/rise/wallet_actions/balances`.
- **Status:** `needs-sandbox` — response shape (pagination, sort, page size) not fully rendered
  in static docs; **and, per feasibility review, whether `wallet_actions/query` actually accepts
  a `walletId`/`customerId` filter is unconfirmed** — this is the parent-child link the chaining
  in C10 depends on. If it doesn't filter server-side, the card needs client-side scoping instead.

### 3d. Issue store credit (action)

- **User story:** Agent issues a store-credit amount to resolve a support case.
- **Endpoint:** `POST /v1/rise/wallet_actions/issue_store_credit`.
- **Request body:** wallet/customer reference, `amount` (string), a `note` field (**docs bug**:
  the field literally says "Deprecated: use `note` instead" — self-referential; confirm live which
  field name is actually accepted), `idempotencyKey` (1–100 chars).
- **Behaviors:** idempotent via `idempotencyKey`; optimistic concurrency via required `revision`.
- **Status:** `resolved` (mechanics) / `needs-sandbox` (deprecated-field-name bug, success
  response shape) / **`needs-sandbox`, now a v1 launch blocker per C10 correction below** (does
  this double-book against a directly-managed gift card balance the merchant might also adjust in
  Rise's own admin UI?).
- **Outbound decimal formatting risk (feasibility review):** `request_body.gtpl` must serialize
  an agent-entered numeric form value into a string matching Rise's expected precision (`"50"` vs
  `"50.00"`) — a `String`-typed GraphQL field gives no native formatting guardrail. Flag for
  build-time attention (§4 addendum to C3).
- **Money-adjacent → GUARDED idiom, see C8 (revised).**

### 3e. Adjust gift card balance directly (`increase` / `decrease`)

- **Endpoints:** `POST /v1/rise/gift-cards/{id}/increase`, `POST
  /v1/rise/gift-cards/{transaction.giftCardId}/decrease`.
- **Status:** `needs-sandbox` — **deferred to Phase 2**, unchanged from v0.1, pending live
  confirmation of the wallet/gift-card relationship (C10).

### 3f. Refund (order-linked) — **downgraded from in-scope action to deferred, see C12**

- **Endpoint:** only a **GET eligibility check** was found — `GET
  /v1/rise/wallet_actions/refund/amounts?transactionId=`. No POST "execute refund" endpoint was
  located.
- **Status:** `needs-sandbox`, and per both the feasibility and adversarial reviews, this is
  strong enough uncertainty that it **should not be locked as its own in-scope v1 action** — see
  C12 and §6.

### 3g. Loyalty card number / rewards

- No separate loyalty endpoint exists. Loyalty card number is a field on the wallet object (3a);
  reward-type activity appears as `WalletAction` entries of type `REWARD` in transaction history
  (3c). Whether that's enough for a loyalty-motivated merchant is `contested` — see C11.

## §4 Cross-cutting decision ledger

| # | Decision | Status | Resolution + citation |
|---|---|---|---|
| C1 | HTTP methods needed | resolved | GET (wallets, gift-cards/{id}), POST (query/search/issue/increase/decrease/refund-check) — all `appcfg`-supported. |
| C2 | ID-type handling | resolved | `walletId`, `giftCardId`, `transactionId` are GUID strings — no numeric-coercion risk. |
| C3 | Monetary field typing | resolved (read path), `needs-sandbox` (write path) | `DECIMAL_VALUE` → String in GraphQL schema. Read-path wire format needs live confirmation. **Addendum (feasibility review): write path also needs confirmation** — `request_body.gtpl` must emit an agent-entered amount in Rise's exact expected string precision; naive numeric round-tripping in Go templates risks mismatches. |
| C4 | Auth header scheme | **contested** | Auth guide shows raw `Authorization: <API_KEY>`; auto-generated endpoint examples show `Bearer YOUR_API_TOKEN`. Rivo precedent: docs got this exactly backwards once already — first sandbox call must resolve this. |
| C5 | Error-mapping strategy | needs-sandbox | Gift Card endpoints document 404/428 with embedded error-code strings; Wallet/WalletAction endpoints document only 200 — assume under-documented (twice-confirmed "docs lie by omission" pattern), not error-free. |
| C6 | Per-merchant config fields | resolved | Admin form needs `riseAccountId` (→ `rise-account-id` header) alongside the API key. |
| C7 | V1 vs V2 endpoint choice | `needs-human` | Build against V2 (actively promoted) but confirm with Rise.ai (`partnerships@rise-ai.com`) before locking build. |
| C8 | Money-adjacent action guardrail | proposed, **scope narrowed post-security-review** | GUARDED idiom (typed `approve` + merchant-configured **per-transaction** cap, fail-closed). **Security review flags this is per-transaction only — no cumulative/daily cap or per-agent velocity limit is described.** Recommend the per-transaction cap ship in v1 (matches shipped precedent) but log the velocity gap as a named residual for the human sign-off, not silently absorbed. Also: **no role-based gating exists in the ledger's model** — if the App Platform action template can't check agent role/permission, name that as a platform limitation explicitly (security review) rather than leaving it implicit. |
| C9 | Auth model: static API key vs. OAuth partnership | `needs-human`, **scope expanded post-security-review** | Recommend the static-API-key path (no external partnership approval on the critical path). **But per security review, this decision cannot close on "which model" alone** — a leaked static key is total compromise of the merchant's entire Rise wallet system (reads + all writes, every customer). Human sign-off must also cover: rotation cadence, revocation procedure if a key is compromised, and whether Gladly alerts on auth failures that could indicate a silently-broken/rotated key (fail-open risk if pull failures aren't surfaced to anyone). |
| C10 | Wallet / gift card / transaction Data Type modeling | **contested — now gates v1, not just Phase 2** | v0.1 proposed two Data Types (`rise_wallet`, `rise_gift_card`) with the wallet/gift-card double-count question gating only the *deferred* 3e. **Adversarial review correctly identified this as inconsistent: `issue_store_credit` (3d, v1 in-scope) operates on the same unresolved wallet model.** If wallet balance and gift-card balance are actually the same underlying money viewed two ways, `issue_store_credit` could double-book against a gift card the merchant separately manages in Rise's own admin UI — meaning **C8's cap is meaningless until this resolves.** This is now a **pre-build gate that blocks stage 3→4 for `issue_store_credit` specifically**, not merely a Phase-2 scope-in question (see §7). Additionally, **feasibility review identified a missing third Data Type**: transaction/ledger history (3c) needs its own chained Data Type (e.g. `rise_wallet_transaction`, parent-linked to the wallet) so the agent has a rendered surface to pick a `transactionId` from — the model is three Data Types, not two. |
| C11 | Loyalty/reward display without a separate balance | **contested, not settled** (adversarial review) | v0.1 proposed folding `REWARD`-type entries into the generic transaction ledger with a label. Tula — one of only two demand citations — is specifically loyalty-motivated (PNG/beauty, evaluating Rise as "a data layer"). Whether a filtered ledger line adequately serves a loyalty-forward merchant, versus needing a points/tier-forward card treatment, is a **UX judgment call for the human**, not a closed API constraint. Recommend: ship the ledger-line treatment for v1 (it's honest to what Rise actually tracks), but flag to CS/product that if Tula or a similar merchant closes, revisit the card's visual hierarchy for loyalty emphasis. |
| C12 | Refund mechanism and whether it's a distinct action | **contested, pre-build blocker — scope decision, not just a fact-check** | v0.1 kept "Refund-to-credit" in-scope as its own action with "mechanism TBD." **Both the feasibility and adversarial reviews independently flagged this as premature scope-lock (P0):** the two possible sandbox outcomes aren't cosmetic variants — if refund resolves to "`issue_store_credit` with a transaction reference in `note`," there is no second action/template pair at all, and locking a distinct "Refund to credit" button with its own guardrail copy (as the §3b mockup shows) may need to be scrapped, not adjusted. **Moved to deferred, see §6.** Additionally, **security review requires**: whichever mechanism is confirmed, the issued/refunded amount must be validated against the value returned by `GET /wallet_actions/refund/amounts`, independent of and in addition to the flat per-transaction cap (C8) — otherwise an agent could "refund" more than a transaction was ever worth while staying under the general cap. |
| C13 | Attribution / audit trail | needs-human + build-time | Security review: neither the Gladly timeline entry nor the ledger currently specify storing the Rise `transactionId` alongside the acting agent's identity in a way that's queryable against Rise's own audit log. Without that pairing, a disputed/fraudulent issuance can't be cleanly traced end-to-end. Recommend the action's result-transform explicitly echoes `transactionId` + Gladly agent id into the timeline entry. |
| C14 | Interaction design vs. Gorgias parity | resolved | Gorgias uses preset-amount buttons for issuing credit; Gladly will use a free-text amount + cap field (matches every other GUARDED action shipped: recharge, rivo) instead. This is a **deliberate interaction-design divergence from the parity anchor**, not an oversight — parity in §2b governs *what* is exposed, not *how*. |

## §5 Independent 3-lens review — findings (ran against v0.1)

**Security lens** (fraud/abuse, caps, gating, attribution — explicitly not PII):
1. Cap is per-transaction only, no cumulative/daily/velocity limit. P1, confidence 85.
2. Refund cap isn't tied to the eligibility-check amount — blank-check risk if refund reuses
   `issue_store_credit`. **P0, confidence 80.** → folded into C12.
3. No enforcement ties the eligibility amount to the issued amount programmatically. P1, conf 75.
4. Static API key has unbounded, undifferentiated blast radius (full wallet-system compromise on
   leak) with no rotation/revocation plan described. **P0, confidence 85.** → folded into C9.
5. No rotation/revocation plan on compromise; no fail-open alerting on auth failures. P1, conf 70.
6. No described correlation between Gladly's timeline entry and Rise's own audit log
   (transactionId + acting-agent pairing). P1, confidence 70. → folded into C13.
7. Idempotency-key generation/ownership model unspecified. P2, confidence 55.
8. If 3e ships later, doc doesn't yet say it inherits C8's cap. P2, confidence 65.
9. No role-based gating described — name as platform limitation if true. P2, confidence 60.
   → folded into C8.

**Feasibility lens** (platform primitives, breaking changes, prerequisites):
1. Refund action (3f) should not stay "in-scope v1" given the mechanism is unconfirmed to exist
   at all. **P0, confidence 85.** → folded into C12, §6.
2. Missing third chained Data Type for transaction history — C10 only resolved two. **P0,
   confidence 80.** → folded into C10.
3. Chain-key existence (does `wallet_actions/query` filter by wallet/customer id?) unconfirmed.
   P1, confidence 70. → folded into 3c.
4. C3 only addressed the read-path decimal format; outbound `request_body.gtpl` formatting is a
   separate, unaddressed risk. P1, confidence 75. → folded into C3.
5. "Matched by email, same as Stay" asserted more confidently than evidenced — multi-wallet
   possibility unconfirmed. P2, confidence 60. → folded into 3a.
6. 3e/C10 deferral reasoning affirmed as correct. P3 (affirming), confidence 85.

**Adversarial lens** (premise-testing, leaky abstractions, UX tension):
1–3. Citation quality: both citations are pipeline artifacts, not voice-of-customer; AG Jeans is
   18mo-old post-close signal; Tula is an SC diligence step, not a customer ask. P1, confidence
   70–80. → folded into §1 caveat.
4. Gorgias-anchor used for scope (§2b) but §6 quietly diverges on interaction design (no preset
   buttons) without flagging it as a decision. P2, confidence 70. → folded into C14.
5. Sole reliance on Gorgias's feature shape as a ceiling, with no independent check on what a
   Gladly agent needs mid-conversation. P2, confidence 65. → noted, no scope change (no
   contradicting evidence found; logged as a watch-item for post-launch feedback).
6. C11 loyalty-merge treated as settled when it's a UX judgment call for a loyalty-motivated
   citation (Tula). P1, confidence 70. → folded into C11 (now contested).
7–8. `issue_store_credit` (v1 in-scope) shares the exact unresolved double-count risk that gates
   the *deferred* 3e — inconsistency. **P0, confidence 80.** → folded into C10 (now gates v1).
9. Refund kept in-scope with "mechanism TBD" is optimistic scope-lock. P1, confidence 60. →
   folded into C12.

**Net effect of review:** 3 independent P0s (refund scope-lock, missing transaction Data Type,
wallet/gift-card double-count gating v1) required real revisions, not just annotations — applied
above. This is exactly the failure mode the 3-lens step exists to catch before build.

## §6 Scope (revised — still pending human sign-off, not yet locked)

**In-scope (v1):**
- Wallet balance + linked gift card summary + expiration + loyalty card number — data pull, card
  (3a, 3b, 3g; parity: Gorgias)
- Transaction/ledger history as its **own chained Data Type** (`rise_wallet_transaction`,
  parent-linked to the wallet), including labeled reward-type entries — data pull (3c; supports
  C11 and any future reference-lookup need)
- Issue store credit — action, GUARDED with free-text amount + per-transaction cap (3d, C8, C14;
  parity: Gorgias; citations: AG Jeans, Tula — read with the §1 caveat) — **build proceeds on
  documented shapes per the factory's stage 3↔4 policy, but this action's cap logic is not
  considered live-verified until C10's double-count question clears in sandbox (pre-build gate,
  §7)**

**Deferred (not rejected — named reason, revisit Phase 2 or once sandboxed):**
- **Refund-to-credit** (moved here from in-scope in v0.1, per §5 P0 findings) — the POST
  mechanism was never confirmed to exist as a distinct endpoint; resolve via sandbox whether it's
  its own action or collapses into `issue_store_credit` with a transaction reference before
  scoping its guardrail design
- Direct gift-card `increase`/`decrease` as agent actions distinct from `issue_store_credit` —
  pending live confirmation they don't double-book the same wallet balance (3e, C10)
- Disable gift card — no citation, no parity precedent; if it enters a later phase, ships
  off-by-default behind an admin config toggle enforced in the action

**Out-of-scope:**
- Referral/referrer data — no Gorgias parity precedent, no merchant citation (§2b)
- A standalone "loyalty points balance" field — does not exist in Rise.ai's data model (§2c) —
  **but the display treatment for loyalty-motivated merchants remains a contested, revisit-later
  question (C11), not a closed door**

## §7 Pre-build verification gate

| # | Item | Blocks | Status |
|---|---|---|---|
| 1 | Auth header scheme: raw key vs. `Bearer` (C4) | all pulls/actions | 🔎 needs-sandbox |
| 2 | Wallet/wallet-action 200-vs-error-on-empty and undocumented error shapes (C5) | wallet data pull transform | 🔎 needs-sandbox |
| 3 | Exact `DECIMAL_VALUE` wire format, read AND write path (C3) | schema typing + `issue_store_credit` request body | 🔎 needs-sandbox |
| 4 | **Wallet vs. gift-card double-count** (C10) | **`issue_store_credit` cap validity — blocks stage 3→4 for this action, not just Phase-2 scope-in** | 🔎 needs-sandbox — **elevated from v0.1** |
| 5 | `wallet_actions/query` server-side filter by wallet/customer id (3c, C10) | transaction Data Type chaining | 🔎 needs-sandbox |
| 6 | Multi-wallet-per-customer possibility (3a) | 1-card-per-customer assumption | 🔎 needs-sandbox |
| 7 | Refund mechanism — distinct endpoint, or `issue_store_credit` reuse (C12) | whether refund re-enters scope at all, and its guardrail design | 🔎 needs-sandbox — **not a v1 blocker any more since it's deferred, but must clear before Phase 2 scoping** |
| 8 | V1 vs. V2 endpoint confirmation (C7) | which base docs to build against | 🙋 needs-human (Rise.ai partnerships contact, or proceed on V2 by default and flag) |
| 9 | Static API key vs. OAuth, **plus rotation/revocation/alerting plan** (C9) | admin form auth idiom | 🙋 needs-human sign-off — **scope expanded from v0.1, not just a model choice** |
| 10 | Cumulative/velocity cap alongside per-transaction cap (C8) | whether v1 ships with only a per-transaction guardrail | 🙋 needs-human — named residual, not silently absorbed |
| 11 | Loyalty display treatment for loyalty-motivated merchants (C11) | whether v1's ledger-line treatment is sufficient | 🙋 needs-human, non-blocking for v1 build, revisit if Tula closes |
| 12 | Citation freshness — is AG Jeans' pain point still live post-close? Broader Slack signal pass | none — bar already mechanically cleared, but affects confidence | 🙋 needs-human, optional, not blocking |

No sandbox access has been arranged yet. Items 1–6 must clear before `issue_store_credit` can be
considered validated (stage 4), even though stage 3 (build) can proceed on documented shapes per
the factory's stage 3↔4 boundary policy.

## Sign-off

**Not locked.** Decisions requiring your explicit call before this becomes `BUILD-SCOPE.md`:
- C7 — build against Rise.ai V2 docs (recommended) or confirm with Rise.ai partnerships first?
- C9 — static API key (recommended) vs. OAuth partnership, and who owns rotation/revocation?
- C8 — ship v1 with only a per-transaction cap, or require a cumulative/velocity cap before launch?
- C11 — accept the ledger-line loyalty treatment for v1, or hold for a loyalty-forward card design?
- C12 — confirmed: refund moves to deferred, not in v1. (Recommend accepting this — flag if not.)

Reply with your calls on the above (or "proceed on your recommendations") and I'll produce the
locked `BUILD-SCOPE.md` + refresh `factory-state.md` to advance to stage 2 (plan).
