package mapa;

import lista.ListaEncadeada;

public class MapaDispersao<T, K> {

    private ListaEncadeada<NoMapa<T, K>> info[];
private static final double FATOR_CARGA_MAXIMO = 0.75;

    public MapaDispersao(int tamanho) {
        info = new ListaEncadeada[tamanho];
    }

    private int calcularHash(K chave) {
        return Math.abs(chave.hashCode()) % info.length;
    }

    public void inserir(K chave, T dado) {

          if (calcularFatorCarga() > FATOR_CARGA_MAXIMO) {
        rehash();
    }
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

    private void rehash() {
    ListaEncadeada<NoMapa<T, K>>[] novoArray =
        new ListaEncadeada[info.length * 2];

    // Troca o array ANTES de reinserir
    // para que calcularHash use o novo tamanho
    info = novoArray;

    // Percorre todas as entradas do array antigo
    ListaEncadeada<NoMapa<T, K>> entradas = entradas();
    NoLista<NoMapa<T, K>> p = entradas.getPrimeiro();

    while (p != null) {
        inserir(
            p.getInfo().getChave(),
            p.getInfo().getValor()
        );
        p = p.getProximo();
    }
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
