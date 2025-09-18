package challenge_mottu_2_semestre.challenge_mottu.controller.thymeleaf;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.MotoDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Galpao;
import challenge_mottu_2_semestre.challenge_mottu.model.Moto;
import challenge_mottu_2_semestre.challenge_mottu.repository.GalpaoRepository;
import challenge_mottu_2_semestre.challenge_mottu.service.MotoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/motos-view")
public class MotoThymeleafController {

    @Autowired
    private MotoService motoService;

    @Autowired
    private GalpaoRepository galpaoRepository;

    // LISTAR
    @GetMapping("/todos")
    public String listar(Model model) {
        List<Moto> motos = motoService.listarTodas();
        model.addAttribute("motos", motos);
        if (motos.isEmpty()) model.addAttribute("mensagem", "Nenhuma moto cadastrada");
        return "moto/listar";
    }

    // ADICIONAR
    @GetMapping("/adicionar")
    public String mostrarFormularioAdicionar(Model model) {
        MotoDTO dto = new MotoDTO();
        model.addAttribute("motoDTO", dto);
        model.addAttribute("galpoes", galpaoRepository.findAll());
        return "moto/adicionar";
    }

    @PostMapping("/adicionar")
    public String adicionar(@ModelAttribute MotoDTO dto, Model model) {
        try {
            motoService.salvar(dto);
            model.addAttribute("mensagemSucesso", "Moto cadastrada com sucesso!");
            return "redirect:/motos-view/todos";
        } catch (Exception e) {
            model.addAttribute("mensagemErro", e.getMessage());
            model.addAttribute("motoDTO", dto);
            model.addAttribute("galpoes", galpaoRepository.findAll());
            return "moto/adicionar";
        }
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Moto> moto = motoService.buscarPorId(id);
        if (moto.isEmpty()) return "redirect:/motos-view/todos";

        MotoDTO dto = new MotoDTO();
        BeanUtils.copyProperties(moto.get(), dto);

        dto.setId(moto.get().getId());
        dto.setEmManutencao(moto.get().isEmManutencao());
        if (moto.get().getGalpao() != null) {
            dto.setGalpaoId(moto.get().getGalpao().getId());
        }

        model.addAttribute("motoDTO", dto);
        model.addAttribute("galpoes", galpaoRepository.findAll());
        return "moto/editar";
    }

    @PostMapping("/editar/{id}")
    public String editar(@PathVariable Long id, @ModelAttribute MotoDTO dto, Model model) {
        try {
            motoService.editar(id, dto);
            return "redirect:/motos-view/todos";
        } catch (Exception e) {
            model.addAttribute("mensagemErro", e.getMessage());
            model.addAttribute("motoDTO", dto);
            model.addAttribute("galpoes", galpaoRepository.findAll());
            return "moto/editar";
        }
    }

    // EXCLUIR
    @GetMapping("/excluir/{id}")
    public String mostrarFormularioExcluir(@PathVariable Long id, Model model) {
        Optional<Moto> moto = motoService.buscarPorId(id);
        if (moto.isEmpty()) return "redirect:/motos-view/todos";
        model.addAttribute("moto", moto.get());
        return "moto/excluir";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, Model model) {
        try {
            motoService.excluir(id);
        } catch (Exception e) {
            model.addAttribute("mensagem", e.getMessage());
        }
        return "redirect:/motos-view/todos";
    }
}
