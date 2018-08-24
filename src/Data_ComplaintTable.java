import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Data_ComplaintTable {
    /*维修信息    RNo编号    RSubDate提交日期    RTitle标题    RText正文    RState状态    RReply回复    RSolveDate处理日期    ONo业主编号*/
    private SimpleStringProperty CNo,CSubDate,CTitle,CText,CState,CReply,CSolveDate;
    private SimpleStringProperty ONo,OName,OSex,OTel,OID,ONote;
    public Data_ComplaintTable(String CNo,String CSubDate,String CTitle,String CText,String CState,String CReply,String CSolveDate,String ONo,String OName,String OSex,String OTel,String OID,String ONote){
        this.CNo = new SimpleStringProperty(CNo);
        this.CSubDate = new SimpleStringProperty(CSubDate);
        this.CTitle = new SimpleStringProperty(CTitle);
        this.CText = new SimpleStringProperty(CText);
        this.CState = new SimpleStringProperty(CState);
        this.CReply = new SimpleStringProperty(CReply);
        this.CSolveDate = new SimpleStringProperty(CSolveDate);
        this.ONo = new SimpleStringProperty(ONo);
        this.OName = new SimpleStringProperty(OName);
        this.OSex = new SimpleStringProperty(OSex);
        this.OTel = new SimpleStringProperty(OTel);
        this.OID = new SimpleStringProperty(OID);
        this.ONote = new SimpleStringProperty(ONote);
    }
    public StringProperty getCNo(){
        return CNo;
    }
    public StringProperty getCSubDate(){
        return CSubDate;
    }
    public StringProperty getCTitle(){
        return CTitle;
    }
    public StringProperty getCText(){
        return CText;
    }
    public StringProperty getCState(){
        return CState;
    }
    public StringProperty getCReply(){
        return CReply;
    }
    public StringProperty getCSolveDate(){
        return CSolveDate;
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
    public void setCNo(String CNo){
        this.CNo.set(CNo);
    }
    public void setCSubDate(String RSubDate){
        this.CSubDate.set(RSubDate);
    }
    public void setCTitle(String CTitle){
        this.CTitle.set(CTitle);
    }
    public void setCText(String CText){
        this.CText.set(CText);
    }
    public void setCState(String CState){
        this.CState.set(CState);
    }
    public void setCReply(String CReply){
        this.CReply.set(CReply);
    }
    public void setCSolveDate(String CSolveDate){
        this.CSolveDate.set(CSolveDate);
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