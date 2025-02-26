package users;
import exceptions.DuplicateAddressException;
import exceptions.InsufficientFundsException;
import exceptions.PropertyNotFoundException;
import exceptions.UnauthorizedEditException;
import models.*;
import management.PropertyManager;
import services.CleaningService;
import services.DesignService;
import services.MovingService;
import services.SuretyService;

import java.net.StandardSocketOptions;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class Broker extends User {
    List <String> Mail;
    PropertyManager propertyManager = PropertyManager.getInstance();
    List <Property> properties = propertyManager.getProperties();
    public Broker(String name) {
        super(name);
        Mail=new ArrayList<>();
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Broker)) {
            return false;
        }
        Broker broker = (Broker) obj;
        return name.equals(broker.name);
    }
    public void editProperty(Property property,Integer newPrice, Integer newSize, Boolean newStatus) throws UnauthorizedEditException, PropertyNotFoundException {

        if (property == null) {
            throw new IllegalArgumentException("Property cannot be null");
        }
        int currPrice = newPrice == null ? property.getPrice() : newPrice;
        int currSize = newSize == null ? property.getSizeInSquareMeters() : newSize;
        boolean currStatus = newStatus == null ? property.isSold() : newStatus;
        boolean found=false;
        for(Property pr1:properties){
            if(pr1.equals(property)){
                found=true;
                if(!pr1.getOwner().getBroker().equals(this)){//no need for cast since we are checking for the instance of Broker before
                    throw new UnauthorizedEditException("Only the broker of this property can edit the property");
                }
                break;
            }
        }
        if(!found){
            throw new PropertyNotFoundException("Property not found");
        }
        else {
            for (Property pr1 : properties) {
                if (pr1.equals(property)) {
                    // Update only if values are provided
                    property.setProperty(
                            currPrice,currSize,currStatus);
                    System.out.println("Property at " + property.getAddress() + " updated by broker" + getName()+ "to price: $" + currPrice + " and size: " + currSize + " sqm" + " and status: " + (currStatus ? "Sold" : "Available"));
                    break;
                }
            }
        }
    }

    public void sellProperty(Broker broker,Property property,Buyer buyer,boolean isCleanService,boolean isMovingService,boolean isSuretyService,boolean isDesignService) throws UnauthorizedEditException, PropertyNotFoundException, InsufficientFundsException {
        if (property == null||broker==null||buyer==null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        int size = property.getSizeInSquareMeters();
        if(isCleanService){
            property=new CleaningService(property);
        }
        if(isMovingService){
            property=new MovingService(property);
        }
        if(isSuretyService){
            property=new SuretyService(property);
        }
        if(isDesignService){
            property=new DesignService(property);
        }
        int price = property.getPrice();

        boolean found=false;
        for(Property pr1:properties){
            if(pr1.equals(property)){
                found=true;
                if(!pr1.getOwner().getBroker().equals(broker)){/
                    throw new UnauthorizedEditException("Only the broker of this property can sell the property");
                }
                if(buyer.getFunds()<price){
                    throw new InsufficientFundsException("Buyer has insufficient funds to buy the property");
                }
                break;
            }
        }
        if(!found){
            throw new PropertyNotFoundException("Property not found");
        }
        else {
            for (Property pr1 : properties) {
                if (pr1.equals(property)) {

                    property.setProperty(
                            price,size,true);
                    buyer.setFunds(buyer.getFunds()-price);
                    System.out.println("Property at " + property.getAddress() + " sold by broker" + broker.getName()+ "to Buyer " + buyer + " at price:" +price+ "$");
                    break;
                }
            }
        }
    }




    }
}