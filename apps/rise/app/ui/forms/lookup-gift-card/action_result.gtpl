{{- if not .action.result.success -}}
{
  "errors": [
    { "attr": "code", "detail": {{ .action.result.message | default "The gift card could not be looked up." | toJson }} }
  ]
}
{{- else if not .action.result.found -}}
{
  "errors": [
    { "attr": "code", "detail": "No gift card found with that code." }
  ]
}
{{- else -}}
{{- $detail := printf "Balance: $%s of $%s %s" .action.result.balance .action.result.initialValue .action.result.currency -}}
{{- if .action.result.expirationDate -}}
  {{- $detail = printf "%s -- expires %s" $detail .action.result.expirationDate -}}
{{- end -}}
{
  "message": "Gift card found",
  "detail": {{ $detail | toJson }}
}
{{- end -}}
