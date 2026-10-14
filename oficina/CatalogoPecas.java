package oficina;

import oficina.exception.PecaExistenteException;

// TODO: implemente CatalogoPecas - buffer circular - veja docs/ENUNCIADO.md 4.5
public class CatalogoPecas {
    private Peca[] pecas;
    private int qtd;
    private int pos;

    public CatalogoPecas(int capacidade){
        if (capacidade <= 0) throw new IllegalArgumentException("capacidade deve ser maior que zero");
        this.pecas = new Peca[capacidade];
        this.qtd = 0;
        this.pos = 0;
    }

    public void adiciona(String codigo, String nome, double preco){
        if (temPeca(codigo)) throw new PecaExistenteException("peca ja existe: " + codigo);
        Peca peca = new Peca(nome, preco, codigo);
        this.pecas[this.pos] = peca;
        this.pos = (this.pos + 1) % this.pecas.length;
        if (this.qtd < this.pecas.length) qtd++;
    }

    public void adicionaPecas(String codigo, String nome, double preco){
        adiciona(codigo, nome, preco);
    }

    public String[] pegaPecas(){
        String[] resultado = new String[this.qtd];
        for (int index = 0; index < this.qtd; index++) {
            resultado[index] = this.pecas[posReal(index)].toString();
        }
        return resultado;
    }

    public int conta(){
        return this.qtd;
    }

    public int contaPecas(){
        return conta();
    }

    public String imprimePecas(){
        StringBuilder sb = new StringBuilder();
        sb.append("- ").append(conta()).append(" pecas(s) cadastradas(s)\n- ");
        String[] listaPecas = pegaPecas();
        for (int i = 0; i < listaPecas.length; i++) {
            if (i > 0) sb.append("|");
            sb.append(listaPecas[i].replaceAll(":.*", ""));
        }
        return sb.toString();
    }

    public boolean temPeca(String codigo){
        if (codigo == null) return false;
        for (int i = 0; i < this.qtd; i++) {
            if (this.pecas[posReal(i)].getCodigo().equalsIgnoreCase(codigo)) return true;
        }
        return false;
    }

    public String[] busca(String chave){
        
    }

    private int posReal(int posLogica){
        if (qtd < pecas.length) return posLogica;
        return (pos + posLogica) % pecas.length;
    }
}
