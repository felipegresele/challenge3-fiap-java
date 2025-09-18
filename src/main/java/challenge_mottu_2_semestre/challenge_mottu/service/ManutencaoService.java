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

    // Listar todas as manutenções
    public List<Manutencao> listarTodos() {
        return manutencaoRepository.findAll();
    }

    // Salvar uma nova manutenção
    public Manutencao salvar(ManutencaoDTO dto) {
        Manutencao manutencao = new Manutencao();
        BeanUtils.copyProperties(dto, manutencao);

        if (dto.getMotoId() != null) {
            Optional<Moto> motoOpt = motoRepository.findById(dto.getMotoId());
            motoOpt.ifPresent(m -> {
                manutencao.setMoto(m);
                m.setEmManutencao(true); // marca a moto como em manutenção
                m.setStatus(StatusMoto.MANUTENCAO);
                motoRepository.save(m);
            });
        }

        return manutencaoRepository.save(manutencao);
    }

    // Atualizar manutenção existente
    public Manutencao atualizar(Long id, ManutencaoDTO dto) {
        Optional<Manutencao> manutencaoOpt = manutencaoRepository.findById(id);
        if (manutencaoOpt.isEmpty()) {
            throw new IllegalArgumentException("Manutenção não encontrada");
        }

        Manutencao manutencao = manutencaoOpt.get();
        BeanUtils.copyProperties(dto, manutencao);

        if (dto.getMotoId() != null) {
            Optional<Moto> motoOpt = motoRepository.findById(dto.getMotoId());
            motoOpt.ifPresent(m -> {
                manutencao.setMoto(m);
                // atualiza o status da moto de acordo com a manutenção
                m.setEmManutencao(true);
                m.setStatus(StatusMoto.MANUTENCAO);
                motoRepository.save(m);
            });
        }

        return manutencaoRepository.save(manutencao);
    }

    // Deletar manutenção
    public void deletar(Long id) {
        Optional<Manutencao> manutencaoOpt = manutencaoRepository.findById(id);
        if (manutencaoOpt.isEmpty()) {
            throw new IllegalArgumentException("Manutenção não encontrada");
        }

        Manutencao manutencao = manutencaoOpt.get();
        Moto moto = manutencao.getMoto();
        if (moto != null) {
            moto.setEmManutencao(false); // libera a moto
            moto.setStatus(StatusMoto.DISPONIVEL);
            motoRepository.save(moto);
        }

        manutencaoRepository.delete(manutencao);
    }

    // Buscar manutenção por ID
    public Optional<Manutencao> buscarPorId(Long id) {
        return manutencaoRepository.findById(id);
    }
}
