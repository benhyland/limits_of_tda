package net.digihippo.bread;

public interface OrderOperations {

    void placeOrder(int accountId, int orderId, int amount);
    void cancelOrder(int accountId, int orderId);
}
