package shell;

import kernel.TransactionManager;

public class CommandParser{
    private TransactionManager tm;

    public CommandParser(TransactionManager tm){
        this.tm=tm;
    }


    public void parse(String command){
        command=command.trim();
        if(command.isEmpty()){
            return;
        }
        String tokens[]=command.split("\\s+");

        if(tokens[0].equalsIgnoreCase("create")){
            if(tokens.length<3){
                System.out.println("Usage: create <name> <balance>");
                return;
            }
            String name = tokens[1];
            double balance= Double.parseDouble(tokens[2]);

            tm.createAccount(name,balance);

        }
        else if(tokens[0].equalsIgnoreCase("balance")){
            if(tokens.length<2){
                System.out.println("Usage: balance <name>");
                return;
            }
            String name=tokens[1];
            tm.checkBalance(name);
        }
        else if(tokens[0].equalsIgnoreCase("transfer")){
            if(tokens.length<4){
                System.out.println("Usage: tranfer <sender_name> <receiver_name> <amount>");
                return;
            }
            try{
                double amount=Double.parseDouble(tokens[3]);
                tm.transfer(tokens[1],tokens[2],amount);
            }
            catch(NumberFormatException e){
                System.out.println("Invalid amount.");
            }
        }
        else if(tokens[0].equalsIgnoreCase("register")){
            if(tokens.length<3){
                System.out.println("Usage: register <username> <password>");
                return;
            }
            tm.register(tokens[1],tokens[2]);
        }
        else if(tokens[0].equalsIgnoreCase("login")){
            if(tokens.length<3){
                System.out.println("Usage: login <username> <password>");
                return;
            }
            tm.login(tokens[1],tokens[2]);
        }
        else if(tokens[0].equalsIgnoreCase("logout")){
            if(tokens.length<3){
                System.out.println("Usage: logout <username> <password>");
                return;
            }
            tm.logout(tokens[1],tokens[2]);
        }
        else if(tokens[0].equalsIgnoreCase("grant")){
            if(tokens.length<3){
                System.out.println("Usage: grant <username>");
                return;
            }
            tm.grantTransfer(tokens[1]);
        }
        else if(tokens[0].equalsIgnoreCase("revoke")){
            if(tokens.length<3){
                System.out.println("Usage: revoke <username>");
                return;
            }
            tm.revokeTransfer(tokens[1]);
        }
        else if(tokens[0].equalsIgnoreCase("delete")){
            if(tokens.length<3){
                System.out.println("Usage: delete <username> ");
                return;
            }
            tm.deleteUser(tokens[1]);
        }
        else{
            System.out.println(tokens[0]+" is not a valid command.");
            System.out.println("Unknown command. Type 'help' for a list of commands.*Reminder to add help command*");
        }
        return;
    }
}
