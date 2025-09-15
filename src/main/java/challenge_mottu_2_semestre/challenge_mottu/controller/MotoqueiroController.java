package challenge_mottu_2_semestre.challenge_mottu.controller;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.MotoqueiroDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Moto;
import challenge_mottu_2_semestre.challenge_mottu.model.Motoqueiro;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoqueiroRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class MotoqueiroController {

    @Autowired
    public MotoqueiroRepository motoqueiroRepository;

    @GetMapping("/motoqueiros/listar")
    public ResponseEntity getAll() {
        List<Motoqueiro> listaMotoqueiros = motoqueiroRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listaMotoqueiros);
    }

    @PostMapping("/motoqueiros/save")
    public ResponseEntity cadastrarMotoqueiro(@RequestBody MotoqueiroDTO dto) {

        Motoqueiro motoqueiro = new Motoqueiro();

        //pega o valor do campo da classe DTO e atribui ao valor do atributo da classe motoqueiro
        motoqueiro.setNomeCompleto(dto.getNomeCompleto());
        motoqueiro.setCpf(dto.getCpf());
        motoqueiro.setTelefone(dto.getTelefone());
        motoqueiro.setAtivo(dto.isAtivo());

        motoqueiroRepository.save(motoqueiro);
        return ResponseEntity.status(HttpStatus.OK).body(motoqueiro);
    }

    @DeleteMapping("/motoqueiros/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") Long id) {
        Optional<Motoqueiro> motoqueiro = motoqueiroRepository.findById(id);
        if (motoqueiro.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Motoqueiro não encontrado");
        }
        motoqueiroRepository.delete(motoqueiro.get());
        return ResponseEntity.status(HttpStatus.OK).body("Motoqueiro deletado");
    }

    @PutMapping("/motoqueiros/{id}")
    public ResponseEntity update(@PathVariable(value = "id") Long id, @RequestBody MotoqueiroDTO dto) {
        Optional<Motoqueiro> motoqueiro = motoqueiroRepository.findById(id);
        if (motoqueiro.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Motoqueiro não encontrado");
        }
        var motoqueiroModel = motoqueiro.get();
        BeanUtils.copyProperties(dto, motoqueiroModel);
        return ResponseEntity.status(HttpStatus.OK).body(motoqueiroRepository.save(motoqueiroModel));
    }



}
