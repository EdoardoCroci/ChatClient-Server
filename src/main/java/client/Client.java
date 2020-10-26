package client;

import java.io.*;
import java.net.*;

public class Client {
    private final static int serverPort = 6789;
    private Socket s;
    private BufferedReader input;
    private BufferedReader inFromServer;
    private DataOutputStream outToServer;
    
    public static void main(String args[]) throws UnknownHostException, IOException {
        Client client = new Client();
        client.connect();
    } 
    
    public void connect() {
        //Inizializzazione input
        input = new BufferedReader(new InputStreamReader(System.in));
        
        try {
            // Recupero ip localhost
            InetAddress ip = InetAddress.getByName("localhost"); 
          
            // Conessione al server 
            s = new Socket(ip, serverPort); 

            // Apertura canali
            inFromServer = new BufferedReader (new InputStreamReader (s.getInputStream()));
            outToServer = new DataOutputStream(s.getOutputStream()); 

            SendThread sendThread = new SendThread();
            ReadThread readThread = new ReadThread();
        }
        catch(Exception ex) {
           ex.toString();
        }    
    }

public class SendThread extends Thread{
    public SendThread() {
        start();
    }
    
    @Override 
    public void run() {
        try {
            for(;;) {
                String mex = input.readLine();
                if(mex.equals("FINE")) {
                    outToServer.writeBytes("Connessione in chiusura..." + '\n');  
                    inFromServer.close();
                    outToServer.close();
                    s.close();          
                    System.exit(0);
                    break;
                }
                System.out.println("IO: " + mex);
                outToServer.writeBytes(mex + '\n');
            }           
        }
        catch (Exception ex) {
            ex.toString();
            System.exit(1);
        }
    } 
}

public class ReadThread extends Thread{
    public ReadThread() {
        start();
    }
    
    @Override 
    public void run() {
        try {
            for(;;) {
                String mex = inFromServer.readLine();
                System.out.println(mex);
            }           
        }
        catch (Exception ex) {
            ex.toString();
            System.exit(1);
        }
    } 
}
}