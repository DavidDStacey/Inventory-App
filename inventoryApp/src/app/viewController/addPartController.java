package app.viewController;

import app.model.inHousePart;
import app.model.inventory;
import app.model.outsourcedPart;
import app.model.part;
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

public class addPartController implements Initializable
{
    @FXML private TextField txtAddPartDyn;
    @FXML private Label lblAddPartDyn;
    @FXML private TextField txtAddPartMax;
    @FXML private TextField txtAddPartMin;
    @FXML private TextField txtAddPartPrice;
    @FXML private TextField txtAddPartInv;
    @FXML private TextField txtAddPartName;
    @FXML private Label lblAddPartIDNumber;
    @FXML private RadioButton radioAddPartOutsourced;
    @FXML private RadioButton radioAddPartInHouse;

    private boolean outsourced;
    private String eMessege = new String();
    private int partID;

    //adds the part to inhouse
    public void addPartInHouseRadio(ActionEvent actionEvent)
    {
        outsourced = false;
        radioAddPartOutsourced.setSelected(false);
        lblAddPartDyn.setText("Machine ID");
        txtAddPartDyn.setPromptText("Machine ID");
    }

    // adds part as outsourced
    public void addPartOutsourcedRadio(ActionEvent actionEvent)
    {
        outsourced = true;
        radioAddPartInHouse.setSelected(false);
        lblAddPartDyn.setText("Company Name");
        txtAddPartDyn.setPromptText("Company Name");
    }

    public void addPartSave(ActionEvent event) throws IOException
    {
        String partName = txtAddPartName.getText();
        String partInv = txtAddPartInv.getText();
        String partPrice = txtAddPartPrice.getText();
        String partMin = txtAddPartMin.getText();
        String partMax = txtAddPartMax.getText();
        String partDyn = txtAddPartDyn.getText();

        try
        {
            eMessege = part.isPartValid(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), eMessege);
            if (eMessege.length() > 0)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Part");
                alert.setContentText(eMessege);
                alert.showAndWait();
                eMessege = "";
            }
            else
            {
                if (!outsourced)
                {
                    System.out.println("Part name: " + partName);
                    inHousePart x = new inHousePart();
                    x.setPartID(partID);
                    x.setPartName(partName);
                    x.setPartCost(Double.parseDouble(partPrice));
                    x.setPartInv(Integer.parseInt(partInv));
                    x.setPartMin(Integer.parseInt(partMin));
                    x.setPartMax(Integer.parseInt(partMax));
                    x.setMachineID(Integer.parseInt(partDyn));
                    inventory.addPart(x);
                }
                else
                {
                    System.out.println("Part name: " + partName);
                    outsourcedPart x = new outsourcedPart();
                    x.setPartID(partID);
                    x.setPartName(partName);
                    x.setPartCost(Double.parseDouble(partPrice));
                    x.setPartInv(Integer.parseInt(partInv));
                    x.setPartMin(Integer.parseInt(partMin));
                    x.setPartMax(Integer.parseInt(partMax));
                    x.setCompanyName(partDyn);
                    inventory.addPart(x);
                }
                Parent p = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(p);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch(NumberFormatException e)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Adding Part");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }

    public void addPartCancel(ActionEvent event) throws IOException
    {
        Parent parent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        partID = inventory.getPartIDCount();
        lblAddPartIDNumber.setText("Auto-Gen: " + partID);
    }
}
