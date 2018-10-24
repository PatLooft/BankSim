package edu.temple.cis.c3238.banksim;

/**
 * @author Cay Horstmann
 * @author Modified by Paul Wolfgang
 * @author Modified by Charles Wang
 */
public class Account {

    private volatile int balance;
    private final int id;
    private final Bank myBank;

    Account(Bank myBank, int id, int initialBalance) {
        this.myBank = myBank;
        this.id = id;
        balance = initialBalance;
    }

    int getBalance() {
        return balance;
    }

    synchronized boolean withdraw(int amount) {
        if (amount <= balance) {
            int currentBalance = balance;
//            Thread.yield(); // Try to force collision
            balance = currentBalance - amount;
            return true;
        } else {
            return false;
        }
    }

    synchronized void deposit(int amount) {
        int currentBalance = balance;
//        Thread.yield();   // Try to force collision
        balance = currentBalance + amount;
        notifyAll();
    }

    synchronized void waitForAvailableFunds(int amount) {
        while (amount >= balance && !myBank.isTesting()) {
            try {
                wait(300);
            } catch (InterruptedException ex) {
                /* ignore */
            }
        }
    }
    
    @Override
    public String toString() {
        return String.format("Account[%d] balance %d", id, balance);
    }
}
