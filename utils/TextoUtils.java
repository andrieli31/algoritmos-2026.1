package utils;

import java.text.Normalizer;

public class TextoUtils {

    public static String normalizar(
            String texto) {

        texto = texto.toLowerCase();

        texto = Normalizer.normalize(
            texto,
            Normalizer.Form.NFD
        );

        texto = texto.replaceAll(
            "[\\p{InCombiningDiacriticalMarks}]",
            ""
        );

        texto = texto.replaceAll(
            "[^a-z0-9]",
            ""
        );

        return texto;
    }
}