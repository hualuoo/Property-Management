import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.Optional;
import java.util.regex.Pattern;

public class Controller_IndexNewHouse {
    public Label HNo_Label,HAreaError_Label,HInfoError_Label,OIDError_Label;
    public TextField HBuild_TextField,HPark_TextField,HFloor_TextField,HRoom_TextField,HArea_TextField,HType_TextField;
    public TextField ONo_TextField,OName_TextField,OTel_TextField,OID_TextField;
    public TextArea HNote_TextArea,ONote_TextArea;
    public ChoiceBox HState_ChoiceBox,OSex_ChoiceBox;
    public Button Confirm_Button,Exit_Button;

    public Data_HouseTable houseTableData;
    private Stage dialogStage;

    String query,query_Insert,query_Insert2;
    int check_HBuild,check_HPark,check_HFloor,check_HRoom,check_HArea,check_OID;

    public Data_HouseTable getHouseTableData(){
        return houseTableData;
    }

    public void setDialogStage(Stage dialogStage) {
        //传参Stage
        this.dialogStage = dialogStage;
    }
    public void setHouseTableData(Data_HouseTable houseTableData){
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
        //初始化禁止使用确认按钮
        Confirm_Button.setDisable(true);

        //文本框文字修改监听
        HBuild_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                check_HBuildTextField();
                create_HNo();
            }
        });
        HPark_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                check_HParkTextField();
                create_HNo();
            }
        });
        HFloor_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                check_HFloorTextField();
                create_HNo();
            }
        });
        HRoom_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                check_HRoomTextField();
                create_HNo();
            }
        });
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

    public void create_HNo(){
        //根据输入的房屋信息生成编号
        HNo_Label.setText(HBuild_TextField.getText() + "#" + HPark_TextField.getText() + "-" + HRoom_TextField.getText());
    }

    public void check_HBuildTextField() {
        String regex = "[0-9]*";
        if (HBuild_TextField.getText() == null || HBuild_TextField.getText().length() == 0) {
            HInfoError_Label.setText("房屋楼宇号不能为空");
            Confirm_Button.setDisable(true);
            check_HBuild = 0;
            return;
        }
        if (Pattern.compile(regex).matcher(HBuild_TextField.getText()).matches() == false) {
            HInfoError_Label.setText("请输入正确的房屋楼宇号");
            Confirm_Button.setDisable(true);
            check_HBuild = 0;
            return;
        }
        HInfoError_Label.setText("");
        check_HBuild = 1;
        Confirm_Button.setDisable(false);
    }
    public void check_HParkTextField() {
        String regex = "[0-9]*";
        if (HPark_TextField.getText() == null || HPark_TextField.getText().length() == 0) {
            HInfoError_Label.setText("房屋单元号不能为空");
            Confirm_Button.setDisable(true);
            check_HPark = 0;
            return;
        }
        if (Pattern.compile(regex).matcher(HPark_TextField.getText()).matches() == false) {
            HInfoError_Label.setText("请输入正确的房屋单元号");
            Confirm_Button.setDisable(true);
            check_HPark = 0;
            return;
        }
        HInfoError_Label.setText("");
        check_HPark = 1;
        Confirm_Button.setDisable(false);
    }
    public void check_HFloorTextField() {
        String regex = "[0-9]*";
        if (HFloor_TextField.getText() == null || HFloor_TextField.getText().length() == 0) {
            HInfoError_Label.setText("房屋楼层号不能为空");
            Confirm_Button.setDisable(true);
            check_HFloor = 0;
            return;
        }
        if (Pattern.compile(regex).matcher(HFloor_TextField.getText()).matches() == false) {
            HInfoError_Label.setText("请输入正确的房屋楼层号");
            Confirm_Button.setDisable(true);
            check_HFloor = 0;
            return;
        }
        HInfoError_Label.setText("");
        check_HFloor = 1;
        Confirm_Button.setDisable(false);
    }
    public void check_HRoomTextField() {
        String regex = "[0-9]*";
        if(HRoom_TextField.getText() == null || HRoom_TextField.getText().length()==0){
            HInfoError_Label.setText("房屋号不能为空");
            Confirm_Button.setDisable(true);
            check_HRoom = 0;
            return;
        }
        if(Pattern.compile(regex).matcher(HRoom_TextField.getText()).matches()==false){
            HInfoError_Label.setText("请输入正确的房屋号");
            Confirm_Button.setDisable(true);
            check_HRoom = 0;
            return;
        }
        HInfoError_Label.setText("");
        check_HRoom = 1;
        Confirm_Button.setDisable(false);
    }
    public void check_HAreaTextField(){
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
        alert.setHeaderText("您确认是否添加该条信息？");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }
        if(HState_ChoiceBox.getSelectionModel().getSelectedItem().toString().equals("未销售")){
            //房屋状态"未销售"
            //检查数据
            if(check_HBuild == 1 && check_HPark == 1 && check_HFloor == 1 && check_HRoom == 1 && check_HArea == 1){
                //houseTableData数据修改  "未销售"
                addHouseTableData_SellNo();
                //将数据写入数据库  "未销售"
                addSQL_SellNo();
                //添加成功信息框
                succeedAdd();
            }
            else {
                errorAdd();
            }
        }
        else {
            //房屋状态"已销售""待入住"
            //检查数据
            if(check_HBuild == 1 && check_HPark == 1 && check_HFloor == 1 && check_HRoom == 1 && check_HArea == 1 && check_OID == 1){
                //houseTableData数据修改  "已销售""待入住"
                addHouseTableData_SellYes();
                //将数据写入数据库  "已销售""待入住"
                addSQL_SellYes();
                //添加成功信息框
                succeedAdd();
            }
            else {
                errorAdd();
            }
        }
    }
    public void addHouseTableData_SellNo(){
        houseTableData.setHNo(HNo_Label.getText());
        houseTableData.setHBuild(HBuild_TextField.getText() + "幢");
        houseTableData.setHPark(HPark_TextField.getText() + "单元");
        houseTableData.setHFloor(HFloor_TextField.getText() + "层");
        houseTableData.setHRoom(HRoom_TextField.getText() + "室");
        houseTableData.setHArea(HArea_TextField.getText() + "㎡");
        houseTableData.setHState(HState_ChoiceBox.getSelectionModel().getSelectedItem().toString());
        houseTableData.setHType(HType_TextField.getText().trim());
        houseTableData.setHNote(HNote_TextArea.getText());
        houseTableData.setONo("");
        houseTableData.setOName("");
        houseTableData.setOSex("");
        houseTableData.setOTel("");
        houseTableData.setOID("");
        houseTableData.setONote("");
    }
    public void addHouseTableData_SellYes(){
        houseTableData.setHNo(HNo_Label.getText());
        houseTableData.setHBuild(HBuild_TextField.getText() + "幢");
        houseTableData.setHPark(HPark_TextField.getText() + "单元");
        houseTableData.setHFloor(HFloor_TextField.getText() + "层");
        houseTableData.setHRoom(HRoom_TextField.getText() + "室");
        houseTableData.setHArea(HArea_TextField.getText() + "㎡");
        houseTableData.setHState(HState_ChoiceBox.getSelectionModel().getSelectedItem().toString());
        houseTableData.setHType(HType_TextField.getText());
        houseTableData.setHNote(HNote_TextArea.getText());
        houseTableData.setONo(ONo_TextField.getText().trim());
        houseTableData.setOName(OName_TextField.getText().trim());
        houseTableData.setOSex(OSex_ChoiceBox.getSelectionModel().getSelectedItem().toString());
        houseTableData.setOTel(OTel_TextField.getText().trim());
        houseTableData.setOID(OID_TextField.getText());
        houseTableData.setONote(ONote_TextArea.getText());
    }
    public void addSQL_SellNo(){
        query_Insert = "INSERT INTO House_Info VALUES" +
                "(\'" + HNo_Label.getText().trim() + "\'," + HBuild_TextField.getText().trim() + "," + HPark_TextField.getText().trim() + "," +
                HFloor_TextField.getText().trim() + "," + HRoom_TextField.getText().trim() + "," + HArea_TextField.getText().trim() + ",\'" +
                HState_ChoiceBox.getSelectionModel().getSelectedItem() + "\',\'" + HType_TextField.getText().trim() + "\',\'" + HNote_TextArea.getText().trim() + "\',NULL);";
        query = query_Insert;
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    public void addSQL_SellYes(){
        query_Insert = "INSERT INTO Owner_Info VALUES" +
                "(\'" + ONo_TextField.getText().trim() + "\',\'" + OName_TextField.getText().trim() + "\',\'" + OSex_ChoiceBox.getSelectionModel().getSelectedItem() + "\',\'" +
                OTel_TextField.getText().trim() + "\',\'" + OID_TextField.getText().trim() + "\',\'" + ONote_TextArea.getText().trim() + "\');";
        query_Insert2 = "INSERT INTO House_Info VALUES" +
                "(\'" + HNo_Label.getText().trim() + "\'," + HBuild_TextField.getText().trim() + "," + HPark_TextField.getText().trim() + "," +
                HFloor_TextField.getText().trim() + "," + HRoom_TextField.getText().trim() + "," + HArea_TextField.getText().trim() + ",\'" +
                HState_ChoiceBox.getSelectionModel().getSelectedItem() + "\',\'" + HType_TextField.getText().trim() + "\',\'" + HNote_TextArea.getText().trim() + "\',\'" + ONo_TextField.getText().trim() + "\');";
        query = query_Insert + " " + query_Insert2;
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    public void errorAdd(){
        Alert alert2 = new Alert(Alert.AlertType.ERROR);
        alert2.setTitle("小区物业管理系统");
        alert2.setHeaderText("输入数据检测异常，添加数据失败！");
        alert2.initOwner(Confirm_Button.getScene().getWindow());
        alert2.showAndWait();
    }
    public void succeedAdd(){
        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert2.setTitle("小区物业管理系统");
        alert2.setHeaderText("信息添加成功！");
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