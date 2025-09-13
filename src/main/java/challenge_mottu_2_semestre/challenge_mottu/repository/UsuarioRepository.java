package challenge_mottu_2_semestre.challenge_mottu.repository;

import challenge_mottu_2_semestre.challenge_mottu.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
}
