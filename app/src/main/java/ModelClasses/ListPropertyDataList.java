package ModelClasses;

import java.util.List;

public class ListPropertyDataList {

    List<PropertyData> propertyDataList;

    public ListPropertyDataList(List<PropertyData> propertyDataList) {
        this.propertyDataList = propertyDataList;
    }

    public List<PropertyData> getPropertyDataList() {
        return propertyDataList;
    }

    public void setPropertyDataList(List<PropertyData> propertyDataList) {
        this.propertyDataList = propertyDataList;
    }
    public ListPropertyDataList()
    { }
}
