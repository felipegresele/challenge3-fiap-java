package challenge_mottu_2_semestre.challenge_mottu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "manutencao")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Manutencao {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Descrição é obrigatório")
    @Size(min = 10, max = 300, message = "A descrição deve ter entre 10 a 300 caracteres")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @JoinColumn(nullable = false) //nao pode ser nulo este campo na tabela
    private PrioridadeManutencao prioridadeManutencao = PrioridadeManutencao.MEDIA;

    @NotNull(message = "Data de abertura é obrigatória")
    @PastOrPresent(message = "Data de abertura não pode ser futura")
    private LocalDateTime dataAbertura;

    @PastOrPresent(message = "Data de fechamento não pode ser futura")
    private LocalDateTime dataFechamento;

    private boolean emAndamento;
}
