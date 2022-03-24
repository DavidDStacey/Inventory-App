package app.controller;

// lots of imports
import app.model.inventory;
import app.model.part;
import app.model.product;
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
import static app.model.inventory.getPartInventory;
import static app.model.inventory.getProductInventory;
import static app.controller.mianScreenController.productToModifyIndex;

// class to edit a product
public class modifyProductController implements Initializable
{
    // all the fxml so it can communicate with the fxml file
    @FXML private TextField txtModifyProductMax;
    @FXML private TextField txtModifyProductMin;
    @FXML private TextField txtModifyProductPrice;
    @FXML private TextField txtModifyProductInv;
    @FXML private TextField txtModifyProductName;
    @FXML private Label lblModifyProductIDNumber;
    @FXML private TextField txtModifyProductSearch;
    @FXML private TableView<part> viewModifyProductAdd;
    @FXML private TableColumn<part, Integer> viewModifyProductAddIDColumn;
    @FXML private TableColumn<part, String> viewModifyProductAddNameColumn;
    @FXML private TableColumn<part, Integer> viewModifyProductAddInvColumn;
    @FXML private TableColumn<part, Double> viewModifyProductAddPriceColumn;
    @FXML private TableView<part> viewModifyProductDelete;
    @FXML private TableColumn<part, Integer> viewModifyProductDeleteIDColumn;
    @FXML private TableColumn<part, String> viewModifyProductDeleteNameColumn;
    @FXML private TableColumn<part, Integer> viewModifyProductDeleteInvColumn;
    @FXML private TableColumn<part, Double> viewModifyProductDeletePriceColumn;

    private ObservableList<part> currentParts = FXCollections.observableArrayList();
    private int productIndex = productToModifyIndex();
    private String exceptionMessage = new String();
    private int productID;

    // method to search
    public void search(ActionEvent actionEvent)
    {
        String searchPart = txtModifyProductSearch.getText();
        int partIndex = -1;
        //cant find it
        if (inventory.lookupPart(searchPart) == -1)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("The search term matches no parts.");
            alert.showAndWait();
        }
        // found it
        else
        {
            partIndex = inventory.lookupPart(searchPart);
            part p = getPartInventory().get(partIndex);
            ObservableList<part> pList = FXCollections.observableArrayList();
            pList.add(p);
            viewModifyProductAdd.setItems(pList);
        }
    }

    //method to add part to product
    public void add(ActionEvent actionEvent)
    {
        part p = viewModifyProductAdd.getSelectionModel().getSelectedItem();
        currentParts.add(p);
        updateDeletePartsTableView();
    }

    //method to delete
    public void delete(ActionEvent actionEvent)
    {
        part p = viewModifyProductDelete.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Part Deletion");
        alert.setHeaderText("Are you sure about that");
        alert.setContentText("Are you sure you want to delete " + p.getPartName() + " from parts?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            currentParts.remove(p);
        }
    }

    // chagned your mind
    public void modifyProductCancel(ActionEvent event) throws IOException
    {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/mainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    // make change permanent
    public void modifyProductSave(ActionEvent event) throws IOException
    {
        String productName = txtModifyProductName.getText();
        String productInv = txtModifyProductInv.getText();
        String productPrice = txtModifyProductPrice.getText();
        String productMin = txtModifyProductMin.getText();
        String productMax = txtModifyProductMax.getText();
        try
        {
            exceptionMessage = product.isProductValid(productName, Integer.parseInt(productMin), Integer.parseInt(productMax), Integer.parseInt(productInv), Double.parseDouble(productPrice), currentParts, exceptionMessage);
            if (exceptionMessage.length() > 0)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Product");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            }
            else
            {
                System.out.println("Product name: " + productName);
                product newProduct = new product();
                newProduct.setProductID(productID);
                newProduct.setProductName(productName);
                newProduct.setProductInStock(Integer.parseInt(productInv));
                newProduct.setProductPrice(Double.parseDouble(productPrice));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductMax(Integer.parseInt(productMax));
                newProduct.setProductParts(currentParts);
                inventory.updateProduct(productIndex, newProduct);

                Parent modifyProductSaveParent = FXMLLoader.load(getClass().getResource("../view/mainScreen.fxml"));
                Scene scene = new Scene(modifyProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Modifying Product");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }

    public void updateAddPartsTableView()
    {
        viewModifyProductAdd.setItems(getPartInventory());
    }

    public void updateDeletePartsTableView()
    {
        viewModifyProductDelete.setItems(currentParts);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        product product = getProductInventory().get(productIndex);
        productID = getProductInventory().get(productIndex).getProductID();
        lblModifyProductIDNumber.setText("Auto-Gen: " + productID);
        txtModifyProductName.setText(product.getProductName());
        txtModifyProductInv.setText(Integer.toString(product.getProductInStock()));
        txtModifyProductPrice.setText(Double.toString(product.getProductPrice()));
        txtModifyProductMin.setText(Integer.toString(product.getProductMin()));
        txtModifyProductMax.setText(Integer.toString(product.getProductMax()));
        currentParts = product.getProductParts();
        viewModifyProductAddIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        viewModifyProductAddNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        viewModifyProductAddInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        viewModifyProductAddPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        viewModifyProductDeleteIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        viewModifyProductDeleteNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        viewModifyProductDeleteInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        viewModifyProductDeletePriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        updateAddPartsTableView();
        updateDeletePartsTableView();
    }
}
