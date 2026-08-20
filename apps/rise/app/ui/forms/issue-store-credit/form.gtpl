{{- $wallet := .data.wallet -}}
{
  "title": "Issue Store Credit",
  "submitButton": "Issue Store Credit",
  "sections": [
    {
      "type": "input",
      "label": "Wallet",
      "attr": "walletId",
      "input": {
        "type": "select",
        "options": [
          {{- if kindIs "map" $wallet }}
          {
            "text": {{ printf "Store credit wallet (balance $%s)" $wallet.balance | toJson }},
            "value": {{ $wallet.id | toJson }}
          }
          {{- else }}
          {
            "text": "No Rise.ai wallet found for this customer",
            "value": ""
          }
          {{- end }}
        ]
      }
    },
    {
      "type": "input",
      "label": "Amount",
      "attr": "amount",
      "input": {
        "type": "text",
        "placeholder": "12.50"
      },
      "hint": "Enter the amount in dollars and cents, e.g. 12.50."
    },
    {
      "type": "input",
      "label": "Note",
      "attr": "note",
      "input": {
        "type": "text",
        "placeholder": "Reason for this credit",
        "optional": true
      }
    },
    {
      "type": "input",
      "label": "Confirm",
      "attr": "confirm",
      "input": {
        "type": "text",
        "placeholder": "approve"
      },
      "hint": "Type approve to confirm this store credit issuance."
    }
  ]
}
