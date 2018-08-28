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

public class Controller_OwnerEditRecord_NewHouse {
    public String ONo;
    public TextField HNo_TextField;
    public ChoiceBox HState_ChoiceBox;
    public Button Confirm_Button,Cancel_Button;
    //数据库代码以及返回结果
    String query;
    ResultSet result;
    public void setONo(String ONo){
        this.ONo = ONo;
    }
    public void initialize(){
        //初始化
        //将"业主信息管理 - 房屋信息 - 新增"控制器保存到map中
        StageManager.CONTROLLER.put("Controller_OwnerEditRecord_NewHouse", this);
        //ChoiceBox加入选择项
        HState_ChoiceBox.setItems(FXCollections.observableArrayList(
                "已销售", "待入住")
        );
    }
    public void click_ConfirmButton(){
        if (HNo_TextField.getText()==null || HNo_TextField.getText().length()==0){
            //如果未输入房屋编号
            error_NullHNo();
            return;
        }
        if (HState_ChoiceBox.getSelectionModel().getSelectedItem()==null || HState_ChoiceBox.getSelectionModel().getSelectedItem().toString().length()==0){
            //如果未选择房屋状态
            error_NullHState();
            return;
        }
        query = "SELECT * FROM House_Info WHERE HNo=\'" + HNo_TextField.getText() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        //ResultSet的next方法需要try-catch输出报错
        try {
            if (result.next() == false) {
                //如果数据库查不到房屋编号
                error_CantFindHNo();
                return;
            }
            if (result.getString("HState").trim().equals("已销售") || result.getString("HState").trim().equals("待入住")){
                //如果房屋已销售或已入住
                error_HouseIsSell(result.getString("HState").trim());
                return;
            }
            //修改数据库中房屋数据
            editHouseInfoToSQL();
            //刷新"业主信息管理 - 房屋信息"窗口的TableView
            flush_TableView();
            //关闭"业主信息管理 - 房屋信息 - 新增"窗口
            close_Windows();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_CancelButton(){
        //关闭"业主信息管理 - 房屋信息 - 新增"窗口
        close_Windows();
    }
    void error_NullHNo(){
        //未输入房屋编号时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋编号不能为空，请输入房屋编号！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullHState(){
        //未选择房屋状态时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请选择房屋状态！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_CantFindHNo(){
        //未查到房屋编号时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("搜索不到编号为" + HNo_TextField.getText() + "的房屋信息！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_HouseIsSell(String HState){
        //房屋已销售或已入住的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("编号为" + HNo_TextField.getText() + "的房屋" + HState + "，您只能添加未销售的房屋！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void editHouseInfoToSQL(){
        //修改数据库中房屋数据
        query = "UPDATE House_Info SET HState=\'" + HState_ChoiceBox.getSelectionModel().getSelectedItem() + "\',ONo=" + ONo + " WHERE HNo=\'" + HNo_TextField.getText() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void flush_TableView(){
        //刷新"业主信息管理 - 房屋信息"窗口的TableView
        Controller_OwnerEditRecord controller_ownerEditRecord=(Controller_OwnerEditRecord) StageManager.CONTROLLER.get("Controller_OwnerEditRecord");
        controller_ownerEditRecord.HouseTableView_List.clear();
        controller_ownerEditRecord.showHouseTableView();
    }
    void close_Windows(){
        //关闭"业主信息管理 - 房屋信息 - 新增"窗口
        //remove"业主信息管理 - 房屋信息 - 新增"窗口和其控制器
        StageManager.STAGE.remove("Stage_OwnerEditRecord_NewHouse");
        StageManager.CONTROLLER.remove("Controller_OwnerEditRecord_NewHouse");
        //关闭窗口
        Stage stage = (Stage)Confirm_Button.getScene().getWindow();
        stage.close();
    }
}