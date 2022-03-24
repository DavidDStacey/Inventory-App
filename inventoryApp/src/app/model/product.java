package app.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class product
{
    private static ObservableList<part> parts = FXCollections.observableArrayList();
    private final IntegerProperty productID;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty stock;
    private final IntegerProperty min;
    private final IntegerProperty max;

    //constructor
    public product()
    {
        productID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        stock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }

    //getters
    public IntegerProperty productIDProperty() { return productID; }
    public StringProperty productNameProperty() { return name; }
    public DoubleProperty productPriceProperty() { return price; }
    public IntegerProperty productInvProperty() { return stock; }
    public IntegerProperty productMinProperty() { return min; }
    public IntegerProperty productMaxProperty() { return max; }
    public int getProductID() { return this.productID.get(); }
    public String getProductName() { return this.name.get(); }
    public double getProductPrice() { return this.price.get(); }
    public int getProductInStock() { return this.stock.get(); }
    public int getProductMin() { return this.min.get(); }
    public int getProductMax() { return this.max.get(); }
    public ObservableList getProductParts() { return parts; }


    // setters
    public void setProductID(int productID) { this.productID.set(productID); }
    public void setProductName(String name) { this.name.set(name); }
    public void setProductPrice(double price) { this.price.set(price); }
    public void setProductInStock(int inStock) { this.stock.set(inStock); }
    public void setProductMin(int min) { this.min.set(min); }
    public void setProductMax(int max) { this.max.set(max); }
    public void setProductParts(ObservableList<part> parts) { this.parts = parts; }


    // checks product to see if it makes sense
    public static String isProductValid(String name, int min, int max, int inv, double price, ObservableList<part> parts, String eMessage)
    {
        double sumOfParts = 0.00;
        for (int i = 0; i < parts.size(); i++)
        {
            sumOfParts = sumOfParts + parts.get(i).getPartCost();
        }
        if (name == null)
        {
            eMessage = eMessage + "Name is required. ";
        }
        if (inv < 1)
        {
            eMessage = eMessage + "Can not have less than 1. ";
        }
        if (price <= 0)
        {
            eMessage = eMessage + "Can not be free. ";
        }
        if (max < min)
        {
            eMessage = eMessage + "The Max must be greater than or equal to the Min. ";
        }
        if (inv < min || inv > max)
        {
            eMessage = eMessage + "The inventory must be between the Min and Max values. ";
        }
        if (sumOfParts > price)
        {
            eMessage = eMessage + "Price of product must be more than all the parts together. ";
        }
        return eMessage;
    }
}
