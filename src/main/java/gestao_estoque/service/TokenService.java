package gestao_estoque.service;

import gestao_estoque.dto.TokenResponse;
import gestao_estoque.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    private final JwtEncoder encoder;
    private final long expiracaoMinutos;

    public TokenService(JwtEncoder encoder,
                        @Value("${app.jwt.expiracao-minutos:60}") long expiracaoMinutos) {
        this.encoder = encoder;
        this.expiracaoMinutos = expiracaoMinutos;
    }

    public TokenResponse gerar(Usuario usuario) {
        Instant agora = Instant.now();
        Instant expira = agora.plus(expiracaoMinutos, ChronoUnit.MINUTES);

        JwtClaimsSet dados = JwtClaimsSet.builder()
                .issuer("gestao-estoque")
                .subject(usuario.getEmail())
                .issuedAt(agora)
                .expiresAt(expira)
                .claim("perfil", usuario.getPerfil().name())
                .build();

        JwsHeader cabecalho = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = encoder.encode(JwtEncoderParameters.from(cabecalho, dados)).getTokenValue();

        return new TokenResponse(token, "Bearer", expira);
    }
}