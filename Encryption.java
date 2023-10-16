import java.util.*;
import java.io.*;
public class Encryption {
    private static int shift = 23;
    public static void setShift(int n)
    {
        shift =n;
    }
    File file;
    ArrayList<String> arr = new ArrayList<String>();
    Encryption(File file)
    {
        this.file = file;
    }
    static String encrypt(String str)
    {
        String str2="";
        for(int i =0;i<str.length();i++)
        {
            str2=(char)(str.charAt(i)-shift)+str2;
        }
        return str2;
    }
    static String decrypt(String str)
    {
        String str2="";
        for(int i =0;i<str.length();i++)
        {
            str2=(char)(str.charAt(i)+shift)+str2;
        }
        return str2;
    }
    void encryptTheFile() 
    {
        try
        {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine())
        {
            arr.add(scanner.nextLine());
        }
        FileWriter fr = new FileWriter(file);
        for(String str :arr)
        {
            
            fr.write(encrypt(str)+"\n");
        }fr.close();
        }
        catch(Exception e)
        {
            System.out.println("file not found");
        }
    }
    public void decryptTheFile()
    {   arr.clear();
        {
            try
            {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine())
            {
                arr.add(scanner.nextLine());
            }
            FileWriter fr = new FileWriter(file);
            for(String str :arr)
            {
                
                fr.write(decrypt(str)+"\n");
            }fr.close();
            }
            catch(Exception e)
            {
                System.out.println("file not found");
            }
    }
}
    public static void main(String[] args) {
    
        File parent = new File("/home/sarangsu/java/whatsapp/database");
        File child = new File(parent, "userlist.txt");
       new Encryption(child).encryptTheFile();
        // new Encryption(child).decryptTheFile();
    }
}
