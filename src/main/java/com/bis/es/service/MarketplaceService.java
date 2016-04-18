package com.bis.es.service;

import com.bis.es.domain.Item;
import com.bis.es.domain.Order;

import java.math.BigDecimal;
import java.util.List;

public interface MarketplaceService {

    /**
     * Add a bid
     * @param bidItem
     */
    void addBid(Item bidItem);

    /**
     * Add an offer
     * @param offerItem
     */
    void addOffer(Item offerItem);

    /**
     * List the bids for a given buyer user ID
     * @param buyerUserId
     * @return bids
     */
    List<Item> getBids(int buyerUserId);

    /**
     *  List the offers for a given seller user ID
     * @param sellerUserId
     * @return offers
     */
    List<Item> getOffers(int sellerUserId);

    /**
     * sellerUserId
     * @param sellerUserId
     * @return seller orders
     */
    List<Order> getSellerOrders(int sellerUserId);

    /**
     * List the orders for a given buyer user ID
     * @param buyerUserId
     * @return buyer orders
     */
    List<Order> getBuyerOrders(int buyerUserId);

    /**
     * Retrieve the current bid price for a given item ID (i.e. the highest price of all bids for that
     * item
     * @param itemId
     * @return current bid price for the item
     */
    BigDecimal retrieveCurrentBidPrice(int itemId);

    /**
     * Retrieve the current offer price for a given item ID (i.e. the lowest price of all offers for that
     * item)
     * @param itemId
     * @return current offer price
     */
    BigDecimal retrieveCurrentOfferPrice(int itemId);


}
