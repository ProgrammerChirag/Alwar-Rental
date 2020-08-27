package Admin.ModelClass;

public class AdminCredentials {
    String AdminID;
    String AdminPass;

    public AdminCredentials(String adminID, String adminPass) {
        AdminID = adminID;
        AdminPass = adminPass;
    }
    public AdminCredentials()
    {}

    public String getAdminID() {
        return AdminID;
    }

    public void setAdminID(String adminID) {
        AdminID = adminID;
    }

    public String getAdminPass() {
        return AdminPass;
    }

    public void setAdminPass(String adminPass) {
        AdminPass = adminPass;
    }
}
