package challenge_mottu_2_semestre.challenge_mottu.controller.thymeleaf;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.ManutencaoDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.DTO.MotoqueiroDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Manutencao;
import challenge_mottu_2_semestre.challenge_mottu.model.Motoqueiro;
import challenge_mottu_2_semestre.challenge_mottu.repository.ManutencaoRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoqueiroRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/manutencoes")
public class ManutencaoThymeleafController {

    @Autowired
    private ManutencaoRepository manutencaoRepository;

    // LISTAR GALPÕES
    @GetMapping("/todos")
    public String listarmManutencoes(Model model) {
        List<Manutencao> manutencoes = manutencaoRepository.findAll();
        model.addAttribute("manutencoes", manutencoes);

        if (manutencoes.isEmpty()) {
            model.addAttribute("mensagem", "Nenhuma manutenção cadastrada.");
        }

        return "manutencao/listar";
    }

    // MOSTRAR FORMULÁRIO ADICIONAR
    @GetMapping("/adicionar")
    public String mostrarFormularioAdicionar() {
        return "manutencao/adicionar";
    }

    // ADICIONAR GALPÃO
    @PostMapping("/adicionar")
    public String adicionarManutencao(ManutencaoDTO dto, Model model) {
        Manutencao manutencao = new Manutencao();
        manutencao.setDescricao(dto.getDescricao());
        manutencao.setPrioridadeManutencao(dto.getPrioridadeManutencao());
        manutencao.setDataAbertura(dto.getDataAbertura());
        manutencao.setDataFechamento(dto.getDataFechamento());
        manutencao.setEmAndamento(dto.isEmAndamento());

        manutencaoRepository.save(manutencao);
        model.addAttribute("mensagem", "Manutenção cadastrada com sucesso!");

        return "manutencao/adicionar";
    }

    // MOSTRAR FORMULÁRIO EDITAR
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Manutencao> manutencao = manutencaoRepository.findById(id);
        if (manutencao.isPresent()) {
            model.addAttribute("manutencao", manutencao.get());
            return "manutencao/editar";
        } else {
            model.addAttribute("mensagem", "Manutenção não encontrada.");
            return "redirect:/manutencoes/todos";
        }
    }

    // EDITAR GALPÃO
    @PostMapping("/editar/{id}")
    public String editarManutencao(@PathVariable Long id, MotoqueiroDTO dto, Model model) {
        Optional<Manutencao> manutencaoOptional = manutencaoRepository.findById(id);
        if (manutencaoOptional.isPresent()) {
            Manutencao manutencao = manutencaoOptional.get();
            BeanUtils.copyProperties(dto, manutencao);
            manutencaoRepository.save(manutencao);
            model.addAttribute("mensagem", "Manutenção atualizada com sucesso!");
        } else {
            model.addAttribute("mensagem", "Manutenção não encontrada.");
        }
        return "redirect:/manutencoes/todos";
    }

    // MOSTRAR FORMULÁRIO EXCLUIR
    @GetMapping("/excluir/{id}")
    public String mostrarFormularioExcluir(@PathVariable Long id, Model model) {
        Optional<Manutencao> manutencao = manutencaoRepository.findById(id);
        if (manutencao.isPresent()) {
            model.addAttribute("manutencao", manutencao.get());
            return "manutencao/excluir";
        } else {
            model.addAttribute("mensagem", "Manutenção não encontrada.");
            return "redirect:/manutencoes/todos";
        }
    }

    // EXCLUIR GALPÃO
    @PostMapping("/excluir/{id}")
    public String excluirManutencao(@PathVariable Long id, Model model) {
        Optional<Manutencao> manutencao = manutencaoRepository.findById(id);
        if (manutencao.isPresent()) {
            manutencaoRepository.delete(manutencao.get());
            model.addAttribute("mensagem", "Manutenção excluída com sucesso!");
        } else {
            model.addAttribute("mensagem", "Manutenção não encontrada.");
        }
        return "redirect:/manutencoes/todos";
    }

}
