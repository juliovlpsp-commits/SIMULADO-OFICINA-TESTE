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

    }
}
