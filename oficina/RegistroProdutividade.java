package oficina;
import oficina.exception.DadosInvalidosException;

// TODO: implemente RegistroProdutividade - veja docs/ENUNCIADO.md 4.2
public class RegistroProdutividade {
    private String nome;
    private int meta;
    private int horasFeitas;
    private static final int META_PADRAO = 160;

    public RegistroProdutividade(String nome) {
        this(nome, 160);
    }
    public RegistroProdutividade(String nome, int meta) {
        this.nome = nome;
        this.meta = meta;
        this.horasFeitas = 0;
    }
public void adicionaHoras(int valor) {
        if (valor < 0) {
            throw new DadosInvalidosException("Horas adicionadas não podem ser negativas: " + valor);
        }
        this.horasFeitas += valor;
    }
