package gestao_estoque.dto;

import gestao_estoque.model.Fornecedor;

public record FornecedorResponse(
        Long id,
        String nome,
        String cnpj,
        String email,
        String telefone
) {
    public static FornecedorResponse de(Fornecedor f) {
        return new FornecedorResponse(
                f.getId(), f.getNome(), f.getCnpj(), f.getEmail(), f.getTelefone());
    }
}