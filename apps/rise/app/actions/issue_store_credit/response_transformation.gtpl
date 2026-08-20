{{- /* Success/error field names (transactionId, newBalance) are a documented-shape assumption -- verify live at stage 4. */ -}}
{{- $code := .response.statusCode -}}
{{- if and (ge $code 200) (lt $code 300) -}}
  {{- $txnId := "" -}}
  {{- if and (kindIs "map" .rawData) .rawData.transactionId -}}{{- $txnId = .rawData.transactionId | toString -}}{{- end -}}
  {{- $newBalance := "" -}}
  {{- if and (kindIs "map" .rawData) .rawData.newBalance -}}{{- $newBalance = .rawData.newBalance | toString -}}{{- end -}}
  {{- dict "success" true "transactionId" $txnId "newBalance" $newBalance | toJson -}}
{{- else if and (ge $code 400) (lt $code 500) -}}
  {{- $msg := .response.status -}}
  {{- if kindIs "map" .rawData -}}{{- $msg = .rawData.message | default (.rawData.error | default $msg) -}}{{- end -}}
  {{- dict "success" false "message" ($msg | toString) | toJson -}}
{{- else -}}
  {{- fail (printf "unexpected response status %d from Rise.ai" $code) -}}
{{- end -}}
