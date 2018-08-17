import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Data_ParkingTable {
    private SimpleStringProperty PNo,PRegion,PState,CarNo,PNote;
    private SimpleStringProperty ONo,OName,OSex,OTel,OID,ONote;
    public Data_ParkingTable(String PNo,String PRegion,String PState,String CarNo,String PNote,String ONo,String OName,String OSex,String OTel,String OID,String ONote){
        this.PNo = new SimpleStringProperty(PNo);
        this.PRegion = new SimpleStringProperty(PRegion);
        this.PState = new SimpleStringProperty(PState);
        this.CarNo = new SimpleStringProperty(CarNo);
        this.PNote = new SimpleStringProperty(PNote);
        this.ONo = new SimpleStringProperty(ONo);
        this.OName = new SimpleStringProperty(OName);
        this.OSex = new SimpleStringProperty(OSex);
        this.OTel = new SimpleStringProperty(OTel);
        this.OID = new SimpleStringProperty(OID);
        this.ONote = new SimpleStringProperty(ONote);
    }
    public StringProperty getPNo(){
        return PNo;
    }
    public StringProperty getPRegion(){
        return PRegion;
    }
    public StringProperty getPState(){
        return PState;
    }
    public StringProperty getCarNo(){
        return CarNo;
    }
    public StringProperty getPNote(){
        return PNote;
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
    public void setPNo(String PNo){
        this.PNo.set(PNo);
    }
    public void setPRegion(String PRegion){
        this.PRegion.set(PRegion);
    }
    public void setPState(String PState){
        this.PState.set(PState);
    }
    public void setCarNo(String CarNo){
        this.CarNo.set(CarNo);
    }
    public void setPNote(String PNote){
        this.PNote.set(PNote);
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