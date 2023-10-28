import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
public class DataManager 
{
    ArrayList<User> data = new ArrayList<User>();
    int longest_name_length = 0;
    File Database = new File("/home/sarangsu/java/whatsapp/database");
    String allUser = "";
    File user_list = new File(Database, "userlist.txt");
    Message message = new Message();
    public DataManager()
    {
        System.out.println("this is from constructor");
        try
        { 
            Scanner s =new Scanner(user_list);
            while(s.hasNextLine())
            {
                String temp = s.nextLine();
                temp = Encryption.decrypt(temp);
                String splitter[] = temp.split("‡");
                allUser +=splitter[0]+":"; 
                data.add(new User(splitter[0], splitter[1], splitter[2] ,splitter[3].equals("t"),new File(Database,splitter[0]+".txt")));
                longest_name_length = Math.max(longest_name_length,splitter[0].length());
            }
        }catch(Exception e)
        {
            System.out.println(color.bold_red+"user lsit file not found"+color.resetColor);
        }
        System.out.println("All user list ");
        for (User i : data) 
        {
            System.out.println("Name : "+i.name +"  E-mail-id : "+i.mail_id+"  Is a Admin = "+i.admin);    
        }
    }
    User isValidUser(String name , String password)
    {
        for(User user : data)
        {
            if(user.name.equals(name))
            {   
                // return user;
                if(user.password.equals(password))
                {
                    return user;
                } 
                System.out.println(color.bold_red+"User name is correct but password is incorrect"+color.resetColor);
                return null;
            }
        }
        System.out.println(color.bold_red+"User Does Not Exist"+color.resetColor);
        return null;
    }
    // User getUser(String name)
    public synchronized void addUser(String userName , String mail_id , String password , boolean isAdmin)
    {       try {

            File user_file = new File(Database , userName+".txt");
            FileWriter fr = new FileWriter(user_list , true);
            fr.write(Encryption.encrypt(userName+"‡"+mail_id+"‡"+password+"‡"+(isAdmin?"t":"f"))+"\n");
            fr.close();
            System.out.println(user_file.createNewFile());
            data.add(new User(userName, mail_id, password, isAdmin ,user_file));
            System.out.println(color.boldGreenColor+"succesfully added the user"+color.resetColor);
            } 
            catch (Exception e) 
            {
                System.out.println("erroe in the add user block");
                e.printStackTrace();
            }
        }
    public synchronized String checkDuplicateUsername(String userName ,String mail_id)
    {
        for (User user : data) {
            if(user.name.equals(userName) || user.mail_id.equals(mail_id))
            {
                if(user.name.equals(userName))
                {
                    System.out.println(color.bold_red+"user Name Alredy Exist"+color.resetColor);
                    return "error"+":"+"username";
                }
                else
                {
                    System.out.println(color.bold_red+"Email id Already Exist"+color.resetColor);
                    return "error"+":"+"mail";
                }
            }
        }
        return "go";
    }
    public ArrayList<String> message(String sender ,String reciver)
    {
        ArrayList<String> arr = Message.getMessage(sender, reciver);
        
        return arr;
    }
    public ArrayList<String> getNewMessage(int n ,String sender , String reciver)
    {
        ArrayList<String> array = Message.getMessage(n,sender,reciver);
        return array;
    }
    public synchronized void writeMessage(String str ,String reciver) 
    {
        try
        {
            File file = new File(Database, reciver+".txt");
            FileWriter fr =new FileWriter(file , true);
            fr.write(str+"\n");
            fr.close();
            System.out.println("wrote the new message into the file of the reciver");
        }
        catch(Exception e)
        {
            System.out.println("error in writting the message in file of the reciver");
            System.out.println(e.getMessage());
        }
    }
    public void delete(User user)
    { 
        try
        {
            data.remove(user);
            System.out.println("the user left");
            for (User users : data) 
            {
                System.out.println(users.name);
            }
            FileWriter fr =new FileWriter(user_list);
            for(User obj : data)
            {
                fr.write(Encryption.encrypt(obj.name+"‡"+obj.mail_id+"‡"+obj.password+"‡"+(obj.admin?"t":"f"))+"\n");
            }
            fr.close();
            System.out.println("sucessfull deleted the user");
        }
        catch(Exception e)
        {
            System.out.println("error in deletblock "+e.getMessage());
        }
    }
}
    