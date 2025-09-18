package challenge_mottu_2_semestre.challenge_mottu.controller;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.MotoqueiroDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Motoqueiro;
import challenge_mottu_2_semestre.challenge_mottu.service.MotoqueiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/motoqueiros")
public class MotoqueiroRestController {

    @Autowired
    private MotoqueiroService motoqueiroService;

    @GetMapping("/listar")
    public ResponseEntity<List<Motoqueiro>> getAll() {
        List<Motoqueiro> listaMotoqueiros = motoqueiroService.listarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(listaMotoqueiros);
    }

    @PostMapping("/save")
    public ResponseEntity<Motoqueiro> cadastrarMotoqueiro(@RequestBody MotoqueiroDTO dto) {
        Motoqueiro motoqueiro = motoqueiroService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(motoqueiro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody MotoqueiroDTO dto) {
        return motoqueiroService.editar(id, dto)
                .<ResponseEntity<?>>map(m -> ResponseEntity.ok(m))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Motoqueiro não encontrado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boolean excluido = motoqueiroService.excluir(id);
        if (excluido) {
            return ResponseEntity.ok("Motoqueiro deletado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Motoqueiro não encontrado");
        }
    }
}