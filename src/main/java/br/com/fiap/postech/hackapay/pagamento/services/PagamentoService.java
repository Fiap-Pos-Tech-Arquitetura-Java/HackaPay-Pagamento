package br.com.fiap.postech.hackapay.pagamento.services;

import br.com.fiap.postech.hackapay.pagamento.dto.PagamentoAutorizacao;
import br.com.fiap.postech.hackapay.pagamento.entities.Pagamento;
import java.util.List;

public interface PagamentoService {
    PagamentoAutorizacao save(String token, Pagamento pagamento);

    List<Pagamento> findByCpf(String cpf);
}
