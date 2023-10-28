import java.util.*;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Client {
    public Socket socket;
    ObjectInputStream br ;
    ObjectOutputStream pr ;
    String allUsers;
    User user;
    Scanner s = new Scanner(System.in);

    public Client() {
        System.out.println("in constructor");
        try {
            socket = new Socket("localhost", 9090);
            System.out.println("connectd to tghe server");
            br = new ObjectInputStream(socket.getInputStream());
            pr =new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void start() throws Exception {
        allUsers = (String)br.readObject();
        System.out.println(color.bold_blue + "ent 1 to login" + color.resetColor);
        System.out.println(color.bold_blue + "ent 2 to signup" + color.resetColor);
        System.out.println(color.bold_blue + "ent 3 to exit" + color.resetColor);
        int ch;
        while (true) {
            try {
                ch = Integer.parseInt(s.nextLine());
                if (ch > 3 || ch < 1) {
                    throw new InputMismatchException();
                }
            } catch (Exception e) {
                System.out.println(color.bold_red + "ent only no 1/2/3 " + color.resetColor);
                continue;
            }
            break;
        }
        pr.writeObject(ch+"");
        switch (ch) {
            case 1:
                login();
                break;
            case 2:
                signUp();
                break;
            case 3:
                System.out.println(color.bold_red + "EXITING THE PROGRAM" + color.resetColor);
                System.exit(1);
                break;
        }
    }

    public void login() throws Exception {

        Scanner s = new Scanner(System.in);
        while (true) {
            System.out.println("Ent the user name");
            String username = s.nextLine();
            System.out.println("Ent the password");
            Console console = System.console();
            /*  String password = new String(console.readPassword());*/String password="Qwerty@123#";
            String login_sender = "login:" + username + ":" + password;
            pr.writeObject(login_sender);
            String login_clent_response[] = ((String)br.readObject()).split(":");
            if (login_clent_response[0].equals("go")) {
                user = new User(login_clent_response[1], login_clent_response[2], null,
                        login_clent_response[3].equals("t"), null);
                // System.out.println("user name " + user.name + "\nmail_id " + user.mail_id + "\nis admin " + user.admin);
                System.out.println(color.boldGreenColor + "Sucessfully logined"+color.resetColor);
                dashBord();
            } else {
                System.out.println(color.bold_red + "wrong password raa" + color.resetColor);
                System.out.println("1 -- > try again");
                System.out.println("2 -- > go back to index page");
                int ch = Integer.parseInt(s.nextLine());
                if (ch == 2) {
                    pr.writeObject("go-back");
                    start();
                }
                pr.writeObject("retry");
            }

        }
    }

    public void signUp() throws Exception {
        Scanner s = new Scanner(System.in);
        System.out.println(color.boldCyanColor + "ent the user name" + color.resetColor);
        System.out.print(" ".repeat(20));
        String userName = s.nextLine();
        System.out.println(color.boldCyanColor + "ent the gmail id" + color.resetColor);
        System.out.print(" ".repeat(20));
        String mailId = s.nextLine();
        Console console = System.console();
        String password = "";
        pr.writeObject(userName + ":" + mailId);
        String add_user_response = (String)br.readObject();
        System.out.println("server response for addding  " + add_user_response);
        if (add_user_response.equals("go")) {
            while (true) {
                try {
                    password = new String(
                            console.readPassword(color.bold_blue + "Enter password: " + color.resetColor));
                    if (PasswordChecker.identifyPasswordStrength(password)) {
                        System.out.println("password is strong");
                        pr.writeObject(userName + ":" + mailId + ":" + password + ":");
                        break;
                    } else {
                        // System.out.println(bold_red+"Password Is Not Strong\nrenter the password");
                        throw new Exception();
                    }
                } catch (Exception e) {
                    System.out.println(color.bold_red + "password is not strong retry" + color.resetColor);
                }
            }
        } else {
            System.out.println(color.bold_red + add_user_response + color.resetColor);
            System.out.println(" 1  - > retry");
            System.out.println(" 2  - > go back");
            int ch = Integer.parseInt((s.nextLine()));
            if (ch == 1) {
                pr.writeObject("go");
                signUp();
            } else {
                pr.writeObject("exit");
                start();
            }
        }
        System.out.println("ss");
        // pr.println("signup:"+userName+":"+password+":"+mailId);
        System.out.println("ss");
        if (((String)br.readObject()).equals("go")) {
            System.out.println("user added succesfully");
        } else {
            System.out.println("error in adding the user");
        }
        start();
        // if()
    }

    public void messageUser() throws Exception
    {
        System.out.println("Select the user to message with no");
        int i = 1;
        String splitter[] = allUsers.split(":");
        int space = 10;
        for (String user : splitter) 
        {
            System.out.println(user+" ".repeat(space-user.length()) + " --  " + i++);
        }
        int ch = s.nextInt();
        // System.out.println("user want to see the message of " + splitter[ch - 1]);
        pr.writeObject(splitter[ch - 1]);
        try
        {
        ObjectInputStream ob = new ObjectInputStream(socket.getInputStream());
        // ob.reset();
        ArrayList<String> arr = (ArrayList<String>)ob.readObject();
        // System.out.println("got all the packets");
        for (String string : arr) 
        {
            // System.out.println(string);    
        }
        // System.out.println("PRINTED ALL THE ELEMENT ");
        displayPreviousMessage(arr, user.name, splitter[ch - 1]);
        
        while(true)//dobut why dead code
        {
            String message = sendMessage();
            if(message.equals("exit"))
            {
                System.out.println("the user want to got to the dashbord");
                pr.writeObject("exit");
                dashBord();
            }
            else if(message.equals(""))
            {
                System.out.println("checking for new messages");
                pr.writeObject("new");
                String temp = (String)br.readObject();
                if(temp.equals("go"))
                {
                    System.out.println("the message for new message "+temp);
                    System.out.println("has new message from server");
                    // br.readLine();
                    // socket.getInputStream().
                    // ObjectInputStream ob = new ObjectInputStream(socket.getInputStream());
                    System.out.println(ob.getClass());
                    // System.out.println(ob.readObject());
                    ArrayList<String> arri = (ArrayList<String>)ob.readObject();
                    for (String mess : arri) 
                    {
                        System.out.println("message  ===== > "+mess);
                        System.out.println(timing(mess.split("‡")[3])); 
                        System.out.println(mess.split("‡")[2]);   
                    }
                    System.out.println("read all packets");
                }
                else
                {
                    System.out.println("no new message response from server");
                }
            }
            else
            {
                System.out.println("the user want to continue to message");
                pr.writeObject(message);
            }  
        }
        
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println(e);
            System.out.println("some error in printing message user block");
        }        
    }
    public String sendMessage()
    {
        String message ="",temp = "";
        Scanner s=new Scanner(System.in);
        temp = s.nextLine();
        if(temp.isEmpty())
        {
            System.out.println("is empty");
            message = "";
        }
        else
        {   
            System.out.println("not empty");
            do
            {
            message +=temp+"#";
            }
            while(s.hasNextLine()&& !(temp = s.nextLine()).equals("stop"));
            System.out.println("the message is "+message);
            message = message.substring(0, message.length()-1);
        }
        return message;
    }
    public static String getMessage()
    {
        Scanner s=new Scanner(System.in);
        System.out.print(" enter the string ");
        String str="",sentence="";
        while(s.hasNextLine()&& !(str=s.nextLine()).equals("stop"))
        {
            sentence+=str+'#';
        }
        sentence=sentence.substring(0,sentence.length()-1);
        System.out.println("sentence =="+sentence);
        return sentence;
    }
    public static String timing(String message) {

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        LocalDateTime sended_Time = LocalDateTime.parse(message, format);
        Duration duration = Duration.between(sended_Time, currentTime);
        String Message_Heading = "";
        long secondsDifference = duration.getSeconds();
        long hourDifference = duration.toHours();
        long daysDifference = duration.toDays();
        if(sended_Time.getDayOfYear()==currentTime.getDayOfYear())
        {
        if(secondsDifference < 3600)
        {
        if (secondsDifference < 60) {
        Message_Heading +=" ( "+secondsDifference +" seconds ago )";
        } else if (secondsDifference < 3600) {
        long minutes = secondsDifference / 60;
        Message_Heading += " ( "+minutes + " minutes ago )";
        }
        }
        else if(hourDifference<24)
        {
        Message_Heading += "today"+(sended_Time.getHour()>12?(sended_Time.getHour()-12):sended_Time.getHour())+":"+sended_Time.getMinute()+((sended_Time.getHour()<13)?"AM ":" PM ");
        }
        }
        else if(sended_Time.getDayOfYear()==currentTime.getDayOfYear()-1)
        {
        Message_Heading +="yesterday"+(sended_Time.getHour()>12?(sended_Time.getHour()-12):sended_Time.getHour())+":"+sended_Time.getMinute()+((sended_Time.getHour()<13)?"AM ":" PM ");
        }
        else if(daysDifference<8)
        {
        DayOfWeek dayOfWeek = sended_Time.getDayOfWeek();
        String dayOfWeekName = dayOfWeek.toString();
        dayOfWeekName = dayOfWeekName.charAt(0)+dayOfWeekName.substring(1).toLowerCase();
        Message_Heading +=dayOfWeekName+" "+(sended_Time.getHour()>12?(sended_Time.getHour()-12):sended_Time.getHour())+":"+sended_Time.getMinute()+((sended_Time.getHour()<13)?"AM ":" PM ");
        }
        else
        {
        Message_Heading += sended_Time.getDayOfMonth()+" "+sended_Time.getMonth()+" "+sended_Time.getYear()+" "+(sended_Time.getHour()>12?(sended_Time.getHour()-12):sended_Time.getHour())+":"+sended_Time.getMinute()+((sended_Time.getHour()<13)?"AM ":" PM ");
        }
        return Message_Heading+"\n";
    }
    public void displayPreviousMessage(ArrayList<String> arr, String sender, String reciver) {
        int space = 118;
        for (String str : arr) {
            String temp = str;
            String splitter[] = temp.split("‡");
            String heading = timing(splitter[3]);
            if (splitter[1].equals(sender)) {
                // System.out.println(splitter[1]+" "+heading); with the name of the message sender
                System.out.println(heading);// without the name
                StringTokenizer kk = new StringTokenizer(splitter[2], "‡");
                while (kk.hasMoreTokens()) {
                    System.out.println(kk.nextToken() + " ");
                }
            } else {
                String pp = heading;// removed the name of the message owner
                System.out.println(" ".repeat(space-pp.length())+pp);
                StringTokenizer kk = new StringTokenizer(splitter[2], "‡");
                // System.out.println(splitter[1]+" "+heading);
                while (kk.hasMoreTokens()) {
                    String ll = kk.nextToken();
                    System.out.println(" ".repeat(space - ll.length()) + ll);
                }
            }
        }
    }

    public void viewAllUser() {
        String splitter[] = allUsers.split(":");
        int i = 1;
        for (String user : splitter) {
            System.out.println(user + " -- 1 " + i);
        }
    }
    public void delAccount()
    {
        try
        {
            System.out.println("Are u sure about deleting the account");
            System.out.println("for confirmation pls enter Y else N");
            char ch= new Scanner(System.in).next().charAt(0);
            if(Character.toLowerCase(ch)=='y')
            {
                System.out.println("user gave the confirmation to delete the account");
                pr.writeObject("delete");
                start();
            }
            else
            {
                pr.writeObject("go");
                dashBord(); 
            }
        }catch(Exception e)
        {
            System.out.println("error in deleting the user");
        }
    }
    public void dashBord()throws Exception {
        System.out.println("1 -- > Message");
        System.out.println("3 -- > delete account ");
        System.out.println("3 -- > Status");
        System.out.println("4 -- > go back");
        int i = s.nextInt();
        switch (i) {
            case 1:
                pr.writeObject(1+"");
                messageUser();
                break;
            case 2:
            
                viewAllUser();
                break;
            case 3:
                pr.writeObject(3);
                delAccount();
                break;
            default:
                System.exit(0);
                break;

        }

    }
    public static void main(String[] args) throws Exception {
        Client obj = new Client();
        obj.start();
    }
}

