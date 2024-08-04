package br.com.fiap.postech.hackapay.pagamento.controller;

import br.com.fiap.postech.hackapay.pagamento.helper.PagamentoHelper;
import br.com.fiap.postech.hackapay.pagamento.helper.UserHelper;
import br.com.fiap.postech.hackapay.pagamento.integration.CartaoIntegracao;
import br.com.fiap.postech.hackapay.security.SecurityHelper;
import br.com.fiap.postech.hackapay.security.UserDetailsServiceImpl;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class PagamentoControllerIT {

    public static final String PAGAMENTO = "/hackapay/pagamentos";
    @LocalServerPort
    private int port;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private CartaoIntegracao cartaoIntegracao;

    @MockBean
    private SecurityHelper securityHelper;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Nested
    class CadastrarPagamento {
        @Test
        void devePermitirCadastrarPagamento() {
            var pagamento = PagamentoHelper.getPagamento(false);
            var userDetails = UserHelper.getUserDetails("umUsuarioQualquer");
            var token = "token";
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
            when(cartaoIntegracao.atualizaLimiteCartao(token, pagamento)).thenReturn(ResponseEntity.ok().build());
            when(securityHelper.getToken()).thenReturn(token);
            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(pagamento)
                    .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken(userDetails.getUsername()))
            .when()
                .post(PAGAMENTO)
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(matchesJsonSchemaInClasspath("schemas/pagamento.autorizacao.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoCadastrarPagamento_RequisicaoXml() {
            /*
              Na aula o professor instanciou uma string e enviou no .body()
              Mas como o teste valida o contentType o body pode ser enviado com qualquer conteudo
              ou nem mesmo ser enviado como ficou no teste abaixo.
             */
            var userDetails = UserHelper.getUserDetails("umUsuarioQualquer");
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
            given()
                .contentType(MediaType.APPLICATION_XML_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken(userDetails.getUsername()))
            .when()
                .post(PAGAMENTO)
            .then()
                .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"));
        }
    }

    @Nested
    class BuscarPagamento {
        @Test
        void devePermitirBuscarPagamentoPorId() {
            var cpf = "80346534038";
            var userDetails = UserHelper.getUserDetails("umUsuarioQualquer");
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken(userDetails.getUsername()))
            .when()
                .get(PAGAMENTO + "/cliente/{cpf}", cpf)
            .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("schemas/pagamento.list.schema.json"));
        }
    }
}
