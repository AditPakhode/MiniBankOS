package Auth;

public class Session {
    private static user currentUser=null;

    //Login: set current user
    public static void login(user User){
        currentUser=User;
    }


    //Logout: clearing the session
    public static void logout(){
        currentUser=null;
    }

    //get current user
    public static user getCurrentUser(){
        return currentUser;
    }

    //check if logged in
    public static boolean isLoggedIn(){
        return currentUser != null;
    }
}
