package net.digihippo.bread;

import java.util.HashSet;
import java.util.Set;

public class Accounts implements AccountOperations {

    private final BalanceAndOrderOperations balancesAndOrders;
    private final OutboundEvents events;

    private final Set<Integer> accountIds;

    public Accounts(BalanceAndOrderOperations balancesAndOrders, OutboundEvents events) {
        this.balancesAndOrders = balancesAndOrders;
        this.events = events;
        this.accountIds = new HashSet<Integer>();
    }

    @Override
    public void createAccount(int accountId) {
        if(accountExists(accountId)) {
            events.accountAlreadyExists(accountId);
        }
        else {
            accountIds.add(accountId);
            events.accountCreatedSuccessfully(accountId);
        }
    }

    @Override
    public void deposit(int accountId, int creditAmount) {
        if(accountExists(accountId)) {
            balancesAndOrders.deposit(accountId, creditAmount);
        }
        else {
            events.accountNotFound(accountId);
        }
    }

    @Override
    public void placeOrder(int accountId, int orderId, int amount) {
        if(accountExists(accountId)) {
            balancesAndOrders.placeOrder(accountId, orderId, amount);
        }
        else {
            events.accountNotFound(accountId);
        }
    }

    @Override
    public void cancelOrder(int accountId, int orderId) {
        if(accountExists(accountId)) {
            balancesAndOrders.cancelOrder(accountId, orderId);
        }
        else {
            events.accountNotFound(accountId);
        }
    }

    @Override
    public void placeWholesaleOrder() {
        balancesAndOrders.placeWholesaleOrder();
    }

    @Override
    public void onWholesaleOrder(int quantity) {
        balancesAndOrders.onWholesaleOrder(quantity);
    }

    private boolean accountExists(int accountId) {
        return accountIds.contains(accountId);
    }
}
