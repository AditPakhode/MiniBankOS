package shell;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import Auth.*;

import kernel.TransactionManager;

public class Terminal {
    private CommandParser parser;
    private BlockingQueue<String> commandQueue=new LinkedBlockingQueue<>();
    private AuthManager authManager;

    public Terminal(TransactionManager transactionManager, AuthManager authManager){
        parser=new CommandParser(transactionManager);
        this.authManager=authManager;
    }
    public void start(){
        Scanner sc=new Scanner(System.in);
        
        System.out.println("Booting BankingOS...");
        System.out.println("BankingOS Terminal Ready. Type 'help' for a list of commands. *Reminder to add help command*\n Type 'exit' to shutdown.");
        
    
        Thread inputThread=new Thread(() -> {

            while(true){
                String username="guest";
                if(Session.isLoggedIn()){
                    username=Session.getCurrentUser().getUsername();
                }
                System.out.print("BankingOS@"+username+"> ");
                
                String command=sc.nextLine().trim();

                if(command.isEmpty()) continue;
                try{
                    commandQueue.put(command);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                if(command.equalsIgnoreCase("exit")){
                    break;
                }
            }
        });
        
        Thread executorThread=new Thread(() -> {
            while(true){
                try{
                    String command=commandQueue.take();
                    if(command.equalsIgnoreCase("exit")){
                    System.out.println("Shutting down BankingOS...");
                    break;
                }
                String []tokens=command.split(" ");
                String cmd=tokens[0];

                if(cmd.equalsIgnoreCase("register")){
                    if(tokens.length<3){
                        System.out.println("Usage: register <username> <password>");
                        continue;
                    }
                    boolean success=authManager.register(tokens[1],tokens[2]);

                    if(success){
                        System.out.println("User registered sucessfully.");
                    }
                    else{
                        System.out.println("User already exists.");
                    }
                    continue;
                }
                if(cmd.equalsIgnoreCase("login")){
                    if(tokens.length>3){
                        System.out.println("Usage: login <username> <password>");
                        continue;
                    }
                    user u=authManager.login(tokens[1], tokens[2]);
                    if(u!=null){
                        Session.login(u);
                        System.out.println("Login successful");
                    }
                    else{
                        System.out.println("Invalid credentials.");
                    }
                    continue;
                }
                if(cmd.equalsIgnoreCase("logout")){
                    Session.logout();
                    System.out.println("Logged out.");
                    continue;
                }
                
                if (!Session.isLoggedIn()){
                    System.out.println("Please login first.");
                    continue;
                }
                parser.parse(command);
                System.out.println();


                System.out.println();
                System.out.println("BankingOS@terminal$: ");
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                }
            });

        inputThread.start();
        executorThread.start();
    
    }
}
