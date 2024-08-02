package br.com.fiap.postech.hackapay.pagamento.repository;

import br.com.fiap.postech.hackapay.pagamento.entities.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {
    List<Pagamento> findByCpf(String cpf);
}
