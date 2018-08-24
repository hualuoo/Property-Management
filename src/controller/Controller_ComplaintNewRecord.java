package controller;

import util.SQL_Connect;
import util.StageManager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Controller_ComplaintNewRecord{
    private int count;
    //输入框组件
    public Label CNo_Label;
    public TextField CTitle_TextField,ONo_TextField,OName_TextField,OTel_TextField;
    public DatePicker CSubDate_DatePicker,CSolveDate_DatePicker;
    public TextArea CText_TextArea,CReply_TextArea;
    public RadioButton NoComplaint_RadioButton,YesComplaint_RadioButton;
    public Button Search_Button,Confirm_Button,Back_Button;
    //数据库代码以及返回结果
    String query;
    ResultSet result;
    //检查是否已搜索业主信息
    int check_Search;
    public void setCount(int count){
        //传参单号
        this.count = count;
        //设置单号Label
        CNo_Label.setText("#" + (count + 1));
    }
    public void initialize() {
        //初始化
        //将"投诉单管理-新增"控制器保存到map中
        StageManager.CONTROLLER.put("Controller_ComplaintNewRecord", this);
        //投诉情况单选框监听
        NoComplaint_RadioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            //投诉情况"未处理"单选框监听
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                select_NoComplaint(newValue);
            }
        });
        YesComplaint_RadioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            //投诉情况"已处理"单选框监听
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                select_YesComplaint(newValue);
            }
        });
        //自定义日期选择器DatePicker格式为"yyyy-MM-dd"
        setDataStyle(CSubDate_DatePicker);
        setDataStyle(CSolveDate_DatePicker);
        //默认选择未投诉
        NoComplaint_RadioButton.setSelected(true);
        //业主编号-文本栏 变动监听
        ONo_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            //只要业主编号文字变动即需要重新点击"搜索"按钮
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                check_Search = 0;
                OName_TextField.setText("");
                OTel_TextField.setText("");
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
    public void select_NoComplaint(Boolean newValue){
        //投诉情况"未处理"单选框监听
        if (newValue){
            //设置"已处理"单选框不勾选
            YesComplaint_RadioButton.setSelected(false);
            //设置"回复"文本框和"解决时间"选择器不可使用并置空
            CReply_TextArea.setDisable(true);
            CReply_TextArea.setText("");
            CSolveDate_DatePicker.setDisable(true);
            CSolveDate_DatePicker.setValue(null);
        }
        else {
            //设置"已处理"单选框勾选
            YesComplaint_RadioButton.setSelected(true);
        }
    }
    public void select_YesComplaint(Boolean newValue){
        //投诉情况"已处理"单选框监听
        if (newValue){
            //设置"未处理"单选框不勾选
            NoComplaint_RadioButton.setSelected(false);
            //设置"回复"文本框和"解决时间"选择器可使用
            CReply_TextArea.setDisable(false);
            CSolveDate_DatePicker.setDisable(false);
        }
        else {
            //设置"未处理"单选框勾选
            NoComplaint_RadioButton.setSelected(true);
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
        query = "SELECT ONo,OName,OSex,OTel,OID,ONote FROM Owner_Info WHERE ONo=\'" + ONo_TextField.getText().trim() + "\'";
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
        //确认是否添加该投诉单
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您确认是否添加该条投诉单？");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            //如果选择了"否"
            return;
        }
        if (CTitle_TextField.getText() == null || CTitle_TextField.getText().length()==0){
            //如果标题为空报错
            error_NullRTitle();
            return;
        }
        if (CSubDate_DatePicker.getEditor().getText() == null || CSubDate_DatePicker.getEditor().getText().length()==0){
            //如果提交日期为空报错
            error_NullRSubDate();
            return;
        }
        if (check_Search !=1){
            //如果未单击"搜索"按钮报错
            error_NullSearch();
            return;
        }
        if (CText_TextArea.getText() == null || CText_TextArea.getText().length()==0){
            //如果内容为空报错
            error_NullRText();
            return;
        }
        if (NoComplaint_RadioButton.selectedProperty().getValue()==true){
            //如果勾选了"未处理"
            //添加"已处理"投诉单数据到数据库
            addDataToSQL_NoComplaint();
            //成功添加弹窗
            succeed_Add();
            //刷新"投诉单管理-主界面"窗口的TableView
            flush_TableView();
            //关闭"投诉单管理-新增"窗口
            close_Windows();
            return;
        }
        if (YesComplaint_RadioButton.selectedProperty().getValue()==true){
            if (CReply_TextArea.getText() == null || CReply_TextArea.getText().length()==0){
                //如果回复为空报错
                error_NullRReply();
                return;
            }
            if (CSolveDate_DatePicker.getEditor().getText() == null || CSolveDate_DatePicker.getEditor().getText().length()==0){
                //如果解决日期为空报错
                error_NullRSolveDate();
                return;
            }
            //添加"已处理"投诉单数据到数据库
            addDataToSQL_YesComplaint();
            //成功添加弹窗
            succeed_Add();
            //刷新"投诉单管理-主界面"窗口的TableView
            flush_TableView();
            //关闭"投诉单管理-新增"窗口
            close_Windows();
            return;
        }
    }
    public void click_BackButton(){
        //单击"返回"按钮
        close_Windows();
    }
    public void flush_TableView(){
        //刷新"投诉单管理-主界面"窗口的TableView
        Controller_ComplaintMain controller_complaintMain=(Controller_ComplaintMain) StageManager.CONTROLLER.get("Controller_ComplaintMain");
        controller_complaintMain.ComplaintTableView_List.clear();
        controller_complaintMain.showComplaintTableView();
    }
    void addDataToSQL_NoComplaint(){
        //添加"未处理"投诉单数据到数据库
        query = "INSERT INTO Complaint_Info VALUES" +
                "(\'" + CSubDate_DatePicker.getEditor().getText() + "\',\'" + CTitle_TextField.getText() + "\',\'" +
                CText_TextArea.getText() + "\',\'" + "未处理" + "\',NULL,NULL,\'" + ONo_TextField.getText() + "\');";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void addDataToSQL_YesComplaint(){
        //添加"已处理"投诉单数据到数据库
        query = "INSERT INTO Complaint_Info VALUES" +
                "(\'" + CSubDate_DatePicker.getEditor().getText() + "\',\'" + CTitle_TextField.getText() + "\',\'" +
                CText_TextArea.getText() + "\',\'" + "已处理" + "\',\'" + CReply_TextArea.getText() + "\',\'" + CSolveDate_DatePicker.getEditor().getText() + "\',\'" + ONo_TextField.getText() + "\');";
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
        //未输入投诉单编号时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请输入投诉单标题！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullRSubDate(){
        //未选择投诉单提交日期的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请选择投诉单提交日期！");
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
        //未输入投诉单内容时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请输入投诉单内容！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullRReply(){
        //未输入投诉单回复时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请输入投诉单回复！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullRSolveDate(){
        //未选择投诉单解决回复时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请选择投诉单解决日期！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void succeed_Add(){
        //添加成功弹窗
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("投诉单添加成功");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    public void close_Windows(){
        //关闭"投诉单管理-新增"窗口
        //remove"投诉单管理-新增"窗口和其控制器
        StageManager.STAGE.remove("Stage_ComplaintNewRecord");
        StageManager.CONTROLLER.remove("Controller_ComplaintNewRecord");
        //关闭窗口
        Stage stage = (Stage)Back_Button.getScene().getWindow();
        stage.close();
    }
}