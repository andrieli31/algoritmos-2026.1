package ui;

import javax.swing.*;
import java.awt.*;

import busca.Buscador;
import indexacao.Indexador;
import lista.ListaEncadeada;
import mapa.MapaDispersao;
import mapa.NoLista;
import persistencia.Persistencia;

public class TelaPrincipal extends JFrame {

    private static final int TAMANHO_MAPA = 10000;

    private JTextField campoBusca;
    private JTextArea areaResultado;
    private JButton botaoBuscar;

    private MapaDispersao<ListaEncadeada<String>,
                          String> indice;
    private Buscador buscador;

    public TelaPrincipal() {

        setTitle("Pesquisa de Arquivos");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        // ── Componentes ──────────────────────────
        campoBusca = new JTextField();
        botaoBuscar = new JButton("Buscar");
        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        JScrollPane scroll =
            new JScrollPane(areaResultado);

        // Painel superior: campo + botão buscar
        JPanel painelTopo =
            new JPanel(new BorderLayout(5, 0));
        painelTopo.setBorder(
            BorderFactory.createEmptyBorder(5,5,0,5));
        painelTopo.add(campoBusca,
                       BorderLayout.CENTER);
        painelTopo.add(botaoBuscar,
                       BorderLayout.EAST);

        // Painel de ações de indexação
        JButton botaoIndexar =
            new JButton("Indexar Diretório");
        JButton botaoReindexar =
            new JButton("Reindexar");
        JLabel labelStatus =
            new JLabel("Nenhum índice carregado.");

        JPanel painelAcoes =
            new JPanel(new FlowLayout(
                FlowLayout.LEFT, 5, 2));
        painelAcoes.add(botaoIndexar);
        painelAcoes.add(botaoReindexar);
        painelAcoes.add(labelStatus);

        JPanel painelNorte = new JPanel();
        painelNorte.setLayout(
            new BoxLayout(painelNorte,
                          BoxLayout.Y_AXIS));
        painelNorte.add(painelTopo);
        painelNorte.add(painelAcoes);

        add(painelNorte, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // ── Inicialização do índice ───────────────
        if (Persistencia.indiceExiste()) {
            indice = Persistencia.carregar();
            if (indice != null) {
                buscador = new Buscador(indice);
                labelStatus.setText(
                    "Índice carregado do disco.");
            }
        } else {
            labelStatus.setText(
                "Nenhum índice. Clique em "
                + "\"Indexar Diretório\".");
        }

        // ── Eventos ──────────────────────────────
        botaoBuscar.addActionListener(
            e -> buscar(labelStatus));

        campoBusca.addActionListener(
            e -> buscar(labelStatus));

        botaoIndexar.addActionListener(e -> {
            JFileChooser chooser =
                new JFileChooser();
            chooser.setFileSelectionMode(
                JFileChooser.DIRECTORIES_ONLY);

            int ret = chooser.showOpenDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                String caminho = chooser
                    .getSelectedFile()
                    .getAbsolutePath();

                labelStatus.setText(
                    "Indexando...");
                areaResultado.setText("");

                // Roda em thread para não travar a UI
                new Thread(() -> {
                    indice =
                        new MapaDispersao<>(
                            TAMANHO_MAPA);

                    Indexador indexador =
                        new Indexador(indice);

                    indexador.indexarDiretorio(
                        caminho);

                    Persistencia.salvar(indice);

                    buscador =
                        new Buscador(indice);

                    SwingUtilities.invokeLater(() -> {
                        labelStatus.setText(
                            "Indexação concluída. "
                            + "Índice salvo.");
                        areaResultado.setText(
                            "Indexação concluída!\n"
                            + "Diretório: " + caminho
                            + "\nAgora você pode "
                            + "pesquisar palavras.");
                    });
                }).start();
            }
        });

        botaoReindexar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Isso irá apagar o índice atual "
                + "e reindexar.\nContinuar?",
                "Reindexar",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                new java.io.File("indice.dat")
                    .delete();
                indice = null;
                buscador = null;
                labelStatus.setText(
                    "Índice removido. Clique em "
                    + "\"Indexar Diretório\".");
                areaResultado.setText("");
            }
        });

        setVisible(true);
    }

    // ─────────────────────────────────────────────
    //  Realiza a busca e exibe resultados
    // ─────────────────────────────────────────────
    private void buscar(JLabel labelStatus) {

        String texto = campoBusca.getText().trim();

        if (texto.isEmpty()) {
            areaResultado.setText(
                "Digite uma ou mais palavras "
                + "para pesquisar.");
            return;
        }

        if (buscador == null) {
            areaResultado.setText(
                "Nenhum índice disponível.\n"
                + "Clique em \"Indexar Diretório\" "
                + "primeiro.");
            return;
        }

        long inicio = System.currentTimeMillis();

        ListaEncadeada<String> resultado =
            buscador.buscarVarias(texto);

        long fim = System.currentTimeMillis();

        areaResultado.setText("");

        String[] palavras = texto.trim()
            .split("\\s+");

        if (palavras.length == 1) {
            areaResultado.append(
                "Resultados para: \""
                + texto + "\"\n");
        } else {
            areaResultado.append(
                "Documentos com TODAS as palavras: \""
                + texto + "\"\n");
        }

        areaResultado.append(
            "─────────────────────────────────\n");

        if (resultado.estaVazia()) {
            areaResultado.append(
                "Nenhum documento encontrado.");
        } else {
            int count = 1;
            NoLista<String> p =
                resultado.getPrimeiro();

            while (p != null) {
                areaResultado.append(
                    count + ". " + p.getInfo() + "\n");
                count++;
                p = p.getProximo();
            }

            areaResultado.append(
                "─────────────────────────────────\n");
            areaResultado.append(
                (count - 1)
                + " documento(s) em "
                + (fim - inicio) + " ms.");
        }
    }
}
