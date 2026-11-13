package gestao_estoque.service;

import gestao_estoque.dto.GiroEstoqueResponse;
import gestao_estoque.dto.PosicaoEstoqueResponse;
import gestao_estoque.exception.RegraNegocioException;
import gestao_estoque.repository.RelatorioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RelatorioService {

    private final RelatorioRepository repository;

    public RelatorioService(RelatorioRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<PosicaoEstoqueResponse> posicaoEstoque() {
        return repository.posicaoEstoque();
    }

    @Transactional(readOnly = true)
    public List<GiroEstoqueResponse> giroEstoque(int dias) {
        if (dias < 1 || dias > 365) {
            throw new RegraNegocioException("O período deve estar entre 1 e 365 dias");
        }
        return repository.giroEstoque(dias);
    }

    @Transactional
    public void conciliarEstoque() {
        repository.conciliarEstoque();
    }
}