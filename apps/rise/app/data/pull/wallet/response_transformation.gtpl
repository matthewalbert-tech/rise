{{- /* Field names (balance, loyalty_card_number, gift_card, expires_at) are a documented-shape assumption -- verify live at stage 4. */ -}}
{{- if not (kindIs "map" .rawData) -}}{{- stop "Rise.ai wallet response was not in the expected shape." -}}{{- end -}}
{{- $w := dict "id" (.rawData.id | toString) "balance" (.rawData.balance | toString) "currency" (.rawData.currency | default "USD" | toString) -}}
{{- if .rawData.loyalty_card_number -}}
  {{- $_ := set $w "loyaltyCardNumber" (.rawData.loyalty_card_number | toString) -}}
{{- end -}}
{{- if kindIs "map" .rawData.gift_card -}}
  {{- $gc := dict "id" (.rawData.gift_card.id | toString) "code" (.rawData.gift_card.code | toString) "balance" (.rawData.gift_card.balance | toString) "status" (.rawData.gift_card.status | default "active" | toString) -}}
  {{- if .rawData.gift_card.expires_at -}}{{- $_ := set $gc "expiresAt" (.rawData.gift_card.expires_at | toString) -}}{{- end -}}
  {{- $_ := set $w "giftCard" $gc -}}
{{- end -}}
{{- $w | toJson -}}
