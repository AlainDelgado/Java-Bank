import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class User {
    private String firstName;
    private String lastName;
    private byte pinHash[];
    private int userID;
    private List<Account> accounts;
    public User(String firstName, String lastName, String pin, Bank theBank){
        // user's name
        this.firstName = firstName;
        this.lastName = lastName;
        // store pin's MD5 hash, rather than the original value, for security reasons.
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }
        // get a new, unique unversal ID for the user.
        this.userID = theBank.getNewUserId();

        // create empy list of accounts.
        this.accounts = new ArrayList<>();
        // print log message
        System.out.printf("New user %s %s with ID %s created. \n", lastName, firstName, this.userID);

    }
    public boolean validatePin (String aPin){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(aPin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }
    public void addAccount (Account anAcct){
        this.accounts.add(anAcct);
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public List<Account> getAccounts() {
        return this.accounts;
    }
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public void addAcctTransaction(int acctIdx, BigDecimal amount, String memo){
        this.accounts.get(acctIdx).addTransaction(amount, memo);
    }
    public void printAccountsSummary(){
        System.out.printf("\n\n%s's accounts summary\n" , this.firstName);
        for (int a = 0; a < this.accounts.size(); a++) {
            System.out.printf("  %d) %s\n", a+1,
                    this.accounts.get(a).getSummaryLine());
        }
        System.out.println();

    }
    public int numAccounts(){
        return this.accounts.size();
    }
    public void printAccTransHistory(int acctIdx){
        this.accounts.get(acctIdx).printTransHistory();
    }
}
