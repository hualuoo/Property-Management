package controller;

import application.Main;
import data.Data_OwnerTable;
import data.Data_RepairTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import util.SQL_Connect;
import util.StageManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.sql.ResultSet;

public class Controller_OwnerMain {
    //操作员Label
    public Label LoginUser_Label;
    //业主信息TableView组件
    /*业主信息    ONo业主编号    OName业主姓名    OSex业主性别    OTel业主电话    OID业主身份证号码    ONote业主备注*/
    public TableView<Data_OwnerTable> Owner_TableView;
    public TableColumn<Data_OwnerTable ,String> ONo_TableColumn;
    public TableColumn<Data_OwnerTable ,String> OName_TableColumn;
    public TableColumn<Data_OwnerTable ,String> OSex_TableColumn;
    public TableColumn<Data_OwnerTable ,String> OTel_TableColumn;
    public TableColumn<Data_OwnerTable ,String> OID_TableColumn;
    public TableColumn<Data_OwnerTable ,String> ONote_TableColumn;
    public TableColumn<Data_OwnerTable ,String> HouseCount_TableColumn;
    public TableColumn<Data_OwnerTable ,String> ParkingCount_TableColumn;
    ObservableList<Data_OwnerTable> OwnerTableView_List = FXCollections.observableArrayList();
    //数据库代码以及返回结果
    String query,query2,query3;
    ResultSet result,result2,result3;
    //计数有多少条业主信息
    int count;
    public void initialize() {
        //初始化
        //将"报修单管理-主界面"控制器保存到map中
        StageManager.CONTROLLER.put("Controller_OwnerMain", this);
        //显示操作员用户名
        LoginUser_Label.setText("操作员：" + Main.loginUser);
        //设置TableView数据来自ObservableList
        Owner_TableView.setItems(OwnerTableView_List);
        //将每个TableColumn列分别与对应的Data的get方法绑定
        ONo_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getONo());
        OName_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getOName());
        OSex_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getOSex());
        OTel_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getOTel());
        OID_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getOID());
        ONote_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getONote());
        HouseCount_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getHouseCount());
        ParkingCount_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getParkingCount());
        //从数据库读取数据并显示在TableView中
        showOwnerTableView();
    }
    public void showOwnerTableView(){
        //从数据库读取数据并显示在TableView中
        //业主信息计数置0
        count = 0;
        //数据库指令
        query = "SELECT ONo,OName,OSex,OTel,OID,ONote FROM Owner_Info";
        //调用SQL方法类获取ResultSet结果
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        //ResultSet的next方法需要try-catch输出报错
        try {
            //while循环分别获取数据直到ResultSet的结尾，并new一个使用Data方法赋值的data变量
            while (result.next()){
                query2 = "SELECT COUNT(*) HouseCount FROM House_Info WHERE ONo=" + result.getString("ONo");
                SQL_Connect sql_connect2 = new SQL_Connect();
                result2 = sql_connect2.sql_Query(query2);
                result2.next();
                OwnerTableView_List.add(new Data_OwnerTable(result.getString("ONo"),
                        result.getString("OName"),
                        result.getString("OSex"),
                        result.getString("OTel"),
                        result.getString("OID"),
                        result.getString("ONote"),
                        result.getString(result2.getString("HouseCount")),
                        result.getString("OTel")));
                count++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_IndexToggleButton(){
        //主界面-房屋管理 界面切换
        try {
            Parent Index_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_IndexMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-房屋管理");
            Main.Login_Stage.setScene(new Scene(Index_Root, 1000, 615));
            StageManager.CONTROLLER.remove("Controller_OwnerMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_CarToggleButton(){
        //车位管理 界面切换
        try {
            Parent Car_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_CarMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-车位管理");
            Main.Login_Stage.setScene(new Scene(Car_Root, 1000, 615));
            StageManager.CONTROLLER.remove("Controller_OwnerMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_ChargeToggleButton(){
        //收费管理 界面切换
        try {
            Parent Repair_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_ChargeMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-收费管理");
            Main.Login_Stage.setScene(new Scene(Repair_Root, 1000, 615));
            StageManager.CONTROLLER.remove("Controller_OwnerMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_RepairToggleButton(){
        //报修管理 界面切换
        try {
            Parent Repair_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_RepairMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-报修管理");
            Main.Login_Stage.setScene(new Scene(Repair_Root, 1000, 615));
            StageManager.CONTROLLER.remove("Controller_OwnerMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_ComplaintToggleButton(){
        //投诉管理 界面切换
        try {
            Parent Complaint_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_ComplaintMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-投诉管理");
            Main.Login_Stage.setScene(new Scene(Complaint_Root, 1000, 615));
            StageManager.CONTROLLER.remove("Controller_OwnerMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
