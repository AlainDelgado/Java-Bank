import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bank {
    private String name;
    private List<User> users;
    private List<Account> accounts;
    public Bank (String name){
        this.name = name;
        this.users = new ArrayList<User>();
        this.accounts = new ArrayList<Account>();
    }
    public int getNewUserId(){
        int id = 0;
        Random rng = new Random();
        int len = 1;
        boolean nonUnique ;
        do {
            for (int i = 0; i < len; i++) {
                id += (rng.nextInt(10));
            }
            nonUnique = false;

            for(User u : this.users){
                if (id == u.getUserID()) {
                    nonUnique = true;
                    break;
                }
            }
        } while (nonUnique);
        return id;
    }
    public void addAccount (Account anAcct){
        this.accounts.add(anAcct);
    }
    public User addUser (String firstName, String lastName, String pin){
        // create a new User object and add it to our list.
        User newUser = new User(firstName, lastName, pin, this);
        this.users.add(newUser);

        // create a saving accounts for the user and add to User and Bank
        // accounts lists
        Account newAccount = new Account("Savings", newUser, this);

        newUser.addAccount(newAccount);
        this.accounts.add(newAccount);

        return newUser;
    }
    public String getNewAccountUUID (){
        // inits
        String uuid;
        Random rng = new Random();
        int len = 10;
        boolean nonUnique;

        // do while loop. Continue looping until we get an unique ID
        do {
            // generate the number
            uuid = "";
            for (int c = 0; c < len; c++) {
                uuid += ((Integer)rng.nextInt(10)).toString();
            }
            // check to make sure it's unique
            nonUnique = false;
            for(Account a : this.accounts){
                if (uuid.compareTo(a.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }
        } while (nonUnique);
        return uuid;
    }

    public User userLogin (String userId, String pin){
        // search thorugh the list of users
        for (User u : this.users) {
            String temp = String.valueOf(u.getUserID());
            if(temp.compareTo(userId) == 0 && u.validatePin (pin)) {
                return u;
            }
        }
        // if we haven't found the user or have an incorrrect pin.
        return null;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
