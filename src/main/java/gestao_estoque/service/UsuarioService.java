package gestao_estoque.service;

import gestao_estoque.dto.UsuarioRequest;
import gestao_estoque.dto.UsuarioResponse;
import gestao_estoque.exception.RegraNegocioException;
import gestao_estoque.model.Usuario;
import gestao_estoque.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponse criar(UsuarioRequest req) {
        if (repository.existsByEmail(req.email())) {
            throw new RegraNegocioException("Já existe um usuário com esse e-mail");
        }
        Usuario u = new Usuario();
        u.setNome(req.nome());
        u.setEmail(req.email());
        u.setSenhaHash(passwordEncoder.encode(req.senha()));
        u.setPerfil(req.perfil());
        return UsuarioResponse.de(repository.save(u));
    }
}