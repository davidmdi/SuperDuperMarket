package control;

import Logic.Logic;

import java.awt.*;
import java.util.Date;
import java.util.Map;

public class Controller {

    // instance of logic object
    private Logic logic ;

    public Controller() {
        this.logic = new Logic();
    }

    public String checkIsXml(String xmlPathString) {
        String returnString = logic.loadXML(xmlPathString);
        return returnString;
    }

    public String DisplayStoreDetails() {
        return logic.DisplayStoreDetails() ;
    }

    public String DisplayAllProducts() {
        //need to change to array of string and then build a big string to return .
        String returnString = logic.DisplayAllProducts();
        return returnString;
    }

    public String presentOrderHistory() {
        String returnString = logic.presentOrderHistory() ;
        return returnString;
    }

    public boolean checkStoreIdInput(int option) {
       return logic.isStoreExist(option);
    }

    public String askStoreDetailesForOrder() {
        return logic.storeDetailesForOrder();
    }

    public boolean isLocationHitsAstore(Point cord) {
        return this.logic.askIsLocationHitsAstore(cord);
    }

    public String showStoreItemsForUser(int chosenStoreIdFromUser) {
        return logic.askItemsDetailesForUserOrder(chosenStoreIdFromUser);
    }

    public boolean isItemBeSoldByStore(int choosedItemId, int chosenStoreIdFromUser) {
        return  logic.doesStoreSellsThisItem(choosedItemId,chosenStoreIdFromUser);
    }

    public String makeDisplay(int chosenStoreIdFromUser, Point cord, Map<Integer, Float> itemIdMap) {
        return logic.askOrderDisplay(chosenStoreIdFromUser , cord,itemIdMap);
    }

    public String sendOrderToStore(int chosenStoreIdFromUser, Date orderDate, Point cord, Map<Integer, Float> itemIdMap) {
        return logic.makeAnOrder(chosenStoreIdFromUser ,orderDate , cord , itemIdMap  );
    }

    public int getPurcheCatagoryAsInt(int choosedItemId) {
        return logic.askPurcheCategoryAsInt(choosedItemId);
    }

    public boolean isXmlLoaded() {
        return logic.askIsXmlLoaded();
    }
}
