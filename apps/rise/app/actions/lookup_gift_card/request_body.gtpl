{{- /* Filter syntax confirmed live 2026-08-20 against dev.rise.ai's "About API Query Language" doc: direct equality is {"<field>": <value>}. */ -}}
{{- $code := .inputs.code | toString | trim -}}
{{- dict "query" (dict "filter" (dict "code" $code)) | toJson -}}
