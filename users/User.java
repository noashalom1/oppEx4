package users;

import management.PropertyManager;
import models.Address;
import models.Property;

import java.util.List;

// Abstract User Class
public abstract class User {
    protected String name;

    public User(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void searchPropertiesByRadius(Address center, double radius) {
        List<Property> results = PropertyManager.getInstance().getPropertiesWithinRadius(center, radius);
        System.out.println("Properties within radius " + radius + ":");
        results.forEach(Property::displayInfo);
    }

    public void getSoldPropertiesByRadius(Address center, double radius) {
        List<Property> results = PropertyManager.getInstance().getPropertiesWithinRadius(center, radius);
        System.out.println("Sold properties within radius " + radius + ":");
        results.stream().filter(Property::isSold).forEach(Property::displayInfo);
    }
}