package controller;

import data.Data_HouseTable;
import util.SQL_Connect;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.stage.Stage;
import util.StageManager;

import java.sql.ResultSet;
import java.util.Optional;
import java.util.regex.Pattern;

public class Controller_IndexNewHouse {
    public Label HNo_Label,HAreaError_Label,HInfoError_Label,OIDError_Label;
    public TextField HBuild_TextField,HPark_TextField,HFloor_TextField,HRoom_TextField,HArea_TextField,HType_TextField;
    public TextField ONo_TextField,OName_TextField,OTel_TextField,OID_TextField;
    public TextArea HNote_TextArea,ONote_TextArea;
    public ChoiceBox HState_ChoiceBox,OSex_ChoiceBox;
    public Button Search_Button,Confirm_Button,Exit_Button;
    //检查是否搜索
    int check_Search;
    //数据库代码以及返回结果
    String query;
    ResultSet result;

    public void initialize() {
        //初始化
        //将"房屋管理 - 新增"控制器保存到map中
        StageManager.CONTROLLER.put("Controller_IndexNewHouse", this);
        //默认未搜索
        check_Search = 0;
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
        //添加房屋窗口的房屋销售情况选择框默认为"未销售"
        HState_ChoiceBox.setValue("未销售");

        //文本框文字修改监听
        HBuild_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                create_HNo();
            }
        });
        HPark_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                create_HNo();
            }
        });
        HFloor_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                create_HNo();
            }
        });
        HRoom_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                create_HNo();
            }
        });
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
    public void create_HNo(){
        //根据输入的房屋信息生成编号
        HNo_Label.setText(HBuild_TextField.getText() + "#" + HPark_TextField.getText() + "-" + HRoom_TextField.getText());
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
    public void click_ConfirmButton(){
        //按下"确认"按钮
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您确认是否添加该条信息？");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        Optional<ButtonType> alertresult = alert.showAndWait();
        if (alertresult.get() != ButtonType.OK) {
            return;
        }
        if (HBuild_TextField.getText()==null || HBuild_TextField.getText().length()==0){
            //如果未输入房屋楼宇号
            error_NullHBuild();
            return;
        }
        String regex = "[0-9]*";
        if (Pattern.compile(regex).matcher(HBuild_TextField.getText()).matches() == false){
            //房屋楼宇号输入错误
            error_WrongHBuild();
            return;
        }
        if (HBuild_TextField.getText().length()>4){
            //房屋楼宇号超出长度
            error_LangHBuild();
            return;
        }
        if (HPark_TextField.getText()==null || HPark_TextField.getText().length()==0){
            //如果未输入房屋单元号
            error_NullHPark();
            return;
        }
        if (Pattern.compile(regex).matcher(HPark_TextField.getText()).matches() == false){
            //房屋单元号输入错误
            error_WrongHPark();
            return;
        }
        if (HPark_TextField.getText().length()>3){
            //房屋单元号超出长度
            error_LangHPark();
            return;
        }
        if (HFloor_TextField.getText()==null || HFloor_TextField.getText().length()==0){
            //如果未输入房屋楼层号
            error_NullHFloor();
            return;
        }
        if (Pattern.compile(regex).matcher(HFloor_TextField.getText()).matches() == false){
            //房屋楼层号输入错误
            error_WrongHFloor();
            return;
        }
        if (HFloor_TextField.getText().length()>3){
            //房屋楼层号超出长度
            error_LangHFloor();
            return;
        }
        if (HRoom_TextField.getText()==null || HRoom_TextField.getText().length()==0){
            //如果未输入房屋门牌号
            error_NullHRoom();
            return;
        }
        if (Pattern.compile(regex).matcher(HRoom_TextField.getText()).matches() == false){
            //房屋门牌号输入错误
            error_WrongHRoom();
            return;
        }
        if (HRoom_TextField.getText().length()>5){
            //房屋门牌号超出长度
            error_LangHRoom();
            return;
        }
        if (HArea_TextField.getText()==null || HArea_TextField.getText().length()==0){
            //如果未输入房屋面积
            error_NullHArea();
            return;
        }
        if (Pattern.compile(regex).matcher(HArea_TextField.getText()).matches() == false){
            //房屋面积输入错误
            error_WrongHArea();
            return;
        }
        if (HArea_TextField.getText().length()>4){
            //房屋面积超出长度
            error_LangHArea();
            return;
        }
        if (HState_ChoiceBox.getSelectionModel().getSelectedItem()==null || HState_ChoiceBox.getSelectionModel().getSelectedItem().toString().length()==0){
            //如果未选择房屋状态
            error_NullHState();
            return;
        }
        if (HType_TextField.getText()==null || HType_TextField.getText().length()==0){
            //如果未输入房屋户型
            error_NullHType();
            return;
        }
        if (length(HType_TextField.getText())>13){
            //房屋户型超出长度
            error_LangHType();
            return;
        }
        query = "SELECT * FROM House_Info WHERE HNo=\'" + HNo_Label.getText() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        try {
            if (result.next() == true) {
                //如果数据库已有该房屋编号
                error_HaveHNo();
                return;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //ResultSet的next方法需要try-catch输出报错
        if(HState_ChoiceBox.getSelectionModel().getSelectedItem().toString().equals("未销售")){
            //房屋状态"未销售"
            if (HNote_TextArea.getText()==null || HType_TextField.getText().length()==0){
                //添加数据到数据库(未销售,备注为空)
                addHouseInfoToSQL_NoSellNullNote();
                //刷新"房屋信息管理"窗口的TableView
                flush_TableView();
                //添加成功弹窗
                succeedAdd();
                //关闭"业主信息管理- 新增"窗口
                close_Windows();
            }
            else {
                //添加数据到数据库(未销售,备注不为空)
                addHouseInfoToSQL_NoSellFullNote();
                //刷新"房屋信息管理"窗口的TableView
                flush_TableView();
                //添加成功弹窗
                succeedAdd();
                //关闭"业主信息管理- 新增"窗口
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
                //添加数据到数据库(已销售,备注为空)
                addHouseInfoToSQL_YesSellNullNote();
                //刷新"房屋信息管理"窗口的TableView
                flush_TableView();
                //添加成功弹窗
                succeedAdd();
                //关闭"业主信息管理- 新增"窗口
                close_Windows();
            }
            else {
                //添加数据到数据库(已销售,备注不为空)
                addHouseInfoToSQL_YesSellFullNote();
                //刷新"房屋信息管理"窗口的TableView
                flush_TableView();
                //添加成功弹窗
                succeedAdd();
                //关闭"业主信息管理- 新增"窗口
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
    void error_NullHBuild(){
        //未输入房屋楼宇号时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋楼宇号不能为空，请输入楼宇号！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_WrongHBuild(){
        //房屋楼宇号输入错误的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋楼宇号仅能为数字，请输入正确的楼宇号！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_LangHBuild(){
        //房屋楼宇号超出长度时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋楼宇号超出长度，最高仅能输入3位数字！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullHPark(){
        //未输入房屋单元号时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋单元号不能为空，请输入单元号！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_WrongHPark(){
        //房屋单元号输入错误的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋单元号仅能为数字，请输入正确的单元号！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_LangHPark(){
        //房屋单元号超出长度时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋单元号超出长度，最高仅能输入2位数字！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullHFloor(){
        //未输入房屋楼层号时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋楼层号不能为空，请输入楼层号！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_WrongHFloor(){
        //房屋楼层号输入错误的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋楼层号仅能为数字，请输入正确的楼层号！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_LangHFloor(){
        //房屋楼层号超出长度时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋楼层号超出长度，最高仅能输入2位数字！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullHRoom(){
        //未输入房屋门牌号时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋门牌号不能为空，请输入门牌号！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_WrongHRoom(){
        //房屋门牌号输入错误的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋门牌号仅能为数字，请输入正确的门牌号！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_LangHRoom(){
        //房屋门牌号超出长度时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("房屋门牌号超出长度，最高仅能输入4位数字！");
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
    void error_HaveHNo(){
        //数据库中已有该编号房屋信息
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("数据库中已有编号为" + HNo_Label.getText() + "的房屋信息！");
        alert.initOwner(Confirm_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void addHouseInfoToSQL_NoSellNullNote(){
        //添加数据到数据库(未销售,备注为空)
        query = "INSERT INTO House_Info VALUES" +
                "(\'" + HNo_Label.getText().trim() + "\'," + HBuild_TextField.getText().trim() + "," + HPark_TextField.getText().trim() + "," +
                HFloor_TextField.getText().trim() + "," + HRoom_TextField.getText().trim() + "," + HArea_TextField.getText().trim() + ",\'" +
                HState_ChoiceBox.getSelectionModel().getSelectedItem() + "\',\'" + HType_TextField.getText().trim() + "\',NULL,NULL);";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void addHouseInfoToSQL_NoSellFullNote(){
        //添加数据到数据库(未销售,备注不为空)
        query = "INSERT INTO House_Info VALUES" +
                "(\'" + HNo_Label.getText() + "\'," + HBuild_TextField.getText() + "," + HPark_TextField.getText() + "," +
                HFloor_TextField.getText() + "," + HRoom_TextField.getText() + "," + HArea_TextField.getText() + ",\'" +
                HState_ChoiceBox.getSelectionModel().getSelectedItem() + "\',\'" + HType_TextField.getText() + "\',\'" + HNote_TextArea.getText() + "\',NULL);";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void addHouseInfoToSQL_YesSellNullNote(){
        //添加数据到数据库(已销售,备注为空)
        query = "INSERT INTO House_Info VALUES" +
                "(\'" + HNo_Label.getText().trim() + "\'," + HBuild_TextField.getText().trim() + "," + HPark_TextField.getText().trim() + "," +
                HFloor_TextField.getText().trim() + "," + HRoom_TextField.getText().trim() + "," + HArea_TextField.getText().trim() + ",\'" +
                HState_ChoiceBox.getSelectionModel().getSelectedItem() + "\',\'" + HType_TextField.getText().trim() + "\',NULL," + ONo_TextField.getText() + ");";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void addHouseInfoToSQL_YesSellFullNote(){
        //添加数据到数据库(已销售,备注不为空)
        query = "INSERT INTO House_Info VALUES" +
                "(\'" + HNo_Label.getText() + "\'," + HBuild_TextField.getText() + "," + HPark_TextField.getText() + "," +
                HFloor_TextField.getText() + "," + HRoom_TextField.getText() + "," + HArea_TextField.getText() + ",\'" +
                HState_ChoiceBox.getSelectionModel().getSelectedItem() + "\',\'" + HType_TextField.getText() + "\',\'" + HNote_TextArea.getText() + "\'," + ONo_TextField.getText() + ");";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void flush_TableView(){
        //刷新"房屋信息管理"窗口的TableView
        Controller_IndexMain controller_indexMain=(Controller_IndexMain) StageManager.CONTROLLER.get("Controller_IndexMain");
        controller_indexMain.HouseTableData_List.clear();
        controller_indexMain.showHouseTable("初始化");
    }
    void succeedAdd(){
        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert2.setTitle("小区物业管理系统");
        alert2.setHeaderText("信息添加成功！");
        alert2.initOwner(Confirm_Button.getScene().getWindow());
        alert2.showAndWait();
        close_Windows();
    }
    public void click_ExitButton(){
        close_Windows();
    }
    public void close_Windows(){
        StageManager.STAGE.remove("Stage_IndexNewHouse");
        StageManager.CONTROLLER.remove("Controller_IndexNewHouse");
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