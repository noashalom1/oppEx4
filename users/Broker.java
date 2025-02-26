package users;
import exceptions.DuplicateAddressException;
import exceptions.UnauthorizedEditException;
import models.*;
import management.PropertyManager;

import java.net.StandardSocketOptions;
import java.sql.SQLOutput;
import java.util.List;

public class Broker extends User {
    public Broker(String name) {
        super(name);
    }

    public void addProperty(Property property) throws DuplicateAddressException {
        PropertyManager.getInstance().addProperty(property);
        System.out.println(name + " added property: " + property.getAddress());
    }

    public void editPropertyDetails(Property property, double newPrice, double newSize) throws UnauthorizedEditException {
        property.editPropertyDetails(this, newPrice, newSize);
    }
    public void loadPropertyList(String dir){
        PropertyManager.getInstance().loadPropertiesFromFile(dir);
    }

    public void listAllProperties() {
        List<Property> properties=PropertyManager.getInstance().getProperties();
        for (Property pr1:properties){
            pr1.displayInfo();
            System.out.println("------------------------------------------");

        }
    }
}