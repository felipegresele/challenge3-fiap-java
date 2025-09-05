package challenge_mottu_2_semestre.challenge_mottu.controller;

import challenge_mottu_2_semestre.challenge_mottu.model.Moto;
import challenge_mottu_2_semestre.challenge_mottu.model.Role;
import challenge_mottu_2_semestre.challenge_mottu.model.Usuario;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.RoleRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mottu")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/exemplo")
    public String exemplo() {
        return "exemplo";
    }

    @GetMapping("/listar/usuarios")
    public List<Usuario> listarUsuariosCadastrados() {
        return usuarioRepository.findAll();
    }

    @PostMapping("/save/usuario")
    public Usuario cadastrarMoto(@RequestBody Usuario user) {

        Role role = roleRepository.findById(user.getRole().getId())
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));

        user.setRole(role);
        return usuarioRepository.save(user);
    }

}
