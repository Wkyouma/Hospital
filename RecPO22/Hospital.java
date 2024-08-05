import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Hospital {
    // Cria um dicionario para armazenar os medicos e outro para armazenar os pacientes
    // A chave do medico é o ID e a chave do Paciente é o CPF
    private static Map<Integer, Medico> medicosMap = new HashMap<>();
    private static Map<Long, Paciente> pacientesMap = new HashMap<>();

    public static void main(String[] args) {
        try {
            readPacientesCSV();
            readMedicosCSV();
            associaConsultas();

            Scanner scanner = new Scanner(System.in);
            int opcao;

            do {
                exibirMenu();
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        visualizarPacientesDoMedico(scanner);
                        break;
                    case 2:
                        visualizarConsultasTodosMedicosNoPeriodo(scanner);
                        break;
                    case 3:
                        visualizarMedicosDoPaciente(scanner);
                        break;
                    case 4:
                        visualizarConsultasPacienteMedico(scanner);
                        break;
                    case 5:
                        visualizarPacientesSemConsultarMedicoPorTempo(scanner);
                        break;
                    case 6:
                        visualizarConsultasPaciente(scanner);
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } while (opcao != 7);

            scanner.close();
        } catch (Exception e) {
            System.out.println("Ocorreu um erro: " + e.getMessage());
        }
    }

    private static void exibirMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Visualizar pacientes de um determinado médico");
        System.out.println("2. Visualizar consultas de um médico em um período específico");
        System.out.println("3. Todos os médicos que um determinado paciente já consultou ou tem consulta agendada");
        System.out.println("4. Consultas que um determinado paciente realizou com determinado médico");
        System.out.println("5. Pacientes de um determinado médico que não o consulta há mais de um determinado tempo");
        System.out.println("6. Consultas agendadas que um determinado paciente possui");
        System.out.println("7. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void visualizarConsultasPacienteMedico(Scanner scanner) {
    try {
        System.out.print("Digite o CPF do paciente: ");
        Long cpfPaciente = Long.parseLong(scanner.nextLine());

        System.out.print("Digite o código do médico: ");
        int codigoMedico = Integer.parseInt(scanner.nextLine());

        Paciente paciente = pacientesMap.get(cpfPaciente);
        Medico medico = medicosMap.get(codigoMedico);

        if (paciente != null && medico != null) {
            System.out.println("Consultas do paciente " + paciente.getNome() + " com o médico " + medico.getNome() + ":");

            List<Consulta> consultas = paciente.getConsultas().stream()
                    .filter(consulta -> consulta.getIDmedico() == codigoMedico && consulta.getData().isBefore(LocalDate.now()))
                    .collect(Collectors.toList());

            if (!consultas.isEmpty()) {
                for (Consulta consulta : consultas) {
                    System.out.println(consulta);
                }
            } else {
                System.out.println("O paciente não realizou consultas com este médico.");
            }
        } else {
            System.out.println("Paciente ou médico não encontrado.");
        }
    } catch (NumberFormatException e) {
        System.out.println("Entrada inválida. Certifique-se de digitar números para CPF e código do médico.");
    } catch (Exception e) {
        System.out.println("Ocorreu um erro: " + e.getMessage());
    }
}

private static void visualizarPacientesSemConsultarMedicoPorTempo(Scanner scanner) {
    try {
        System.out.print("Digite o código do médico: ");
        int codigoMedico = Integer.parseInt(scanner.nextLine());

        Medico medicoSelecionado = medicosMap.get(codigoMedico);
        if (medicoSelecionado != null) {
            System.out.print("Digite o número de meses desde a última consulta: ");
            int mesesLimite = Integer.parseInt(scanner.nextLine());

            LocalDate dataLimite = LocalDate.now().minus(Period.ofMonths(mesesLimite));

            System.out.println("Pacientes do médico " + medicoSelecionado.getNome() + " que não o consultam há mais de " + mesesLimite + " meses:");
            for (Paciente paciente : medicoSelecionado.getPacientes()) {
                List<Consulta> consultasDoPaciente = paciente.getConsultas();
                boolean consultou = false;
                for (Consulta consulta : consultasDoPaciente) {
                    if (consulta.getIDmedico() == codigoMedico && consulta.getData().isAfter(dataLimite)) {
                        consultou = true;
                        break;
                    }
                }
                if (!consultou) {
                    System.out.println(paciente.getNome());
                }
            }
        } else {
            System.out.println("Médico não encontrado.");
        }
    } catch (NumberFormatException e) {
        System.out.println("Entrada inválida. Certifique-se de digitar um número para o código do médico e o número de meses.");
    } catch (Exception e) {
        System.out.println("Ocorreu um erro: " + e.getMessage());
    }
}

private static void visualizarMedicosDoPaciente(Scanner scanner) {
    try {
        System.out.print("Digite o CPF do paciente: ");
        Long cpfPaciente = Long.parseLong(scanner.nextLine());

        Paciente paciente = pacientesMap.get(cpfPaciente);
        if (paciente != null) {
            System.out.println("Médicos consultados pelo paciente " + paciente.getNome() + ":");

            TreeSet<Medico> medicosConsultados = new TreeSet<>();

            for (Consulta consulta : paciente.getConsultas()) {
                Medico medico = medicosMap.get(consulta.getIDmedico());
                if (medico != null) {
                    medicosConsultados.add(medico);
                }
            }

            if (!medicosConsultados.isEmpty()) {
                for (Medico medico : medicosConsultados) {
                    System.out.println(medico.getNome());
                }
            } else {
                System.out.println("O paciente não tem consultas registradas.");
            }
        } else {
            System.out.println("Paciente não encontrado.");
        }
    } catch (NumberFormatException e) {
        System.out.println("Entrada inválida. Certifique-se de digitar um número para o CPF do paciente.");
    } catch (Exception e) {
        System.out.println("Ocorreu um erro: " + e.getMessage());
    }
}

private static void visualizarConsultasTodosMedicosNoPeriodo(Scanner scanner) {
    try {
        LocalDate dataInicio;
        LocalDate dataFim;

       
        while (true) {
            System.out.print("Digite a data de início (AAAA-MM-DD): ");
            String inputInicio = scanner.nextLine();

            try {
                dataInicio = LocalDate.parse(inputInicio);
                break; 
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Tente novamente.");
            }
        }

        while (true) {
            System.out.print("Digite a data de fim (AAAA-MM-DD): ");
            String inputFim = scanner.nextLine();

            try {
                dataFim = LocalDate.parse(inputFim);
                if (dataFim.isAfter(dataInicio)) {
                    break; // Sai do loop se a data for válida e depois da data de início
                } else {
                    System.out.println("Data de fim deve ser depois da data de início. Tente novamente.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Tente novamente.");
            }
        }

        System.out.println("Todas as consultas de todos os médicos no período de " + dataInicio + " a " + dataFim + ":");

        for (Medico medico : medicosMap.values()) {
            System.out.println("Consultas do médico " + medico.getNome() + ":");
            for (Paciente paciente : pacientesMap.values()) {
                for (Consulta consulta : paciente.getConsultas()) {
                    if (consulta.getIDmedico() == medico.getCodigo() &&
                            consulta.getData().isAfter(dataInicio.minusDays(1)) &&
                            consulta.getData().isBefore(dataFim.plusDays(1))) {
                        System.out.println(consulta);
                    }
                }
            }
            System.out.println();
        }
    } catch (Exception e) {
        System.out.println("Ocorreu um erro: " + e.getMessage());
    }
}

private static void visualizarConsultasPaciente(Scanner scanner) {
    try {
        System.out.print("Digite o CPF do paciente: ");
        Long cpfPaciente = Long.parseLong(scanner.nextLine());

        Paciente paciente = pacientesMap.get(cpfPaciente);

        if (paciente != null) {
            System.out.println("Consultas agendadas para o paciente " + paciente.getNome() + ":");

            List<Consulta> consultas = paciente.getConsultas().stream()
                    .filter(consulta -> consulta.getData().isAfter(LocalDate.now()))
                    .collect(Collectors.toList());

            if (!consultas.isEmpty()) {
                for (Consulta consulta : consultas) {
                    System.out.println(consulta);
                }
            } else {
                System.out.println("O paciente não possui consultas agendadas para o futuro.");
            }
        } else {
            System.out.println("Paciente não encontrado.");
        }
    } catch (NumberFormatException e) {
        System.out.println("Entrada inválida. Certifique-se de digitar um número para o CPF do paciente.");
    } catch (Exception e) {
        System.out.println("Ocorreu um erro: " + e.getMessage());
    }
}

private static void visualizarPacientesDoMedico(Scanner scanner) {
    try {
        System.out.print("Digite o código do médico: ");
        int codigoMedico = Integer.parseInt(scanner.nextLine());

        Medico medicoSelecionado = medicosMap.get(codigoMedico);
        if (medicoSelecionado != null) {
            System.out.println("Pacientes do médico " + medicoSelecionado.getNome() + ":");
            medicoSelecionado.listPacientes();
        } else {
            System.out.println("Médico não encontrado.");
        }
    } catch (NumberFormatException e) {
        System.out.println("Entrada inválida. Certifique-se de digitar um número para o código do médico.");
    } catch (Exception e) {
        System.out.println("Ocorreu um erro: " + e.getMessage());
    }
}

private static void readPacientesCSV() {
    try {
        File file = new File("./pacientes.csv");
        Scanner scanner = new Scanner(file); // Lendo o arquivo pacientes.csv

        while (scanner.hasNextLine()) { // Enquanto houver linhas no arquivo
            String line = scanner.nextLine(); // Lê a linha
            String[] values = line.split(","); // Separa os valores da linha por ',' e armazena em um array
            String nome = values[0];
            Long cpf = Long.parseLong(values[1]); // Convertendo o CPF para Long, usando int daria erro

            Paciente paciente = new Paciente(nome, cpf);  // Criando o objeto paciente
            pacientesMap.put(cpf, paciente);  // Adicionando o paciente ao dicionário e usando CPF como chave
            // paciente.showInfo();
        }
        scanner.close();
    } catch (FileNotFoundException e) { // Caso não encontre o arquivo
        System.out.println("Arquivo não encontrado.");
    }
}

private static void readMedicosCSV() {
    try {
        File file = new File("./medicos.csv");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] values = line.split(",");
            int intCodigo = Integer.parseInt(values[1]); // Convertendo o ID do medico para inteiro

            Medico medico = new Medico(values[0], intCodigo);
            medicosMap.put(intCodigo, medico);

            if (values.length > 2) { // Lendo o arquivo a partir do campo 2 para associar os pacientes
                for (int i = 2; i < values.length; i++) {
                    Long pacienteCpf = Long.parseLong(values[i]);
                    Paciente paciente = pacientesMap.get(pacienteCpf); // Buscando o paciente no dicionário pelo CPF
                    if (paciente != null) {
                        medico.addPaciente(paciente); // Adicionando o paciente ao médico
                    } else {
                        System.out.println("Paciente não encontrado: CPF: " + pacienteCpf);
                    }
                }
            }
        }
        scanner.close();
    } catch (FileNotFoundException e) {
        System.out.println("Arquivo não encontrado.");
    }
}

private static void associaConsultas() {
    try {
        File file = new File("./consultas.csv");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] values = line.split(",");
            LocalDate date = LocalDate.parse(values[0]); // Convertendo a data para LocalDate
            LocalTime time = LocalTime.parse(values[1]); // Convertendo o horário para LocalTime    
            int medCode = Integer.parseInt(values[2]); // Convertendo o ID do médico para inteiro
            String medNome = values[3]; // Nome do médico
            Long pacienteCpf = Long.parseLong(values[5]); // Convertendo o CPF do paciente para Long

            Medico medico = medicosMap.get(medCode); // Buscando o médico no dicionário pelo ID
            Paciente paciente = pacientesMap.get(pacienteCpf); // Buscando o paciente no dicionário pelo CPF

            if (medico != null && paciente != null) {
                Consulta consulta = new Consulta(date, time, medCode, medNome, paciente); // Criando o objeto consulta
                paciente.addConsulta(consulta); // Adicionando a consulta ao paciente
            } else {
                System.out.println("Consulta não marcada.");
            }
        }
        scanner.close();
    } catch (FileNotFoundException e) {
        System.out.println("Arquivo não encontrado.");
    }
}
}
