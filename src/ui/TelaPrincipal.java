package ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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

    private MapaDispersao<
            ListaEncadeada<String>,
            String> indice;

    private Buscador buscador;

    public TelaPrincipal() {

        // ─────────────────────────────────────────
        // CONFIGURAÇÃO DA JANELA
        // ─────────────────────────────────────────
        setTitle("Pesquisa de Arquivos");

        setSize(850, 600);

        setDefaultCloseOperation(
            JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        getContentPane().setBackground(
            new Color(245, 245, 245));

        // ─────────────────────────────────────────
        // TÍTULO
        // ─────────────────────────────────────────
        JLabel titulo =
            new JLabel("Pesquisa de Arquivos");

        titulo.setFont(
            new Font(
                "Segoe UI",
                Font.BOLD,
                28
            )
        );

        titulo.setBorder(
            BorderFactory.createEmptyBorder(
                15, 20, 10, 0
            )
        );

        // ─────────────────────────────────────────
        // CAMPO DE BUSCA
        // ─────────────────────────────────────────
        campoBusca = new JTextField();

        campoBusca.setFont(
            new Font(
                "Segoe UI",
                Font.PLAIN,
                18
            )
        );

        campoBusca.setPreferredSize(
            new Dimension(0, 45)
        );

        campoBusca.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(200, 200, 200),
                    1,
                    true
                ),
                BorderFactory.createEmptyBorder(
                    8, 15, 8, 15
                )
            )
        );

        campoBusca.setToolTipText(
            "Digite uma ou mais palavras..."
        );

        JPanel painelBusca =
            new JPanel(new BorderLayout());

        painelBusca.setBackground(
            new Color(245, 245, 245)
        );

        painelBusca.setBorder(
            BorderFactory.createEmptyBorder(
                0, 20, 10, 20
            )
        );

        painelBusca.add(
            campoBusca,
            BorderLayout.CENTER
        );

        // ─────────────────────────────────────────
        // BOTÕES
        // ─────────────────────────────────────────
        JButton botaoIndexar =
            new JButton("Indexar Diretório");

        JButton botaoReindexar =
            new JButton("Reindexar");

        Font fonteBotao =
            new Font(
                "Segoe UI",
                Font.PLAIN,
                14
            );

        botaoIndexar.setFont(fonteBotao);
        botaoReindexar.setFont(fonteBotao);

        botaoIndexar.setFocusPainted(false);
        botaoReindexar.setFocusPainted(false);

        botaoIndexar.setBackground(
            new Color(230, 230, 230)
        );

        botaoReindexar.setBackground(
            new Color(230, 230, 230)
        );

        // ─────────────────────────────────────────
        // STATUS
        // ─────────────────────────────────────────
        JLabel labelStatus =
            new JLabel(
                "Nenhum índice carregado."
            );

        labelStatus.setFont(
            new Font(
                "Segoe UI",
                Font.PLAIN,
                13
            )
        );

        labelStatus.setForeground(
            new Color(80, 80, 80)
        );

        // ─────────────────────────────────────────
        // PAINEL DE AÇÕES
        // ─────────────────────────────────────────
        JPanel painelAcoes =
            new JPanel(
                new FlowLayout(
                    FlowLayout.LEFT,
                    10,
                    5
                )
            );

        painelAcoes.setBackground(
            new Color(245, 245, 245)
        );

        painelAcoes.setBorder(
            BorderFactory.createEmptyBorder(
                0, 15, 10, 15
            )
        );

        painelAcoes.add(botaoIndexar);
        painelAcoes.add(botaoReindexar);
        painelAcoes.add(labelStatus);

        // ─────────────────────────────────────────
        // PAINEL SUPERIOR
        // ─────────────────────────────────────────
        JPanel painelNorte =
            new JPanel();

        painelNorte.setLayout(
            new BoxLayout(
                painelNorte,
                BoxLayout.Y_AXIS
            )
        );

        painelNorte.setBackground(
            new Color(245, 245, 245)
        );

        painelNorte.add(titulo);
        painelNorte.add(painelBusca);
        painelNorte.add(painelAcoes);

        add(
            painelNorte,
            BorderLayout.NORTH
        );

        // ─────────────────────────────────────────
        // ÁREA DE RESULTADOS
        // ─────────────────────────────────────────
        areaResultado =
            new JTextArea();

        areaResultado.setEditable(false);

        areaResultado.setFont(
            new Font(
                "Consolas",
                Font.PLAIN,
                15
            )
        );

        areaResultado.setBackground(
            Color.WHITE
        );

        areaResultado.setBorder(
            BorderFactory.createEmptyBorder(
                15, 15, 15, 15
            )
        );

        JScrollPane scroll =
            new JScrollPane(areaResultado);

        scroll.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(
                    0, 15, 15, 15
                ),
                BorderFactory.createLineBorder(
                    new Color(220, 220, 220)
                )
            )
        );

        add(scroll, BorderLayout.CENTER);

        // ─────────────────────────────────────────
        // CARREGAMENTO DO ÍNDICE
        // ─────────────────────────────────────────
        if (Persistencia.indiceExiste()) {

            indice =
                Persistencia.carregar();

            if (indice != null) {

                buscador =
                    new Buscador(indice);

                labelStatus.setText(
                    "Índice carregado do disco."
                );
            }

        } else {

            labelStatus.setText(
                "Nenhum índice encontrado."
            );
        }

        // ─────────────────────────────────────────
        // BUSCA AUTOMÁTICA
        // ─────────────────────────────────────────
        campoBusca.getDocument()
            .addDocumentListener(
                new DocumentListener() {

            @Override
            public void insertUpdate(
                    DocumentEvent e) {

                buscar(labelStatus);
            }

            @Override
            public void removeUpdate(
                    DocumentEvent e) {

                buscar(labelStatus);
            }

            @Override
            public void changedUpdate(
                    DocumentEvent e) {

                buscar(labelStatus);
            }
        });

        // ─────────────────────────────────────────
        // INDEXAR DIRETÓRIO
        // ─────────────────────────────────────────
        botaoIndexar.addActionListener(
            e -> {

            JFileChooser chooser =
                new JFileChooser();

            chooser.setFileSelectionMode(
                JFileChooser.DIRECTORIES_ONLY
            );

            int ret =
                chooser.showOpenDialog(this);

            if (
                ret ==
                JFileChooser.APPROVE_OPTION
            ) {

                String caminho =
                    chooser
                    .getSelectedFile()
                    .getAbsolutePath();

                labelStatus.setText(
                    "Indexando..."
                );

                areaResultado.setText("");

                new Thread(() -> {

                    indice =
                        new MapaDispersao<>(
                            TAMANHO_MAPA
                        );

                    Indexador indexador =
                        new Indexador(indice);

                    long inicio =
                        System.currentTimeMillis();

                    indexador.indexarDiretorio(
                        caminho
                    );

                    Persistencia.salvar(
                        indice
                    );

                    buscador =
                        new Buscador(indice);

                    long fim =
                        System.currentTimeMillis();

                    SwingUtilities
                        .invokeLater(() -> {

                        labelStatus.setText(
                            "Indexação concluída."
                        );

                        areaResultado.setText(
                            "Indexação concluída!\n\n"
                            + "Diretório:\n"
                            + caminho
                            + "\n\nTempo: "
                            + (fim - inicio)
                            + " ms"
                        );
                    });

                }).start();
            }
        });

        // ─────────────────────────────────────────
        // REINDEXAR
        // ─────────────────────────────────────────
        botaoReindexar.addActionListener(
            e -> {

            int confirm =
                JOptionPane
                    .showConfirmDialog(
                        this,
                        "Isso apagará "
                        + "o índice atual.\n"
                        + "Continuar?",
                        "Reindexar",
                        JOptionPane.YES_NO_OPTION
                    );

            if (
                confirm ==
                JOptionPane.YES_OPTION
            ) {

                new java.io.File(
                    "indice.dat"
                ).delete();

                indice = null;
                buscador = null;

                labelStatus.setText(
                    "Índice removido."
                );

                areaResultado.setText("");
            }
        });

        // ─────────────────────────────────────────
        // EXIBE JANELA
        // ─────────────────────────────────────────
        setVisible(true);
    }

    // ─────────────────────────────────────────────
    // BUSCA
    // ─────────────────────────────────────────────
    private void buscar(
            JLabel labelStatus) {

        String texto =
            campoBusca.getText().trim();

        if (texto.isEmpty()) {

            areaResultado.setText(
                "Digite uma palavra "
                + "para pesquisar."
            );

            return;
        }

        if (buscador == null) {

            areaResultado.setText(
                "Nenhum índice carregado.\n"
                + "Indexe um diretório primeiro."
            );

            return;
        }

        long inicio =
            System.currentTimeMillis();

        ListaEncadeada<String> resultado;

        String[] palavras =
            texto.split("\\s+");

        if (
            palavras.length == 1
            && palavras[0].length() >= 3
        ) {

            resultado =
                buscador.buscarPorPrefixo(
                    palavras[0]
                );

        } else {

            resultado =
                buscador.buscarVarias(texto);
        }

        long fim =
            System.currentTimeMillis();

        areaResultado.setText("");

        if (palavras.length == 1) {

            areaResultado.append(
                "Resultados para: \""
                + texto
                + "\"\n"
            );

        } else {

            areaResultado.append(
                "Documentos com TODAS "
                + "as palavras: \""
                + texto
                + "\"\n"
            );
        }

        areaResultado.append(
            "──────────────────────────────\n"
        );

        if (resultado.estaVazia()) {

            areaResultado.append(
                "Nenhum documento encontrado."
            );

        } else {

            int count = 1;

            NoLista<String> p =
                resultado.getPrimeiro();

            while (p != null) {

                areaResultado.append(
                    count
                    + ". "
                    + p.getInfo()
                    + "\n"
                );

                count++;

                p = p.getProximo();
            }

            areaResultado.append(
                "\n──────────────────────────────\n"
            );

            areaResultado.append(
                (count - 1)
                + " documento(s) encontrado(s)"
                + "\nTempo: "
                + (fim - inicio)
                + " ms"
            );
        }
    }
}