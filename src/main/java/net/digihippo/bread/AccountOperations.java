package net.digihippo.bread;

public interface AccountOperations
{
    void createAccount(int accountId);
    void deposit(int accountId, int creditAmount);
    void placeOrder(int accountId, int orderId, int amount);
    void cancelOrder(int accountId, int orderId);
    void placeWholesaleOrder();
    void onWholesaleOrder(int quantity);
}
