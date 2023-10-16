import java.util.*;
public class PasswordChecker
{
    public PasswordChecker()
    {
        
    }
    public static void main(String su[])
    {
        Scanner s=new Scanner(System.in);
        PasswordChecker test=new PasswordChecker();
        System.out.print("enter the password to identify its stength ==> ");
        test.identifyPasswordStrength(s.nextLine());
    }
    public static boolean isValidLength(String password)
    {
        return password.length()>7?true:false;
    }
    public static boolean hasSpecialCharater(String password)
    {
        // retrurn Character.isDigit()
        for(int i=0;i<password.length();i++)
        {
            if (!Character.isDigit(password.charAt(i))
                && !Character.isLetter(password.charAt(i))
                && !Character.isWhitespace(password.charAt(i)))
                {
                return true;
                }
             
        } return false;  
    }
    public static boolean hasCaps(String password)
    {
        for(int i=0;i<password.length();i++)
        {
            if (!Character.isUpperCase(password.charAt(i)))
                {
                return true;
                }
             
        } return false;  
    }
    public static boolean isDigitPresent(String password)
    {
        for(int i=0;i<password.length();i++)
        {
            if (!Character.isDigit(password.charAt(i)))
                {
                return true;
                }
             
        } return false;  
    }
    public static boolean identifyPasswordStrength(String password)
    {
        return(isDigitPresent(password)&&hasCaps(password)&&isValidLength(password)&&hasSpecialCharater(password));
    }
}

