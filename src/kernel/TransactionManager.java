package kernel;

import system.BankDatabase;

public class TransactionManager{
    private BankDatabase bank;

    public TransactionManager(BankDatabase bank){
        this.bank=bank;
    }

    public void executeTransfer(String from, String to, double amount){

        Thread t=new Thread(() -> {bank.transfer(from, to, amount);});
        t.start();
        try{
            t.join();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        return;

    }
}