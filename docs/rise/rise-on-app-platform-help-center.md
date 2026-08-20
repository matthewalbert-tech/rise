# Rise.ai on App Platform

*Updated on August 20, 2026 · 5 minute read*

## Overview

**Latest App version:** v1.0.0

Rise.ai is a gift card and store credit platform. The Rise.ai App Platform integration brings a Customer's Rise.ai wallet and gift card information directly into Gladly, so Team members don't have to open a separate Rise.ai admin tab to answer a store-credit question.

With the integration installed, Team members can see a Customer's Rise.ai wallet balance, gift card code, and recent transaction history right on the Customer Profile, and can run two approved actions — issuing store credit and looking up a gift card by code — without leaving the Conversation.

## Key benefits

**Answer store credit questions faster.** The Rise.ai Wallet Card shows a Customer's current balance and gift card code on the Customer Profile, so Team members don't need to search Rise.ai's own admin panel mid-conversation.

**Resolve requests without switching tools.** When a refund or goodwill credit is warranted, Team members can issue store credit directly from the Customer Profile's + menu.

**Track down gift cards that aren't linked to an account.** Not every Rise.ai gift card is attached to a wallet. The Look Up Gift Card action finds a gift card by its exact code, independent of whether it's tied to a Customer's wallet.

## Supported features

The Rise.ai App Platform integration is available across multiple areas of Gladly:

- **Gladly AI** — The wallet data pull is available in Guides; Guides can retrieve a Customer's store credit balance during a Conversation to help answer questions like "How much store credit do I have?"
- **Gladly Team** — One Rise.ai Wallet Card displays in Customer Details, showing the Customer's store credit balance, gift card code (when the wallet has one), and a transaction history drawer.
- **Agent actions** — The Issue Store Credit and Look Up Gift Card action forms register in the Customer Profile's + menu. Results are recorded in the Conversation timeline.

## Details

### What data can you access

**Customer Profile matching**
- Primary email address — the app searches Rise.ai using the Customer Profile's primary email address.

**Wallet** (shown on the Rise.ai Wallet Card)
- Store credit balance
- Currency
- Gift card code (when the wallet has a linked gift card)

**Transaction** (shown in the Card's transaction history drawer, when the wallet has activity)
- Type (e.g. issue, redeem, reward)
- Amount
- Note (when available)
- Date

### Supported actions

**Issue Store Credit** — Inputs: wallet ID, amount, an optional note, and a typed confirmation ("approve"). Valid whenever a Rise.ai wallet is available for the Customer. Issues store credit to the Customer's wallet up to the merchant-configured per-transaction cap; amounts above the cap are rejected. The result includes the Rise.ai transaction ID and the wallet's new balance.

**Look Up Gift Card** — Input: a gift card code. Valid for any Customer, since it doesn't depend on the Customer's own wallet. Looks up a gift card by its exact code and returns its balance, initial value, currency, and expiration or disable date when set.

### How does customer matching work?

- **Primary email address** — the app searches Rise.ai using the Customer Profile's primary email address.
- **No email address** — if the Customer Profile has no primary email address on file, no request is made and the Card does not show wallet data.
- **Incorrect email address** — if the Customer Profile has someone else's email address, the Card may show that person's Rise.ai wallet data instead.

## Key use cases

### 1. Answering a store credit balance question

**Use case:** A Customer asks how much store credit they have left.

**How it works:**
1. The Team member opens the Customer Profile.
2. The Rise.ai Wallet Card shows the current balance without any lookup.
3. The Team member relays the balance to the Customer.

**Business impact:** Faster resolution, no tab-switching to Rise.ai's own admin.

### 2. Issuing a goodwill store credit

**Use case:** A Customer's order arrived damaged, and the Team member wants to issue store credit as a goodwill gesture.

**How it works:**
1. The Team member opens the + menu on the Customer Profile and selects Issue Store Credit.
2. They enter the wallet ID, the amount, an optional note, and type "approve" to confirm.
3. Gladly submits the request to Rise.ai and records the transaction ID and new balance in the Conversation timeline.

**Business impact:** Store credit gets issued in the same tool the Team member is already using, with a built-in confirmation step and per-transaction cap to prevent mistakes.

### 3. Finding a gift card that isn't tied to a wallet

**Use case:** A Customer has a gift card code but no Rise.ai wallet, so it doesn't show up automatically on the Customer Profile.

**How it works:**
1. The Team member opens the + menu and selects Look Up Gift Card.
2. They enter the gift card code exactly as the Customer provided it.
3. Gladly returns the gift card's balance, initial value, and expiration or disable date if set.

**Business impact:** Team members can help Customers with standalone gift cards without needing separate access to Rise.ai's admin.

## Additional resources

Users assigned the Team Manager or Administrator roles can contact Gladly Support. Log into Gladly as one of these roles, click the hamburger menu on the top-left corner, then click Contact Gladly Support. Gladly Support's primary operating hours are Monday through Friday, 6 AM to 6 PM Pacific Time, but you can submit a request anytime.
