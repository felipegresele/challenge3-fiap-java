package challenge_mottu_2_semestre.challenge_mottu.controller.thymeleaf;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.ManutencaoDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Manutencao;
import challenge_mottu_2_semestre.challenge_mottu.service.ManutencaoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/manutencoes-view")
public class ManutencaoThymeleafController {

    @Autowired
    private ManutencaoService manutencaoService;

    @GetMapping("/todos")
    public String listarManutencoes(Model model) {
        List<Manutencao> manutencoes = manutencaoService.listarTodos();
        model.addAttribute("manutencoes", manutencoes);
        if (manutencoes.isEmpty()) {
            model.addAttribute("mensagem", "Nenhuma manutenção cadastrada.");
        }
        return "manutencao/listar";
    }

    @GetMapping("/adicionar")
    public String mostrarFormularioAdicionar(Model model) {
        model.addAttribute("manutencaoDTO", new ManutencaoDTO());
        return "manutencao/adicionar";
    }

    @PostMapping("/adicionar")
    public String adicionarManutencao(ManutencaoDTO dto, Model model) {
        String mensagem = validarDatas(dto);
        if (!mensagem.isEmpty()) {
            model.addAttribute("mensagem", mensagem);
            model.addAttribute("manutencaoDTO", dto);
            return "manutencao/adicionar";
        }

        // Se houver data de fechamento, a manutenção não pode estar em andamento
        if (dto.getDataFechamento() != null) {
            dto.setEmAndamento(false);
        }

        manutencaoService.salvar(dto);
        return "redirect:/manutencoes-view/todos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Manutencao manutencao = manutencaoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Manutenção não encontrada"));
        ManutencaoDTO dto = new ManutencaoDTO();
        BeanUtils.copyProperties(manutencao, dto);
        model.addAttribute("manutencaoDTO", dto);
        return "manutencao/editar";
    }

    @PostMapping("/editar/{id}")
    public String editarManutencao(@PathVariable Long id, ManutencaoDTO dto, Model model) {
        String mensagem = validarDatas(dto);
        if (!mensagem.isEmpty()) {
            model.addAttribute("mensagem", mensagem);
            model.addAttribute("manutencaoDTO", dto);
            return "manutencao/editar";
        }

        if (dto.getDataFechamento() != null) {
            dto.setEmAndamento(false);
        }

        manutencaoService.atualizar(id, dto);
        return "redirect:/manutencoes-view/todos";
    }

    @GetMapping("/excluir/{id}")
    public String mostrarFormularioExcluir(@PathVariable Long id, Model model) {
        Manutencao manutencao = manutencaoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Manutenção não encontrada"));
        model.addAttribute("manutencao", manutencao);
        return "manutencao/excluir";
    }

    @PostMapping("/excluir/{id}")
    public String excluirManutencao(@PathVariable Long id) {
        manutencaoService.deletar(id);
        return "redirect:/manutencoes-view/todos";
    }

    // Validações de datas e checkbox
    private String validarDatas(ManutencaoDTO dto) {
        LocalDateTime agora = LocalDateTime.now();

        if (dto.getDataAbertura() != null && dto.getDataAbertura().isAfter(agora)) {
            return "A data de abertura não pode ser no futuro.";
        }

        if (dto.getDataFechamento() != null && dto.getDataFechamento().isAfter(agora)) {
            return "A data de fechamento não pode ser no futuro.";
        }

        if (dto.getDataAbertura() != null && dto.getDataFechamento() != null &&
                dto.getDataAbertura().isAfter(dto.getDataFechamento())) {
            return "A data de abertura deve ser anterior à data de fechamento.";
        }

        return "";
    }
}
