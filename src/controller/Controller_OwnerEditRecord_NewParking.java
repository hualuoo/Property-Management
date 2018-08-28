package controller;

import util.SQL_Connect;
import util.StageManager;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.util.regex.Pattern;

public class Controller_OwnerEditRecord_NewParking {
    public String ONo;
    public TextField PNo_TextField,CarNo_TextField;
    public ChoiceBox PState_ChoiceBox;
    public Button Confirm_Button,Cancel_Button;
    //数据库代码以及返回结果
    String query;
    ResultSet result;
    public void setONo(String ONo){
        this.ONo = ONo;
    }
    public void initialize(){
        //初始化
        //将"业主信息管理 - 车位信息 - 新增"控制器保存到map中
        StageManager.CONTROLLER.put("Controller_OwnerEditRecord_NewParking", this);
        //ChoiceBox加入选择项
        PState_ChoiceBox.setItems(FXCollections.observableArrayList(
                "已销售", "已出租")
        );
    }
    public void click_ConfirmButton(){
        if (PNo_TextField.getText()==null || PNo_TextField.getText().length()==0){
            //如果未输入车位编号
            error_NullPNo();
            return;
        }
        if (PState_ChoiceBox.getSelectionModel().getSelectedItem()==null || PState_ChoiceBox.getSelectionModel().getSelectedItem().toString().length()==0){
            //如果未选择车位状态
            error_NullPState();
            return;
        }
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
        query = "SELECT * FROM Parking_Info WHERE PNo=\'" + PNo_TextField.getText() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        //ResultSet的next方法需要try-catch输出报错
        try {
            if (result.next() == false) {
                //如果数据库查不到车位编号
                error_CantFindPNo();
                return;
            }
            if (result.getString("PState").trim().equals("已销售") || result.getString("PState").trim().equals("已出租")){
                //如果车位已销售或已出租
                error_ParkingIsSell(result.getString("PState").trim());
                return;
            }
            //修改数据库中车位数据
            editParkingInfoToSQL();
            //刷新"业主信息管理 - 车位信息"窗口的TableView
            flush_TableView();
            //关闭"业主信息管理 - 车位信息 - 新增"窗口
            close_Windows();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_CancelButton(){
        //关闭"业主信息管理 - 车位信息 - 新增"窗口
        close_Windows();
    }
    void error_NullPNo(){
        //未输入车位编号时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("车位编号不能为空，请输入车位编号！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullPState(){
        //未选择车位状态时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您未选择车位状态，请选择车位状态！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
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
    void error_CantFindPNo(){
        //未查到车位编号时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("搜索不到编号为" + PNo_TextField.getText() + "的车位信息！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_ParkingIsSell(String PState){
        //车位已销售或已出租的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("编号为" + PNo_TextField.getText() + "的车位" + PState + "，您只能添加未销售的车位！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void editParkingInfoToSQL(){
        //修改数据库中车位数据
        query = "UPDATE Parking_Info SET PState=\'" + PState_ChoiceBox.getSelectionModel().getSelectedItem() + "\',CarNo=\'" + CarNo_TextField.getText() + "\',ONo=" + ONo + " WHERE PNo=\'" + PNo_TextField.getText() + "\'";
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
        //关闭"业主信息管理 - 车位信息 - 新增"窗口
        //remove"业主信息管理 - 车位信息 - 新增"窗口和其控制器
        StageManager.STAGE.remove("Stage_OwnerEditRecord_NewParking");
        StageManager.CONTROLLER.remove("Controller_OwnerEditRecord_NewParking");
        //关闭窗口
        Stage stage = (Stage)Confirm_Button.getScene().getWindow();
        stage.close();
    }
}