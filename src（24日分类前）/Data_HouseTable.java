import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Data_HouseTable {
    private SimpleStringProperty HNo,HBuild,HPark,HFloor,HRoom,HArea,HState,HType,HNote;
    private SimpleStringProperty ONo,OName,OSex,OTel,OID,ONote;
    public Data_HouseTable(String HNo,String HBuild,String HPark,String HFloor,String HRoom,String HArea,String HState,String HType,String HNote,String ONo,String OName,String OSex,String OTel,String OID,String ONote){
        this.HNo = new SimpleStringProperty(HNo);
        this.HBuild = new SimpleStringProperty(HBuild);
        this.HPark = new SimpleStringProperty(HPark);
        this.HFloor = new SimpleStringProperty(HFloor);
        this.HRoom = new SimpleStringProperty(HRoom);
        this.HArea = new SimpleStringProperty(HArea);
        this.HState = new SimpleStringProperty(HState);
        this.HType = new SimpleStringProperty(HType);
        this.HNote = new SimpleStringProperty(HNote);
        this.ONo = new SimpleStringProperty(ONo);
        this.OName = new SimpleStringProperty(OName);
        this.OSex = new SimpleStringProperty(OSex);
        this.OTel = new SimpleStringProperty(OTel);
        this.OID = new SimpleStringProperty(OID);
        this.ONote = new SimpleStringProperty(ONote);
    }
    public StringProperty getHNo(){
        return HNo;
    }
    public StringProperty getHBuild(){
        return HBuild;
    }
    public StringProperty getHPark(){
        return HPark;
    }
    public StringProperty getHFloor(){
        return HFloor;
    }
    public StringProperty getHRoom(){
        return HRoom;
    }
    public StringProperty getHArea(){
        return HArea;
    }
    public StringProperty getHState(){
        return HState;
    }
    public StringProperty getHType(){
        return HType;
    }
    public StringProperty getHNote(){
        return HNote;
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
    public void setHNo(String HNo){
        this.HNo.set(HNo);
    }
    public void setHBuild(String HBuild){
        this.HBuild.set(HBuild);
    }
    public void setHPark(String HPark){
        this.HPark.set(HPark);
    }
    public void setHFloor(String HFloor){
        this.HFloor.set(HFloor);
    }
    public void setHRoom(String HRoom){
        this.HRoom.set(HRoom);
    }
    public void setHArea(String HArea){
        this.HArea.set(HArea);
    }
    public void setHState(String HState){
        this.HState.set(HState);
    }
    public void setHType(String HType){
        this.HType.set(HType);
    }
    public void setHNote(String HNote){
        this.HNote.set(HNote);
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
}