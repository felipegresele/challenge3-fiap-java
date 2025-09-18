package challenge_mottu_2_semestre.challenge_mottu.service;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.ManutencaoDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Manutencao;
import challenge_mottu_2_semestre.challenge_mottu.model.Moto;
import challenge_mottu_2_semestre.challenge_mottu.model.StatusMoto;
import challenge_mottu_2_semestre.challenge_mottu.repository.ManutencaoRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManutencaoService {

    @Autowired
    private ManutencaoRepository manutencaoRepository;

    @Autowired
    private MotoRepository motoRepository;

    public List<Manutencao> listarTodos() {
        return manutencaoRepository.findAll();
    }

    public Manutencao salvar(ManutencaoDTO dto) {
        Manutencao manutencao = new Manutencao();
        BeanUtils.copyProperties(dto, manutencao, "moto"); // ignorando moto
        if (dto.getMotoId() != null) {
            Moto moto = motoRepository.findById(dto.getMotoId())
                    .orElseThrow(() -> new IllegalArgumentException("Moto não encontrada"));
            manutencao.setMoto(moto);
            moto.setEmManutencao(true);
            moto.setStatus(StatusMoto.MANUTENCAO);
            motoRepository.save(moto);
        }
        return manutencaoRepository.save(manutencao);
    }

    public Manutencao atualizar(Long id, ManutencaoDTO dto) {
        Manutencao manutencao = manutencaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Manutenção não encontrada"));
        BeanUtils.copyProperties(dto, manutencao, "moto"); // ignorando moto
        if (dto.getMotoId() != null) {
            Moto moto = motoRepository.findById(dto.getMotoId())
                    .orElseThrow(() -> new IllegalArgumentException("Moto não encontrada"));
            manutencao.setMoto(moto);
            moto.setEmManutencao(true);
            moto.setStatus(StatusMoto.MANUTENCAO);
            motoRepository.save(moto);
        } else {
            manutencao.setMoto(null);
        }
        return manutencaoRepository.save(manutencao);
    }

    public void deletar(Long id) {
        Manutencao manutencao = manutencaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Manutenção não encontrada"));
        Moto moto = manutencao.getMoto();
        if (moto != null) {
            moto.setEmManutencao(false);
            moto.setStatus(StatusMoto.DISPONIVEL);
            motoRepository.save(moto);
        }
        manutencaoRepository.delete(manutencao);
    }

    public Optional<Manutencao> buscarPorId(Long id) {
        return manutencaoRepository.findById(id);
    }
}
