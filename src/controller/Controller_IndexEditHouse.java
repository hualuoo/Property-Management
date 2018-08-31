package controller;

import data.Data_HouseTable;
import util.SQL_Connect;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.stage.Stage;
import util.StageManager;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.regex.Pattern;

public class Controller_IndexEditHouse {
    public Label HNo_Label;
    public TextField HBuild_TextField,HPark_TextField,HFloor_TextField,HRoom_TextField,HArea_TextField,HType_TextField;
    public TextField ONo_TextField,OName_TextField,OTel_TextField,OID_TextField;
    public TextArea HNote_TextArea,ONote_TextArea;
    public ChoiceBox HState_ChoiceBox,OSex_ChoiceBox;
    public Button Search_Button,Confirm_Button,Exit_Button;
    public Data_HouseTable data_houseTable;
    //检查是否搜索
    int check_Search;
    //数据库代码以及返回结果
    String query;
    ResultSet result;

    public void setdata_houseTable(Data_HouseTable data_houseTable){
        //设置修改数据
        this.data_houseTable = data_houseTable;
        check_Search = 1;
        HNo_Label.setText(data_houseTable.getHNo().getValue().trim());
        HBuild_TextField.setText(data_houseTable.getHBuild().getValue().replace("幢",""));
        HPark_TextField.setText(data_houseTable.getHPark().getValue().replace("单元",""));
        HFloor_TextField.setText(data_houseTable.getHFloor().getValue().replace("层",""));
        HRoom_TextField.setText(data_houseTable.getHRoom().getValue().replace("室",""));
        HArea_TextField.setText(data_houseTable.getHArea().getValue().replace("㎡",""));
        HState_ChoiceBox.setValue(data_houseTable.getHState().getValue().trim());
        HType_TextField.setText(data_houseTable.getHType().getValue());
        if(data_houseTable.getHNote().getValue()==null || data_houseTable.getHNote().getValue().length() == 0)
            HNote_TextArea.setText("");
        else
            HNote_TextArea.setText(data_houseTable.getHNote().getValue().trim());
        if(data_houseTable.getONo().getValue()==null || data_houseTable.getONo().getValue().length() == 0){
            //HState_ChoiceBox选择触发器已将业主信息TextField清空
        }
        else {
            ONo_TextField.setText(data_houseTable.getONo().getValue());
            OName_TextField.setText(data_houseTable.getOName().getValue());
            OSex_ChoiceBox.setValue(data_houseTable.getOSex().getValue().trim());
            OTel_TextField.setText(data_houseTable.getOTel().getValue());
            OID_TextField.setText(data_houseTable.getOID().getValue());
            ONote_TextArea.setText(data_houseTable.getONote().getValue());
        }
    }
    public void initialize() {
        //初始化
        //将"房屋管理-编辑"控制器保存到map中
        StageManager.CONTROLLER.put("Controller_IndexEditHouse", this);
        //ChoiceBox加入选择项
        HState_ChoiceBox.setItems(FXCollections.observableArrayList(
                "已销售", "未销售", "待入住")
        );
        OSex_ChoiceBox.setItems(FXCollections.observableArrayList(
                "男", "女")
        );

        //房屋销售情况选择框变更监听
        HState_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue)->
                changeHState_ChoiceBox(newValue));
    }

    public void changeHState_ChoiceBox(Number newValue) {
        //房屋销售情况选择框变更
        if (newValue.intValue() == 1) {
            //选择第2项"未销售"
            ONo_TextField.setText("");
            OName_TextField.setText("");
            OTel_TextField.setText("");
            OID_TextField.setText("");
            ONote_TextArea.setText("");
            ONo_TextField.setDisable(true);
            Search_Button.setDisable(true);
        }
        else {
            //选择其他选项
            ONo_TextField.setDisable(false);
            Search_Button.setDisable(false);
            check_Search = 0;
        }
    }
    public void click_SearchButton(){
        //单击"搜索"按钮
        if(ONo_TextField.getText() == null || ONo_TextField.getText().length()==0){
            //如果 业主编号-文本栏 为空，弹出未输入业主编号时的错误弹窗
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
                OSex_ChoiceBox.setValue("男");
                OTel_TextField.setText("");
                OID_TextField.setText("");
                ONote_TextArea.setText("");
                error_NullOwener();
                return;
            }
            else {
                //如果数据库查到业主信息
                check_Search = 1;
                OName_TextField.setText(result.getString("OName"));
                OSex_ChoiceBox.setValue(result.getString("OSex").trim());
                OTel_TextField.setText(result.getString("OTel"));
                OID_TextField.setText(result.getString("OID"));
                ONote_TextArea.setText(result.getString("ONote"));
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
        alert.setHeaderText("您确认是否添加该条信息？");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        Optional<ButtonType> alertresult = alert.showAndWait();
        if (alertresult.get() != ButtonType.OK) {
            return;
        }
        String regex = "[0-9]*";
        if (HArea_TextField.getText() == null || HArea_TextField.getText().length() == 0) {
            //如果未输入房屋面积
            error_NullHArea();
            return;
        }
        if (Pattern.compile(regex).matcher(HArea_TextField.getText()).matches() == false) {
            //房屋面积输入错误
            error_WrongHArea();
            return;
        }
        if (HArea_TextField.getText().length() > 4) {
            //房屋面积超出长度
            error_LangHArea();
            return;
        }
        if (HState_ChoiceBox.getSelectionModel().getSelectedItem() == null || HState_ChoiceBox.getSelectionModel().getSelectedItem().toString().length() == 0) {
            //如果未选择房屋状态
            error_NullHState();
            return;
        }
        if (HType_TextField.getText() == null || HType_TextField.getText().length() == 0) {
            //如果未输入房屋户型
            error_NullHType();
            return;
        }
        if (length(HType_TextField.getText()) > 13) {
            //房屋户型超出长度
            error_LangHType();
            return;
        }
        //ResultSet的next方法需要try-catch输出报错
        if(HState_ChoiceBox.getSelectionModel().getSelectedItem().toString().equals("未销售")){
            //房屋状态"未销售"
            if (HNote_TextArea.getText()==null || HType_TextField.getText().length()==0){
                //修改数据到数据库(未销售,备注为空)
                editHouseInfoToSQL_NoSellNullNote();
                //刷新"房屋信息管理"窗口的TableView
                flush_TableView();
                //修改成功弹窗
                succeedEdit();
                //关闭"业主信息管理- 修改"窗口
                close_Windows();
            }
            else {
                //修改数据到数据库(未销售,备注不为空)
                editHouseInfoToSQL_NoSellFullNote();
                //刷新"房屋信息管理"窗口的TableView
                flush_TableView();
                //修改成功弹窗
                succeedEdit();
                //关闭"业主信息管理- 修改"窗口
                close_Windows();
            }
        }
        else {
            //房屋状态"已销售""待入住"
            //检查是否获取了业主信息
            if (check_Search != 1){
                error_NullSearch();
                return;
            }
            if (HNote_TextArea.getText()==null || HType_TextField.getText().length()==0){
                //修改数据到数据库(已销售,备注为空)
                editHouseInfoToSQL_YesSellNullNote();
                //刷新"房屋信息管理"窗口的TableView
                flush_TableView();
                //添加成功弹窗
                succeedEdit();
                //关闭"业主信息管理- 修改"窗口
                close_Windows();
            }
            else {
                //添加数据到数据库(已销售,备注不为空)
                editHouseInfoToSQL_YesSellFullNote();
                //刷新"房屋信息管理"窗口的TableView
                flush_TableView();
                //添加成功弹窗
                succeedEdit();
                //关闭"业主信息管理- 修改"窗口
                close_Windows();
            }
        }
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
    void error_NullHArea(){
        //未输入房屋面积时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋面积不能为空，请输入面积！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_WrongHArea(){
        //房屋面积输入错误的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋面积仅能为数字，请输入正确的面积！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_LangHArea(){
        //房屋面积超出长度时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋面积超出长度，最高仅能输入3位数字！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullHState(){
        //未选择房屋状态时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您未选择房屋状态，请选择状态！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullHType(){
        //未输入房屋户型时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋户型不能为空，请输入户型！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_LangHType(){
        //房屋户型超出长度时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋户型超出长度，仅能输入12个字符(每个汉字占2个字符)！");
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
    void editHouseInfoToSQL_NoSellNullNote(){
        //修改数据到数据库(未销售,备注为空)
        query = "UPDATE House_Info SET " +
                "HArea=" + HArea_TextField.getText() + "," +
                "HState=\'未销售\'," +
                "HType=\'" + HType_TextField.getText() + "\'," +
                "HNote=NULL," +
                "ONo=NULL " +
                "WHERE HNo=\'" + HNo_Label.getText() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void editHouseInfoToSQL_NoSellFullNote(){
        //修改数据到数据库(未销售,备注不为空)
        query = "UPDATE House_Info SET " +
                "HArea=" + HArea_TextField.getText() + "," +
                "HState=\'未销售\'," +
                "HType=\'" + HType_TextField.getText() + "\'," +
                "HNote=\'" + HNote_TextArea.getText() + "\'," +
                "ONo=NULL " +
                "WHERE HNo=\'" + HNo_Label.getText() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void editHouseInfoToSQL_YesSellNullNote(){
        //修改数据到数据库(已销售,备注为空)
        query = "UPDATE House_Info SET " +
                "HArea=" + HArea_TextField.getText() + "," +
                "HState=\'" + HState_ChoiceBox.getSelectionModel().getSelectedItem() + "\'," +
                "HType=\'" + HType_TextField.getText() + "\'," +
                "HNote=NULL," +
                "ONo=" + ONo_TextField.getText() + " " +
                "WHERE HNo=\'" + HNo_Label.getText() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void editHouseInfoToSQL_YesSellFullNote(){
        //修改数据到数据库(已销售,备注不为空)
        query = "UPDATE House_Info SET " +
                "HArea=" + HArea_TextField.getText() + "," +
                "HState=\'" + HState_ChoiceBox.getSelectionModel().getSelectedItem() + "\'," +
                "HType=\'" + HType_TextField.getText() + "\'," +
                "HNote=\'" + HNote_TextArea.getText() + "\'," +
                "ONo=" + ONo_TextField.getText() + " " +
                "WHERE HNo=\'" + HNo_Label.getText() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void flush_TableView(){
        //刷新"房屋信息管理"窗口的TableView
        Controller_IndexMain controller_indexMain=(Controller_IndexMain) StageManager.CONTROLLER.get("Controller_IndexMain");
        controller_indexMain.HouseTableData_List.clear();
        controller_indexMain.showHouseTable("初始化");
    }
    public void errorEdit(){
        Alert alert2 = new Alert(Alert.AlertType.ERROR);
        alert2.setTitle("小区物业管理系统");
        alert2.setHeaderText("输入数据检测异常，修改数据失败！");
        alert2.initOwner(Confirm_Button.getScene().getWindow());
        alert2.showAndWait();
    }
    public void succeedEdit(){
        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert2.setTitle("小区物业管理系统");
        alert2.setHeaderText("信息修改成功！");
        alert2.initOwner(Confirm_Button.getScene().getWindow());
        alert2.showAndWait();
        close_Windows();
    }
    public void click_ExitButton(){
        close_Windows();
    }
    public void close_Windows(){
        Stage stage = (Stage)Confirm_Button.getScene().getWindow();
        stage.close();
    }
    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            //获取一个字符
            String temp = value.substring(i, i + 1);
            //判断是否为中文字符
            if (temp.matches(chinese)) {
                //中文字符长度为2
                valueLength += 2;
            } else {
                //其他字符长度为1
                valueLength ++;
            }
        }
        return valueLength;
    }
}