package ModelClasses;

public class SellerData {

    String Name , Address , number , username , password , email , numberOfPost  , NumOfCustomer ;

    public SellerData(){}


    public SellerData(String name, String address, String number, String username, String password, String email, String numberOfPost, String numOfCustomer) {
        Name = name;
        Address = address;
        this.number = number;
        this.username = username;
        this.password = password;
        this.email = email;
        this.numberOfPost = numberOfPost;
        NumOfCustomer = numOfCustomer;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumberOfPost() {
        return numberOfPost;
    }

    public void setNumberOfPost(String numberOfPost) {
        this.numberOfPost = numberOfPost;
    }

    public String getNumOfCustomer() {
        return NumOfCustomer;
    }

    public void setNumOfCustomer(String numOfCustomer) {
        NumOfCustomer = numOfCustomer;
    }
}
