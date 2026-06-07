package persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import lista.ListaEncadeada;
import mapa.MapaDispersao;
import mapa.NoLista;
import mapa.NoMapa;

public class Persistencia {

    private static final String ARQUIVO =
            "indice.dat";

    // ─────────────────────────────────────────────
    //  Salva o índice em disco
    // ─────────────────────────────────────────────
    public static void salvar(
            MapaDispersao<ListaEncadeada<String>,
                          String> indice) {

        try {
            FileOutputStream fos =
                new FileOutputStream(ARQUIVO);
            ObjectOutputStream oos =
                new ObjectOutputStream(fos);

            // Percorre todas as entradas do mapa
            ListaEncadeada<
                NoMapa<ListaEncadeada<String>, String>>
                    entradas = indice.entradas();

            // Conta quantas entradas existem
            int total = entradas.obterComprimento();
            oos.writeInt(indice.getTamanho());
            oos.writeInt(total);

            NoLista<
                NoMapa<ListaEncadeada<String>, String>>
                    p = entradas.getPrimeiro();

            while (p != null) {
                String chave =
                    p.getInfo().getChave();
                ListaEncadeada<String> docs =
                    p.getInfo().getValor();

                // Salva a chave
                oos.writeObject(chave);

                // Salva os documentos da lista
                int qtdDocs = docs.obterComprimento();
                oos.writeInt(qtdDocs);

                NoLista<String> d =
                    docs.getPrimeiro();
                while (d != null) {
                    oos.writeObject(d.getInfo());
                    d = d.getProximo();
                }

                p = p.getProximo();
            }

            oos.close();

        } catch (Exception e) {
            System.err.println(
                "Erro ao salvar índice: "
                + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    //  Carrega o índice do disco para a memória
    // ─────────────────────────────────────────────
    public static MapaDispersao<
                    ListaEncadeada<String>, String>
            carregar() {

        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            return null;
        }

        try {
            FileInputStream fis =
                new FileInputStream(arquivo);
            ObjectInputStream ois =
                new ObjectInputStream(fis);

            int tamanhoMapa = ois.readInt();
            int total = ois.readInt();

            MapaDispersao<ListaEncadeada<String>,
                          String> indice =
                new MapaDispersao<>(tamanhoMapa);

            for (int i = 0; i < total; i++) {
                String chave =
                    (String) ois.readObject();

                int qtdDocs = ois.readInt();
                ListaEncadeada<String> docs =
                    new ListaEncadeada<>();

                for (int j = 0; j < qtdDocs; j++) {
                    String doc =
                        (String) ois.readObject();
                    docs.inserir(doc);
                }

                indice.inserir(chave, docs);
            }

            ois.close();
            return indice;

        } catch (Exception e) {
            System.err.println(
                "Erro ao carregar índice: "
                + e.getMessage());
            return null;
        }
    }

    public static boolean indiceExiste() {
        return new File(ARQUIVO).exists();
    }
}
