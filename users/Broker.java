package users;

import exceptions.InsufficientFundsException;
import exceptions.PropertyNotFoundException;
import exceptions.UnauthorizedEditException;
import exceptions.UnavailableProperty;
import models.*;
import management.PropertyManager;
import services.CleaningService;
import services.DesignService;
import services.MovingService;
import services.SuretyService;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.List;

public class Broker extends User {
    List<String> Mail;
    PropertyManager propertyManager = PropertyManager.getInstance();
    List<Property> properties = propertyManager.getProperties();

    public Broker(String name) {
        super(name);
        Mail = new ArrayList<>();
    }

    public String getName() {
        return name;
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

    public void editProperty(Property property, Integer newPrice, Integer newSize, Boolean newStatus) throws UnauthorizedEditException, PropertyNotFoundException {
        List<Property> allProperties = PropertyManager.getInstance().getProperties();
        if (!allProperties.contains(property)) {
            System.out.println("Property not found");
            throw new PropertyNotFoundException("Property not found");
        }
//        if (property == null) {
//            throw new IllegalArgumentException("Property cannot be null");
//        }
        int currPrice = newPrice == null ? property.getPrice() : newPrice;
        int currSize = newSize == null ? property.getSizeInSquareMeters() : newSize;
        boolean currStatus = newStatus == null ? property.isSold() : newStatus;
        boolean found = false;
        for (Property pr1 : properties) {
            if (pr1.equals(property)) {
                found = true;
                if (!pr1.getOwner().getBroker().equals(this)) {//no need for cast since we are checking for the instance of Broker before
                    System.out.println("Only " + pr1.getOwner().getBroker().getName() + " can edit property at " + pr1.getAddress());
                    throw new UnauthorizedEditException("Only the broker of this property can edit the property");
                }
                break;
            }
        }
        if (!found) {
            System.out.println("Property not found");
            throw new PropertyNotFoundException("Property not found");
        } else {
            for (Property pr1 : properties) {
                if (pr1.equals(property)) {
                    // Update only if values are provided
                    property.setProperty(
                            currPrice, currSize, currStatus);
                    System.out.println("Property at " + property.getAddress() + " updated by broker" + getName() + " to price: $" + currPrice + " and size: " + currSize + " sqm" + " and status: " + (currStatus ? "Sold" : "Available"));
                    break;
                }
            }
        }
    }

    public void sellProperty(Broker broker, Property property, Buyer buyer, boolean isCleanService, boolean isMovingService, boolean isSuretyService, boolean isDesignService) throws UnauthorizedEditException, PropertyNotFoundException, InsufficientFundsException, UnavailableProperty {

        if (property == null || broker == null || buyer == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }

        // Keep a reference to the original property so we can find it in PropertyManager’s list
        Property originalProperty = property;

        // Create a "decoratedProperty" for final price calculation
        Property decoratedProperty = property;
        if (isCleanService) {
            decoratedProperty = new CleaningService(decoratedProperty);
        }
        if (isMovingService) {
            decoratedProperty = new MovingService(decoratedProperty);
        }
        if (isSuretyService) {
            decoratedProperty = new SuretyService(decoratedProperty);
        }
        if (isDesignService) {
            decoratedProperty = new DesignService(decoratedProperty);
        }

        int finalPrice = decoratedProperty.getPrice(); // Price after added services

        boolean found = false;

        for (Property pr1 : properties) {
            // Compare with the original property in the list
            if (pr1.equals(originalProperty)) {
                found = true;

                // 1. Check broker ownership
                if (!pr1.getOwner().getBroker().equals(broker)) {
                    throw new UnauthorizedEditException("Only the broker of this property can sell the property");
                }

                // 2. Check buyer funds
                if (buyer.getFunds() < finalPrice) {
                    System.out.println("Buyer " + buyer.getName() + " does not have enough budget for property at " + pr1.getAddress());
                    throw new InsufficientFundsException("Buyer has insufficient funds to buy the property");
                }

                // 3. Check if property is already sold
                if (pr1.isSold()) {
                    throw new UnavailableProperty("This property is unavailable");
                }

                // 4. Finalize the sale
                pr1.setProperty(finalPrice, pr1.getSizeInSquareMeters(), true);
                buyer.setFunds(buyer.getFunds() - finalPrice);
                System.out.println("SUCCESS: Property at " + pr1.getAddress()
                        + " was sold by broker " + broker.getName()
                        + " to buyer " + buyer.getName()
                        + " for $" + finalPrice);
                break;
            }
        }

        if (!found) {
            throw new PropertyNotFoundException("Property not found");
        }
    }

}
