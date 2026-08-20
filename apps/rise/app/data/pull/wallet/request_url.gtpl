{{- /* Live-confirmed 2026-08-20: the "query" param uses dot-notation keys (query.email=...), not a bare "email" param or a JSON blob. */ -}}
{{- $email := "" -}}
{{- if .customer.primaryEmailAddress -}}{{- $email = .customer.primaryEmailAddress | toString | trim -}}{{- end -}}
{{- if eq $email "" -}}{{- stop "Customer has no email address to match a Rise.ai wallet." -}}{{- end -}}
{{- printf "https://platform.rise.ai/v1/rise/wallets?query.email=%s" (urlquery $email) -}}
