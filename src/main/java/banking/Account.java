package banking;

public class Account {

    private int id;
    private final String cardNumber;
    private String pinNumber;
    private int balance;

    public Account(int id, String cardNumber, String pinNumber) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.pinNumber = pinNumber;
        this.balance = 0;
    }

    public Account(int id, String cardNumber, String pinNumber, int balance) {
        this(id, cardNumber, pinNumber);
        this.balance = balance;
    }


    public String getCardNumber() {
        return cardNumber;
    }

    public String getPinNumber() {
        return pinNumber;
    }

    public int getBalance() {
        return balance;
    }

    public void addMoney(int deposit) {
        balance += deposit;
    }

    public void withdrawMoney(int amount) {
        balance -= amount;
    }

    public String toString() {
        return "Balance: " + balance;
    }

}
