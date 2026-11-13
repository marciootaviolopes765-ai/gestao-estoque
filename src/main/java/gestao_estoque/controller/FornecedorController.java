package gestao_estoque.controller;

import gestao_estoque.dto.FornecedorRequest;
import gestao_estoque.dto.FornecedorResponse;
import gestao_estoque.service.FornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
@Tag(name = "Fornecedores", description = "Cadastro e consulta de fornecedores")
public class FornecedorController {

    private final FornecedorService service;

    public FornecedorController(FornecedorService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista todos os fornecedores")
    public List<FornecedorResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um fornecedor pelo id")
    public FornecedorResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra um novo fornecedor",
            description = "O CNPJ não pode estar repetido (erro 422).")
    public FornecedorResponse criar(@Valid @RequestBody FornecedorRequest req) {
        return service.criar(req);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza os dados de um fornecedor")
    public FornecedorResponse atualizar(@PathVariable Long id, @Valid @RequestBody FornecedorRequest req) {
        return service.atualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui um fornecedor",
            description = "Não é possível excluir um fornecedor que possui produtos cadastrados.")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}