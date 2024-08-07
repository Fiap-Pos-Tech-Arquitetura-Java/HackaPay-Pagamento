package br.com.fiap.postech.hackapay.pagamento.integration;

import br.com.fiap.postech.hackapay.pagamento.helper.PagamentoHelper;
import br.com.fiap.postech.hackapay.pagamento.helper.UserHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockRestServiceServer
@ExtendWith(MockitoExtension.class)
class CartaoIntegracaoTest {

    @SpyBean
    RestClient.Builder builder;

    @Autowired
    @InjectMocks
    private CartaoIntegracao cartaoIntegracao;

    private MockRestServiceServer mockServer;

    @Test
    void atualizaLimiteCartao() throws URISyntaxException {
        var pagamento = PagamentoHelper.getPagamento(true);
        var uri = "http://localhost:8082/api/cartao/atualizaLimiteCartao/" + pagamento.getValor();
        var userDetails = UserHelper.getUserDetails("umUsuarioQualquer");

        mockServer = MockRestServiceServer.bindTo(builder).build();
        mockServer.expect(requestTo(uri)).andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/json"))
                .andExpect(header(HttpHeaders.AUTHORIZATION, UserHelper.getToken(userDetails.getUsername())))
                .andRespond(withSuccess());

        assertThatThrownBy(() -> cartaoIntegracao.atualizaLimiteCartao("token", pagamento))
                .isInstanceOf(ResourceAccessException.class);
    }
}