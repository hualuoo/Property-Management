import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Controller_ComplaintEditRecord{
    public Data_ComplaintTable data_complaintTable;
    private Stage dialogStage;
    private int count;

    public Label CNo_Label;
    public TextField CTitle_TextField,ONo_TextField,OName_TextField,OTel_TextField;
    public DatePicker CSubDate_DatePicker,CSolveDate_DatePicker;
    public TextArea CText_TextArea,CReply_TextArea;
    public RadioButton NoComplaint_RadioButton,YesComplaint_RadioButton;
    public Button Search_Button,Confirm_Button,Back_Button;

    String query;
    ResultSet result;
    int check_Search;

    public void setDialogStage(Stage dialogStage) {
        //传参Stage
        this.dialogStage = dialogStage;
    }
    public void setdata_ComplaintTable(Data_ComplaintTable data_complaintTable){

        //传参房屋数据
        this.data_complaintTable = data_complaintTable;
        //投诉情况获取
        if(data_complaintTable.getCState().get().equals("未处理")){
            NoComplaint_RadioButton.setSelected(true);
            //设置初始数据(未处理)
            setDefaultData_NoComplaint();
        }
        else {
            //设置初始数据(已处理)
            YesComplaint_RadioButton.setSelected(true);
            setDefaultData_YesComplaint();
        }
    }
    public void setCount(int count){
        this.count = count;
    }
    public void initialize() {
        //默认已获取业主信息
        check_Search = 1;
        //投诉情况单选框监听
        NoComplaint_RadioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            //投诉情况(未选择)单选框监听
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                select_NoComplaint(newValue);
            }
        });
        YesComplaint_RadioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            //投诉情况(已选择)单选框监听
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                select_YesComplaint(newValue);
            }
        });
        //自定义日期选择器DatePicker
        setDataStyle(CSubDate_DatePicker);
        setDataStyle(CSolveDate_DatePicker);
        //文本栏变动监听
        ONo_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            //只要业主编号文字变动即需要重新点击"搜索"按钮
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals(data_complaintTable.getONo().get())==false){
                    check_Search = 0;
                    OName_TextField.setText("");
                    OTel_TextField.setText("");
                }
            }
        });
    }
    void setDefaultData_NoComplaint(){
        //设置初始数据(未处理)
        CNo_Label.setText("#" + data_complaintTable.getCNo().get());
        CTitle_TextField.setText(data_complaintTable.getCTitle().get().trim());
        LocalDate RSubDate = LocalDate.parse(data_complaintTable.getCSubDate().get(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        CSubDate_DatePicker.setValue(RSubDate);
        ONo_TextField.setText(data_complaintTable.getONo().get());
        OName_TextField.setText(data_complaintTable.getOName().get());
        OTel_TextField.setText(data_complaintTable.getOTel().get());
        CText_TextArea.setText(data_complaintTable.getCText().get().trim());
    }
    void setDefaultData_YesComplaint(){
        //设置初始数据(已处理)
        CNo_Label.setText("#" + data_complaintTable.getCNo().get());
        CTitle_TextField.setText(data_complaintTable.getCTitle().get().trim());
        LocalDate RSubDate = LocalDate.parse(data_complaintTable.getCSubDate().get(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        CSubDate_DatePicker.setValue(RSubDate);
        ONo_TextField.setText(data_complaintTable.getONo().get());
        OName_TextField.setText(data_complaintTable.getOName().get());
        OTel_TextField.setText(data_complaintTable.getOTel().get());
        CText_TextArea.setText(data_complaintTable.getCText().get().trim());
        CReply_TextArea.setText(data_complaintTable.getCReply().get().trim());
        LocalDate RSolveDate = LocalDate.parse(data_complaintTable.getCSolveDate().get(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        CSolveDate_DatePicker.setValue(RSolveDate);
    }
    public void setDataStyle(DatePicker datepicker){
        //自定义日期选择器
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
        //投诉情况(未选择)单选框监听
        if (newValue){
            YesComplaint_RadioButton.setSelected(false);
            CReply_TextArea.setDisable(true);
            CReply_TextArea.setText("");
            CSolveDate_DatePicker.setDisable(true);
            CSolveDate_DatePicker.setValue(null);
        }
        else {
            YesComplaint_RadioButton.setSelected(true);
        }
    }
    public void select_YesComplaint(Boolean newValue){
        //投诉情况(已选择)单选框监听
        if (newValue){
            NoComplaint_RadioButton.setSelected(false);
            CReply_TextArea.setDisable(false);
            CSolveDate_DatePicker.setDisable(false);
        }
        else {
            NoComplaint_RadioButton.setSelected(true);
        }
    }
    public void click_SearchButton(){
        //按下"搜索"按钮
        if(ONo_TextField.getText() == null || ONo_TextField.getText().length()==0){
            error_NullONo();
            return;
        }
        query = "SELECT ONo,OName,OSex,OTel,OID,ONote FROM Owner_Info WHERE ONo=\'" + ONo_TextField.getText().trim() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        try {
            if(result.next() == false){
                check_Search = 0;
                OName_TextField.setText("");
                OTel_TextField.setText("");
                error_NullOwener();
                return;
            }
            else {
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
        System.out.print(check_Search);
        //按下"确认"按钮
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您确认是否修改该条保修单？");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }
        if (CTitle_TextField.getText() == null || CTitle_TextField.getText().length()==0){
            error_NullRTitle();
            return;
        }
        if (CSubDate_DatePicker.getEditor().getText() == null || CSubDate_DatePicker.getEditor().getText().length()==0){
            error_NullRSubDate();
            return;
        }
        if (check_Search !=1){
            error_NullSearch();
            return;
        }
        if (CText_TextArea.getText() == null || CText_TextArea.getText().length()==0){
            error_NullRText();
            return;
        }
        if (NoComplaint_RadioButton.selectedProperty().getValue()==true){
            editDataToList_NoComplaint();
            editDataToSQL_NoComplaint();
            SQL_Connect sql_connect = new SQL_Connect();
            sql_connect.sql_Update(query);
            succeed_Add();
            close_Windows();
            return;
        }
        if (YesComplaint_RadioButton.selectedProperty().getValue()==true){
            if (CReply_TextArea.getText() == null || CReply_TextArea.getText().length()==0){
                error_NullRReply();
                return;
            }
            if (CSolveDate_DatePicker.getEditor().getText() == null || CSolveDate_DatePicker.getEditor().getText().length()==0){
                error_NullRSolveDate();
                return;
            }
            editDataToList_YesComplaint();
            editDataToSQL_YesComplaint();
            SQL_Connect sql_connect = new SQL_Connect();
            sql_connect.sql_Update(query);
            succeed_Add();
            close_Windows();
            return;
        }
    }
    public void click_BackButton(){
        //按下返回按钮
        close_Windows();
    }
    void editDataToList_NoComplaint(){
        //修改tableview中的数据("未处理")
        data_complaintTable.setCTitle(CTitle_TextField.getText());
        data_complaintTable.setCSubDate(CSubDate_DatePicker.getEditor().getText());
        data_complaintTable.setONo(ONo_TextField.getText());
        data_complaintTable.setOName(OName_TextField.getText());
        data_complaintTable.setOTel(OTel_TextField.getText());
        data_complaintTable.setCText(CText_TextArea.getText());
        data_complaintTable.setCState("未处理");
        data_complaintTable.setCReply("");
        data_complaintTable.setCSolveDate("");
    }
    void editDataToList_YesComplaint(){
        //修改tableview中的数据("已处理")
        data_complaintTable.setCTitle(CTitle_TextField.getText());
        data_complaintTable.setCSubDate(CSubDate_DatePicker.getEditor().getText());
        data_complaintTable.setONo(ONo_TextField.getText());
        data_complaintTable.setOName(OName_TextField.getText());
        data_complaintTable.setOTel(OTel_TextField.getText());
        data_complaintTable.setCText(CText_TextArea.getText());
        data_complaintTable.setCState("已处理");
        data_complaintTable.setCReply(CReply_TextArea.getText());
        data_complaintTable.setCSolveDate(CSolveDate_DatePicker.getEditor().getText());
    }
    void editDataToSQL_NoComplaint(){
        //修改数据库中的数据("未处理")
        query = "UPDATE Complaint_Info SET " +
                "CSubDate=\'" + CSubDate_DatePicker.getEditor().getText() + "\'," +
                "CTitle=\'" + CTitle_TextField.getText() + "\'," +
                "CText=\'" + CText_TextArea.getText() + "\'," +
                "CState=\'未处理\'," +
                "CReply=NULL," +
                "CSolveDate=NULL " +
                "WHERE CNo=\'" + data_complaintTable.getCNo().get() + "\'";
    }
    void editDataToSQL_YesComplaint(){
        //修改数据库中的数据("已处理")
        query = "UPDATE Complaint_Info SET " +
                "CSubDate=\'" + CSubDate_DatePicker.getEditor().getText() + "\'," +
                "CTitle=\'" + CTitle_TextField.getText() + "\'," +
                "CText=\'" + CText_TextArea.getText() + "\'," +
                "CState=\'已处理\'," +
                "CReply=\'" + CReply_TextArea.getText() + "\'," +
                "CSolveDate=\'" + CSolveDate_DatePicker.getEditor().getText() + "\' " +
                "WHERE CNo=\'" + data_complaintTable.getCNo().get() + "\'";
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("投诉单修改成功");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    public void close_Windows(){
        Stage stage = (Stage)Confirm_Button.getScene().getWindow();
        stage.close();
    }
}