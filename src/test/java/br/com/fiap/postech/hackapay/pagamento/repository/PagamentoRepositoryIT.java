package br.com.fiap.postech.hackapay.pagamento.repository;

import br.com.fiap.postech.hackapay.pagamento.entities.Pagamento;
import br.com.fiap.postech.hackapay.pagamento.helper.PagamentoHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class PagamentoRepositoryIT {
    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Test
    void devePermitirCriarEstrutura() {
        var totalRegistros = pagamentoRepository.count();
        assertThat(totalRegistros).isEqualTo(3);
    }

    @Test
    void devePermitirCadastrarPagamento() {
        // Arrange
        var pagamento = PagamentoHelper.getPagamento(true);
        // Act
        var pagamentoCadastrado = pagamentoRepository.save(pagamento);
        // Assert
        assertThat(pagamentoCadastrado).isInstanceOf(Pagamento.class).isNotNull();
        assertThat(pagamentoCadastrado.getId()).isEqualTo(pagamento.getId());
        assertThat(pagamentoCadastrado.getValor()).isEqualTo(pagamento.getValor());
        assertThat(pagamentoCadastrado.getStatus()).isEqualTo(pagamento.getStatus());
    }
    @Test
    void devePermitirBuscarPagamento() {
        // Arrange
        var id = UUID.fromString("56833f9a-7fda-49d5-a760-8e1ba41f35a8");
        var cpf = "80346534038";
        // Act
        var pagamentoOpcional = pagamentoRepository.findById(id);
        // Assert
        assertThat(pagamentoOpcional).isPresent();
        pagamentoOpcional.ifPresent(
                pagamentoRecebido -> {
                    assertThat(pagamentoRecebido).isInstanceOf(Pagamento.class).isNotNull();
                    assertThat(pagamentoRecebido.getId()).isEqualTo(id);
                    assertThat(pagamentoRecebido.getCpf()).isEqualTo(cpf);
                }
        );
    }
    @Test
    void devePermitirRemoverPagamento() {
        // Arrange
        var id = UUID.fromString("8855e7b2-77b6-448b-97f8-8a0b529f3976");
        // Act
        pagamentoRepository.deleteById(id);
        // Assert
        var pagamentoOpcional = pagamentoRepository.findById(id);
        assertThat(pagamentoOpcional).isEmpty();
    }
    @Test
    void devePermitirListarPagamentos() {
        // Arrange
        // Act
        var pagamentosListados = pagamentoRepository.findAll();
        // Assert
        assertThat(pagamentosListados).hasSize(3);
    }
}
