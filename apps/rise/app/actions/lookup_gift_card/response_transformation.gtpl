{{- /* Field names (giftCards[], code, balance, initialValue, currency, expirationDate, disableDate) live-confirmed 2026-08-20 against dev.rise.ai's "Query Gift Cards" doc. No explicit "status" field exists -- derive active/disabled from disableDate presence on display. */ -}}
{{- $code := .response.statusCode -}}
{{- if and (ge $code 200) (lt $code 300) -}}
  {{- $cards := list -}}
  {{- if and (kindIs "map" .rawData) .rawData.giftCards -}}{{- $cards = .rawData.giftCards -}}{{- end -}}
  {{- if gt (len $cards) 0 -}}
    {{- $gc := index $cards 0 -}}
    {{- $result := dict "success" true "found" true "code" ($gc.code | toString) "balance" ($gc.balance | toString) "initialValue" ($gc.initialValue | toString) "currency" ($gc.currency | toString) -}}
    {{- if $gc.expirationDate -}}{{- $_ := set $result "expirationDate" ($gc.expirationDate | toString) -}}{{- end -}}
    {{- if $gc.disableDate -}}{{- $_ := set $result "disableDate" ($gc.disableDate | toString) -}}{{- end -}}
    {{- $result | toJson -}}
  {{- else -}}
    {{- dict "success" true "found" false "message" "No gift card found with that code." | toJson -}}
  {{- end -}}
{{- else if and (ge $code 400) (lt $code 500) -}}
  {{- $msg := .response.status -}}
  {{- if kindIs "map" .rawData -}}{{- $msg = .rawData.message | default $msg -}}{{- end -}}
  {{- dict "success" false "message" ($msg | toString) | toJson -}}
{{- else -}}
  {{- fail (printf "unexpected response status %d from Rise.ai" $code) -}}
{{- end -}}
