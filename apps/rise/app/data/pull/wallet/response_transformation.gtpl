{{- /* Field names (balance, loyalty_card_number) are a documented-shape assumption -- verify live at stage 4. */ -}}
{{- if not (kindIs "map" .rawData) -}}{{- stop "Rise.ai wallet response was not in the expected shape." -}}{{- end -}}
{{- $w := dict "id" (.rawData.id | toString) "balance" (.rawData.balance | toString) "currency" (.rawData.currency | default "USD" | toString) -}}
{{- if .rawData.loyalty_card_number -}}
  {{- $_ := set $w "loyaltyCardNumber" (.rawData.loyalty_card_number | toString) -}}
{{- end -}}
{{- $w | toJson -}}
