package oficina;

// TODO: implemente Oficina com main e try/catch - veja docs/ENUNCIADO.md 4.6
public class Oficina {
    public static void main(String[] args) {
    // seu codigo aqui
    Mecanico m = new Mecanico("M. Silva");
    System.out.println(m.getStatus()); // "disponivel" -> 0/0
    m.defineHoras(30); m.defineSemanas(1); System.out.println(m.getStatus()); // "disponivel" -> 30/1=30 <=44
    m.defineHoras(90); m.defineSemanas(2); System.out.println(m.getStatus()); // "sobrecarregado" -> 90/2=45 >44
    m.defineHoras(-5); // lanca DadosInvalidosException

    RegistroProdutividade r1 = new RegistroProdutividade("M. Silva"); // meta 160
    r1.adicionaHoras(80); r1.atingiuMeta();// false
    r1.adicionaHoras(80); r1.atingiuMeta();// true
    RegistroProdutividade r2 = new RegistroProdutividade("Ana", 100);
    r2.adicionaHoras(100); r2.toString(); // "Ana 100/100"
    new RegistroProdutividade("M. Silva", -10); // lanca DadosInvalidosException
    
    Servico s = new Servico("Troca de oleo");
    s.registraHoras(5); s.registraAvaliacao(1,8); s.registraAvaliacao(2,7); s.registraAvaliacao(3,6); s.registraAvaliacao(4,9);
    s.calculaMedia(); // 7.5
    s.aprovado(); // true

    // Ponderado: pesos [4,3,2,1] -> (8*4+7*3+6*2+9*1)/(10) = 7.4
    Servico sp = new Servico("Motor", 4, new int[]{4,3,2,1});

    s.registraAvaliacao(5, 10); // lanca IllegalArgumentException


    }
}
