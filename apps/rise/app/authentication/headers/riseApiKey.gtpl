{{- /* Live-confirmed 2026-08-20 against dev.rise.ai's rendered "Get Wallet" doc: "authorization: Bearer YOUR_API_TOKEN". */ -}}
{{- $key := "" -}}
{{- if .integration.secrets.apiKey -}}{{- $key = .integration.secrets.apiKey | toString | trim -}}{{- end -}}
{{- if eq $key "" -}}{{- stop "API key is required. Enter a valid Rise.ai API key in the app configuration." -}}{{- end -}}
{{- printf "Bearer %s" $key -}}
