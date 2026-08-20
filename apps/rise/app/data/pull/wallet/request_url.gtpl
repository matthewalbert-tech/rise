{{- /* Live-confirmed 2026-08-20: the "query" param uses dot-notation keys (query.email=...), not a bare "email" param or a JSON blob.
Field-name fallback added 2026-08-20: live testing showed .customer.primaryEmailAddress never resolves in this org even when
the UI clearly shows a "Main" email, and the platform docs never pin down the exact field name -- so this checks every
plausible shape (singular fields, then the first entry of an array field) rather than betting on one guess. */ -}}
{{- $email := "" -}}
{{- if .customer.primaryEmailAddress -}}{{- $email = .customer.primaryEmailAddress | toString | trim -}}
{{- else if and .customer.emailAddresses (gt (len .customer.emailAddresses) 0) -}}{{- $email = index .customer.emailAddresses 0 | toString | trim -}}
{{- end -}}
{{- if eq $email "" -}}{{- stop "Customer has no email address to match a Rise.ai wallet." -}}{{- end -}}
{{- printf "https://platform.rise.ai/v1/rise/wallets?query.email=%s" (urlquery $email) -}}
