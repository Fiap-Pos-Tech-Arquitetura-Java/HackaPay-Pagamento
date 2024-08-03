package br.com.fiap.postech.hackapay.pagamento.controller;

import br.com.fiap.postech.hackapay.pagamento.dto.PagamentoAutorizacao;
import br.com.fiap.postech.hackapay.pagamento.entities.Pagamento;
import br.com.fiap.postech.hackapay.pagamento.services.PagamentoService;
import br.com.fiap.postech.hackapay.security.SecurityHelper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pagamento")
public class PagamentoController {

    private final PagamentoService pagamentoService;
    private final SecurityHelper securityHelper;

    @Autowired
    public PagamentoController(PagamentoService pagamentoService, SecurityHelper securityHelper) {
        this.pagamentoService = pagamentoService;
        this.securityHelper = securityHelper;
    }

    @Operation(summary = "registra um pagamento")
    @PostMapping
    public ResponseEntity<PagamentoAutorizacao> save(@Valid @RequestBody Pagamento pagamentoDTO) {
        String token = securityHelper.getToken();
        PagamentoAutorizacao savedPagamentoDTO = pagamentoService.save(token, pagamentoDTO);
        return new ResponseEntity<>(savedPagamentoDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "lista os pagamentos de um CPF")
    @GetMapping("/{cpf}")
    public ResponseEntity<?> findByCpf(@PathVariable String cpf) {
        try {
            List<Pagamento> pagamentos = pagamentoService.findByCpf(cpf);
            return ResponseEntity.ok(pagamentos);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
