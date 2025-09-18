package challenge_mottu_2_semestre.challenge_mottu.controller;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.ManutencaoDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Manutencao;
import challenge_mottu_2_semestre.challenge_mottu.model.Moto;
import challenge_mottu_2_semestre.challenge_mottu.repository.ManutencaoRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoqueiroRepository;
import challenge_mottu_2_semestre.challenge_mottu.service.ManutencaoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/manutencoes")
public class ManutencaoRestController {

    @Autowired
    private ManutencaoService manutencaoService;

    // Listar todas
    @GetMapping("/listar")
    public ResponseEntity<List<Manutencao>> listarTodos() {
        return ResponseEntity.ok(manutencaoService.listarTodos());
    }

    // Salvar nova manutenção
    @PostMapping("/save")
    public ResponseEntity<?> salvar(@RequestBody ManutencaoDTO dto) {
        try {
            Manutencao manutencao = manutencaoService.salvar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(manutencao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Atualizar manutenção
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ManutencaoDTO dto) {
        try {
            Manutencao manutencao = manutencaoService.atualizar(id, dto);
            return ResponseEntity.ok(manutencao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Deletar manutenção
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            manutencaoService.deletar(id);
            return ResponseEntity.ok("Serviço de manutenção deletado");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}