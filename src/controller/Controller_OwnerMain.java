package controller;

import application.Main;
import data.Data_OwnerTable;
import data.Data_RepairTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import util.SQL_Connect;
import util.StageManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

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
    //搜索框组件
    public TextField Search_RNo_TextField,Search_RName_TextField;
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
        //TableView的双击监听
        Owner_TableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    click_EditButton();
                }
            }
        });
    }
    public void showOwnerTableView(){
        //从数据库读取数据并显示在TableView中
        //业主信息计数置0
        count = 0;
        //数据库指令
        query = "SELECT Count2.ONo,OName,OSex,OTel,OID,ONote,ISNULL(HouseCount,0) HouseCount,ISNULL(ParkingCount,0) ParkingCount\n" +
                "FROM (SELECT Owner_Info.ONo,OName,OSex,OTel,OID,ONote,ISNULL(HouseCount,0) HouseCount\n" +
                "      FROM Owner_Info LEFT JOIN (SELECT Owner_Info.ONo,COUNT(HNo) HouseCount\n" +
                "                                 FROM Owner_Info,House_Info\n" +
                "                                 WHERE Owner_Info.ONo=House_Info.ONo\n" +
                "                                 GROUP BY Owner_Info.ONo) Count1\n" +
                "      ON Owner_Info.ONo=Count1.ONo) Count2\n" +
                "LEFT JOIN (SELECT Owner_Info.ONo,COUNT(PNo) ParkingCount\n" +
                "           FROM Owner_Info,Parking_Info\n" +
                "           WHERE Owner_Info.ONo=Parking_Info.ONo\n" +
                "           GROUP BY Owner_Info.ONo) Count3\n" +
                "ON Count2.ONo=Count3.ONo";
        //调用SQL方法类获取ResultSet结果
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        //ResultSet的next方法需要try-catch输出报错
        try {
            //while循环分别获取数据直到ResultSet的结尾，并new一个使用Data方法赋值的data变量
            while (result.next()){
                OwnerTableView_List.add(new Data_OwnerTable(result.getString("ONo"),
                        result.getString("OName"),
                        result.getString("OSex"),
                        result.getString("OTel"),
                        result.getString("OID"),
                        result.getString("ONote"),
                        result.getString("HouseCount"),
                        result.getString("ParkingCount")));
                count++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_NewButton(){

    }
    public void click_EditButton(){
        //单击"编辑"按钮
        //未选择需要编辑的信息的报错
        if(Owner_TableView.getSelectionModel().getSelectedIndex() < 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("小区物业管理系统");
            alert.setHeaderText("您未选择需要编辑的信息，无法编辑");
            alert.initOwner(Main.Login_Stage);
            alert.showAndWait();
            return;
        }
        //FXMLLoader的load方法需要try-catch输出报错
        try{
            //创建"业主信息管理-编辑"窗口
            Stage Stage_OwnerEditRecord;
            //加载FXML窗口
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller_IndexMain.class.getResource("/GUI/GUI_OwnerEditRecord.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage_OwnerEditRecord = new Stage();
            Stage_OwnerEditRecord.setTitle("小区物业管理系统-业主信息-修改");
            Stage_OwnerEditRecord.setScene(new Scene(page, 666, 505));
            Stage_OwnerEditRecord.getIcons().add(new Image("/image/logo.png"));
            Stage_OwnerEditRecord.setX((Main.width-666)/2);
            Stage_OwnerEditRecord.setY((Main.height-505)/2);
            Stage_OwnerEditRecord.initModality(Modality.APPLICATION_MODAL);
            Stage_OwnerEditRecord.setResizable(false);
            Stage_OwnerEditRecord.show();
            //将"业主信息管理-修改"窗口保存到map中
            StageManager.STAGE.put("Stage_OwnerEditRecord", Stage_OwnerEditRecord);
            //从map调取"业主信息管理-修改"控制器并调用setdata_ComplaintTable方法传投诉单数据
            Controller_OwnerEditRecord controller_ownerEditRecord=(Controller_OwnerEditRecord) StageManager.CONTROLLER.get("Controller_OwnerEditRecord");
            controller_ownerEditRecord.setdata_ownerTable(Owner_TableView.getSelectionModel().getSelectedItem());
            //监听"业主信息-修改"窗口如果按窗口右上角X退出，remove"投诉单管理-修改"窗口和其控制器
            Stage_OwnerEditRecord.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    OwnerTableView_List.clear();
                    showOwnerTableView();
                    StageManager.STAGE.remove("Stage_OwnerEditRecord");
                    StageManager.CONTROLLER.remove("Controller_OwnerEditRecord");
                }
            });
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
