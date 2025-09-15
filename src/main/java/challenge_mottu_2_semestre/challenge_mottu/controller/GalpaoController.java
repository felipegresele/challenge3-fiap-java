package challenge_mottu_2_semestre.challenge_mottu.controller;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.GalpaoDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Galpao;
import challenge_mottu_2_semestre.challenge_mottu.repository.GalpaoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/galpoes")
public class GalpaoController {

    @Autowired
    private GalpaoRepository galpaoRepository;

    // LISTAR GALPÕES
    @GetMapping("/todos")
    public String listarGalpoes(Model model) {
        List<Galpao> galpoes = galpaoRepository.findAll();
        model.addAttribute("galpoes", galpoes);

        if (galpoes.isEmpty()) {
            model.addAttribute("mensagem", "Nenhum galpão cadastrado.");
        }

        return "galpao/listar";
    }

    // MOSTRAR FORMULÁRIO ADICIONAR
    @GetMapping("/adicionar")
    public String mostrarFormularioAdicionar() {
        return "galpao/adicionar";
    }

    // ADICIONAR GALPÃO
    @PostMapping("/adicionar")
    public String adicionarGalpao(GalpaoDTO dto, Model model) {
        Galpao galpao = new Galpao();
        galpao.setNome(dto.getNome());
        galpao.setEndereco(dto.getEndereco());
        galpao.setCapacidade(dto.getCapacidade());

        galpaoRepository.save(galpao);
        model.addAttribute("mensagem", "Galpão cadastrado com sucesso!");

        return "galpao/adicionar";
    }

    // MOSTRAR FORMULÁRIO EDITAR
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Galpao> galpao = galpaoRepository.findById(id);
        if (galpao.isPresent()) {
            model.addAttribute("galpao", galpao.get());
            return "galpao/editar";
        } else {
            model.addAttribute("mensagem", "Galpão não encontrado.");
            return "redirect:/galpoes/todos";
        }
    }

    // EDITAR GALPÃO
    @PostMapping("/editar/{id}")
    public String editarGalpao(@PathVariable Long id, GalpaoDTO dto, Model model) {
        Optional<Galpao> galpaoOptional = galpaoRepository.findById(id);
        if (galpaoOptional.isPresent()) {
            Galpao galpao = galpaoOptional.get();
            BeanUtils.copyProperties(dto, galpao);
            galpaoRepository.save(galpao);
            model.addAttribute("mensagem", "Galpão atualizado com sucesso!");
        } else {
            model.addAttribute("mensagem", "Galpão não encontrado.");
        }
        return "redirect:/galpoes/todos";
    }

    // MOSTRAR FORMULÁRIO EXCLUIR
    @GetMapping("/excluir/{id}")
    public String mostrarFormularioExcluir(@PathVariable Long id, Model model) {
        Optional<Galpao> galpao = galpaoRepository.findById(id);
        if (galpao.isPresent()) {
            model.addAttribute("galpao", galpao.get());
            return "galpao/excluir";
        } else {
            model.addAttribute("mensagem", "Galpão não encontrado.");
            return "redirect:/galpoes/todos";
        }
    }

    // EXCLUIR GALPÃO
    @PostMapping("/excluir/{id}")
    public String excluirGalpao(@PathVariable Long id, Model model) {
        Optional<Galpao> galpao = galpaoRepository.findById(id);
        if (galpao.isPresent()) {
            galpaoRepository.delete(galpao.get());
            model.addAttribute("mensagem", "Galpão excluído com sucesso!");
        } else {
            model.addAttribute("mensagem", "Galpão não encontrado.");
        }
        return "redirect:/galpoes/todos";
    }
}
