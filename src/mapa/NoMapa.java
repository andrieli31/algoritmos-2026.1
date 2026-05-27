package mapa;

import java.util.Objects;

public class NoMapa<T, K> {

    private K chave;
    private T valor;

    public K getChave() {
        return chave;
    }

    public void setChave(K chave) {
        this.chave = chave;
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        NoMapa other = (NoMapa) obj;

        if (chave == null) {
            return other.chave == null;
        }

        return chave.equals(other.chave);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(chave);
    }
}