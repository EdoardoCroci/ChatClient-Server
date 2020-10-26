package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;

public class GestioneChat{    
    private ArrayList<ServerThread> sockets = new ArrayList<>();
    private ServerSocket s; 

    public void start(){
          try{
              System.out.println("server in attesa");
              s = new ServerSocket(6789);
              
              for(int i = 0; i < 2; i++) { 
                sockets.add(new ServerThread(s.accept())); 
                sockets.get(i).start();
                System.out.println("Client n. " + (i+1) + " connesso");
              }
              
              s.close();
          } catch (Exception e){
              System.out.println(e.getMessage());
              System.out.println("Errore durante l'istanza del messaggio!");
              System.exit(1);
          }
    }
  
    public static void main(String[] args) {
        GestioneChat tcpServer = new GestioneChat();
        tcpServer.start();
    }

public class ServerThread extends Thread{
    private Socket clientS = null;
    private BufferedReader inDalClient;
    private DataOutputStream outVersoClient;
    private String usernameClient;
    
    public ServerThread (Socket socket){
        this.clientS = socket;
    }

    @Override
    public void run (){
        try{
            comunica();
        } catch (Exception e){
            e.toString();
        }
    }

    public void comunica () throws Exception {
        inDalClient = new BufferedReader(new InputStreamReader(clientS.getInputStream()));
        outVersoClient = new DataOutputStream(clientS.getOutputStream());
        
        outVersoClient.writeBytes("Inserisci username");
        usernameClient = inDalClient.readLine();
        
        for(;;) {
            String mex = inDalClient.readLine();
            if(mex.equals("FINE")){
                outVersoClient.writeBytes(usernameClient + ": utente disconnesso");
                sockets.remove(this);
                break;
            }
            else if (sockets.size() > 1)    outVersoClient.writeBytes(usernameClient + ": " + mex + " (ricevuta e ritrasmessa)" + '\n');
            else outVersoClient.writeBytes("Nessuno è connesso" + '\n');
            
        }
        outVersoClient.close();
        inDalClient.close();
        clientS.close();
    }
}
}
