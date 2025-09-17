package challenge_mottu_2_semestre.challenge_mottu.controller.thymeleaf;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.MotoqueiroDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Motoqueiro;
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
@RequestMapping("/motoqueiros")
public class MotoqueiroThymeleafController {

        @Autowired
        private MotoqueiroRepository motoqueiroRepository;

        // LISTAR GALPÕES
        @GetMapping("/todos")
        public String listarmMotoqueiros(Model model) {
            List<Motoqueiro> motoqueiros = motoqueiroRepository.findAll();
            model.addAttribute("motoqueiros", motoqueiros);

            if (motoqueiros.isEmpty()) {
                model.addAttribute("mensagem", "Nenhum motoqueiro cadastrado.");
            }

            return "motoqueiro/listar";
        }

        // MOSTRAR FORMULÁRIO ADICIONAR
        @GetMapping("/adicionar")
        public String mostrarFormularioAdicionar() {
            return "motoqueiro/adicionar";
        }

        // ADICIONAR GALPÃO
        @PostMapping("/adicionar")
        public String adicionarMotoqueiro(MotoqueiroDTO dto, Model model) {
            Motoqueiro motoqueiro = new Motoqueiro();
            motoqueiro.setNomeCompleto(dto.getNomeCompleto());
            motoqueiro.setCpf(dto.getCpf());
            motoqueiro.setTelefone(dto.getTelefone());
            motoqueiro.setAtivo(dto.isAtivo());

            motoqueiroRepository.save(motoqueiro);
            model.addAttribute("mensagem", "Motoqueiro cadastrado com sucesso!");

            return "motoqueiro/adicionar";
        }

        // MOSTRAR FORMULÁRIO EDITAR
        @GetMapping("/editar/{id}")
        public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
            Optional<Motoqueiro> motoqueiro = motoqueiroRepository.findById(id);
            if (motoqueiro.isPresent()) {
                model.addAttribute("motoqueiro", motoqueiro.get());
                return "motoqueiro/editar";
            } else {
                model.addAttribute("mensagem", "Galpão não encontrado.");
                return "redirect:/motoqueiros/todos";
            }
        }

        // EDITAR GALPÃO
        @PostMapping("/editar/{id}")
        public String editarMotoqueiro(@PathVariable Long id, MotoqueiroDTO dto, Model model) {
            Optional<Motoqueiro> motoqueiroOptional = motoqueiroRepository.findById(id);
            if (motoqueiroOptional.isPresent()) {
                Motoqueiro motoqueiro = motoqueiroOptional.get();
                BeanUtils.copyProperties(dto, motoqueiro);
                motoqueiroRepository.save(motoqueiro);
                model.addAttribute("mensagem", "Motoqueiro atualizado com sucesso!");
            } else {
                model.addAttribute("mensagem", "Motoqueiro não encontrado.");
            }
            return "redirect:/motoqueiros/todos";
        }

        // MOSTRAR FORMULÁRIO EXCLUIR
        @GetMapping("/excluir/{id}")
        public String mostrarFormularioExcluir(@PathVariable Long id, Model model) {
            Optional<Motoqueiro> motoqueiro = motoqueiroRepository.findById(id);
            if (motoqueiro.isPresent()) {
                model.addAttribute("motoqueiro", motoqueiro.get());
                return "motoqueiro/excluir";
            } else {
                model.addAttribute("mensagem", "Motoqueiro não encontrado.");
                return "redirect:/motoqueiros/todos";
            }
        }

        // EXCLUIR GALPÃO
        @PostMapping("/excluir/{id}")
        public String excluirMotoqueiro(@PathVariable Long id, Model model) {
            Optional<Motoqueiro> motoqueiro = motoqueiroRepository.findById(id);
            if (motoqueiro.isPresent()) {
                motoqueiroRepository.delete(motoqueiro.get());
                model.addAttribute("mensagem", "Motoqueiro excluído com sucesso!");
            } else {
                model.addAttribute("mensagem", "Motoqueiro não encontrado.");
            }
            return "redirect:/motoqueiros/todos";
        }
    }
