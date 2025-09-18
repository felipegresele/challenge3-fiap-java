    package challenge_mottu_2_semestre.challenge_mottu.model.DTO;

    import challenge_mottu_2_semestre.challenge_mottu.model.AnoMoto;
    import challenge_mottu_2_semestre.challenge_mottu.model.ModeloMoto;
    import challenge_mottu_2_semestre.challenge_mottu.model.Motoqueiro;
    import challenge_mottu_2_semestre.challenge_mottu.model.StatusMoto;

    import java.time.LocalDateTime;

    public class MotoDTO {
        private Long id;  // adicionado
        private String placa;
        private ModeloMoto modelo;
        private AnoMoto ano;
        private StatusMoto status;
        private LocalDateTime dataSaida;
        private LocalDateTime dataRetorno;
        private Long motoboyId;
        private Long galpaoId;
        private Boolean emManutencao;

        // Getters e setters
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

        public Long getMotoboyId() {
            return motoboyId;
        }

        public void setMotoboyId(Long motoboyId) {
            this.motoboyId = motoboyId;
        }

        public Long getGalpaoId() {
            return galpaoId;
        }

        public void setGalpaoId(Long galpaoId) {
            this.galpaoId = galpaoId;
        }

        public Boolean getEmManutencao() {
            return emManutencao;
        }

        public void setEmManutencao(Boolean emManutencao) {
            this.emManutencao = emManutencao;
        }
    }
