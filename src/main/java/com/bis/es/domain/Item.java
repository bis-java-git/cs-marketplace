package com.bis.es.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Item {

    private final int itemId;

    private final BigDecimal price;

    private int quantity;

    private final Integer userId;

    private String uniqueID = UUID.randomUUID().toString();

    public Item(Integer itemId, BigDecimal price, int quantity, int userId) {
        this.itemId = itemId;
        this.price = price;
        this.quantity = quantity;
        this.userId = userId;
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

    public Integer getUserId() {
        return userId;
    }

    public void adjustQuantity(int quantity) {
        this.quantity = this.quantity - quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return getItemId() == item.getItemId() &&
                getQuantity() == item.getQuantity() &&
                Objects.equals(getPrice(), item.getPrice()) &&
                Objects.equals(getUserId(), item.getUserId()) &&
                Objects.equals(uniqueID, item.uniqueID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItemId(), getPrice(), getQuantity(), getUserId(), uniqueID);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Item{");
        sb.append("itemId=").append(itemId);
        sb.append(", price=").append(price);
        sb.append(", quantity=").append(quantity);
        sb.append(", userId=").append(userId);
        sb.append(", uniqueID='").append(uniqueID).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
