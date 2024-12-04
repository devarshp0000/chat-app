/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author DEVARSH
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
class ClientHandle implements Runnable{
    Socket s1, s2;
    
    DataInputStream in1;
    DataInputStream in2;
    
    DataOutputStream out1;
    DataOutputStream out2;

    public ClientHandle(Socket S1, Socket S2) {
        this.s1 = S1;
        this.s2 = S2;
    }
    
    @Override
    public void run(){
        try {
            this.in1 = new DataInputStream(s1.getInputStream());
            this.out1 = new DataOutputStream(s1.getOutputStream());
            
            this.in2 = new DataInputStream(s2.getInputStream());
            this.out2 = new DataOutputStream(s2.getOutputStream());
            
            while(true){
                if(in1.available()!=0){
                    outString(in1, out2);
                }
                if(in2.available()!=0){
                    outString(in2, out1);
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ClientHandle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void outString(DataInputStream in, DataOutputStream out) throws IOException{
        String s = in.readUTF();
        out.writeUTF(s);
    }
    
}
public class Server {

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        ServerSocket ss = new ServerSocket(6969);
        Socket s, s1;
        
        while(true){
            System.out.println("Server running: waiting for clients to join");
            s = ss.accept();
            System.out.println("Client 1 joined");
            
            s1 = ss.accept();
            System.out.println("Client 2 joined");
            
            ClientHandle ch = new ClientHandle(s, s1);
            new Thread(ch).start();
        }
        
    }
}
