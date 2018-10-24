package edu.temple.cis.c3238.banksim;

class TestThread implements Runnable {

    private final Bank bank;
    private final Account[] accounts;
    private final int numAccounts;
    private final int initialBalance;

    TestThread(Bank bank, Account[] accounts, int numAccounts, int initialBalance) {
        this.bank = bank;
        this.accounts = accounts;
        this.numAccounts = numAccounts;
        this.initialBalance = initialBalance;
    }

    @Override
    public void run() {
        bank.bankLock.lock();
        bank.setTesting(true);
        while (bank.bankLock.getQueueLength() < numAccounts) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
            bank.bankLock.unlock();
            System.exit(1);
        } else {
            System.out.println(Thread.currentThread().toString() +
                    " The bank is in balance");
        }
        bank.setTesting(false);
        bank.bankLock.unlock();
    }
}
