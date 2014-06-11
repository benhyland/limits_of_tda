package net.digihippo.bread;

public interface Order {

    void setPrevious(Order order);
    void setNext(Order order);
    void fill(int remainingQuantity);

    void remove(int orderId);
}
