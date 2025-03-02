package users;

import models.*;
import management.PropertyManager;

import java.util.List;

public class Buyer extends User {
    private int budget;
    private List<Property> properties;

    public Buyer(String name) {
        super(name);

    }

    public int getFunds() {
        return budget;
    }

    public void setFunds(int newBudget) {
        budget = newBudget;
    }
}