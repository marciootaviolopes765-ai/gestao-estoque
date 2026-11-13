package gestao_estoque.dto;

import java.math.BigDecimal;

public record GiroEstoqueResponse(
        Long produtoId,
        String produto,
        String sku,
        Integer quantidadeAtual,
        Long totalEntradas,
        Long totalSaidas,
        BigDecimal giro,
        BigDecimal diasDeCobertura,
        String classificacao
) {
}