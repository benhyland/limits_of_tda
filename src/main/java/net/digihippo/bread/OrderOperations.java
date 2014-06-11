package net.digihippo.bread;

public interface OrderOperations {

    void placeOrder(int accountId, int orderId, int quantity);
    void cancelOrder(int accountId, int orderId);

    void placeWholesaleOrder();
}
