package app.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//class for inventory
public class inventory
{
    private static ObservableList<product> productInventory = FXCollections.observableArrayList();
    private static ObservableList<part> partInventory = FXCollections.observableArrayList();
    private static int partIDCount = 0;
    private static int productIDCount = 0;

    public static ObservableList<part> getPartInventory()
    {
        return partInventory;
    }

    public static void addPart(part p)
    {
        partInventory.add(p);
    }

    public static void deletePart(part p)
    {
        partInventory.remove(p);
    }

    public static void updatePart(int index, part p)
    {
        partInventory.set(index, p);
    }

    public static int getPartIDCount()
    {
        partIDCount++;
        return partIDCount;
    }

    public static boolean validatePartDelete(part p)
    {
        boolean found = false;
        for (int i=0; i<productInventory.size(); i++)
        {
            if (productInventory.get(i).getProductParts().contains(p))
            {
                found = true;
            }
        }
        return found;
    }

    public static int lookupPart(String searchTerm)
    {
        boolean found = false;
        int index = 0;
        if (isInteger(searchTerm))
        {
            for (int i = 0; i < partInventory.size(); i++)
            {
                if (Integer.parseInt(searchTerm) == partInventory.get(i).getPartID())
                {
                    index = i;
                    found = true;
                }
            }
        }
        else
        {
            for (int i = 0; i < partInventory.size(); i++)
            {
                searchTerm = searchTerm.toLowerCase();
                if (searchTerm.equals(partInventory.get(i).getPartName().toLowerCase()))
                {
                    index = i;
                    found = true;
                }
            }
        }
        if (found)
        {
            return index;
        }
        else
        {
            return -1;
        }
    }

    public static ObservableList<product> getProductInventory()
    {
        return productInventory;
    }

    public static void addProduct(product p)
    {
        productInventory.add(p);
    }

    public static void removeProduct(product p)
    {
        productInventory.remove(p);
    }

    public static int getProductIDCount()
    {
        productIDCount++;
        return productIDCount;
    }

    public static int lookupProduct(String searchTerm)
    {
        boolean found = false;
        int index = 0;
        if (isInteger(searchTerm))
        {
            for (int i = 0; i < productInventory.size(); i++)
            {
                if (Integer.parseInt(searchTerm) == productInventory.get(i).getProductID())
                {
                    index = i;
                    found = true;
                }
            }
        }
        else
        {
            for (int i = 0; i < productInventory.size(); i++)
            {
                if (searchTerm.equals(productInventory.get(i).getProductName()))
                {
                    index = i;
                    found = true;
                }
            }
        }

        if (found)
        {
            return index;
        }
        else
        {
            return -1;
        }
    }

    public static void updateProduct(int index, product p)
    {
        productInventory.set(index, p);
    }

    public static boolean validateProductDelete(product p)
    {
        boolean found = false;
        int productID = p.getProductID();
        for (int i=0; i < productInventory.size(); i++)
        {
            if (productInventory.get(i).getProductID() == productID)
            {
                if (!productInventory.get(i).getProductParts().isEmpty())
                {
                    found = true;
                }
            }
        }
        return found;
    }

    public static boolean isInteger(String input)
    {
        try
        {
            Integer.parseInt(input);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
