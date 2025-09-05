package challenge_mottu_2_semestre.challenge_mottu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "galpoes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Galpao {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Nome do galpão é obrigatório")
    private String nome;
    @NotNull(message = "Endereço do galpão é obrigatório")
    private String endereco;
    @NotNull(message = "Capacidade do galpão é obrigatório")
    private int capacidade;

}
