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

public class Controller_ComplaintMain {
    //操作员Label
    public Label LoginUser_Label;
    //投诉信息TableView组件
    //投诉信息    CNo编号    RSubDate提交日期    RTitle标题    RText正文    RState状态    RReply回复    RSolveDate解决日期    ONo业主编号
    public TableView<Data_ComplaintTable> Complaint_TableView;
    public TableColumn<Data_ComplaintTable ,String> CNo_TableColumn;
    public TableColumn<Data_ComplaintTable ,String> CSubDate_TableColumn;
    public TableColumn<Data_ComplaintTable ,String> CTitle_TableColumn;
    public TableColumn<Data_ComplaintTable ,String> CState_TableColumn;
    public TableColumn<Data_ComplaintTable ,String> ONo_TableColumn;
    public TableColumn<Data_ComplaintTable ,String> OName_TableColumn;
    ObservableList<Data_ComplaintTable> ComplaintTableView_List = FXCollections.observableArrayList();
    //搜索框组件
    public TextField Search_CNo_TextField,Search_OName_TextField;
    public DatePicker Search_CSubDate_DatePicker;
    public CheckBox Search_NoComplaint_CheckBox,Search_YesComplaint_CheckBox;
    public Button Search_Clean_Button;
    //数据库代码以及返回结果
    String query;
    ResultSet result;
    //计数有多少条投诉单
    int count;
    public void initialize() {
        //将"投诉单管理-主界面"控制器保存到map中
        StageManager.CONTROLLER.put("Controller_ComplaintMain", this);
        //初始化
        //显示操作员用户名
        LoginUser_Label.setText("操作员：" + Main.loginUser);
        //自定义日期选择器DatePicker格式为"yyyy-MM-dd"
        setDataStyle(Search_CSubDate_DatePicker);
        //设置TableView数据来自ObservableList
        Complaint_TableView.setItems(ComplaintTableView_List);
        //将每个TableColumn列分别与对应的Data的get方法绑定
        CNo_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getCNo());
        CSubDate_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getCSubDate());
        CTitle_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getCTitle());
        CState_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getCState());
        ONo_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getONo());
        OName_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getOName());
        //从数据库读取数据并显示在TableView中
        showComplaintTableView();
        //TableView的双击监听
        Complaint_TableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    click_EditButton();
                }
            }
        });
        //搜索栏-投诉单号-文本框 变动监听
        Search_CNo_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_Complaint();
            }
        });
        //搜索栏-投诉单提交时间-时间选择器 变动监听
        Search_CSubDate_DatePicker.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_Complaint();
            }
        });
        //搜索栏-业主姓名-文本框 变动监听
        Search_OName_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_Complaint();
            }
        });
        //搜索栏-未处理-复选框 变动监听
        Search_NoComplaint_CheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                search_Complaint();
            }
        });
        //搜索栏-已处理-复选框 变动监听
        Search_YesComplaint_CheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                search_Complaint();
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
    public void showComplaintTableView(){
        //从数据库读取数据并显示在TableView中
        //单号计数置0
        count = 0;
        //数据库指令
        query = "SELECT CNo,CSubDate,CTitle,CText,CState,CReply,CSolveDate,Complaint_Info.ONo,OName,OTel FROM Complaint_Info LEFT JOIN Owner_Info ON Complaint_Info.ONo=Owner_Info.ONo";
        //调用SQL方法类获取ResultSet结果
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        //ResultSet的next方法需要try-catch输出报错
        try {
            //while循环分别获取数据直到ResultSet的结尾，并new一个使用Data方法赋值的data变量
            while (result.next()){
                ComplaintTableView_List.add(new Data_ComplaintTable(result.getString("CNo"),
                        result.getString("CSubDate"),
                        result.getString("CTitle"),
                        result.getString("CText"),
                        result.getString("CState"),
                        result.getString("CReply"),
                        result.getString("CSolveDate"),
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
    public void search_Complaint(){
        //搜索栏输入框变动调用的搜索方法
        //清空ObservableList内数据
        ComplaintTableView_List.clear();
        if(Search_CSubDate_DatePicker.getValue()==null && Search_NoComplaint_CheckBox.isSelected() && Search_YesComplaint_CheckBox.isSelected()){
            //当搜索栏-提交日志-时间选择器为空 搜索栏-未处理-复选框勾选 搜索栏-已处理-复选框勾选
            query = "SELECT CNo,CSubDate,CTitle,CText,CState,CReply,CSolveDate,Complaint_Info.ONo,OName,OTel FROM Complaint_Info LEFT JOIN Owner_Info ON Complaint_Info.ONo=Owner_Info.ONo WHERE " +
                    "CNo LIKE \'%" + Search_CNo_TextField.getText() + "%\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_CSubDate_DatePicker.getValue()!=null && Search_NoComplaint_CheckBox.isSelected() && Search_YesComplaint_CheckBox.isSelected()){
            //当搜索栏-提交日志-时间选择器不为空 搜索栏-未处理-复选框勾选 搜索栏-已处理-复选框勾选
            query = "SELECT CNo,CSubDate,CTitle,CText,CState,CReply,CSolveDate,Complaint_Info.ONo,OName,OTel FROM Complaint_Info LEFT JOIN Owner_Info ON Complaint_Info.ONo=Owner_Info.ONo WHERE " +
                    "CNo LIKE \'%" + Search_CNo_TextField.getText() + "%\' AND " +
                    "CSubDate=\'" + Search_CSubDate_DatePicker.getValue() + "\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_CSubDate_DatePicker.getValue()==null && Search_NoComplaint_CheckBox.isSelected()==false && Search_YesComplaint_CheckBox.isSelected()){
            //当搜索栏-提交日志-时间选择器为空 搜索栏-未处理-复选框未勾选 搜索栏-已处理-复选框勾选
            query = "SELECT CNo,CSubDate,CTitle,CText,CState,CReply,CSolveDate,Complaint_Info.ONo,OName,OTel FROM Complaint_Info LEFT JOIN Owner_Info ON Complaint_Info.ONo=Owner_Info.ONo WHERE " +
                    "CNo LIKE \'%" + Search_CNo_TextField.getText() + "%\' AND " +
                    "CState =\'已处理\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_CSubDate_DatePicker.getValue()!=null && Search_NoComplaint_CheckBox.isSelected()==false && Search_YesComplaint_CheckBox.isSelected()){
            //当搜索栏-提交日志-时间选择器不为空 搜索栏-未处理-复选框未勾选 搜索栏-已处理-复选框勾选
            query = "SELECT CNo,CSubDate,CTitle,CText,CState,CReply,CSolveDate,Complaint_Info.ONo,OName,OTel FROM Complaint_Info LEFT JOIN Owner_Info ON Complaint_Info.ONo=Owner_Info.ONo WHERE " +
                    "CNo LIKE \'%" + Search_CNo_TextField.getText() + "%\' AND " +
                    "CSubDate=\'" + Search_CSubDate_DatePicker.getValue() + "\' AND " +
                    "CState =\'已处理\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_CSubDate_DatePicker.getValue()==null && Search_NoComplaint_CheckBox.isSelected() && Search_YesComplaint_CheckBox.isSelected()==false){
            //当搜索栏-提交日志-时间选择器为空 搜索栏-未处理-复选框勾选 搜索栏-已处理-复选框未勾选
            query = "SELECT CNo,CSubDate,CTitle,CText,CState,CReply,CSolveDate,Complaint_Info.ONo,OName,OTel FROM Complaint_Info LEFT JOIN Owner_Info ON Complaint_Info.ONo=Owner_Info.ONo WHERE " +
                    "CNo LIKE \'%" + Search_CNo_TextField.getText() + "%\' AND " +
                    "CState =\'未处理\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_CSubDate_DatePicker.getValue()!=null && Search_NoComplaint_CheckBox.isSelected() && Search_YesComplaint_CheckBox.isSelected()==false){
            //当搜索栏-提交日志-时间选择器不为空 搜索栏-未处理-复选框勾选 搜索栏-已处理-复选框未勾选
            query = "SELECT CNo,CSubDate,CTitle,CText,CState,CReply,CSolveDate,Complaint_Info.ONo,OName,OTel FROM Complaint_Info LEFT JOIN Owner_Info ON Complaint_Info.ONo=Owner_Info.ONo WHERE " +
                    "CNo LIKE \'%" + Search_CNo_TextField.getText() + "%\' AND " +
                    "CSubDate=\'" + Search_CSubDate_DatePicker.getValue() + "\' AND " +
                    "CState =\'未处理\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_NoComplaint_CheckBox.isSelected()==false && Search_YesComplaint_CheckBox.isSelected()==false){
            //当搜索栏-未处理-复选框、搜索栏-已处理-复选框均未勾选
            return;
        }
        //调用SQL方法类获取ResultSet结果
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        //ResultSet的next方法需要try-catch输出报错
        try {
            while (result.next()){
                //while循环分别获取数据直到ResultSet的结尾，并new一个使用Data方法赋值的data变量
                ComplaintTableView_List.add(new Data_ComplaintTable(result.getString("CNo"),
                        result.getString("CSubDate"),
                        result.getString("CTitle"),
                        result.getString("CText"),
                        result.getString("CState"),
                        result.getString("CReply"),
                        result.getString("CSolveDate"),
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
        //单击搜索栏"清空"按钮
        Search_CNo_TextField.setText("");
        Search_CSubDate_DatePicker.setValue(null);
        Search_NoComplaint_CheckBox.setSelected(true);
        Search_YesComplaint_CheckBox.setSelected(true);
        Search_OName_TextField.setText("");
    }
    public void click_NewButton(){
        //单击"新建"按钮
        //FXMLLoader的load方法需要try-catch输出报错
        try{
            //创建"投诉单管理-新增"窗口
            Stage Stage_ComplaintNewRecord;
            //加载FXML窗口
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller_IndexMain.class.getResource("GUI_ComplaintNewRecord.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage_ComplaintNewRecord = new Stage();
            Stage_ComplaintNewRecord.setTitle("小区物业管理系统-投诉单-新增");
            Stage_ComplaintNewRecord.setScene(new Scene(page, 333, 505));
            Stage_ComplaintNewRecord.getIcons().add(new Image("/image/logo.png"));
            Stage_ComplaintNewRecord.setX((Main.width-333)/2);
            Stage_ComplaintNewRecord.setY((Main.height-505)/2);
            Stage_ComplaintNewRecord.initModality(Modality.APPLICATION_MODAL);
            Stage_ComplaintNewRecord.setResizable(false);
            Stage_ComplaintNewRecord.show();
            //将"投诉单管理-新增"窗口保存到map中
            StageManager.STAGE.put("Stage_ComplaintNewRecord", Stage_ComplaintNewRecord);
            //从map调取"投诉单管理-新增"控制器并调用setCount方法传单号
            Controller_ComplaintNewRecord controller_complaintNewRecord=(Controller_ComplaintNewRecord) StageManager.CONTROLLER.get("Controller_ComplaintNewRecord");
            controller_complaintNewRecord.setCount(count);
            //监听"投诉单管理-新增"窗口如果按窗口右上角X退出，remove"投诉单管理-新增"窗口和其控制器
            Stage_ComplaintNewRecord.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    StageManager.STAGE.remove("Stage_ComplaintNewRecord");
                    StageManager.CONTROLLER.remove("Controller_ComplaintNewRecord");
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
        if(Complaint_TableView.getSelectionModel().getSelectedIndex() < 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("小区物业管理系统");
            alert.setHeaderText("您未选择需要编辑的信息，无法编辑");
            alert.initOwner(Main.Login_Stage);
            alert.showAndWait();
            return;
        }
        //FXMLLoader的load方法需要try-catch输出报错
        try{
            //创建"投诉单管理-编辑"窗口
            Stage Stage_ComplaintEditRecord;
            //加载FXML窗口
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller_IndexMain.class.getResource("GUI_ComplaintEditRecord.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage_ComplaintEditRecord = new Stage();
            Stage_ComplaintEditRecord.setTitle("小区物业管理系统-投诉单-修改");
            Stage_ComplaintEditRecord.setScene(new Scene(page, 333, 505));
            Stage_ComplaintEditRecord.getIcons().add(new Image("/image/logo.png"));
            Stage_ComplaintEditRecord.setX((Main.width-333)/2);
            Stage_ComplaintEditRecord.setY((Main.height-505)/2);
            Stage_ComplaintEditRecord.initModality(Modality.APPLICATION_MODAL);
            Stage_ComplaintEditRecord.setResizable(false);
            Stage_ComplaintEditRecord.show();
            //将"投诉单管理-修改"窗口保存到map中
            StageManager.STAGE.put("Stage_ComplaintEditRecord", Stage_ComplaintEditRecord);
            //从map调取"投诉单管理-修改"控制器并调用setdata_ComplaintTable方法传投诉单数据
            Controller_ComplaintEditRecord controller_complaintEditRecord=(Controller_ComplaintEditRecord) StageManager.CONTROLLER.get("Controller_ComplaintEditRecord");
            controller_complaintEditRecord.setdata_ComplaintTable(Complaint_TableView.getSelectionModel().getSelectedItem());
            //监听"投诉单管理-修改"窗口如果按窗口右上角X退出，remove"投诉单管理-修改"窗口和其控制器
            Stage_ComplaintEditRecord.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    StageManager.STAGE.remove("Stage_ComplaintEditRecord");
                    StageManager.CONTROLLER.remove("Controller_ComplaintEditRecord");
                }
            });
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
            StageManager.CONTROLLER.remove("Controller_ComplaintMain");
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
            StageManager.CONTROLLER.remove("Controller_ComplaintMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_RepairToggleButton(){
        //维修信息界面切换
        try {
            Parent Repair_Root = FXMLLoader.load(getClass().getResource("GUI_RepairMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-维修信息界面");
            Main.Login_Stage.setScene(new Scene(Repair_Root, 1000, 615));
            StageManager.CONTROLLER.remove("Controller_ComplaintMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}