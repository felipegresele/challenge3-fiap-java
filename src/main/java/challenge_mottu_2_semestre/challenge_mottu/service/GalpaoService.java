package challenge_mottu_2_semestre.challenge_mottu.service;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.GalpaoDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Galpao;
import challenge_mottu_2_semestre.challenge_mottu.repository.GalpaoRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GalpaoService {

    @Autowired
    private MotoRepository motoRepository;

    private final GalpaoRepository galpaoRepository;

    public GalpaoService(GalpaoRepository galpaoRepository) {
        this.galpaoRepository = galpaoRepository;
    }

    public List<Galpao> listarTodos() {
        return galpaoRepository.findAll();
    }

    public Galpao salvar(GalpaoDTO dto) {
        Galpao galpao = new Galpao();
        galpao.setNome(dto.getNome());
        galpao.setEndereco(dto.getEndereco());
        galpao.setCapacidade(dto.getCapacidade());
        return galpaoRepository.save(galpao);
    }

    public Optional<Galpao> editar(Long id, GalpaoDTO dto) {
        Optional<Galpao> galpaoOpt = galpaoRepository.findById(id);
        if (galpaoOpt.isEmpty()) {
            return Optional.empty();
        }

        Galpao galpao = galpaoOpt.get();
        BeanUtils.copyProperties(dto, galpao, "id"); // não sobrescreve o ID
        Galpao atualizado = galpaoRepository.save(galpao);

        return Optional.of(atualizado);
    }

    public boolean excluir(Long id) {
        Optional<Galpao> galpao = galpaoRepository.findById(id);
        if (galpao.isPresent()) {
            return true;
        }

        boolean emUso = motoRepository.existsByGalpaoId(id);
        if (emUso ) {
            throw new IllegalStateException("Impossível excluir: Galpão está sendo usado em uma moto ou mais motos ");
        }

        galpaoRepository.delete(galpao.get());
        return false;
    }
}
