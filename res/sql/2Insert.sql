--Version 2

--写入数据
use MovieDB
go

/*
	写入电影数据
*/
insert into Movie values ('01','终结者1','詹姆斯·卡梅隆/乔纳森·莫斯托等','阿诺·施瓦辛格等','terminator1.jpg','科幻片',30,'这是一个未来的世界，天下已经由机器人来操控。机器人想完全占有这个世界，把人类赶尽杀绝，然而却遇到了顽强抵抗的人类精英康纳。于是，终结者机器人T-800受命回到1984年，杀害康纳母亲莎拉，目的是灭掉康纳的出生。 康纳得知后，火速派战士雷斯前往救援。雷斯来到1984年的洛杉矶，及时搭救了被机器人追杀的莎拉——她当时还是一个大学生。然而，莎拉把雷斯当成疯子，不相信未来机器人统治世界。 直到莎拉又一次遭到机器人追击，她才相信了这一切。奔走中她和雷斯相爱，怀上了未来的康纳，而雷斯也陷入和机器人的苦斗当中。人类世界能否从因为这场斗争改变原来的噩运？')
insert into Movie values (
	'02','的士速递','吉拉尔·皮雷','萨米·纳塞利，玛丽昂·歌迪亚，佛瑞德瑞克·迪分索，伯尔纳法西',
	'taxi1.jpg',
	'喜剧片',30,
	'一个出租车司机因为在限速30英里的区域飞车120英里，被警察逮着，其后跟警察搭档，参与侦破一个涉嫌抢银行的国际团伙。该团伙每次都乘坐一辆耀眼的红色奔驰车逃离现场'
)
insert into Movie values (
	'03','异次元杀阵1','文森佐·纳塔利','尼科勒·德搏尔 ， 尼基·瓜达尼，妮可·德波儿，大卫·休莱特， Andrew Miller，Julian Richings',
	'cube1.jpg',
	'恐怖片',20,
	'警司昆廷、监狱专家兼传感器专家伦尼斯、医生霍洛韦、建筑师沃思、数学系的大学生利文和身患孤独症的学者卡赞，六个素不相识的人，一觉醒来后发现一同身处于由一个个形状相同的立方体组成的结构复杂的高度精密迷宫中。他们唯一的出路只有逃离这座迷宫，然而，一个个立方体尽管外貌相同，里面的机关却各不一样，复杂异常。依靠利文数学知识的推理，他们终于以为发现迷宫的运行规律，凭此一次次解开各个机关，迷宫的边缘近在眼前。当他们正欲合力逃出迷宫之际，意外发生了。'
)
insert into Movie values (
	'04','头号玩家','史蒂文·斯皮尔伯格','泰伊·谢里丹、奥利维亚·库克、西蒙·佩吉、本·门德尔森、马克·里朗斯、T·J·米勒',
	'头号玩家.jpg',
	'科幻片',45,
	'该片根据恩斯特·克莱恩同名小说改编，讲述了一个现实生活中无所寄托、沉迷游戏的大男孩，凭着对虚拟游戏设计者的深入剖析，历经磨难，找到隐藏在关卡里的三把钥匙，成功通关游戏，并且还收获了网恋女友的故事'
)

--模板
/*
insert into Movie values (
	'序号','电影名','导演','主演',
	'封面图片名',
	'电影类型',99999999,
	'电影简介'
)
*/

/*
	写入放映厅
*/
insert into Theater values ('01','天空厅','5x5')
insert into Theater values ('02','海洋厅','5x5')
insert into Theater values ('03','茉莉厅','5x5')
insert into Theater values ('04','远山厅','4x4')
insert into Theater values ('05','浅湖厅','2x2')

--模板
/*
insert into Theater values ('ID','放映厅名',100)
*/

/*
	写入放映计划
*/
insert into Schedule values ('01','2020-05-31 19:00:00','01','01')
insert into Schedule values ('02','2020-05-31 20:30:00','02','01')
insert into Schedule values ('03','2020-06-01 19:00:00','03','02')
insert into Schedule values ('04','2020-06-01 20:00:00','04','02')
insert into Schedule values ('05','2020-05-31 19:00:00','01','03')
insert into Schedule values ('06','2020-05-31 20:30:00','02','03')
insert into Schedule values ('07','2020-06-01 19:00:00','03','04')
insert into Schedule values ('08','2020-06-01 20:00:00','04','04')

--模板
/*
insert into Schedule values ('计划ID','yyyy-mm-dd hh:mm:ss','电影ID','放映厅ID')
*/

/*
	写入测试用户
*/
insert into Users values ('0','???',NULL,NULL) 
insert into Users values ('1','guest','10000','普通用户')
insert into Users values ('2','admin','10086','管理员')

/*
	写入电影票
*/
insert into Ticket values ('01','0',1,1,'01','未售')
insert into Ticket values ('02','0',1,1,'01','已售')
insert into Ticket values ('03',NULL,2,2,'02','未售')
insert into Ticket values ('04',NULL,2,4,'03','未售')
insert into Ticket values ('05','1',2,2,'04','已售')
insert into Ticket values ('06',NULL,2,4,'04','未售')
insert into Ticket values ('07',NULL,2,6,'04','未售')

--模板
/*
insert into Ticket values ('票ID','用户ID',行,列,'计划ID','未售')
*/