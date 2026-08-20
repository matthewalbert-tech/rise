{{- /* Envelope shape (bare array vs {"data": [...]}) is an assumption -- verify live at stage 4. */ -}}
{{- $wallet := index .externalData.rise_wallet 0 -}}
{{- $rows := .rawData -}}
{{- if and (kindIs "map" .rawData) .rawData.data -}}{{- $rows = .rawData.data -}}{{- end -}}
{{- if not (kindIs "slice" $rows) -}}{{- $rows = list -}}{{- end -}}
{{- $out := list -}}
{{- range $rows -}}
  {{- $note := "" -}}
  {{- if .note -}}{{- $note = .note | toString -}}{{- end -}}
  {{- $row := dict "id" (.id | toString) "type" (.type | toString) "amount" (.amount | toString) "createdAt" (.createdAt | toString) "walletId" $wallet.id -}}
  {{- if ne $note "" -}}{{- $_ := set $row "note" $note -}}{{- end -}}
  {{- $out = append $out $row -}}
{{- end -}}
{{- $out | toJson -}}
