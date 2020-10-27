package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class GestioneChat{    
    private ArrayList<ServerThread> sockets = new ArrayList<>();
    private ServerSocket s; 
    private Socket socket = new Socket();

    public void start(){
          try{
            System.out.println("server in attesa");
            s = new ServerSocket(6789);

            for(int i = 0; i < 2; i++) { 
              sockets.add(new ServerThread(s.accept())); 
              sockets.get(i).start();                          
            }             
            s.close();
            }catch (Exception e){
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
        
        outVersoClient.writeBytes("Inserisci username" + '\n');       
        usernameClient = inDalClient.readLine();  
        System.out.println(usernameClient + " connesso");
        outVersoClient.writeBytes(usernameClient + " connesso" + '\n');
        
        for(;;) {
            String mex = inDalClient.readLine();
            if(mex.equalsIgnoreCase("FINE")){
                System.out.println(usernameClient + ": utente disconnesso" + '\n');
                sockets.remove(this);
                break;
            }
            else if (sockets.size() > 1) {
                for (ServerThread s : sockets)
                    {
                        if(s != this) s.outVersoClient.writeBytes(usernameClient + ": " + mex + "\n");
                    }
            }              
            else outVersoClient.writeBytes("Nessuno è connesso" + '\n');           
        }
        outVersoClient.close();
        inDalClient.close();
        clientS.close();
    }
}
}