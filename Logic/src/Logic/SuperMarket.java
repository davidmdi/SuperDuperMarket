package Logic;

import Logic.SDM_CLASS.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class SuperMarket {
private SuperDuperMarketDescriptor sdmSuper;
private Set<Store> storesSet;
private Set<SDMItem>  itemSet;
private Map<Integer,Store>  storeMap;
private Map<Integer,SDMItem> itemMap;
private List<Order> ordersHistory;

    public List<Order> getOrdersHistory() {
        return ordersHistory;
    }

    public void setOrdersHistory(List<Order> ordersHistory) {
        this.ordersHistory = ordersHistory;
    }

    public Map<Integer, Store> getStoreMap() {
        return storeMap;
    }

    public void setStoreMap(Map<Integer, Store> storeMap) {
        this.storeMap = storeMap;
    }

    public Map<Integer, SDMItem> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<Integer, SDMItem> itemMap) {
        this.itemMap = itemMap;
    }

    @Override
    public String toString() {
        return "SuperMarket{" +
                "sdmSuper=" + sdmSuper +
                ", storesSet=" + storesSet +
                ", itemSet=" + itemSet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuperMarket that = (SuperMarket) o;
        return Objects.equals(sdmSuper, that.sdmSuper) &&
                Objects.equals(storesSet, that.storesSet) &&
                Objects.equals(itemSet, that.itemSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sdmSuper, storesSet, itemSet);
    }

    public Set<Store> getStoresSet() {
        return storesSet;
    }

    public void setStoresSet(Set<Store> storesSet) {
        this.storesSet = storesSet;
    }

    public SuperMarket(SuperDuperMarketDescriptor sdmSuper) {
        this.sdmSuper = sdmSuper;
        this.setItemSet(new HashSet<SDMItem>(sdmSuper.getSDMItems().getSDMItem()));
        this.storesSet = new HashSet<Store>();
        this.storeMap = new HashMap();
        this.ordersHistory = new ArrayList<Order>();
        this.itemMap = new HashMap<>();
        for (SDMStore store:sdmSuper.getSDMStores().getSDMStore()) {
            Store storeToAdd = new Store(store);
            this.storesSet.add(storeToAdd);
            this.storeMap.put(store.getId(),storeToAdd);
        }
        for (SDMItem item: this.getItemSet()) {
            this.itemMap.put(item.getId(),item);
        }
    }

    public SuperDuperMarketDescriptor getSdmSuper() {
        return sdmSuper;
    }

    public void setSdmSuper(SuperDuperMarketDescriptor sdmSuper) {
        this.sdmSuper = sdmSuper;
    }

    public Set<SDMItem> getItemSet() {
        return itemSet;
    }

    public void setItemSet(Set<SDMItem> itemSet) {
        this.itemSet = itemSet;
    }

    public String printStoresDetiales(){
       String returnString = "";
       Set<Store> storeSet = this.getStoresSet();
        for (Store store: storeSet) {
            returnString = returnString + "**" + storeDryInfo(store) + " ;Selling item list:\n" +
                    itemsInfo(store) + "Store orders:"+orderInfo(store)+"\n" +
            "PPK:"+ store.getStore().getDeliveryPpk()+
            "\nPayment on delivery:"+store.calculateAllDeliverysPayment()+"\n";
        }
       return returnString;
    }

    private String orderInfo(Store store) {
        String returnString = "";
        DecimalFormat format= new DecimalFormat("##.##");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM-HH:mm");
        formatter.setLenient(false);
        if(store.getStoresOrders().isEmpty())
            return "No orders yet.";
        else {
            for (Order order:store.getStoresOrders()) {
             returnString= returnString +  "#Order date: " + formatter.format(order.getDate()) +
                        " ;Number of items in order:" + order.getNumberOfItems() +
                        " ;Total items cost: " + order.getItemsTotalCost() +
                        " ; Delivery cost: " + format.format(order.getCostOfDelivery()) +
                        " ;Total items and delivery cost:" + format.format(order.getTotalDeliveryAndItemsCost()) + "\n";
            }
        }
        return returnString;

    }

    private String storeDryInfo(Store store) {
        return  "Store id:" + store.getStore().getId() +
                " ;Store name:" + store.getStore().getName() ;
    }

    private String itemsInfo(Store store) {
        String returnString ="";
        Set<SDMItem> itemSet= this.getItemSet();
        for (SDMItem item: itemSet ) {
            int itemId = item.getId();
            if(store.getPriceMap().keySet().contains(itemId)) // if store sells item :
                returnString = returnString +
                        "#Item Id:" + itemId + " ;Item name:" + item.getName() +
                        " ;Purchase-category:" + item.getPurchaseCategory() +
                        " ;One unit price:" + store.getPriceMap().get(itemId)+
                        " ;Item sold:" + store.getNumberOfSoldItem().get(itemId) + checkQuantity(item) + "\n";
        }
        return returnString;
    }

    private String checkQuantity(SDMItem item) {
        return item.getPurchaseCategory().toLowerCase().equals("weight") ? "KG" : "times";
    }

    public String printItemsDetiales() {
        String returnString = "";
        for (SDMItem item:this.getItemSet()) {
            returnString = returnString + "**" + itemDryInfo(item) + " ;This item sold by:" + howManyStoreSellThisItem(item)
                    +  " stores" + " ;Avrage price:" + avgPriceForItem(item) + " ;This item Sold "
                    + howManytimesItemSold(item) + " " +checkQuantity(item) +"\n";
        }
        return returnString;
    }

    private double howManytimesItemSold(SDMItem item) {
        double answer = 0 ;
        int itemId = item.getId();
        for (Store store : this.getStoresSet()) {
            if(store.getPriceMap().keySet().contains(itemId))
                answer+=store.getNumberOfSoldItem().get(itemId);
        }
        return answer;
    }

    private int howManyStoreSellThisItem(SDMItem item) {
        int answer = 0 ;
        int itemId = item.getId();
        for (Store store : this.getStoresSet()) {
            if(store.getPriceMap().keySet().contains(itemId))
                answer++;
        }
        return answer;
    }

    private String avgPriceForItem(SDMItem item) {
        double answer = 0;
        int itemId = item.getId();
        int numOfStoreWhoSellsitem = howManyStoreSellThisItem(item);
        for (Store store : this.getStoresSet()) {
            if (store.getPriceMap().keySet().contains(itemId))
                answer += store.getPriceMap().get(itemId);
        }
        return convertTOstringFormat(answer/numOfStoreWhoSellsitem);
    }

    public String convertTOstringFormat(double v) {
        DecimalFormat format= new DecimalFormat("##.##");
        return format.format(v);
    }

    private String itemDryInfo(SDMItem item) {
      return   "Item Id:" + item.getId() + " ;Item name:" + item.getName() +
                " ;Purchase-category:" + item.getPurchaseCategory() ;
    }

    public String displayHistory() {
        String returnString = "";
        if(this.getOrdersHistory().isEmpty())
            returnString = "<No orders made yet.>";
        else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM-HH:mm");
            formatter.setLenient(false);
            for (Order order : this.getOrdersHistory()) {
                returnString = returnString +
                        "**Order id:" + order.getOrderId() +
                        " ;Date:" + formatter.format(order.getDate())
                        + " ;Store id:" + order.getStoreId()
                        + " ;Store name:" +
                        this.getStoreMap().get(order.getStoreId()).getStore().getName() +
                        " ;Total items type:" + order.getNumberOfItems() +
                        " ;Total item cost:" + order.getItemsTotalCost() +
                        "\n;Delivery cost:" + convertTOstringFormat(order.getCostOfDelivery()) +
                        " ;Total cost(items+delivery):"
                        + convertTOstringFormat(order.getTotalDeliveryAndItemsCost())  + "\n";
            }
        }
        return returnString;
    }
}
