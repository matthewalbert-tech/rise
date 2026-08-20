{{- if not .inputs.code -}}
  {{- stop "Enter a gift card code to look up." -}}
{{- end -}}
{{- "https://platform.rise.ai/v1/rise/gift-cards/query" -}}
