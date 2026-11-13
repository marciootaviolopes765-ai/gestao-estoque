package gestao_estoque.dto;

import gestao_estoque.model.TipoMovimentacao;
import jakarta.validation.constraints.*;

public record MovimentacaoRequest(
        @NotNull(message = "O produto é obrigatório")
        Long produtoId,

        @NotNull(message = "O tipo é obrigatório (ENTRADA ou SAIDA)")
        TipoMovimentacao tipo,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser maior que zero")
        Integer quantidade,

        @Size(max = 255)
        String observacao
) {
}