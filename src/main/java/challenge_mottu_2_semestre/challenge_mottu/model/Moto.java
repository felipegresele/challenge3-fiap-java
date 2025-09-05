package challenge_mottu_2_semestre.challenge_mottu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "motos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A placa da moto é obrigatória")
    @Size(min = 7, max = 8, message = "A placa deve ter entre 7 e 8 caracteres")
    @Column(nullable = false, unique = true, length = 8)
    private String placa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModeloMoto modelo = ModeloMoto.MOTTU_POP; //definindo como padrao

    @NotNull(message = "O ano da moto é obrigatório")
    @Min(value = 2020, message = "Ano mínimo permitido é 2020")
    @Max(value = 2026, message = "Ano máximo permitido é 2026")
    private Integer ano;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) //define q esse campo na tabela do banco nao pode ser vazia como se fosse o @NotNull
    private StatusMoto status = StatusMoto.DISPONIVEL;

    @NotNull(message = "A data de saída é obrigatória")
    private LocalDateTime dataSaida;

    @NotNull(message = "A data de retorono é obrigatória")
    private LocalDateTime dataRetorno;

    @ManyToOne
    @JoinColumn(name = "motoboy_id")
    private Motoqueiro motoboyEmUso;

    @ManyToOne(optional = false)
    @JoinColumn(name = "galpao_id", nullable = false)
    private Galpao galpao;

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
