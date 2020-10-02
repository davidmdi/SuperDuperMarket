package Logic;

import Logic.SDM_CLASS.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

import java.text.SimpleDateFormat;
import java.util.*;

public class Logic {
     // need to have instance of superMarket!!!!!
   private SuperMarket mySupermarket;
   private boolean isSuperMarketIsValid ;


    public SuperMarket getMySupermarket() {
        return mySupermarket;
    }

    public void setMySupermarket(SuperMarket mySupermarket) {
        this.mySupermarket = mySupermarket;
    }

    public boolean isSuperMarketIsValid() {
        return isSuperMarketIsValid;
    }

    public void setSuperMarketIsValid(boolean superMarketIsValid) {
        isSuperMarketIsValid = superMarketIsValid;
    }

    // RAFI - consistency, either use this or don't
    public Logic() {
        this.mySupermarket = null;
        isSuperMarketIsValid = false;
    }

    public String loadXML(String xmlPathString)  {
        String returnString ;
        Path xmlPath = Paths.get(xmlPathString);
        // RAFI -  use if(isValidFile()))
        // RAFI - don't use over 80-100 chars in line
        if(Files.exists(xmlPath) &&  xmlPathString.substring(xmlPathString.length() - 4).
                toLowerCase().equals(".xml")) {
            returnString = createSDMSuperMarket(xmlPath);  //  create instance of SDM-sdmsupermarket with jaxb
        }
        else {
            returnString = "<" + xmlPathString + " is not " + ".xml" + " file or not exist!>\n"; // 3.1
        }
        return returnString;
    }

