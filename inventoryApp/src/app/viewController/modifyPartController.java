package app.viewController;

import app.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static app.model.inventory.getPartInventory;
import static app.viewController.mianScreenController.partToModifyIndex;

public class modifyPartController implements Initializable
{
    @FXML private TextField txtModifyPartDyn;
    @FXML private Label lblModifyPartDyn;
    @FXML private TextField txtModifyPartMax;
    @FXML private TextField txtModifyPartMin;
    @FXML private TextField txtModifyPartPrice;
    @FXML private TextField txtModifyPartInv;
    @FXML private TextField txtModifyPartName;
    @FXML private Label lblModifyPartIDNumber;
    @FXML private RadioButton radioModifyPartOutsourced;
    @FXML private RadioButton radioModifyPartInHouse;

    private boolean Outsourced;
    int partIndex = partToModifyIndex();
    private String exceptionMessage = new String();
    private int partID;

    //Modify the part to inhouse
    public void modifyPartInHouseRadio(ActionEvent actionEvent)
    {
        Outsourced = false;
        radioModifyPartOutsourced.setSelected(false);
        lblModifyPartDyn.setText("Machine ID");
        txtModifyPartDyn.setText("");
        txtModifyPartDyn.setPromptText("Machine ID");
    }

    // Modifys part as outsourced
    public void modifyPartOutsourcedRadio(ActionEvent actionEvent)
    {
        Outsourced = true;
        radioModifyPartInHouse.setSelected(false);
        lblModifyPartDyn.setText("Company Name");
        txtModifyPartDyn.setText("");
        txtModifyPartDyn.setPromptText("Company Name");
    }

    public void modifyPartSave(ActionEvent event) throws IOException
    {
        String pName = txtModifyPartName.getText();
        String pInv = txtModifyPartInv.getText();
        String pPrice = txtModifyPartPrice.getText();
        String pMin = txtModifyPartMin.getText();
        String pMax = txtModifyPartMax.getText();
        String pDyn = txtModifyPartDyn.getText();

        try
        {
            exceptionMessage = part.isPartValid(pName, Integer.parseInt(pMin), Integer.parseInt(pMax), Integer.parseInt(pInv), Double.parseDouble(pPrice), exceptionMessage);
            if (exceptionMessage.length() > 0)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Part");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            }
            else
            {
                if (Outsourced == false)
                {
                    System.out.println("Part name: " + pName);
                    inHousePart inPart = new inHousePart();
                    inPart.setPartID(partID);
                    inPart.setPartName(pName);
                    inPart.setPartInv(Integer.parseInt(pInv));
                    inPart.setPartCost(Double.parseDouble(pPrice));
                    inPart.setPartMin(Integer.parseInt(pMin));
                    inPart.setPartMax(Integer.parseInt(pMax));
                    inPart.setMachineID(Integer.parseInt(pDyn));
                    inventory.updatePart(partIndex, inPart);
                }
                else
                {
                    System.out.println("Part name: " + pName);
                    outsourcedPart outPart = new outsourcedPart();
                    outPart.setPartID(partID);
                    outPart.setPartName(pName);
                    outPart.setPartInv(Integer.parseInt(pInv));
                    outPart.setPartCost(Double.parseDouble(pPrice));
                    outPart.setPartMin(Integer.parseInt(pMin));
                    outPart.setPartMax(Integer.parseInt(pMax));
                    outPart.setCompanyName(pDyn);
                    inventory.updatePart(partIndex, outPart);
                }
                Parent modifyProductSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(modifyProductSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Modifying Part");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }

    public void modifyPartCancel(ActionEvent event) throws IOException
    {
        Parent parent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        part p = getPartInventory().get(partIndex);
        partID = getPartInventory().get(partIndex).getPartID();
        lblModifyPartIDNumber.setText("Auto-Gen: " + partID);
        txtModifyPartName.setText(p.getPartName());
        txtModifyPartInv.setText(Integer.toString(p.getPartInv()));
        txtModifyPartPrice.setText(Double.toString(p.getPartCost()));
        txtModifyPartMin.setText(Integer.toString(p.getPartMin()));
        txtModifyPartMax.setText(Integer.toString(p.getPartMax()));
        if (p instanceof inHousePart)
        {
            lblModifyPartDyn.setText("Machine ID");
            txtModifyPartDyn.setText(Integer.toString(((inHousePart) getPartInventory().get(partIndex)).getMachineID()));
            radioModifyPartInHouse.setSelected(true);
            radioModifyPartOutsourced.setSelected(false);
        }
        else
        {
            lblModifyPartDyn.setText("Company Name");
            txtModifyPartDyn.setText(((outsourcedPart) getPartInventory().get(partIndex)).getCompanyName());
            radioModifyPartOutsourced.setSelected(true);
            radioModifyPartInHouse.setSelected(false);
        }
    }
}
