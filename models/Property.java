package models;
import users.*;
import exceptions.*;

import java.sql.SQLOutput;
import java.util.Arrays;


// Template Method Pattern - defines the skeleton of an algorithm in Property
public abstract class Property {
    private double sizeInSquareMeters;
    private double price;
    private boolean isSold;
    private Address address;

    public Property(double sizeInSquareMeters, double price, Address address, boolean isSold) {
        this.sizeInSquareMeters = sizeInSquareMeters;
        this.price = price;
        this.address = address;
        this.isSold = isSold;
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
    public void displayInfo() {
        System.out.println("Address: " + address);
        System.out.println("Size: " + sizeInSquareMeters + " sqm");
        System.out.println("Price: $" + getPrice());
        System.out.println("Status: " + (isSold ? "Sold" : "Available"));
    }


    public static Property fromFileString(String fileString) {
        String[] parts = fileString.split(",");
        double size = Double.parseDouble(parts[0]);
        double price = Double.parseDouble(parts[1]);
        boolean isSold = Boolean.parseBoolean(parts[2]);
        String addressString = String.join(",", Arrays.copyOfRange(parts, 3, parts.length));
        Address address = Address.fromString(addressString);
        Property property = new Apartment(size, price, address, isSold);
        property.setSold(isSold);
        return property;
    }

    // Restricting edit access to Broker only
    public void setProperty(User user, double newPrice, double newSize) throws UnauthorizedEditException {
        if (!(user instanceof Broker)) {
            throw new UnauthorizedEditException("Only brokers can edit property details.");
        }
        this.price = newPrice;
        this.sizeInSquareMeters = newSize;
        System.out.println("Property at " + address + " updated by broker to price: $" + newPrice + " and size: " + newSize + " sqm");
    }

    // Hook Method - to be implemented by subclasses
    public abstract void getInfo();
}