package challenge_mottu_2_semestre.challenge_mottu.service;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.MotoDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Galpao;
import challenge_mottu_2_semestre.challenge_mottu.model.Moto;
import challenge_mottu_2_semestre.challenge_mottu.model.Motoqueiro;
import challenge_mottu_2_semestre.challenge_mottu.model.StatusMoto;
import challenge_mottu_2_semestre.challenge_mottu.repository.GalpaoRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoRepository;
import challenge_mottu_2_semestre.challenge_mottu.repository.MotoqueiroRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MotoService {

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private GalpaoRepository galpaoRepository;

    @Autowired
    private MotoqueiroRepository motoqueiroRepository;

    public List<Moto> listarTodas() {
        return motoRepository.findAll();
    }

    public Moto salvar(MotoDTO dto) throws Exception {
        Moto moto = new Moto();
        BeanUtils.copyProperties(dto, moto);

        // Galpão
        if (dto.getGalpaoId() != null) {
            Galpao galpao = galpaoRepository.findById(dto.getGalpaoId())
                    .orElseThrow(() -> new Exception("Galpão não encontrado"));
            moto.setGalpao(galpao);
        }

        // Motoqueiro
        if (dto.getStatus() == StatusMoto.TRANSITO) {
            if (dto.getMotoboyId() == null) throw new Exception("Moto em trânsito precisa ter um motoqueiro!");
            Motoqueiro motoqueiro = motoqueiroRepository.findById(dto.getMotoboyId())
                    .orElseThrow(() -> new Exception("Motoqueiro não encontrado"));
            moto.setMotoboyEmUso(motoqueiro);
        } else {
            moto.setMotoboyEmUso(null);
        }

        validarStatusEHorarios(moto);

        return motoRepository.save(moto);
    }

    public Moto editar(Long id, MotoDTO dto) throws Exception {
        Moto moto = motoRepository.findById(id).orElseThrow(() -> new Exception("Moto não encontrada"));
        BeanUtils.copyProperties(dto, moto);

        // Galpão
        if (dto.getGalpaoId() != null) {
            galpaoRepository.findById(dto.getGalpaoId()).ifPresent(moto::setGalpao);
        }

        // Motoqueiro
        if (dto.getStatus() == StatusMoto.TRANSITO) {
            if (dto.getMotoboyId() == null) throw new Exception("Moto em trânsito precisa ter um motoqueiro!");
            Motoqueiro motoqueiro = motoqueiroRepository.findById(dto.getMotoboyId())
                    .orElseThrow(() -> new Exception("Motoqueiro não encontrado"));
            moto.setMotoboyEmUso(motoqueiro);
        } else {
            moto.setMotoboyEmUso(null);
        }

        validarStatusEHorarios(moto);

        return motoRepository.save(moto);
    }

    public void excluir(Long id) throws Exception {
        Moto moto = motoRepository.findById(id).orElseThrow(() -> new Exception("Moto não encontrada"));
        motoRepository.delete(moto);
    }

    public Optional<Moto> buscarPorId(Long id) {
        return motoRepository.findById(id);
    }

    private void validarStatusEHorarios(Moto moto) throws Exception {
        StatusMoto status = moto.getStatus();
        if (status == null) throw new Exception("Status da moto não pode ser nulo");

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime saida = moto.getDataSaida();
        LocalDateTime retorno = moto.getDataRetorno();

        switch (status) {
            case DISPONIVEL:
                // Pode não ter datas
                if ((saida != null && retorno == null) || (saida == null && retorno != null)) {
                    throw new Exception("Se uma data for preenchida, ambas devem ser preenchidas.");
                }

                if (saida != null && saida.isAfter(agora)) {
                    throw new Exception("Data de saída não pode ser futura.");
                }

                if (retorno != null && retorno.isAfter(agora)) {
                    throw new Exception("Data de retorno não pode ser futura.");
                }

                if (saida != null && retorno != null && saida.isAfter(retorno)) {
                    throw new Exception("Data de saída deve ser menor que a data de retorno.");
                }

                moto.setEmManutencao(false);
                break;

            case TRANSITO:
                if (saida == null) {
                    throw new Exception("Moto em trânsito precisa ter data de saída e retorno.");
                }

                if (retorno != null ) {
                    throw new Exception("Data de retorno deve ser vazia, o motoqueiro ainda está em trânsito.");
                }

                if (saida.isAfter(agora)) {
                    throw new Exception("Data de saída não pode ser futura.");
                }

                if (moto.getMotoboyEmUso() != null) {
                    boolean motoqueiroEmUso = motoRepository.existsByMotoboyEmUsoAndStatus(
                            moto.getMotoboyEmUso(), StatusMoto.TRANSITO);
                    if (motoqueiroEmUso) {
                        throw new Exception("Este motoqueiro já esta em trânsito com outra moto!");
                    }
                }

                moto.setEmManutencao(false);
                break;

            case MANUTENCAO:
                if (saida != null || retorno != null) {
                    throw new Exception("Moto em manutenção não pode ter datas de saída ou retorno.");
                }
                moto.setEmManutencao(true);
                moto.setMotoboyEmUso(null);
                break;

            default:
                throw new Exception("Status da moto inválido");
        }
    }
}