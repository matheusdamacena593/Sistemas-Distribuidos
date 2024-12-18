import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

            String mensagem;

            while ((mensagem = entrada.readLine()) != null) {

                if (mensagem.equalsIgnoreCase("0")) {
                    System.out.println("Conexão encerrada. Cliente " + socket.getInetAddress().getHostName() + "na porta " + socket.getPort() + " desconectado.");
                    socket.close();
                    return;
                }

                List<String> linhasGabarito = lerArquivoComoLista(arquivoGabarito);

                List<String> linhasProva = new ArrayList<>();
                String linha;
                while ((linha = entrada.readLine()) != null) {
                    if (linha.equals("INICIO DA PROVA")) {
                        continue;
                    }
                    if (linha.equals("FIM DA PROVA")) {
                        break;
                    }
                    linhasProva.add(linha);
                }

                if (linhasProva.isEmpty()) {
                    saida.println("Erro: Não foi possível ler o arquivo de respostas.");
                    socket.close();
                    return;
                }

                if (linhasGabarito.isEmpty()) {
                    saida.println("Erro: Não foi possível ler o arquivo de gabarito.");
                    socket.close();
                    return;
                }

                if (linhasProva.size() != linhasGabarito.size()) {
                    saida.println("Erro: O número de perguntas/respostas no arquivo não coincide com o gabarito.");
                } else {
                    int totalPerguntas = linhasProva.size();
                    int acertos = 0;
                    int erros = 0;

                    for (int i = 0; i < totalPerguntas; i++) {
                        String linhaProva = linhasProva.get(i);
                        String linhaGabarito = linhasGabarito.get(i);

                        int indexComeco = (linhaProva.charAt(1) == '-') ? 2 : 3;

                        String respostaProva = linhaProva.substring(indexComeco);
                        String respostaGabarito = linhaGabarito.substring(indexComeco);

                        System.out.println((i + 1) + "° Comparação: ");
                        System.out.println("Resposta: " + respostaProva);
                        System.out.println("Gabarito: " + respostaGabarito + "\n");

                        for (int j = 0; j < respostaProva.length(); j++) {
                            char letraResposta = respostaProva.charAt(j);
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> lerArquivoComoLista(String caminhoArquivo) {
        List<String> linhas = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo));
            String linha;
            
            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linhas;
    }
}