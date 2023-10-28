import java.io.*;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class Message {
    static File Database = new File("/home/sarangsu/java/whatsapp/database");
    static LocalDateTime getdate(String str) {
        str = str.substring(str.lastIndexOf("‡") + 1, str.length());
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        LocalDateTime date = LocalDateTime.parse(str, format);
        return date;
    }
    static void reWrite_Message_File(ArrayList<String> s, File file) {
        System.out.println("rewrit the file ");
        try (FileWriter fr = new FileWriter(file)) {
            for (String message : s) {

                fr.write(message + "\n");
            }
            fr.close();
        } catch (Exception e) {
            System.out.println("error while re-writing the message logged user file");
        }
    }
    static ArrayList<String> getMessage(String sender, String reciver) {
        ArrayList<String> sendermessagelist = new ArrayList<String>();
        ArrayList<String> message = new ArrayList<String>();
        try {
            Scanner senderFile = new Scanner(new File(Database, sender+".txt"));
            Scanner reciverFile = new Scanner(new File(Database, reciver+".txt"));
            String senderMessage = null, reciverMessage = null;
            while (senderFile.hasNextLine() || reciverFile.hasNextLine() || senderMessage != null || reciverMessage != null) {
                while (senderMessage == null && senderFile.hasNextLine()) {
                    String temp = senderFile.nextLine();
                    System.out.println("temp    = > "+temp);
                    
                    if (temp.split("‡")[1].equals(reciver)) {
                        senderMessage = temp;
                        System.out.println("condition "+senderMessage.split("‡")[1].equals(reciver));
                        System.out.println(" conditioned checked message "+senderMessage);
                        sendermessagelist.add('t'+temp.substring(1));//'t means message has been read '
                        break;
                    }
                    else
                    {
                        sendermessagelist.add(temp);
                    }
                }
                System.out.println("senders     message  = "+senderMessage);
                while (reciverMessage == null && reciverFile.hasNextLine()) {   
                    String temp = reciverFile.nextLine();
                    // reciverMessage = 
                    System.out.println("reciver message == "+temp);
                    if (temp.split("‡")[1].equals(sender)) {
                        reciverMessage = temp;
                        break;
                    }
                }
                System.out.println("sende message == "+senderMessage +"   reciver message "+reciverMessage);
                if (reciverMessage != null && senderMessage != null&& getdate(reciverMessage).isBefore(getdate(senderMessage))) {
                    String splitter[] = reciverMessage.split("‡");
                    System.out.println("reciver message " + splitter[2]+" is printed");
                    message.add(reciverMessage);
                    reciverMessage = null;
                } else if (reciverMessage != null && senderMessage != null
                        && getdate(reciverMessage).isAfter(getdate(senderMessage))) {
                    String splitter[] = senderMessage.split("‡");
                    System.out.println("sender message " + splitter[2]+" is printed");
                    message.add(senderMessage);
                    senderMessage = null;
                } else {
                    System.out.println(color.bold_red + "same time soo part 1-->" + senderMessage + "part 2-->"
                            + reciverMessage + color.resetColor);
                    if(senderMessage == null && reciverMessage == null )
                    {
                     break;   
                    }        
                    else if (senderMessage == null) {
                        System.out.println("reciver message " + reciverMessage);
                        message.add(reciverMessage);
                        reciverMessage = null;
                    } 
                    else{
                        System.out.println("sender message " + reciverMessage);
                        message.add(senderMessage); 
                        senderMessage = null;
                    }
                }
                System.out.println("one wile is completed");

            }
        } catch (Exception e) {
            System.out.println("eroor while getting the message in the the get conversation block"+e.getMessage());
        }
        System.out.println("sender message rewriiten");
        for (String str : sendermessagelist) {
            System.out.println(str);
        }
        System.out.println("order in which the message to wriiten");
        for (String str : message) {
            System.out.println(str);
        }
        reWrite_Message_File(sendermessagelist,new File(Database, sender));
        return message;
    }
    static ArrayList<String> getMessage(int n ,String user,String reciver)
    {
        System.out.println("vlaue of n "+n);
        System.out.println("----------------------------------------------------------------------------");
        ArrayList<String>arr = new ArrayList<>();
        try
        {
            System.out.println("in checkig new mesage");
            File file = new File(Database, user+".txt");
            System.out.println("the current user "+user);
            System.out.println("checking the message with the name "+reciver);
            Scanner s =new Scanner(file);
            System.out.println("viewing all message in the file to check new message");
            while(s.hasNextLine())
            {
                String pp =s.nextLine();
                System.out.println(pp);
                if(pp.split("‡")[1].equals(reciver))
                {   
                    // System.out.println(pp.split("‡")[1]+" 00000 "+reciver);
                    if(--n<0)
                    {
                        System.out.println("new message founded "+pp);
                        arr.add(pp);
                        // continue;
                    }
                    
                }
            }
                // System.out.println("new messages");
                // for (String ii : arr) 
                // {
                //     System.out.println(ii);    
                // }
            
            }
            catch(Exception e)
            {
                System.out.println("error in getting new message");
            }
        return(arr);
    }
    public static void main(String[] args) {

    }
}
