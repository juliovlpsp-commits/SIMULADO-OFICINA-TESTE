# SIMULADO - OFICINA MECANICA | Sistema de Gestao de Oficina

**Disciplina:** Programacao 2 - UFCG | **Pacote:** `oficina` | **Java:** 17+
**Tipo:** Treino para simulado - contexto completamente diferente do LAB02
**Entrega:** Local (nao avaliado) | **Tempo estimado:** 2h30

> Leia TODO o enunciado antes de comecar. A avaliacao do simulado cobra exatamente o que esta aqui.

---

## 1. VISAO GERAL

Voce vai implementar o sistema de gestao da **Oficina do Seu Ze** - uma oficina mecanica de bairro que precisa controlar:

1. **Jornada dos mecanicos** (quem esta sobrecarregado)
2. **Produtividade mensal** (meta de horas)
3. **Servicos** (avaliacao do cliente por etapa)
4. **Catalogo de pecas** (estoque em buffer circular + busca)

O sistema e `CLI` puro, sem interface grafica. A classe `Oficina.java` e o ponto de entrada e testa tudo (igual `CoISA` no LAB02).

**O que muda em relacao ao LAB02:**
* Dominio totalmente novo (oficina, nao academico)
* 2 excecoes obrigatorias novas
* Validacoes com `throw` ao inves de `return` silencioso
* `CatalogoPecas` tem regra de peca unica por codigo

---

## 2. ESTRUTURA DE PASTAS - OBRIGATORIA

```
SIMULADO-OFICINA-TESTE/
├── oficina/
│   ├── Oficina.java                  # classe principal com main (VOCE EDITA)
│   ├── Mecanico.java                 # controle de jornada
│   ├── RegistroProdutividade.java    # horas produtivas vs meta
│   ├── Servico.java                  # servico com 4 avaliacoes
│   ├── Peca.java                     # peca do catalogo
│   ├── CatalogoPecas.java            # colecao circular de pecas
│   └── exception/
│       ├── DadosInvalidosException.java        # extends IllegalArgumentException
│       └── PecaExistenteException.java         # extends RuntimeException
├── docs/
│   ├── ENUNCIADO.md                  # este arquivo
│   ├── Oficina_UML.puml              # diagrama PlantUML (voce faz)
│   └── Oficina_UML.png               # imagem exportada (kroki.io)
├── README.md
└── .gitignore
```

> Nao crie pastas extras. O corretor compila com: `javac -d out oficina/exception/*.java oficina/*.java`

---

## 3. REGRAS GERAIS

| Regra | Detalhe |
|-------|---------|
| **Pacote** | Todas as classes em `package oficina;` e excecoes em `package oficina.exception;` |
| **Javadoc** | Obrigatorio em TODAS as classes e metodos publicos. Estilo: **minusculo, sem acento, sem ponto final**, 1 linha + `@param`/`@return` quando houver parametro/retorno. Ex: `/** controle de jornada do mecanico */` |
| **Estilo de codigo** | Simples, de iniciante. Nao use `Stream`, `Optional`, `var`, `Record`. Use `for` tradicional, `if/else` simples, nomes intuitivos (`pos`, `qtd`, `lista`, `chave`) |
| **Validacao** | Sempre com `throw new DadosInvalidosException("mensagem")` ou `PecaExistenteException`. Nunca `return` silencioso quando o enunciado mandar lancar excecao. Mensagem sempre em minusculo sem acento |
| **Git** | Commit unico `OFICINA - SIMULADO` |
| **Compilacao** | Tem que compilar sem `warnings` de falta de `javadoc` (pode ter warnings normais) |

---

## 4. CLASSE POR CLASSE - ESPECIFICACAO COMPLETA

### 4.1 `Mecanico.java` - Controle de jornada

**Conceito:** Controla se o mecanico esta **sobrecarregado**. A CLT diz que acima de **44h/semana** em media ja e sobrecarga.

```java
package oficina;
import oficina.exception.DadosInvalidosException;

public class Mecanico {
    private String nome;
    private int horasTrabalhadas; // total de horas no periodo
    private int semanas;          // numero de semanas do periodo
    private static final int LIMITE_HORAS = 44;
}
```

