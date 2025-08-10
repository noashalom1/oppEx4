# 🏠 Real Estate Management System

## 📌 Overview
This project is an **Object-Oriented Java** application for managing real estate properties, brokers, sellers, and buyers.  
The system supports adding, editing, deleting, and comparing properties based on **location**, **size**, and **price per square meter**.  
It also includes dynamic filtering, observer notifications, and extra property services using the **Decorator Pattern**.

---

## ✨ Features
- **Property Management** (Add / Edit / Delete / Compare)
- **Duplicate Address Prevention** with `DuplicateAddressException`
- **Sub-unit Handling**:
  - Prevents adding both a general property and its sub-units at the same time.
  - Always stores the **most specific** property (general address OR sub-units, not both).
- **Dynamic Property Filtering** (by size, price, etc.)
- **Observer Notifications** to brokers when a seller deletes a property
- **Extra Property Services** (Cleaning, Moving, Insurance, Design)
- **Unified Property Display** with specific formatting per property type

---

## 🛠 Design Patterns Used
1. **Singleton** – `PropertyManager`
   - Ensures only one instance exists for property management.
2. **Factory Method** – `FilterFactory`
   - Creates `PropertyFilter` objects dynamically based on criteria.
3. **Observer** – `Seller` (Subject) & `Broker` (Observer)
   - Brokers are notified when a seller deletes a property.
4. **Decorator** – Additional Property Services (`SuretyService`, `MovingService`, `DesignService`, `CleaningService`)
   - Adds extra services without modifying the base `Property` class.
5. **Template Method** – `Property` (abstract) & subclasses (`Apartment`, etc.)
   - Defines a structure for displaying property details, customized in subclasses.

---

## 📐 SOLID Principles Applied
- **SRP (Single Responsibility Principle)**  
  Each class has a single responsibility:
  - `Broker` – handles transactions
  - `Seller` – handles property deletion
  - `PropertyManager` – manages properties
  - `FilterFactory` – creates filters
- **OCP (Open/Closed Principle)**  
  `PropertyFilter` interface allows adding new filters without modifying existing code.

---

## 📂 Project Structure
```
src/
 ├── managers/
 │    └── PropertyManager.java
 ├── models/
 │    ├── Property.java (abstract)
 │    ├── Apartment.java
 │    ├── Seller.java
 │    ├── Broker.java
 │    └── Buyer.java
 ├── filters/
 │    ├── PropertyFilter.java
 │    ├── SizeFilter.java
 │    ├── PriceFilter.java
 │    └── FilterFactory.java
 ├── services/
 │    ├── CleaningService.java
 │    ├── MovingService.java
 │    ├── DesignService.java
 │    └── SuretyService.java
 ├── exceptions/
 │    └── DuplicateAddressException.java
 └── Main.java
```

---

## 🚀 How to Run
1. **Clone the repository:**
   ```bash
   git clone <repo_url>
   ```
2. **Compile the project:**
   ```bash
   javac -d bin src/**/*.java
   ```
3. **Run the application:**
   ```bash
   java -cp bin Main
   ```

---

## 💻 Example Usage
```java
public class Main {
    public static void main(String[] args) {
        PropertyManager manager = PropertyManager.getInstance();

        // Create a property
        Property apartment = new Apartment("123 Main St", 80, 12000);

        // Add extra services using Decorator
        apartment = new CleaningService(apartment);
        apartment = new MovingService(apartment);

        // Add property to the manager
        try {
            manager.addProperty(apartment);
        } catch (DuplicateAddressException e) {
            System.out.println("Property already exists: " + e.getMessage());
        }

        // Apply filter (e.g., by size)
        PropertyFilter sizeFilter = FilterFactory.createFilter("size", 70);
        manager.getProperties().stream()
               .filter(sizeFilter::matches)
               .forEach(System.out::println);
    }
}
```

---

## 📄 Future Improvements
- Add `LocationFilter` for geographic filtering.
- Implement persistence using a database or file storage.
- Add a GUI interface (JavaFX).

---
