package gestao_estoque.repository;

import gestao_estoque.model.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    List<MovimentacaoEstoque> findByProdutoIdOrderByDataHoraDesc(Long produtoId);

    List<MovimentacaoEstoque> findAllByOrderByDataHoraDesc();
}