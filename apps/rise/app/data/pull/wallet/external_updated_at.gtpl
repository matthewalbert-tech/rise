{{- /* Rise.ai's wallet endpoint documents no update timestamp -- empty output lets the platform fall back to pull time. */ -}}
{{- if .updatedAt -}}{{- .updatedAt -}}{{- end -}}
