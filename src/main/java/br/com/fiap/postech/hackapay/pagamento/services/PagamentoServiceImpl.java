package br.com.fiap.postech.hackapay.pagamento.services;

import br.com.fiap.postech.hackapay.pagamento.entities.Pagamento;
import br.com.fiap.postech.hackapay.pagamento.repository.PagamentoRepository;
import io.micrometer.common.util.StringUtils;
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

    @Autowired
    public PagamentoServiceImpl(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @Override
    public Pagamento save(Pagamento pagamento) {
        //if (pagamentoRepository.findByCpf(pagamento.getCpf()).isPresent()) {
        //    throw new IllegalArgumentException("Já existe um pagamento cadastrado com esse cpf.");
        //}
        if (pagamento.getDescricao() == null) {
            pagamento.setDescricao("descricao fixa conforme orientacao do professor no discord");
        }
        if (pagamento.getMetodoPagamento() == null) {
            pagamento.setMetodoPagamento("Cartão de Credito - fixa conforme orientacao do professor no discord");
        }
        pagamento.setStatus("äprovado");
        pagamento.setId(UUID.randomUUID());
        return pagamentoRepository.save(pagamento);
    }

    @Override
    public List<Pagamento> findByCpf(String cpf) {
        return pagamentoRepository.findByCpf(cpf);
    }
}
