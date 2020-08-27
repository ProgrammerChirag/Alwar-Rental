package ModelClasses;

public class TransactionData {

    String Address ;
    String SellerName;
    String SellerPhoneNumber;
    String SellerID;
    String TransactionMedium;
    String PurchaseType;
    String AmountPaid;
    String Date ;

    public TransactionData()
    {}

    public TransactionData(String address, String sellerName, String sellerPhoneNumber, String sellerID, String transactionMedium, String purchaseType, String amountPaid, String date) {
        Address = address;
        SellerName = sellerName;
        SellerPhoneNumber = sellerPhoneNumber;
        SellerID = sellerID;
        TransactionMedium = transactionMedium;
        PurchaseType = purchaseType;
        AmountPaid = amountPaid;
        Date = date;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }

    public String getSellerPhoneNumber() {
        return SellerPhoneNumber;
    }

    public void setSellerPhoneNumber(String sellerPhoneNumber) {
        SellerPhoneNumber = sellerPhoneNumber;
    }

    public String getSellerID() {
        return SellerID;
    }

    public void setSellerID(String sellerID) {
        SellerID = sellerID;
    }

    public String getTransactionMedium() {
        return TransactionMedium;
    }

    public void setTransactionMedium(String transactionMedium) {
        TransactionMedium = transactionMedium;
    }

    public String getPurchaseType() {
        return PurchaseType;
    }

    public void setPurchaseType(String purchaseType) {
        PurchaseType = purchaseType;
    }

    public String getAmountPaid() {
        return AmountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        AmountPaid = amountPaid;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
