package controller;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.stage.Stage;
import util.SQL_Connect;
import util.StageManager;

import java.util.regex.Pattern;

public class Controller_OwnerNewRecord{
    //业主信息组件
    /*业主信息    ONo业主编号    OName业主姓名    OSex业主性别    OTel业主电话    OID业主身份证号码    ONote业主备注*/
    public Label ONo_Label;
    public TextField OName_TextField,OTel_TextField,OID_TextField;
    public ChoiceBox OSex_ChoiceBox;
    public TextArea ONote_TextArea;
    public Button Confirm_Button,Cancel_Button;
    public String query;
    public void setcount(int count){
        ONo_Label.setText("#" + (count + 1));
    }
    public void initialize() {
        //初始化
        //将"业主管理 - 新增"控制器保存到map中
        StageManager.CONTROLLER.put("Controller_OwnerNewRecord", this);
        //ChoiceBox加入选择项
        OSex_ChoiceBox.setItems(FXCollections.observableArrayList(
                "男", "女")
        );

    }
    public void click_ConfirmButton(){
        if (OName_TextField.getText()==null || OName_TextField.getText().length()==0){
            //如果未输入业主姓名
            error_NullOName();
            return;
        }
        if (length(OName_TextField.getText())>8){
            //业主姓名超出长度
            error_LangOName();
            return;
        }
        if (OSex_ChoiceBox.getSelectionModel().getSelectedItem()==null || OSex_ChoiceBox.getSelectionModel().getSelectedItem().toString().length()==0){
            //如果未选择业主性别
            error_NullOSex();
            return;
        }
        if (OTel_TextField.getText()==null || OTel_TextField.getText().length()==0){
            //如果未输入业主联系电话(手机号码或者固定电话)
            error_NullOTel();
            return;
        }
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";      //手机号码正则
        String regex2 = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";  //固定电话正则
        if (Pattern.compile(regex).matcher(OTel_TextField.getText()).matches() == false && Pattern.compile(regex2).matcher(OTel_TextField.getText()).matches() == false){
            //业主联系电话(手机号码或者固定电话)输入错误
            error_WrongOTel();
            return;
        }
        if (OID_TextField.getText()==null || OID_TextField.getText().length()==0){
            //如果未输入业主身份证号码
            error_NullOID();
            return;
        }
        regex = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";    //身份证号码正则
        if (Pattern.compile(regex).matcher(OID_TextField.getText()).matches() == false){
            //业主身份证号码输入错误
            error_WrongOID();
            return;
        }
        if (ONote_TextArea.getText()==null || ONote_TextArea.getText().length()==0){
            //业主备注为空时
            //添加数据到数据库(业主备注为空)
            addOwnerInfoToSQL_NullONote();
            //刷新"业主信息管理"窗口的TableView
            flush_TableView();
            //关闭"业主信息管理- 新增"窗口
            close_Windows();
        }
        else {
            if (length(ONote_TextArea.getText())>100){
                //业主备注超出长度
                error_LangONote();
                return;
            }
            //业主备注不为空时
            //添加数据到数据库(业主备注不为空)
            addOwnerInfoToSQL_FullONote();
            //刷新"业主信息管理"窗口的TableView
            flush_TableView();
            //关闭"业主信息管理 - 新增"窗口
            close_Windows();
        }
    }
    public void click_CancelButton(){
        //关闭"业主信息管理 - 新增"窗口
        close_Windows();
    }
    void error_NullOName(){
        //未输入业主姓名时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("业主姓名不能为空，请输入姓名！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_LangOName(){
        //业主姓名超出长度时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("业主姓名超出长度，仅能输入8个字符(每个汉字占2个字符)！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullOSex(){
        //未选择业主性别时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您未选择业主性别，请选择性别！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullOTel(){
        //未输入业主联系方式的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("业主联系电话不能为空，请输入联系电话！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_WrongOTel(){
        //业主联系电话(手机号码或者固定电话)输入错误的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("检测到业主联系电话输入错误，请输入正确的电话号码！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullOID(){
        //未输入业主身份证号码的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("业主身份证号码不能为空，请输入身份证号码！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_WrongOID(){
        //业主身份证号码输入错误的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("检测到业主身份证号码输入错误，请输入正确的身份证号码！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_LangONote(){
        //业主备注超出长度时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("业主备注超出长度，仅能输入100个字符(每个汉字占2个字符)！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void addOwnerInfoToSQL_NullONote(){
        //添加数据到数据库(业主备注为空)
        query = "INSERT INTO Owner_Info\n" +
                "VALUES\n" +
                "(\'" + OName_TextField.getText() + "\',\'" + OSex_ChoiceBox.getSelectionModel().getSelectedItem() + "\',\'" + OTel_TextField.getText() + "\',\'" + OID_TextField.getText() + "\',NULL);";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void addOwnerInfoToSQL_FullONote(){
        //添加数据到数据库(业主备注不为空)
        query = "INSERT INTO Owner_Info\n" +
                "VALUES\n" +
                "(\'" + OName_TextField.getText() + "\',\'" + OSex_ChoiceBox.getSelectionModel().getSelectedItem() + "\',\'" + OTel_TextField.getText() + "\',\'" + OID_TextField.getText() + "\',\'" + ONote_TextArea.getText() + "\');";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void flush_TableView(){
        //刷新"业主信息管理"窗口的TableView
        Controller_OwnerMain controller_ownerMain=(Controller_OwnerMain) StageManager.CONTROLLER.get("Controller_OwnerMain");
        controller_ownerMain.OwnerTableView_List.clear();
        controller_ownerMain.showOwnerTableView();
    }
    void close_Windows(){
        //关闭"业主信息管理 - 新增"窗口
        //remove"业主信息管理 - 新增"窗口和其控制器
        StageManager.STAGE.remove("Stage_OwnerNewRecord");
        StageManager.CONTROLLER.remove("Controller_OwnerNewRecord");
        //关闭窗口
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