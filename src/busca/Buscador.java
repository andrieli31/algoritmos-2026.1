package busca;

import lista.ListaEncadeada;
import mapa.MapaDispersao;
import mapa.NoLista;

public class Buscador {

    // MapaDispersao<T=valor, K=chave>
    private MapaDispersao<ListaEncadeada<String>,
                          String> indice;

    public Buscador(
            MapaDispersao<ListaEncadeada<String>,
                          String> indice) {
        this.indice = indice;
    }

    public ListaEncadeada<String> buscar(
            String palavra) {

        palavra = palavra.toLowerCase();

        ListaEncadeada<String> resultado =
            indice.buscar(palavra);

        if (resultado == null) {
            return new ListaEncadeada<>();
        }

        return resultado;
    }

    public ListaEncadeada<String> buscarVarias(
            String texto) {

        texto = texto.toLowerCase();

        texto = texto.replaceAll(
            "[^a-zA-ZÀ-ÿ0-9 ]", " ");

        String[] palavras = texto.split("\\s+");

        if (palavras.length == 0
                || palavras[0].isEmpty()) {
            return new ListaEncadeada<>();
        }

        ListaEncadeada<String> resultado =
            indice.buscar(palavras[0]);

        if (resultado == null) {
            return new ListaEncadeada<>();
        }

        for (int i = 1; i < palavras.length; i++) {

            if (palavras[i].isEmpty()) continue;

            ListaEncadeada<String> atual =
                indice.buscar(palavras[i]);

            if (atual == null) {
                return new ListaEncadeada<>();
            }

            resultado =
                interseccao(resultado, atual);
        }

        return resultado;
    }

    private ListaEncadeada<String> interseccao(
            ListaEncadeada<String> lista1,
            ListaEncadeada<String> lista2) {

        ListaEncadeada<String> resultado =
            new ListaEncadeada<>();

        NoLista<String> atual =
            lista1.getPrimeiro();

        while (atual != null) {
            String documento = atual.getInfo();

            if (lista2.contem(documento)) {
                resultado.inserir(documento);
            }

            atual = atual.getProximo();
        }

        return resultado;
    }
}
