package banking;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        if (args[0].equals("-fileName")) {
            BankAccounts bankAccounts = new BankAccounts();
            AccountServices accountServices = new AccountServices(bankAccounts, args[1]);
            UserInterface ui = new UserInterface(accountServices);
            ui.start();
        }

    }

}

