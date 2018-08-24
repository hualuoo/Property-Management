import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
        query = "SELECT RNo,RSubDate,RTitle,RText,RState,RReply,RSolveDate,Repair_Info.ONo,OName,OSex,OTel,OID,ONote FROM Repair_Info LEFT JOIN Owner_Info ON Repair_Info.ONo=Owner_Info.ONo";
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
                        result.getString("OSex"),
                        result.getString("OTel"),
                        result.getString("OID"),
                        result.getString("ONote")));
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
                        result.getString("OSex"),
                        result.getString("OTel"),
                        result.getString("OID"),
                        result.getString("ONote")));
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
        try{
            Stage New_Stage;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller_IndexMain.class.getResource("GUI_RepairNewRecord.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            New_Stage = new Stage();
            New_Stage.setTitle("小区物业管理系统-维修单-新增");
            New_Stage.setScene(new Scene(page, 333, 505));

            //将"维修单管理-新增"窗口保存到map中
            StageManager.STAGE.put("New_Stage", New_Stage);
            //将"维修单管理-主界面"控制器保存到map中
            StageManager.CONTROLLER.put("Controller_RepairMain", this);

            New_Stage.getIcons().add(new Image("/image/logo.png"));
            New_Stage.setX((Main.width-333)/2);
            New_Stage.setY((Main.height-505)/2);
            New_Stage.initModality(Modality.APPLICATION_MODAL);
            New_Stage.show();
            New_Stage.setResizable(false);
            Controller_RepairNewRecord controller = loader.getController();
            controller.setDialogStage(New_Stage);
            controller.setCount(count);
            Data_RepairTable newdata_RepairTable = new Data_RepairTable("","","","","","","","","","","","","");
            controller.setdata_RepairTable(newdata_RepairTable);
            newdata_RepairTable = controller.getdata_RepairTable();
            RepairTableView_List.add(newdata_RepairTable);
            count++;

            New_Stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    RepairTableView_List.clear();
                    showRepairTableView();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_EditButton(){
        if(Repair_TableView.getSelectionModel().getSelectedIndex() < 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("小区物业管理系统");
            alert.setHeaderText("您未选择需要编辑的信息，无法编辑");
            alert.initOwner(Main.Login_Stage);
            alert.showAndWait();
            return;
        }
        try{
            Stage Edit_Stage;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller_IndexMain.class.getResource("GUI_RepairEditRecord.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Edit_Stage = new Stage();
            Edit_Stage.setTitle("小区物业管理系统-维修单-修改");
            Edit_Stage.setScene(new Scene(page, 333, 505));
            Edit_Stage.getIcons().add(new Image("/image/logo.png"));
            Edit_Stage.setX((Main.width-333)/2);
            Edit_Stage.setY((Main.height-505)/2);
            Edit_Stage.initModality(Modality.APPLICATION_MODAL);
            Edit_Stage.show();
            Edit_Stage.setResizable(false);
            Controller_RepairEditRecord controller = loader.getController();
            controller.setDialogStage(Edit_Stage);
            controller.setdata_RepairTable(Repair_TableView.getSelectionModel().getSelectedItem());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_IndexToggleButton(){
        //主界面-房屋管理界面切换
        try {
            Parent Index_Root = FXMLLoader.load(getClass().getResource("GUI_IndexMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-主界面");
            Main.Login_Stage.setScene(new Scene(Index_Root, 1000, 615));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_CarToggleButton(){
        //车辆管理界面切换
        try {
            Parent Car_Root = FXMLLoader.load(getClass().getResource("GUI_CarMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-车辆管理界面");
            Main.Login_Stage.setScene(new Scene(Car_Root, 1000, 615));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}