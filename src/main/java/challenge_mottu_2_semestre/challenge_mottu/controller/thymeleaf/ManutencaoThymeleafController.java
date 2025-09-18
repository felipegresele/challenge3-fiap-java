package challenge_mottu_2_semestre.challenge_mottu.controller.thymeleaf;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.ManutencaoDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Manutencao;
import challenge_mottu_2_semestre.challenge_mottu.model.Moto;
import challenge_mottu_2_semestre.challenge_mottu.model.StatusMoto;
import challenge_mottu_2_semestre.challenge_mottu.repository.ManutencaoRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/manutencoes-view")
public class ManutencaoThymeleafController {

    @Autowired
    private ManutencaoRepository manutencaoRepository;

    @Autowired
    private MotoRepository motoRepository;

    public String formatarData(LocalDateTime data) {
        if (data == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return data.format(formatter);
    }

    @GetMapping("/todos")
    public String listarManutencoes(Model model) {
        List<Manutencao> manutencoes = manutencaoRepository.findAll();
        model.addAttribute("manutencoes", manutencoes);
        if (manutencoes.isEmpty()) {
            model.addAttribute("mensagem", "Nenhuma manutenção cadastrada.");
        }
        return "manutencao/listar";
    }

    @GetMapping("/adicionar")
    public String mostrarFormularioAdicionar(Model model) {
        model.addAttribute("manutencaoDTO", new ManutencaoDTO());
        List<StatusMoto> statusPermitidos = List.of(StatusMoto.DISPONIVEL, StatusMoto.MANUTENCAO);
        model.addAttribute("motos", motoRepository.findByStatusIn(statusPermitidos));
        return "manutencao/adicionar";
    }

    @PostMapping("/adicionar")
    public String adicionarManutencao(ManutencaoDTO dto, Model model) {
        if (dto.getDataAbertura() != null && dto.getDataAbertura().isAfter(LocalDateTime.now())) {
            model.addAttribute("mensagem", "A data de abertura não pode ser no futuro.");
            model.addAttribute("manutencaoDTO", dto);
            return "manutencao/adicionar";
        }
        Manutencao manutencao = new Manutencao();
        BeanUtils.copyProperties(dto, manutencao, "moto");
        if (dto.getMotoId() != null) {
            Moto moto = motoRepository.findById(dto.getMotoId()).orElseThrow();
            manutencao.setMoto(moto);
        }
        manutencaoRepository.save(manutencao);
        return "redirect:/manutencoes-view/todos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Manutencao manutencao = manutencaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Manutenção não encontrada"));
        ManutencaoDTO dto = new ManutencaoDTO();
        BeanUtils.copyProperties(manutencao, dto);
        if (manutencao.getMoto() != null) {
            dto.setMotoId(manutencao.getMoto().getId());
        }
        model.addAttribute("manutencaoDTO", dto);
        List<StatusMoto> statusPermitidos = List.of(StatusMoto.DISPONIVEL, StatusMoto.MANUTENCAO);
        model.addAttribute("motos", motoRepository.findByStatusIn(statusPermitidos));
        return "manutencao/editar";
    }

    @PostMapping("/editar/{id}")
    public String editarManutencao(@PathVariable Long id, ManutencaoDTO dto, Model model) {
        Manutencao manutencao = manutencaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Manutenção não encontrada"));
        BeanUtils.copyProperties(dto, manutencao, "moto");
        if (dto.getMotoId() != null) {
            Moto moto = motoRepository.findById(dto.getMotoId()).orElseThrow();
            manutencao.setMoto(moto);
        } else {
            manutencao.setMoto(null);
        }
        manutencaoRepository.save(manutencao);
        return "redirect:/manutencoes-view/todos";
    }

    @GetMapping("/excluir/{id}")
    public String mostrarFormularioExcluir(@PathVariable Long id, Model model) {
        Manutencao manutencao = manutencaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Manutenção não encontrada"));
        model.addAttribute("manutencao", manutencao);
        return "manutencao/excluir";
    }

    @PostMapping("/excluir/{id}")
    public String excluirManutencao(@PathVariable Long id, Model model) {
        Manutencao manutencao = manutencaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Manutenção não encontrada"));
        if (manutencao.getMoto() != null) {
            manutencao.getMoto().setEmManutencao(false);
            manutencao.getMoto().setStatus(StatusMoto.DISPONIVEL);
            motoRepository.save(manutencao.getMoto());
        }
        manutencaoRepository.delete(manutencao);
        return "redirect:/manutencoes-view/todos";
    }
}
