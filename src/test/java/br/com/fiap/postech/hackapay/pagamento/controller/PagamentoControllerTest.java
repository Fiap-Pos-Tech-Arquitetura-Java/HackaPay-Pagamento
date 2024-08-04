package br.com.fiap.postech.hackapay.pagamento.controller;

import br.com.fiap.postech.hackapay.pagamento.dto.PagamentoAutorizacao;
import br.com.fiap.postech.hackapay.pagamento.entities.Pagamento;
import br.com.fiap.postech.hackapay.pagamento.helper.PagamentoHelper;
import br.com.fiap.postech.hackapay.pagamento.services.PagamentoService;
import br.com.fiap.postech.hackapay.security.SecurityHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PagamentoControllerTest {
    public static final String PAGAMENTO = "/pagamento";
    private MockMvc mockMvc;
    @Mock
    private PagamentoService pagamentoService;
    @Mock
    private SecurityHelper securityHelper;
    private AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        PagamentoController pagamentoController = new PagamentoController(pagamentoService, securityHelper);
        mockMvc = MockMvcBuilders.standaloneSetup(pagamentoController).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    public static String asJsonString(final Object object) throws Exception {
        return new ObjectMapper().writeValueAsString(object);
    }

    @Nested
    class CadastrarPagamento {
        @Test
        void devePermitirCadastrarPagamento() throws Exception {
            // Arrange
            var pagamento = PagamentoHelper.getPagamento(false);
            var pagamentoAutorizacao = new PagamentoAutorizacao("blaBlaBla");
            when(pagamentoService.save(anyString(), any(Pagamento.class))).thenReturn(pagamentoAutorizacao);
            when(securityHelper.getToken()).thenReturn("token");
            // Act
            mockMvc.perform(
                            post(PAGAMENTO).contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(pagamento)))
                    .andExpect(status().isCreated());
            // Assert
            verify(pagamentoService, times(1)).save(anyString(), any(Pagamento.class));
        }

        @Test
        void deveGerarExcecao_QuandoRegistrarPagamento_RequisicaoXml() throws Exception {
            // Arrange
            var pagamento = PagamentoHelper.getPagamento(false);
            when(pagamentoService.save(anyString(), any(Pagamento.class))).thenAnswer(r -> r.getArgument(0));
            // Act
            mockMvc.perform(
                            post("/pagamento").contentType(MediaType.APPLICATION_XML)
                                    .content(asJsonString(pagamento)))
                    .andExpect(status().isUnsupportedMediaType());
            // Assert
            verify(pagamentoService, never()).save(anyString(), any(Pagamento.class));
        }
    }
    @Nested
    class BuscarPagamento {
        @Test
        void devePermitirBuscarPagamentoPorId() throws Exception {
            // Arrange
            var pagamento = PagamentoHelper.getPagamento(true);
            when(pagamentoService.findByCpf(anyString())).thenReturn(List.of(pagamento));
            // Act
            mockMvc.perform(get("/pagamento/{cpf}", pagamento.getCpf()))
                    .andExpect(status().isOk());
            // Assert
            verify(pagamentoService, times(1)).findByCpf(anyString());
        }
    }
}