package gestao_estoque.repository;

import gestao_estoque.dto.GiroEstoqueResponse;
import gestao_estoque.dto.PosicaoEstoqueResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RelatorioRepository {

    private final JdbcTemplate jdbc;

    public RelatorioRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<PosicaoEstoqueResponse> posicaoEstoque() {
        return jdbc.query(
                "SELECT * FROM vw_posicao_estoque ORDER BY produto",
                (rs, linha) -> new PosicaoEstoqueResponse(
                        rs.getLong("produto_id"),
                        rs.getString("produto"),
                        rs.getString("sku"),
                        rs.getString("fornecedor"),
                        rs.getInt("quantidade_atual"),
                        rs.getInt("estoque_minimo"),
                        rs.getInt("estoque_maximo"),
                        rs.getBigDecimal("preco"),
                        rs.getBigDecimal("valor_em_estoque"),
                        rs.getString("situacao")));
    }

    public List<GiroEstoqueResponse> giroEstoque(int dias) {
        return jdbc.query(
                "SELECT * FROM fn_giro_estoque(?)",
                (rs, linha) -> new GiroEstoqueResponse(
                        rs.getLong("produto_id"),
                        rs.getString("produto"),
                        rs.getString("sku"),
                        rs.getInt("quantidade_atual"),
                        rs.getLong("total_entradas"),
                        rs.getLong("total_saidas"),
                        rs.getBigDecimal("giro"),
                        rs.getBigDecimal("dias_de_cobertura"),
                        rs.getString("classificacao")),
                dias);
    }

    public void conciliarEstoque() {
        jdbc.execute("CALL sp_conciliar_estoque()");
    }
}