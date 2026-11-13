package gestao_estoque.service;

import gestao_estoque.model.Perfil;
import gestao_estoque.model.Usuario;
import gestao_estoque.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInicializador implements CommandLineRunner {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final String email;
    private final String senha;

    public AdminInicializador(UsuarioRepository repository,
                              PasswordEncoder passwordEncoder,
                              @Value("${app.admin.email}") String email,
                              @Value("${app.admin.senha}") String senha) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.email = email;
        this.senha = senha;
    }

    @Override
    public void run(String... args) {
        if (!repository.existsByEmail(email)) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail(email);
            admin.setSenhaHash(passwordEncoder.encode(senha));
            admin.setPerfil(Perfil.ADMIN);
            repository.save(admin);
        }
    }
}