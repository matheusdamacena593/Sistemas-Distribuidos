import java.io.*;
import java.net.Socket;

public class Cliente {

    public static void main(String[] args) {
        try {
            Socket cliente = new Socket("127.0.0.1", 12345);
            System.out.println("Conectado ao servidor!");

	    // Busca o arquivo no caminho passado
            File file = new File("/home/ifg/Área de Trabalho/Sistemas Distribuidos/Programa 2/arquivo.txt");
            
            // Essa classe Ler todas as entradas de dados, nela passamos no arquivo txt
            BufferedReader leitorArquivo = new BufferedReader(new FileReader(file));

            PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);
 	    
            String linha;
            while ((linha = leitorArquivo.readLine()) != null) {
                saida.println(linha);
            }
            
            System.out.println("Arquivo enviado para o servidor com sucesso!");

            leitorArquivo.close();
            saida.close();
            cliente.close();

        } catch (IOException e) {
            System.out.println("Erro ao conectar ou enviar dados: " + e.getMessage());
        }
    }
}
