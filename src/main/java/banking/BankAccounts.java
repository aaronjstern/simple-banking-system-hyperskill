package banking;

import java.util.ArrayList;

public class BankAccounts {

    ArrayList<Account> bankAccountsList;

    public BankAccounts() {
        this.bankAccountsList = new ArrayList<>();
    }

    public void addAccount(Account account) {
        this.bankAccountsList.add(account);
    }
}
