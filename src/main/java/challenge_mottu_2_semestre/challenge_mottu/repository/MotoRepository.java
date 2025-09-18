package challenge_mottu_2_semestre.challenge_mottu.repository;

import challenge_mottu_2_semestre.challenge_mottu.model.Moto;
import challenge_mottu_2_semestre.challenge_mottu.model.Motoqueiro;
import challenge_mottu_2_semestre.challenge_mottu.model.StatusMoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotoRepository extends JpaRepository<Moto, Long> {
    boolean existsByMotoboyEmUsoId(Long id);
    boolean existsByGalpaoId(Long galpaoId);
    boolean existsByMotoboyEmUsoAndStatus(Motoqueiro motoqueiro, StatusMoto status);
}
