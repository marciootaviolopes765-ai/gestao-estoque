package gestao_estoque.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FornecedorRequest(
        @Schema(description = "Razão social ou nome fantasia", example = "Distribuidora ABC")
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150)
        String nome,

        @Schema(description = "CNPJ do fornecedor", example = "12.345.678/0001-90")
        @NotBlank(message = "O CNPJ é obrigatório")
        @Size(max = 18)
        String cnpj,

        @Schema(example = "contato@abc.com")
        @Email(message = "E-mail inválido")
        String email,

        @Schema(example = "11999998888")
        @Size(max = 20)
        String telefone
) {
}