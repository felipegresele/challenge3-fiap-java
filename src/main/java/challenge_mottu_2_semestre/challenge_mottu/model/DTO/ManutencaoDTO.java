package challenge_mottu_2_semestre.challenge_mottu.model.DTO;

import challenge_mottu_2_semestre.challenge_mottu.model.PrioridadeManutencao;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

public class ManutencaoDTO {

    private String descricao;

    private PrioridadeManutencao prioridadeManutencao = PrioridadeManutencao.MEDIA;

    @PastOrPresent(message = "Data de abertura não pode ser futura")
    private LocalDateTime dataAbertura;

    @PastOrPresent(message = "Data de fechamento não pode ser futura")
    private LocalDateTime dataFechamento;

    private boolean emAndamento;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public PrioridadeManutencao getPrioridadeManutencao() {
        return prioridadeManutencao;
    }

    public void setPrioridadeManutencao(PrioridadeManutencao prioridadeManutencao) {
        this.prioridadeManutencao = prioridadeManutencao;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public boolean isEmAndamento() {
        return emAndamento;
    }

    public void setEmAndamento(boolean emAndamento) {
        this.emAndamento = emAndamento;
    }

}
