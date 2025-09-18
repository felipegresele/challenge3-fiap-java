package challenge_mottu_2_semestre.challenge_mottu.controller.thymeleaf;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.ManutencaoDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.DTO.MotoqueiroDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Manutencao;
import challenge_mottu_2_semestre.challenge_mottu.model.Moto;
import challenge_mottu_2_semestre.challenge_mottu.model.Motoqueiro;
import challenge_mottu_2_semestre.challenge_mottu.repository.ManutencaoRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoqueiroRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/manutencoes-view")
public class ManutencaoThymeleafController {

    @Autowired
    private ManutencaoRepository manutencaoRepository;

    @Autowired
    private MotoRepository motoRepository;

    // Formatar datas para inputs do tipo datetime-local
    public String formatarData(LocalDateTime data) {
        if (data == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return data.format(formatter);
    }

    // LISTAR MANUTENÇÕES
    @GetMapping("/todos")
    public String listarManutencoes(Model model) {
        List<Manutencao> manutencoes = manutencaoRepository.findAll();
        model.addAttribute("manutencoes", manutencoes);

        if (manutencoes.isEmpty()) {
            model.addAttribute("mensagem", "Nenhuma manutenção cadastrada.");
        }

        return "manutencao/listar";
    }

    // MOSTRAR FORMULÁRIO ADICIONAR
    @GetMapping("/adicionar")
    public String mostrarFormularioAdicionar(Model model) {
        ManutencaoDTO dto = new ManutencaoDTO();
        model.addAttribute("manutencaoDTO", dto);

        List<Moto> motos = motoRepository.findAll();
        model.addAttribute("motos", motos);

        return "manutencao/adicionar";
    }

    // ADICIONAR MANUTENÇÃO
    @PostMapping("/adicionar")
    public String adicionarManutencao(ManutencaoDTO dto, Model model) {

        if (dto.getDataAbertura() != null && dto.getDataAbertura().isAfter(LocalDateTime.now())) {
            model.addAttribute("mensagem", "A data de abertura não pode ser no futuro.");
            model.addAttribute("manutencaoDTO", dto);
            return "manutencao/adicionar";
        }

        Manutencao manutencao = new Manutencao();
        BeanUtils.copyProperties(dto, manutencao);

        if (dto.getMotoId() != null) {
            Optional<Moto> moto = motoRepository.findById(dto.getMotoId());
            moto.ifPresent(manutencao::setMoto);
        }

        manutencaoRepository.save(manutencao);
        model.addAttribute("mensagem", "Manutenção cadastrada com sucesso!");
        return "redirect:/manutencoes-view/todos";
    }

    // MOSTRAR FORMULÁRIO EDITAR
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Manutencao> manutencao = manutencaoRepository.findById(id);
        if (manutencao.isPresent()) {
            ManutencaoDTO dto = new ManutencaoDTO();
            BeanUtils.copyProperties(manutencao.get(), dto);
            if (manutencao.get().getMoto() != null) {
                dto.setMotoId(manutencao.get().getMoto().getId());
            }
            model.addAttribute("manutencaoDTO", dto);

            List<Moto> motos = motoRepository.findAll();
            model.addAttribute("motos", motos);

            return "manutencao/editar";
        } else {
            model.addAttribute("mensagem", "Manutenção não encontrada.");
            return "redirect:/manutencoes-view/todos";
        }
    }

    // EDITAR MANUTENÇÃO
    @PostMapping("/editar/{id}")
    public String editarManutencao(@PathVariable Long id, ManutencaoDTO dto, Model model) {
        Optional<Manutencao> manutencaoOptional = manutencaoRepository.findById(id);
        if (manutencaoOptional.isPresent()) {
            Manutencao manutencao = manutencaoOptional.get();
            BeanUtils.copyProperties(dto, manutencao, "id");

            if (dto.getMotoId() != null) {
                Optional<Moto> moto = motoRepository.findById(dto.getMotoId());
                moto.ifPresent(manutencao::setMoto);
            }

            manutencaoRepository.save(manutencao);
            model.addAttribute("mensagem", "Manutenção atualizada com sucesso!");
        } else {
            model.addAttribute("mensagem", "Manutenção não encontrada.");
        }
        return "redirect:/manutencoes-view/todos";
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
            return "redirect:/manutencoes-view/todos";
        }
    }

    // EXCLUIR MANUTENÇÃO
    @PostMapping("/excluir/{id}")
    public String excluirManutencao(@PathVariable Long id, Model model) {
        Optional<Manutencao> manutencao = manutencaoRepository.findById(id);
        if (manutencao.isPresent()) {
            manutencaoRepository.delete(manutencao.get());
            model.addAttribute("mensagem", "Manutenção excluída com sucesso!");
        } else {
            model.addAttribute("mensagem", "Manutenção não encontrada.");
        }
        return "redirect:/manutencoes-view/todos";
    }
}