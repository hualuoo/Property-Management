INSERT INTO user_table
VALUES
('root','e10adc3949ba59abbe56e057f20f883e');

INSERT INTO Owner_Info
VALUES
('O00001','张三','男','13111111111','100100199001011211','暂无'),
('O00002','李四','男','0580-1111111','100100199001011311','暂无');

INSERT INTO House_Info
VALUES
('1#1-101',1,1,1,101,100,'已销售','三室一厅',NULL,'O00001'),
('2#2-201',2,2,2,201,80,'未销售','三室一厅',NULL,NULL),
('3#1-402',3,1,4,402,96,'待入住','三室一厅',NULL,'O00002');

INSERT INTO Parking_Info
VALUES
('A-1','A','已出租','粤S00001',NULL,'O00001'),
('B-13','B','未出售',NULL,NULL,NULL),
('C-2','C','已出租','粤S00002',NULL,'O00002');