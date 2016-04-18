package com.bis.es.service;

import ch.qos.logback.classic.Logger;
import com.bis.es.domain.Item;
import com.bis.es.domain.Order;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class MarketplaceServiceImpl implements MarketplaceService {

    final static Logger logger = (Logger) LoggerFactory.getLogger(MarketplaceServiceImpl.class);

    /**
     * Buyers/Sellers order queue
     */
    private final Queue<Order> orders = new ConcurrentLinkedQueue<>();

    /**
     * Bid List
     */
    private final Queue<Item> bids = new ConcurrentLinkedQueue<>();

    /**
     * Offer List
     */
    private final Queue<Item> offers = new ConcurrentLinkedQueue<>();

    /**
     * Add bid to the bid queue
     * @param bidItem
     */
    @Override
    public void addBid(Item bidItem) {
        bids.add(bidItem);
        processOffer(bidItem);
    }

    /**
     * Add offer to the offer queue
     * @param offerItem
     */
    @Override
    public void addOffer(Item offerItem) {
        offers.add(offerItem);
        processBid(offerItem);
    }

    /**
     * List the bids for a given buyer user ID
     * @param buyerUserId
     * @return bids
     */
    @Override
    public List<Item> getBids(int buyerUserId) {
        return bids.stream().filter(b -> b.getUserId().equals(buyerUserId)).collect(Collectors.toList());
    }

    /**
     *  List the offers for a given seller user ID
     * @param sellerUserId
     * @return offers
     */
    @Override
    public List<Item> getOffers(int sellerUserId) {
        return offers.stream().filter(o -> o.getUserId().equals(sellerUserId)).collect(Collectors.toList());
    }

    /**
     * sellerUserId
     * @param sellerUserId
     * @return seller orders
     */
    @Override
    public List<Order> getSellerOrders(int sellerUserId) {
        return orders.stream().filter(o -> o.getSellerUserId()==sellerUserId).collect(Collectors.toList());
    }

    /**
     * List the orders for a given buyer user ID
     * @param buyerUserId
     * @return buyer orders
     */
    @Override
    public List<Order> getBuyerOrders(int buyerUserId) {
        return orders.stream().filter(o -> o.getBuyerUserId()==buyerUserId).collect(Collectors.toList());
    }

    /**
     * Retrieve the current bid price for a given item ID (i.e. the highest price of all bids for that
     * item
     * @param itemId
     * @return current bid price for the item
     */
    @Override
    public BigDecimal retrieveCurrentBidPrice(int itemId) {
        BigDecimal bidPrice = null;
        Optional<Item> item = bids.stream().filter(b -> b.getItemId()==itemId).max((b1, b2) -> b1.getPrice().compareTo(b2.getPrice()));
        if (item.isPresent()) {
            bidPrice = item.get().getPrice();
            logger.info("Bid Price for itemId {} is {}", itemId, bidPrice);
        }
        logger.info("Bit Price for itemId {} not present", itemId);
        return bidPrice;
    }

    /**
     * Retrieve the current offer price for a given item ID (i.e. the lowest price of all offers for that
     * item)
     * @param itemId
     * @return current offer price
     */
    @Override
    public BigDecimal retrieveCurrentOfferPrice(int itemId) {
        BigDecimal offerPrice = null;
        final Optional<Item> item = offers.stream().filter(o -> o.getItemId()==itemId).min((o1, o2) -> o1.getPrice().compareTo(o2.getPrice()));
        if (item.isPresent()) {
            offerPrice = item.get().getPrice();
            logger.info("Offer Price for itemId {} is {}", itemId, offerPrice);
        }
        logger.info("Offer Price for itemId {} not present", itemId);
        return offerPrice;
    }

    /**
     * process order for offer matched
     * @param bid
     * @param foundOffer
     */
    private void processOrder(Item bid, Item foundOffer) {
        //1. Create order as best match is found
        orders.add(new Order(bid.getItemId(), foundOffer.getPrice(), bid.getQuantity(), bid.getUserId(), foundOffer.getUserId()));
        //Check offer quantity, if reduce to zero then remove offer
        if (foundOffer.getQuantity()>=bid.getQuantity())  {
            //2. Adjust offer quantity
            foundOffer.adjustQuantity(bid.getQuantity());
            if (foundOffer.getQuantity() == 0) {
                //3. remove offer
                offers.remove(foundOffer);
            }
        }
        //4. remove bid
        bids.remove(bid);
    }

    /**
     * Processes incoming offer
     * @param bid
     */
    private void processOffer(final Item bid) {
        final Item foundOffer = findBestOffer(bid);
        if (foundOffer != null) {
            logger.info("found best bid {} for offer {}", bid, foundOffer);
            processOrder(bid, foundOffer);
        }
    }

    /**
     * Processes incoming bid
     * @param offer
     */
    private void processBid(final Item offer) {
        final Item foundBid = findBestBid(offer);
        if (foundBid != null) {
            logger.info("found best offer {} for bid {}", offer, foundBid);
            processOrder(foundBid, offer);
        }
    }

    /**
     * Find nest match for a bid
     * Rules
     * The bid item ID and sell item ID are the same
     * The bid price is greater than or equal to the offer price
     * The offer quantity is greater than or equal to the bid quantity
     * @param bid
     * @return
     */
    private Item findBestOffer(final Item bid) {
        Optional<Item> offer = offers.stream().filter((o -> o.getItemId()==(bid.getItemId()) && (bid.getPrice().compareTo(o.getPrice()) >= 1) && (o.getQuantity() >= bid.getQuantity()))).findFirst();
        if (offer.isPresent()) {
            return offer.get();

        }
        return null;
    }

    /**
     * Find nest match for an offer
     * Rules
     * The bid item ID and sell item ID are the same
     * The bid price is greater than or equal to the offer price
     * The offer quantity is greater than or equal to the bid quantity
     * @param offer
     * @return
     */
    private Item findBestBid(final Item offer) {
        Optional<Item> bid = bids.stream().filter((b -> b.getItemId()==(offer.getItemId()) && (b.getPrice().compareTo(offer.getPrice()) >= 1) && (offer.getQuantity() >= b.getQuantity()))).findFirst();
        if (bid.isPresent()) {
            return bid.get();
        }
        return null;
    }
}
