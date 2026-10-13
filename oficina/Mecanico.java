package oficina;
import oficina.exception.DadosInvalidosException;

// TODO: implemente Mecanico - veja docs/ENUNCIADO.md 4.1
public class Mecanico {
    private String nome;
    private int semanas;
    private int horasTrabalhadas;
    private static final int LIMITE_HORAS = 44;

    public Mecanico(String nome, int semanas, int horasTrabalhadas) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new DadosInvalidosException("Nome do mecânico não pode ser nulo ou vazio.");
        }
        this.nome = nome;
        this.semanas = 0;
        this.horasTrabalhadas = 0;
    }
    public void defineHoras(int valor) {
        if (valor < 0 || valor > LIMITE_HORAS) {
            throw new DadosInvalidosException("Horas trabalhadas inválidas: " + valor);
        }
        this.horasTrabalhadas = valor;
    }
    public void defineSemanas(int valor) {
        if (valor < 0) {
            throw new DadosInvalidosException("Número de semanas inválido: " + valor);
        }
        this.semanas = valor;
    }

}
