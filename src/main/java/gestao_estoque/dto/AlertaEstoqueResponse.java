package gestao_estoque.dto;

import gestao_estoque.model.Produto;

public record AlertaEstoqueResponse(
        Long produtoId,
        String nome,
        String sku,
        Integer quantidadeAtual,
        Integer estoqueMinimo,
        Integer estoqueMaximo,
        Integer sugestaoReposicao,
        String fornecedorNome
) {
    public static AlertaEstoqueResponse de(Produto p) {
        int sugestao = Math.max(0, p.getEstoqueMaximo() - p.getQuantidadeAtual());
        return new AlertaEstoqueResponse(
                p.getId(), p.getNome(), p.getSku(),
                p.getQuantidadeAtual(), p.getEstoqueMinimo(), p.getEstoqueMaximo(),
                sugestao, p.getFornecedor().getNome());
    }
}