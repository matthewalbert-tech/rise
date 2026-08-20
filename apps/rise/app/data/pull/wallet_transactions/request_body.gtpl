{{- /* Chained on RiseWallet.id (KTD1) -- filters by the parent wallet, not a gift-card id. */ -}}
{{- if not .externalData.rise_wallet -}}
  {{- stop "No parent wallet available to scope the transaction query." -}}
{{- end -}}
{{- $wallet := index .externalData.rise_wallet 0 -}}
{{- dict "walletId" $wallet.id | toJson -}}
