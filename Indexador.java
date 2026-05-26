import java.io.File;

public class Indexador {

     public void indexarDiretorio(String caminho){
        File pasta = new File(caminho);
        File[] arquivos = pasta.listFiles();

        if (arquivos == null) {
            return;
        }

        for (int i = 0; i < arquivos.length; i++) {
            if (arquivos[i].isDirectory()) {
                indexarDiretorio(arquivos[i].getAbsolutePath());
            }else{
                if (arquivos[i].getName().endsWith(".txt")) {
                    System.out.println(arquivos[i].getAbsolutePath());
                }
            }
        }
    }

}
