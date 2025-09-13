package challenge_mottu_2_semestre.challenge_mottu.controller;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.GalpaoDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Galpao;
import challenge_mottu_2_semestre.challenge_mottu.repository.GalpaoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/mottu")
public class GalpaoController {

    @Autowired
    public GalpaoRepository galpaoRepository;

    @GetMapping("/galpoes/listar")
    public ResponseEntity getAll() {
        List<Galpao> listaGalpoes = galpaoRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listaGalpoes);
    }

    @PostMapping("/galpoes/save")
    public ResponseEntity cadastrarGalpao(@RequestBody GalpaoDTO dto) {

        Galpao galpao = new Galpao();

        //pega o valor do campo da classe DTO e atribui ao valor do atributo da classe motoqueiro
       galpao.setNome(dto.getNome());
        galpao.setEndereco(dto.getEndereco());
        galpao.setCapacidade(dto.getCapacidade());

        galpaoRepository.save(galpao);
        return ResponseEntity.status(HttpStatus.OK).body(galpao);
    }

    @DeleteMapping("/galpoes/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") Long id) {
        Optional<Galpao> galpao = galpaoRepository.findById(id);
        if (galpao.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Galpão não encontrado");
        }
        galpaoRepository.delete(galpao.get());
        return ResponseEntity.status(HttpStatus.OK).body("Galpão deletado");
    }

    @PutMapping("/galpoes/{id}")
    public ResponseEntity update(@PathVariable(value = "id") Long id, @RequestBody GalpaoDTO dto) {
        Optional<Galpao> galpao = galpaoRepository.findById(id);
        if (galpao.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Galpão não encontrado");
        }
        var galpaoModel = galpao.get();
        BeanUtils.copyProperties(dto, galpaoModel);
        return ResponseEntity.status(HttpStatus.OK).body(galpaoRepository.save(galpaoModel));
    }
}
