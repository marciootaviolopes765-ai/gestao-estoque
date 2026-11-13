package gestao_estoque.dto;

public record ResumoEstoqueResponse(
        long totalProdutos,
        long estoqueBaixo,
        long estoqueNormal,
        long estoqueAlto
) {
}