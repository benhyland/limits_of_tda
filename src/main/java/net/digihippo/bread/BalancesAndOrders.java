package net.digihippo.bread;

import java.util.HashMap;
import java.util.Map;

public class BalancesAndOrders implements BalanceAndOrderOperations {

    private static int PRICE_OF_BREAD = 12;

    private final OutboundEvents events;

    private final Map<Integer, Integer> balances;
    private final Map<Integer, Map<Integer,Integer>> orders;

    public BalancesAndOrders(OutboundEvents events)
    {
        this.events = events;
        this.balances = new HashMap<Integer, Integer>();
        this.orders = new HashMap<Integer, Map<Integer, Integer>>();
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
            events.orderCancelled(accountId, orderId);
            deposit(accountId, cost(quantity));
        }
        else {
            events.orderNotFound(accountId, orderId);
        }
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
            events.orderPlaced(accountId, quantity);
        }
    }
}
