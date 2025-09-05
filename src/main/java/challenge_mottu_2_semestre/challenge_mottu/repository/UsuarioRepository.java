package challenge_mottu_2_semestre.challenge_mottu.repository;

import challenge_mottu_2_semestre.challenge_mottu.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
