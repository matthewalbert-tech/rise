<!-- INTERNAL SCOPE DOC — rise.ai only. Do not generalize to other apps. -->

# Rise.ai — Locked Build Scope

Locked 2026-08-19 · Signed off by Matthew Albert ("proceed on your recommendations") · Full
reasoning, citations, decision ledger, and 3-lens review: [`SCOPING.md`](./SCOPING.md).

## In-scope (v1)

### Data Types

```graphql
type RiseWallet @dataType(name: "rise_wallet", version: "1.0") {
  id: String!
  balance: String!                    # DECIMAL_VALUE wire format — confirm exact precision live (§7 item 3)
  currency: String!
  loyaltyCardNumber: String
  giftCard: RiseGiftCardSummary
  transactions: [RiseWalletTransaction] @parentId(template: "{{.id}}")
}

type RiseGiftCardSummary {
  id: String!
  code: String!
  balance: String!
  expiresAt: String
  status: String!
}

type RiseWalletTransaction @dataType(name: "rise_wallet_transaction", version: "1.0") {
  id: String!
  type: String!                       # ISSUE | REWARD | REDEEM | etc. — enumerate live, see C11
  amount: String!
  note: String
  createdAt: String!
}
```

- `rise_wallet` pull: `GET /v1/rise/wallets` matched by customer email (3a).
- `rise_wallet_transaction` pull: chained detail pull off `rise_wallet`, `POST
  /v1/rise/wallet_actions/query` (or `gift-cards/transactions/query`) filtered to the parent
  wallet — **filter-by-parent-id support is `needs-sandbox`, pre-build gate item 5.**

### Actions

```graphql
input IssueStoreCreditInput {
  walletId: String!
  amount: String!                     # free-text + cap, NOT Gorgias's preset buttons (C14)
  note: String                        # NB: docs bug — field name may not be "note" live, confirm (§3d)
  idempotencyKey: String!
}

type IssueStoreCreditResult {
  success: Boolean!
  transactionId: String               # must be echoed to the Gladly timeline for audit correlation (C13)
  newBalance: String
  message: String
}

type Mutation {
  issueStoreCredit(input: IssueStoreCreditInput!): IssueStoreCreditResult
    @action(name: "issue_store_credit")
}
```

- `POST /v1/rise/wallet_actions/issue_store_credit`.
- **GUARDED idiom required:** typed `approve` in `confirmationCopy` + merchant-configured
  per-transaction cap read from `.integration.configuration`, fail-closed (`stop`) if unset or
  exceeded (C8).
- **Not validated as live-safe until pre-build gate item 4 clears** (wallet vs. gift-card
  double-count, C10) — build against this documented shape is fine per the factory's stage 3↔4
  policy, but do not treat the cap as trustworthy until sandbox confirms wallet and gift-card
  balances aren't the same money counted twice.

### Card

- One `flexible.card` on `rise_wallet` rendering: store-credit balance, linked gift card
  code/balance/expiration, loyalty card number, and an expandable transaction/ledger panel
  (reward-type entries labeled). See the mockup:
  https://claude.ai/code/artifact/5e5f09be-87be-4560-93c3-5915f95a7e3a
- "Issue store credit" as a compose "+" menu action bound to `issueStoreCredit`.

## Deferred (not rejected — named reason)

| Item | Missing capability / reason |
|---|---|
| Refund-to-credit | No confirmed POST "execute refund" endpoint exists in Rise.ai's docs — only a GET eligibility check. Resolve via sandbox whether it's a distinct action or `issue_store_credit` reuse with a transaction reference before it re-enters scope (SCOPING.md C12). |
| Direct gift-card `increase`/`decrease` | Pending live confirmation it doesn't double-book against the same wallet balance `issue_store_credit` writes to (SCOPING.md C10, 3e). |
| Disable gift card | No merchant citation, no competitive-parity precedent. If scoped later, ships off-by-default behind an admin config toggle enforced in the action template. |
| Cumulative/velocity cap alongside the per-transaction cap | Named residual from sign-off (C8) — v1 ships per-transaction only. |

## Out-of-scope

| Item | Reason |
|---|---|
| Referral/referrer data | No Gorgias parity precedent, no merchant citation (SCOPING.md §2b). |
| Standalone "loyalty points" balance/currency | Does not exist in Rise.ai's data model — rewards post as labeled transactions in the same wallet ledger (SCOPING.md C11). Display treatment revisit-able, not this door. |

## Pre-build verification gate (must clear before stage 4 / validate; item 4 blocks live-use of `issueStoreCredit` specifically)

See [`SCOPING.md` §7](./SCOPING.md#7-pre-build-verification-gate) for the full table (12 items).
Highest-priority before any sandbox work begins:

1. Auth header scheme — raw key vs. `Bearer` (contested, docs disagree with themselves).
2. Wallet vs. gift-card double-count — **blocks `issueStoreCredit` going live**, not just Phase 2.
3. `DECIMAL_VALUE` wire format, both read and write path.
4. `wallet_actions/query` parent-id filter support (needed for the transaction Data Type chain).

No sandbox access has been arranged yet. Auth model is a static per-merchant API key (`apiKey` +
`riseAccountId`) — **rotation/revocation ownership is unassigned**, carry forward as an open
item into stage 2 (plan) rather than losing it.

## Sign-off

Locked 2026-08-19 — Matthew Albert. Full decision-by-decision resolution in `SCOPING.md`'s
Sign-off section.
