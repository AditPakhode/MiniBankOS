package shell;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Terminal {
    private CommandParser parser;
    private BlockingQueue<String> commandQueue=new LinkedBlockingQueue<>();

    public Terminal(){
        parser=new CommandParser();
    }
    public void start(){
        Scanner sc=new Scanner(System.in);
        
        System.out.println("Booting BankingOS...");
        System.out.println("BankingOS Terminal Ready. Type 'help' for a list of commands. *Reminder to add help command*\n Type 'exit' to shutdown.");

        Thread inputThread=new Thread(() -> {

            while(true){
                System.out.print("BankingOS@terminal$: ");
                
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
                parser.parse(command);

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
