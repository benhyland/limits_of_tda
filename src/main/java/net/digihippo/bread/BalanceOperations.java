package net.digihippo.bread;

public interface BalanceOperations {

    void deposit(int accountId, int creditAmount);
    void placeOrder(int accountId, int orderId, int quantity);
}
