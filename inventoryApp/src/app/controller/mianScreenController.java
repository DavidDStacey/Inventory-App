package app.controller;

// lots of imports
import app.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import static app.model.inventory.*;

public class mianScreenController implements Initializable
{
    @FXML private TableView<part> viewParts;
    @FXML private TableColumn<part, Integer> viewPartsIDColumn;
    @FXML private TableColumn<part, String> viewPartsNameColumn;
    @FXML private TableColumn<part, Integer> viewPartsInvColumn;
    @FXML private TableColumn<part, Double> viewPartsPriceColumn;
    @FXML private TableView<product> viewProducts;
    @FXML private TableColumn<product, Integer> viewProductsIDColumn;
    @FXML private TableColumn<product, String> viewProductsNameColumn;
    @FXML private TableColumn<product, Integer> viewProductsInvColumn;
    @FXML private TableColumn<product, Double> viewProductsPriceColumn;
    @FXML private TextField txtSearchParts;
    @FXML private TextField txtSearchProducts;

    private static part modifyPart;
    private static int modifyPartIndex;
    private static product modifyProduct;
    private static int modifyProductIndex;

    public static int partToModifyIndex()
    {
        return modifyPartIndex;
    }

    public static int productToModifyIndex()
    {
        return modifyProductIndex;
    }

    //exits the page
    @FXML public void exitButton(ActionEvent actionEvent)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            System.exit(0);
        }
        else
        {
            System.out.println("You clicked cancel.");
        }
    }

// -------------------------- PARTS SECTION ------------------------------------

    @FXML public void partsSearch(ActionEvent actionEvent)
    {
        String searchPart = txtSearchParts.getText();
        int partIndex = -1;
        if (inventory.lookupPart(searchPart) == -1)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part Not Found");
            alert.setContentText("Can not find search term");
            alert.showAndWait();
        }
        else
        {
            partIndex = inventory.lookupPart(searchPart);
            part p = inventory.getPartInventory().get(partIndex);
            ObservableList<part> pList = FXCollections.observableArrayList();
            pList.add(p);
            viewParts.setItems(pList);
        }
    }

    @FXML public void openAddPartScreen(ActionEvent event) throws IOException
    {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/addPart.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML public void openModifyPartScreen(ActionEvent event) throws IOException
    {
        modifyPart = viewParts.getSelectionModel().getSelectedItem();
        modifyPartIndex = getPartInventory().indexOf(modifyPart);
        Parent parent = FXMLLoader.load(getClass().getResource("../view/modifyPart.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML public void partsDelete(ActionEvent actionEvent)
    {
        part p = viewParts.getSelectionModel().getSelectedItem();
        deletePart(p);
        updatePartsTableView();
    }

    private void updatePartsTableView()
    {
        viewParts.setItems(getPartInventory());
    }

    // -------------------------- END PARTS SECTION ------------------------------------

    // -------------------------- PRODUCT SECTION ------------------------------------

    @FXML public void productsSearch(ActionEvent actionEvent)
    {
        String searchProduct = txtSearchProducts.getText();
        int prodIndex = -1;
        if (inventory.lookupProduct(searchProduct) == -1)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Product not found");
            alert.setContentText("No matches found for search.");
            alert.showAndWait();
        }
        else
        {
            prodIndex = inventory.lookupProduct(searchProduct);
            product p = inventory.getProductInventory().get(prodIndex);
            ObservableList<product> pList = FXCollections.observableArrayList();
            pList.add(p);
            viewProducts.setItems(pList);
        }
    }

    @FXML public void openAddProductScreen(ActionEvent event) throws IOException
    {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/addProduct.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML public void openModifyProductScreen(ActionEvent event) throws IOException
    {
        modifyProduct = viewProducts.getSelectionModel().getSelectedItem();
        modifyProductIndex = getProductInventory().indexOf(modifyProduct);
        Parent parent = FXMLLoader.load(getClass().getResource("../view/modifyProduct.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML public void productsDelete(ActionEvent actionEvent)
    {
        product p = viewProducts.getSelectionModel().getSelectedItem();
        if (validateProductDelete(p))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Product cannot be deleted!");
            alert.setContentText("Product contains parts.");
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Product Deletion");
            alert.setHeaderText("Confirm Delete?");
            alert.setContentText("Are you sure you want to delete " + p.getProductName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
            {
                removeProduct(p);
                updateProductsTableView();
            }
        }
    }

    private void updateProductsTableView()
    {
        viewProducts.setItems(getProductInventory());
    }

    // -------------------------- END PRODUCT SECTION ------------------------------------

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        viewPartsIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        viewPartsNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        viewPartsInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        viewPartsPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        viewProductsIDColumn.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        viewProductsNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        viewProductsInvColumn.setCellValueFactory(cellData -> cellData.getValue().productInvProperty().asObject());
        viewProductsPriceColumn.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty().asObject());
        updatePartsTableView();
        updateProductsTableView();
    }
}
