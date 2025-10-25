# Simulador de Algoritmos de Substituição de Páginas

Trabalho da disciplina de Projeto de Sistema Operacional, que consiste em um simulador em Java para comparar o desempenho de diferentes algoritmos de substituição de páginas.

O programa é executado via console, solicitando ao usuário a cadeia de referência de páginas e o número de quadros (frames) de memória disponíveis.

## Algoritmos Implementados

O simulador implementa e compara 4 dos principais algoritmos de substituição de páginas:

1.  **FIFO (First-In, First-Out)**
2.  **LRU (Least Recently Used)**
3.  **Relógio (Clock / Segunda Chance)**
4.  **Ótimo (Optimal)**

## Como Executar

Existem duas formas de executar o projeto: via IDE ou via terminal.

### Opção 1: Execução via IDE (Recomendado)

1.  Clone este repositório.
2.  Abra o projeto em sua IDE de preferência (o projeto está configurado para IntelliJ IDEA, mas funciona em outras).
3.  Localize o arquivo `src/SimuladorPaginas.java`.
4.  Execute o método `main()` contido neste arquivo.

### Opção 2: Execução via Terminal

Se você tiver o JDK (Java Development Kit) instalado, pode compilar e executar o projeto manualmente.

1.  Clone o repositório:
    ```bash
    git clone git@github.com:limajpp/so-substituicao-de-paginas.git
    ```

2.  Abra o projeto e execute o método "main" da classe "SimuladorPaginas"

### Interação com o Programa

Após a execução, o programa fará duas perguntas:

1.  **Digite a cadeia de referência de páginas:**
    * Insira os números das páginas separados por espaço.
    * *Exemplo de teste curto:* `7 0 1 2 0 3 0 4`
    * *Exemplo de teste longo:* `7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1`

2.  **Digite o número de quadros (frames) de memória:**
    * Insira um número inteiro positivo.
    * *Exemplo:* `4`

O programa exibirá o passo a passo (trace) de cada algoritmo e, ao final, um resumo comparativo com o total de faltas de página de cada um.

## Autores

* Autor 1: João Pedro Cavalcante Araripe e Lima
* Autor 2: Marcus Vinícius Herbster-Ferreira
