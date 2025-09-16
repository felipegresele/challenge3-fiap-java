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
    @RequestMapping("/galpoes")
    public class GalpaoRestController {

            @Autowired
            private GalpaoRepository galpaoRepository;

            @GetMapping("/listar")
            public ResponseEntity getAll() {
                List<Galpao> listaGalpao = galpaoRepository.findAll();
                return ResponseEntity.status(HttpStatus.OK).body(listaGalpao);
            }

            @PostMapping("/save")
            public ResponseEntity salvar(@RequestBody GalpaoDTO dto) {

                Galpao galpao = new Galpao();

                galpao.setNome(dto.getNome());
                galpao.setEndereco(dto.getEndereco());
                galpao.setCapacidade(dto.getCapacidade());
                galpaoRepository.save(galpao);

                return ResponseEntity.status(HttpStatus.CREATED).body(galpao);
            }

            @PutMapping("/{id}")
            public ResponseEntity editar(@PathVariable Long id,
                                                 @RequestBody GalpaoDTO dto) {
                Optional<Galpao> galpao = galpaoRepository.findById(id);
                if (galpao.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Galpão não encontrado");
                }
                var galpaoModel = galpao.get();
                BeanUtils.copyProperties(dto, galpao);

                return ResponseEntity.status(HttpStatus.OK).body(galpaoRepository.save(galpaoModel));
            }

            @DeleteMapping("/{id}")
            public ResponseEntity excluir(@PathVariable Long id) {
                Optional<Galpao> galpao = galpaoRepository.findById(id);
                if (galpao.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Galpão não encontrado");
                }
                galpaoRepository.delete(galpao.get());
                return ResponseEntity.status(HttpStatus.OK).body("Galpão deletado");
            }
        }