| Metodo | Assinatura | Descricao | Regra |
|--------|------------|-----------|-------|
| Construtor | `Mecanico(String nome)` | Cria mecanico com nome, `horas=0`, `semanas=0` | Se `nome == null` ou `trim().isEmpty()` -> `throw new DadosInvalidosException("nome nao pode ser vazio")` |
| `defineHoras` | `void defineHoras(int valor)` | Define total de horas | Se `valor < 0` -> `throw new DadosInvalidosException("horas nao pode ser menor que zero")` |
| `defineSemanas` | `void defineSemanas(int valor)` | Define semanas | Se `valor < 0` -> mesmo throw, mensagem `"semanas nao pode ser menor que zero"` |
| `getStatus` | `String getStatus()` | Retorna `"sobrecarregado"` se `semanas > 0 && (double)horas/semanas > 44`, senao `"disponivel"` | ⚠️ Atencao ao `(double)` e ao `semanas > 0` para evitar `Infinity` |
| `getNome` | `String getNome()` | Getter | - |
| `toString` | `String toString()` | Retorna `"nome - horas/semanas - status"` Ex: `"Ze - 90/2 - sobrecarregado"` | - |

**Exemplos:**
```java
Mecanico m = new Mecanico("Ze");
m.getStatus() // "disponivel" -> 0/0
m.defineHoras(30); m.defineSemanas(1); m.getStatus() // "disponivel" -> 30/1=30 <=44
m.defineHoras(90); m.defineSemanas(2); m.getStatus() // "sobrecarregado" -> 90/2=45 >44
m.defineHoras(-5) // lanca DadosInvalidosException
```

---

### 4.2 `RegistroProdutividade.java` - Meta de horas

**Conceito:** Cada mecanico tem meta mensal. Padrao UFCG: 160h. Oficina pequena pode ter meta custom.

```java
public class RegistroProdutividade {
    private String nome;      // nome do mecanico ou setor
    private int meta;         // horas esperadas
    private int horasFeitas;  // horas realizadas
    private static final int META_PADRAO = 160;
}
```

| Metodo | Assinatura | Descricao | Regra |
|--------|------------|-----------|-------|
| Construtor 1 | `RegistroProdutividade(String nome)` | Chama `this(nome, 160)` | - |
| Construtor 2 | `RegistroProdutividade(String nome, int meta)` | Cria registro | Se `nome` vazio/null -> `DadosInvalidosException("nome nao pode ser vazio")`. Se `meta <= 0` -> `DadosInvalidosException("meta deve ser maior que zero")` |
| `adicionaHoras` | `void adicionaHoras(int h)` | Soma horas | Se `h < 0` -> `DadosInvalidosException("horas nao pode ser menor que zero")`. Senao `horasFeitas += h` |
| `atingiuMeta` | `boolean atingiuMeta()` | `true` se `horasFeitas >= meta` | - |
| `toString` | `String toString()` | `"nome horasFeitas/meta"` Ex: `"Ze 100/160"` | - |

**Exemplos:**
```java
RegistroProdutividade r1 = new RegistroProdutividade("Ze"); // meta 160
r1.adicionaHoras(80); r1.atingiuMeta() // false
r1.adicionaHoras(80); r1.atingiuMeta() // true
RegistroProdutividade r2 = new RegistroProdutividade("Ana", 100);
r2.adicionaHoras(100); r2.toString() // "Ana 100/100"
new RegistroProdutividade("Ze", -10) // lanca DadosInvalidosException
```

---

### 4.3 `Servico.java` - Servico com avaliacoes

**Conceito:** Servico da oficina avaliado em 4 etapas (orcamento, execucao, prazo, atendimento). Cada etapa nota 0-10. Aprovado se media >= 7. Na versao **premium** o cliente da pesos diferentes.

```java
import java.util.Arrays;
public class Servico {
    private String nomeServico;
    private int horas;         // horas gastas no servico
    private double[] avaliacoes; // tamanho definido no construtor
    private int[] pesos;         // null = media simples
    private static final double MEDIA_APROVACAO = 7.0;
}
```

