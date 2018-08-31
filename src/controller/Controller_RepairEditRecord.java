package controller;

import data.Data_RepairTable;
import util.SQL_Connect;
import util.StageManager;

import javafx.util.Callback;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Controller_RepairEditRecord{
    public Data_RepairTable data_repairTable;
    //输入框组件
    public Label RNo_Label;
    public TextField RTitle_TextField,ONo_TextField,OName_TextField,OTel_TextField;
    public DatePicker RSubDate_DatePicker,RSolveDate_DatePicker;
    public TextArea RText_TextArea,RReply_TextArea;
    public RadioButton NoRepair_RadioButton,YesRepair_RadioButton;
    public Button Search_Button,Confirm_Button,Back_Button;
    //数据库代码以及返回结果
    String query;
    ResultSet result;
    //检查是否已搜索业主信息
    int check_Search;
    public void setdata_RepairTable(Data_RepairTable data_repairTable){
        //传参房屋数据
        this.data_repairTable = data_repairTable;
        //维修情况获取
        if(data_repairTable.getRState().get().equals("未维修")){
            NoRepair_RadioButton.setSelected(true);
            //设置初始数据(未维修)
            setDefaultData_NoRepair();
        }
        else {
            //设置初始数据(已维修)
            YesRepair_RadioButton.setSelected(true);
            setDefaultData_YesRepair();
        }
    }
    public void initialize() {
        //将"维修单管理-编辑"控制器保存到map中
        StageManager.CONTROLLER.put("Controller_RepairEditRecord", this);
        //默认已获取业主信息
        check_Search = 1;
        //维修情况单选框监听
        NoRepair_RadioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            //维修情况(未选择)单选框监听
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                select_NoRepair(newValue);
            }
        });
        YesRepair_RadioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            //维修情况(已选择)单选框监听
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                select_YesRepair(newValue);
            }
        });
        //自定义日期选择器DatePicker格式为"yyyy-MM-dd"
        setDataStyle(RSubDate_DatePicker);
        setDataStyle(RSolveDate_DatePicker);
        //设置 开始时间选择器 只能选择 结束时间选择器 之前的日期
        //设置 结束时间选择器 只能选择 开始时间选择器 之后的日期
        setDateEndAfterBegin(RSubDate_DatePicker,RSolveDate_DatePicker);
        //文本栏变动监听
        ONo_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            //只要业主编号文字变动即需要重新点击"搜索"按钮
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals(data_repairTable.getONo().get())==false){
                    check_Search = 0;
                    OName_TextField.setText("");
                    OTel_TextField.setText("");
                }
            }
        });
    }
    void setDefaultData_NoRepair(){
        //设置初始数据(未维修)
        RNo_Label.setText("#" + data_repairTable.getRNo().get());
        RTitle_TextField.setText(data_repairTable.getRTitle().get().trim());
        LocalDate RSubDate = LocalDate.parse(data_repairTable.getRSubDate().get(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        RSubDate_DatePicker.setValue(RSubDate);
        ONo_TextField.setText(data_repairTable.getONo().get());
        OName_TextField.setText(data_repairTable.getOName().get());
        OTel_TextField.setText(data_repairTable.getOTel().get());
        RText_TextArea.setText(data_repairTable.getRText().get().trim());
    }
    void setDefaultData_YesRepair(){
        //设置初始数据(已维修)
        RNo_Label.setText("#" + data_repairTable.getRNo().get());
        RTitle_TextField.setText(data_repairTable.getRTitle().get().trim());
        LocalDate RSubDate = LocalDate.parse(data_repairTable.getRSubDate().get(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        RSubDate_DatePicker.setValue(RSubDate);
        ONo_TextField.setText(data_repairTable.getONo().get());
        OName_TextField.setText(data_repairTable.getOName().get());
        OTel_TextField.setText(data_repairTable.getOTel().get());
        RText_TextArea.setText(data_repairTable.getRText().get().trim());
        RReply_TextArea.setText(data_repairTable.getRReply().get().trim());
        LocalDate RSolveDate = LocalDate.parse(data_repairTable.getRSolveDate().get(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        RSolveDate_DatePicker.setValue(RSolveDate);
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
    public void setDateEndAfterBegin (DatePicker BeginDate,DatePicker EndDate){
        //设置 开始时间选择器 只能选择 结束时间选择器 之前的日期
        //设置 结束时间选择器 只能选择 开始时间选择器 之后的日期
        final Callback<DatePicker, DateCell> Before_EndDate =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (EndDate.getEditor().getText() != null && EndDate.getEditor().getText().length()!=0){
                                    if (item.isAfter(EndDate.getValue().plusDays(0))) {
                                        setDisable(true);
                                        setStyle("-fx-background-color: #ffc0cb;");
                                    }
                                }
                            }
                        };
                    }
                };
        final Callback<DatePicker, DateCell> After_BeginDate =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (BeginDate.getEditor().getText() != null && BeginDate.getEditor().getText().length()!=0){
                                    if (item.isBefore(BeginDate.getValue().plusDays(0))) {
                                        setDisable(true);
                                        setStyle("-fx-background-color: #ffc0cb;");
                                    }
                                }
                            }
                        };
                    }
                };
        BeginDate.setDayCellFactory(Before_EndDate);
        EndDate.setDayCellFactory(After_BeginDate);
    }
    public void select_NoRepair(Boolean newValue){
        //维修情况(未选择)单选框监听
        if (newValue){
            YesRepair_RadioButton.setSelected(false);
            RReply_TextArea.setDisable(true);
            RReply_TextArea.setText("");
            RSolveDate_DatePicker.setDisable(true);
            RSolveDate_DatePicker.setValue(null);
        }
        else {
            YesRepair_RadioButton.setSelected(true);
        }
    }
    public void select_YesRepair(Boolean newValue){
        //维修情况(已选择)单选框监听
        if (newValue){
            NoRepair_RadioButton.setSelected(false);
            RReply_TextArea.setDisable(false);
            RSolveDate_DatePicker.setDisable(false);
        }
        else {
            NoRepair_RadioButton.setSelected(true);
        }
    }
    public void click_SearchButton(){
        //单击"搜索"按钮
        //如果 业主编号-文本栏 为空，弹出未输入业主编号时的错误弹窗
        if(ONo_TextField.getText() == null || ONo_TextField.getText().length()==0){
            error_NullONo();
            return;
        }
        //数据库指令
        query = "SELECT ONo,OName,OSex,OTel,OID,ONote FROM Owner_Info WHERE ONo=" + ONo_TextField.getText().trim();
        //调用SQL方法类获取ResultSet结果
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        //ResultSet的next方法需要try-catch输出报错
        try {
            if(result.next() == false){
                //如果数据库查不到业主信息
                check_Search = 0;
                OName_TextField.setText("");
                OTel_TextField.setText("");
                error_NullOwener();
                return;
            }
            else {
                //如果数据库查到业主信息
                check_Search = 1;
                OName_TextField.setText(result.getString("OName").trim());
                OTel_TextField.setText(result.getString("OTel").trim());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_ConfirmButton() {
        //按下"确认"按钮
        //确认是否修改该保修单
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您确认是否修改该条保修单？");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            //如果选择了"否"
            return;
        }
        if (RTitle_TextField.getText() == null || RTitle_TextField.getText().length()==0){
            //如果标题为空报错
            error_NullRTitle();
            return;
        }
        if (RSubDate_DatePicker.getEditor().getText() == null || RSubDate_DatePicker.getEditor().getText().length()==0){
            //如果提交日期为空报错
            error_NullRSubDate();
            return;
        }
        if (check_Search !=1){
            //如果未单击"搜索"按钮报错
            error_NullSearch();
            return;
        }
        if (RText_TextArea.getText() == null || RText_TextArea.getText().length()==0){
            //如果内容为空报错
            error_NullRText();
            return;
        }
        if (NoRepair_RadioButton.selectedProperty().getValue()==true){
            //如果勾选了"未维修"
            //修改数据库中的数据("未维修")
            editDataToSQL_NoRepair();
            //成功修改弹窗
            succeed_Edit();
            //刷新"维修单管理-主界面"窗口的TableView
            flush_TableView();
            //关闭"维修单管理-新增"窗口
            close_Windows();
            return;
        }
        if (YesRepair_RadioButton.selectedProperty().getValue()==true){
            //如果勾选了"已维修"
            if (RReply_TextArea.getText() == null || RReply_TextArea.getText().length()==0){
                //如果回复为空报错
                error_NullRReply();
                return;
            }
            if (RSolveDate_DatePicker.getEditor().getText() == null || RSolveDate_DatePicker.getEditor().getText().length()==0){
                //如果解决日期为空报错
                error_NullRSolveDate();
                return;
            }
            //修改数据库中的数据("已维修")
            editDataToSQL_YesRepair();
            //刷新"维修单管理-主界面"窗口的TableView
            flush_TableView();
            //成功修改弹窗
            succeed_Edit();
            //关闭"维修单管理-新增"窗口
            close_Windows();
            return;
        }
    }
    public void flush_TableView(){
        //刷新"维修单管理-主界面"窗口的TableView
        Controller_RepairMain controller_repairMain = (Controller_RepairMain) StageManager.CONTROLLER.get("Controller_RepairMain");
        controller_repairMain.RepairTableView_List.clear();
        controller_repairMain.showRepairTableView();
    }
    public void click_BackButton(){
        //按下返回按钮
        close_Windows();
    }
    void editDataToSQL_NoRepair(){
        //修改数据库中的数据("未维修")
        query = "UPDATE Repair_Info SET " +
                "RSubDate=\'" + RSubDate_DatePicker.getEditor().getText() + "\'," +
                "RTitle=\'" + RTitle_TextField.getText() + "\'," +
                "RText=\'" + RText_TextArea.getText() + "\'," +
                "RState=\'未维修\'," +
                "RReply=NULL," +
                "RSolveDate=NULL," +
                "ONo=" + ONo_TextField.getText() + " " +
                "WHERE RNo=\'" + data_repairTable.getRNo().get() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void editDataToSQL_YesRepair(){
        //修改数据库中的数据("已维修")
        query = "UPDATE Repair_Info SET " +
                "RSubDate=\'" + RSubDate_DatePicker.getEditor().getText() + "\'," +
                "RTitle=\'" + RTitle_TextField.getText() + "\'," +
                "RText=\'" + RText_TextArea.getText() + "\'," +
                "RState=\'已维修\'," +
                "RReply=\'" + RReply_TextArea.getText() + "\'," +
                "RSolveDate=\'" + RSolveDate_DatePicker.getEditor().getText() + "\'," +
                "ONo=" + ONo_TextField.getText() + " " +
                "WHERE RNo=\'" + data_repairTable.getRNo().get() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void error_NullONo(){
        //未输入业主编号时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请输入需要搜索的业主编号再进行搜索！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullOwener(){
        //未查到业主编号时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("搜索不到编号为" + ONo_TextField.getText().trim() + "的业主信息！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullRTitle(){
        //未输入维修单编号时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请输入维修单标题！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullRSubDate(){
        //未选择维修单提交日期的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请选择维修单提交日期！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullSearch(){
        //未先获取业主数据时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请先单击搜索按钮获取业主信息！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullRText(){
        //未输入维修单内容时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请输入维修单内容！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullRReply(){
        //未输入维修单回复时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请输入维修单回复！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullRSolveDate(){
        //未选择维修单解决回复时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请选择维修单解决日期！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void succeed_Edit(){
        //成功修改弹窗
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("维修单修改成功");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    public void close_Windows(){
        //关闭"维修单管理-新增"窗口
        //remove"维修单管理-新增"窗口和其控制器
        StageManager.STAGE.remove("Stage_RepairNewRecord");
        StageManager.CONTROLLER.remove("Controller_RepairNewRecord");
        //关闭窗口
        Stage stage = (Stage)Back_Button.getScene().getWindow();
        stage.close();
    }
}