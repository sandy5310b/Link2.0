import java.util.*;
import java.io.*;
public class User implements Serializable {
    public String name = "";
    public String mail_id = "";
    public String password = "";
    public boolean admin = false;
    File path ;
    public User(String name , String mail_id , String password , boolean admin ,File path)
    {
        this.name = name ;
        this.mail_id = mail_id;
        this.password = password;
        this.path = path;
        this.admin = admin ;
    }    
    public static void main(String[] args) {

    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null ) return false;
        User user = (User) obj;
        return this.name.equals(user.name ) && this.mail_id.equals(user.mail_id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name, mail_id, password ,path ,admin);
    }
    @Override 
    public String toString() {
        return "hello fef";
     }
}
