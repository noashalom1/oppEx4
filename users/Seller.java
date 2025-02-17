package users;
import models.*;
import management.PropertyManager;

// Observer Pattern - Seller notifies Broker on property deletion
public class Seller extends User {
    private Broker broker;

    public Seller(String name, Broker broker) {
        super(name);
        this.broker = broker;
    }

    public void deleteProperty(Property property) {
        PropertyManager.getInstance().removeProperty(property);
        System.out.println(name + " deleted property at " + property.getAddress());
        notifyBroker(property);
    }

    private void notifyBroker(Property property) {
        System.out.println("Notification to " + broker.name + ": Property at " + property.getAddress() + " has been deleted by " + name);
    }
}