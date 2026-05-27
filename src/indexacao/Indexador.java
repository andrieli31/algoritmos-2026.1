package indexacao;

import java.io.File;
import java.util.Scanner;

import lista.ListaEncadeada;
import mapa.MapaDispersao;

public class Indexador {

    // MapaDispersao<T=valor, K=chave>
    // indice: chave=palavra (String),
    //         valor=lista de arquivos
    private MapaDispersao<ListaEncadeada<String>,
                          String> indice;

    public Indexador(
            MapaDispersao<ListaEncadeada<String>,
                          String> indice) {
        this.indice = indice;
    }

    public void indexarDiretorio(String caminho) {
        File pasta = new File(caminho);
        File[] arquivos = pasta.listFiles();

        if (arquivos == null) {
            return;
        }

        for (int i = 0; i < arquivos.length; i++) {
            if (arquivos[i].isDirectory()) {
                // recursão para sub-pastas
                indexarDiretorio(
                    arquivos[i].getAbsolutePath());
            } else {
                if (arquivos[i].getName()
                        .endsWith(".txt")) {
                    processarArquivo(arquivos[i]);
                }
            }
        }
    }

    private void processarArquivo(File arquivo) {
        try {
            Scanner leitor = new Scanner(arquivo);

            while (leitor.hasNext()) {
                String token = leitor.next();
                String palavra = normalizar(token);

                if (deveIndexar(palavra)) {
                    String caminho =
                        arquivo.getAbsolutePath();

                    ListaEncadeada<String> docs =
                        indice.buscar(palavra);

                    if (docs == null) {
                        docs = new ListaEncadeada<>();
                        indice.inserir(palavra, docs);
                    }

                    // Evita inserir o mesmo arquivo
                    // mais de uma vez
                    if (!docs.contem(caminho)) {
                        docs.inserir(caminho);
                    }
                }
            }
            leitor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Converte para minúsculas e remove pontuação
    private String normalizar(String token) {
        return token.toLowerCase()
            .replaceAll(
                "[^a-záàâãéèêíïóôõöúüçñ]", "");
    }

    // Regras: 3+ letras, não só dígitos/pontos
    private boolean deveIndexar(String palavra) {
        if (palavra == null
                || palavra.length() < 3) {
            return false;
        }
        if (palavra.matches("[0-9.]+")) {
            return false;
        }
        return true;
    }
}
