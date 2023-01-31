import java.math.BigDecimal;
import java.util.ArrayList;

public class Account {
    private String name;
    private String uuid;
    private User holder;
    private ArrayList<Transaction> transactions;
    public Account(String name, User holder, Bank theBank){
        this.name = name;
        this.holder = holder;
        this.uuid = theBank.getNewAccountUUID();
        this.transactions = new ArrayList<Transaction>();
    }
    public String getUUID (){
        return this.uuid;
    }
    public String getSummaryLine(){
        BigDecimal balance = this.getBalance();
        if(balance.compareTo(BigDecimal.ZERO) >= 0){
            return String.format("%s : $%.02f : %s", this.uuid, balance, this.name);
        } else {
            return String.format("%s : $(%.02f) : %s", this.uuid, balance, this.name);
        }
    }
    public BigDecimal getBalance(){
        BigDecimal balance = new BigDecimal("00.00");
        for (Transaction t : this.transactions) {
            balance = balance.add(t.getAmount());
        }
        return balance;
    }
    public void printTransHistory (){
        System.out.println("\n******************************************\n");
        System.out.printf("\nTransaction history for account %s\n", this.uuid);
        for (int t = this.transactions.size()-1; t >= 0; t--) {
            System.out.println(this.transactions.get(t).getSummaryLine());
        }
        System.out.println();
    }
    public void addTransaction(BigDecimal amount, String memo){
            Transaction newTrans = new Transaction(amount, memo, this);
            this.transactions.add(newTrans);
    }
    public String getName() {
        return name;
    }
}