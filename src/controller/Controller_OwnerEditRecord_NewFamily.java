package controller;

import javafx.scene.control.*;
import util.SQL_Connect;
import util.StageManager;

import javafx.collections.FXCollections;
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.util.regex.Pattern;

public class Controller_OwnerEditRecord_NewFamily {
    public String ONo;
    public TextField FName_TextField,FTel_TextField,FID_TextField,FRelation_TextField;
    public ChoiceBox FSex_ChoiceBox;
    public TextArea FNote_TextArea;
    public Button Confirm_Button,Cancel_Button;
    //数据库代码以及返回结果
    String query;
    ResultSet result;
    public void setONo(String ONo){
        this.ONo = ONo;
    }
    public void initialize(){
        //初始化
        //将"业主信息管理 - 家庭成员信息 - 新增"控制器保存到map中
        StageManager.CONTROLLER.put("Controller_OwnerEditRecord_NewFamily", this);
        //ChoiceBox加入选择项
        FSex_ChoiceBox.setItems(FXCollections.observableArrayList(
                "男", "女")
        );
    }
    public void click_ConfirmButton(){
        if (FName_TextField.getText()==null || FName_TextField.getText().length()==0){
            //如果未输入家庭成员姓名
            error_NullFName();
            return;
        }
        if (length(FName_TextField.getText())>8){
            //家庭成员姓名超出长度
            error_LangFName();
            return;
        }
        if (FSex_ChoiceBox.getSelectionModel().getSelectedItem()==null || FSex_ChoiceBox.getSelectionModel().getSelectedItem().toString().length()==0){
            //如果未选择家庭成员性别
            error_NullFSex();
            return;
        }
        if (FTel_TextField.getText()==null || FTel_TextField.getText().length()==0){
            //如果未输入家庭成员联系电话(手机号码或者固定电话)
            error_NullFTel();
            return;
        }
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";      //手机号码正则
        String regex2 = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";  //固定电话正则
        if (Pattern.compile(regex).matcher(FTel_TextField.getText()).matches() == false && Pattern.compile(regex2).matcher(FTel_TextField.getText()).matches() == false){
            //家庭成员联系电话(手机号码或者固定电话)输入错误
            error_WrongFTel();
            return;
        }
        if (FID_TextField.getText()==null || FID_TextField.getText().length()==0){
            //如果未输入家庭成员身份证号码
            error_NullFID();
            return;
        }
        regex = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";    //身份证号码正则
        if (Pattern.compile(regex).matcher(FID_TextField.getText()).matches() == false){
            //家庭成员身份证号码输入错误
            error_WrongFID();
            return;
        }
        if (FRelation_TextField.getText()==null || FRelation_TextField.getText().length()==0){
            //如果未输入家庭成员与业主关系
            error_NullFRelation();
            return;
        }
        if (length(FRelation_TextField.getText())>10){
            //家庭成员与业主关系超出长度
            error_LangFRelation();
            return;
        }
        if (FNote_TextArea.getText()==null || FNote_TextArea.getText().length()==0){
            //家庭成员备注为空时
            //添加数据到数据库(家庭成员备注为空)
            addFamilyInfoToSQL_NullFNote();
            //刷新"业主信息管理 - 家庭成员信息"窗口的TableView
            flush_TableView();
            //关闭"业主信息管理 - 家庭成员信息 - 新增"窗口
            close_Windows();
        }
        else {
            if (length(FNote_TextArea.getText())>100){
                //家庭成员备注超出长度
                error_LangFNote();
                return;
            }
            //家庭成员备注不为空时
            //添加数据到数据库(家庭成员备注不为空)
            addFamilyInfoToSQL_FullFNote();
            //刷新"业主信息管理 - 家庭成员信息"窗口的TableView
            flush_TableView();
            //关闭"业主信息管理 - 家庭成员信息 - 新增"窗口
            close_Windows();
        }
    }
    public void click_CancelButton(){
        //关闭"业主信息管理 - 家庭成员信息 - 新增"窗口
        close_Windows();
    }
    void error_NullFName(){
        //未输入家庭成员姓名时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("家庭成员姓名不能为空，请输入姓名！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_LangFName(){
        //家庭成员姓名超出长度时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("家庭成员姓名超出长度，仅能输入8个字符(每个汉字占2个字符)！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullFSex(){
        //未选择家庭成员性别时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您未选择家庭成员性别，请选择性别！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullFTel(){
        //未输入家庭成员联系方式的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("家庭成员联系方式不能为空，请输入家庭成员联系方式！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_WrongFTel(){
        //家庭成员联系电话(手机号码或者固定电话)输入错误的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("检测到家庭成员联系电话输入错误，请输入正确的电话号码！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullFRelation(){
        //未输入家庭成员与业主关系
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("家庭成员与业主关系不能为空，请输入与业主关系！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_LangFRelation(){
        //家庭成员与业主关系超出长度时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("家庭成员与业主关系超出长度，仅能输入10个字符(每个汉字占2个字符)！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_WrongFID(){
        //家庭成员身份证号码输入错误的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("检测到家庭成员身份证号码输入错误，请输入正确的身份证号码！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullFID(){
        //未输入家庭成员身份证号码的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("家庭成员身份证号码不能为空，请输入身份证号码！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_LangFNote(){
        //家庭成员备注超出长度时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("家庭成员备注超出长度，仅能输入100个字符(每个汉字占2个字符)！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void addFamilyInfoToSQL_NullFNote(){
        //添加数据到数据库(家庭成员备注为空)
        query = "INSERT INTO Family_Info\n" +
                "VALUES\n" +
                "(\'" + FName_TextField.getText() + "\',\'" + FSex_ChoiceBox.getSelectionModel().getSelectedItem() + "\',\'" + FTel_TextField.getText() + "\',\'" + FID_TextField.getText() + "\',\'" + FRelation_TextField.getText() + "\',NULL," + ONo + ");";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void addFamilyInfoToSQL_FullFNote(){
        //添加数据到数据库(家庭成员备注不为空)
        query = "INSERT INTO Family_Info\n" +
                "VALUES\n" +
                "(\'" + FName_TextField.getText() + "\',\'" + FSex_ChoiceBox.getSelectionModel().getSelectedItem() + "\',\'" + FTel_TextField.getText() + "\',\'" + FID_TextField.getText() + "\',\'" + FRelation_TextField.getText() + "\',\'" + FNote_TextArea.getText() + "\''," + ONo + ");";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void flush_TableView(){
        //刷新"业主信息管理 - 家庭成员信息"窗口的TableView
        Controller_OwnerEditRecord controller_ownerEditRecord=(Controller_OwnerEditRecord) StageManager.CONTROLLER.get("Controller_OwnerEditRecord");
        controller_ownerEditRecord.FamilyTableView_List.clear();
        controller_ownerEditRecord.showFamilyTableView();
    }
    void close_Windows(){
        //关闭"业主信息管理 - 家庭成员信息 - 新增"窗口
        //remove"业主信息管理 - 家庭成员信息 - 新增"窗口和其控制器
        StageManager.STAGE.remove("Stage_OwnerEditRecord_NewFamily");
        StageManager.CONTROLLER.remove("Controller_OwnerEditRecord_NewFamily");
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