package challenge_mottu_2_semestre.challenge_mottu.controller;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.ManutencaoDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Manutencao;
import challenge_mottu_2_semestre.challenge_mottu.model.Moto;
import challenge_mottu_2_semestre.challenge_mottu.repository.ManutencaoRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoqueiroRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/manutencao")
public class ManutencaoRestController {


    @Autowired
    private ManutencaoRepository manutencaoRepository;

    @GetMapping("/listar")
    public ResponseEntity getAll() {
        List<Manutencao> listaManutencoes = manutencaoRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listaManutencoes);
    }

    @PostMapping("/save")
    public ResponseEntity salvar(@RequestBody ManutencaoDTO dto) {

        Manutencao manutencao = new Manutencao();

        manutencao.setDescricao(dto.getDescricao());
        manutencao.setPrioridadeManutencao(dto.getPrioridadeManutencao());
        manutencao.setDataAbertura(dto.getDataAbertura());
        manutencao.setDataFechamento(dto.getDataFechamento());
        manutencao.setEmAndamento(dto.isEmAndamento());

        return ResponseEntity.status(HttpStatus.CREATED).body(manutencao);
    }

    @PutMapping("/{id}")
    public ResponseEntity editar(@PathVariable Long id,
                                 @RequestBody ManutencaoDTO dto) {
        Optional<Manutencao> manutencao = manutencaoRepository.findById(id);
        if (manutencao.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serviço de manutenção não encontrado");
        }
        var manutencaoModel = manutencao.get();
        BeanUtils.copyProperties(dto, manutencao);

        return ResponseEntity.status(HttpStatus.OK).body(manutencaoRepository.save(manutencaoModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable Long id) {
        Optional<Manutencao> manutencao = manutencaoRepository.findById(id);
        if (manutencao.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serviço de manutenção não encontrado");
        }
        manutencaoRepository.delete(manutencao.get());
        return ResponseEntity.status(HttpStatus.OK).body("Serviço de manutenção deletado");
    }

}
