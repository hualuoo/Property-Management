import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Controller_RepairNewRecord{
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

    public Data_RepairTable getdata_RepairTable(){
        return data_repairTable;
    }
    public void setDialogStage(Stage dialogStage) {
        //传参Stage
        this.dialogStage = dialogStage;
    }
    public void setdata_RepairTable(Data_RepairTable data_repairTable){
        //传参房屋数据
        this.data_repairTable = data_repairTable;
    }
    public void setCount(int count){
        this.count = count;
        RNo_Label.setText("#" + (count + 1));
    }
    public void initialize() {
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
        //默认选择未维修
        NoRepair_RadioButton.setSelected(true);
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
        //按下"确认"按钮
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您确认是否添加该条保修单？");
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
            addDataToList_NoRepair();
            addDataToSQL_NoRepair();
            SQL_Connect sql_connect = new SQL_Connect();
            sql_connect.sql_Update(query);
            succeed_Add();
            close_Windows();
            return;
        }
        if (YesRepair_RadioButton.selectedProperty().getValue()==true){
            addDataToList_YesRepair();
            addDataToSQL_YesRepair();
            SQL_Connect sql_connect = new SQL_Connect();
            sql_connect.sql_Update(query);
            succeed_Add();
            close_Windows();
            return;
        }
    }
    public void click_BackButton(){
        Controller_RepairMain controller_repairMain=(Controller_RepairMain) StageManager.CONTROLLER.get("Controller_RepairMain");
        controller_repairMain.RepairTableView_List.clear();
        controller_repairMain.showRepairTableView();
        close_Windows();
    }
    void addDataToList_NoRepair(){
        data_repairTable.setRNo(count+1+"");
        data_repairTable.setRTitle(RTitle_TextField.getText());
        data_repairTable.setRSubDate(RSubDate_DatePicker.getEditor().getText());
        data_repairTable.setONo(ONo_TextField.getText());
        data_repairTable.setOName(OName_TextField.getText());
        data_repairTable.setOTel(OTel_TextField.getText());
        data_repairTable.setRText(RText_TextArea.getText());
        data_repairTable.setRState("未维修");
    }
    void addDataToList_YesRepair(){
        data_repairTable.setRNo(count+1+"");
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
    void addDataToSQL_NoRepair(){
        query = "INSERT INTO Repair_Info VALUES" +
                "(\'" + RSubDate_DatePicker.getEditor().getText() + "\',\'" + RTitle_TextField.getText() + "\',\'" +
                RText_TextArea.getText() + "\',\'" + "未维修" + "\',NULL,NULL,\'" + ONo_TextField.getText() + "\');";
    }
    void addDataToSQL_YesRepair(){
        query = "INSERT INTO Repair_Info VALUES" +
                "(\'" + RSubDate_DatePicker.getEditor().getText() + "\',\'" + RTitle_TextField.getText() + "\',\'" +
                RText_TextArea.getText() + "\',\'" + "已维修" + "\',\'" + RReply_TextArea.getText() + "\',\'" + RSolveDate_DatePicker.getEditor().getText() + "\',\'" + ONo_TextField.getText() + "\');";
    }
    void error_NullONo(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请输入需要搜索的业主编号再进行搜索！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullOwener(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("搜索不到编号为" + ONo_TextField.getText().trim() + "的业主信息！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullRTitle(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请输入维修单标题！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullRSubDate(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请选择维修单提交日期！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullSearch(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请先单击搜索按钮获取业主信息！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullRText(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请输入维修单内容！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void succeed_Add(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("维修单添加成功");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    public void close_Windows(){
        Stage stage = (Stage)Confirm_Button.getScene().getWindow();
        stage.close();
    }
}