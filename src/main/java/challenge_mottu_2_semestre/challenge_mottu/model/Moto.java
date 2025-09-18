package challenge_mottu_2_semestre.challenge_mottu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity(name = "motos")
@Table(name = "motos")
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placa;

    @Enumerated(EnumType.STRING)
    private ModeloMoto modelo = ModeloMoto.MOTTU_POP;

    @Enumerated(EnumType.STRING)
    private AnoMoto ano = AnoMoto.ANO_2016;

    @Enumerated(EnumType.STRING)
    private StatusMoto status = StatusMoto.DISPONIVEL;

    private LocalDateTime dataSaida;

    private LocalDateTime dataRetorno;

    private boolean emManutencao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "motoboy_id")
    private Motoqueiro motoboyEmUso;

    @ManyToOne(optional = false)
    @JoinColumn(name = "galpao_id", nullable = false)
    private Galpao galpao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public AnoMoto getAno() {
        return ano;
    }

    public void setAno(AnoMoto ano) {
        this.ano = ano;
    }

    public StatusMoto getStatus() {
        return status;
    }

    public void setStatus(StatusMoto status) {
        this.status = status;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public LocalDateTime getDataRetorno() {
        return dataRetorno;
    }

    public void setDataRetorno(LocalDateTime dataRetorno) {
        this.dataRetorno = dataRetorno;
    }

    public Motoqueiro getMotoboyEmUso() {
        return motoboyEmUso;
    }

    public void setMotoboyEmUso(Motoqueiro motoboyEmUso) {
        this.motoboyEmUso = motoboyEmUso;
    }

    public Galpao getGalpao() {
        return galpao;
    }

    public void setGalpao(Galpao galpao) {
        this.galpao = galpao;
    }

    public boolean isEmManutencao() {return emManutencao;}

    public void setEmManutencao(boolean emManutencao) {
        this.emManutencao = emManutencao;
    }

    public void atribuirMotoqueiroEmUmaMoto(Motoqueiro motoqueiro) {
        if (this.status != StatusMoto.TRANSITO) {
            throw new IllegalStateException("Só é possível atribuir um motoqueiro se a moto estiver EM_USO");
        }
        this.motoboyEmUso = motoqueiro;
    }

    public void liberarMoto() {
        this.motoboyEmUso = null;
        this.status = StatusMoto.DISPONIVEL;
    }
}
