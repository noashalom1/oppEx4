package models;
import users.*;
import exceptions.*;

import java.sql.SQLOutput;
import java.util.Arrays;


// Template Method Pattern - defines the skeleton of an algorithm in Property
public abstract class Property {
    private int sizeInSquareMeters;
    private int price;
    private boolean isSold;
    private Address address;

    private Seller owner;

    public Property(int sizeInSquareMeters, int price, Address address, boolean isSold, Seller owner) {
        this.sizeInSquareMeters = sizeInSquareMeters;
        this.price = price;
        this.address = address;
        this.isSold = isSold;
        this.owner = owner;
    }
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Property other = (Property) obj;
        return sizeInSquareMeters == other.sizeInSquareMeters && price == other.price && isSold == other.isSold && address.equals(other.address);
    }

    public Address getAddress() {
        return address;
    }
    public Seller getOwner() {
        return owner;
    }

    public int getPrice() {
        return price;
    }

    public int getSizeInSquareMeters() {
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
        // Check for null or empty input
        if (fileString == null || fileString.trim().isEmpty()) {
            System.out.println("File string is empty");
            return null;
        }
        String[] parts = fileString.split(",");
        // Ensure there are at least 4 parts (size, price, isSold, address)
        if (parts.length < 4) {
            System.out.println("Invalid string format");
            return null;
        }
        try {
            int size = Integer.parseInt(parts[0].trim());
            int price = Integer.parseInt(parts[1].trim());
            boolean isSold = Boolean.parseBoolean(parts[2].trim());
            // Extract the address (remaining parts)
            String addressString = String.join(",", Arrays.copyOfRange(parts, 3, parts.length));
            Address address = Address.fromString(addressString);
            // Ensure the address is valid
            if (address == null) {
                return null;
            }
            // Create and return the apartment
            return new Apartment(size, price, address, isSold);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format");
            return null;
        } catch (Exception e) {
            System.out.println("unexpected error");
            return null;
        }
    }


    // Restricting edit access to Broker only
    public void setProperty(int newPrice, int newSize,boolean newStatus) throws UnauthorizedEditException {
        this.price = newPrice;
        this.sizeInSquareMeters = newSize;
        this.isSold = newStatus;
    }

    // Hook Method - to be implemented by subclasses
    public abstract void getInfo();
}