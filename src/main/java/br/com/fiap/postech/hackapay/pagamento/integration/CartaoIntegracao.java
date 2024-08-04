package br.com.fiap.postech.hackapay.pagamento.integration;

import br.com.fiap.postech.hackapay.pagamento.entities.Pagamento;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CartaoIntegracao {

    @Value("${hackapay.cartao.url}")
    String baseURI;

    public ResponseEntity<Void> atualizaLimiteCartao(String token, Pagamento pagamento) {
        RestClient restClient = RestClient.create();
        restClient.post()
                .uri(baseURI + "/atualizaLimiteCartao/{valor}", pagamento.getValor())
                .body(pagamento)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> { throw new IllegalArgumentException("Falha na autorizacao do pagamento: "); })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> { throw new IllegalArgumentException("Falha na autorizacao do pagamento.."); })
                .toBodilessEntity();
        return ResponseEntity.ok().build();
    }
}
