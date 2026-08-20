{{- /* GUARDED idiom (docs/rise/BUILD-SCOPE.md C8): typed confirmation + merchant cap, fail-closed. */ -}}
{{- if not .inputs.walletId -}}
  {{- stop "No Rise.ai wallet is available for this customer." -}}
{{- end -}}
{{- $confirm := "" -}}
{{- if .inputs.confirm -}}{{- $confirm = .inputs.confirm | toString | trim -}}{{- end -}}
{{- if ne $confirm "approve" -}}
  {{- stop "Type approve to confirm this store credit issuance." -}}
{{- end -}}
{{- $amount := "" -}}
{{- if .inputs.amount -}}{{- $amount = .inputs.amount | toString | trim -}}{{- end -}}
{{- if not (regexMatch "^(0|[1-9][0-9]*)\\.[0-9]{2}$" $amount) -}}
  {{- stop "Enter a valid amount with exactly two decimal places, e.g. 12.50." -}}
{{- end -}}
{{- if eq $amount "0.00" -}}
  {{- stop "Amount must be greater than zero." -}}
{{- end -}}
{{- $cap := "" -}}
{{- if .integration.configuration.storeCreditCap -}}{{- $cap = .integration.configuration.storeCreditCap | toString | trim -}}{{- end -}}
{{- if eq $cap "" -}}
  {{- stop "Store credit issuance is not enabled. Configure a per-transaction cap in the app settings to allow this action." -}}
{{- end -}}
{{- if not (regexMatch "^(0|[1-9][0-9]*)\\.[0-9]{2}$" $cap) -}}
  {{- stop "The configured store credit cap is invalid. Contact your Gladly admin." -}}
{{- end -}}
{{- $amountCents := int64 (replace "." "" $amount) -}}
{{- $capCents := int64 (replace "." "" $cap) -}}
{{- if gt $amountCents $capCents -}}
  {{- stop (printf "Amount exceeds the per-transaction cap of %s." $cap) -}}
{{- end -}}
{{- "https://platform.rise.ai/v1/rise/wallet_actions/issue_store_credit" -}}
