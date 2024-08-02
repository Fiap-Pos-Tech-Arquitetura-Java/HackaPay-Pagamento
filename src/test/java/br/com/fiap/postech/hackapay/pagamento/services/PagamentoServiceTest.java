package br.com.fiap.postech.hackapay.pagamento.services;

import br.com.fiap.postech.hackapay.pagamento.entities.Pagamento;
import br.com.fiap.postech.hackapay.pagamento.helper.PagamentoHelper;
import br.com.fiap.postech.hackapay.pagamento.repository.PagamentoRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.fail;

class PagamentoServiceTest {
    private PagamentoService pagamentoService;

    @Mock
    private PagamentoRepository pagamentoRepository;

    private AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        pagamentoService = new PagamentoServiceImpl(pagamentoRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class CadastrarPagamento {
        @Test
        void devePermitirCadastrarPagamento() {
            // Arrange
            var pagamento = PagamentoHelper.getPagamento(false);
            when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(r -> r.getArgument(0));
            // Act
            var pagamentoSalvo = pagamentoService.save(pagamento);
            // Assert
            assertThat(pagamentoSalvo)
                    .isInstanceOf(Pagamento.class)
                    .isNotNull();
            assertThat(pagamentoSalvo.getNumero()).isEqualTo(pagamento.getNumero());
            assertThat(pagamentoSalvo.getId()).isNotNull();
            verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
        }
    }

    @Nested
    class BuscarPagamento {
        @Test
        void devePermitirBuscarPagamentoPorCpf() {
            // Arrange
            var pagamento = PagamentoHelper.getPagamento(true);
            when(pagamentoRepository.findByCpf(pagamento.getCpf())).thenReturn(List.of(pagamento));
            // Act
            var pagamentoObtido = pagamentoService.findByCpf(pagamento.getCpf());
            // Assert
            assertThat(pagamentoObtido).isNotNull();
            assertThat(pagamentoObtido).isNotEmpty();
            assertThat(pagamentoObtido.get(0)).isEqualTo(pagamento);
            verify(pagamentoRepository, times(1)).findByCpf(anyString());
        }
    }
}