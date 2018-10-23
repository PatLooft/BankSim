package edu.temple.cis.c3238.banksim;

/**
 * @author Cay Horstmann
 * @author Modified by Paul Wolfgang
 * @author Modified by Charles Wang
 */

class Bank {

    public static final int NTEST = 10;
    private final Account[] accounts;
    private long ntransacts = 0;
    private final int initialBalance;
    private final int numAccounts;

    Bank(int numAccounts, int initialBalance) {
        this.initialBalance = initialBalance;
        this.numAccounts = numAccounts;
        accounts = new Account[numAccounts];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account(this, i, initialBalance);
        }
        ntransacts = 0;
    }

    void transfer(int from, int to, int amount) {
//        accounts[from].waitForAvailableFunds(amount);
        if (accounts[from].withdraw(amount)) {
            accounts[to].deposit(amount);
        }
        if (shouldTest()) test();
    }

    void test() {
        Runnable testRunnable = new TestThread(accounts, numAccounts, initialBalance);
        Thread testThread = new Thread(testRunnable);
        testThread.start();
    }

    int size() {
        return accounts.length;
    }
    
    
    boolean shouldTest() {
        return ++ntransacts % NTEST == 0;
    }

}
