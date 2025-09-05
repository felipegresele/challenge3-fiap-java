package challenge_mottu_2_semestre.challenge_mottu.security;

import challenge_mottu_2_semestre.challenge_mottu.model.Role;
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
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, "ADMIN"));
            roleRepository.save(new Role(null, "USER"));
        }
    }
}
