package br.com.fiap.postech.hackapay.pagamento.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PagamentoAutorizacao(
        @JsonProperty("chave_pagamento")
        String chavePagamento
) {
}
