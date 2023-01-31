
import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private BigDecimal amount;
    private Date timeStamp;
    private String memo;
    private Account inAccount;
    public Transaction(BigDecimal amount, Account inAccount){
        this.amount = amount;
        this.inAccount = inAccount;
        this.timeStamp = new Date();
        this.memo = "";

    }
    public Transaction(BigDecimal amount, String memo, Account inAccount){
        this(amount, inAccount);
        this.memo = memo;
    }
    public BigDecimal getAmount() {
        return this.amount;
    }
    public String getSummaryLine(){
        if(this.amount.compareTo(BigDecimal.ZERO) >= 0) {
            return String.format("%s : $%.02f : %s", this.timeStamp.toString(),
                    this.amount, this.memo);
        } else {
            return String.format("%s : $(%.02f) : %s", this.timeStamp.toString(),
                    this.amount, this.memo);
        }
    }
}
