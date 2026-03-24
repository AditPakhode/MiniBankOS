package kernel;

import system.BankDatabase;
import logging.Logger;
import kernel.scheduler.*;

import java.util.HashMap;
import java.util.Map;

import Auth.AuthManager;
import Auth.Session;
import Auth.user;

public class TransactionManager{
    private BankDatabase bank;
    private Logger logger;
    private Scheduler scheduler;
    private AuthManager authManager;


    public TransactionManager(BankDatabase bank, Scheduler scheduler, AuthManager authManager){
        this.bank=bank;
        this.scheduler=scheduler;
        this.logger=new Logger();//new logger object for each thread
        this.authManager=authManager;
    }
    public void createAccount(String name, double balance){
        user current=Session.getCurrentUser();
        
        if(current==null || !current.isAdmin()){
            System.out.println("Access denied: Only admin can create accounts.");
            return;
        }
        String logEntry="CREATE "+ name+ " " + balance;
        TransactionProcess process= new TransactionProcess(
            "TX-"+System.currentTimeMillis(),
            () ->{
            logger.log("BEGIN "+logEntry);
            bank.createAccount(name, balance);
            logger.log("COMMIT "+logEntry);
        },
        2//priority
    );
        scheduler.submitProcess(process);
    }
    public void transfer(String from, String to, double amount){
        user current = Session.getCurrentUser();
        if(current==null){
            System.out.println("Access denied: not logged in.");
            return;
        }

        //admin cannot transfer
        if(current.isAdmin()){
            System.out.println("Access denied: Admin cannot perform transfers.");
            return;
        }

        //must own source account
        if(!current.getUsername().equals(from)){
            System.out.println("Access denied: Cannot trasnfer from other accounts.");
            return;
        }

        //permission check
        if(!current.canTransfer()){
            System.out.println("Access denied: Insufficient permissions.");
            return;
        }

        //if cursor has reached till this point it can transfer
        String logEntry="TRANSFER "+from+" "+to+" "+amount;
        TransactionProcess process=new TransactionProcess(
            "TX-"+System.currentTimeMillis(),
            () ->{
                logger.log("BEGIN "+logEntry);
                bank.transfer(from, to, amount);
                logger.log("COMMIT "+logEntry);
            },
            1//higher priority
        );
        scheduler.submitProcess(process);
    }

    public void grantTransfer(String username){
        user current=Session.getCurrentUser();

        if(current==null|| !current.isAdmin()){
            System.out.println("Access denied: Only admins can grant permissions.");
            return;
        }
        user target=authManager.getuser(username);
        if(target==null){
            System.out.println("User not found.");
            return;
        }
        target.setTransferPermission(true);
        System.out.println("Permission granted for user "+username);
    }

    public void revokeTransfer(String username){
        user current=Session.getCurrentUser();

        if(current==null || !current.isAdmin()){
            System.out.println("Access denied: Only admins can revoke permissions.");
            return;
        }
        user target=authManager.getuser(username);
        if(target==null){
            System.out.println("User not found.");
            return;
        }
        target.setTransferPermission(false);
        System.out.println("Transfer permission revoked for user "+username);
    }

    public void deleteUser(String username){
        user currentUser=Session.getCurrentUser();
        if(currentUser==null){
            System.out.println("Please login first.");
            return;
        }

        if(!currentUser.isAdmin()){
            System.out.println("Only admins can delete users.");
            return;
        }
        if(username.equalsIgnoreCase("root")){
            System.out.println("Root user cannot be deleted.");
            return;
        }
        if(currentUser.getUsername().equals(username)){
            System.out.println("Cannot delete youself.");
            return;
        }
        boolean success=authManager.deleteUser(username);
        if(!success){
            System.out.println("User not found.");
            return;
        }
        System.out.println("User "+username+" deleted.");
    }

    public void register(String username, String password){
        if(Session.isLoggedIn()){
            System.out.println("Logout before registering a new user.");
            return;
        }

        boolean success=authManager.register(username, password);
        if(success){
            System.out.println("User registered successfully.");
        }
        else{
            System.out.println("User already exists.");
        }
    }
    public void login(String username, String password){
        if(Session.isLoggedIn()){
            System.out.println("Logout before logging in.");
            return;
        }
        user u=authManager.login(username, password);
        if(u==null){
            System.out.println("Invalid credentials."); 
        }
        
        Session.login(u);
        System.out.println("Login successful. Welcome "+username );

    }
    public void logout(String username, String password){
        if(!Session.isLoggedIn()){
            System.out.println("No user is currently logged in.");
            return;
        }
        Session.logout();
        System.out.println("Logout Successfull.");
    }

    public void checkBalance(String name){
        user current=Session.getCurrentUser();

        if(current == null){
            System.out.println("Access denied: not logged in.");
            return;
        }
        
        //admin can only viewe any account balance
        if(current.isAdmin()){
            bank.checkBalance(name);
            return;
        }

        //user can only view their own account balance
        if(!current.getUsername().equals(name)){
            System.out.println("Access denied: not unauthorized access");
            return;
        }
        bank.checkBalance(name);
    }
}