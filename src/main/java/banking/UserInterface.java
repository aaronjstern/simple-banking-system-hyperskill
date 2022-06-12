package banking;

import java.sql.SQLException;
import java.util.Scanner;

public class UserInterface{

    private boolean uiOn;
    private AccountServices accountServices;
    Scanner scanner;

    public UserInterface(AccountServices accountServices) {
        scanner = new Scanner(System.in);
        this.accountServices = accountServices;
        this.uiOn = true;

    }

    public void start() throws SQLException {

        while (uiOn) {
            System.out.println("1. Create an account\n2. Log into account\n0. Exit\n");

            int command = Integer.parseInt(scanner.nextLine());
            switch (command) {
                case 1:
                    accountServices.createAccount();
                    break;
                case 2:
                    if (accountServices.logIntoAccount()){
                        startLoggedIn();
                    } else {
                        System.out.println("Wrong Card number or pin");
                    }
                    break;
                case 0:
                    System.out.println("Bye!");
                    uiOn = false;
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
    }

    public void startLoggedIn() throws SQLException {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("1. Balance\n2. Add income\n3. Do transfer\n4. Close account\n5. Log Out\n0. Exit\n");
            int command = Integer.parseInt(scanner.nextLine());
            switch (command) {
                case 1:
                    String currentBalance = accountServices.getCurrentAccount().toString();
                    System.out.println(currentBalance);
                    break;
                case 2:
                    accountServices.addMoney();
                    break;
                case 3:
                    accountServices.doTransfer();
                    break;
                case 4:
                    accountServices.deleteAccount();
                    break;
                case 5:
                    loggedIn = false;
                    accountServices.logOutOfAccount();
                    break;
                case 0:
                    loggedIn = false;
                    uiOn = false;
                    System.out.println("Bye!");
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
    }
}
