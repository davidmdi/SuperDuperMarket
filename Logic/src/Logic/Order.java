package Logic;

import java.util.Date;
import java.util.Objects;

public class Order {
    //private DataFormat dataFormat;
    private Date date;
    private static int idGeneretor = 1;
    private int orderId;
    private int StoreId;
    private double numberOfItems;
    private double itemsTotalCost;
    private double costOfDelivery;
    private double totalDeliveryAndItemsCost;

    public Order(Date date,int storeId, int numberOfItems, double itemsTotalCost,
                 double costOfDelivery, double totalDeliveryAndItemsCost) {
        this.date = date;
        this.orderId = this.idGeneretor++ ;
        StoreId = storeId;
        this.numberOfItems = numberOfItems;
        this.itemsTotalCost = itemsTotalCost;
        this.costOfDelivery = costOfDelivery;
        this.totalDeliveryAndItemsCost = totalDeliveryAndItemsCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId == order.orderId &&
                StoreId == order.StoreId &&
                numberOfItems == order.numberOfItems &&
                Double.compare(order.itemsTotalCost, itemsTotalCost) == 0 &&
                Double.compare(order.costOfDelivery, costOfDelivery) == 0 &&
                Double.compare(order.totalDeliveryAndItemsCost, totalDeliveryAndItemsCost) == 0 &&
                Objects.equals(date, order.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, orderId, StoreId, numberOfItems, itemsTotalCost, costOfDelivery, totalDeliveryAndItemsCost);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static int getIdGeneretor() {
        return idGeneretor;
    }

    public static void setIdGeneretor(int idGeneretor) {
        Order.idGeneretor = idGeneretor;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStoreId() {
        return StoreId;
    }

    public void setStoreId(int storeId) {
        StoreId = storeId;
    }

    public double getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public double getItemsTotalCost() {
        return itemsTotalCost;
    }

    public void setItemsTotalCost(double itemsTotalCost) {
        this.itemsTotalCost = itemsTotalCost;
    }

    public double getCostOfDelivery() {//foramt to change
        return costOfDelivery;
    }

    public void setCostOfDelivery(double costOfDelivery) {
        this.costOfDelivery = costOfDelivery;
    }

    public double getTotalDeliveryAndItemsCost() {
        return totalDeliveryAndItemsCost;
    }

    public void setTotalDeliveryAndItemsCost(double totalDeliveryAndItemsCost) {
        this.totalDeliveryAndItemsCost = totalDeliveryAndItemsCost;
    }
}


//need to think about the ctor.

