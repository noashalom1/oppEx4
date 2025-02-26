package users;
import exceptions.PropertyNotFoundException;
import exceptions.UnauthorizedDeleteException;
import models.*;
import management.PropertyManager;

import java.util.ArrayList;
import java.util.List;

// Observer Pattern - Seller notifies Broker on property deletion
public class Seller extends User {
    private Broker broker;


    public Seller(String name, Broker broker) {
        super(name);
        this.broker = broker;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Seller)) {
            return false;
        }
        Seller seller = (Seller) obj;
        return name.equals(seller.name);
    }

    public void deleteProperty(Property property) throws UnauthorizedDeleteException, PropertyNotFoundException {
        List <Property> allProperties = PropertyManager.getInstance().getProperties();
        if(!allProperties.contains(property)){
           throw new PropertyNotFoundException("Property not found");
        }
        for(Property p:allProperties){
            if(p.equals(property)){
                if(!p.getOwner().equals(this)){
                    throw new UnauthorizedDeleteException("You are not the owner of this property");
                }
                else{
                    allProperties.remove(p);
                    System.out.println(name + " deleted property at " + property.getAddress());
                    notifyBroker(property);
                    return;
                }
            }
        }

        allProperties.remove(property);
        System.out.println(name + " deleted property at " + property.getAddress());
        notifyBroker(property);
    }

    private void notifyBroker(Property property) {
        System.out.println("Notification to " + broker.name + ": Property at " + property.getAddress() + " has been deleted by " + name);
    }

    public Broker getBroker() {
        return broker;
    }
}