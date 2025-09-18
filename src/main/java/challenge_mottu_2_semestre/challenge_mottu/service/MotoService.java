package challenge_mottu_2_semestre.challenge_mottu.service;

import challenge_mottu_2_semestre.challenge_mottu.model.DTO.MotoDTO;
import challenge_mottu_2_semestre.challenge_mottu.model.Galpao;
import challenge_mottu_2_semestre.challenge_mottu.model.Moto;
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

        if (dto.getGalpaoId() != null) {
            Optional<Galpao> galpao = galpaoRepository.findById(dto.getGalpaoId());
            if (galpao.isEmpty()) throw new Exception("Moto não encontrado");
            moto.setGalpao(galpao.get());
        }

        if (dto.getMotoboyEmUso() != null) {
            moto.setMotoboyEmUso(dto.getMotoboyEmUso());
        }

        validarStatusEHorarios(moto);

        return motoRepository.save(moto);
    }

    public Moto editar(Long id, MotoDTO dto) throws Exception {
        Optional<Moto> motoOptional = motoRepository.findById(id);
        if (motoOptional.isEmpty()) throw new Exception("Moto não encontrada");

        Moto moto = motoOptional.get();
        BeanUtils.copyProperties(dto, moto);

        if (dto.getGalpaoId() != null) {
            galpaoRepository.findById(dto.getGalpaoId()).ifPresent(moto::setGalpao);
        }

        if (dto.getMotoboyEmUso() != null) {
            moto.setMotoboyEmUso(dto.getMotoboyEmUso());
        }

        validarStatusEHorarios(moto);

        return motoRepository.save(moto);
    }

    public void excluir(Long id) throws Exception {
        Optional<Moto> motoOptional = motoRepository.findById(id);
        if (motoOptional.isEmpty()) throw new Exception("Moto não encontrada");
        motoRepository.delete(motoOptional.get());
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
                if (saida != null && saida.isAfter(agora))
                    throw new Exception("Horário de saída não pode ser maior que o horário atual");
                if (retorno != null && retorno.isAfter(agora))
                    throw new Exception("Horário de retorno não pode ser maior que o horário atual");
                if (saida != null && retorno != null && saida.isAfter(retorno))
                    throw new Exception("Horário de saída não pode ser maior que o horário de retorno");
                if (saida != null && retorno != null && retorno.isAfter(saida.plusDays(2)))
                    throw new Exception("Horário de retorno não pode exceder 2 dias após a saída");
                moto.setEmManutencao(false);
                break;

            case TRANSITO:
                if (saida == null) throw new Exception("Moto em trânsito precisa ter horário de saída");
                if (saida.isAfter(agora)) throw new Exception("Horário de saída não pode ser maior que o horário atual");
                if (retorno != null) throw new Exception("Moto em trânsito não pode ter horário de retorno");
                moto.setDataRetorno(null);
                moto.setEmManutencao(false);
                break;

            case MANUTENCAO:
                if (saida != null || retorno != null)
                    throw new Exception("Moto em manutenção não pode ter horários de saída ou retorno");
                moto.setEmManutencao(true);
                break;

            default:
                throw new Exception("Status da moto inválido");
        }
    }
}