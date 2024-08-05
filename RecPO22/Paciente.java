import java.util.ArrayList;
import java.util.List;


public class Paciente {
    private String nome;
    private Long cpf;
    private List<Consulta> consultas;

    public Paciente(String nome, Long cpf) {
        this.nome = nome;
        this.consultas = new ArrayList<>();
        this.cpf = cpf;
    }

    public void addConsulta(Consulta consulta) {
        consultas.add(consulta);
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public String getNome() {
        return nome;
    }

    public void showInfo() {
        System.out.println("Paciente: " + this.nome + " - CPF: " + this.cpf);
    }

    public List<String> NomesMed() {
        List<String> list = new ArrayList<>();

        for (Consulta consulta : consultas) {
            if (!list.contains(consulta.getNomeMedico())) {
                list.add(consulta.getNomeMedico());
            }
        }
        System.out.println(list);
        return list;
    }

}