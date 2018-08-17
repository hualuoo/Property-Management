/*登录软件用户表*/
CREATE TABLE user_table(
Username CHAR(10) NOT NULL,
Password CHAR(32) NOT NULL,
)


/*业主信息    ONo业主编号    OName业主姓名    OSex业主性别    OTel业主电话    OID业主身份证号码    ONote业主备注*/
CREATE TABLE Owner_Info(
ONo CHAR(6) NOT NULL CONSTRAINT O_Prim PRIMARY KEY,
OName CHAR(8) NOT NULL,
OSex NCHAR(2) NOT NULL,
OTel CHAR(13) NOT NULL,
OID CHAR(18) NOT NULL,
ONote CHAR(100))

/*房屋信息    HNo房屋编号    HBuild房屋楼宇号    HPark房屋单元号    HFloor房屋楼层号    HRoom房屋门牌号    HArea房屋面积    HState房屋状态    HType房屋户型    HNote房屋备注    ONo业主编号*/
CREATE TABLE House_Info(
HNo CHAR(12) NOT NULL CONSTRAINT H_Prim PRIMARY KEY,
HBuild INT NOT NULL,
HPark INT NOT NULL,
HFloor INT NOT NULL,
HRoom INT NOT NULL,
HArea INT NOT NULL,
HState CHAR(6) NOT NULL,
HType CHAR(8) NOT NULL,
HNote CHAR(100),
ONo CHAR(6) CONSTRAINT House_Owner_Fore FOREIGN KEY REFERENCES Owner_Info(ONo))

/*车位信息    PNo车位编号    PRegion车位区域    CarNo车牌号    PState车位状态    ONo业主编号*/
CREATE TABLE Parking_Info(
PNo CHAR(6) NOT NULL CONSTRAINT P_Prim PRIMARY KEY,
PRegion CHAR(1) NOT NULL,
CarNo CHAR(8),
PState CHAR(6) NOT NULL,
ONo CHAR(6) CONSTRAINT Parking_Owner_Fore FOREIGN KEY REFERENCES Owner_Info(ONo)
)

/*删除房屋的同时删除业主信息*/
USE Sql_Curriculum_Design
GO
CREATE TRIGGER del_H ON House_Info
AFTER DELETE
AS
	DELETE FROM Owner_Info
	WHERE Owner_Info.ONo
	IN (SELECT ONo FROM DELETED)
GO

/*修改业主ID的同时修改房屋的业主ID*/
USE Sql_Curriculum_Design
GO
CREATE TRIGGER Update_O ON Owner_Info
FOR UPDATE
AS
	IF UPDATE(ONo)
	UPDATE House_Info SET ONo=inserted.ONo
	FROM House_Info,deleted,inserted
	WHERE House_Info.ONo=deleted.ONo
GO