import java.io.*;
import java.net.*;
import java.util.Random;

public class Cliente {
    public static void main(String[] args) {
        int servidorPorta = 8888;
        String servidorIP = "localhost";
        String arquivoRespostas = "./prova.txt";
        String mensagem;

        try {
            Socket socketCliente = new Socket(servidorIP, servidorPorta);
            OutputStream saidaDados = socketCliente.getOutputStream();
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            PrintWriter saida = new PrintWriter(saidaDados, true);

            System.out.println("Servidor conectado.\n");
            System.out.println("Digite 'parar' para desconectar ou aperte 'Enter' para efetuar a correção: ");

            while ((mensagem = teclado.readLine()) != null) {
                saida.println(mensagem);
                if (mensagem.equalsIgnoreCase("parar")) {
                    System.out.println("\nConexão encerrada. Servidor desconectado.");
                    socketCliente.close();
                    break;
                } else if (!mensagem.equalsIgnoreCase("parar")){
                    saida.println(arquivoRespostas);

                    String resultado = entrada.readLine();

                    System.out.println("\nResultado da Prova: \n\n" + resultado);
                    System.out.print("\nDigite 'parar' para desconectar ou aperte 'Enter' para efetuar correção: ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
