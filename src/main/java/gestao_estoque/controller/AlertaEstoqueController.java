package gestao_estoque.controller;

import gestao_estoque.dto.AlertaEstoqueResponse;
import gestao_estoque.dto.ResumoEstoqueResponse;
import gestao_estoque.service.AlertaEstoqueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@Tag(name = "Alertas", description = "Alertas gerais")
@RequestMapping("/alertas")
public class AlertaEstoqueController {

    private final AlertaEstoqueService service;

    public AlertaEstoqueController(AlertaEstoqueService service) {
        this.service = service;
    }

    @GetMapping("/estoque-baixo")
    @Operation(summary = "Produtos no estoque mínimo ou abaixo, com sugestão de reposição")
    public List<AlertaEstoqueResponse> estoqueBaixo() {
        return service.estoqueBaixo();
    }

    @GetMapping("/estoque-alto")
    @Operation(summary = "Produtos com 90% ou mais do estoque máximo")
    public List<AlertaEstoqueResponse> estoqueAlto() {
        return service.estoqueAlto();
    }

    @GetMapping("/resumo")
    @Operation(summary = "Quantidade de produtos por situação de estoque")
    public ResumoEstoqueResponse resumo() {
        return service.resumo();
    }
}