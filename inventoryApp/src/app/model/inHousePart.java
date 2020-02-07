package app.model;

// if part is in house the part class works with this to make the object
public class inHousePart extends part
{
    private int machineID;
    public int getMachineID() { return machineID; }
    public void setMachineID(int machineID) { this.machineID = machineID; }
}
