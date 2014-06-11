package net.digihippo.bread;

import java.util.HashMap;
import java.util.Map;

public class BreadShop implements AccountOperations {
    public static int PRICE_OF_BREAD = 12;

    private final OutboundEvents events;

    private final AccountOperations accounts;

    public BreadShop(OutboundEvents events) {
        this.events = events;
        this.accounts = new Accounts(new BalancesAndOrders(events), events);
    }

    @Override
    public void createAccount(int id) {
        accounts.createAccount(id);
    }

    @Override
    public void deposit(int accountId, int creditAmount) {
        accounts.deposit(accountId, creditAmount);
    }

    @Override
    public void placeOrder(int accountId, int orderId, int amount) {
        accounts.placeOrder(accountId, orderId, amount);
    }

    @Override
    public void cancelOrder(int accountId, int orderId) {
        accounts.cancelOrder(accountId, orderId);
    }

    public void placeWholesaleOrder() {
        throw new UnsupportedOperationException("Implement me in Objective A");
    }

    public void onWholesaleOrder(int quantity) {
        throw new UnsupportedOperationException("Implement me in Objective B");
    }
}
