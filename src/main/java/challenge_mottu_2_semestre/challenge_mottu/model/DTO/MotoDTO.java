package challenge_mottu_2_semestre.challenge_mottu.model.DTO;

import challenge_mottu_2_semestre.challenge_mottu.model.ModeloMoto;
import challenge_mottu_2_semestre.challenge_mottu.model.StatusMoto;

public class MotoDTO {

    private String placa;
    private ModeloMoto modelo;
    private int ano;
    private StatusMoto status;
    private Long galpaoId;

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public ModeloMoto getModelo() {
        return modelo;
    }

    public void setModelo(ModeloMoto modelo) {
        this.modelo = modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public StatusMoto getStatus() {
        return status;
    }

    public void setStatus(StatusMoto status) {
        this.status = status;
    }

    public Long getGalpaoId() {
        return galpaoId;
    }

    public void setGalpaoId(Long galpaoId) {
        this.galpaoId = galpaoId;
    }

}
