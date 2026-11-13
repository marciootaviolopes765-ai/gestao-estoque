package gestao_estoque.service;

import gestao_estoque.dto.MovimentacaoRequest;
import gestao_estoque.dto.MovimentacaoResponse;
import gestao_estoque.exception.RecursoNaoEncontradoException;
import gestao_estoque.exception.RegraNegocioException;
import gestao_estoque.model.MovimentacaoEstoque;
import gestao_estoque.model.Produto;
import gestao_estoque.model.TipoMovimentacao;
import gestao_estoque.repository.MovimentacaoEstoqueRepository;
import gestao_estoque.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;

    public MovimentacaoEstoqueService(MovimentacaoEstoqueRepository movimentacaoRepository,
                                      ProdutoRepository produtoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public MovimentacaoResponse registrar(MovimentacaoRequest req) {
        Produto produto = produtoRepository.findById(req.produtoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto não encontrado: " + req.produtoId()));

        int novoSaldo;
        if (req.tipo() == TipoMovimentacao.ENTRADA) {
            novoSaldo = produto.getQuantidadeAtual() + req.quantidade();
            if (novoSaldo > produto.getEstoqueMaximo()) {
                throw new RegraNegocioException(
                        "Entrada ultrapassa o estoque máximo (" + produto.getEstoqueMaximo()
                                + "). Estoque atual: " + produto.getQuantidadeAtual());
            }
        } else {
            novoSaldo = produto.getQuantidadeAtual() - req.quantidade();
            if (novoSaldo < 0) {
                throw new RegraNegocioException(
                        "Estoque insuficiente. Disponível: " + produto.getQuantidadeAtual());
            }
        }

        produto.setQuantidadeAtual(novoSaldo);

        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setProduto(produto);
        mov.setTipo(req.tipo());
        mov.setQuantidade(req.quantidade());
        mov.setObservacao(req.observacao());

        return MovimentacaoResponse.de(movimentacaoRepository.save(mov));
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoResponse> listar(Long produtoId) {
        List<MovimentacaoEstoque> lista = (produtoId == null)
                ? movimentacaoRepository.findAllByOrderByDataHoraDesc()
                : movimentacaoRepository.findByProdutoIdOrderByDataHoraDesc(produtoId);
        return lista.stream().map(MovimentacaoResponse::de).toList();
    }
}