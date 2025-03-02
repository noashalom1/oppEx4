package models;

import users.Seller;

public class Apartment extends Property {
    public Apartment(int sizeInSquareMeters, int price, Address address, boolean isSold, Seller owner) {
        super(sizeInSquareMeters, price, address, isSold, owner);
    }

    @Override
    public void getInfo() {
        System.out.println("Apartment");
        super.displayInfo();
    }
}