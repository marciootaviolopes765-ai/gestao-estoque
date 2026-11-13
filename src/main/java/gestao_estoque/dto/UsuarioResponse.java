package gestao_estoque.dto;

import gestao_estoque.model.Perfil;
import gestao_estoque.model.Usuario;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        Perfil perfil
) {
    public static UsuarioResponse de(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getPerfil());
    }
}