import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor extends Thread {
    private String arquivoGabarito = "./gabarito.txt";
    private Socket socket;
    private ServerSocket servidorSocket;

    public Servidor() {
        //Feito para instanciar a classe na main sem paramentros
    }

    public Servidor(Socket socket) {
        this.socket = socket;
    }

    private void criarServidorSocket(int porta) throws IOException {
        this.servidorSocket = new ServerSocket(porta);
        System.out.println("Servidor criado com sucesso na porta " + porta);
        System.out.println("Aguardando conexões...");
    }

    private Socket esperarConexao() throws IOException{
        Socket socket = this.servidorSocket.accept();
        System.out.println("Usuario: " + socket.getInetAddress() + " conectado.");
        return socket;
    }

    public static void main(String[] args) {
        try {
            Servidor servidor = new Servidor();
            
            servidor.criarServidorSocket(8888);

            while (true) {
                Socket clienteConectado = servidor.esperarConexao();
                Thread thread = new Servidor(clienteConectado);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("IOException " + e);
        }
    }
    
    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);

            String arquivoRespostas = entrada.readLine();
            String respostas = lerRespostasDoArquivo(arquivoRespostas);

            if (respostas == null) {
                saida.println("Erro: Não foi possível ler o arquivo de respostas.");
                socket.close();
                return;
            }

            String gabarito = lerGabaritoDoArquivo();
            if (gabarito != null) {
                String[] linhasRespostas = respostas.split("\n");
                String[] linhasGabarito = gabarito.split("\n");

                if (linhasRespostas.length != linhasGabarito.length) {
                    saida.println("Erro: O número de perguntas/respostas no arquivo não coincide com o gabarito.");
                } else {
                    int totalPerguntas = linhasRespostas.length;
                    int acertos = 0;
                    int erros = 0;

                    for (int i = 0; i < totalPerguntas; i++) {
                        int indexComeco = (linhasRespostas[i].charAt(1) == '-') ? 2 : 3;

                        String resposta = linhasRespostas[i].substring(indexComeco);
                        String respostaGabarito = linhasGabarito[i].substring(indexComeco);

                        System.out.println((i+1) + "° Comparção: ");
                        System.out.println("Resposta: " + resposta);
                        System.out.println("Gabarito: " + respostaGabarito + "\n");
                        
                        for (int j = 0; j < resposta.length(); j++) {
                            char letraResposta = resposta.charAt(j);
                            char letraGabarito = respostaGabarito.charAt(j);

                            if (letraResposta == letraGabarito) {
                                acertos++;
                            } else {
                                erros++;
                            }
                        }
                    }

                    saida.println("Acertos: " + acertos + " - " + "Erros: " + erros);
                }
            } else {
                saida.println("Erro: Não foi possível ler o arquivo de gabarito.");
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String lerGabaritoDoArquivo() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.arquivoGabarito));
            StringBuilder gabarito = new StringBuilder();
            String linha;

            while ((linha = br.readLine()) != null) {
                gabarito.append(linha);
                gabarito.append("\n");
            }

            br.close();

            return gabarito.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String lerRespostasDoArquivo(String nomeArquivo) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));
            StringBuilder respostas = new StringBuilder();
            String linha;

            while ((linha = br.readLine()) != null) {
                respostas.append(linha);
                respostas.append("\n");
            }

            br.close();

            return respostas.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