| Metodo | Assinatura | Descricao | Regra |
|--------|------------|-----------|-------|
| Construtor 1 | `Servico(String nome)` | `this(nome, 4)` | - |
| Construtor 2 | `Servico(String nome, int qtdAvaliacoes)` | `this(nome, qtdAvaliacoes, null)` | Se `qtdAvaliacoes <= 0` -> `DadosInvalidosException("quantidade deve ser maior que zero")` |
| Construtor 3 | `Servico(String nome, int qtdAvaliacoes, int[] pesos)` | Principal | Se `nome` vazio/null -> `DadosInvalidosException`. Cria `avaliacoes = new double[qtdAvaliacoes]`. Se `pesos != null && pesos.length == qtdAvaliacoes` faz `Arrays.copyOf(pesos, ...)`, senao `pesos = null` |
| `registraHoras` | `void registraHoras(int h)` | Soma horas | Se `h < 0` -> `DadosInvalidosException("horas nao pode ser menor que zero")` |
| `registraAvaliacao` | `void registraAvaliacao(int num, double valor)` | Registra nota. `num` 1-based (usuario passa 1..4) | Se `num <1 || num > length` -> `IllegalArgumentException("numero da avaliacao invalido")`. Se `valor <0 || valor >10` -> `IllegalArgumentException("valor da avaliacao invalido")`. Senao `avaliacoes[num-1]=valor` |
| `calculaMedia` | `private double calculaMedia()` | Media simples ou ponderada | Se `pesos==null`: `soma/length`. Se com pesos: `soma(valor*peso)/somaPesos` com protecao `somaPesos>0 ? ... : 0` |
| `aprovado` | `boolean aprovado()` | `true` se `calculaMedia() >= 7.0` | - |
| `toString` | `String toString()` | `"nome horas media [avaliacoes]"` Ex: `"Troca de oleo 5 7.5 [8.0, 7.0, 6.0, 9.0]"` | Usa `Arrays.toString(avaliacoes)` e `calculaMedia()` |

**Exemplos:**
```java
Servico s = new Servico("Troca de oleo");
s.registraHoras(5); s.registraAvaliacao(1,8); s.registraAvaliacao(2,7); s.registraAvaliacao(3,6); s.registraAvaliacao(4,9);
s.calculaMedia() // 7.5
s.aprovado() // true

// Ponderado: pesos [4,3,2,1] -> (8*4+7*3+6*2+9*1)/(10) = 7.4
Servico sp = new Servico("Motor", 4, new int[]{4,3,2,1});

s.registraAvaliacao(5, 10) // lanca IllegalArgumentException
```

---

### 4.4 `Peca.java` - Peca do catalogo

**Conceito:** Objeto de valor simples. Uma peca tem codigo unico, nome e preco.

```java
public class Peca {
    private String codigo;  // ex: "P001"
    private String nome;    // ex: "Filtro de oleo"
    private double preco;   // ex: 45.90
}
```

| Metodo | Assinatura | Descricao | Regra |
|--------|------------|-----------|-------|
| Construtor | `Peca(String codigo, String nome, double preco)` | Cria peca | Se `codigo == null || trim().isEmpty()` -> `IllegalArgumentException("codigo nao pode ser vazio")`. Mesmo para `nome`. Se `preco < 0` -> `DadosInvalidosException("preco nao pode ser menor que zero")` |
| `getCodigo` | `String getCodigo()` | Getter | - |
| `getNome` | `String getNome()` | Getter | - |
| `getPreco` | `double getPreco()` | Getter | - |
| `toString` | `String toString()` | `"codigo: nome - R$ preco"` Ex: `"P001: Filtro de oleo - R$ 45.9"` | - |

---

### 4.5 `CatalogoPecas.java` - Colecao circular (MAIS IMPORTANTE)

**Conceito:** O estoque fisico tem tamanho fixo. Quando enche, a peca mais antiga e removida para dar lugar a nova (buffer circular). Nao pode ter duas pecas com mesmo codigo (ignora ou lanca excecao).

```java
import java.util.Arrays;
import oficina.exception.PecaExistenteException;
public class CatalogoPecas {
    private Peca[] pecas;
    private int qtd;
    private int pos; // proxima posicao a escrever
}
```

