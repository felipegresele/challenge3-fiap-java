package challenge_mottu_2_semestre.challenge_mottu.service;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.MotoqueiroDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Moto;
import challenge_mottu_2_semestre.challenge_mottu.model.Motoqueiro;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoqueiroRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MotoqueiroService {

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private MotoqueiroRepository motoqueiroRepository;

    public List<Motoqueiro> listarTodos() {
        return motoqueiroRepository.findAll();
    }

    public Motoqueiro salvar(MotoqueiroDTO dto) {
        Motoqueiro motoqueiro = new Motoqueiro();
        motoqueiro.setNomeCompleto(dto.getNomeCompleto());
        motoqueiro.setCpf(dto.getCpf());
        motoqueiro.setTelefone(dto.getTelefone());
        motoqueiro.setAtivo(dto.isAtivo());
        return motoqueiroRepository.save(motoqueiro);
    }

    public Optional<Motoqueiro> editar(Long id, MotoqueiroDTO dto) {
        Optional<Motoqueiro> motoqueiroOpt = motoqueiroRepository.findById(id);
        if (motoqueiroOpt.isPresent()) {
            Motoqueiro motoqueiro = motoqueiroOpt.get();
            BeanUtils.copyProperties(dto, motoqueiro);
            return Optional.of(motoqueiroRepository.save(motoqueiro));
        }
        return Optional.empty();
    }

    public boolean excluir(Long id) {
        Optional<Motoqueiro> motoqueiroOpt = motoqueiroRepository.findById(id);

        if (motoqueiroOpt.isEmpty()) {
            return false;
        }

        boolean emUso = motoRepository.existsByMotoboyEmUsoId(id);
        if (emUso) {
            throw new IllegalStateException("Impossível excluir: Motoqueiro está sendo usado em uma moto.");
        }

        // Só deleta se não estiver em uso
        motoqueiroRepository.delete(motoqueiroOpt.get());
        return true;
    }

}