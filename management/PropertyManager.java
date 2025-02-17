package management;
import exceptions.*;
import models.*;
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

    public void addProperty(Property property) throws DuplicateAddressException {
        for (Property existingProperty : properties) {
            if (existingProperty.getAddress().equals(property.getAddress())) {
                throw new DuplicateAddressException("A property with address " + property.getAddress() + " already exists.");
            }
        }
        properties.add(property);
    }

    public void removeProperty(Property property) {
        properties.remove(property);
    }//add exception for property not excited

    public List<Property> getProperties() {
        return Collections.unmodifiableList(properties);
    }

    public void listAllProperties() {
        properties.forEach(property -> property.displayPropertyInfo());
    }

    public List<Property> getPropertiesWithinRadius(Address center, double radius) {
        List<Property> result = new ArrayList<>();
        for (Property property : properties) {
            if (center.calculateDistance(property.getAddress()) <= radius) {
                result.add(property);
            }
        }
        return result;
    }
    public double getAveragePricePerSquareMeter(Address center, double radius) {
        List<Property> nearbyProperties = getPropertiesWithinRadius(center, radius);
        if (nearbyProperties.isEmpty()) return 0;

        double totalPricePerSquareMeter = 0;
        for (Property property : nearbyProperties) {
            totalPricePerSquareMeter += property.getPrice() / property.getSizeInSquareMeters();
        }

        return totalPricePerSquareMeter / nearbyProperties.size();
    }
    public List<Property> getSoldPropertiesWithinRadius(Address center, double radius) {
        List<Property> nearbyProperties = getPropertiesWithinRadius(center, radius);
        List<Property> soldProperties = new ArrayList<>();

        for (Property property : nearbyProperties) {
            if (property.isSold()) {
                soldProperties.add(property);
            }
        }

        return soldProperties;
    }
    public List<Property> getAvailablePropertiesWithinRadius(Address center, double radius) {
        List<Property> nearbyProperties = getPropertiesWithinRadius(center, radius);
        List<Property> availableProperties = new ArrayList<>();

        for (Property property : nearbyProperties) {
            if (!property.isSold()) {
                availableProperties.add(property);
            }
        }

        return availableProperties;
    }
    public List<Property> getPropertiesByPriceComparison(Address center, double radius, double targetPricePerSquareMeter, String comparison) {
        List<Property> nearbyProperties = getPropertiesWithinRadius(center, radius);
        List<Property> matchedProperties = new ArrayList<>();

        for (Property property : nearbyProperties) {
            double pricePerSquareMeter = property.getPrice() / property.getSizeInSquareMeters();

            switch (comparison.toLowerCase()) {
                case "higher":
                    if (pricePerSquareMeter > targetPricePerSquareMeter) matchedProperties.add(property);
                    break;
                case "lower":
                    if (pricePerSquareMeter < targetPricePerSquareMeter) matchedProperties.add(property);
                    break;
                case "equal":
                    if (pricePerSquareMeter == targetPricePerSquareMeter) matchedProperties.add(property);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid comparison type. Use 'higher', 'lower', or 'equal'.");
            }
        }

        return matchedProperties;
    }

    public void savePropertiesToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Property property : properties) {
                writer.write(property.toFileString());
                writer.newLine();
            }
            System.out.println("Properties saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving properties: " + e.getMessage());
        }
    }

    public void loadPropertiesFromFile(String filename) {
        properties.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Property property = Property.fromFileString(line);
                if (property != null) {
                    properties.add(property);
                }
            }
            System.out.println("Properties loaded from " + filename);
        } catch (IOException e) {
            System.err.println("Error loading properties: " + e.getMessage());
        }
    }
}