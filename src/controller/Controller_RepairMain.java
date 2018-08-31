package controller;

import data.Data_HouseTable;
import data.Data_RepairTable;
import util.SQL_Connect;
import util.StageManager;
import application.Main;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Controller_RepairMain {
    //操作员Label
    public Label LoginUser_Label;
    //维修信息TableView组件
    //维修信息    RNo编号    RSubDate提交日期    RTitle标题    RText正文    RState状态    RReply回复    RSolveDate解决日期    ONo业主编号
    public TableView<Data_RepairTable> Repair_TableView;
    public TableColumn<Data_RepairTable ,String> RNo_TableColumn;
    public TableColumn<Data_RepairTable ,String> RSubDate_TableColumn;
    public TableColumn<Data_RepairTable ,String> RTitle_TableColumn;
    public TableColumn<Data_RepairTable ,String> RState_TableColumn;
    public TableColumn<Data_RepairTable ,String> ONo_TableColumn;
    public TableColumn<Data_RepairTable ,String> OName_TableColumn;
    ObservableList<Data_RepairTable> RepairTableView_List = FXCollections.observableArrayList();
    //搜索框组件
    public TextField Search_RNo_TextField,Search_OName_TextField;
    public DatePicker Search_RSubDate_DatePicker;
    public CheckBox Search_NoRepair_CheckBox,Search_YesRepair_CheckBox;
    public Button Search_Clean_Button;
    //数据库代码以及返回结果
    String query;
    ResultSet result;
    //计数有多少条维修单
    int count;
    public void initialize() {
        //初始化
        //将"报修单管理-主界面"控制器保存到map中
        StageManager.CONTROLLER.put("Controller_RepairMain", this);
        //显示操作员用户名
        LoginUser_Label.setText("操作员：" + Main.loginUser);
        //自定义日期选择器DatePicker格式为"yyyy-MM-dd"
        setDataStyle(Search_RSubDate_DatePicker);
        //设置TableView数据来自ObservableList
        Repair_TableView.setItems(RepairTableView_List);
        //将每个TableColumn列分别与对应的Data的get方法绑定
        RNo_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getRNo());
        RSubDate_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getRSubDate());
        RTitle_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getRTitle());
        RState_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getRState());
        ONo_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getONo());
        OName_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getOName());
        //从数据库读取数据并显示在TableView中
        showRepairTableView();
        //RState_TableColumn列符合条件的文字颜色改变
        RState_TableColumn.setCellFactory(new Callback<TableColumn<Data_RepairTable, String>, TableCell<Data_RepairTable, String>>() {
            public TableCell call(TableColumn param) {
                return new TableCell<Data_HouseTable, String>() {
                    ObservableValue observableValue;
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            observableValue = getTableColumn().getCellObservableValue(getIndex());
                            if (item.contains("已")) {
                                this.setTextFill(Color.GREEN);
                            }
                            else {
                                this.setTextFill(Color.RED);
                            }
                            setText(item);
                        }
                    }
                };
            }
        });
        //TableView的双击监听
        Repair_TableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    click_EditButton();
                }
            }
        });
        //搜索栏-维修单号-文本框 变动监听
        Search_RNo_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_Repair();
            }
        });
        //搜索栏-维修单提交时间-时间选择器 变动监听
        Search_RSubDate_DatePicker.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_Repair();
            }
        });
        //搜索栏-业主姓名-文本框 变动监听
        Search_OName_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_Repair();
            }
        });
        //搜索栏-未维修-复选框 变动监听
        Search_NoRepair_CheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                search_Repair();
            }
        });
        //搜索栏-已维修-复选框 变动监听
        Search_YesRepair_CheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                search_Repair();
            }
        });
    }
    public void setDataStyle(DatePicker datepicker){
        //自定义日期选择器DatePicker格式为"yyyy-MM-dd"
        String pattern = "yyyy-MM-dd";
        StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        datepicker.setConverter(converter);
    }
    public void showRepairTableView(){
        //从数据库读取数据并显示在TableView中
        //单号计数置0
        count = 0;
        //数据库指令
        query = "SELECT RNo,RSubDate,RTitle,RText,RState,RReply,RSolveDate,Repair_Info.ONo,OName,OTel FROM Repair_Info LEFT JOIN Owner_Info ON Repair_Info.ONo=Owner_Info.ONo";
        //调用SQL方法类获取ResultSet结果
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        //ResultSet的next方法需要try-catch输出报错
        try {
            //while循环分别获取数据直到ResultSet的结尾，并new一个使用Data方法赋值的data变量
            while (result.next()){
                RepairTableView_List.add(new Data_RepairTable(result.getString("RNo"),
                        result.getString("RSubDate"),
                        result.getString("RTitle"),
                        result.getString("RText"),
                        result.getString("RState"),
                        result.getString("RReply"),
                        result.getString("RSolveDate"),
                        result.getString("ONo"),
                        result.getString("OName"),
                        result.getString("OTel")));
                count++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void search_Repair(){
        //搜索栏输入框变动调用的搜索方法
        //清空ObservableList内数据
        RepairTableView_List.clear();
        if(Search_RSubDate_DatePicker.getValue()==null && Search_NoRepair_CheckBox.isSelected() && Search_YesRepair_CheckBox.isSelected()){
            //当搜索栏-提交日志-时间选择器为空 搜索栏-未维修-复选框勾选 搜索栏-已维修-复选框勾选
            query = "SELECT RNo,RSubDate,RTitle,RText,RState,RReply,RSolveDate,Repair_Info.ONo,OName,OSex,OTel,OID,ONote FROM Repair_Info LEFT JOIN Owner_Info ON Repair_Info.ONo=Owner_Info.ONo WHERE " +
                    "RNo LIKE \'%" + Search_RNo_TextField.getText() + "%\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_RSubDate_DatePicker.getValue()!=null && Search_NoRepair_CheckBox.isSelected() && Search_YesRepair_CheckBox.isSelected()){
            //当搜索栏-提交日志-时间选择器不为空 搜索栏-未维修-复选框勾选 搜索栏-已维修-复选框勾选
            query = "SELECT RNo,RSubDate,RTitle,RText,RState,RReply,RSolveDate,Repair_Info.ONo,OName,OSex,OTel,OID,ONote FROM Repair_Info LEFT JOIN Owner_Info ON Repair_Info.ONo=Owner_Info.ONo WHERE " +
                    "RNo LIKE \'%" + Search_RNo_TextField.getText() + "%\' AND " +
                    "RSubDate=\'" + Search_RSubDate_DatePicker.getValue() + "\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_RSubDate_DatePicker.getValue()==null && Search_NoRepair_CheckBox.isSelected()==false && Search_YesRepair_CheckBox.isSelected()){
            //当搜索栏-提交日志-时间选择器为空 搜索栏-未维修-复选框未勾选 搜索栏-已维修-复选框勾选
            query = "SELECT RNo,RSubDate,RTitle,RText,RState,RReply,RSolveDate,Repair_Info.ONo,OName,OSex,OTel,OID,ONote FROM Repair_Info LEFT JOIN Owner_Info ON Repair_Info.ONo=Owner_Info.ONo WHERE " +
                    "RNo LIKE \'%" + Search_RNo_TextField.getText() + "%\' AND " +
                    "RState =\'已维修\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_RSubDate_DatePicker.getValue()!=null && Search_NoRepair_CheckBox.isSelected()==false && Search_YesRepair_CheckBox.isSelected()){
            //当搜索栏-提交日志-时间选择器不为空 搜索栏-未维修-复选框未勾选 搜索栏-已维修-复选框勾选
            query = "SELECT RNo,RSubDate,RTitle,RText,RState,RReply,RSolveDate,Repair_Info.ONo,OName,OSex,OTel,OID,ONote FROM Repair_Info LEFT JOIN Owner_Info ON Repair_Info.ONo=Owner_Info.ONo WHERE " +
                    "RNo LIKE \'%" + Search_RNo_TextField.getText() + "%\' AND " +
                    "RSubDate=\'" + Search_RSubDate_DatePicker.getValue() + "\' AND " +
                    "RState =\'已维修\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_RSubDate_DatePicker.getValue()==null && Search_NoRepair_CheckBox.isSelected() && Search_YesRepair_CheckBox.isSelected()==false){
            //当搜索栏-提交日志-时间选择器为空 搜索栏-未维修-复选框勾选 搜索栏-已维修-复选框未勾选
            query = "SELECT RNo,RSubDate,RTitle,RText,RState,RReply,RSolveDate,Repair_Info.ONo,OName,OSex,OTel,OID,ONote FROM Repair_Info LEFT JOIN Owner_Info ON Repair_Info.ONo=Owner_Info.ONo WHERE " +
                    "RNo LIKE \'%" + Search_RNo_TextField.getText() + "%\' AND " +
                    "RState =\'未维修\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_RSubDate_DatePicker.getValue()!=null && Search_NoRepair_CheckBox.isSelected() && Search_YesRepair_CheckBox.isSelected()==false){
            //当搜索栏-提交日志-时间选择器不为空 搜索栏-未维修-复选框勾选 搜索栏-已维修-复选框未勾选
            query = "SELECT RNo,RSubDate,RTitle,RText,RState,RReply,RSolveDate,Repair_Info.ONo,OName,OSex,OTel,OID,ONote FROM Repair_Info LEFT JOIN Owner_Info ON Repair_Info.ONo=Owner_Info.ONo WHERE " +
                    "RNo LIKE \'%" + Search_RNo_TextField.getText() + "%\' AND " +
                    "RSubDate=\'" + Search_RSubDate_DatePicker.getValue() + "\' AND " +
                    "RState =\'未维修\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_NoRepair_CheckBox.isSelected()==false && Search_YesRepair_CheckBox.isSelected()==false){
            //当搜索栏-未维修-复选框、搜索栏-已维修-复选框均未勾选
            return;
        }
        //调用SQL方法类获取ResultSet结果
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        //ResultSet的next方法需要try-catch输出报错
        try {
            while (result.next()){
                //while循环分别获取数据直到ResultSet的结尾，并new一个使用Data方法赋值的data变量
                RepairTableView_List.add(new Data_RepairTable(result.getString("RNo"),
                        result.getString("RSubDate"),
                        result.getString("RTitle"),
                        result.getString("RText"),
                        result.getString("RState"),
                        result.getString("RReply"),
                        result.getString("RSolveDate"),
                        result.getString("ONo"),
                        result.getString("OName"),
                        result.getString("OTel")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_SearchCleanButton(){
        //单击搜索栏-清空-按钮
        Search_RNo_TextField.setText("");
        Search_RSubDate_DatePicker.setValue(null);
        Search_NoRepair_CheckBox.setSelected(true);
        Search_YesRepair_CheckBox.setSelected(true);
        Search_OName_TextField.setText("");
    }
    public void click_NewButton(){
        //单击"新建"按钮
        //FXMLLoader的load方法需要try-catch输出报错
        try{
            //创建"维修单管理-新增"窗口
            Stage Stage_RepairNewRecord;
            //加载FXML窗口
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller_IndexMain.class.getResource("/GUI/GUI_RepairNewRecord.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage_RepairNewRecord = new Stage();
            Stage_RepairNewRecord.setTitle("小区物业管理系统-维修单-新增");
            Stage_RepairNewRecord.setScene(new Scene(page, 333, 505));
            Stage_RepairNewRecord.getIcons().add(new Image("/image/logo.png"));
            Stage_RepairNewRecord.setX((Main.width-333)/2);
            Stage_RepairNewRecord.setY((Main.height-505)/2);
            Stage_RepairNewRecord.initModality(Modality.APPLICATION_MODAL);
            Stage_RepairNewRecord.setResizable(false);
            Stage_RepairNewRecord.show();
            //将"维修单管理-新增"窗口保存到map中
            StageManager.STAGE.put("Stage_RepairNewRecord", Stage_RepairNewRecord);
            //从map调取"维修单管理-新增"控制器并调用setCount方法传单号
            Controller_RepairNewRecord controller_repairNewRecord = (Controller_RepairNewRecord) StageManager.CONTROLLER.get("Controller_RepairNewRecord");
            controller_repairNewRecord.setCount(count);
            //监听"维修单管理-新增"窗口如果按窗口右上角X退出，remove"维修单管理-新增"窗口和其控制器
            Stage_RepairNewRecord.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    StageManager.STAGE.remove("Stage_RepairNewRecord");
                    StageManager.CONTROLLER.remove("Controller_RepairNewRecord");
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_EditButton(){
        //单击"编辑"按钮
        //未选择需要编辑的信息的报错
        if(Repair_TableView.getSelectionModel().getSelectedIndex() < 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("小区物业管理系统");
            alert.setHeaderText("您未选择需要编辑的信息，无法编辑");
            alert.initOwner(Main.Login_Stage);
            alert.showAndWait();
            return;
        }
        //FXMLLoader的load方法需要try-catch输出报错
        try{
            //创建"维修单管理-编辑"窗口
            Stage Stage_RepairEditRecord;
            //加载FXML窗口
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller_IndexMain.class.getResource("/GUI/GUI_RepairEditRecord.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage_RepairEditRecord = new Stage();
            Stage_RepairEditRecord.setTitle("小区物业管理系统-维修单-修改");
            Stage_RepairEditRecord.setScene(new Scene(page, 333, 505));
            Stage_RepairEditRecord.getIcons().add(new Image("/image/logo.png"));
            Stage_RepairEditRecord.setX((Main.width-333)/2);
            Stage_RepairEditRecord.setY((Main.height-505)/2);
            Stage_RepairEditRecord.initModality(Modality.APPLICATION_MODAL);
            Stage_RepairEditRecord.setResizable(false);
            Stage_RepairEditRecord.show();
            //将"投诉单管理-修改"窗口保存到map中
            StageManager.STAGE.put("Stage_RepairEditRecord", Stage_RepairEditRecord);
            //从map调取"投诉单管理-修改"控制器并调用setdata_ComplaintTable方法传投诉单数据
            Controller_RepairEditRecord controller_repairEditRecord=(Controller_RepairEditRecord) StageManager.CONTROLLER.get("Controller_RepairEditRecord");
            controller_repairEditRecord.setdata_RepairTable(Repair_TableView.getSelectionModel().getSelectedItem());
            //监听"投诉单管理-修改"窗口如果按窗口右上角X退出，remove"投诉单管理-修改"窗口和其控制器
            Stage_RepairEditRecord.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    StageManager.STAGE.remove("Stage_RepairEditRecord");
                    StageManager.CONTROLLER.remove("Controller_RepairEditRecord");
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
    public void click_OwnerToggleButton(){
        //业主管理 界面切换
        try {
            Parent Family_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_OwnerMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-业主管理界面");
            Main.Login_Stage.setScene(new Scene(Family_Root, 1000, 615));
            StageManager.CONTROLLER.remove("Controller_RepairMain");
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
            StageManager.CONTROLLER.remove("Controller_RepairMain");
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
            StageManager.CONTROLLER.remove("Controller_RepairMain");
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
            StageManager.CONTROLLER.remove("Controller_RepairMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}