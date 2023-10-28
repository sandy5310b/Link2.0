import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientHandler extends Thread
{
    public Socket socket;
    public DataManager dataManager; 
    User user; 
    ObjectInputStream br ;
    ObjectOutputStream pr ;
    public ClientHandler(Socket socket , DataManager dataManager)throws Exception
    {
        System.out.println("inside the clienthandler constructor");
        this.socket = socket;
        this.dataManager = dataManager ;
        try
        {
            System.out.println("inside try block");
            pr = new ObjectOutputStream(socket.getOutputStream());
            br = new ObjectInputStream(socket.getInputStream());
            System.out.println("exiting try block");
        }
        catch(Exception e)
        {
            System.out.println("hhe");
        }
    }    
    public void run()
    {
       
        System.out.println("insidd start ");
        try 
        {
            Start();
        } 
        catch (Exception e) 
        {
            System.out.println("some problem in the start "+e.getMessage());
            System.out.println(e);
        }
    }
    void Start()throws Exception
    {
        System.out.println("starting . . . ");
        pr.writeObject(dataManager.allUser);
        while(true)
        {
        String temp = (String)br.readObject();
        System.out.println("choise "+temp);
        int n = Integer.parseInt(temp);
        System.out.println("the user choise is "+n);
        if(n==1)
        {
         SignIn();   
        }
        else
        {
         SignUp();
        }}
    }
    void SignUp()throws Exception
    {
        try{
        System.out.println("in signUp block");
        String arr[] = ((String)br.readObject()).split(":"); 
        System.out.println("request to add "+Arrays.toString(arr));
        String add_user_response ="";
                while(true)
                {
                    add_user_response = dataManager.checkDuplicateUsername(arr[0], arr[1]);
                    System.out.println("respons efrom data base "+ (add_user_response));
                    if(add_user_response.split(":")[0].equals("error"))
                    {
                        pr.writeObject(add_user_response);
                        String temp = (String)br.readObject();
                        if(temp.equals("exit"))
                        {
                            System.out.println("user want to go the index page");
                            Start();
                        }
                        arr = ((String)br.readObject()).split(":");
                    }
                    else
                    {
                        System.out.println("user can be added to the list");
                        pr.writeObject("go");
                        String temp[] = ((String)br.readObject()).split(":");
                        // sy
                        dataManager.addUser(temp[0], temp[1], temp[2], false);
                        System.out.println(Arrays.toString(temp));
                        pr.writeObject("go");
                        Start(); 
                        break;
                    }

                }
            }
            catch(Exception e)
            {
                System.out.println("exception in singup block");
                System.out.println(e.getMessage());
            }
    }
    void delAccount()
    {
        try 
        {
            String ch = (String)br.readObject();
            if(ch.equals("delete"))
            {
                System.out.println("requst from the client to delete the user "+user.name);
                dataManager.delete(user);
            }
            start();
        } 
        catch (Exception e) 
        {
            // TODO: handle exception
        }
        
    }
    void SignIn()throws Exception
    {
        System.out.println("is signin block");
        String arr[] = ((String)br.readObject()).split(":"); 
        user = dataManager.isValidUser(arr[1], arr[2]);
        if(user!=null)
        {
            pr.writeObject("go:"+user.name+":"+user.mail_id+":"+(user.admin?"t":"f"));
            System.out.println("password is correct");
            DashBord();
        }
        else{
                pr.writeObject("error"); 
        } 
        String temp = (String)br.readObject();
        if(temp.equals("retry"))
        {
            System.out.println("client requested to retry");
            SignIn();
        }
        else
        {
            System.out.println("proceed user page");
            start();
        }
    }
    void DashBord() throws Exception
    {
        System.out.println("hello sarang");
        int choice = Integer.parseInt((String)br.readObject());
        switch(choice)
        {
            case 1 :
                    System.out.println("user want to go to message section");
                    message();
                    break;
            case 3 : 
                    System.out.println("user requested to delete the account");
                    delAccount();
                    break;

        }
    }
    void message1()
    {
        System.out.println("The user is requsting to see the messages");

    }
    void message()
    {
        System.out.println("the name of the user  is "+user.name);
        try
        {
            String reciver = (String)br.readObject();
            System.out.println("request form the client to see the message of "+reciver);
            ObjectOutputStream ob = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("hello reciver ");
            ArrayList<String>messageList = dataManager.message(user.name,reciver);
            int count = 0;
            for (String string : messageList) {
                if(string.split("‡")[1].equals(reciver))
                {
                    count ++;
                }
            }
            // pr.println(reciver);
            ob.writeObject(messageList);
            ob.flush();
            System.out.println("sended all the packets");
            while(true)
            {
                System.out.println("heloo waithing  for the response in the message block");
                String temp = (String)br.readObject();
                System.out.println(temp);
                if(temp.equals("exit"))
                {
                    System.out.println("client want to stop the message");
                    DashBord();
                }
                else if(temp.equals("new"))
                {
                    System.out.println("the user want to get new message");
                    ArrayList<String> array = dataManager.getNewMessage(count,user.name,reciver);
                    for (String kk : array) 
                    {
                        System.out.println(kk);    
                    }
                    if(array.size()==0)
                    {
                        System.out.println("no new message");
                        pr.writeObject("no");
                    }
                    else
                    {
                        count += array.size(); 
                        System.out.println("has new message in the file");
                        pr.writeObject("go");
                        ob.writeObject(array);
                        ob.flush();
                        System.out.println("All the new message packet are sended succesfully from the client side");
                    }
                }
                else
                {
                    System.out.println("write the message into the file");
                    DateTimeFormatter pp = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
                    try
                    {
                        String pps = ("f‡"+user.name+'‡'+temp+'‡'+LocalDateTime.now().format(pp));
                        dataManager.writeMessage(pps,reciver);
                    }
                    catch(Exception e)
                    {
                        System.out.println("error writing the message the sender wrot to the reciver");
                        
                    }
                    
                }
            }
        }
        catch(Exception e)    
        {
            System.out.println(e.getMessage());
            System.out.println(e);
            System.out.println("error in message block");
        }
}
}