package br.com.fiap.postech.hackapay.pagamento.services;

import br.com.fiap.postech.hackapay.pagamento.dto.PagamentoAutorizacao;
import br.com.fiap.postech.hackapay.pagamento.helper.PagamentoHelper;
import br.com.fiap.postech.hackapay.pagamento.integration.CartaoIntegracao;
import br.com.fiap.postech.hackapay.security.SecurityHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class PagamentoServiceIT {
    @Autowired
    private PagamentoService pagamentoService;

    @MockBean
    private CartaoIntegracao cartaoIntegracao;

    @MockBean
    private SecurityHelper securityHelper;

    @Nested
    class CadastrarPagamento {
        @Test
        void devePermitirCadastrarPagamento() {
            // Arrange
            var pagamento = PagamentoHelper.getPagamento(false);
            var token = "token";
            when(cartaoIntegracao.atualizaLimiteCartao(token, pagamento)).thenReturn(ResponseEntity.ok().build());
            when(securityHelper.getToken()).thenReturn(token);
            // Act
            var pagamentoSalvo = pagamentoService.save(token, pagamento);
            // Assert
            assertThat(pagamentoSalvo)
                    .isInstanceOf(PagamentoAutorizacao.class)
                    .isNotNull();
            assertThat(pagamentoSalvo.chavePagamento()).isNotNull();
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
