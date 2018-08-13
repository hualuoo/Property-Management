import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.Optional;
import java.util.regex.Pattern;

public class IndexEditHouse_Controller {
    public Label HNo_Label,Information_Label;
    public TextField HBuild_TextField,HPark_TextField,HFloor_TextField,HRoom_TextField,HArea_TextField,HType_TextField;
    public TextField ONo_TextField,OName_TextField,OTel_TextField,OID_TextField;
    public TextArea HNote_TextArea,ONote_TextArea;
    public ChoiceBox HState_ChoiceBox,OSex_ChoiceBox;
    public Button Confirm_Button,Exit_Button;
    public HouseTableData houseTableData;
    private Stage dialogStage;
    public String query,query_Update,query_Update2,query_Delete,query_Insert;
    public void initialize() {
        HState_ChoiceBox.setItems(FXCollections.observableArrayList(
                "已销售", "未销售", "待入住")
        );
        OSex_ChoiceBox.setItems(FXCollections.observableArrayList(
                "男", "女")
        );
        HState_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue)->
            setTextField(newValue));
        HBuild_TextField.setDisable(true);
        HPark_TextField.setDisable(true);
        HFloor_TextField.setDisable(true);
        HRoom_TextField.setDisable(true);
        HArea_TextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            //文本框焦点获取监听
            public void changed(ObservableValue<? extends Boolean> arg0,Boolean arg1, Boolean arg2) {
                check_HArea();
            }
        });
        OID_TextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            //文本框焦点获取监听
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0,Boolean arg1, Boolean arg2) {
                check_ID();
            }
        });
    }
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    public void setTextField(Number newValue){
        if(newValue.intValue() == 1){
            ONo_TextField.setDisable(true);
            ONo_TextField.setText("");
            OName_TextField.setDisable(true);
            OName_TextField.setText("");
            OSex_ChoiceBox.setDisable(true);
            OTel_TextField.setDisable(true);
            OTel_TextField.setText("");
            OID_TextField.setDisable(true);
            OID_TextField.setText("");
            ONote_TextArea.setDisable(true);
            ONote_TextArea.setText("");
        }
        else {
            ONo_TextField.setDisable(false);
            OName_TextField.setDisable(false);
            OSex_ChoiceBox.setDisable(false);
            OTel_TextField.setDisable(false);
            OID_TextField.setDisable(false);
            ONote_TextArea.setDisable(false);
        }
    }
    public void setHouse(HouseTableData houseTableData){
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
    public void button_Confirm_Click(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您确认是否编辑该条信息？");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            edit_HouseData();
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("小区物业管理系统");
            alert2.setHeaderText("信息修改成功！");
            alert2.initOwner(Confirm_Button.getScene().getWindow());
            alert2.showAndWait();
            close_Windows();
        }
    }
    public void exit_Button_Click(){
        close_Windows();
    }
    public void edit_HouseData(){
        if(HState_ChoiceBox.getSelectionModel().getSelectedItem().toString().equals("未销售")==true){
            //如果选择的是未销售
            houseTableData.setHArea(HArea_TextField.getText().trim() + "㎡");
            houseTableData.setHType(HType_TextField.getText().trim());
            houseTableData.setHNote(HNote_TextArea.getText().trim());
            if(houseTableData.getHState().getValue().trim().equals("未销售")==true){
                //未销售 变 未销售
                query_Update = "UPDATE House_Info SET " +
                        "HArea=" + HArea_TextField.getText().trim().replace("㎡","") + "," +
                        "HType=\'" + HType_TextField.getText().trim() + "\'," +
                        "HNote=\'" + HNote_TextArea.getText().trim() + "\' " +
                        "WHERE HNo=\'" + HNo_Label.getText().trim() + "\' ";
                query = query_Update;
            }
            else {
                //已销售待入住 变 未销售
                query_Update = "UPDATE House_Info SET " +
                        "HArea=" + HArea_TextField.getText().trim().replace("㎡","") + "," +
                        "HState=\'" + HState_ChoiceBox.getSelectionModel().getSelectedItem() + "\'," +
                        "HType=\'" + HType_TextField.getText().trim() + "\'," +
                        "HNote=\'" + HNote_TextArea.getText().trim() + "\'," +
                        "ONo = NULL " +
                        "WHERE HNo=\'" + HNo_Label.getText().trim() + "\'";
                query_Delete = "DELETE FROM Owner_Info WHERE ONo=\'" + houseTableData.getONo().getValue().trim() + "\'";
                query = query_Update + " " + query_Delete;
                houseTableData.setHState(HState_ChoiceBox.getSelectionModel().getSelectedItem().toString());
                houseTableData.setONo("");
                houseTableData.setOName("");
                houseTableData.setOSex("");
                houseTableData.setOTel("");
                houseTableData.setOID("");
                houseTableData.setONote("");
            }
        }
        else {
            houseTableData.setHArea(HArea_TextField.getText().trim() + "㎡");
            houseTableData.setHType(HType_TextField.getText().trim());
            houseTableData.setHNote(HNote_TextArea.getText().trim());
            if(houseTableData.getHState().getValue().trim().equals("未销售")==true){
                //未销售 变 已销售待入住
                query_Insert = "INSERT INTO Owner_Info VALUES" +
                        "(\'" + ONo_TextField.getText().trim() + "\',\'" + OName_TextField.getText().trim() + "\',\'" + OSex_ChoiceBox.getSelectionModel().getSelectedItem() + "\',\'" +
                        OTel_TextField.getText().trim() + "\',\'" + OID_TextField.getText().trim() + "\',\'" + ONote_TextArea.getText().trim() + "\');";
                query_Update = "UPDATE House_Info SET " +
                        "HArea=" + HArea_TextField.getText().trim().replace("㎡","") + "," +
                        "HState=\'" + HState_ChoiceBox.getSelectionModel().getSelectedItem() + "\'," +
                        "HType=\'" + HType_TextField.getText().trim() + "\'," +
                        "HNote=\'" + HNote_TextArea.getText().trim() + "\'," +
                        "ONo=\'" + ONo_TextField.getText() + "\' " +
                        "WHERE HNo=\'" + HNo_Label.getText().trim()+ "\'";
                query = query_Insert + " " + query_Update;
            }
            else {
                //已销售待入住 变 已销售待入住
                //已写触发器，业主ID变化自动应用到House_Info.ONo
                query_Update = "ALTER TABLE House_Info NOCHECK CONSTRAINT ALL " +
                        "UPDATE Owner_Info SET ONo=\'" + ONo_TextField.getText().trim() + "\',"+
                        "OName=\'" + OName_TextField.getText().trim() + "\'," +
                        "OSex=\'" + OSex_ChoiceBox.getSelectionModel().getSelectedItem() + "\'," +
                        "OTel=\'" + OTel_TextField.getText().trim() + "\'," +
                        "OID=\'" + OID_TextField.getText().trim() + "\'," +
                        "ONote=\'" + ONote_TextArea.getText().trim() + "\' " +
                        "WHERE Owner_Info.ONo=\'" + houseTableData.getONo().getValue().trim() + "\' " +
                        "ALTER TABLE House_Info CHECK CONSTRAINT ALL";
                query_Update2 = "UPDATE House_Info SET " +
                        "HArea=" + HArea_TextField.getText().trim().replace("㎡","") + "," +
                        "HState=\'" + HState_ChoiceBox.getSelectionModel().getSelectedItem() + "\'," +
                        "HType=\'" + HType_TextField.getText().trim() + "\'," +
                        "HNote=\'" + HNote_TextArea.getText().trim() + "\' " +
                        "WHERE HNo=\'" + HNo_Label.getText().trim() + "\'";
                query = query_Update + " " + query_Update2;
            }
            houseTableData.setHState(HState_ChoiceBox.getSelectionModel().getSelectedItem().toString());
            houseTableData.setONo(ONo_TextField.getText().trim());
            houseTableData.setOName(OName_TextField.getText().trim());
            houseTableData.setOSex(OSex_ChoiceBox.getSelectionModel().getSelectedItem().toString());
            houseTableData.setOTel(OTel_TextField.getText().trim());
            houseTableData.setOID(OID_TextField.getText().trim());
            houseTableData.setONote(ONote_TextArea.getText().trim());
        }
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    public void close_Windows(){
        Stage stage = (Stage)Confirm_Button.getScene().getWindow();
        stage.close();
    }
    public void check_HArea(){
        HArea_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //文本框文字修改监听 正则判断
                String regex = "[0-9]*";
                if(newValue==null || newValue.length()==0){
                    Information_Label.setText("");
                }
                else{
                    if(Pattern.compile(regex).matcher(newValue).matches()==false){
                        Information_Label.setText("请输入正确的房屋面积！");
                        Information_Label.setTextFill(Color.web("#ff1a00"));
                    }
                    else {
                        Information_Label.setText("输入正确");
                        Information_Label.setTextFill(Color.web("#2E8B57"));
                    }
                }
            }
        });
    }
    public void check_ID(){
        OID_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //文本框文字修改监听 正则判断
                String regex = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                        "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
                if(newValue==null || newValue.length()==0){
                    Information_Label.setText("");
                }
                else{
                    if(Pattern.compile(regex).matcher(newValue).matches()==false){
                        Information_Label.setText("请输入正确的身份证号码！");
                        Information_Label.setTextFill(Color.web("#ff1a00"));
                    }
                    else {
                        Information_Label.setText("输入正确");
                        Information_Label.setTextFill(Color.web("#2E8B57"));
                    }
                }
            }
        });
    }
}