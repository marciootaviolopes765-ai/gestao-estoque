package gestao_estoque.dto;

import java.math.BigDecimal;

public record PosicaoEstoqueResponse(
        Long produtoId,
        String produto,
        String sku,
        String fornecedor,
        Integer quantidadeAtual,
        Integer estoqueMinimo,
        Integer estoqueMaximo,
        BigDecimal preco,
        BigDecimal valorEmEstoque,
        String situacao
) {
}