package UI;

import control.Controller;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class UI {

    // Defines :
    static final int LOAD_XML = 1 ;
    static final int DISPLAY_STORE_DETAILS = 2 ;
    static final int  DISPLAY_ALL_ITEMS = 3 ;
    static final int  MAKE_AN_ORDER = 4 ;
    static final int  SHOW_HISTORY = 5;
    static final int   EXIT = 6 ;
    static final int MIN_RANGE = 1 ;
    static final int MAX_RANGE = 6 ;
    // finish Defines


    private  Controller control ;
    private boolean keepRunning;



    private UI() {
        this.control = new Controller();
        this.keepRunning = true ;
    }

    public void setKeepRunning(boolean keepRunning) {
        this.keepRunning = keepRunning;
    }

    public boolean isKeepRunning() {
        return keepRunning;
    }

    public  static void printWelcome(){
        System.out.println("Welcome to Super Duper Market !!!");
    }

    public static void printMenu(){
        System.out.println("Please choose your option by pressing the corresponding selection number:");
        System.out.println("1.Load xml file. (Attention you can not use any other option **2-5** ,before loading legal.xml file.)");
        System.out.println("2.Display stores details.");
        System.out.println("3.Display all products.");
        System.out.println("4.Make an order.");
        System.out.println("5.Present order history.");
        System.out.print("6.exit \n");
    }

    public static String getInputFromUser(){
        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }

    public  String checkInputForMenu(String input ){
        int option = 0;
        String returnString = "" ; // initial ;
        try{
            option = Integer.parseInt(input);
            if(option < MIN_RANGE || option > MAX_RANGE )
               returnString =  " \n<You entered option out of range! Please choose valid option (1,2,3,4,5,6)>";
            else {
               switch (option){
                   case LOAD_XML :printString("Please enter .xml file path");
                           String xmlPathString = getInputFromUser();
                           returnString =  this.control.checkIsXml(xmlPathString) ;
                            break;
                   case DISPLAY_STORE_DETAILS : returnString = this.control.DisplayStoreDetails(); break;
                   case DISPLAY_ALL_ITEMS : returnString = this.control.DisplayAllProducts(); break;
                   case MAKE_AN_ORDER :
                       if(control.isXmlLoaded())
                       returnString = dialogForOrder();
                       else
                           returnString = "<you should load legal .xml file before.>";
                       break;
                   case SHOW_HISTORY : returnString = this.control.presentOrderHistory();   break;
                   case EXIT : returnString = "BYE BYE" ;
                   this.setKeepRunning(false);
                   break;
               }
            }
        }
        catch(NumberFormatException e){
            returnString = e.getMessage() + ":" +
                    "<You entered string instead of integer. \nPlease choose valid option (1,2,3,4,5,6) >";
        }finally {
            return returnString;
        }
    }

    private String dialogForOrder() {
        showStoreDetailes();
        int chosenStoreIdFromUser = getChosenStoreIdFromUser();
        Date orderDate = getDateFromUser();
        Point cord = getCordFromUser();
        Map<Integer,Float> itemIdMap = new HashMap<Integer,Float>();
        while (addItemToOrder(itemIdMap,chosenStoreIdFromUser)); // q has not pressed.
        String presentOrderToUser = makeAnOrder(chosenStoreIdFromUser,orderDate,cord,itemIdMap);
         return presentOrderToUser;
    }

    private String makeAnOrder(int chosenStoreIdFromUser, Date orderDate, Point cord, Map<Integer, Float> itemIdMap) {
        displayOrder(chosenStoreIdFromUser,cord,itemIdMap);
        if(confirmOrder())
            return this.control.sendOrderToStore(chosenStoreIdFromUser,orderDate,cord,itemIdMap);
        return "Abort order.";
    }

    private boolean confirmOrder() {
        boolean keepAsking = true;
        while(keepAsking) {
            printString("Would you like save order? y/n");
            String input = getInputFromUser();
            if (input.toLowerCase().equals("y"))
                return true;
            else if (input.toLowerCase().equals("n"))
                return false;
        }
        return true;
    }

    private void displayOrder(int chosenStoreIdFromUser, Point cord, Map<Integer, Float> itemIdMap) {
       printString(control.makeDisplay(chosenStoreIdFromUser,cord,itemIdMap));
    }

    private boolean addItemToOrder(Map<Integer, Float> itemIdMap, int chosenStoreIdFromUser) {
        int input = 0 ,choosedItemId = 0 , quantityInt = 0 ,perchesCatagory = 0 ;
        float quantityFloat = 0 , quantity = 0 ;
        String itemId="" , itemDisplay =  this.control.showStoreItemsForUser(chosenStoreIdFromUser) ;
        try{
            printString(itemDisplay);// presents the items in store.
            printString("For adding item to order please insert item id ; To finish order please press \"q\":");
            itemId = getInputFromUser();
            choosedItemId = Integer.parseInt(itemId);
            perchesCatagory = control.getPurcheCatagoryAsInt(choosedItemId);
            if(this.control.isItemBeSoldByStore(choosedItemId,chosenStoreIdFromUser)){
                printString("please enter quantity:");
                try {
                    String quantityInput = getInputFromUser();
                    if(perchesCatagory == 1 ) {
                        quantityInt = Integer.parseInt(quantityInput);
                        quantity = quantityInt;
                    } else {
                        quantityFloat = Float.parseFloat(quantityInput);
                        quantity = quantityFloat;
                    }
                    printString("great choice.");
                }catch (NumberFormatException e){
                    printString("<"+e.getMessage() +">"+ "not legal quantity");
                    return true;
                }if(itemIdMap.containsKey(choosedItemId)){
                    float preQuantity = itemIdMap.get(choosedItemId);
                    float newQuantity = preQuantity + quantity;
                    itemIdMap.replace(choosedItemId,newQuantity);
                    return true;
                } else
                    itemIdMap.put(choosedItemId,quantity);
                return true;
            } else
                printString("<The store you chose do not sell this item.>");
            return true;
        }catch (NumberFormatException e){
            if(itemId.equals("q"))
                return false  ;
            printString( "<"+e.getMessage() + ">"+ " Please enter item Id ");
            return true;
        }
    }

    private Point getCordFromUser() {
        int x=0 , y=0 ;
        Point cord = new Point(x,y);

        try {
            while(isNotlegalCord(cord)) {
                printString("please enter x location: ");
                String xLocation = getInputFromUser();
                x = Integer.parseInt(xLocation);
                printString("please enter y location: ");
                String yLocation = getInputFromUser();
                y = Integer.parseInt(yLocation);
                //if(isNotlegalCord(cord))
                cord.setLocation(x,y);
            }
        }catch (NumberFormatException e){
            printString(e.getMessage() );
            return getCordFromUser();
        }
        return cord;
    }

    private boolean isNotlegalCord(Point cord) {
        int x = (int) cord.getX();
        int y = (int)cord.getY();
        boolean answer = false;
        if(x<1 || x>50 || y<1 || y>50){
            printString("please enter location in range of 1-50");
            answer = true;
        }

        else{
            if(control.isLocationHitsAstore(cord)){
                printString("<There is a store on this location>,please enter other location.");
                answer =  true;
            }
        }
           return answer;
    }

    private Date getDateFromUser() {
        boolean validDatee = false;
        printString("Please enter order date in dd/mm-hh:mm format");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM-HH:mm");
        formatter.setLenient(false);
        Date parsedDate = null;
        try{
            String dateInput = getInputFromUser();
            parsedDate = formatter.parse(dateInput);
            printString("<validated DATE TIME> " + formatter.format(parsedDate));
        }catch (ParseException e) {
            printString("<"+e.getMessage()+">");
            getDateFromUser();
        }
        return parsedDate;
    }

    private int getChosenStoreIdFromUser() {
        int option = 0;
        printString("Please choose Store by entering chosen store id.");
       try{
           option = Integer.parseInt(getInputFromUser());
           if(!this.control.checkStoreIdInput(option)) {
               printString("<chosen store Id not exist > , please enter store id.");
               option = getChosenStoreIdFromUser();
           }
       }catch (NumberFormatException e){
           printString(e.getMessage() + "string is no id.");
           return getChosenStoreIdFromUser();
       }
       return option;
    }

    private void showStoreDetailes() {
        printString("OUR STORE LIST:");
        printString(this.control.askStoreDetailesForOrder());
    }

    public static void  printString(String s){
        System.out.println(s);
    }

    public void execute(){
         String massage ;
         String input ;
         printMenu();
         input = getInputFromUser();
         massage =  checkInputForMenu(input);
         printString(massage);
    }

    public static void main(String[] args) {
        UI app = new UI();
        app.printWelcome();
        while (app.isKeepRunning())
            app.execute();
    }

}
