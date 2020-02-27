import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Finder {
	private File[] arquivosDaPasta;
	private File arquivo; //arquivo sendo lido 
	private File backup; //caminho do diretório para backup desse arquivo
	private int nivel; //quantas pastas já foram acessadas na hierarquia 
	private double modificado = 1561985986000.0; // 01/07/2019 Data em millis da classificação de arquivos recentes ou antigos
	
	Finder(String caminho) {
		arquivo = new File(caminho);
		nivel = 0;
		iniciar();
	}

	Finder(String caminho, int nivel) {
		arquivo = new File(caminho);
		this.nivel = nivel;
		iniciar();
	}

	Finder(File f, int nivel){
		arquivo = f;
		this.nivel = nivel;
		iniciar();
	}
	

	
	private void iniciar() {
		arquivosDaPasta = arquivo.listFiles();

//		Definir pasta de backup. Por padrão, é no mesmo diretório onde começou a leitura
//		Exemplo: C:\pasta\..   -->   C:\backup\..
		String aux = arquivo.getAbsolutePath(),
			   pastaBackup;
		int posicaoAntes = aux.length(),
			posicaoDepois = 0;
		
		for(int i = nivel; i >= 0; --i) {
			posicaoDepois = posicaoAntes;
			posicaoAntes = aux.lastIndexOf("\\");
			aux = aux.substring(0, posicaoAntes);
		}
		pastaBackup = arquivo.getAbsolutePath().substring(0,posicaoAntes) + "\\backup"+ arquivo.getAbsolutePath().substring(posicaoDepois);

		backup = new File(pastaBackup);

		
		if (arquivosDaPasta != null)
			for (File f : arquivosDaPasta) {
				System.out.print("\nLendo: " + f.getAbsolutePath());
				
				if (f.isFile()) {
					// se for arquivo
					arquivo(f);
				} else if (f.isDirectory()) {
					// se for pasta
					pasta(f);
				} else {
					System.out.print("Arquivo não reconhecido: " + f.getAbsolutePath() + f.getName());
				}
			}
	}

	private void arquivo(File f) {
//		Quando o File é arquivo, verifica se a última modificação é mais antiga ou recente que o controle
//		Caso seja mais antiga, é criada uma pasta para o backup desse arquivo
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");		 
		System.out.print(" " + sdf.format(f.lastModified()));
		
		if(f.lastModified() < modificado) {
//			Cria a hierarquia de diretórios para esse arquivo
			backup.mkdir();
			
			
//			Faz a cópia do arquivo
			File copia = new File(this.backup + "\\" + f.getName());
			
			System.out.print(" -> Copia ");

			try {
				copiar(f, copia);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
//			Faz a cópia da data de modificação para o arquivo copiado
			Date dataModificado = null;

			try {
				dataModificado = sdf.parse(sdf.format(f.lastModified()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
            copia.setLastModified(dataModificado.getTime());

            
//          Deleta o arquivo da pasta de origem
			f.delete();
		} else
			System.out.print(" Recente");
	}

	private void pasta(File f) {
		//Se o File for pasta, cria um novo Finder para olhar dentro dela
		new Finder(f, nivel+1);	
		System.out.print(" Pasta");
	}
	
	private void copiar(File src, File dst) throws IOException {
//		Método para cópia de arquivos
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
