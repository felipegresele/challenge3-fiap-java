package challenge_mottu_2_semestre.challenge_mottu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private Long id;

    private String nome;
    private String username;
    private String senha;

    @ManyToOne(fetch = FetchType.EAGER) //sempre q buscar um usuario o sistema vai trazer junto o tipo da role dele
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
