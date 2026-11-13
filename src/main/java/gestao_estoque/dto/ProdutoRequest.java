package gestao_estoque.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoRequest(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150)
        String nome,

        @NotBlank(message = "O SKU é obrigatório")
        @Size(max = 50)
        String sku,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        BigDecimal preco,

        @NotNull(message = "O estoque mínimo é obrigatório")
        @Min(value = 0, message = "O estoque mínimo não pode ser negativo")
        Integer estoqueMinimo,

        @NotNull(message = "O estoque máximo é obrigatório")
        @Min(value = 1, message = "O estoque máximo deve ser maior que zero")
        Integer estoqueMaximo,

        @NotNull(message = "O fornecedor é obrigatório")
        Long fornecedorId
) {
}