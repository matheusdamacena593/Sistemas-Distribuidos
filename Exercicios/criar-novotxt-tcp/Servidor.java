import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(12345);
            System.out.println("Servidor esperando conexão na porta 12345...");

            Socket cliente = servidor.accept();
            System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());


            BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            PrintWriter escritorArquivo = new PrintWriter(new FileWriter("recebido.txt"));

            String linha;
            while ((linha = entrada.readLine()) != null) {
                if (linha.equals("EOF")) break;
                escritorArquivo.println(linha);
            }
            
            System.out.println("Arquivo recebido e salvo como 'recebido.txt'");

            escritorArquivo.close();
            entrada.close();
            cliente.close();
            servidor.close();

        } catch (IOException e) {
            System.out.println("Erro ao iniciar servidor ou ao receber dados: " + e.getMessage());
        }
    }
}
