package gestao_estoque.dto;

import java.time.Instant;

public record TokenResponse(
        String token,
        String tipo,
        Instant expiraEm
) {
}