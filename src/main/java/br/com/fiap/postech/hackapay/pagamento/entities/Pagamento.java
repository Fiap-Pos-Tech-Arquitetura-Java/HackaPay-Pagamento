package br.com.fiap.postech.hackapay.pagamento.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_pagamento")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pagamento {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
    @Column(name = "cpf", nullable = false)
    private String cpf;
    @Transient
    private String numero;
    @Transient
    @JsonProperty("data_validade")
    private String dataValidade;
    @Transient
    private String cvv;
    @Column(name = "valor", nullable = false)
    private Double valor;
    @Column(name = "descricao", nullable = false)
    private String descricao;
    @Column(name = "metodo_pagamento", nullable = false)
    @JsonProperty("metodo_pagamento")
    private String metodoPagamento;
    @Column(name = "status", nullable = false)
    private String status;

    public Pagamento() {
        super();
    }

    public Pagamento(String cpf, String numero, String dataValidade, String cvv, Double valor, String status) {
        this.cpf = cpf;
        this.numero = numero;
        this.dataValidade = dataValidade;
        this.cvv = cvv;
        this.valor = valor;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pagamento pagamento)) return false;
        return Objects.equals(id, pagamento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(String dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
