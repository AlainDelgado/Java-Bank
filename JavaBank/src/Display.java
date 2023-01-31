import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
    Need to add:
    - Memo is only one word. Which is fine. Just need to deny transfer is user writes more than one word.
    - Get not able to find user with ID: x when you have a second user.
*/
public class Display {
    Scanner sc = new Scanner(System.in);
    Bank bank = new Bank("Java-Bank");
    public void firstScreen() {
        boolean valid = true;
        while (valid) {
            try {
                System.out.println("******************************************");
                System.out.printf("\nWelcome to %s", bank.getName() + "\n" +
                        "Please select one of the following options (1-3)" + "\n" +
                        "1) Create user \n" +
                        "2) Log in \n" +
                        "3) Exit\n");
                System.out.println("\n******************************************\n");
                int userChoice = sc.nextInt();
                switch (userChoice) {
                    case 1:
                        createUser();
                        valid = false;
                    case 2:
                        loginMenu();
                        valid = false;
                    case 3:
                        System.exit(1);
                        valid = false;
                    default:
                        System.err.println("Not an option. Please select again.");
                }
            } catch (Exception e) {
                System.err.println("Invalid input. Try again.");
                sc.next();
            }
        }
    }
    public void createUser() {
        boolean valid = true;
        String regularExpression = "[A-Za-z]{3,15}$";
        while (valid) {
            try {
                System.out.println("******************************************\n");
                System.out.printf("First name: ");
                String firstName = sc.next();
                System.out.printf("Last name: ");
                String lastName = sc.next();
                if (firstName.matches(regularExpression) && lastName.matches(regularExpression)) {
                    System.out.printf("What is your desired pin: ");
                    String pinCode = sc.next();
                    User newUser = bank.addUser(firstName, lastName, pinCode);
                    Account newAcc = new Account("Checking", newUser, bank);
                    newUser.addAccount(newAcc);
                    bank.addAccount(newAcc);
                    firstScreen();
                    valid = false;
                } else {
                    System.err.println("Invalid input. Returning to main menu");
                    firstScreen();
                }
            } catch (Exception e) {
                System.err.println("Invalid input. Returning to main menu");
                firstScreen();
            }
        }
    }
    public void loginMenu(){
            boolean valid = true;
            while (valid) {
                try {
                    System.out.println("******************************************\n");
                    System.out.printf("Please enter your ID: ");
                    int inputID = sc.nextInt();
                    String temp = Integer.toString(inputID);
                    System.out.printf("Please enter your PIN code: ");
                    String pinInput = sc.next();
                    User authUser = bank.userLogin(temp, pinInput);
                    if (authUser == null){
                        System.out.println("Incorrect user ID/pin combination. " + "Please try again.");
                    } else {
                        loggedInScreen(authUser);
                    }
                } catch (Exception e) {
                    System.out.println("Invalid data. ");
                    sc.next();
                }
            }
        }
    public void loggedInScreen(User theUser) {

        boolean valid = true;

        while (valid) {
            try {
                System.out.println("\n******************************************\n");
                System.out.println("Hi, " + theUser.getFirstName() + " " + theUser.getLastName() + ".");
                System.out.println("Please select the following options (1-4)");
                System.out.println("1) Deposit \n2) Withdraw\n3) Transfer Funds \n4) Check balance\n5) Log out\n6) Show account transaction History\n7) Exit and Log out");
                switch (sc.nextInt()) {
                    case 1:
                        depositFunds(theUser);
                        break;
                    case 2:
                        withdrawFunds(theUser);
                        break;
                    case 3:
                        transferFunds(theUser);
                        break;
                    case 4:
                        checkBalance(theUser);
                        break;
                    case 5:
                        firstScreen();
                        break;
                    case 6:
                        viewTransHistory(theUser);
                        break;
                    case 7: sc.close();
                    System.exit(1);
                    default:
                        System.err.println("Not a valid selection. Please try again. ");
                }
            } catch (Exception e) {
                System.err.println("Not a valid input. ");
                sc.next();
            }
        }
    }
    public void depositFunds(User theUser) {
        System.out.println("******************************************\n");
        System.out.println("Which account? \n1) Savings \n2) Checking \n3) Return");
        BigDecimal amount = new BigDecimal("00.00");
        boolean valid = true;
        int accNum = 0;
        BigDecimal funds = new BigDecimal("00.00");
        while (valid) {
            try {
                accNum = sc.nextInt();
                if(accNum == 1 || accNum == 2){
                    if(accNum == 1){
                        System.out.println("Current balance in savings: $" + theUser.getAccounts().get(0).getBalance());
                        System.out.printf("Amount to deposit: ");
                        funds = funds.add(sc.nextBigDecimal());
                        System.out.printf("Enter a memo: ");
                        String memo = sc.next();
                        theUser.addAcctTransaction(accNum-1, funds, memo);
                        valid = false;
                        loggedInScreen(theUser);
                    } else {
                        System.out.println("Current balance in checking: $" + theUser.getAccounts().get(1).getBalance());
                        System.out.printf("Amount to deposit: ");
                        funds = funds.add(sc.nextBigDecimal());
                        System.out.printf("Enter a memo: ");
                        String memo = sc.next();
                        theUser.addAcctTransaction(accNum-1, funds, memo);
                        valid = false;
                        loggedInScreen(theUser);
                    }
                } else if (accNum == 3){
                    System.out.println("Returning.");
                    loggedInScreen(theUser);
                } else {
                    System.err.println("Not a valid selection, please try again.");
                }
            } catch (Exception ex) {
                System.out.println("Not a valid selection. IN CATCH");
                sc.nextInt();
            }
        }
    }
    public void withdrawFunds(User theUser) {
        System.out.println("******************************************\n");
        System.out.println("Which account? \n1) Savings \n2) Checking \n3) Return");
        boolean valid = true;
        int accNum = 0;
        BigDecimal currentBalance = new BigDecimal("00.00");
        BigDecimal funds = new BigDecimal("00.00");
        while (valid) {
            try {
                accNum = sc.nextInt();
                if(accNum == 1 || accNum == 2){
                    if(accNum == 1){
                        System.out.println("Current balance in savings: $" + theUser.getAccounts().get(0).getBalance());
                        System.out.printf("Amount to withdraw: ");
                        currentBalance = currentBalance.add(theUser.getAccounts().get(0).getBalance());
                        funds = funds.add(sc.nextBigDecimal());
                        if(negativeBalance(currentBalance, funds)){
                            System.out.printf("Enter a memo: ");
                            String memo = sc.next();
                            theUser.addAcctTransaction(accNum-1, funds.negate(), memo);
                            valid = false;
                            loggedInScreen(theUser);
                        } else {
                            System.out.println("Returning");
                            withdrawFunds(theUser);
                        }
                    } else {
                        System.out.println("Current balance in checking: $" + theUser.getAccounts().get(1).getBalance());
                        System.out.printf("Amount to withdraw: ");
                        currentBalance = currentBalance.add(theUser.getAccounts().get(0).getBalance());
                        funds = funds.add(sc.nextBigDecimal());
                        if(negativeBalance(currentBalance, funds)){
                            System.out.printf("Enter a memo: ");
                            String memo = sc.next();
                            theUser.addAcctTransaction(accNum-1, funds.negate(), memo);
                            valid = false;
                            loggedInScreen(theUser);
                        } else {
                            System.out.println("Returning");
                            withdrawFunds(theUser);
                        }
                    }
                } else if (accNum == 3){
                    System.out.println("Returning.");
                    loggedInScreen(theUser);
                } else {
                    System.err.println("Not a valid selection, please try again.");
                }
            } catch (Exception ex) {
                System.out.println("Not a valid selection. IN CATCH");
                sc.nextInt();
            }
        }
    }
    public void transferFunds(User theUser) {
        System.out.println("******************************************\n");
        System.out.println("Which account to transfer from? \n1) Savings \n2) Checking \n3) Return");
        BigDecimal amount = new BigDecimal("00.00");
        boolean valid = true;
        int accNum = 0;
        BigDecimal funds = new BigDecimal("00.00");
        BigDecimal currentBalance = new BigDecimal("00.00");
        while (valid) {
            try {
                accNum = sc.nextInt();
                if(accNum == 1 || accNum == 2){
                    if(accNum == 1){
                        System.out.println("Current balance in savings: $" + theUser.getAccounts().get(0).getBalance());
                        System.out.printf("Transfer amount to Checking: ");
                        funds = funds.add(sc.nextBigDecimal());
                        currentBalance = currentBalance.add(theUser.getAccounts().get(accNum-1).getBalance());
                        if(negativeBalance(currentBalance, funds)){
                            amount = funds.negate();
                            System.out.printf("Enter memo: ");
                            String memo = sc.next();
                            theUser.addAcctTransaction(accNum-1, funds.negate(), memo);
                            theUser.addAcctTransaction(accNum, amount, memo);
                            valid = false;
                            loggedInScreen(theUser);
                        } else {
                            System.out.println("Returning");
                            transferFunds(theUser);
                        }
                    } else {
                        System.out.println("Current balance in checking: $" + theUser.getAccounts().get(1).getBalance());
                        System.out.printf("Transfer amount to Savings: ");
                        funds = funds.add(sc.nextBigDecimal());
                        currentBalance = currentBalance.add(theUser.getAccounts().get(accNum-1).getBalance());
                        if(negativeBalance(currentBalance, funds)){
                            amount = funds.negate();
                            System.out.printf("Enter memo: ");
                            String memo = sc.next();
                            theUser.addAcctTransaction(accNum-1, funds.negate(), memo);
                            theUser.addAcctTransaction(accNum-2, amount, memo);
                            valid = false;
                            loggedInScreen(theUser);
                        } else {
                            System.out.println("Returning");
                            transferFunds(theUser);
                        }
                    }
                } else if (accNum == 3){
                    System.out.println("Returning.");
                    loggedInScreen(theUser);
                } else {
                    System.err.println("Not a valid selection, please try again.");
                }
            } catch (Exception ex) {
                System.out.println("Not a valid selection. IN CATCH");
                sc.nextInt();
            }
        }
    }
    public void checkBalance(User theUser) {
        boolean valid = true;
        while (valid){
            try{
                System.out.println("\n******************************************\n");
                System.out.println(theUser.getAccounts().get(0).getName() + " $" + theUser.getAccounts().get(0).getBalance());
                System.out.println(theUser.getAccounts().get(1).getName() + " $" + theUser.getAccounts().get(1).getBalance());
                System.out.println("Press (R) to return or (E) to log out and exit. ");
                System.out.println();
                System.out.println("\n******************************************");
                String ret = sc.next();
                if(ret.equalsIgnoreCase("R")){
                    loggedInScreen(theUser);
                    valid = false;
                } else if (ret.equalsIgnoreCase("E")){
                    System.out.println("Goodbye!");
                    System.exit(1);
                } else {
                    System.out.println("Not a valid selection. Please try again.");
                }
            } catch (Exception e){
                System.out.println("Invalid selection. Please try again.");
            }
        }
    }
    public void viewTransHistory(User theUser){
        int theAcct;

        // get account whose transaction history to look at
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "whose transactions you want to see: "
                    , theUser.numAccounts());
            theAcct = sc.nextInt()-1;
            if(theAcct < 0 || theAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again. ");
            }
        } while(theAcct < 0 || theAcct >= theUser.numAccounts());

        // print the transaction history
        theUser.printAccTransHistory(theAcct);


    }
    public static boolean negativeBalance (BigDecimal currentBalance, BigDecimal amount){
        boolean enoughFunds = false;
        if(currentBalance.compareTo(amount) >= 0){
            enoughFunds = true;
        } else {
            System.err.println("Not enough funds");
            enoughFunds = false;
        }
        return enoughFunds;
    }
}
