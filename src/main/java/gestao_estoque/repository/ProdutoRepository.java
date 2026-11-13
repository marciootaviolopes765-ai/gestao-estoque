package gestao_estoque.repository;

import gestao_estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsBySku(String sku);

    boolean existsByFornecedorId(Long fornecedorId);
    @Query("SELECT p FROM Produto p WHERE p.quantidadeAtual <= p.estoqueMinimo ORDER BY p.quantidadeAtual ASC")
    List<Produto> findEstoqueBaixo();

    @Query("SELECT p FROM Produto p WHERE p.quantidadeAtual >= p.estoqueMaximo * 0.9 ORDER BY p.quantidadeAtual DESC")
    List<Produto> findEstoqueAlto();

}