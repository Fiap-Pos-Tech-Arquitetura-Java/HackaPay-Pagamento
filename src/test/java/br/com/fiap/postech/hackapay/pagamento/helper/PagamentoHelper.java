package br.com.fiap.postech.hackapay.pagamento.helper;

import br.com.fiap.postech.hackapay.pagamento.entities.Pagamento;

import java.util.UUID;

public class PagamentoHelper {
    public static Pagamento getPagamento(boolean geraId) {
        var pagamento = new Pagamento(
                "27971198000",
                "4417810025751018",
                "12/30",
                "237",
                123.45,
                null
        );
        if (geraId) {
            pagamento.setId(UUID.randomUUID());
        }
        return pagamento;
    }
}
