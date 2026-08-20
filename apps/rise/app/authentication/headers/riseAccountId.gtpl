{{- $id := "" -}}
{{- if .integration.configuration.riseAccountId -}}{{- $id = .integration.configuration.riseAccountId | toString | trim -}}{{- end -}}
{{- if eq $id "" -}}{{- stop "Rise.ai account ID is required. Enter it in the app configuration." -}}{{- end -}}
{{- $id -}}
