package mapa;

import lista.ListaEncadeada;

public class MapaDispersao<T, K> {

    private ListaEncadeada<NoMapa<T, K>> info[];

    public MapaDispersao(int tamanho) {
        info = new ListaEncadeada[tamanho];
    }

    private int calcularHash(K chave) {
        return Math.abs(chave.hashCode()) % info.length;
    }

    public void inserir(K chave, T dado) {
        int indice = calcularHash(chave);

        if (info[indice] == null) {
            info[indice] = new ListaEncadeada<>();
        }
        NoMapa<T, K> no = new NoMapa<>();
        no.setChave(chave);
        no.setValor(dado);

        info[indice].inserir(no);
    }

    public void remover(K chave) {
        int indice = calcularHash(chave);
        if (info[indice] != null) {
            NoMapa no = new NoMapa<>();
            no.setChave(chave);
            info[indice].retirar(no);
        }
    }

    public T buscar(K chave) {
        int indice = calcularHash(chave);

        if (info[indice] != null) {
            NoMapa<T, K> noMapa = new NoMapa<>();
            noMapa.setChave(chave);

            NoLista<NoMapa<T, K>> no;
            no = info[indice].buscar(noMapa);

            if (no != null) {
                return no.getInfo().getValor();
            }
        }
        return null;
    }

    public double calcularFatorCarga() {
        int qtdElementos = 0;
        for (int i = 0; i < info.length; i++) {
            if (info[i] != null) {
                qtdElementos +=
                    info[i].obterComprimento();
            }
        }
        return 1.0 * qtdElementos / info.length;
    }

    public int getTamanho() {
        return info.length;
    }

    /**
     * Retorna todos os pares (chave, valor) do mapa
     * como uma ListaEncadeada de NoMapa.
     * Usado pela persistência para salvar o índice.
     */
    public ListaEncadeada<NoMapa<T, K>> entradas() {
        ListaEncadeada<NoMapa<T, K>> resultado =
            new ListaEncadeada<>();

        for (int i = 0; i < info.length; i++) {
            if (info[i] == null) continue;

            NoLista<NoMapa<T, K>> p =
                info[i].getPrimeiro();

            while (p != null) {
                resultado.inserir(p.getInfo());
                p = p.getProximo();
            }
        }
        return resultado;
    }
}
