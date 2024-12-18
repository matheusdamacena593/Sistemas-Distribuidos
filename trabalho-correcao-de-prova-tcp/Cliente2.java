import java.io.*;
import java.net.*;
import java.util.Random;

public class Cliente2 {
    public static void main(String[] args) {
        int servidorPorta = 8888;
        String servidorIP = "localhost";
        int opcao = 0;
        String arquivoRespostas;

        try {
            Socket socketCliente = new Socket(servidorIP, servidorPorta);
            OutputStream saidaDados = socketCliente.getOutputStream();
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
           
            PrintWriter saida = new PrintWriter(saidaDados, true);

            System.out.println("Servidor conectado.\n");

            do {
                System.out.println("Bem vindo ao Menu de provas: ");
                System.out.println("Digite o numero da prova:");
                System.out.println("1 - Prova tipo 1\n2 - Prova tipo 2\n3 - Prova tipo 3\n4 - Prova tipo 4\n5 - Prova tipo 5\n0 - Fechar conexão");

                opcao = Integer.parseInt(teclado.readLine());

                while(opcao < 0 || opcao > 5){
                    System.out.println("Opcao invalida. Digite um numero entre 0 e 5");
                    opcao = Integer.parseInt(teclado.readLine());
                }

                arquivoRespostas = escolherProva(opcao);

                    saida.println(opcao);
                    if (opcao == 0) {
                        System.out.println("\nConexão encerrada. Servidor desconectado.");
                        socketCliente.close();
                        break;
                    }

                    File file = new File(arquivoRespostas);

                    BufferedReader leitorArquivo = new BufferedReader(new FileReader(file));

                    saida.println("INICIO DA PROVA");

                    String linha;
                    while ((linha = leitorArquivo.readLine()) != null) {
                        saida.println(linha);
                    }

                    saida.println("FIM DA PROVA");

                    BufferedReader entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                    String resultado = entrada.readLine();

                    System.out.println("\nResultado da Prova: \n\n" + resultado);
                    System.out.println("--------------------------\n\n");         

            }while (opcao != 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String escolherProva(int opcao){
        String arquivoRespostas;
        switch(opcao){
            case 1:
                arquivoRespostas = "./prova-tipo-1.txt";
                break;
            case 2:
                arquivoRespostas = "./prova-tipo-2.txt";
                break;
            case 3:
                arquivoRespostas = "./prova-tipo-3.txt";
                break;
            case 4:
                arquivoRespostas = "./prova-tipo-4.txt";
                break;
            case 5:
                arquivoRespostas = "./prova-tipo-5.txt";
                break;
            case 0:
                arquivoRespostas = "";
            default:
                arquivoRespostas = "";
        }

        return arquivoRespostas;
    }
}
