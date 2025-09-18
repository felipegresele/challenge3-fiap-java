package challenge_mottu_2_semestre.challenge_mottu.repository;

import challenge_mottu_2_semestre.challenge_mottu.model.Moto;
import challenge_mottu_2_semestre.challenge_mottu.model.Motoqueiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MotoqueiroRepository extends JpaRepository<Motoqueiro, Long> {

}
