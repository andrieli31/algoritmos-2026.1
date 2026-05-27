package lista;

import mapa.NoLista;

public class ListaEncadeada<T> {

	    private NoLista<T> primeiro;

	    public ListaEncadeada() {
	        primeiro = null;
	    }

	    public void inserir(T info) {
	        NoLista<T> novo = new NoLista<>();
	        novo.setInfo(info);
	        novo.setProximo(primeiro);
	        this.primeiro = novo;
	    }

	    public NoLista<T> getPrimeiro() {
	        return primeiro;
	    }
	    
	    public void exibir() {
	        NoLista<T> p = primeiro;
	        while (p != null) {
	            System.out.println(p.getInfo());
	            p = p.getProximo();
	        }
	    }

	    public boolean estaVazia() {
	        return primeiro == null;
	    }

	    public NoLista<T> buscar(T valor) {
	        NoLista<T> p = primeiro;
	        while (p != null) {
	            if (p.getInfo().equals(valor)) {  
	                return p;
	            }
	            p = p.getProximo();
	        }
	        return null;
	    }

	    public NoLista<T> retirar(T valor) {
	        NoLista<T> anterior = null;
	        NoLista<T> p = primeiro;

	        while (p != null && !p.getInfo().equals(valor)) {
	            anterior = p;
	            p = p.getProximo();
	        }

	        if (p != null) {
	            if (p == primeiro) {
	                this.primeiro = p.getProximo();
	            } else {
	                anterior.setProximo(p.getProximo());
	            }
	        }
	        return p;
	    }

	    public int obterComprimento() {
	        int contador = 0;
	        NoLista<T> p = primeiro;
	        while (p != null) {
	            contador++;
	            p = p.getProximo();
	        }
	        return contador;
	    }

	    public NoLista<T> obterNo(int posicao) {
	        if (posicao < 0) throw new IndexOutOfBoundsException();
	        int indice = 0;
	        NoLista<T> p = primeiro;
	        while (p != null) {
	            if (indice == posicao) return p;
	            indice++;
	            p = p.getProximo();
	        }
	        throw new IndexOutOfBoundsException();
	    }

	    public boolean contem(T valor) {
	        return buscar(valor) != null;
	    }
	    
	    @Override
	    public String toString() {
	        String resultado = "";
	        NoLista<T> p = primeiro;
	        while (p != null) {
	            resultado += p.getInfo();
	            if (p.getProximo() != null) resultado += ", ";
	            p = p.getProximo();
	        }
	        return resultado;
	    }
	    
	    
	}