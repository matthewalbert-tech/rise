{{- if .action.result.success -}}
{
  "message": "Store credit issued",
  "detail": {{ printf "New balance: $%s (Rise transaction %s)." .action.result.newBalance .action.result.transactionId | toJson }}
}
{{- else -}}
{
  "errors": [
    { "attr": "confirm", "detail": {{ .action.result.message | default "The credit could not be issued." | toJson }} }
  ]
}
{{- end -}}
