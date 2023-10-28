import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
public class Server 
{
    public static void main(String[] args) {
        try 
        {
        
            ServerSocket obj = new ServerSocket(9090);
            DataManager db = new DataManager();
        while(true)
        {   
            Socket socket = obj.accept();
            System.out.println("new client connected");
            
            ClientHandler clienthandler = new ClientHandler(socket,db);
            System.out.println("kkk");
            clienthandler.start();
        }}
         catch (Exception e) {
            
            System.out.println("error in the server "+e.getMessage());
        }
    }
}
