package gestao_estoque.dto;

import gestao_estoque.model.MovimentacaoEstoque;
import gestao_estoque.model.TipoMovimentacao;

import java.time.LocalDateTime;

public record MovimentacaoResponse(
        Long id,
        Long produtoId,
        String produtoNome,
        TipoMovimentacao tipo,
        Integer quantidade,
        LocalDateTime dataHora,
        String observacao,
        Integer estoqueAtualDoProduto
) {
    public static MovimentacaoResponse de(MovimentacaoEstoque m) {
        return new MovimentacaoResponse(
                m.getId(),
                m.getProduto().getId(),
                m.getProduto().getNome(),
                m.getTipo(),
                m.getQuantidade(),
                m.getDataHora(),
                m.getObservacao(),
                m.getProduto().getQuantidadeAtual());
    }
}