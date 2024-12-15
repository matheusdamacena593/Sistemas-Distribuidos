import java.io.*;
import java.net.*;
import java.util.Random;

public class Cliente {
    public static void main(String[] args) {
        String servidorIP = "localhost";
        int servidorPorta = 8888;
        String arquivoRespostas = "./respostas.txt";

        try {
            Socket socketCliente = new Socket(servidorIP, servidorPorta);

            OutputStream saidaDados = socketCliente.getOutputStream();
            PrintWriter saida = new PrintWriter(saidaDados, true);

            saida.println(arquivoRespostas);

            BufferedReader entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            String resultado = entrada.readLine();

            System.out.println("Resultado da Prova: \n\n" + resultado);

            socketCliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
