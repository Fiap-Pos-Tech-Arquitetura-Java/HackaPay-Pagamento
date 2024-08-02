package br.com.fiap.postech.hackapay.pagamento.services;

import br.com.fiap.postech.hackapay.pagamento.entities.Pagamento;
import java.util.List;
import java.util.UUID;

public interface PagamentoService {
    Pagamento save(Pagamento pagamento);

    List<Pagamento> findByCpf(String cpf);
}
