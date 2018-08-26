package controller;

import data.Data_HouseTable;
import util.SQL_Connect;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.Optional;
import java.util.regex.Pattern;

public class Controller_IndexEditHouse {
    public Label HNo_Label,HAreaError_Label,HInfoError_Label,OIDError_Label;
    public TextField HBuild_TextField,HPark_TextField,HFloor_TextField,HRoom_TextField,HArea_TextField,HType_TextField;
    public TextField ONo_TextField,OName_TextField,OTel_TextField,OID_TextField;
    public TextArea HNote_TextArea,ONote_TextArea;
    public ChoiceBox HState_ChoiceBox,OSex_ChoiceBox;
    public Button Confirm_Button,Exit_Button;

    public Data_HouseTable houseTableData;
    private Stage dialogStage;

    String query,query_Update,query_Delete,query_Insert,query_Update2;
    int check_HArea,check_OID;

    public void setDialogStage(Stage dialogStage) {
        //传参Stage
        this.dialogStage = dialogStage;
    }
    public void setHouse(Data_HouseTable houseTableData){
        //设置修改数据
        this.houseTableData = houseTableData;
        HNo_Label.setText(houseTableData.getHNo().getValue().trim());
        HBuild_TextField.setText(houseTableData.getHBuild().getValue().replace("幢","").trim());
        HPark_TextField.setText(houseTableData.getHPark().getValue().replace("单元","").trim());
        HFloor_TextField.setText(houseTableData.getHFloor().getValue().replace("层","").trim());
        HRoom_TextField.setText(houseTableData.getHRoom().getValue().replace("室","").trim());
        HArea_TextField.setText(houseTableData.getHArea().getValue().replace("㎡","").trim());
        HState_ChoiceBox.setValue(houseTableData.getHState().getValue().trim());
        HType_TextField.setText(houseTableData.getHType().getValue().trim());
        if(houseTableData.getHNote().getValue()==null || houseTableData.getHNote().getValue().length() == 0)
            HNote_TextArea.setText("");
        else
            HNote_TextArea.setText(houseTableData.getHNote().getValue().trim());
        if(houseTableData.getONo().getValue()==null || houseTableData.getONo().getValue().length() == 0){
            //HState_ChoiceBox选择触发器已将业主信息TextField清空
        }
        else {
            ONo_TextField.setText(houseTableData.getONo().getValue().trim());
            OName_TextField.setText(houseTableData.getOName().getValue().trim());
            OSex_ChoiceBox.setValue(houseTableData.getOSex().getValue().trim());
            OTel_TextField.setText(houseTableData.getOTel().getValue().trim());
            OID_TextField.setText(houseTableData.getOID().getValue().trim());
            if(houseTableData.getONote().getValue()==null || houseTableData.getONote().getValue().length() == 0)
                ONote_TextArea.setText("");
            else
                ONote_TextArea.setText(houseTableData.getONote().getValue().trim());
        }
    }
    public void initialize() {
        //初始化
        //ChoiceBox加入选择项
        HState_ChoiceBox.setItems(FXCollections.observableArrayList(
                "已销售", "未销售", "待入住")
        );
        OSex_ChoiceBox.setItems(FXCollections.observableArrayList(
                "男", "女")
        );

        //禁止地址栏输入信息
        HBuild_TextField.setDisable(true);
        HPark_TextField.setDisable(true);
        HFloor_TextField.setDisable(true);
        HRoom_TextField.setDisable(true);
        //初始化禁止使用确认按钮
        Confirm_Button.setDisable(true);

        //房屋销售情况选择框变更监听
        HState_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue)->
                changeHState_ChoiceBox(newValue));

        //文本框文字修改监听
        HArea_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                check_HAreaTextField();
            }
        });
        OID_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                check_OIDTextField();
            }
        });
    }

    public void changeHState_ChoiceBox(Number newValue) {
        //房屋销售情况选择框变更
        if (newValue.intValue() == 1) {
            //选择第2项"未销售"
            ONo_TextField.setText("");
            OName_TextField.setText("");
            OTel_TextField.setText("");
            OID_TextField.setText("");
            ONote_TextArea.setText("");
            ONo_TextField.setDisable(true);
            OName_TextField.setDisable(true);
            OSex_ChoiceBox.setDisable(true);
            OTel_TextField.setDisable(true);
            OID_TextField.setDisable(true);
            ONote_TextArea.setDisable(true);
            OIDError_Label.setText("");
        } else {
            //选择其他选项
            ONo_TextField.setDisable(false);
            OName_TextField.setDisable(false);
            OSex_ChoiceBox.setDisable(false);
            OTel_TextField.setDisable(false);
            OID_TextField.setDisable(false);
            ONote_TextArea.setDisable(false);
        }
    }

    public void check_HAreaTextField(){
        //检查房屋面积
        String regex = "^[0-9]+(.[0-9]+)?$";
        if(HArea_TextField.getText() == null || HArea_TextField.getText().length()==0){
            HAreaError_Label.setText("房屋面积不能为空");
            Confirm_Button.setDisable(true);
            check_HArea = 0;
            return;
        }
        if(Pattern.compile(regex).matcher(HArea_TextField.getText()).matches()==false){
            HAreaError_Label.setText("请输入正确的房屋面积");
            Confirm_Button.setDisable(true);
            check_HArea = 0;
            return;
        }
        if(Double.parseDouble(HArea_TextField.getText())==0){
            HAreaError_Label.setText("房屋面积不能等于0");
            Confirm_Button.setDisable(true);
            check_HArea = 0;
            return;
        }
        HAreaError_Label.setText("");
        check_HArea = 1;
        Confirm_Button.setDisable(false);
    }
    public void check_OIDTextField(){
        //检查身份证号码
        String regex = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        if(OID_TextField.getText() == null || OID_TextField.getText().length()==0){
            OIDError_Label.setText("不能为空");
            Confirm_Button.setDisable(true);
            check_OID = 0;
            return;
        }
        if(Pattern.compile(regex).matcher(OID_TextField.getText()).matches()==false){
            OIDError_Label.setText("输入错误");
            Confirm_Button.setDisable(true);
            check_OID = 0;
            return;
        }
        OIDError_Label.setText("");
        check_OID = 1;
        Confirm_Button.setDisable(false);
    }

    public void click_ConfirmButton(){
        //按下"确认"按钮
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您确认是否编辑该条信息？");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }
        if(HState_ChoiceBox.getSelectionModel().getSelectedItem().toString().equals("未销售") && houseTableData.getHState().getValue().trim().equals("未销售")){
            //房屋状态"未销售"不变("未销售"变"未销售")
            if(check_HArea == 1){
                //将数据写入数据库  "未销售"不变("未销售"变"未销售")
                addSQL_SellNo_SellNo();
                //houseTableData数据修改  "未销售"不变("未销售"变"未销售")
                editHouseTableData_SellNo_SellNo();
                //修改成功信息框
                succeedEdit();
            }
            else {
                errorEdit();
            }
        }
        if(HState_ChoiceBox.getSelectionModel().getSelectedItem().toString().equals("未销售") && houseTableData.getHState().getValue().trim().equals("未销售")==false){
            //房屋状态"已销售"变"未销售"
            if(check_HArea == 1){
                //将数据写入数据库  "已销售"变"未销售"
                addSQL_SellYes_SellNo();
                //houseTableData数据修改  "已销售"变"未销售"
                editHouseTableData_SellYes_SellNo();
                //修改成功信息框
                succeedEdit();
            }
            else {
                errorEdit();
            }
        }
        if(HState_ChoiceBox.getSelectionModel().getSelectedItem().toString().equals("未销售")==false && houseTableData.getHState().getValue().trim().equals("未销售")){
            //房屋状态"未销售"变"已销售"
            if(check_HArea == 1 && check_OID == 1){
                //将数据写入数据库  "未销售"变"已销售"
                addSQL_SellNo_SellYes();
                //houseTableData数据修改  "未销售"变"已销售"
                editHouseTableData_SellNo_SellYes();
                //修改成功信息框
                succeedEdit();
            }
            else {
                errorEdit();
            }
        }
        if(HState_ChoiceBox.getSelectionModel().getSelectedItem().toString().equals("未销售")==false && houseTableData.getHState().getValue().trim().equals("未销售")==false){
            //房屋状态"已销售"不变("已销售"变"已销售")
            if(check_HArea == 1 && check_OID == 1){
                //将数据写入数据库  "已销售"不变("已销售"变"已销售")
                addSQL_SellYes_SellYes();
                //houseTableData数据修改  "已销售"不变("已销售"变"已销售")
                editHouseTableData_SellYes_SellYes();
                //修改成功信息框
                succeedEdit();
            }
            else {
                errorEdit();
            }
        }
    }
    public void addSQL_SellNo_SellNo(){
        //将数据写入数据库  "未销售"不变("未销售"变"未销售")
        query_Update = "UPDATE House_Info SET " +
                "HArea=" + HArea_TextField.getText() + "," +
                "HType=\'" + HType_TextField.getText().trim() + "\'," +
                "HNote=\'" + HNote_TextArea.getText() + "\' " +
                "WHERE HNo=\'" + HNo_Label.getText() + "\' ";
        query = query_Update;
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    public void editHouseTableData_SellNo_SellNo(){
        //houseTableData数据修改  "未销售"不变("未销售"变"未销售")
        houseTableData.setHArea(HArea_TextField.getText() + "㎡");
        houseTableData.setHType(HType_TextField.getText().trim());
        houseTableData.setHNote(HNote_TextArea.getText());
    }
    public void addSQL_SellYes_SellNo(){
        //将数据写入数据库  "已销售"变"未销售"
        query_Update = "UPDATE House_Info SET " +
                "HArea=" + HArea_TextField.getText() + "," +
                "HState=\'" + HState_ChoiceBox.getSelectionModel().getSelectedItem() + "\'," +
                "HType=\'" + HType_TextField.getText().trim() + "\'," +
                "HNote=\'" + HNote_TextArea.getText() + "\'," +
                "ONo = NULL " +
                "WHERE HNo=\'" + HNo_Label.getText() + "\'";
        query_Delete = "DELETE FROM Owner_Info WHERE ONo=\'" + houseTableData.getONo().getValue().trim() + "\'";
        query = query_Update + " " + query_Delete;
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    public void editHouseTableData_SellYes_SellNo(){
        //houseTableData数据修改  "已销售"变"未销售"
        houseTableData.setHArea(HArea_TextField.getText() + "㎡");
        houseTableData.setHType(HType_TextField.getText().trim());
        houseTableData.setHNote(HNote_TextArea.getText());
        houseTableData.setHState(HState_ChoiceBox.getSelectionModel().getSelectedItem().toString());
        houseTableData.setONo("");
        houseTableData.setOName("");
        houseTableData.setOSex("");
        houseTableData.setOTel("");
        houseTableData.setOID("");
        houseTableData.setONote("");
    }
    public void addSQL_SellNo_SellYes(){
        //将数据写入数据库  "未销售"变"已销售"
        query_Insert = "INSERT INTO Owner_Info VALUES" +
                "(" + ONo_TextField.getText().trim() + ",\'" + OName_TextField.getText().trim() + "\',\'" + OSex_ChoiceBox.getSelectionModel().getSelectedItem() + "\',\'" +
                OTel_TextField.getText().trim() + "\',\'" + OID_TextField.getText() + "\',\'" + ONote_TextArea.getText().trim() + "\');";
        query_Update = "UPDATE House_Info SET " +
                "HArea=" + HArea_TextField.getText() + "," +
                "HState=\'" + HState_ChoiceBox.getSelectionModel().getSelectedItem() + "\'," +
                "HType=\'" + HType_TextField.getText().trim() + "\'," +
                "HNote=\'" + HNote_TextArea.getText() + "\'," +
                "ONo=" + ONo_TextField.getText() + " " +
                "WHERE HNo=\'" + HNo_Label.getText() + "\'";
        query = query_Insert + " " + query_Update;
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    public void editHouseTableData_SellNo_SellYes(){
        //houseTableData数据修改  "未销售"变"已销售"
        houseTableData.setHArea(HArea_TextField.getText() + "㎡");
        houseTableData.setHType(HType_TextField.getText().trim());
        houseTableData.setHNote(HNote_TextArea.getText());
        houseTableData.setHState(HState_ChoiceBox.getSelectionModel().getSelectedItem().toString());
        houseTableData.setONo(ONo_TextField.getText().trim());
        houseTableData.setOName(OName_TextField.getText().trim());
        houseTableData.setOSex(OSex_ChoiceBox.getSelectionModel().getSelectedItem().toString());
        houseTableData.setOTel(OTel_TextField.getText());
        houseTableData.setOID(OID_TextField.getText().trim());
        houseTableData.setONote(ONote_TextArea.getText());
    }
    public void addSQL_SellYes_SellYes(){
        //将数据写入数据库  "已销售"不变("已销售"变"已销售")
        query_Update = "ALTER TABLE House_Info NOCHECK CONSTRAINT ALL " +
                "UPDATE Owner_Info SET ONo=\'" + ONo_TextField.getText().trim() + "\',"+
                "OName=\'" + OName_TextField.getText().trim() + "\'," +
                "OSex=\'" + OSex_ChoiceBox.getSelectionModel().getSelectedItem() + "\'," +
                "OTel=\'" + OTel_TextField.getText().trim() + "\'," +
                "OID=\'" + OID_TextField.getText() + "\'," +
                "ONote=\'" + ONote_TextArea.getText().trim() + "\' " +
                "WHERE Owner_Info.ONo=" + houseTableData.getONo().getValue().trim() + " " +
                "ALTER TABLE House_Info CHECK CONSTRAINT ALL";
        query_Update2 = "UPDATE House_Info SET " +
                "HArea=" + HArea_TextField.getText() + "," +
                "HType=\'" + HType_TextField.getText().trim() + "\'," +
                "HNote=\'" + HNote_TextArea.getText() + "\' " +
                "WHERE HNo=\'" + HNo_Label.getText() + "\'";
        query = query_Update + " " + query_Update2;
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    public void editHouseTableData_SellYes_SellYes(){
        //houseTableData数据修改  "已销售"不变("已销售"变"已销售")
        houseTableData.setHArea(HArea_TextField.getText() + "㎡");
        houseTableData.setHType(HType_TextField.getText().trim());
        houseTableData.setHNote(HNote_TextArea.getText());
        houseTableData.setONo(ONo_TextField.getText().trim());
        houseTableData.setOName(OName_TextField.getText().trim());
        houseTableData.setOSex(OSex_ChoiceBox.getSelectionModel().getSelectedItem().toString());
        houseTableData.setOTel(OTel_TextField.getText());
        houseTableData.setOID(OID_TextField.getText().trim());
        houseTableData.setONote(ONote_TextArea.getText());
    }
    public void errorEdit(){
        Alert alert2 = new Alert(Alert.AlertType.ERROR);
        alert2.setTitle("小区物业管理系统");
        alert2.setHeaderText("输入数据检测异常，修改数据失败！");
        alert2.initOwner(Confirm_Button.getScene().getWindow());
        alert2.showAndWait();
    }
    public void succeedEdit(){
        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert2.setTitle("小区物业管理系统");
        alert2.setHeaderText("信息修改成功！");
        alert2.initOwner(Confirm_Button.getScene().getWindow());
        alert2.showAndWait();
        close_Windows();
    }
    public void click_ExitButton(){
        close_Windows();
    }
    public void close_Windows(){
        Stage stage = (Stage)Confirm_Button.getScene().getWindow();
        stage.close();
    }
}