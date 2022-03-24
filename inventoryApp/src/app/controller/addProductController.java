package app.controller;

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

public class addProductController implements Initializable
{
    @FXML private TextField txtAddProductMax;
    @FXML private TextField txtAddProductMin;
    @FXML private TextField txtAddProductPrice;
    @FXML private TextField txtAddProductInv;
    @FXML private TextField txtAddProductName;
    @FXML private Label lblAddProductIDNumber;
    @FXML private TextField txtAddProductSearch;
    @FXML private TableView<part> viewAddProductAdd;
    @FXML private TableColumn<part, Integer> viewAddProductAddIDColumn;
    @FXML private TableColumn<part, String> viewAddProductAddNameColumn;
    @FXML private TableColumn<part, Integer> viewAddProductAddInvColumn;
    @FXML private TableColumn<part, Double> viewAddProductAddPriceColumn;
    @FXML private TableView<part> viewAddProductDelete;
    @FXML private TableColumn<part, Integer> viewAddProductDeleteIDColumn;
    @FXML private TableColumn<part, String> viewAddProductDeleteNameColumn;
    @FXML private TableColumn<part, Integer> viewAddProductDeleteInvColumn;
    @FXML private TableColumn<part, Double> viewAddProductDeletePriceColumn;

    private ObservableList<part> currentParts = FXCollections.observableArrayList();
    private String exceptionMessage = "";
    private int productID;

    public void search(ActionEvent actionEvent)
    {
        String searchPart = txtAddProductSearch.getText();
        int partIndex = -1;
        if (inventory.lookupPart(searchPart) == -1)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("The term entered does not match any parts.");
            alert.showAndWait();
        }
        else
        {
            partIndex = inventory.lookupPart(searchPart);
            part p = getPartInventory().get(partIndex);
            ObservableList<part> pList = FXCollections.observableArrayList();
            pList.add(p);
            viewAddProductAdd.setItems(pList);

        }

    }

    public void add(ActionEvent actionEvent)
    {
        part p = viewAddProductAdd.getSelectionModel().getSelectedItem();
        currentParts.add(p);
        updateDeletePartTableView();
    }

    public void delete(ActionEvent actionEvent)
    {
        part p = viewAddProductDelete.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Part Deletion");
        alert.setHeaderText("Confirm");
        alert.setContentText("Are you sure you want to delete " + p.getPartName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            currentParts.remove(p);
        }
    }

    public void addProductCancel(ActionEvent event) throws IOException
    {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/mainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void addProductSave(ActionEvent event) throws IOException
    {
        String productName = txtAddProductName.getText();
        String productInv = txtAddProductInv.getText();
        String productPrice = txtAddProductPrice.getText();
        String productMin = txtAddProductMin.getText();
        String productMax = txtAddProductMax.getText();

        try
        {
            exceptionMessage = product.isProductValid(productName, Integer.parseInt(productMin), Integer.parseInt(productMax), Integer.parseInt(productInv), Double.parseDouble(productPrice), currentParts, exceptionMessage);
            if (exceptionMessage.length() > 0)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Product");
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
                inventory.addProduct(newProduct);

                Parent addProductSaveParent = FXMLLoader.load(getClass().getResource("../view/mainScreen.fxml"));
                Scene scene = new Scene(addProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Adding Product");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }

    public void updateAddPartTableView()
    {
        viewAddProductAdd.setItems(getPartInventory());
    }

    public void updateDeletePartTableView()
    {
        viewAddProductDelete.setItems(currentParts);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        viewAddProductAddIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        viewAddProductAddNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        viewAddProductAddInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        viewAddProductAddPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        viewAddProductDeleteIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        viewAddProductDeleteNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        viewAddProductDeleteInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        viewAddProductDeletePriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        updateAddPartTableView();
        updateDeletePartTableView();
        productID = inventory.getProductIDCount();
        lblAddProductIDNumber.setText("Auto-Gen: " + productID);
    }
}
