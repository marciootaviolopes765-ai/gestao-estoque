package gestao_estoque.service;

import gestao_estoque.dto.AlertaEstoqueResponse;
import gestao_estoque.dto.ResumoEstoqueResponse;
import gestao_estoque.model.Produto;
import gestao_estoque.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlertaEstoqueService {

    private final ProdutoRepository produtoRepository;

    public AlertaEstoqueService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public List<AlertaEstoqueResponse> estoqueBaixo() {
        return produtoRepository.findEstoqueBaixo().stream()
                .map(AlertaEstoqueResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public List<AlertaEstoqueResponse> estoqueAlto() {
        return produtoRepository.findEstoqueAlto().stream()
                .map(AlertaEstoqueResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public ResumoEstoqueResponse resumo() {
        List<Produto> todos = produtoRepository.findAll();
        long baixo = todos.stream()
                .filter(p -> p.getQuantidadeAtual() <= p.getEstoqueMinimo()).count();
        long alto = todos.stream()
                .filter(p -> p.getQuantidadeAtual() >= p.getEstoqueMaximo() * 0.9).count();
        long normal = todos.size() - baixo - alto;
        return new ResumoEstoqueResponse(todos.size(), baixo, normal, alto);
    }
}