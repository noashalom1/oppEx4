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
        List<Property> allProperties = PropertyManager.getInstance().getProperties();
        if (!allProperties.contains(property)) {
            System.out.println("Property not found");
            throw new PropertyNotFoundException("Property not found");
        }
        for (Property p : allProperties) {
            if (p.equals(property)) {
                if (!p.getOwner().equals(this)) {
                    System.out.println("Only " + p.getOwner().getName() + " can delete property at " + p.getAddress());
                    throw new UnauthorizedDeleteException("You are not the owner of this property");
                } else {
                    PropertyManager.getInstance().removeProperty(p);
                    System.out.println(name + " deleted property at " + property.getAddress());
                    notifyBroker(property);
                    return;
                }
            }
        }

        PropertyManager.getInstance().removeProperty(property);
        System.out.println(name + " deleted property at " + property.getAddress());
        notifyBroker(property);
    }

    private void notifyBroker(Property property) {
        System.out.println("Notification to " + broker.name + ": Property at " + property.getAddress() + " has been deleted by " + name);
    }

    public Broker getBroker() {
        return broker;
    }

    @Override
    public String toString() {
        return name + " (Broker: " + (broker != null ? broker.getName() : "Unknown") + ")";
    }

    public static Seller fromString(String sellerString) {
        // בדיקת קלט ריק או null
        if (sellerString == null || sellerString.trim().isEmpty()) {
            System.out.println("Seller string is empty");
            return null;
        }

        String[] parts = sellerString.split(",");

        // Ensure there are at least 2 parts (seller name and broker name)
        if (parts.length < 2) {
            System.out.println("Invalid seller format");
            return null;
        }

        try {
            String sellerName = parts[0].trim();
            String brokerName = parts[1].trim();

            // וידוא שהשמות אינם ריקים
            if (sellerName.isEmpty() || brokerName.isEmpty()) {
                System.out.println("Seller or broker name is missing.");
                return null;
            }

            // יצירת Broker ו-Seller
            Broker broker = new Broker(brokerName);
            return new Seller(sellerName, broker);
        } catch (Exception e) {
            System.out.println("Unexpected error while parsing seller");
            return null;
        }
    }

}