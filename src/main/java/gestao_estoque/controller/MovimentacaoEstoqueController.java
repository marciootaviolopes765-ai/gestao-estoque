package gestao_estoque.controller;

import gestao_estoque.dto.MovimentacaoRequest;
import gestao_estoque.dto.MovimentacaoResponse;
import gestao_estoque.service.MovimentacaoEstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
@Tag(name = "Movimentações", description = "Movimentações de Produtos")
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService service;

    public MovimentacaoEstoqueController(MovimentacaoEstoqueService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Registra uma entrada ou saída de estoque")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentacaoResponse registrar(@Valid @RequestBody MovimentacaoRequest req) {
        return service.registrar(req);
    }

    @GetMapping
    @Operation(summary = "Lista o histórico (filtro opcional por produto)")
    public List<MovimentacaoResponse> listar(@RequestParam(required = false) Long produtoId) {
        return service.listar(produtoId);
    }
}