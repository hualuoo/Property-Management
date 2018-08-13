import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.Optional;
import java.util.regex.Pattern;

public class IndexNewHouse_Controller {
    public Label HNo_Label,Information_Label,HAreaError_Label;
    public TextField HBuild_TextField,HPark_TextField,HFloor_TextField,HRoom_TextField,HArea_TextField,HType_TextField;
    public TextField ONo_TextField,OName_TextField,OTel_TextField,OID_TextField;
    public TextArea HNote_TextArea,ONote_TextArea;
    public ChoiceBox HState_ChoiceBox,OSex_ChoiceBox;
    public Button Confirm_Button,Exit_Button;
    public HouseTableData houseTableData;
    private Stage dialogStage;
    public String query,query_Insert,query_Insert2;
    public void setDialogStage(Stage dialogStage) {
        //传参Stage
        this.dialogStage = dialogStage;
    }
    public void setHouseTableData(HouseTableData houseTableData){
        //传参房屋数据
        this.houseTableData = houseTableData;
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
        //房屋销售情况选择框变更监听
        HState_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue)->
                changeHState_ChoiceBox(newValue));
        //添加房屋窗口的房屋销售情况选择框默认为"未销售"
        HState_ChoiceBox.setValue("未销售");

        check_HBuild();
        check_HPark();
        check_HFloor();
        check_HRoom();

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
    public void changeHState_ChoiceBox(Number newValue) {
        //房屋销售情况选择框变更
        if (newValue.intValue() == 1) {
            //选择第2项"未销售"
            ONo_TextField.setDisable(true);
            OName_TextField.setDisable(true);
            OSex_ChoiceBox.setDisable(true);
            OTel_TextField.setDisable(true);
            OID_TextField.setDisable(true);
            ONote_TextArea.setDisable(true);
            ONo_TextField.setText("");
            OName_TextField.setText("");
            OTel_TextField.setText("");
            OID_TextField.setText("");
            ONote_TextArea.setText("");
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
    public void button_Confirm_Click(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您确认是否添加该条信息？");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            New_HouseData();
            AddSQLData();
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("小区物业管理系统");
            alert2.setHeaderText("信息添加成功！");
            alert2.initOwner(Confirm_Button.getScene().getWindow());
            alert2.showAndWait();
            close_Windows();
        }
    }
    public HouseTableData getHouseTableData(){
        return houseTableData;
    }
    public void exit_Button_Click(){
        close_Windows();
    }
    public void New_HouseData(){
        houseTableData.setHNo(HNo_Label.getText().trim());
        houseTableData.setHBuild(HBuild_TextField.getText().trim() + "幢");
        houseTableData.setHPark(HPark_TextField.getText().trim() + "单元");
        houseTableData.setHFloor(HFloor_TextField.getText().trim() + "层");
        houseTableData.setHRoom(HRoom_TextField.getText().trim() + "室");
        houseTableData.setHArea(HArea_TextField.getText().trim() + "㎡");
        houseTableData.setHState(HState_ChoiceBox.getSelectionModel().getSelectedItem().toString());
        houseTableData.setHType(HType_TextField.getText().trim());
        houseTableData.setHNote(HNote_TextArea.getText().trim());
        if(HState_ChoiceBox.getSelectionModel().getSelectedItem().toString().equals("未销售")==true){
            houseTableData.setONo("");
            houseTableData.setOName("");
            houseTableData.setOSex("");
            houseTableData.setOTel("");
            houseTableData.setOID("");
            houseTableData.setONote("");
        }
        else {
            houseTableData.setONo(ONo_TextField.getText().trim());
            houseTableData.setOName(OName_TextField.getText().trim());
            houseTableData.setOSex(OSex_ChoiceBox.getSelectionModel().getSelectedItem().toString());
            houseTableData.setOTel(OTel_TextField.getText().trim());
            houseTableData.setOID(OID_TextField.getText().trim());
            houseTableData.setONote(ONote_TextArea.getText().trim());
        }
    }
    public void AddSQLData(){
        if(HState_ChoiceBox.getSelectionModel().getSelectedItem().toString().equals("未销售")==true){
            query_Insert = "INSERT INTO House_Info VALUES" +
                    "(\'" + HNo_Label.getText().trim() + "\'," + HBuild_TextField.getText().trim() + "," + HPark_TextField.getText().trim() + "," +
                    HFloor_TextField.getText().trim() + "," + HRoom_TextField.getText().trim() + "," + HArea_TextField.getText().trim() + ",\'" +
                    HState_ChoiceBox.getSelectionModel().getSelectedItem() + "\',\'" + HType_TextField.getText().trim() + "\',\'" + HNote_TextArea.getText().trim() + "\',NULL);";
            query = query_Insert;
        }
        else {
            query_Insert = "INSERT INTO Owner_Info VALUES" +
                    "(\'" + ONo_TextField.getText().trim() + "\',\'" + OName_TextField.getText().trim() + "\',\'" + OSex_ChoiceBox.getSelectionModel().getSelectedItem() + "\',\'" +
                    OTel_TextField.getText().trim() + "\',\'" + OID_TextField.getText().trim() + "\',\'" + ONote_TextArea.getText().trim() + "\');";
            query_Insert2 = "INSERT INTO House_Info VALUES" +
                    "(\'" + HNo_Label.getText().trim() + "\'," + HBuild_TextField.getText().trim() + "," + HPark_TextField.getText().trim() + "," +
                    HFloor_TextField.getText().trim() + "," + HRoom_TextField.getText().trim() + "," + HArea_TextField.getText().trim() + ",\'" +
                    HState_ChoiceBox.getSelectionModel().getSelectedItem() + "\',\'" + HType_TextField.getText().trim() + "\',\'" + HNote_TextArea.getText().trim() + "\',\'" + ONo_TextField.getText().trim() + "\');";
            query = query_Insert + " " + query_Insert2;
        }
        //System.out.print(query);
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    public void close_Windows(){
        Stage stage = (Stage)Confirm_Button.getScene().getWindow();
        stage.close();
    }
    public void check_HBuild(){
        HBuild_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //文本框文字修改监听 正则判断
                String regex = "[0-9]*";
                if(newValue==null || newValue.length()==0){
                    Information_Label.setText("");
                }
                else{
                    if(Pattern.compile(regex).matcher(newValue).matches()==false){
                        Information_Label.setText("请输入正确的楼宇号码！");
                        Information_Label.setTextFill(Color.web("#ff1a00"));
                    }
                    else {
                        Information_Label.setText("");
                        create_HNo();
                    }
                }
            }
        });
    }
    public void check_HPark(){
        HPark_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //文本框文字修改监听 正则判断
                String regex = "[0-9]*";
                if(newValue==null || newValue.length()==0){
                    Information_Label.setText("");
                }
                else{
                    if(Pattern.compile(regex).matcher(newValue).matches()==false){
                        Information_Label.setText("请输入正确的单元号码！");
                        Information_Label.setTextFill(Color.web("#ff1a00"));
                    }
                    else {
                        Information_Label.setText("");
                        create_HNo();
                    }
                }
            }
        });
    }
    public void check_HFloor(){
        HFloor_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //文本框文字修改监听 正则判断
                String regex = "[0-9]*";
                if(newValue==null || newValue.length()==0){
                    Information_Label.setText("");
                }
                else{
                    if(Pattern.compile(regex).matcher(newValue).matches()==false){
                        Information_Label.setText("请输入正确的楼层号码！");
                        Information_Label.setTextFill(Color.web("#ff1a00"));
                    }
                    else {
                        Information_Label.setText("");
                    }
                }
            }
        });
    }public void check_HRoom(){
        HRoom_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //文本框文字修改监听 正则判断
                String regex = "[0-9]*";
                if(newValue==null || newValue.length()==0){
                    Information_Label.setText("");
                }
                else{
                    if(Pattern.compile(regex).matcher(newValue).matches()==false){
                        Information_Label.setText("请输入正确的门牌号！");
                        Information_Label.setTextFill(Color.web("#ff1a00"));
                    }
                    else {
                        Information_Label.setText("");
                        create_HNo();
                    }
                }
            }
        });
    }
    public void check_HArea(){
        HArea_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //房屋面积文本框文字修改监听，正则判断
                String regex = "[0-9]*";
                if(newValue==null || newValue.length()==0){
                    HAreaError_Label.setText("请输入房屋面积");
                    HAreaError_Label.setTextFill(Color.web("#ff1a00"));
                }
                else{
                    if(Pattern.compile(regex).matcher(newValue).matches()==false ){
                        HAreaError_Label.setText("请输入正确的房屋面积");
                        HAreaError_Label.setTextFill(Color.web("#ff1a00"));
                    }
                    else {
                        HAreaError_Label.setText("");
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
                        Information_Label.setText("身份证号码输入正确");
                        Information_Label.setTextFill(Color.web("#2E8B57"));
                    }
                }
            }
        });
    }
    public void create_HNo(){
        //根据输入的房屋信息生成编号
        HNo_Label.setText(HBuild_TextField.getText() + "#" + HPark_TextField.getText() + "-" + HRoom_TextField.getText());
    }
}