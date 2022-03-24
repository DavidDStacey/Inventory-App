package app.model;

// if part is outsourced works with part class
public class outsourcedPart extends part
{
    private String companyName;
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
