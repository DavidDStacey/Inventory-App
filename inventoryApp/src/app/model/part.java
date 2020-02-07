package app.model;

import javafx.beans.property.*;

//part class for part object works with inHouse or outsourced classes
public class part
{
    private final IntegerProperty partID;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty inv;
    private final IntegerProperty min;
    private final IntegerProperty max;

    // constructor
    public part()
    {
        partID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        inv = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }

    // getters
    public IntegerProperty partIDProperty() { return partID; }
    public StringProperty partNameProperty() {
        return name;
    }
    public DoubleProperty partPriceProperty() {
        return price;
    }
    public IntegerProperty partInvProperty() {
        return inv;
    }
    public IntegerProperty partMinProperty() {
        return min;
    }
    public IntegerProperty partMaxProperty() {
        return max;
    }
    public int getPartID() {
        return this.partID.get();
    }
    public String getPartName() {
        return this.name.get();
    }
    public double getPartCost() {
        return this.price.get();
    }
    public int getPartInv() {
        return this.inv.get();
    }
    public int getPartMin() {
        return this.min.get();
    }
    public int getPartMax() {
        return this.max.get();
    }

    // setters
    public void setPartID(int partID) {
        this.partID.set(partID);
    }
    public void setPartName(String name) {
        this.name.set(name);
    }
    public void setPartCost(double price) {
        this.price.set(price);
    }
    public void setPartInv(int inv) {
        this.inv.set(inv);
    }
    public void setPartMin(int min) {
        this.min.set(min);
    }
    public void setPartMax(int max) {
        this.max.set(max);
    }

    // sees if part makes sense
    public static String isPartValid(String name, int min, int max, int inv, double price, String errorMessage)
    {
        if (name == null)
        {
            errorMessage = errorMessage + "The name field is required. ";
        }
        if (inv < 1)
        {
            errorMessage = errorMessage + "The inventory count cannot be less than 1. ";
        }
        if (price <= 0)
        {
            errorMessage = errorMessage + "The price must be greater than $0. ";
        }
        if (max < min)
        {
            errorMessage = errorMessage + "The Max must be greater than or equal to the Min. ";
        }
        if (inv < min || inv > max)
        {
            errorMessage = errorMessage + "The inventory must be between the Min and Max values. ";
        }
        return errorMessage;
    }
}
