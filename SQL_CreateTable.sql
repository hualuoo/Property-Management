/*登录软件用户表*/
CREATE TABLE user_table(
Username CHAR(10) NOT NULL,
Password CHAR(32) NOT NULL,
)

/*业主信息    ONo业主编号    OName业主姓名    OSex业主性别    OTel业主电话    OID业主身份证号码    ONote业主备注*/
CREATE TABLE Owner_Info(
ONo INT IDENTITY(1,1) NOT NULL CONSTRAINT O_Prim PRIMARY KEY,
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
HType CHAR(12) NOT NULL,
HNote CHAR(100),
ONo INT CONSTRAINT House_Owner_Fore FOREIGN KEY REFERENCES Owner_Info(ONo))

/*车位信息    PNo车位编号    PRegion车位区域    PState车位状态    CarNo车牌号    PNote车位备注    ONo业主编号*/
CREATE TABLE Parking_Info(
PNo CHAR(6) NOT NULL CONSTRAINT P_Prim PRIMARY KEY,
PRegion CHAR(1) NOT NULL,
PState CHAR(6) NOT NULL,
CarNo CHAR(8),
PNote CHAR(100),
ONo INT CONSTRAINT Parking_Owner_Fore FOREIGN KEY REFERENCES Owner_Info(ONo)
)

/*维修信息    RNo编号    RSubDate提交日期    RTitle标题    RText正文    RState状态    RReply回复    RSolveDate解决日期    ONo业主编号*/
CREATE TABLE Repair_Info(
RNo INT IDENTITY(1,1) NOT NULL CONSTRAINT R_Prim PRIMARY KEY,
RSubDate DATE NOT NULL,
RTitle CHAR(20) NOT NULL,
RText CHAR(200) NOT NULL,
RState CHAR(6) NOT NULL,
RReply CHAR(200),
RSolveDate DATE,
ONo INT CONSTRAINT Repair_Owner_Fore FOREIGN KEY REFERENCES Owner_Info(ONo)
)

/*投诉信息    RNo编号    RSubDate提交日期    RTitle标题    RText正文    RState状态    RReply回复    RSolveDate解决日期    ONo业主编号*/
CREATE TABLE Complaint_Info(
CNo INT IDENTITY(1,1) NOT NULL CONSTRAINT C_Prim PRIMARY KEY,
CSubDate DATE NOT NULL,
CTitle CHAR(20) NOT NULL,
CText CHAR(200) NOT NULL,
CState CHAR(6) NOT NULL,
CReply CHAR(200),
CSolveDate DATE,
ONo INT CONSTRAINT Complaint_Owner_Fore FOREIGN KEY REFERENCES Owner_Info(ONo)
)

/*家庭成员信息    FNo家庭成员编号    FName家庭成员姓名    FSex家庭成员性别    FTel家庭成员电话    FID家庭成员身份证号码    FRelation与业主关系    FNote家庭成员备注    ONo业主编号*/
CREATE TABLE Family_Info(
FNo INT IDENTITY(1,1) NOT NULL CONSTRAINT F_Prim PRIMARY KEY,
FName CHAR(8) NOT NULL,
FSex NCHAR(2) NOT NULL,
FTel CHAR(13) NOT NULL,
FID CHAR(18) NOT NULL,
FRelation CHAR(10) NOT NULL,
FNote CHAR(100),
ONo INT CONSTRAINT Family_Owner_Fore FOREIGN KEY REFERENCES Owner_Info(ONo))

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