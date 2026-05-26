import java.io.File;
import java.util.Scanner;

public class Indexador {

     public void indexarDiretorio(String caminho){
        File pasta = new File(caminho);
        File[] arquivos = pasta.listFiles();

        if (arquivos == null) {
            return;
        }

        for (int i = 0; i < arquivos.length; i++) {
            if (arquivos[i].isDirectory()) {
                //recursão para caso existam sub pastas no diretório
                indexarDiretorio(arquivos[i].getAbsolutePath());
            }else{
                if (arquivos[i].getName().endsWith(".txt")) {
                    processarArquivos(arquivos[i]);
                }
            }
        }
    }

    private void processarArquivos(File arquivo){
        try {
            Scanner leitor = new Scanner(arquivo);
            //enquanto existir palavra, continua na mesma linha
            while (leitor.hasNext()) {
                String palavra = leitor.next();
                System.out.println(palavra);
            }
            leitor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
