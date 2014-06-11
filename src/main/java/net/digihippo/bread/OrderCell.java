package net.digihippo.bread;

public class OrderCell implements Order {

    private final OutboundEvents events;
    private final int accountId;
    private final int orderId;
    private final int quantity;
    private Order previous;
    private Order next;
    private int filled;

    public OrderCell(final OutboundEvents events, final int accountId, final int orderId, final int quantity, final Order previous) {
        this.events = events;
        this.accountId = accountId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.previous = previous;
        this.next = null;
    }

    @Override
    public void setPrevious(final Order order) {
        previous = order;
    }

    @Override
    public void setNext(final Order order) {
        next = order;
    }

    @Override
    public void fill(final int remainingQuantity) {
        if(remainingQuantity > 0) {
            final int fillTarget = quantity - filled;
            final int toFill = fillTarget > remainingQuantity ? remainingQuantity : fillTarget;
            if(fillTarget > 0) {
                filled += toFill;
                events.orderFilled(accountId, orderId, toFill);
            }
            if(next != null) {
                next.fill(remainingQuantity - toFill);
            }
        }
    }

    @Override
    public void remove(final int orderId) {
        if(orderId == this.orderId) {
            previous.setNext(next);
            if(next != null) {
                next.setPrevious(previous);
            }
        }
        else {
            if(next != null) {
                next.remove(orderId);
            }
        }
    }
}
