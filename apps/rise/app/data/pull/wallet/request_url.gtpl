{{- $email := "" -}}
{{- if .customer.primaryEmailAddress -}}{{- $email = .customer.primaryEmailAddress | toString | trim -}}{{- end -}}
{{- if eq $email "" -}}{{- stop "Customer has no email address to match a Rise.ai wallet." -}}{{- end -}}
{{- printf "https://platform.rise.ai/v1/rise/wallets?email=%s" (urlquery $email) -}}
