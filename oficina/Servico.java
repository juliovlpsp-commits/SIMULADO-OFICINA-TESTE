package oficina;

import java.util.Arrays;

import oficina.exception.DadosInvalidosException;

// TODO: implemente Servico - veja docs/ENUNCIADO.md 4.3
public class Servico {
    private String nomeServico;
    private int horas;
    private double[] avaliacoes;
    private int[] pesos;
    private static final double MEDIA_APROVACAO = 7.0;

    public Servico(String nome){
        this(nome, 4);
    }
    public Servico(String nome, int qtdAvalicacoes){
        this(nome, qtdAvalicacoes, null);
    }
    public Servico(String nome, int qtdAvalicoes, int[] pesos){
        this.nomeServico = nome;
        this.horas = 0;
        this.avaliacoes = new double[qtdAvalicoes];
        if (pesos != null && pesos.length == qtdAvalicoes){
            this.pesos = Arrays.copyOf(pesos, qtdAvalicoes);
        }
        this.pesos = null;
    }
    
    public void registraHoras(int h){
        if (h < 0) throw new DadosInvalidosException("hora tem que ser maior que zero.");
        this.horas += h;
    }
    public void registraAvaliacao(int num, double valor){
        if (num < 1 || num > avaliacoes.length) throw new IllegalArgumentException();
        if (valor < 1 || valor > 10) throw new IllegalArgumentException();
        this.avaliacoes[num - 1] = valor;
    }
    private double calculaMedia(){
        double soma = 0.0;
        if (this.pesos == null){
            for (double nota : avaliacoes) {
                soma += nota;
            }
            return soma / avaliacoes.length;
        }
        int somaPesos = 0;
        for (int i = 0; i < avaliacoes.length; i++) {
            soma += this.avaliacoes[i] * this.pesos[i];
            somaPesos += this.pesos[i];
        }
        return somaPesos > 0 ? soma / somaPesos : 0.0;
    }
    public boolean aprovado(){
        return calculaMedia() >= MEDIA_APROVACAO;
    }
    @Override
    public String toString(){
        return nomeServico + " " + horas + " " + calculaMedia() + " " + 
        Arrays.toString(avaliacoes);
    }
    
}