| Metodo | Assinatura | Descricao | Regra |
|--------|------------|-----------|-------|
| Construtor | `CatalogoPecas(int capacidade)` | Cria array | Se `capacidade <=0` -> `IllegalArgumentException("capacidade deve ser maior que zero")` |
| `adiciona` | `void adiciona(String codigo, String nome, double preco)` | Adiciona peca | Se `temPeca(codigo)` -> `throw new PecaExistenteException("peca ja existe: " + codigo)`. Senao `pecas[pos] = new Peca(...)` `pos = (pos+1)%pecas.length` `if(qtd < length) qtd++` |
| `adicionaPeca` | `void adicionaPeca(String codigo, String nome, double preco)` | Alias que chama `adiciona(...)` | - |
| `pegaPecas` | `String[] pegaPecas()` | Retorna `toString()` de cada peca na ordem logica (mais antiga primeiro) | Usa `posReal(i)` e `pecas[posReal(i)].toString()` |
| `conta` | `int conta()` | Retorna `qtd` | - |
| `contaPecas` | `int contaPecas()` | Alias de `conta()` | - |
| `imprimePecas` | `String imprimePecas()` | `"- X peca(s) cadastrada(s)\n- cod1 \| cod2 \| cod3"` | Pega codigos via `getCodigo()` na ordem logica |
| `temPeca` | `boolean temPeca(String codigo)` | Busca por codigo `equalsIgnoreCase` | Se `codigo==null` return `false` |
| `busca` | `String[] busca(String chave)` | Busca por **nome** que contem a chave (case-insensitive), retorna **codigos** ordenados | Se `chave==null || isEmpty()` return `new String[0]`. Percorre pecas, se `peca.getNome().toLowerCase().contains(chave.toLowerCase())` adiciona `peca.getCodigo()`. Retorna ordenado com `Arrays.sort(resultado, String.CASE_INSENSITIVE_ORDER)` |
| `posReal` | `private int posReal(int posLogica)` | Converte pos logica em real | `if(qtd < pecas.length) return posLogica; return (pos + posLogica) % pecas.length;` |

**Exemplo de buffer (capacidade 2):**
```java
CatalogoPecas cat = new CatalogoPecas(2);
cat.adiciona("P001","Filtro",10); // [P001, null] qtd=1 pos=1
cat.adiciona("P002","Pastilha",20); // [P001,P002] qtd=2 pos=0 (voltou)
cat.adiciona("P003","Vela",30); // [P003,P002] qtd=2 pos=1 (sobrescreveu P001)
cat.pegaPecas() // ["P002: Pastilha - R$ 20.0", "P003: Vela - R$ 30.0"] -> ordem logica correta! P002 e mais antiga que P003
cat.adiciona("P002","Outra",99) // lanca PecaExistenteException
cat.busca("filtro") // [] (P001 foi removido)
cat.busca("past") // ["P002"]
```

**Exemplo de busca ordenada:**
```java
cat.adiciona("P010","Filtro de oleo",10);
cat.adiciona("P002","Filtro de ar",20);
cat.busca("filtro") // ["P002", "P010"] -> ordenado por codigo
```

---

### 4.6 `Oficina.java` - Classe principal com main e try/catch

Tem que testar TODAS as classes e mostrar `try/catch` funcionando. Sugestao de saida:

```java
package oficina;
import oficina.exception.DadosInvalidosException;
import oficina.exception.PecaExistenteException;

public class Oficina {
    public static void main(String[] args) {
        // 1. testa Mecanico
        // 2. testa RegistroProdutividade
        // 3. testa Servico
        // 4. testa CatalogoPecas
        // em cada um, pelo menos 1 try/catch de erro
        // ex: try{ m.defineHoras(-5); } catch(DadosInvalidosException e){ System.out.println(e.getMessage()); }
    }
}
```

**Saida esperada parcial:**
```
disponivel
sobrecarregado
horas nao pode ser menor que zero
false
true
Ze 100/160
false
true
Troca de oleo 5 7.5 [8.0, 7.0, 6.0, 9.0]
- 2 peca(s) cadastrada(s)
- P001 | P002
peca ja existe: P001
[P002]
```

---

## 5. EXCECOES

