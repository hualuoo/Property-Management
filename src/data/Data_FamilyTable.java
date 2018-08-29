package data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Data_FamilyTable {
    /*家庭成员信息    FNo家庭成员编号    FName家庭成员姓名    FSex家庭成员性别    FTel家庭成员电话    FID家庭成员身份证号码    FRelation与业主关系    FNote家庭成员备注*/
    private SimpleStringProperty FNo,FName,FSex,FTel,FID,FRelation,FNote;
    private SimpleStringProperty ONo;
    public Data_FamilyTable(String FNo,String FName,String FSex,String FTel,String FID,String FRelation,String FNote,String ONo){
        this.FNo = new SimpleStringProperty(FNo);
        this.FName = new SimpleStringProperty(FName);
        this.FSex = new SimpleStringProperty(FSex);
        this.FTel = new SimpleStringProperty(FTel);
        this.FID = new SimpleStringProperty(FID);
        this.FRelation = new SimpleStringProperty(FRelation);
        this.FNote = new SimpleStringProperty(FNote);
        this.ONo = new SimpleStringProperty(ONo);
    }
    public StringProperty getFNo(){
        return FNo;
    }
    public StringProperty getFName(){
        return FName;
    }
    public StringProperty getFSex(){
        return FSex;
    }
    public StringProperty getFTel(){
        return FTel;
    }
    public StringProperty getFID(){
        return FID;
    }
    public StringProperty getFRelation(){
        return FRelation;
    }
    public StringProperty getFNote(){
        return FNote;
    }
    public StringProperty getONo(){
        return ONo;
    }
    public void setFNo(String FNo){
        this.FNo.set(FNo);
    }
    public void setFName(String FName){
        this.FName.set(FName);
    }
    public void setFSex(String FSex){
        this.FSex.set(FSex);
    }
    public void setFTel(String FTel){
        this.FTel.set(FTel);
    }
    public void setFID(String FID){
        this.FID.set(FID);
    }
    public void setFRelatione(String FRelation){
        this.FRelation.set(FRelation);
    }
    public void setFNote(String FNote){
        this.FNote.set(FNote);
    }
    public void setONo(String ONo){
        this.ONo.set(ONo);
    }
}