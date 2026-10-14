package oficina;

// TODO: implemente CatalogoPecas - buffer circular - veja docs/ENUNCIADO.md 4.5
public class CatalogoPecas {
    private Peca[] pecas;
    private int qtd;
    private int pos;

    public CatalogoPecas(int capacidade){

    }
    public void adiciona(String codigo, String nome, double preco){

    }
    public String[] pegaPecas(){
        
    }
    public int conta(){

    }
    public int contaPecas(){
        
    }
    public String imprimePecas(){
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < qtd; i++) {
            if (i > 0) {
                resultado.append("\n");
            }
            Peca peca = pecas[posReal(i)];
            resultado.append(peca);
        }
        return resultado.toString();
    }
    public boolean temPeca(String codigo){

    }
    public String[] busca(String chave){

    }
    private int posReal(int posLogica){
        
    }
}
