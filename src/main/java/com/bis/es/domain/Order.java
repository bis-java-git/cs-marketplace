package com.bis.es.domain;

import java.math.BigDecimal;

/**
 * Processed order
 */
public final class Order {

    private final int itemId;

    private final BigDecimal price;

    private final int quantity;

    private final int buyerUserId;

    private final int sellerUserId;

    public Order(int itemId,
                 BigDecimal price,
                 int quantity,
                 int buyerUserId,
                 int sellerUserId) {
        this.itemId = itemId;
        this.price = price;
        this.quantity = quantity;
        this.buyerUserId = buyerUserId;
        this.sellerUserId = sellerUserId;
    }

    public int getItemId() {
        return itemId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getBuyerUserId() {
        return buyerUserId;
    }

    public int getSellerUserId() {
        return sellerUserId;
    }

}

