{{- /* Envelope confirmed live 2026-08-20: response is {"wallet": {...}}, not a bare object.
A wallet's balance/currency/code come from the embedded giftCardInfo, not top-level fields --
defensively check for a top-level balance first in case a wallet with no gift card ever
surfaces one directly, per the vendor's own schema mismatch pattern seen elsewhere. Loyalty
card number dropped per user decision 2026-08-20. */ -}}
{{- if not (and (kindIs "map" .rawData) (kindIs "map" .rawData.wallet)) -}}
  {{- stop "Rise.ai wallet response was not in the expected shape." -}}
{{- end -}}
{{- $w := .rawData.wallet -}}
{{- $out := dict "id" ($w.id | toString) -}}
{{- if $w.balance -}}
  {{- $_ := set $out "balance" ($w.balance | toString) -}}
  {{- $_ := set $out "currency" ($w.currency | default "USD" | toString) -}}
{{- else if kindIs "map" $w.giftCardInfo -}}
  {{- $_ := set $out "balance" ($w.giftCardInfo.balance | toString) -}}
  {{- $_ := set $out "currency" ($w.giftCardInfo.currency | default "USD" | toString) -}}
  {{- if $w.giftCardInfo.code -}}{{- $_ := set $out "giftCardCode" ($w.giftCardInfo.code | toString) -}}{{- end -}}
{{- else -}}
  {{- $_ := set $out "balance" "0.00" -}}
  {{- $_ := set $out "currency" "USD" -}}
{{- end -}}
{{- $out | toJson -}}
