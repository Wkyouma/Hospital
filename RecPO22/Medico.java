import java.util.ArrayList;
import java.util.List;


public class Medico implements Comparable<Medico> {

    private String nome;
    private int codigo;
    private List<Paciente> pacientes;

    public Medico(String nome, int codigo) {
        this.nome = nome;
        this.codigo = codigo;
        this.pacientes = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }
    public List<Paciente> getPacientes() {
        return pacientes;
    }
    public int getCodigo() {
        return codigo;
    }

    public void showInfo() {
        System.out.println("Medico: " + this.getNome() + " - ID: " + this.getCodigo());
    }
    
    public void listPacientes() {
        for (Paciente paciente : pacientes) {
            paciente.showInfo();
        }
    }
    
    public void addPaciente(Paciente paciente) {
        pacientes.add(paciente);
    }

    @Override
    public int compareTo(Medico outroMedico) {
        // Implementação da comparação de médicos
        return Integer.compare(this.codigo, outroMedico.getCodigo());
    }
}
