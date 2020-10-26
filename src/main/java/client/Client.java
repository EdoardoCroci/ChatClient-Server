package client;

import java.io.*;
import java.net.*;

public class Client {
    final static int serverPort = 6789;
    static Socket s;
    static BufferedReader input;
    static BufferedReader inFromServer;
    static DataOutputStream outToServer;
    
    public static void main(String args[]) throws UnknownHostException, IOException { 
        //Inizializzazione input
        input = new BufferedReader(new InputStreamReader(System.in));
        
        // Recupero ip localhost
        InetAddress ip = InetAddress.getByName("localhost"); 
          
        // Conessione al server 
        s = new Socket(ip, serverPort); 
          
        // Apertura canali
        inFromServer = new BufferedReader (new InputStreamReader (s.getInputStream()));
        outToServer = new DataOutputStream(s.getOutputStream()); 
  
        // Thread invio messaggi 
        Thread sendMessage = new Thread(() -> { //new runnable
            while (true) {
                try {
                    // Lettura del messaggio da tastiera
                    String mex = input.readLine();
                    // Scrittura del messaggio ricevuto
                    outToServer.writeBytes(mex);
                } catch (IOException e) {
                    e.toString();
                }
            } 
        }); 
          
        // Thread lettura messaggio
        Thread readMessage = new Thread(() -> { //new runnable
            while (true) {
                try {
                    // Lettura del messaggio arrivato al client
                    String mex = inFromServer.readLine();
                    System.out.println(mex);
                } catch (IOException e) {
                    
                    e.toString();
                }
            } 
        }); 
  
        sendMessage.start(); 
        readMessage.start(); 
    } 
}
