package management;

import exceptions.*;
import models.*;
import users.Broker;
import users.Buyer;
import users.User;

import java.util.*;
import java.io.*;

// Singleton Pattern - ensures only one instance of PropertyManager exists
public class PropertyManager {
    private static PropertyManager instance;
    private List<Property> properties = new ArrayList<>();

    private PropertyManager() {

    }

    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }
    public void removeProperty(Property property) {
        properties.remove(property); // This modifies the underlying list
    }

    public void addProperty(Property property) throws DuplicateAddressException, SubUnitException {
        List<Property> toRemove = new ArrayList<>();
        for (Property existingProperty : properties) {
            if (existingProperty.getAddress().equals(property.getAddress()))
                throw new DuplicateAddressException("Property at " + property.getAddress() + " already exists. Skipping addition.");
            if (existingProperty.getAddress().isSubUnitOf(property.getAddress()))
                throw new SubUnitException("existing property at " + existingProperty.getAddress() + " is a sub-unit of the property that is tried to add. Skipping addition.");
            if (property.getAddress().isSubUnitOf(existingProperty.getAddress())) {
                toRemove.add(existingProperty);
            }
        }
        if (!toRemove.isEmpty()) {
            for (Property p : toRemove) {
                properties.remove(p);
            }
        }
        properties.add(property);
    }

    public List<Property> getProperties() {
        return Collections.unmodifiableList(properties);
    }

    public void listAllProperties() {
        properties.forEach(property -> property.displayInfo());
    }

    public void loadProperties(String filename) {
        properties.clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                Property newApartment = Property.fromFileString(line);
                if (newApartment != null) {
                    try {
                        addProperty(newApartment);
                    } catch (DuplicateAddressException | SubUnitException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            }
            System.out.println("Properties loaded from " + filename);
        } catch (IOException e) {
            System.err.println("Error loading properties: " + e.getMessage());
        }
    }
}