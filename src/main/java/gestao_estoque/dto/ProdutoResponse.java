package gestao_estoque.dto;

import gestao_estoque.model.Produto;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        String nome,
        String sku,
        BigDecimal preco,
        Integer quantidadeAtual,
        Integer estoqueMinimo,
        Integer estoqueMaximo,
        Long fornecedorId,
        String fornecedorNome
) {
    public static ProdutoResponse de(Produto p) {
        return new ProdutoResponse(
                p.getId(), p.getNome(), p.getSku(), p.getPreco(),
                p.getQuantidadeAtual(), p.getEstoqueMinimo(), p.getEstoqueMaximo(),
                p.getFornecedor().getId(), p.getFornecedor().getNome());
    }
}