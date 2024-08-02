package br.com.fiap.postech.hackapay.pagamento.repository;

import br.com.fiap.postech.hackapay.pagamento.entities.Pagamento;
import br.com.fiap.postech.hackapay.pagamento.helper.PagamentoHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PagamentoRepositoryTest {
    @Mock
    private PagamentoRepository pagamentoRepository;

    AutoCloseable openMocks;
    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirCadastrarPagamento() {
        // Arrange
        var pagamento = PagamentoHelper.getPagamento(false);
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);
        // Act
        var savedPagamento = pagamentoRepository.save(pagamento);
        // Assert
        assertThat(savedPagamento).isNotNull().isEqualTo(pagamento);
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }

    @Test
    void devePermitirBuscarPagamento() {
        // Arrange
        var pagamento = PagamentoHelper.getPagamento(true);
        when(pagamentoRepository.findById(pagamento.getId())).thenReturn(Optional.of(pagamento));
        // Act
        var pagamentoOpcional = pagamentoRepository.findById(pagamento.getId());
        // Assert
        assertThat(pagamentoOpcional).isNotNull().containsSame(pagamento);
        pagamentoOpcional.ifPresent(
                pagamentoRecebido -> {
                    assertThat(pagamentoRecebido).isInstanceOf(Pagamento.class).isNotNull();
                    assertThat(pagamentoRecebido.getId()).isEqualTo(pagamento.getId());
                    assertThat(pagamentoRecebido.getValor()).isEqualTo(pagamento.getValor());
                }
        );
        verify(pagamentoRepository, times(1)).findById(pagamento.getId());
    }
    @Test
    void devePermitirRemoverPagamento() {
        //Arrange
        var id = UUID.randomUUID();
        doNothing().when(pagamentoRepository).deleteById(id);
        //Act
        pagamentoRepository.deleteById(id);
        //Assert
        verify(pagamentoRepository, times(1)).deleteById(id);
    }
    @Test
    void devePermitirListarPagamentos() {
        // Arrange
        var pagamento1 = PagamentoHelper.getPagamento(true);
        var pagamento2 = PagamentoHelper.getPagamento(true);
        var listaPagamentos = Arrays.asList(
                pagamento1,
                pagamento2
        );
        when(pagamentoRepository.findAll()).thenReturn(listaPagamentos);
        // Act
        var pagamentosListados = pagamentoRepository.findAll();
        assertThat(pagamentosListados)
                .hasSize(2)
                .containsExactlyInAnyOrder(pagamento1, pagamento2);
        verify(pagamentoRepository, times(1)).findAll();
    }
}