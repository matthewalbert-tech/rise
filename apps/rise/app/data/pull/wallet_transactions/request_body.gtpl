{{- /* Chained on RiseWallet.id (KTD1) -- filters by the parent wallet, not a gift-card id. */ -}}
{{- $wallet := index .externalData.rise_wallet 0 -}}
{{- dict "walletId" $wallet.id | toJson -}}
