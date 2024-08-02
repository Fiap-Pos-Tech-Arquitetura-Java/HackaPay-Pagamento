package br.com.fiap.postech.hackapay.pagamento.services;

import br.com.fiap.postech.hackapay.pagamento.entities.Pagamento;
import br.com.fiap.postech.hackapay.pagamento.helper.PagamentoHelper;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.AssertionErrors.fail;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class PagamentoServiceIT {
    @Autowired
    private PagamentoService pagamentoService;

    @Nested
    class CadastrarPagamento {
        @Test
        void devePermitirCadastrarPagamento() {
            // Arrange
            var pagamento = PagamentoHelper.getPagamento(false);
            // Act
            var pagamentoSalvo = pagamentoService.save(pagamento);
            // Assert
            assertThat(pagamentoSalvo)
                    .isInstanceOf(Pagamento.class)
                    .isNotNull();
            assertThat(pagamentoSalvo.getValor()).isEqualTo(pagamento.getValor());
            assertThat(pagamentoSalvo.getId()).isNotNull();
        }
    }

    @Nested
    class BuscarPagamento {
        @Test
        void devePermitirBuscarPagamentoPorId() {
            // Arrange
            var id = UUID.fromString("56833f9a-7fda-49d5-a760-8e1ba41f35a8");
            var cpf = "80346534038";
            // Act
            var pagamentoObtido = pagamentoService.findByCpf(cpf);
            // Assert
            assertThat(pagamentoObtido).isNotNull().isInstanceOf(List.class);
            assertThat(pagamentoObtido.get(0).getCpf()).isEqualTo(cpf);
            assertThat(pagamentoObtido.get(0).getId()).isNotNull();
            assertThat(pagamentoObtido.get(0).getId()).isEqualTo(id);
        }
    }
}
