package net.digihippo.bread;

import java.util.HashMap;
import java.util.Map;

public class BreadShop {
    public static int PRICE_OF_BREAD = 12;

    private final OutboundEvents events;
    final Map<Integer, Account> bank = new HashMap<Integer, Account>();


    public BreadShop(OutboundEvents events) {
        this.events = events;
    }

    public void createAccount(int id) {
        bank.put(id, new Account());
        events.accountCreatedSuccessfully(id);
    }

    public void deposit(int accountId, int creditAmount) {
        Account account = bank.get(accountId);
        if (account != null) {
            final int newBalance = account.deposit(creditAmount);
            events.newAccountBalance(accountId, newBalance);
        } else {
            events.accountNotFound(accountId);
        }
    }

    public void placeOrder(int accountId, int orderId, int amount) {
        Account account = bank.get(accountId);
        if (account != null) {
            int cost = amount * PRICE_OF_BREAD;
            if (account.getBalance() >= cost) {
                account.addOrder(orderId, amount);
                int newBalance = account.deposit(-cost);
                events.orderPlaced(accountId, amount);
                events.newAccountBalance(accountId, newBalance);
            } else {
                events.orderRejected(accountId);
            }
        } else {
            events.accountNotFound(accountId);
        }
    }

    public void cancelOrder(int accountId, int orderId) {
        Account account = bank.get(accountId);
        if (account == null)
        {
            events.accountNotFound(accountId);
            return;
        }

        Integer cancelledQuantity = account.cancelOrder(orderId);
        if (cancelledQuantity == null)
        {
            events.orderNotFound(accountId, orderId);
            return;
        }

        int newBalance = account.deposit(cancelledQuantity * PRICE_OF_BREAD);
        events.orderCancelled(accountId, orderId);
        events.newAccountBalance(accountId, newBalance);
    }

    public void placeWholesaleOrder() {
        throw new UnsupportedOperationException("Implement me in Objective A");
    }

    public void onWholesaleOrder(int quantity) {
        throw new UnsupportedOperationException("Implement me in Objective B");
    }
}
