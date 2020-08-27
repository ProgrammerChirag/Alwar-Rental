package ModelClasses;

public class RequestData {

    String name , reason , numRoom , location , budget , profesion , married_status , gender , requestType;
    String status;
    String cost;
    String sellerID;
    String userID;

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(String dateUpload) {
        this.dateUpload = dateUpload;
    }

    String dateUpload;

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public RequestData(String name, String reason, String numRoom, String location, String budget, String profesion, String married_status, String gender , String requestType , String status , String cost , String  dateUpload , String sellerID , String userID) {


        this.name = name;
        this.reason = reason;
        this.numRoom = numRoom;
        this.location = location;
        this.budget = budget;
        this.profesion = profesion;
        this.married_status = married_status;
        this.gender = gender;
        this.requestType = requestType;
        this.status = status;
        this.dateUpload = dateUpload;;
        this.cost = cost;
        this.sellerID = sellerID;
        this.userID = userID;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RequestData(){}

    public String getRequestType(){return requestType;}
    public void setRequestType(String  requestType){
        this.requestType = requestType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNumRoom() {
        return numRoom;
    }

    public void setNumRoom(String numRoom) {
        this.numRoom = numRoom;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getMarried_status() {
        return married_status;
    }

    public void setMarried_status(String married_status) {
        this.married_status = married_status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
