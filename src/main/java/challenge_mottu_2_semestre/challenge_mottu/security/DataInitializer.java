package challenge_mottu_2_semestre.challenge_mottu.security;

import challenge_mottu_2_semestre.challenge_mottu.model.Role;
import challenge_mottu_2_semestre.challenge_mottu.model.RoleName;
import challenge_mottu_2_semestre.challenge_mottu.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;


    @Override
    public void run(String... args) throws Exception {
        //vai verificar se existe e so adiciona se nao existir
        if (!roleRepository.existsById(1L)) {
            roleRepository.save(new Role(1L, RoleName.ADMIN));
        }
        if (!roleRepository.existsById(2L)) {
            roleRepository.save(new Role(2L, RoleName.OPERADOR));
        }
    }
}
