import java.util.LinkedList;
import java.util.Queue;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Scanner;

public class SimuladorPaginas {

    public static void main(String[] args) {
        int[] cadeiaReferencias;
        int numeroDeFrames;

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("--- Simulador de Substituição de Páginas ---");
            System.out.println("Digite a cadeia de referência de páginas (ex: 7 0 1 2 0 3 0 4):");

            String linhaPaginas = scanner.nextLine();
            String[] paginasStr = linhaPaginas.split(" ");
            cadeiaReferencias = new int[paginasStr.length];
            for (int i = 0; i < paginasStr.length; i++) {
                cadeiaReferencias[i] = Integer.parseInt(paginasStr[i]);
            }

            System.out.println("Digite o número de quadros (frames) de memória (ex: 3 ou 4):");
            numeroDeFrames = scanner.nextInt();

            if (numeroDeFrames <= 0) {
                System.out.println("O número de quadros deve ser um inteiro positivo.");
                scanner.close();
                return;
            }

            System.out.println("\nIniciando simulação...");
            System.out.println("Cadeia de Páginas: " + linhaPaginas);
            System.out.println("Número de Quadros: " + numeroDeFrames);

            // Etapa 1: Simular FIFO
            int faltasFIFO = simularFIFO(cadeiaReferencias, numeroDeFrames);
            // Etapa 2: LRU
            int faltasLRU = simularLRU(cadeiaReferencias, numeroDeFrames);

            System.out.println("\n--- Resultados Finais ---");
            System.out.println("Método FIFO: " + faltasFIFO + " faltas de página");
            System.out.println("Método LRU: " + faltasLRU + " faltas de página");
        } catch (NumberFormatException e) {
            System.err.println("Erro: Entrada inválida. Por favor, insira apenas números inteiros separados por espaço.");
        } catch (Exception e) {
            System.err.println("Ocorreu um erro: " + e.getMessage());
        }
    }

    public static int simularFIFO(int[] cadeiaReferencias, int numeroQuadros) {
        Set<Integer> quadros = new HashSet<>();
        Queue<Integer> filaFIFO = new LinkedList<>();
        int faltasPagina = 0;

        System.out.println("\n--- Detalhes da Execução FIFO ---");

        for (int pagina : cadeiaReferencias) {
            System.out.print("Acessando: " + pagina);

            if (!quadros.contains(pagina)) {
                faltasPagina++;
                System.out.print(" -> Falta! ");
                if (quadros.size() == numeroQuadros) {
                    Integer paginaRemovida = filaFIFO.poll();
                    if (paginaRemovida != null) {
                        quadros.remove(paginaRemovida);
                        System.out.print("[Remove " + paginaRemovida + "] ");
                    } else {
                        System.out.print("[ERRO: A fila FIFO estava vazia quando deveria estar cheia] ");
                    }
                }
                quadros.add(pagina);
                filaFIFO.add(pagina);
                System.out.print("[Adiciona " + pagina + "]");
            } else {
                System.out.print(" -> Hit.");
            }
            System.out.println(" | Quadros: " + filaFIFO);
        }
        return faltasPagina;
    }

    public static int simularLRU(int[] cadeiaReferencias, int numeroQuadros) {
        Set<Integer> quadros = new HashSet<>();
        List<Integer> ordemUso = new LinkedList<>();
        int faltasPagina = 0;

        System.out.println("\n--- Detalhes da Execução LRU ---");

        for (int pagina : cadeiaReferencias) {
            System.out.print("Acessando: " + pagina);

            if (quadros.contains(pagina)) {
                System.out.print(" -> Hit. ");
                ordemUso.remove((Integer) pagina);
                ordemUso.add(pagina);

            } else {
                faltasPagina++;
                System.out.print(" -> Falta! ");

                if (quadros.size() == numeroQuadros) {
                    int paginaRemovida = ordemUso.removeFirst();
                    quadros.remove(paginaRemovida);
                    System.out.print("[Remove LRU " + paginaRemovida + "] ");
                }
                quadros.add(pagina);
                ordemUso.add(pagina);
                System.out.print("[Adiciona " + pagina + "]");
            }
            System.out.println(" | Quadros (LRU...MRU): " + ordemUso);
        }
        return faltasPagina;
    }
}