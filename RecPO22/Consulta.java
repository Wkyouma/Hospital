import java.time.LocalDate;
import java.time.LocalTime;

public class Consulta {
    private LocalDate data;
    private LocalTime horario;
    private int IDmedico;
    private String NomeMed;
    private Paciente paciente;

    public Consulta(LocalDate data, LocalTime horario, int IDmedico, String NomeMed, Paciente paciente) {
        this.data = data;
        this.horario = horario;
        this.IDmedico = IDmedico;
        this.NomeMed = NomeMed;
        this.paciente = paciente;
    }
    public String toString() {
        return "Consulta [Data=" + data + ", Horário=" + horario + ", ID do Médico=" + IDmedico + ", Nome do Médico=" + NomeMed
                + ", Paciente=" + paciente.getNome() + "]";
    }

    public String getNomeMedico(){
        return NomeMed;
    }
    
    public int getIDmedico() {
        return IDmedico;
    }


    public Paciente getPaciente() {
        return this.paciente;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHorario() {
        return horario;
    }
}