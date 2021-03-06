package net.digihippo.bread;

import java.util.HashMap;
import java.util.Map;

public class BalancesAndOrders implements BalanceAndOrderOperations {

    private static int PRICE_OF_BREAD = 12;

    private final OutboundEvents events;

    private final Map<Integer, Integer> balances;
    private final Map<Integer, Map<Integer,Integer>> orders;
    private final Order firstOrder;
    private Order lastOrder;

    public BalancesAndOrders(OutboundEvents events)
    {
        this.events = events;
        this.balances = new HashMap<Integer, Integer>();
        this.orders = new HashMap<Integer, Map<Integer, Integer>>();
        firstOrder = new OrderCell(events, Integer.MIN_VALUE, Integer.MIN_VALUE, 0, null);
        firstOrder.setPrevious(firstOrder);
        lastOrder = firstOrder;
    }

    @Override
    public void deposit(int accountId, int creditAmount) {
        final int oldBalance = currentBalance(accountId);
        final int newBalance = oldBalance + creditAmount;
        balances.put(accountId, newBalance);
        events.newAccountBalance(accountId, newBalance);
    }

    @Override
    public void placeOrder(int accountId, int orderId, int quantity) {
        final int cost = cost(quantity);
        if(balanceIsSufficient(accountId, cost)) {
            storeOrder(accountId, orderId, quantity);
            deposit(accountId, -cost);
        }
        else {
            events.orderRejected(accountId);
        }

    }

    @Override
    public void cancelOrder(int accountId, int orderId) {
        final Map<Integer, Integer> accountOrders = currentOrders(accountId);

        if(accountOrders.containsKey(orderId)) {
            final int quantity = accountOrders.remove(orderId);
            orders.put(accountId, accountOrders);
            firstOrder.remove(orderId);
            events.orderCancelled(accountId, orderId);
            deposit(accountId, cost(quantity));
        }
        else {
            events.orderNotFound(accountId, orderId);
        }
    }

    @Override
    public void placeWholesaleOrder() {
        events.onWholesaleOrder(totalOrderQuantity());
    }

    @Override
    public void onWholesaleOrder(int quantity) {
        firstOrder.fill(quantity);
    }

    private int cost(int quantity) {
        return quantity * PRICE_OF_BREAD;
    }

    private boolean balanceIsSufficient(int accountId, int cost) {
        return currentBalance(accountId) >= cost;
    }

    private int currentBalance(int accountId) {
        return balances.containsKey(accountId) ? balances.get(accountId) : 0;
    }

    private Map<Integer, Integer> currentOrders(int accountId) {
        return orders.containsKey(accountId) ? orders.get(accountId) : new HashMap<Integer, Integer>();
    }

    private void storeOrder(int accountId, int orderId, int quantity) {
        final Map<Integer, Integer> accountOrders = currentOrders(accountId);
        if(accountOrders.containsKey(orderId)) {
            events.orderRejected(orderId);
        }
        else {
            accountOrders.put(orderId, quantity);
            orders.put(accountId, accountOrders);
            final OrderCell newOrder = new OrderCell(events, accountId, orderId, quantity, lastOrder);
            lastOrder.setNext(newOrder);
            lastOrder = newOrder;
            events.orderPlaced(accountId, quantity);
        }
    }

    private int totalOrderQuantity() {
        int quantity = 0;
        for(final Map<Integer, Integer> accountOrders : orders.values()) {
            for(final int q : accountOrders.values()) {
                quantity += q;
            }
        }
        return quantity;
    }
}
