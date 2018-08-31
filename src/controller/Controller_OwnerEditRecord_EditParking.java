package controller;

import util.SQL_Connect;
import util.StageManager;

import javafx.scene.control.Alert;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.util.regex.Pattern;

public class Controller_OwnerEditRecord_EditParking {
    public TextField PNo_TextField,CarNo_TextField;
    public ChoiceBox PState_ChoiceBox;
    public Button Confirm_Button,Cancel_Button;
    //数据库代码以及返回结果
    String query;
    ResultSet result;
    public void setPNo(String PNo){
        PNo_TextField.setText(PNo);
    }
    public void setPState(String PState){
        PState_ChoiceBox.setValue(PState);
    }
    public void setCarNo(String CarNo){
        CarNo_TextField.setText(CarNo);
    }
    public void initialize(){
        //初始化
        //将"业主信息管理 - 车位信息 - 修改"控制器保存到map中
        StageManager.CONTROLLER.put("Controller_OwnerEditRecord_EditParking", this);
        //ChoiceBox加入选择项
        PState_ChoiceBox.setItems(FXCollections.observableArrayList(
                "已销售", "未销售", "已出租")
        );
        //PNo_TextField设置不可用
        PNo_TextField.setDisable(true);
        //车位销售情况选择框变更监听
        PState_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue)->
                changePState_ChoiceBox(newValue));
    }
    public void click_ConfirmButton(){
        if(PState_ChoiceBox.getSelectionModel().getSelectedItem().equals("已销售") || PState_ChoiceBox.getSelectionModel().getSelectedItem().equals("已出租")){
            if (CarNo_TextField.getText()==null || CarNo_TextField.getText().length()==0){
                //如果未输入车牌号码
                error_NullCarNo();
                return;
            }
            String regex = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";
            if(Pattern.compile(regex).matcher(CarNo_TextField.getText()).matches()==false){
                //车牌号码输入错误
                error_WrongCarNo();
                return;
            }
            //如果车位状态改为"已销售"或"已出租"
            //修改数据库中车位数据("已销售"或"已出租")
            editParkingInfoToSQL_YesSell();
            //刷新"业主信息管理 - 车位信息"窗口的TableView
            flush_TableView();
            //关闭"业主信息管理 - 车位信息 - 修改"窗口
            close_Windows();
            return;
        }
        if(PState_ChoiceBox.getSelectionModel().getSelectedItem().equals("未销售")){
            //如果车位状态改为"未销售"
            //修改数据库中车位数据("未销售")
            editParkingInfoToSQL_NoSell();
            //刷新"业主信息管理 - 车位信息"窗口的TableView
            flush_TableView();
            //关闭"业主信息管理 - 车位信息 - 修改"窗口
            close_Windows();
            return;
        }
    }
    public void click_CancelButton(){
        //关闭"业主信息管理 - 车位信息 - 修改"窗口
        close_Windows();
    }
    void changePState_ChoiceBox(Number newValue){
        //车位销售情况选择框变更监听
        if (newValue.intValue() == 1) {
            //选择第2项"未销售"
            CarNo_TextField.setText("");
            CarNo_TextField.setDisable(true);
        }
        else {
            CarNo_TextField.setDisable(false);
        }
    }
    void error_NullCarNo(){
        //未输入车牌号码的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("车牌号码不能为空，请输入车牌号码！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_WrongCarNo(){
        //车牌号码输入错误的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("检测到车牌号码输入错误，请输入正确的车牌号码！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void editParkingInfoToSQL_YesSell(){
        //修改数据库中车位数据("已销售"或"已出租")
        query = "UPDATE Parking_Info SET PState=\'" + PState_ChoiceBox.getSelectionModel().getSelectedItem() + "\',CarNo=\'" + CarNo_TextField.getText() + "\' WHERE PNo=\'" + PNo_TextField.getText() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void editParkingInfoToSQL_NoSell(){
        //修改数据库中车位数据("未销售")
        query = "UPDATE Parking_Info SET PState=\'未销售\',CarNo=NULL,ONo=NULL WHERE PNo=\'" + PNo_TextField.getText() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void flush_TableView(){
        //刷新"业主信息管理 - 车位信息"窗口的TableView
        Controller_OwnerEditRecord controller_ownerEditRecord=(Controller_OwnerEditRecord) StageManager.CONTROLLER.get("Controller_OwnerEditRecord");
        controller_ownerEditRecord.ParkingTableView_List.clear();
        controller_ownerEditRecord.showParkingTableView();
    }
    void close_Windows(){
        //关闭"业主信息管理 - 车位信息 - 修改"窗口
        //remove"业主信息管理 - 车位信息 - 修改"窗口和其控制器
        StageManager.STAGE.remove("Stage_OwnerEditRecord_EditParking");
        StageManager.CONTROLLER.remove("Controller_OwnerEditRecord_EditParking");
        //关闭窗口
        Stage stage = (Stage)Confirm_Button.getScene().getWindow();
        stage.close();
    }
}