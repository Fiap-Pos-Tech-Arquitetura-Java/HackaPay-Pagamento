package br.com.fiap.postech.hackapay.pagamento.services;

import br.com.fiap.postech.hackapay.pagamento.dto.PagamentoAutorizacao;
import br.com.fiap.postech.hackapay.pagamento.entities.Pagamento;
import br.com.fiap.postech.hackapay.pagamento.integration.CartaoIntegracao;
import br.com.fiap.postech.hackapay.pagamento.repository.PagamentoRepository;
import io.micrometer.common.util.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PagamentoServiceImpl implements PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final CartaoIntegracao cartaoIntegracao;

    @Autowired
    public PagamentoServiceImpl(PagamentoRepository pagamentoRepository, CartaoIntegracao cartaoIntegracao) {
        this.pagamentoRepository = pagamentoRepository;
        this.cartaoIntegracao = cartaoIntegracao;
    }

    @Override
    public PagamentoAutorizacao save(String token, Pagamento pagamento) {
        if (pagamento.getDescricao() == null) {
            pagamento.setDescricao("descricao fixa conforme orientacao do professor no discord");
        }
        if (pagamento.getMetodoPagamento() == null) {
            pagamento.setMetodoPagamento("Cartão de Credito - fixa conforme orientacao do professor no discord");
        }
        cartaoIntegracao.atualizaLimiteCartao(token, pagamento);
        pagamento.setStatus("aprovado");
        pagamento.setId(UUID.randomUUID());
        pagamentoRepository.save(pagamento);
        return new PagamentoAutorizacao(RandomStringUtils.randomAlphabetic(10));
    }

    @Override
    public List<Pagamento> findByCpf(String cpf) {
        return pagamentoRepository.findByCpf(cpf);
    }
}
