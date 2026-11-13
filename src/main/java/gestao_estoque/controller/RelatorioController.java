package gestao_estoque.controller;

import gestao_estoque.dto.GiroEstoqueResponse;
import gestao_estoque.dto.PosicaoEstoqueResponse;
import gestao_estoque.service.RelatorioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@Tag(name = "Relatórios", description = "Relatórios do Sistema")
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService service;

    public RelatorioController(RelatorioService service) {
        this.service = service;
    }

    @GetMapping("/posicao-estoque")
    @Operation(summary = "Posição atual do estoque com valor total")
    public List<PosicaoEstoqueResponse> posicaoEstoque() {
        return service.posicaoEstoque();
    }

    @GetMapping("/giro-estoque")
    @Operation(summary = "Giro de estoque dos últimos X dias")
    public List<GiroEstoqueResponse> giroEstoque(@RequestParam(defaultValue = "30") int dias) {
        return service.giroEstoque(dias);
    }

    @PostMapping("/conciliar-estoque")
    @Operation(summary = "Recalcula os saldos a partir do histórico")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void conciliarEstoque() {
        service.conciliarEstoque();
    }
}