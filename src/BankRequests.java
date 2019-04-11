interface BankRequests {

    double getMoneyInAccount(Account account);
    boolean lockMoneyInAccount(Double amount, Account account);
    boolean transferMoney(Account a1, Account a2);
    boolean addMoney(Double amount, Account account);

}
