package ModelClasses;

public class PropertyData {

    String SellerName , AddressProperty , NumberToContact , Email , SellerId ;
    String PropertyType;    // like house shop single room.
    String NumRooms ;
    String Cost; // rent or cost of house.
    String NumBHk; // 1BHK  , 2BHK etc.
    String Background ; // with garden or without garden
    String Area ; // on road or local area , village
    String GenderSeller;
    String NumImages;
    String PurchaseType ; // for rent or for purchase.
    String DateUpload;


    public PropertyData (
                        String sellerName, String addressProperty,
                        String numberToContact, String email, String sellerId,
                        String propertyType, String numRooms, String cost,
                        String numBHk, String background, String area, String genderSeller,
                        String numImages, String purchaseType , String dateUpload
                        )
    {
        SellerName = sellerName;
        AddressProperty = addressProperty;
        NumberToContact = numberToContact;
        Email = email;
        SellerId = sellerId;
        PropertyType = propertyType;
        NumRooms = numRooms;
        Cost = cost;
        NumBHk = numBHk;
        Background = background;
        Area = area;
        GenderSeller = genderSeller;
        NumImages = numImages;
        PurchaseType = purchaseType;
        DateUpload = dateUpload;
    }

    public PropertyData() {

    }

    public String getDateUpload() {
        return DateUpload;
    }

    public void setDateUpload(String dateUpload) {
        DateUpload = dateUpload;
    }

    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }

    public String getAddressProperty() {
        return AddressProperty;
    }

    public void setAddressProperty(String addressProperty) {
        AddressProperty = addressProperty;
    }

    public String getNumberToContact() {
        return NumberToContact;
    }

    public void setNumberToContact(String numberToContact) {
        NumberToContact = numberToContact;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSellerId() {
        return SellerId;
    }

    public void setSellerId(String sellerId) {
        SellerId = sellerId;
    }

    public String getPropertyType() {
        return PropertyType;
    }

    public void setPropertyType(String propertyType) {
        PropertyType = propertyType;
    }

    public String getNumRooms() {
        return NumRooms;
    }

    public void setNumRooms(String numRooms) {
        NumRooms = numRooms;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public String getNumBHk() {
        return NumBHk;
    }

    public void setNumBHk(String numBHk) {
        NumBHk = numBHk;
    }

    public String getBackground() {
        return Background;
    }

    public void setBackground(String background) {
        Background = background;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getGenderSeller() {
        return GenderSeller;
    }

    public void setGenderSeller(String genderSeller) {
        GenderSeller = genderSeller;
    }

    public String getNumImages() {
        return NumImages;
    }

    public void setNumImages(String numImages) {
        NumImages = numImages;
    }

    public String getPurchaseType() {
        return PurchaseType;
    }

    public void setPurchaseType(String purchaseType) {
        PurchaseType = purchaseType;
    }
}
