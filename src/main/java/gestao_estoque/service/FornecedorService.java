package gestao_estoque.service;

import gestao_estoque.dto.FornecedorRequest;
import gestao_estoque.dto.FornecedorResponse;
import gestao_estoque.exception.RecursoNaoEncontradoException;
import gestao_estoque.exception.RegraNegocioException;
import gestao_estoque.model.Fornecedor;
import gestao_estoque.repository.FornecedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import gestao_estoque.repository.ProdutoRepository;

import java.util.List;

@Service
public class FornecedorService {

    private final FornecedorRepository repository;
    private final ProdutoRepository produtoRepository;

    public FornecedorService(FornecedorRepository repository,
                             ProdutoRepository produtoRepository) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
    }


    @Transactional(readOnly = true)
    public List<FornecedorResponse> listar() {
        return repository.findAll().stream().map(FornecedorResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public FornecedorResponse buscar(Long id) {
        return FornecedorResponse.de(buscarEntidade(id));
    }

    @Transactional
    public FornecedorResponse criar(FornecedorRequest req) {
        if (repository.existsByCnpj(req.cnpj())) {
            throw new RegraNegocioException("Já existe um fornecedor com esse CNPJ");
        }
        Fornecedor f = new Fornecedor();
        preencher(f, req);
        return FornecedorResponse.de(repository.save(f));
    }

    @Transactional
    public FornecedorResponse atualizar(Long id, FornecedorRequest req) {
        Fornecedor f = buscarEntidade(id);
        if (!f.getCnpj().equals(req.cnpj()) && repository.existsByCnpj(req.cnpj())) {
            throw new RegraNegocioException("Já existe um fornecedor com esse CNPJ");
        }
        preencher(f, req);
        return FornecedorResponse.de(f);
    }

    @Transactional
    public void excluir(Long id) {
        Fornecedor f = buscarEntidade(id);
        if (produtoRepository.existsByFornecedorId(id)) {
            throw new RegraNegocioException("Não é possível excluir: o fornecedor possui produtos cadastrados");
        }
        repository.delete(f);
    }

    private Fornecedor buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fornecedor não encontrado: " + id));
    }

    private void preencher(Fornecedor f, FornecedorRequest req) {
        f.setNome(req.nome());
        f.setCnpj(req.cnpj());
        f.setEmail(req.email());
        f.setTelefone(req.telefone());
    }
}