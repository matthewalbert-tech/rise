{{- $key := "" -}}
{{- if .integration.secrets.apiKey -}}{{- $key = .integration.secrets.apiKey | toString | trim -}}{{- end -}}
{{- if eq $key "" -}}{{- stop "API key is required. Enter a valid Rise.ai API key in the app configuration." -}}{{- end -}}
{{- $key -}}
