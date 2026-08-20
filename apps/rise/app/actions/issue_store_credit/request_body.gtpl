{{- /* idempotencyKey is the platform-provided correlationId, not an agent-visible field (KTD9, revised) -- Gladly assigns one per action invocation, so no random-value generation, and no untestable form field. */ -}}
{{- $body := dict "walletId" .inputs.walletId "amount" (.inputs.amount | toString | trim) "idempotencyKey" .correlationId -}}
{{- if .inputs.note -}}{{- $_ := set $body "note" (.inputs.note | toString) -}}{{- end -}}
{{- $body | toJson -}}
