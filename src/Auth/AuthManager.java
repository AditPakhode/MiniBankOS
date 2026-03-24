package Auth;
import java.util.HashMap;
import java.util.Map;

public class AuthManager {
    private Map<String, user> users;
    
    public AuthManager(){
        users=new HashMap<>();


        //deafuault admin(root user)
        user admin=new user("root", "root123", user.Role.ADMIN);
        users.put("root",admin);
    }


    //register new user
    public boolean register(String username, String password){
        if(users.containsKey(username)){
            return false;
        }
        user newuser=new user(username, password, user.Role.USER);
        users.put(username, newuser);
        return true;
    }


    //login validation
    public user login(String username, String password){
        if(!users.containsKey(username)){
            return null;
        }
        user user=users.get(username);
        if(user.getPassword().equals(password)){
            return user;
        }
        return null;
    }

    //get user (admin ops of seeing the username of an account)
    public user getuser(String username){
        return (users.containsKey(username))?users.get(username):null;
    }


    public boolean deeteuser(String username){
        if(!users.containsKey(username)){
            return false;
        }

        //don't delete root
        if(username.equals("root")) return false;

        users.remove(username);
        return true;
    }
    public boolean deleteUser(String username){
        if(!users.containsKey(username)){
            return false;
        }
        users.remove(username);
        return true;
    }
}
