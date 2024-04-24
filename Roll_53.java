import java.util.Random;
import java.util.*;


/*-----------------------------------------
            Custom Exception
 -------------------------------------*/
class TransactionLimitException extends Exception {
    public TransactionLimitException(String message) {
        super(message);
    }
}


/*-----------------------------------------
            Account Class Processing
 -------------------------------------*/

class Account {
    private String name;
    private String accountNumber;
    private int balance;
    private int transactionLimit;

    public Account(String name, String accountNumber, int balance, int transactionLimit) {

        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.transactionLimit = transactionLimit;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setTransactionLimit(int transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    public String getName() {
        return name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public int getBalance() {
        return balance;
    }

    public int getTransactionLimit() {
        return transactionLimit;
    }

    public void addBalance(Integer x) {
        balance += x;
    }
    public void withdraw(Integer x) throws TransactionLimitException {
        if (balance >= x)
            balance -= x;
        else {
            balance = 0;
            throw new TransactionLimitException("Maximum WithdrawTransaction Limit Violated");
        }
    }

    public void printInfo() {
        System.out.println("Name: " + name);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Balance: " + balance);
        System.out.println("Transaction Limit: " + transactionLimit);
    }
}



/*-----------------------------------------
            Account Generator
 -------------------------------------*/

class AccountGenerationThread extends Thread {
    private final Account[] arr;
    private final int NUM_ACCOUNTS = 30;
    private final int MAX_TRANSACTION_LIMIT = 1000;
    private final int SLEEP_TIME_MS = 100;
    private final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private final String[] FIRST_NAMES = {
        "Tauseef",
        "Tamzid",
        "Shahriar",
        "Ashraful",
        "Faiaz",
        "Mr.",
        "Anik",
        "Tamim",
        "Amio",
        "Mithu",
        "Manob",
        "Niloy",
        "Rana",
        "Rohan",
        "Tamzid",
        "Fahim",
        "Kawser",
        "Rohul",
        "Naimul",
        "Hemal"
    };
    private final String[] LAST_NAMES = {
        "Rahman",
        "Alam",
        "Dewan",
        "bin Tariq",
        "Hasan",
        "Kabir",
        "Mahmud",
        "Mahmud Ifti",
        "Sheikh",
        "Karim",
        "Amin",
        "Chandu"
    };
    private Random random;

    public AccountGenerationThread(Account[] arr) {
        this.random = new Random();
        this.arr = arr;
    }

    @Override
    public void run() {
        for (int i = 1; i <= NUM_ACCOUNTS; i++) {
            String accountHolderName = generateRandomName();
            String accountNumber = generateRandomAccountNumber();
            int maxTransactionLimit = generateRandomTransactionLimit();

            // Simulate creating account
            System.out.println("Created Account " + i + ":");
            System.out.println("Account Holder Name: " + accountHolderName);
            System.out.println("Account Number: " + accountNumber);
            System.out.println("Maximum Transaction Limit: " + maxTransactionLimit + " BDT");
            System.out.println();

            arr[i - 1] = new Account(accountHolderName, accountNumber, 0, maxTransactionLimit);

            try {
                Thread.sleep(SLEEP_TIME_MS); // Sleep for 1000 milliseconds
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    private String generateRandomName() {
        // Generating a random name (for demonstration purpose, you may replace it with your actual logic)
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        return firstName + " " + lastName;
    }

    private String generateRandomAccountNumber() {
        String accountNumber = "";
        accountNumber += LETTERS.charAt(random.nextInt(LETTERS.length()));
        accountNumber += LETTERS.charAt(random.nextInt(LETTERS.length()));
        // accountNumber.append(generateRandomLetters(2)); // Prefix with two random letters
        for (int i = 0; i < 10; i++) {
            accountNumber += (random.nextInt(10)); // Append 10 random digits
        }
        return accountNumber.toString();
    }

    private int generateRandomTransactionLimit() {
        return random.nextInt(MAX_TRANSACTION_LIMIT) + 1; // Random integer between 1-1000
    }

}


/*-----------------------------------------
            Deposit Generator
 -------------------------------------*/

class DepositGenerationThread extends Thread {
    private static final int NUM_TRANSACTIONS = 50;
    private static final int MAX_DEPOSIT_AMOUNT = 1000;
    private static final int SLEEP_TIME_MS = 100;
    private static final Random random = new Random();
    // private final String[] accountNumbers; // Shared array of account numbers generated in the previous thread
    // private final int[] depositArr; 
    private final ArrayList < Integer > [] depositArr;

    public DepositGenerationThread(ArrayList < Integer > [] depositArr) {
        this.depositArr = depositArr;
    }
    @Override
    public void run() {

        for (int i = 1; i <= NUM_TRANSACTIONS; i++) {
            int accountNumber = random.nextInt(30);
            int depositAmount = generateRandomDepositAmount();

            // Simulate creating deposit transaction
            System.out.println("---------------");
            System.out.println("Created Deposit Transaction " + i + ":");
            System.out.println("Account Number: " + accountNumber);
            System.out.println("Deposit Amount: " + depositAmount + " BDT");
            System.out.println();

            depositArr[accountNumber].add(depositAmount);

            // Put the transaction into the shared array for processing

            try {
                Thread.sleep(SLEEP_TIME_MS); // Sleep for 1000 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int generateRandomDepositAmount() {
        return random.nextInt(MAX_DEPOSIT_AMOUNT) + 1; // Random integer between 1-50000
    }
}


/*-----------------------------------------
            WithDraw Generator --------------------------------
 -------------------------------------*/

class WithdrawGenerationThread extends Thread {
    private static final int NUM_TRANSACTIONS = 50;
    private static final int MAX_WITHDRAW_AMOUNT = 1000;
    private static final int SLEEP_TIME_MS = 100;
    private static final Random random = new Random();
    // private final String[] accountNumbers; // Shared array of account numbers generated in the previous thread
    private final ArrayList < Integer > [] withdrawArr;

    public WithdrawGenerationThread(ArrayList < Integer > [] withdrawArr) {
        this.withdrawArr = withdrawArr;
    }

    @Override
    public void run() {
        for (int i = 1; i <= NUM_TRANSACTIONS; i++) {
            int accountNumber = random.nextInt(30);
            int withdrawAmount = generateRandomDepositAmount();

            // Simulate creating deposit transaction
            System.out.println("---------------");
            System.out.println("Created Withdraw Transaction " + i + ":");
            System.out.println("Account Number: " + accountNumber);
            System.out.println("Deposit Amount: " + withdrawAmount + " BDT");
            System.out.println();

            withdrawArr[accountNumber].add(withdrawAmount);
            // Put the transaction into the shared array for processing

            try {
                Thread.sleep(SLEEP_TIME_MS); // Sleep for 1000 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int generateRandomDepositAmount() {
        return random.nextInt(MAX_WITHDRAW_AMOUNT) + 1; // Random integer between 1-50000
    }
}



/*-----------------------------------------
            Deposit Processing
 -------------------------------------*/
class DepositProcessing extends Thread {
    private final Account[] arr;
    private final ArrayList < Integer > [] depositArr;
    DepositProcessing(ArrayList < Integer > [] depositArr, Account[] arr) {
        this.depositArr = depositArr;
        this.arr = arr;
    }

    public void check(int a, Integer b, int i) throws TransactionLimitException {
        if (b > a) throw new TransactionLimitException("Maximum DepositTransaction Limit Violated" + "Deposit : " + b + " Max : " + a + " for user : " + i);
    }

    public synchronized void run() {
        for (int i = 0; i < 30; i++) {
            for (Integer ele: depositArr[i]) {
                try {
                    check(arr[i].getTransactionLimit(), (int) ele, i + (int) 1);
                    arr[i].addBalance(ele);
                } catch (Exception e) {
                    System.out.println((e));
                }
            }
            System.out.println();
        }
    }

}



/*-----------------------------------------
            WithDraw Processing
 -------------------------------------*/
class WithdrawProcessing extends Thread {
    private final Account[] arr;
    private final ArrayList < Integer > [] withdrawArr;
    WithdrawProcessing(ArrayList < Integer > [] withdrawArr, Account[] arr) {
        this.withdrawArr = withdrawArr;
        this.arr = arr;
    }

    public void check(int a, Integer b, int i) throws TransactionLimitException {
        if (b > a) throw new TransactionLimitException("Maximum DepositTransaction Limit Violated" + "Deposit : " + b + " Max : " + a + " for user : " + i);
    }

    public synchronized void run() {
        for (int i = 0; i < 30; i++) {
            for (Integer ele: withdrawArr[i]) {
                try {
                    check(arr[i].getTransactionLimit(), (int) ele, i + (int) 1);
                    try {
                        arr[i].withdraw(ele);
                    } catch (Exception e) {
                        // TODO: handle exception
                        System.out.println(e);
                    }
                } catch (Exception e) {
                    System.out.println((e));
                }
            }
            System.out.println();
        }
    }

}

public class Roll_53 {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Account[] arr = new Account[30];

        ArrayList < Integer > [] depositArr = new ArrayList[30];
        ArrayList < Integer > [] withdrawArr = new ArrayList[30];
        for (int i = 0; i < 30; i++) {
            depositArr[i] = new ArrayList < > ();
            withdrawArr[i] = new ArrayList < > ();
        }

        AccountGenerationThread accountGenerationThread = new AccountGenerationThread(arr);
        DepositGenerationThread depositGenerationThread = new DepositGenerationThread(depositArr);
        WithdrawGenerationThread withdrawGenerationThread = new WithdrawGenerationThread(withdrawArr);

        accountGenerationThread.start();
        depositGenerationThread.start();
        withdrawGenerationThread.start();

        try {
            accountGenerationThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            depositGenerationThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            withdrawGenerationThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        Random random = new Random();
        int worker_thread = random.nextInt(4) + 2; // Random Number between 2 and 5

        DepositProcessing[] depositProcessing = new DepositProcessing[worker_thread];

        for (int i = 0; i < worker_thread; i++) {
            depositProcessing[i] = new DepositProcessing(depositArr, arr);
            depositProcessing[i].start();

        }

        for (int i = 1; i <= worker_thread; i++) {
            try {
                depositProcessing[i].join();
            } catch (Exception e) {
            }
        }

        WithdrawProcessing[] withdrawProcessings = new WithdrawProcessing[worker_thread];

        for (int i = 0; i < worker_thread; i++) {
            withdrawProcessings[i] = new WithdrawProcessing(depositArr, arr);
            withdrawProcessings[i].start();

        }

        for (int i = 1; i <= worker_thread; i++) {
            try {
                withdrawProcessings[i].join();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        System.out.println("--------------------------------------------------------");
        System.out.println("--------------------------------------------------------");
        System.out.println("---------     REMAINING MONEY  -------------------");
        System.out.println("--------------------------------------------------------");
        System.out.println("--------------------------------------------------------");

        int count = 1;
        for (Account account: arr) {
            System.out.println(count++);
            if (account != null) { // Check if the account object is not null
                account.printInfo();
            } else {
                System.out.println("Account object is null");
            }
        }

        // Print All deposited Money
        // for(int i = 0; i < 30; i++)
        // {
        //     System.out.print(i + " -> ");
        //     for(Integer ele : depositArr[i])
        //     {
        //         System.out.print(ele + " ");
        //     }
        //     System.out.println();
        // }

        // Print All Withdrawed Money
        // for(int i = 0; i < 30; i++)
        // {
        //     System.out.print(i + " -> ");
        //     for(Integer ele : withdrawArr[i])
        //     {
        //         System.out.print(ele + " ");
        //     }
        //     System.out.println();
        // }

    }
}