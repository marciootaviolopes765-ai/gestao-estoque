package gestao_estoque.service;

import gestao_estoque.dto.ProdutoRequest;
import gestao_estoque.dto.ProdutoResponse;
import gestao_estoque.exception.RecursoNaoEncontradoException;
import gestao_estoque.exception.RegraNegocioException;
import gestao_estoque.model.Fornecedor;
import gestao_estoque.model.Produto;
import gestao_estoque.repository.FornecedorRepository;
import gestao_estoque.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;

    public ProdutoService(ProdutoRepository produtoRepository,
                          FornecedorRepository fornecedorRepository) {
        this.produtoRepository = produtoRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listar() {
        return produtoRepository.findAll().stream().map(ProdutoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscar(Long id) {
        return ProdutoResponse.de(buscarEntidade(id));
    }

    @Transactional
    public ProdutoResponse criar(ProdutoRequest req) {
        validarEstoque(req);
        if (produtoRepository.existsBySku(req.sku())) {
            throw new RegraNegocioException("Já existe um produto com esse SKU");
        }
        Produto p = new Produto();
        preencher(p, req);
        return ProdutoResponse.de(produtoRepository.save(p));
    }

    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest req) {
        validarEstoque(req);
        Produto p = buscarEntidade(id);
        if (!p.getSku().equals(req.sku()) && produtoRepository.existsBySku(req.sku())) {
            throw new RegraNegocioException("Já existe um produto com esse SKU");
        }
        preencher(p, req);
        return ProdutoResponse.de(p);
    }

    @Transactional
    public void excluir(Long id) {
        produtoRepository.delete(buscarEntidade(id));
    }

    private Produto buscarEntidade(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + id));
    }

    private void validarEstoque(ProdutoRequest req) {
        if (req.estoqueMinimo() > req.estoqueMaximo()) {
            throw new RegraNegocioException("O estoque mínimo não pode ser maior que o máximo");
        }
    }

    private void preencher(Produto p, ProdutoRequest req) {
        Fornecedor fornecedor = fornecedorRepository.findById(req.fornecedorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Fornecedor não encontrado: " + req.fornecedorId()));
        p.setNome(req.nome());
        p.setSku(req.sku());
        p.setPreco(req.preco());
        p.setEstoqueMinimo(req.estoqueMinimo());
        p.setEstoqueMaximo(req.estoqueMaximo());
        p.setFornecedor(fornecedor);
    }
}