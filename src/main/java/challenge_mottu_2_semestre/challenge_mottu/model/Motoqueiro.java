package challenge_mottu_2_semestre.challenge_mottu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

@Entity
@Table (name = "motoqueiros")
public class Motoqueiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Nome é obrigatório")
    @Size(min = 10, max = 80, message = "Nome deve ter entre 10 a 80 caracteres")
    private String nomeCompleto;

    @NotNull(message = "CPF é obrigatório")
    @Size(min = 11, max = 11, message = "CPF deve ter 11 caracteres")
    private String cpf;

    @NotNull(message = "Telefone é obrigatório")
    private String telefone;

    private boolean ativo;
}
