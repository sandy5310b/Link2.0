import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class ClientHandler extends Thread
{
    public Socket socket;
    public DataManager dataManager; 
    User user; 
    BufferedReader br ;
    PrintWriter pr ;
    public ClientHandler(Socket socket , DataManager dataManager)
    {
        this.socket = socket;
        this.dataManager = dataManager ;
        try {
            br =new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pr =new PrintWriter(socket.getOutputStream(),true);
        } catch (Exception e) { 
            System.out.println("error in the clienhadler contructor");
        }
    }    
    public void run()
    {
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
        pr.println(dataManager.allUser);
        while(true)
        {
        String temp = br.readLine();
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
        String arr[] = br.readLine().split(":"); 
        System.out.println("request to add "+Arrays.toString(arr));
        String add_user_response ="";
                while(true)
                {
                    add_user_response = dataManager.checkDuplicateUsername(arr[0], arr[1]);
                    System.out.println("respons efrom data base "+ (add_user_response));
                    if(add_user_response.split(":")[0].equals("error"))
                    {
                        pr.println(add_user_response);
                        String temp = br.readLine();
                        if(temp.equals("exit"))
                        {
                            System.out.println("user want to go the index page");
                            Start();
                        }
                        arr = br.readLine().split(":");
                    }
                    else
                    {
                        System.out.println("user can be added to the list");
                        pr.println("go");
                        String temp[] = br.readLine().split(":");
                        // sy
                        dataManager.addUser(temp[0], temp[1], temp[2], false);
                        System.out.println(Arrays.toString(temp));
                        pr.println("go");
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
    void SignIn()throws Exception
    {
        String arr[] = br.readLine().split(":"); 
        user = dataManager.isValidUser(arr[1], arr[2]);
        if(user!=null)
        {
            pr.println("go:"+user.name+":"+user.mail_id+":"+(user.admin?"t":"f"));
            System.out.println("password is correct");
            DashBord();
        }
        else{
                pr.println("error"); 
        } 
        String temp = br.readLine();
        if(temp.equals("retry"))
        {
            System.out.println("client requested to retry");
            SignIn();
        }
        else
        {
            System.out.println("proceed user page");
            DashBord();
        }
    }
    void DashBord() throws Exception
    {
        System.out.println("hello sarang");
        int choice = Integer.parseInt(br.readLine());
        switch(choice)
        {
            case 1 :
                    System.out.println("user want to go to message section");
                    message();
                    break;

        }
    }
    void message()
    {
        System.out.println("the name of the user  is "+user.name);
        try
        {
            String reciver = br.readLine();
            System.out.println("request form the client to see the message of "+reciver);
            ObjectOutputStream ob = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("hello reciver ");
            ob.writeObject(dataManager.message(user.name,reciver));
            ob.flush();
            System.out.println("sended all the packets");
            while(true)
            {
                // Thread thread = new Thread(() ->
                // {
                //     File 
                // });
                System.out.println("heloo waithing  for the response in the message block");
                String temp = br.readLine();
                System.out.println(temp);
                System.out.println("heloo waithing  for the response in the message block");
                System.out.println("temp==null"+(temp==null));
                System.out.println(temp.equals("null"));
                if(temp.equals("null"))
                {
                    System.out.println("client want to stop the message");
                    DashBord();
                }
                else
                {
                    System.out.println("write the message into the file");
                    DateTimeFormatter pp = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
                    try
                    {
                        String pps = ("f‡"+"sarang"+'‡'+temp+'‡'+LocalDateTime.now().format(pp));
                        dataManager.writeMessage(pps,reciver);
                    }
                    catch(Exception e)
                    {
                        System.out.println("error writing the message the sender wrot to the reciver");
                        
                    }
                    
                }
            }
            // System.out.println("hello from theb ending");
        }
        catch(Exception e)    
        {
            System.out.println(e.getMessage());
            System.out.println(e);
            System.out.println("error in message block");
        }
}
}
