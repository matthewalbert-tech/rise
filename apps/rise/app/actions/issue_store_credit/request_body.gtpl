{{- $body := dict "walletId" .inputs.walletId "amount" (.inputs.amount | toString | trim) "idempotencyKey" .inputs.idempotencyKey -}}
{{- if .inputs.note -}}{{- $_ := set $body "note" (.inputs.note | toString) -}}{{- end -}}
{{- $body | toJson -}}
