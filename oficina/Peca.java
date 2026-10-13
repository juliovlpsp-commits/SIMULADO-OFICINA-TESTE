package oficina;
import java.util.ArrayList;
import oficina.exception.PecaExistenteException;

// TODO: implemente Peca - veja docs/ENUNCIADO.md 4.4
public class Peca {
    private String nome;
    private double preco;
    private String codigo;
    public static ArrayList<String> pecas = new ArrayList<String>();

    public Peca(String nome, double preco, String codigo) {
        if (codigo == null || codigo.isEmpty()) {
            throw new IllegalArgumentException("Código da peça não pode ser nulo ou vazio.");
        }
        if (pecas.contains(codigo)) {
            throw new PecaExistenteException("Código da peça já existe: " + codigo);
        }
        this.nome = nome;
        this.preco = preco;
        this.codigo = codigo;
    }
    public String getNome() {
        return nome;
    }
    public double getPreco() {
        return preco;
    }
    public String getCodigo() {
        return codigo;
    }
    @Override
    public String toString() {
        return codigo + " - " + nome + " - R$" + String.format("%.2f", preco);

    }
}
