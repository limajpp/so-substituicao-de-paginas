import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SimuladorGUI {

    private final JFrame frame;
    private final JTextField campoCadeia;
    private final JTextField campoQuadros;
    private final JTextArea areaResultados;

    public SimuladorGUI() {
        frame = new JFrame("Simulador de Substituição de Páginas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(550, 450);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel painelEntradas = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        painelEntradas.setBorder(BorderFactory.createTitledBorder("Entradas da Simulação"));

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        painelEntradas.add(new JLabel("Cadeia de Páginas:"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        campoCadeia = new JTextField("7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1");
        painelEntradas.add(campoCadeia, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        painelEntradas.add(new JLabel("Número de Quadros:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        campoQuadros = new JTextField("4");
        painelEntradas.add(campoQuadros, c);

        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        JButton botaoSimular = new JButton("Simular");
        painelEntradas.add(botaoSimular, c);

        areaResultados = new JTextArea();
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaResultados.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaResultados);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados"));

        frame.add(painelEntradas, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        botaoSimular.addActionListener(e -> executarSimulacao());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void executarSimulacao() {
        try {
            String textoCadeia = campoCadeia.getText().trim();
            int numeroQuadros = Integer.parseInt(campoQuadros.getText().trim());

            if (numeroQuadros <= 0) {
                JOptionPane.showMessageDialog(frame, "O número de quadros deve ser positivo.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] paginasStr = textoCadeia.split(" ");
            ArrayList<Integer> paginasList = new ArrayList<>();
            for (String s : paginasStr) {
                if (!s.isEmpty()) {
                    paginasList.add(Integer.parseInt(s));
                }
            }
            int[] cadeiaReferencias = paginasList.stream().mapToInt(i -> i).toArray();

            if (cadeiaReferencias.length == 0) {
                JOptionPane.showMessageDialog(frame, "A cadeia de páginas não pode estar vazia.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            areaResultados.setText("");

            areaResultados.append("Iniciando simulação com:\n");
            areaResultados.append("- Cadeia: " + Arrays.toString(cadeiaReferencias) + "\n");
            areaResultados.append("- Quadros: " + numeroQuadros + "\n");
            areaResultados.append("-------------------------------------------------\n\n");

            int faltasFIFO = SimuladorPaginas.simularFIFO(cadeiaReferencias, numeroQuadros, true);
            areaResultados.append(String.format("%-15s: %d faltas de página\n", "Método FIFO", faltasFIFO));

            int faltasLRU = SimuladorPaginas.simularLRU(cadeiaReferencias, numeroQuadros, true);
            areaResultados.append(String.format("%-15s: %d faltas de página\n", "Método LRU", faltasLRU));

            int faltasRelogio = SimuladorPaginas.simularRelogio(cadeiaReferencias, numeroQuadros, true);
            areaResultados.append(String.format("%-15s: %d faltas de página\n", "Método Relógio", faltasRelogio));

            int faltasOtimo = SimuladorPaginas.simularOtimo(cadeiaReferencias, numeroQuadros, true);
            areaResultados.append(String.format("%-15s: %d faltas de página\n", "Método Ótimo", faltasOtimo));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Entrada inválida. Verifique os números digitados.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Ocorreu um erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimuladorGUI::new);
    }
}
