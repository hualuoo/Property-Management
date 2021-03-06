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
    private Stage dialogStage;
    private int count;

    public Label RNo_Label;
    public TextField RTitle_TextField,ONo_TextField,OName_TextField,OTel_TextField;
    public DatePicker RSubDate_DatePicker,RSolveDate_DatePicker;
    public TextArea RText_TextArea,RReply_TextArea;
    public RadioButton NoRepair_RadioButton,YesRepair_RadioButton;
    public Button Search_Button,Confirm_Button,Back_Button;

    String query;
    ResultSet result;
    int check_Search;

    public void setDialogStage(Stage dialogStage) {
        //传参Stage
        this.dialogStage = dialogStage;
    }
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
    public void setCount(int count){
        this.count = count;
    }
    public void initialize() {
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
        //自定义日期选择器DatePicker
        setDataStyle(RSubDate_DatePicker);
        setDataStyle(RSolveDate_DatePicker);
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
        if (RTitle_TextField.getText() == null || RTitle_TextField.getText().length()==0){
            error_NullRTitle();
            return;
        }
        if (RSubDate_DatePicker.getEditor().getText() == null || RSubDate_DatePicker.getEditor().getText().length()==0){
            error_NullRSubDate();
            return;
        }
        if (check_Search !=1){
            error_NullSearch();
            return;
        }
        if (RText_TextArea.getText() == null || RText_TextArea.getText().length()==0){
            error_NullRText();
            return;
        }
        if (NoRepair_RadioButton.selectedProperty().getValue()==true){
            editDataToList_NoRepair();
            editDataToSQL_NoRepair();
            SQL_Connect sql_connect = new SQL_Connect();
            sql_connect.sql_Update(query);
            succeed_Add();
            close_Windows();
            return;
        }
        if (YesRepair_RadioButton.selectedProperty().getValue()==true){
            if (RReply_TextArea.getText() == null || RReply_TextArea.getText().length()==0){
                error_NullRReply();
                return;
            }
            if (RSolveDate_DatePicker.getEditor().getText() == null || RSolveDate_DatePicker.getEditor().getText().length()==0){
                error_NullRSolveDate();
                return;
            }
            editDataToList_YesRepair();
            editDataToSQL_YesRepair();
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
    void editDataToList_NoRepair(){
        //修改tableview中的数据("未维修")
        data_repairTable.setRTitle(RTitle_TextField.getText());
        data_repairTable.setRSubDate(RSubDate_DatePicker.getEditor().getText());
        data_repairTable.setONo(ONo_TextField.getText());
        data_repairTable.setOName(OName_TextField.getText());
        data_repairTable.setOTel(OTel_TextField.getText());
        data_repairTable.setRText(RText_TextArea.getText());
        data_repairTable.setRState("未维修");
        data_repairTable.setRReply("");
        data_repairTable.setRSolveDate("");
    }
    void editDataToList_YesRepair(){
        //修改tableview中的数据("已维修")
        data_repairTable.setRTitle(RTitle_TextField.getText());
        data_repairTable.setRSubDate(RSubDate_DatePicker.getEditor().getText());
        data_repairTable.setONo(ONo_TextField.getText());
        data_repairTable.setOName(OName_TextField.getText());
        data_repairTable.setOTel(OTel_TextField.getText());
        data_repairTable.setRText(RText_TextArea.getText());
        data_repairTable.setRState("已维修");
        data_repairTable.setRReply(RReply_TextArea.getText());
        data_repairTable.setRSolveDate(RSolveDate_DatePicker.getEditor().getText());
    }
    void editDataToSQL_NoRepair(){
        //修改数据库中的数据("未维修")
        query = "UPDATE Repair_Info SET " +
                "RSubDate=\'" + RSubDate_DatePicker.getEditor().getText() + "\'," +
                "RTitle=\'" + RTitle_TextField.getText() + "\'," +
                "RText=\'" + RText_TextArea.getText() + "\'," +
                "RState=\'未维修\'," +
                "RReply=NULL," +
                "RSolveDate=NULL " +
                "WHERE RNo=\'" + data_repairTable.getRNo().get() + "\'";
    }
    void editDataToSQL_YesRepair(){
        //修改数据库中的数据("已维修")
        query = "UPDATE Repair_Info SET " +
                "RSubDate=\'" + RSubDate_DatePicker.getEditor().getText() + "\'," +
                "RTitle=\'" + RTitle_TextField.getText() + "\'," +
                "RText=\'" + RText_TextArea.getText() + "\'," +
                "RState=\'已维修\'," +
                "RReply=\'" + RReply_TextArea.getText() + "\'," +
                "RSolveDate=\'" + RSolveDate_DatePicker.getEditor().getText() + "\' " +
                "WHERE RNo=\'" + data_repairTable.getRNo().get() + "\'";
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
    void succeed_Add(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("维修单修改成功");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    public void close_Windows(){
        Stage stage = (Stage)Confirm_Button.getScene().getWindow();
        stage.close();
    }
}