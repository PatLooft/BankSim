package edu.temple.cis.c3238.banksim;

class TestThread implements Runnable {

    private final Account[] accounts;
    private final int numAccounts;
    private final int initialBalance;

    TestThread(Account[] accounts, int numAccounts, int initialBalance) {
        this.accounts = accounts;
        this.numAccounts = numAccounts;
        this.initialBalance = initialBalance;
    }

    @Override
    public void run() {
        int sum = 0;
        for (Account account : accounts) {
            System.out.printf("%s %s%n",
                    Thread.currentThread().toString(), account.toString());
            sum += account.getBalance();
        }
        System.out.println(Thread.currentThread().toString() +
                " Sum: " + sum);
        if (sum != numAccounts * initialBalance) {
            System.out.println(Thread.currentThread().toString() +
                    " Money was gained or lost");
            System.exit(1);
        } else {
            System.out.println(Thread.currentThread().toString() +
                    " The bank is in balance");
        }
    }
}
