package Logic;

import Logic.SDM_CLASS.*;

import java.text.DecimalFormat;
import java.util.*;

public class Store {
    private SDMStore store;
    private Set<SDMSell> pricesSet;
    private Map<Integer,Double> numberOfSoldItem;
    private Map<Integer,Integer> priceMap;
    private List<Order> storesOrders;

    public List<Order> getStoresOrders() {
        return storesOrders;
    }

    public void setStoresOrders(List<Order> storesOrders) {
        this.storesOrders = storesOrders;
    }

    public Map<Integer, Double> getNumberOfSoldItem() {
        return numberOfSoldItem;
    }

    public void setNumberOfSoldItem(Map<Integer, Double> numberOfSoldItem) {
        this.numberOfSoldItem = numberOfSoldItem;
    }

    public Map<Integer, Integer> getPriceMap() {
        return priceMap;
    }

    public void setPriceMap(Map<Integer, Integer> priceMap) {
        this.priceMap = priceMap;
    }

    public Store(SDMStore store) {
        this.store = store;
        this.setPricesSet(new HashSet<SDMSell>(store.getSDMPrices().getSDMSell()));
        this.numberOfSoldItem = new HashMap<>();
        this.storesOrders = new ArrayList<Order>();
        this.priceMap = new HashMap<>();
        for (SDMSell price: this.getPricesSet()) { // init priceMap
            int sellingItemId = price.getItemId();
            this.numberOfSoldItem.put(sellingItemId,0.0);
            this.priceMap.put(sellingItemId,price.getPrice());
        }
    }

    @Override
    public String toString() {
        return "Store{" +
                "store=" + store +
                ", pricesSet=" + pricesSet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store1 = (Store) o;
        return Objects.equals(store, store1.store) &&
                Objects.equals(pricesSet, store1.pricesSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(store, pricesSet);
    }

    public SDMStore getStore() {
        return store;
    }

    public void setStore(SDMStore store) {
        this.store = store;
    }

    public Set<SDMSell> getPricesSet() {
        return pricesSet;
    }

    public void setPricesSet(Set<SDMSell> pricesSet) {
        this.pricesSet = pricesSet;
    }

    public void updateSoldItemsAtStore(Map<Integer, Float> itemIdMap) {
        for (int itemId:this.getNumberOfSoldItem().keySet()) {
            if(itemIdMap.keySet().contains(itemId)) {
                double newQuantity = this.getNumberOfSoldItem().get(itemId) + itemIdMap.get(itemId);
                this.numberOfSoldItem.replace(itemId, newQuantity);
            }
        }
    }

    public String calculateAllDeliverysPayment() {
        double deliveryCalc = 0 ;
        if(this.storesOrders.isEmpty())
            return "There was not orders yet.";
        else{
            for (Order order: this.storesOrders) {
                deliveryCalc = deliveryCalc + order.getCostOfDelivery();
            }
        }
        return  new DecimalFormat("##.##").format(deliveryCalc);
    }
}
