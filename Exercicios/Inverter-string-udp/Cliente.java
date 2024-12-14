import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Cliente{
  public static void main(String args[]){
    DatagramSocket s = null;
    
    try{
      String servidor = "localhost";
      
      int porta = 6789;
      
      String msg;
      
      
      Scanner sc = new Scanner(System.in);
      System.out.println("Digite a sua Mensagem: ");
      msg = sc.nextLine();
      
      if (args.length > 0) servidor = args[0];
      if (args.length > 1) porta = Integer.parseInt(args[1]);
      if (args.length > 2) msg = args[2];
      
      byte[] m = msg.getBytes();
      
      InetAddress serv = InetAddress.getByName(servidor);
      
      s = new DatagramSocket();
      
      System.out.println("* Socket criado na porta: " + s.getLocalPort());
      
      DatagramPacket req = new DatagramPacket(m, msg.length(), serv, porta);
      
      s.send(req);
      
      System.out.println("* Datagrama enviado...: " + msg);
      
      byte[] buffer = new byte[1000];
      
      DatagramPacket resp = new DatagramPacket(buffer, buffer.length);
      
      s.setSoTimeout(10000);
      
      s.receive(resp);
      
      System.out.println("* Resposta do servidor: " + new String(resp.getData(), 0, resp.getLength()));
    }catch(SocketException e){
      System.out.println("! Erro socket: " + e.getMessage());
    }catch(IOException e){
      System.out.println("! Erro envio/recepcao do pacote: " + e.getMessage());
    }finally{
      if (s != null) s.close();
    }
  }
}
