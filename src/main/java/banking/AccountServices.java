package banking;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class AccountServices {

    Scanner scanner;
    private BankAccounts bankAccounts;
    private Account currentAccount;
    static Random random = new Random();
    private DatabaseConnector databaseConnector;

    public AccountServices(BankAccounts bankAccounts, String fileName) throws SQLException {
        this.databaseConnector = new DatabaseConnector(fileName);
        scanner = new Scanner(System.in);
        this.bankAccounts = bankAccounts;
    }

    public void createAccount(){
        String pinNumber = randomNumStringGenerator(4);

        ArrayList<Integer> luhnCandidate = luhnCandidateGenerator();

        while (!(validLuhn(luhnCandidate))) {
            luhnCandidate = luhnCandidateGenerator();
        }

        StringBuilder validLuhnString = new StringBuilder();
        for (Integer integer : luhnCandidate) {
            String stringInt = String.valueOf(integer);
            validLuhnString.append(stringInt);
        }

        String cardNumber = validLuhnString.toString();

        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(validLuhnString);
        System.out.println("Your card PIN:");
        System.out.println(pinNumber);
        databaseConnector.addNewCard(String.valueOf(validLuhnString), pinNumber);
    }

    public static String randomNumStringGenerator(int length){
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomInt = random.nextInt(10);
            String stringInt = String.valueOf(randomInt);
            number.append(stringInt);
        }
        return number.toString();
    }

    public boolean validLuhn(ArrayList<Integer> cardNumber){

        ArrayList<Integer> luhnArrayCandidate = new ArrayList<>(cardNumber);

        int checkSum = luhnArrayCandidate.remove(luhnArrayCandidate.size()-1);
        for (int i = 0; i < luhnArrayCandidate.size(); i++) {
            if (i % 2 == 0) {
                int digit = luhnArrayCandidate.get(i);
                int newDigit = digit * 2 < 9 ? digit * 2 : digit * 2 - 9;
                luhnArrayCandidate.set(i, newDigit);
            }
        }
        int sum = 0;
        for (int i : luhnArrayCandidate) {
            sum += i;
        }
        int luhnSum = sum + checkSum;
        return luhnSum % 10 == 0;
    }

    public ArrayList<Integer> luhnCandidateGenerator() {
        String cardNumber = "400000" + randomNumStringGenerator(10);
        ArrayList<Integer> cardNumberInt= new ArrayList<>();
        for (int i = 0; i < cardNumber.length(); i++) {
            cardNumberInt.add(Integer.parseInt(String.valueOf(cardNumber.charAt(i))));
        }
        return cardNumberInt;
    }

    public boolean logIntoAccount() throws SQLException {
        System.out.println("Enter your card number:");
        String cardNum = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pinNum = scanner.nextLine();

        boolean validAccount = false;

        Account account = databaseConnector.getAccount(cardNum, pinNum);

        if (Objects.nonNull(account)) {
            setCurrentAccount(account);
            System.out.println("You have successfully logged in!");
            validAccount = true;
        }

        return validAccount;
    }

    public void logOutOfAccount() {
        setCurrentAccount(null);
        System.out.println("You have successfully logged out!");
    }

    public void setCurrentAccount (Account account) {
        this.currentAccount = account;
    }

    public Account getCurrentAccount() {
        return this.currentAccount;
    }

    public void addMoney() throws SQLException {
        System.out.println("Enter income:");
        int deposit = Integer.parseInt(scanner.nextLine());
        currentAccount.addMoney(deposit);
        databaseConnector.updateBalance(currentAccount);
        System.out.println("Income was added");
    }

    public void doTransfer() throws SQLException {
        System.out.println("Transfer");
        boolean cardCheckOngoing = true;

        boolean transferInProgress = true;

        Account otherAccount = null;

        while (transferInProgress) {
            while (cardCheckOngoing) {
                System.out.println("Enter card number:");
                String transferToCard = scanner.nextLine();
                ArrayList<Integer> cardNumberInt= new ArrayList<>();
                for (int i = 0; i < transferToCard.length(); i++) {
                    cardNumberInt.add(Integer.parseInt(String.valueOf(transferToCard.charAt(i))));
                }

                if (!(validLuhn(cardNumberInt))) {
                    System.out.println("Probably you made a mistake in the card number. Please try again!");
                } else {
                    otherAccount = databaseConnector.getAccount(transferToCard);
                    if ((Objects.isNull(otherAccount))) {
                        System.out.println("Such a card does not exist");
                        cardCheckOngoing = false;
                        transferInProgress = false;
                        break;
                    } else if (currentAccount.getCardNumber().equals(otherAccount.getCardNumber())){
                        System.out.println("You can't transfer money to the same account");
                        cardCheckOngoing = false;
                        transferInProgress = false;
                        break;
                    }
                    else {
                        cardCheckOngoing = false;
                        break;
                    }

                }
            }
            if (!(transferInProgress)) {
                break;
            }
            System.out.println("How much money do you want to transfer:");
            int amount = Integer.parseInt(scanner.nextLine());
            if (amount >  currentAccount.getBalance()) {
                System.out.println("Not enough money!");
                transferInProgress = false;
            } else {
                currentAccount.withdrawMoney(amount);
                otherAccount.addMoney(amount);
                databaseConnector.updateBalance(currentAccount);
                databaseConnector.updateBalance(otherAccount);
                System.out.println("Success!");
                transferInProgress = false;
            }
        }
    }

    public void deleteAccount()  {
        String accountNumber = currentAccount.getCardNumber();
        databaseConnector.deleteAccount(accountNumber);
    }

}
