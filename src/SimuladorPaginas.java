import java.util.LinkedList;
import java.util.Queue;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import utils.FrameRelogio;

import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

public class SimuladorPaginas {

    public static void main(String[] args) {
        int[] cadeiaReferencias;
        int numeroDeFrames;

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("--- Simulador de Substituição de Páginas (Modo Console) ---");
            System.out.println("Digite a cadeia de referência de páginas (ex: 7 0 1 2 0 3 0 4):");

            String linhaPaginas = scanner.nextLine();
            String[] paginasStr = linhaPaginas.trim().split("\\s+");
            cadeiaReferencias = new int[paginasStr.length];
            for (int i = 0; i < paginasStr.length; i++) {
                if (!paginasStr[i].isEmpty()) {
                    cadeiaReferencias[i] = Integer.parseInt(paginasStr[i]);
                }
            }

            System.out.println("Digite o número de quadros (frames) de memória (ex: 3 ou 4):");
            numeroDeFrames = scanner.nextInt();

            if (numeroDeFrames <= 0) {
                System.out.println("O número de quadros deve ser um inteiro positivo.");
                return;
            }

            System.out.println("\nIniciando simulação...");
            System.out.println("Cadeia de Páginas: " + linhaPaginas);
            System.out.println("Número de Quadros: " + numeroDeFrames);

            int faltasFIFO = simularFIFO(cadeiaReferencias, numeroDeFrames, true);
            int faltasLRU = simularLRU(cadeiaReferencias, numeroDeFrames, true);
            int faltasRelogio = simularRelogio(cadeiaReferencias, numeroDeFrames, true);
            int faltasOtimo = simularOtimo(cadeiaReferencias, numeroDeFrames, true);

            System.out.println("\n--- Resultados Finais ---");
            System.out.println("Método FIFO: " + faltasFIFO + " faltas de página");
            System.out.println("Método LRU: " + faltasLRU + " faltas de página");
            System.out.println("Método Relógio: " + faltasRelogio + " faltas de página");
            System.out.println("Método Ótimo: " + faltasOtimo + " faltas de página");

        } catch (NumberFormatException e) {
            System.err.println("Erro: Entrada inválida. Por favor, insira apenas números inteiros separados por espaço.");
        } catch (Exception e) {
            System.err.println("Ocorreu um erro: " + e.getMessage());
        }
    }

    public static int simularFIFO(int[] cadeiaReferencias, int numeroQuadros, boolean verbose) {
        Set<Integer> quadros = new HashSet<>();
        Queue<Integer> filaFIFO = new LinkedList<>();
        int faltasPagina = 0;

        if (verbose) System.out.println("\n--- Detalhes da Execução FIFO ---");

        for (int pagina : cadeiaReferencias) {
            if (verbose) System.out.print("Acessando: " + pagina);

            if (!quadros.contains(pagina)) {
                faltasPagina++;
                if (verbose) System.out.print(" -> Falta! ");
                if (quadros.size() == numeroQuadros) {
                    Integer paginaRemovida = filaFIFO.poll();
                    if (paginaRemovida != null) {
                        quadros.remove(paginaRemovida);
                        if (verbose) System.out.print("[Remove " + paginaRemovida + "] ");
                    } else {
                        if (verbose) System.out.print("[ERRO: Fila FIFO vazia] ");
                    }
                }
                quadros.add(pagina);
                filaFIFO.add(pagina);
                if (verbose) System.out.print("[Adiciona " + pagina + "]");
            } else {
                if (verbose) System.out.print(" -> Hit.");
            }
            if (verbose) System.out.println(" | Quadros: " + filaFIFO);
        }
        return faltasPagina;
    }

    public static int simularLRU(int[] cadeiaReferencias, int numeroQuadros, boolean verbose) {
        Set<Integer> quadros = new HashSet<>();
        List<Integer> ordemUso = new LinkedList<>();
        int faltasPagina = 0;

        if (verbose) System.out.println("\n--- Detalhes da Execução LRU ---");

        for (int pagina : cadeiaReferencias) {
            if (verbose) System.out.print("Acessando: " + pagina);

            if (quadros.contains(pagina)) {
                if (verbose) System.out.print(" -> Hit. ");
                ordemUso.remove((Integer) pagina);
                ordemUso.add(pagina);
            } else {
                faltasPagina++;
                if (verbose) System.out.print(" -> Falta! ");

                if (quadros.size() == numeroQuadros) {
                    int paginaRemovida = ordemUso.removeFirst();
                    quadros.remove(paginaRemovida);
                    if (verbose) System.out.print("[Remove LRU " + paginaRemovida + "] ");
                }
                quadros.add(pagina);
                ordemUso.add(pagina);
                if (verbose) System.out.print("[Adiciona " + pagina + "]");
            }
            if (verbose) System.out.println(" | Quadros (LRU...MRU): " + ordemUso);
        }
        return faltasPagina;
    }

    public static int simularRelogio(int[] cadeiaReferencias, int numeroQuadros, boolean verbose) {
        FrameRelogio[] quadros = new FrameRelogio[numeroQuadros];
        Map<Integer, Integer> mapaPaginas = new HashMap<>();
        int ponteiro = 0;
        int faltasPagina = 0;

        if (verbose) System.out.println("\n--- Detalhes da Execução Relógio ---");

        for (int pagina : cadeiaReferencias) {
            if (verbose) System.out.print("Acessando: " + pagina);

            if (mapaPaginas.containsKey(pagina)) {
                if (verbose) System.out.print(" -> Hit. ");
                int indice = mapaPaginas.get(pagina);
                quadros[indice].bitR = true;
                if (verbose) System.out.print("[Seta R=1 para pág " + pagina + "]");
            } else {
                faltasPagina++;
                if (verbose) System.out.print(" -> Falta! ");
                while (true) {
                    if (quadros[ponteiro] == null) {
                        quadros[ponteiro] = new FrameRelogio(pagina);
                        mapaPaginas.put(pagina, ponteiro);
                        if (verbose) System.out.print("[Adiciona " + pagina + " no Q" + ponteiro + "]");
                        ponteiro = (ponteiro + 1) % numeroQuadros;
                        break;
                    }
                    if (quadros[ponteiro].bitR) {
                        quadros[ponteiro].bitR = false;
                        ponteiro = (ponteiro + 1) % numeroQuadros;
                    } else {
                        int paginaRemovida = quadros[ponteiro].pagina;
                        if (verbose) System.out.print("[Remove " + paginaRemovida + " do Q" + ponteiro + "(R=0)] ");
                        mapaPaginas.remove(paginaRemovida);
                        quadros[ponteiro] = new FrameRelogio(pagina);
                        mapaPaginas.put(pagina, ponteiro);
                        if (verbose) System.out.print("[Adiciona " + pagina + " no Q" + ponteiro + "]");
                        ponteiro = (ponteiro + 1) % numeroQuadros;
                        break;
                    }
                }
            }

            if (verbose) {
                System.out.print(" | Quadros: [");
                for (int i = 0; i < numeroQuadros; i++) {
                    if (quadros[i] == null) {
                        System.out.print(" (vazio) ");
                    } else {
                        System.out.print(" Q" + i + ":" + quadros[i].toString() + " ");
                    }
                }
                System.out.println("] (Ponteiro aponta para Q" + ponteiro + ")");
            }
        }
        return faltasPagina;
    }

    public static int simularOtimo(int[] cadeiaReferencias, int numeroQuadros, boolean verbose) {
        Set<Integer> quadros = new HashSet<>();
        int faltasPagina = 0;

        if (verbose) System.out.println("\n--- Detalhes da Execução Ótimo ---");

        for (int i = 0; i < cadeiaReferencias.length; i++) {
            int pagina = cadeiaReferencias[i];
            if (verbose) System.out.print("Acessando: " + pagina);
            if (!quadros.contains(pagina)) {
                faltasPagina++;
                if (verbose) System.out.print(" -> Falta! ");
                if (quadros.size() == numeroQuadros) {
                    int paginaASubstituir = -1;
                    int maiorDistanciaFutura = -1;
                    for (int paginaNoQuadro : quadros) {
                        int distanciaAtual = Integer.MAX_VALUE;
                        for (int j = i + 1; j < cadeiaReferencias.length; j++) {
                            if (cadeiaReferencias[j] == paginaNoQuadro) {
                                distanciaAtual = j;
                                break;
                            }
                        }
                        if (distanciaAtual == Integer.MAX_VALUE) {
                            paginaASubstituir = paginaNoQuadro;
                            break;
                        }
                        if (distanciaAtual > maiorDistanciaFutura) {
                            maiorDistanciaFutura = distanciaAtual;
                            paginaASubstituir = paginaNoQuadro;
                        }
                    }
                    if (verbose) System.out.print("[Remove " + paginaASubstituir + "] ");
                    quadros.remove(paginaASubstituir);
                }
                quadros.add(pagina);
                if (verbose) System.out.print("[Adiciona " + pagina + "]");
            } else {
                if (verbose) System.out.print(" -> Hit.");
            }
            if (verbose) System.out.println(" | Quadros: " + quadros);
        }
        return faltasPagina;
    }
}
