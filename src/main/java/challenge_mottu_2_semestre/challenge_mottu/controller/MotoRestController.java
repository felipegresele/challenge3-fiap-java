package challenge_mottu_2_semestre.challenge_mottu.controller;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.GalpaoDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.DTO.MotoDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Galpao;
import challenge_mottu_2_semestre.challenge_mottu.model.Moto;
import challenge_mottu_2_semestre.challenge_mottu.repository.GalpaoRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoqueiroRepository;
import challenge_mottu_2_semestre.challenge_mottu.service.MotoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/motos")
public class MotoRestController {

    @Autowired
    private MotoService motoService;

    @GetMapping("/listar")
    public ResponseEntity<List<Moto>> listar() {
        return ResponseEntity.ok(motoService.listarTodas());
    }

    @PostMapping("/save")
    public ResponseEntity<?> salvar(@RequestBody MotoDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(motoService.salvar(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody MotoDTO dto) {
        try {
            return ResponseEntity.ok(motoService.editar(id, dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            motoService.excluir(id);
            return ResponseEntity.ok("Moto excluída com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}