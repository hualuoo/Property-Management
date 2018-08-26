package controller;

import application.Main;
import util.StageManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class Controller_ChargeMain {
    public Label LoginUser_Label;
    public void initialize() {
        //初始化
        //将"报修单管理-主界面"控制器保存到map中
        StageManager.CONTROLLER.put("Controller_ChargeMain", this);
        //显示操作员用户名
        LoginUser_Label.setText("操作员：" + Main.loginUser);
    }
    public void click_IndexToggleButton(){
        //主界面-房屋管理 界面切换
        try {
            Parent Index_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_IndexMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-房屋管理");
            Main.Login_Stage.setScene(new Scene(Index_Root, 1000, 615));
            StageManager.CONTROLLER.remove("Controller_ChargeMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_OwnerToggleButton(){
        //业主管理 界面切换
        try {
            Parent Family_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_OwnerMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-业主管理界面");
            Main.Login_Stage.setScene(new Scene(Family_Root, 1000, 615));
            StageManager.CONTROLLER.remove("Controller_ChargeMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_CarToggleButton(){
        //车位管理 界面切换
        try {
            Parent Car_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_CarMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-车位管理");
            Main.Login_Stage.setScene(new Scene(Car_Root, 1000, 615));
            StageManager.CONTROLLER.remove("Controller_ChargeMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_RepairToggleButton(){
        //报修管理 界面切换
        try {
            Parent Repair_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_RepairMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-报修管理");
            Main.Login_Stage.setScene(new Scene(Repair_Root, 1000, 615));
            StageManager.CONTROLLER.remove("Controller_ChargeMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_ComplaintToggleButton(){
        //投诉管理 界面切换
        try {
            Parent Complaint_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_ComplaintMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-投诉管理");
            Main.Login_Stage.setScene(new Scene(Complaint_Root, 1000, 615));
            StageManager.CONTROLLER.remove("Controller_ChargeMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