### `DadosInvalidosException.java`
```java
package oficina.exception;
public class DadosInvalidosException extends IllegalArgumentException {
    public DadosInvalidosException(String msg) { super(msg); }
}
```

### `PecaExistenteException.java`
```java
package oficina.exception;
public class PecaExistenteException extends RuntimeException {
    public PecaExistenteException(String msg) { super(msg); }
}
```

> Sao `RuntimeException` - nao precisa de `throws` na assinatura, mas se quiser por `throws DadosInvalidosException` pode, nao desconta ponto.

---

## 6. JAVADOC - MODELO EXATO

Copie o estilo do LAB02:

```java
/**
 * controle de jornada do mecanico
 */
public class Mecanico {

    /**
     * define horas trabalhadas
     * @param valor horas trabalhadas
     */
    public void defineHoras(int valor) {
```

* Tudo minusculo, sem acento, sem ponto final
* Toda classe publica tem `/** ... */` em cima
* Todo metodo publico que recebe param ou retorna algo tem `@param` e `@return`

Gere com: `javadoc -d docs/javadoc oficina/*.java oficina/exception/*.java`

---

## 7. UML

**Arquivo:** `docs/Oficina_UML.puml` (fonte) + `docs/Oficina_UML.png` (imagem dark mode)

Inclua:
* 6 classes + 2 excecoes
* Atributos e metodos com visibilidade `-`/`+`
* Relacoes: `CatalogoPecas "1" *-- "0..*" Peca : contem >`, `Oficina ..> Mecanico`, `Oficina ..> CatalogoPecas`, etc.
* Heranca: `DadosInvalidasException --|> IllegalArgumentException`
* Notes: buffer circular, media ponderada

**Gere em:** https://kroki.io ou https://plantuml.com/plantuml
```bash
curl -X POST https://kroki.io/plantuml/png -H "Content-Type: text/plain" --data-binary "@docs/Oficina_UML.puml" -o docs/Oficina_UML.png
```

Tema dark igual `LAB02` e `SGAA-TESTE`: `backgroundColor #121212`, `ClassBackgroundColor #1E1E1E`.

---

## 8. CHECKLIST DE CORRECAO (SIMULADO)

Marque antes de entregar:

- [ ] Todos os 6 arquivos compilam: `javac -d out oficina/exception/*.java oficina/*.java`
- [ ] `java -cp out oficina.Oficina` roda sem erro e mostra try/catch
- [ ] `Mecanico.getStatus()` com `semanas > 0 && (double)...` (nao `Infinity`)
- [ ] `RegistroProdutividade` com 2 construtores e `meta` nao `static`
- [ ] `Servico` com `Arrays.copyOf` e 3 construtores encadeados
- [ ] `Peca` valida `codigo/nome` com `trim().isEmpty()`
- [ ] `CatalogoPecas` com `posReal`, `alias` adicionaPeca/contaPecas, `busca` ordenada com `CASE_INSENSITIVE_ORDER`
- [ ] `posReal` com `if(qtd < length) return pos; return (pos + pos) % length;`
- [ ] 2 excecoes criadas e usadas com `throw new`
- [ ] `Oficina.main` com pelo menos 3 `try/catch`
- [ ] Javadoc em todas as classes/metodos publicos (minusculo)
- [ ] UML `.puml` + `.png` dark mode
- [ ] Nao tem `// TODO` no codigo entregue
- [ ] `out/` esta no `.gitignore`
- [ ] Commit `OFICINA - SIMULADO`

---

## 9. DICAS PARA O SIMULADO DE HOJE

1. **Faca na ordem:** `Peca` -> `Mecanico` -> `RegistroProdutividade` -> `Servico` -> `CatalogoPecas` -> `Oficina` -> `UML`
2. **Tempo:** 30min por classe. Se travar no `CatalogoPecas`, faca o resto e volta.
3. **Teste sempre:** compile a cada classe, nao deixe pra compilar tudo no fim.
4. **Nao copie e cole do LAB02:** digite entendendo. O corretor detecta copia.

**Boa pratica!**

---

*Gerado para treino - baseado no LAB02 LAB-02-EN e SGAA-TESTE - 13/10/2026*
