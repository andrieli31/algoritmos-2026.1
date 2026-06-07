package busca;

import lista.ListaEncadeada;
import mapa.MapaDispersao;
import mapa.NoLista;
import mapa.NoMapa;
import utils.TextoUtils;
public class Buscador {

    // MapaDispersao<T=valor, K=chave>
    private MapaDispersao<ListaEncadeada<String>,
                          String> indice;

    public Buscador(
            MapaDispersao<ListaEncadeada<String>,
                          String> indice) {
        this.indice = indice;
    }
    public ListaEncadeada<String>
    buscarPorPrefixo(String prefixo) {

    	prefixo =
    		    TextoUtils.normalizar(prefixo);
        ListaEncadeada<String> resultado =
            new ListaEncadeada<>();

        ListaEncadeada<
            NoMapa<
                ListaEncadeada<String>,
                String
            >
        > entradas = indice.entradas();

        NoLista<
            NoMapa<
                ListaEncadeada<String>,
                String
            >
        > p = entradas.getPrimeiro();

        while (p != null) {

            String palavra =
                p.getInfo().getChave();

            if (
                palavra.startsWith(prefixo)
            ) {

                ListaEncadeada<String> docs =
                    p.getInfo().getValor();

                NoLista<String> d =
                    docs.getPrimeiro();

                while (d != null) {

                    String caminho =
                        d.getInfo();

                    if (
                        !resultado.contem(caminho)
                    ) {
                        resultado.inserir(caminho);
                    }

                    d = d.getProximo();
                }
            }

            p = p.getProximo();
        }

        return resultado;
    }

    public ListaEncadeada<String> buscar(
            String palavra) {

    	palavra =
    		    TextoUtils.normalizar(palavra);
    	
        ListaEncadeada<String> resultado =
            indice.buscar(palavra);

        if (resultado == null) {
            return new ListaEncadeada<>();
        }

        return resultado;
    }

    public ListaEncadeada<String> buscarVarias(
            String texto) {

        String[] palavras =
            texto.split("\\s+");

        if (
            palavras.length == 0
            || palavras[0].isEmpty()
        ) {

            return new ListaEncadeada<>();
        }

        // Normaliza a primeira palavra
        String primeira =
            TextoUtils.normalizar(
                palavras[0]
            );

        ListaEncadeada<String> resultado =
        	    buscarPorPrefixo(primeira);
        
        if (resultado == null) {

            return new ListaEncadeada<>();
        }

        for (
            int i = 1;
            i < palavras.length;
            i++
        ) {

            if (
                palavras[i].isEmpty()
            ) {
                continue;
            }

            String atualPalavra =
                TextoUtils.normalizar(
                    palavras[i]
                );

            ListaEncadeada<String> atual =
            	    buscarPorPrefixo(
            	        atualPalavra
            	    );

            if (atual == null) {

                return new ListaEncadeada<>();
            }

            resultado =
                interseccao(
                    resultado,
                    atual
                );
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
