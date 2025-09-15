package challenge_mottu_2_semestre.challenge_mottu.controller;

import challenge_mottu_2_semestre.challenge_mottu.model.Role;
import challenge_mottu_2_semestre.challenge_mottu.model.Usuario;
import challenge_mottu_2_semestre.challenge_mottu.repository.RoleRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AppController {

    @Autowired
    public UsuarioRepository usuarioRepository;

    @Autowired
    public RoleRepository roleRepository;

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

    @PostMapping("/doLogin")
    public String loginViaForm(@RequestParam String email,
                               @RequestParam String password,
                               Model model,
                               HttpSession session) {
        Usuario user = usuarioRepository.findByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("errorMessage", "Usuário não encontrado ou senha incorreta");
            model.addAttribute("openLoginModal", true);
            return "home";
        }

        session.setAttribute("usuarioLogado", user);

        return "redirect:/dashboard"; // redireciona direto
    }

    //rota para visualizar a tela de login do HTML
    @GetMapping("/home")
    public String home() {
        return "home"; //nome do arquivo HTML
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return "redirect:/home";
        }

        // passar o usuário para a tela
        model.addAttribute("usuario", usuario);

        return "dashboard";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }

}
