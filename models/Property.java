package models;
import users.*;
import exceptions.*;
import java.util.Arrays;


// Template Method Pattern - defines the skeleton of an algorithm in Property
public abstract class Property {
    private double sizeInSquareMeters;
    private double price;
    private boolean isSold;
    private Address address;

    public Property(double sizeInSquareMeters, double price, Address address) {
        this.sizeInSquareMeters = sizeInSquareMeters;
        this.price = price;
        this.address = address;
        this.isSold = false;
    }

    public Address getAddress() {
        return address;
    }

    public double getPrice() {
        return price;
    }

    public double getSizeInSquareMeters() {
        return sizeInSquareMeters;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    // Template Method
    public void displayPropertyInfo() {
        System.out.println("Address: " + address);
        displaySpecificDetails();  // Hook for subclasses
        System.out.println("Price: $" + price);
        System.out.println("Status: " + (isSold ? "Sold" : "Available"));
    }

    // Hook Method - to be implemented by subclasses
    protected abstract void displaySpecificDetails();

    public String toFileString() {
        return sizeInSquareMeters + "," + price + "," + isSold + "," + address.toString();
    }

    public static Property fromFileString(String fileString) {
        String[] parts = fileString.split(",");
        double size = Double.parseDouble(parts[0]);
        double price = Double.parseDouble(parts[1]);
        boolean isSold = Boolean.parseBoolean(parts[2]);
        String addressString = String.join(",", Arrays.copyOfRange(parts, 3, parts.length));
        Address address = Address.fromString(addressString);
        Property property = new Apartment(size, price, address);
        property.setSold(isSold);
        return property;
    }

    // Restricting edit access to Broker only
    public void editPropertyDetails(User user, double newPrice, double newSize) throws UnauthorizedEditException{
        if (!(user instanceof Broker)) {
            throw new UnauthorizedEditException("Only brokers can edit property details.");
        }
            this.price = newPrice;
            this.sizeInSquareMeters = newSize;
            System.out.println("Property at " + address + " updated by broker to price: $" + newPrice + " and size: " + newSize + " sqm");
        }
    }

