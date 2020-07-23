package ModelClasses;

public class UserData {
    String name , username , email , number , password , address , number_of_requests , number_of_purchase;

    public UserData(String name, String username, String email, String number, String password, String address, String number_of_requests, String number_of_purchase) {

        this.name = name;
        this.username = username;
        this.email = email;
        this.number = number;
        this.password = password;
        this.address = address;
        this.number_of_requests = number_of_requests;
        this.number_of_purchase = number_of_purchase;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber_of_requests() {
        return number_of_requests;
    }

    public void setNumber_of_requests(String number_of_requests) {
        this.number_of_requests = number_of_requests;
    }

    public String getNumber_of_purchase() {
        return number_of_purchase;
    }

    public void setNumber_of_purchase(String number_of_purchase) {
        this.number_of_purchase = number_of_purchase;
    }

    public UserData(){}


}