    private String createSDMSuperMarket(Path path) {
        String returnString = "";
        try {
            SuperDuperMarketDescriptor temp ;
            JAXBContext jaxbContext = JAXBContext.newInstance(SuperDuperMarketDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            temp = (SuperDuperMarketDescriptor) jaxbUnmarshaller.unmarshal(path.toFile());
            returnString = builedSuperMarket(temp);
            if(this.isSuperMarketIsValid)
                this.mySupermarket = new SuperMarket(temp) ;
        } catch (JAXBException e) {
           returnString = e.getMessage();
        } catch (NullPointerException e) {
            returnString = "<one of the members is NUll>";
        }

        return returnString;
    }

    private String builedSuperMarket(SuperDuperMarketDescriptor temp) {
        String returnString = "";
        boolean isItemIdUnique , isStoreIdUnique, iseReferencedToExistItem ,
         isItemDefinedOnlyOnceAtEachStore ,isEachProductSailedByOneStoreAtLeast, isInCordRange;
        if(!(isItemIdUnique = checkisItemIdUnique(temp.getSDMItems())))
            returnString = "<There are two different items with same id , please load legal .xml>";
        if(!(isStoreIdUnique = checkisStoreIdUnique(temp.getSDMStores())))
            returnString = "<There are two different stores with same id , please load legal .xml>";
        if(!(iseReferencedToExistItem = checkiseReferencedToExistItem(temp )))
            returnString = "<There is price witch references to un-exist item in the store , please load legal .xml>";
        if(!(isEachProductSailedByOneStoreAtLeast = checkisEachProductSailedByOneStoreAtLeast(temp)))
            returnString = "<There is Item witch is not sold by any store, please load legal .xml>";
        if(!(isItemDefinedOnlyOnceAtEachStore = checkisItemDefinedOnlyOnceAtEachStore(temp)))
            returnString = "<There is Item witch sold at same store twice or more , please load legal .xml";
        if(!(isInCordRange = checklisInCordRange(temp)))
            returnString = "<There is store out of range , please load legal .xml >";
        if(isItemIdUnique && isStoreIdUnique && iseReferencedToExistItem && isItemDefinedOnlyOnceAtEachStore
                && isEachProductSailedByOneStoreAtLeast &&  isInCordRange  ) {
            returnString = "<xml file has load successfully>";
            this.isSuperMarketIsValid = true;
        }
            else
                this.isSuperMarketIsValid = false;

        return returnString;
    }

    //3.7
    private boolean checklisInCordRange(SuperDuperMarketDescriptor temp) {
        boolean returnBoolean = true;
        int x ,y ;
        for (SDMStore store: temp.getSDMStores().getSDMStore()) {
           x = store.getLocation().getX();
           y = store.getLocation().getY();
           if(x<1 || x>50 || y<1 || y>50)
               returnBoolean =false ;
        }
        return returnBoolean;
    }

    //3.6
    private boolean checkisItemDefinedOnlyOnceAtEachStore(SuperDuperMarketDescriptor temp) {
        boolean returnBoolean = true;
        int size ;
        for (SDMStore store: temp.getSDMStores().getSDMStore()) {
            size = store.getSDMPrices().getSDMSell().size();
            for(int i = 0 ; i < size-1 ;++i) {
                for (int j = i + 1; j < size; ++j) {
                    if (store.getSDMPrices().getSDMSell().get(i).getItemId() ==
                            store.getSDMPrices().getSDMSell().get(j).getItemId()) {
                        returnBoolean = false;
                    }
                }
            }
        }

        return returnBoolean;
    }

    //3.5
    private boolean checkisEachProductSailedByOneStoreAtLeast(SuperDuperMarketDescriptor temp) {
        boolean returnBoolean = true;
        Set<Integer> itemsById = new HashSet<Integer>();
        Set<Integer> itemesIdByStore = new HashSet<Integer>();
        for (SDMItem item :temp.getSDMItems().getSDMItem()) {
            itemsById.add(item.getId());
        }
        for (SDMStore store: temp.getSDMStores().getSDMStore()) {
            for (SDMSell sell:store.getSDMPrices().getSDMSell()) {
                itemesIdByStore.add(sell.getItemId());
            }
        }
        if(!(itemesIdByStore.containsAll(itemsById)))
            returnBoolean =false;
        return returnBoolean;
    }

    //3.4
    private boolean checkiseReferencedToExistItem(SuperDuperMarketDescriptor temp) {
        boolean returnBoolean = true;
        Set<Integer> itemsById = new HashSet<Integer>();
        for (SDMItem item :temp.getSDMItems().getSDMItem()) {
            itemsById.add(item.getId());
        }
        for (SDMStore store: temp.getSDMStores().getSDMStore()) {
            for (SDMSell sell:store.getSDMPrices().getSDMSell()) {
                if(!(itemsById.contains(sell.getItemId())))
                    returnBoolean =false;
            }
        }
        return returnBoolean;
    }

    //3.3
    private boolean checkisStoreIdUnique(SDMStores sdmStores) {
        boolean returnBoolean = true;
        int size = sdmStores.getSDMStore().size();
        for(int i = 0 ; i<size-1;++i)
            for(int j=i+1;j<size;++j)
                if(sdmStores.getSDMStore().get(i).getId() == sdmStores.getSDMStore().get(j).getId() &&
                !sdmStores.getSDMStore().get(i).getName().equals(sdmStores.getSDMStore().get(j).getName())){
                    returnBoolean = false;
                    break;
                }
                return returnBoolean;
    }

    //3.2
    private boolean checkisItemIdUnique(SDMItems sdmItems) {
        boolean returneBoolean = true ;
        int size = sdmItems.getSDMItem().size();
        for(int i = 0 ; i<size-1 ; ++i)
            for(int j = i+1 ; j < size;++j )
                if(sdmItems.getSDMItem().get(i).getId() == sdmItems.getSDMItem().get(j).getId() &&
                !sdmItems.getSDMItem().get(i).getName().equals(sdmItems.getSDMItem().get(j).getName())){
                    returneBoolean = false ;
                    break;
                }
        return returneBoolean ;
    }

    public String DisplayStoreDetails() {
        String returnString ;
        if (this.mySupermarket != null)
            returnString =  this.mySupermarket.printStoresDetiales();
        else
            returnString = "<you should load legal .xml file before.>";
        return returnString;
    }

    public String DisplayAllProducts() {
        String returnString = "";
        if (this.mySupermarket != null)
            returnString =  this.mySupermarket.printItemsDetiales();
        else
            returnString = "<you should load legal .xml file before.>";
        return returnString;
    }

    public String makeAnOrder(int chosenStoreIdFromUser, Date orderDate, Point cord, Map<Integer, Float> itemIdMap) {

        Store store = mySupermarket.getStoreMap().get(chosenStoreIdFromUser);
        double itemCost = calcItemsCost(itemIdMap,chosenStoreIdFromUser );
        double takeAwayCost = calcTakeAwayDistance(cord,store)*store.getStore().getDeliveryPpk();
        Order orderToSave = new Order(orderDate,chosenStoreIdFromUser,itemIdMap.size(),itemCost,takeAwayCost,
                itemCost + takeAwayCost);

        store.updateSoldItemsAtStore(itemIdMap);
        store.getStoresOrders().add(orderToSave);
        mySupermarket.getOrdersHistory().add(orderToSave);

        return "<Order Has been made successfully.>";
    }

    private double calcItemsCost(Map<Integer, Float> itemIdMap,int storeId) {
        double cost = 0 ;

        for (int itemId : itemIdMap.keySet()) {
            Store store = mySupermarket.getStoreMap().get(storeId);
            cost = cost + (itemIdMap.get(itemId) * store.getPriceMap().get(itemId));
        }
        return cost;
    }

    public String presentOrderHistory() {
        String returnString = "";
        if(this.mySupermarket != null) {
           returnString = mySupermarket.displayHistory();
        }
        else
            returnString="<you should load legal .xml file before.>";
        return returnString;
    }

    public boolean isStoreExist(int id) {
        return this.getMySupermarket().getStoreMap().containsKey(id) ? true : false ;

    }

    public String storeDetailesForOrder() {
        String returnString = "";
        for (Store store:getMySupermarket().getStoresSet()) {
            returnString = returnString + "** Store id : \"" + store.getStore().getId() + "\""+
                    "store name: " + "\"" +  store.getStore().getName() + "\"" +
                    "store ppk: \"" + store.getStore().getDeliveryPpk() + "\"\n"; // + returnString;
        }
        return returnString;
    }

    public boolean askIsLocationHitsAstore(Point cord) {
        Location userLocation = new Location();
        int x = (int)cord.getX();
        int y = (int)cord.getY();
        userLocation.setX(x);
        userLocation.setY(y);
        for (Store store: mySupermarket.getStoresSet()) {
            if(store.getStore().getLocation().getX() == cord.getX() &&
                    store.getStore().getLocation().getY() == cord.getY())
                return true;
        }
        return false;
    }

    public String askItemsDetailesForUserOrder(int chosenStoreIdFromUser) {
        Store chosenStore = mySupermarket.getStoreMap().get(chosenStoreIdFromUser);
        Set<SDMItem> itemSet =  mySupermarket.getItemSet();
        String returnString = "" , itemString  ;
        for (SDMItem item: itemSet) {
            returnString = returnString +  "* Item id: " + item.getId() + "; Item name: " + item.getName() +
                    "; Purches category: " + item.getPurchaseCategory() + "; One unit price: " + oneUnitePrice(chosenStore,item) + "\n";
        }
        return returnString;
    }

    private String oneUnitePrice(Store store , SDMItem item) {
        Map<Integer,Integer> priceMap =store.getPriceMap();
        int itemId = item.getId();
        if(priceMap.keySet().contains(itemId))
        {
            if(item.getPurchaseCategory().toLowerCase().equals("weight"))
                return  store.getPriceMap().get(itemId) + " NIS for KG.";
            else
                return  store.getPriceMap().get(itemId) + " NIS for one item.";
        }
        else
            return " The store not selling this item";
    }

    public boolean doesStoreSellsThisItem(int choosedItemId, int chosenStoreIdFromUser) {
        return mySupermarket.getStoreMap().get(chosenStoreIdFromUser).getPriceMap().containsKey(choosedItemId);
    }

    public String askOrderDisplay(int chosenStoreIdFromUser, Point cord, Map<Integer, Float> itemIdMap) {
        String returnString = "Order Summery:\n";
        Store store =  mySupermarket.getStoreMap().get(chosenStoreIdFromUser);
        double dist = calcTakeAwayDistance(cord , store);
        double takeAwayCost =  store.getStore().getDeliveryPpk() * dist ;
        String format1 =  new DecimalFormat("##.##").format(dist);
        String format2 = new DecimalFormat("##.##").format(takeAwayCost);
        for (int itemId: itemIdMap.keySet()) {
            returnString = returnString + "Item id: " + itemId + " ;Item name : " + mySupermarket.getItemMap().get(itemId).getName() +
                    " ;Purchase category: " + mySupermarket.getItemMap().get(itemId).getPurchaseCategory()+
                    " ;Item price: "+store.getPriceMap().get(itemId)+
                    " ;Quantity: " + itemIdMap.get(itemId).floatValue() +
                    " ;Item cost: " + (((store.getPriceMap().get(itemId)) * itemIdMap.get(itemId).floatValue())) + "\n";

        }
        return returnString  +   "Delivery cost : " + format2  +
        " ;Distance for store: " + format1  +
                " , PPK : " +  store.getStore().getDeliveryPpk();
    }

    private double calcTakeAwayDistance(Point cord, Store store) {
        int storeX , storeY , userX , userY ;
        storeX = store.getStore().getLocation().getX();
        storeY = store.getStore().getLocation().getY();
        userX =(int)cord.getX();
        userY = (int)cord.getY();
        int dx = storeX-userX;
        int dy = storeY - userY;
        return Math.sqrt((dx*dx) + (dy*dy));

    }

    public int askPurcheCategoryAsInt(int choosedItemId) {
        if(mySupermarket.getItemMap().get(choosedItemId).getPurchaseCategory().toLowerCase().equals("quantity"))
            return 1;
        return 2;
    }

    public boolean askIsXmlLoaded() {
        if(this.mySupermarket != null)
            return true;
        return false;
    }
}

