package edu.temple.cis.c3238.banksim;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Cay Horstmann
 * @author Modified by Paul Wolfgang
 * @author Modified by Charles Wang
 */

class Bank {

    private static final int NTEST = 10;
    private final Account[] accounts;
    private long ntransacts;
    private final int initialBalance;
    private final int numAccounts;
    private boolean open = true;
    ReentrantLock bankLock;

    Bank(int numAccounts, int initialBalance) {
        this.initialBalance = initialBalance;
        this.numAccounts = numAccounts;
        accounts = new Account[numAccounts];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account(this, i, initialBalance);
        }
        ntransacts = 0;
        bankLock = new ReentrantLock();
    }

    void transfer(int from, int to, int amount) {
//        accounts[from].waitForAvailableFunds(amount);
        if (accounts[from].withdraw(amount)) {
            accounts[to].deposit(amount);
        }
        if (shouldTest()) test();
    }

    private void test() {
        Runnable testRunnable = new TestThread(this, accounts, numAccounts, initialBalance);
        Thread testThread = new Thread(testRunnable);
        testThread.start();
    }

    int size() {
        return accounts.length;
    }
    
    
    private boolean shouldTest() {
        return ++ntransacts % NTEST == 0;
    }

    synchronized boolean isOpen(){
        return open;
    }

    void closeBank(){
        synchronized (this){
            open = false;
        }
        for(Account a: accounts){
            synchronized (a){
                a.notifyAll();
            }
        }
    }

}
