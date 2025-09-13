package challenge_mottu_2_semestre.challenge_mottu.controller;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.UsuarioDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Moto;
import challenge_mottu_2_semestre.challenge_mottu.model.Role;
import challenge_mottu_2_semestre.challenge_mottu.model.Usuario;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.RoleRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/mottu")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/usuarios/listar")
    public ResponseEntity getAll() {
        List<Usuario> listUsuarios = usuarioRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listUsuarios);
    }

    @PostMapping("/usuarios/save")
    @ResponseBody
    public ResponseEntity<?> cadastrarUsuario(@RequestBody UsuarioDTO userDTO) {
        try {
            Role role = roleRepository.findById(userDTO.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role não encontrada"));

            Usuario user = new Usuario();
            user.setEmail(userDTO.getEmail());
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
            user.setRole(role);

            return ResponseEntity.ok(usuarioRepository.save(user));

        } catch (Exception e) {
            e.printStackTrace(); // vai mostrar no console o erro real
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro: " + e.getMessage());
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        usuarioRepository.delete(usuario.get());
        return ResponseEntity.status(HttpStatus.OK).body("Usuario deletado");
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity update(@PathVariable(value = "id") Long id, @RequestBody UsuarioDTO dto) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        //vai verificar se o registro existe
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        var usuarioModel = usuario.get();
        BeanUtils.copyProperties(dto, usuarioModel);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.save(usuarioModel));
    }

    @PostMapping("/create")
    public String cadastrarViaForm(@RequestParam String username,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam Long roleId,
                                   Model model) {
        try {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role não encontrada!"));

            Usuario user = new Usuario();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(role);

            usuarioRepository.save(user);

            model.addAttribute("successMessage", "Usuário cadastro com sucesso!");
        } catch (Exception e ) {
            model.addAttribute("errorMessage", "Erro ao cadastrar: " + e.getMessage());
        }
        return "home";
    }

    @PostMapping("/login")
    public String loginViaForm(@RequestParam String email,
                               @RequestParam String password,
                               Model model) {
        Usuario user = usuarioRepository.findByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("errorMessage", "Usuário não encontrado ou senha incorreta");
            return "home";
        }

        model.addAttribute("successMessage", "Login realizado com sucesso!");
        return "home"; // ou redireciona pra outra página, ex: "dashboard"
    }

    //rota para visualizar a tela de login do HTML
    @GetMapping("/home")
    public String home() {
        return "home"; //nome do arquivo HTML
    }


}
