package challenge_mottu_2_semestre.challenge_mottu.controller;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.GalpaoDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.DTO.MotoDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Galpao;
import challenge_mottu_2_semestre.challenge_mottu.model.Moto;
import challenge_mottu_2_semestre.challenge_mottu.repository.GalpaoRepository;
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
@RequestMapping("/motos")
public class MotoRestController {

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private GalpaoRepository galpaoRepository;

    @Autowired
    private MotoqueiroRepository motoqueiroRepository;

    @GetMapping("/listar")
    public ResponseEntity getAll() {
        List<Moto> listaMotos = motoRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listaMotos);
    }

    @PostMapping("/save")
    public ResponseEntity salvar(@RequestBody MotoDTO dto) {

        Moto moto = new Moto();

        var galpao = galpaoRepository.findById(dto.getGalpaoId());
        if (galpao.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Galpão não encontrado");
        }

        moto.setPlaca(dto.getPlaca());
        moto.setModelo(dto.getModelo());
        moto.setAno(dto.getAno());
        moto.setStatus(dto.getStatus());
        moto.setGalpao(galpao.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(moto);
    }

    @PutMapping("/{id}")
    public ResponseEntity editar(@PathVariable Long id,
                                 @RequestBody MotoDTO dto) {
        Optional<Moto> moto = motoRepository.findById(id);
        if (moto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Moto não encontrado");
        }
        var motoModel = moto.get();
        BeanUtils.copyProperties(dto, moto);

        return ResponseEntity.status(HttpStatus.OK).body(motoRepository.save(motoModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable Long id) {
        Optional<Moto> moto = motoRepository.findById(id);
        if (moto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Moto não encontrado");
        }
        motoRepository.delete(moto.get());
        return ResponseEntity.status(HttpStatus.OK).body("Moto deletado");
    }

}
