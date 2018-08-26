package data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Data_OwnerTable {
    /*业主信息    ONo业主编号    OName业主姓名    OSex业主性别    OTel业主电话    OID业主身份证号码    ONote业主备注*/
    private SimpleStringProperty ONo,OName,OSex,OTel,OID,ONote;
    private SimpleStringProperty HouseCount,ParkingCount;
    public Data_OwnerTable(String ONo,String OName,String OSex,String OTel,String OID,String ONote,String HouseCount,String ParkingCount){
        this.ONo = new SimpleStringProperty(ONo);
        this.OName = new SimpleStringProperty(OName);
        this.OSex = new SimpleStringProperty(OSex);
        this.OTel = new SimpleStringProperty(OTel);
        this.OID = new SimpleStringProperty(OID);
        this.ONote = new SimpleStringProperty(ONote);
        this.HouseCount = new SimpleStringProperty(HouseCount);
        this.ParkingCount = new SimpleStringProperty(ParkingCount);
    }
    public StringProperty getONo(){
        return ONo;
    }
    public StringProperty getOName(){
        return OName;
    }
    public StringProperty getOSex(){
        return OSex;
    }
    public StringProperty getOTel(){
        return OTel;
    }
    public StringProperty getOID(){
        return OID;
    }
    public StringProperty getONote(){
        return ONote;
    }
    public StringProperty getHouseCount(){
        return HouseCount;
    }
    public StringProperty getParkingCount(){
        return ParkingCount;
    }
    public void setONo(String ONo){
        this.ONo.set(ONo);
    }
    public void setOName(String OName){
        this.OName.set(OName);
    }
    public void setOSex(String OSex){
        this.OSex.set(OSex);
    }
    public void setOTel(String OTel){
        this.OTel.set(OTel);
    }
    public void setOID(String OID){
        this.OID.set(OID);
    }
    public void setONote(String ONote){
        this.ONote.set(ONote);
    }
    public void setHouseCount(String HouseCount){
        this.HouseCount.set(HouseCount);
    }
    public void setParkingCount(String ParkingCount){
        this.ParkingCount.set(ParkingCount);
    }
}