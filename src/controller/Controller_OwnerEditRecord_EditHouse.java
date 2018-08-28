package controller;

import util.SQL_Connect;
import util.StageManager;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.ResultSet;

public class Controller_OwnerEditRecord_EditHouse {
    public TextField HNo_TextField;
    public ChoiceBox HState_ChoiceBox;
    public Button Confirm_Button,Cancel_Button;
    //数据库代码以及返回结果
    String query;
    ResultSet result;
    public void setHNo(String HNo){
        HNo_TextField.setText(HNo);
    }
    public void setHState(String HState){
        HState_ChoiceBox.setValue(HState);
    }
    public void initialize(){
        //初始化
        //将"业主信息管理 - 房屋信息 - 修改"控制器保存到map中
        StageManager.CONTROLLER.put("Controller_OwnerEditRecord_EditHouse", this);
        //ChoiceBox加入选择项
        HState_ChoiceBox.setItems(FXCollections.observableArrayList(
                "已销售", "未销售", "待入住")
        );
        //HNo_TextField设置不可用
        HNo_TextField.setDisable(true);
    }
    public void click_ConfirmButton(){
        if(HState_ChoiceBox.getSelectionModel().getSelectedItem().equals("已销售") || HState_ChoiceBox.getSelectionModel().getSelectedItem().equals("待入住")){
            //如果房屋状态改为"已销售"或"待入住"
            //修改数据库中房屋数据("已销售"或"待入住")
            editHouseInfoToSQL_YesSell();
            //刷新"业主信息管理 - 房屋信息"窗口的TableView
            flush_TableView();
            //关闭"业主信息管理 - 房屋信息 - 修改"窗口
            close_Windows();
            return;
        }
        if(HState_ChoiceBox.getSelectionModel().getSelectedItem().equals("未销售")){
            //如果房屋状态改为"未销售"
            //修改数据库中房屋数据("未销售")
            editHouseInfoToSQL_NoSell();
            //刷新"业主信息管理 - 房屋信息"窗口的TableView
            flush_TableView();
            //关闭"业主信息管理 - 房屋信息 - 修改"窗口
            close_Windows();
            return;
        }
    }
    public void click_CancelButton(){
        //关闭"业主信息管理 - 房屋信息 - 修改"窗口
        close_Windows();
    }
    void editHouseInfoToSQL_YesSell(){
        //修改数据库中房屋数据("已销售"或"待入住")
        query = "UPDATE House_Info SET HState=\'" + HState_ChoiceBox.getSelectionModel().getSelectedItem() + "\' WHERE HNo=\'" + HNo_TextField.getText() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void editHouseInfoToSQL_NoSell(){
        //修改数据库中房屋数据("未销售")
        query = "UPDATE House_Info SET HState=\'未销售\',ONo=NULL WHERE HNo=\'" + HNo_TextField.getText() + "\'";
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
        //关闭"业主信息管理 - 房屋信息 - 修改"窗口
        //remove"业主信息管理 - 房屋信息 - 修改"窗口和其控制器
        StageManager.STAGE.remove("Stage_OwnerEditRecord_EditHouse");
        StageManager.CONTROLLER.remove("Controller_OwnerEditRecord_EditHouse");
        //关闭窗口
        Stage stage = (Stage)Confirm_Button.getScene().getWindow();
        stage.close();
    }
}