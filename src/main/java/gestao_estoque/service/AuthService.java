package gestao_estoque.service;

import gestao_estoque.dto.LoginRequest;
import gestao_estoque.dto.TokenResponse;
import gestao_estoque.exception.CredenciaisInvalidasException;
import gestao_estoque.model.Usuario;
import gestao_estoque.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UsuarioRepository repository,
                       PasswordEncoder passwordEncoder,
                       TokenService tokenService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest req) {
        Usuario usuario = repository.findByEmail(req.email())
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!passwordEncoder.matches(req.senha(), usuario.getSenhaHash())) {
            throw new CredenciaisInvalidasException();
        }
        return tokenService.gerar(usuario);
    }
}