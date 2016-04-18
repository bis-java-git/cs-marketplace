package service;

import com.bis.es.domain.Item;
import com.bis.es.service.MarketplaceService;
import com.bis.es.service.MarketplaceServiceImpl;
import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MarketplaceServiceTest {

    private static final int ITEM_ID_12345 = 12345;

    private static final Integer USER_ID_1 = 1;

    private static final Integer USER_ID_2 = 2;

    private static final Integer USER_ID_3 = 3;

    private  static final Integer USER_ID_4 = 4;

    private static final Integer UNKNOWN_ITEM_ID = 9999;

    private MarketplaceService marketplaceService = new MarketplaceServiceImpl();

    @Test
    public void getBidsTest() {
        marketplaceService.addBid(new Item(ITEM_ID_12345, new BigDecimal(25), 10, USER_ID_1));
        marketplaceService.addBid(new Item(ITEM_ID_12345, new BigDecimal(25), 5, USER_ID_1));
        assertEquals(2, marketplaceService.getBids(USER_ID_1).size());
    }

    @Test
    public void getOffersTest() {
        marketplaceService.addOffer(new Item(ITEM_ID_12345, new BigDecimal(25), 5, USER_ID_2));
        marketplaceService.addOffer(new Item(ITEM_ID_12345, new BigDecimal(24), 10, USER_ID_3));
        assertEquals(1, marketplaceService.getOffers(USER_ID_2).size());
        assertEquals(1, marketplaceService.getOffers(USER_ID_3).size());
    }

    @Test
    public void retrieveCurrentBidPrice() {
        marketplaceService.addBid(new Item(ITEM_ID_12345, new BigDecimal(25), 10, USER_ID_1));
        marketplaceService.addBid(new Item(ITEM_ID_12345, new BigDecimal(10), 5, USER_ID_1));
        assertEquals(new BigDecimal(25), marketplaceService.retrieveCurrentBidPrice(ITEM_ID_12345));
        assertNull(marketplaceService.retrieveCurrentBidPrice(UNKNOWN_ITEM_ID));
    }

    @Test
    public void retrieveCurrentOfferPrice() {
        marketplaceService.addOffer(new Item(ITEM_ID_12345, new BigDecimal(25), 5, USER_ID_2));
        marketplaceService.addOffer(new Item(ITEM_ID_12345, new BigDecimal(24), 10, USER_ID_3));
        assertEquals(new BigDecimal(24), marketplaceService.retrieveCurrentOfferPrice(ITEM_ID_12345));
        assertNull(marketplaceService.retrieveCurrentOfferPrice(UNKNOWN_ITEM_ID));
    }

    private void buildOfferItems() {
        marketplaceService.addOffer(new Item(ITEM_ID_12345, new BigDecimal(25), 5, USER_ID_2));
        marketplaceService.addOffer(new Item(ITEM_ID_12345, new BigDecimal(24), 10, USER_ID_3));
    }

    private void buildBidItems() {
        marketplaceService.addBid(new Item(ITEM_ID_12345, new BigDecimal(25), 10, USER_ID_1));
    }

    private void buildItems() {
        buildBidItems();
        buildOfferItems();
    }

    private void checkBidsAndOfferTest() {
        assertEquals(0, marketplaceService.getBids(USER_ID_1).size());
        assertEquals(1, marketplaceService.getOffers(USER_ID_2).size());
        assertEquals(0, marketplaceService.getOffers(USER_ID_3).size());
    }

    private void checkOrdersTest() {
        assertEquals(1, marketplaceService.getBuyerOrders(USER_ID_1).size());
        assertEquals(10, marketplaceService.getBuyerOrders(USER_ID_1).get(0).getQuantity());
        assertEquals(ITEM_ID_12345, marketplaceService.getBuyerOrders(USER_ID_1).get(0).getItemId());
        assertEquals(new BigDecimal(24), marketplaceService.getBuyerOrders(USER_ID_1).get(0).getPrice());

        assertEquals(0, marketplaceService.getBuyerOrders(USER_ID_2).size());

        assertEquals(1, marketplaceService.getSellerOrders(USER_ID_3).size());
        assertEquals(10, marketplaceService.getSellerOrders(USER_ID_3).get(0).getQuantity());
        assertEquals(new BigDecimal(24), marketplaceService.getSellerOrders(USER_ID_3).get(0).getPrice());
    }

    @Test
    public void getBuyerOrdersTest() {
        buildItems();
        checkOrdersTest();
    }

    @Test
    public void getSellersOrdersTest() {
        buildItems();
        checkOrdersTest();
    }

    @Test
    public void addOfferTest() {
        buildItems();
        checkBidsAndOfferTest();
        checkOrdersTest();

        marketplaceService.addBid(new Item(ITEM_ID_12345, new BigDecimal(23), 5, USER_ID_4));
        checkBidsAndOfferTest();
        checkOrdersTest();
        assertEquals(0, marketplaceService.getOffers(USER_ID_4).size());
    }

    @Test
    public void addBidTest() {
        buildOfferItems();
        buildBidItems();
        checkBidsAndOfferTest();
        checkOrdersTest();
    }

    @Test
    public void addOfferWithQuantityAdjustmentTest() {
        marketplaceService.addBid(new Item(ITEM_ID_12345, new BigDecimal(25), 10, USER_ID_1));
        marketplaceService.addOffer(new Item(ITEM_ID_12345, new BigDecimal(25), 5, USER_ID_2));
        marketplaceService.addOffer(new Item(ITEM_ID_12345, new BigDecimal(24), 20, USER_ID_3));

        assertEquals(0, marketplaceService.getBids(USER_ID_1).size());
        assertEquals(1, marketplaceService.getOffers(USER_ID_2).size());
        assertEquals(1, marketplaceService.getOffers(USER_ID_3).size());

        assertEquals(1, marketplaceService.getBuyerOrders(USER_ID_1).size());
        assertEquals(1, marketplaceService.getSellerOrders(USER_ID_3).size());

        assertNotNull(marketplaceService.getBuyerOrders(USER_ID_1).get(0));
        assertEquals(10, marketplaceService.getBuyerOrders(USER_ID_1).get(0).getQuantity());

        assertNotNull(marketplaceService.getSellerOrders(USER_ID_3).get(0));
        assertEquals(10, marketplaceService.getSellerOrders(USER_ID_3).get(0).getQuantity());

    }

    @Test
    public void addBidWithQuantityAdjustmentTest() {
        marketplaceService.addOffer(new Item(ITEM_ID_12345, new BigDecimal(25), 5, USER_ID_2));
        marketplaceService.addOffer(new Item(ITEM_ID_12345, new BigDecimal(24), 20, USER_ID_3));
        marketplaceService.addBid(new Item(ITEM_ID_12345, new BigDecimal(25), 10, USER_ID_1));

        assertEquals(0, marketplaceService.getBids(USER_ID_1).size());
        assertEquals(1, marketplaceService.getOffers(USER_ID_2).size());
        assertEquals(1, marketplaceService.getOffers(USER_ID_3).size());

        assertEquals(1, marketplaceService.getBuyerOrders(USER_ID_1).size());
        assertEquals(1, marketplaceService.getSellerOrders(USER_ID_3).size());

        assertNotNull(marketplaceService.getBuyerOrders(USER_ID_1).get(0));
        assertEquals(10, marketplaceService.getBuyerOrders(USER_ID_1).get(0).getQuantity());

        assertNotNull(marketplaceService.getSellerOrders(USER_ID_3).get(0));
        assertEquals(10, marketplaceService.getSellerOrders(USER_ID_3).get(0).getQuantity());

        assertEquals(1, marketplaceService.getBuyerOrders(USER_ID_1).size());
        assertEquals(1, marketplaceService.getSellerOrders(USER_ID_3).size());
    }

}
