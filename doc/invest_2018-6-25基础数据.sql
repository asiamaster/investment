-- --------------------------------------------------------
-- 主机:                           localhost
-- 服务器版本:                        5.7.18 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 invest 的数据库结构
CREATE DATABASE IF NOT EXISTS `invest` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `invest`;

-- 导出  表 invest.bank_card 结构
CREATE TABLE IF NOT EXISTS `bank_card` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `card_type` int(2) DEFAULT NULL COMMENT '银行卡类型##默认选中储蓄卡，目前仅“储蓄卡”##{provider:"cardTypeProvider", data:[{value:1,text:"储蓄卡"},{value:2, text:"信用卡"}]}',
  `account_name` varchar(20) DEFAULT NULL COMMENT '开户名',
  `id_number` varchar(40) DEFAULT NULL COMMENT '身份证号',
  `subbranch` varchar(40) DEFAULT NULL COMMENT '所属支行##选填，允许5-50位汉字、字母和数字，不允许特殊',
  `card_number` varchar(30) DEFAULT NULL COMMENT '银行卡号##允许填写8-30位纯数字',
  `bank` varchar(50) DEFAULT NULL COMMENT '开户行##数据字典:中国银行，建设银行，招商银行##',
  `is_default` int(2) DEFAULT NULL COMMENT '是否默认##{provider:"YesOrNoProvider"}',
  `is_depository` int(2) DEFAULT NULL COMMENT '是否存管帐户##{provider:"YesOrNoProvider"}',
  `add_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='银行卡信息';

-- 导出  表 invest.biz_number 结构
CREATE TABLE IF NOT EXISTS `biz_number` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL,
  `value` bigint(20) NOT NULL DEFAULT '1',
  `memo` varchar(100) DEFAULT NULL,
  `version` varchar(20) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='记录所有业务的编号\r\n如：\r\n回访编号:HF201712080001\r\n';

-- 导出  表 invest.data_auth 结构
CREATE TABLE IF NOT EXISTS `data_auth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `type` varchar(20) NOT NULL COMMENT '数据权限分类##这个由具体的业务系统确定',
  `data_id` varchar(50) DEFAULT NULL COMMENT '数据权限ID##业务编号',
  `parent_data_id` varchar(50) DEFAULT '0' COMMENT '父级数据ID##外键，关联父级数据权限',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='数据权限';

-- 正在导出表  invest.data_auth 的数据：~2 rows (大约)
DELETE FROM `data_auth`;
/*!40000 ALTER TABLE `data_auth` DISABLE KEYS */;
INSERT INTO `data_auth` (`id`, `name`, `type`, `data_id`, `parent_data_id`, `description`) VALUES
	(1, '所有人', 'dataRange', '1', NULL, '成都市场个人'),
	(2, '个人', 'dataRange', '0', NULL, '成都市场所有人');
/*!40000 ALTER TABLE `data_auth` ENABLE KEYS */;

-- 导出  表 invest.data_dictionary 结构
CREATE TABLE IF NOT EXISTS `data_dictionary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL COMMENT '系统描述',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `system_code` varchar(20) DEFAULT '1' COMMENT '系统编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='数据字典\r\n例:\r\n系统图片:IMAGE_CODE';

-- 正在导出表  invest.data_dictionary 的数据：~5 rows (大约)
DELETE FROM `data_dictionary`;
/*!40000 ALTER TABLE `data_dictionary` DISABLE KEYS */;
INSERT INTO `data_dictionary` (`id`, `code`, `name`, `description`, `created`, `modified`, `system_code`) VALUES
	(1, 'data_auth_type', '数据权限类型', '数据权限类型', '2017-10-27 10:02:42', '2018-06-22 14:46:29', 'PARK'),
	(2, 'system', '系统', '系统', '2017-11-13 11:23:05', '2018-06-22 14:46:35', 'INVEST'),
	(3, 'priority', '优先级', '优先级', '2017-11-21 15:57:18', '2018-06-22 14:46:36', 'INVEST'),
	(4, 'bank', '开户行', '开户行', '2018-01-19 14:47:53', '2018-06-22 14:46:37', 'INVEST'),
	(5, 'security_level', '安全级别', '投资平台安全级别', '2018-01-19 20:10:11', '2018-06-22 14:46:38', 'INVEST');
/*!40000 ALTER TABLE `data_dictionary` ENABLE KEYS */;

-- 导出  表 invest.data_dictionary_value 结构
CREATE TABLE IF NOT EXISTS `data_dictionary_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级id',
  `dd_code` varchar(50) DEFAULT NULL COMMENT '数据字典编码',
  `order_number` int(11) DEFAULT NULL COMMENT '排序号',
  `name` varchar(40) DEFAULT NULL COMMENT '名称',
  `code` varchar(40) DEFAULT NULL COMMENT '编码',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=186 DEFAULT CHARSET=utf8 COMMENT='数据字典值';

-- 正在导出表  invest.data_dictionary_value 的数据：~32 rows (大约)
DELETE FROM `data_dictionary_value`;
/*!40000 ALTER TABLE `data_dictionary_value` DISABLE KEYS */;
INSERT INTO `data_dictionary_value` (`id`, `parent_id`, `dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`) VALUES
	(31, NULL, 'priority', 1, '最高', 'highest', '最高', '2017-11-21 17:14:17', '2018-06-22 10:38:10'),
	(32, NULL, 'priority', 2, '高', 'high', '高', '2017-11-21 17:14:47', '2018-06-22 10:38:10'),
	(33, NULL, 'priority', 3, '常规', 'normal', '常规', '2017-11-21 17:15:02', '2018-06-22 10:38:10'),
	(34, NULL, 'priority', 4, '低', 'low', '低', '2017-11-21 17:15:23', '2018-06-22 10:38:10'),
	(35, NULL, 'system', 1, '投资理财管理系统', 'invest', '投资理财管理系统', '2017-11-13 14:40:31', '2018-06-22 10:38:35'),
	(159, NULL, 'bank', 2, '中国建设银行', 'CBC', '中国建设银行', '2018-01-19 14:49:10', '2018-06-22 10:38:53'),
	(160, NULL, 'bank', 1, '中国银行', 'BC', '中国银行', '2018-01-19 14:50:47', '2018-06-22 10:38:53'),
	(161, NULL, 'bank', 3, '中国农业银行', 'ABC', '中国农业银行', '2018-01-19 14:51:02', '2018-06-22 10:38:53'),
	(162, NULL, 'bank', 4, '中国工商银行', 'ICBC', '中国工商银行', '2018-01-19 14:51:19', '2018-06-22 10:38:53'),
	(163, NULL, 'bank', 5, '民生银行', 'CMSB', '民生银行', '2018-01-19 14:51:46', '2018-06-22 10:38:53'),
	(164, NULL, 'bank', 6, '招商银行', 'CMBC', '招商银行', '2018-01-19 14:52:07', '2018-06-22 10:38:53'),
	(165, NULL, 'bank', 7, '兴业银行', 'CIB', '兴业银行', '2018-01-19 14:52:37', '2018-06-22 10:38:53'),
	(166, NULL, 'bank', 8, '汇丰银行', 'HSBC', '汇丰银行', '2018-01-19 14:53:06', '2018-06-22 10:38:53'),
	(167, NULL, 'bank', 9, '国家开发银行', 'CDB', '国家开发银行', '2018-01-19 14:53:34', '2018-06-22 10:38:53'),
	(168, NULL, 'bank', 10, '北京市商业银行', 'BOB', '北京市商业银行', '2018-01-19 14:54:08', '2018-06-22 10:38:53'),
	(169, NULL, 'bank', 11, '中信银行', 'CCB', '中信银行', '2018-01-19 14:54:49', '2018-06-22 10:38:53'),
	(170, NULL, 'bank', 12, '中国光大银行', 'CEB', '中国光大银行', '2018-01-19 14:55:04', '2018-06-22 10:38:53'),
	(171, NULL, 'bank', 13, '上海浦东发展银行', 'SPDB', '上海浦东发展银行', '2018-01-19 14:55:27', '2018-06-22 10:38:53'),
	(172, NULL, 'bank', 14, '交通银行', 'BCM', '交通银行', '2018-01-19 14:55:46', '2018-06-22 10:38:53'),
	(173, NULL, 'bank', 15, '广东发展银行', 'GDB', '广东发展银行', '2018-01-19 14:56:13', '2018-06-22 10:38:53'),
	(174, NULL, 'bank', 16, '华夏银行', 'HXB', '华夏银行', '2018-01-19 14:57:51', '2018-06-22 10:38:53'),
	(175, NULL, 'bank', 17, '中国农村信用社', 'CUB', '中国农村信用社', '2018-01-19 14:58:14', '2018-06-22 10:38:53'),
	(176, NULL, 'bank', 18, '江西银行', 'JXB', '江西银行', '2018-01-19 14:58:53', '2018-06-22 10:38:53'),
	(177, NULL, 'security_level', 1, '保守型', '1', '保守型', '2018-01-19 20:10:45', '2018-06-22 10:39:08'),
	(178, NULL, 'security_level', 2, '稳健型', '2', '稳健型', '2018-01-19 20:11:00', '2018-06-22 10:39:08'),
	(179, NULL, 'security_level', 3, '进取型', '3', '进取型', '2018-01-19 20:11:23', '2018-06-22 10:39:08'),
	(180, NULL, 'security_level', 4, '激进型', '4', '激进型', '2018-01-19 20:11:59', '2018-06-22 10:39:08'),
	(181, NULL, 'security_level', 5, '需警惕', '5', '需警惕', '2018-01-19 20:12:15', '2018-06-22 10:39:08'),
	(182, NULL, 'bank', 19, '厦门国际银行', 'XIB', '厦门国际银行', '2018-01-23 17:08:56', '2018-06-22 10:38:53'),
	(183, NULL, 'bank', 20, '华瑞银行', 'HRB', '华瑞银行', '2018-01-26 11:40:15', '2018-06-22 10:38:53'),
	(184, NULL, 'bank', 21, '恒丰银行', 'HFB', '恒丰银行', '2018-02-03 11:51:35', '2018-06-22 10:38:53'),
	(185, NULL, 'data_auth_type', 1, '数据范围', 'dataRange', '个人或所有人', '2018-06-25 16:48:02', '2018-06-25 16:48:02');
/*!40000 ALTER TABLE `data_dictionary_value` ENABLE KEYS */;

-- 导出  表 invest.department 结构
CREATE TABLE IF NOT EXISTS `department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级部门id',
  `firm_code` varchar(50) DEFAULT NULL COMMENT '所属市场',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `code` varchar(20) DEFAULT NULL COMMENT '编号',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='部门或组织机构';

-- 正在导出表  invest.department 的数据：~1 rows (大约)
DELETE FROM `department`;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` (`id`, `parent_id`, `firm_code`, `name`, `code`, `description`, `created`, `modified`) VALUES
	(1, NULL, 'group', '财务部', 'group-1', '财务部', '2018-06-07 08:52:06', '2018-06-25 16:51:44');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;

-- 导出  表 invest.firm 结构
CREATE TABLE IF NOT EXISTS `firm` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '名称',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级id',
  `code` varchar(20) DEFAULT NULL COMMENT '编号',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='公司(或市场)\r\n比如顶级为地利集团，下面为市场';

-- 正在导出表  invest.firm 的数据：~2 rows (大约)
DELETE FROM `firm`;
/*!40000 ALTER TABLE `firm` DISABLE KEYS */;
INSERT INTO `firm` (`id`, `name`, `parent_id`, `code`, `description`, `created`, `modified`) VALUES
	(1, '集团', NULL, 'group', 'group', '2018-05-23 10:52:43', '2018-05-25 17:14:23'),
	(2, '成都', 1, 'cd', 'cd', '2018-05-22 16:38:04', '2018-06-25 16:46:55');
/*!40000 ALTER TABLE `firm` ENABLE KEYS */;

-- 导出  过程 invest.getChildDepartments 结构
DELIMITER //
CREATE DEFINER=`root`@`%` PROCEDURE `getChildDepartments`(IN `rootId` BIGINT)
    COMMENT '获取子项目(包含当前参数id)'
BEGIN
		DECLARE sTemp VARCHAR(1000);
           DECLARE sTempChd VARCHAR(1000);
           SET sTemp = '$';
           SET sTempChd =cast(rootId as CHAR);
          WHILE sTempChd is not null DO
             SET sTemp = concat(sTemp,',',sTempChd);
             SELECT group_concat(id) INTO sTempChd FROM department where FIND_IN_SET(parent_id,sTempChd)>0;
          END WHILE;
          select * from department WHERE FIND_IN_SET(id,substring(sTemp,1));
END//
DELIMITER ;

-- 导出  函数 invest.getParentMenus 结构
DELIMITER //
CREATE DEFINER=`root`@`%` FUNCTION `getParentMenus`(
	`rootId` VARCHAR(20)
) RETURNS varchar(1000) CHARSET utf8
BEGIN
DECLARE pid varchar(100) default '';
DECLARE str varchar(1000) default rootId;

WHILE rootId is not null  do
    SET pid =(SELECT parent_id FROM menu WHERE id = rootId);
    IF pid is not null THEN
        SET str = concat(str, ',', pid);
        SET rootId = pid;
    ELSE
        SET rootId = pid;
    END IF;
END WHILE;
return str;
END//
DELIMITER ;

-- 导出  表 invest.investment 结构
CREATE TABLE IF NOT EXISTS `investment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `investor_id` bigint(20) DEFAULT NULL COMMENT '投资人id',
  `platform_id` bigint(20) DEFAULT NULL COMMENT '投资平台id',
  `project_name` varchar(50) DEFAULT NULL COMMENT '项目名称',
  `start_date` date DEFAULT NULL COMMENT '起始时间',
  `end_date` date DEFAULT NULL COMMENT '结束时间',
  `project_duration` int(11) DEFAULT NULL COMMENT '项目期限',
  `project_duration_unit` int(11) DEFAULT NULL COMMENT '项目期限单位##天/周/月/季/年##{provider:"projectDurationUnitProvider", data:[{value:1,text:"天"},{value:2, text:"周"},{value:3, text:"月"},{value:4, text:"季"},{value:5, text:"年"}]}',
  `profit_ratio` float(6,2) DEFAULT '0.00' COMMENT '年化收益率##单位(%)',
  `deducted` bigint(20) DEFAULT '0' COMMENT '已抵扣##已使用的直接抵扣的满减券',
  `interest_coupon` float(6,2) DEFAULT '0.00' COMMENT '加息券##年化收益加息券,单位(%)',
  `cash_coupon` bigint(20) DEFAULT '0' COMMENT '现金券##充值返的现金券或红包，用于下次投资',
  `investment` bigint(20) DEFAULT '0' COMMENT '投资额',
  `is_remind` int(11) DEFAULT '0' COMMENT '是否到期提醒##{provider:"YesOrNoProvider", data:[{value:0,text:"否"},{value:1, text:"是"}]}',
  `bank_card_id` bigint(20) DEFAULT NULL COMMENT '投资卡',
  `notes` varchar(250) DEFAULT NULL COMMENT '备注信息',
  `balance_due` bigint(20) DEFAULT NULL COMMENT '到期余额',
  `arrived` bigint(20) DEFAULT '0' COMMENT '到期金额',
  `is_expired` int(11) DEFAULT '0' COMMENT '是否到期',
  `repayment_method` tinyint(4) DEFAULT '1' COMMENT '还款方式: 到期还款，每月付息，等额本息',
  `month_index` tinyint(4) DEFAULT '0' COMMENT '还款月序号，从1开始，默认为0',
  `repayment_day` int(11) DEFAULT NULL COMMENT '还款日(每月几号)',
  `created_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  `modified_id` bigint(20) DEFAULT NULL COMMENT '修改人',
  `created` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8 COMMENT='投资理财记录';

-- 导出  表 invest.investment_platform 结构
CREATE TABLE IF NOT EXISTS `investment_platform` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `supervision_bank` varchar(20) DEFAULT NULL COMMENT '资金监管银行',
  `is_record` int(4) DEFAULT NULL COMMENT '是否备案',
  `registry_place` varchar(10) DEFAULT NULL COMMENT '注册地',
  `business_place` varchar(120) DEFAULT NULL COMMENT '经营地',
  `platform_background` varchar(120) DEFAULT NULL COMMENT '平台背景',
  `security_level` int(4) DEFAULT NULL COMMENT '安全级别',
  `avg_profit_ratio` float(4,2) DEFAULT '0.00' COMMENT '平均收益率',
  `business_mode` varchar(120) DEFAULT NULL COMMENT '业务模式',
  `flexible` varchar(120) DEFAULT NULL COMMENT '灵活度',
  `guarantee` varchar(60) DEFAULT NULL COMMENT '保障',
  `introduction` varchar(250) DEFAULT NULL COMMENT '平台介绍',
  `uptime` date DEFAULT NULL COMMENT '上线时间',
  `notes` varchar(250) DEFAULT NULL COMMENT '备注信息',
  `created` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='投资平台';

-- 正在导出表  invest.investment_platform 的数据：~18 rows (大约)
DELETE FROM `investment_platform`;
/*!40000 ALTER TABLE `investment_platform` DISABLE KEYS */;
INSERT INTO `investment_platform` (`id`, `name`, `supervision_bank`, `is_record`, `registry_place`, `business_place`, `platform_background`, `security_level`, `avg_profit_ratio`, `business_mode`, `flexible`, `guarantee`, `introduction`, `uptime`, `notes`, `created`, `modified`) VALUES
	(1, '宜人贷', '广东发展银行', 0, '北京', '北京', '上市公司', 2, 8.00, '宜人贷创新实现P2P从客户获取、风险控制、交易达成到客户服务全流程线上操作。宜人贷专注于具有信用维护意识的城市白领人群，这一群体具有较好的教育水平、信用意识以及稳定的还款能力。', '宜定盈可提前退出，但要收取1%～2%的紧急退出费，且会扣除相应的优惠券、宜人币（如有使用）；精英标持有90天之后可以转让，收取成交金额的2‰ ', '质保服务专款，存管在广发银行', '中国首家在美国纽交所上市的P2P公司，投宜人贷在安全性上还是很有保障的，但收益很一般', '2012-01-01', '中国首家在美国纽交所上市的P2P公司，投宜人贷在安全性上还是很有保障的，但收益很一般', '2018-01-19 17:53:28', '2018-01-22 09:56:48'),
	(2, '达人贷', 'XIB', 0, '深圳', '企业地址深圳市福田区沙头街道泰然九路海松大厦A座702室', '民营系', 3, 12.00, '达人贷只做小额信贷业务，其中超80%是小微企业主借贷，借款金额在5-7万。剩余20%不到是薪金贷，给有稳定工资收入的人群放贷，平均借款金额也在7万左右。', '微信宝可转让', 'IPC技术。目前平台的不良率在2.6%左右，待收近1亿，风险准备金330万，数据上看是可以覆盖住风险的。', '深圳一家做信贷的小平台，业务规模不大，但总体做的还算比较扎实。团队经验丰富，且比较稳定，平台行业口碑尚可，投资性价比还不错', '2014-12-27', '业务方面平台是完全合规的，所有项目都遵循小额分散。\r\n12个月内（包含12个月）投资安全都有一定保障。\r\n每天10:00和18:00放标。\r\n工商注册号440300602385364\r\n组织机构代码085957193\r\n统一社会信用代码91440300085957193N\r\n成立日期2013-12-27\r\n登记机关深圳市市场监督管理局\r\n营业期限2013-12-27 至 2023-12-25', '2018-01-23 17:03:25', '2018-01-26 11:07:42'),
	(3, '钱香', 'JXB', 0, '上海', '中国（上海）自由贸易试验区浦东南路2250号2幢一层C157室', '上市公司系', 3, 9.76, '中小企业贷', '不可转让', '第三方担保&质保服务转款', '上海一家专注供黄金珠宝供应链的平台，模式与产业融合比较深，配套的担保措施比较完善，风险可控，比较适合中短期投资选择。', '2015-05-07', '公司名称：上海倾信互联网金融信息服务有限公司\r\n电话：021-68881322\r\n统一社会信用代码：91310000332725243Y\r\n工商注册号：310141000146942\r\n组织机构代码：332725243\r\n法定代表人：黄崇望\r\n注册资本：1600万人民币\r\n成立日期：2015-05-07', '2018-01-26 11:23:52', '2018-01-27 17:56:08'),
	(4, '恒大金服', 'GDB', 0, '深圳', '深圳市前海深港合作区临海大道59号海运中心主塔楼21楼2101G', '恒大集团', 2, 6.50, '车贷 | 个人信用贷 | 中小企业贷 | 优选理财', '部分可转让', '抵押物&风险准备金', '一线平台、广东地区前三平台、广发证券和上市公司大金重工、中银粤财入股。安全级别A级，产品丰富，收益一般', '2016-03-16', '公司名称:恒大互联网金融服务（深圳）有限公司\r\n电话:020-38657256\r\n统一社会信用代码:91440300359451385K\r\n工商注册号:440301114542363\r\n组织机构代码:35945138-5\r\n法定代表人:刘永灼\r\n注册资本:100000万人民币\r\n成立日期:2015-12-07\r\n登记机关:深圳市市场监督管理局', '2018-01-26 11:32:52', '2018-01-27 17:55:21'),
	(5, '小赢理财', 'HRB', 0, '深圳', '深圳市南山区粤海街道海德三道航天科技广场A座7层', '上市公司系', 2, 7.50, '车贷 | 个人信用贷 | 中小企业贷 | 优选理财', '部分项目可申请转让', '信用保证保险', '公司名称:深圳市赢众通金融信息服务有限公司\r\n电话:0755-86282977\r\n统一社会信用代码:914403000885341888\r\n工商注册号:440301108926982\r\n组织机构代码:088534188\r\n注册资本:6510.4156万元\r\n成立日期:2014-03-07\r\n登记机关:深圳市市场监督管理局', '2014-03-07', '公司名称:深圳市赢众通金融信息服务有限责任公司\r\n电话:0755-86282977\r\n统一社会信用代码:914403000885341888\r\n工商注册号:440301108926982\r\n组织机构代码:088534188\r\n法定代表人:唐越\r\n注册资本:6510.4156万元\r\n成立日期:2014-03-07\r\n登记机关:深圳市市场监督管理局', '2018-01-26 11:43:06', '2018-01-27 17:54:27'),
	(6, '投哪网', 'GDB', 0, '广东', '广东', '上市公司系', 2, 9.00, '车贷 | 个人信用贷 | 中小企业贷 | 优选理财', '持有30天以上可以转让，但要收取一定费用', '抵押物&风险准备金', '一线平台、广东地区前三平台、广发证券和上市公司大金重工、中银粤财入股。安全级别A级，产品丰富，收益一般', '2012-05-04', '公司名称:深圳投哪金融服务有限公司\r\n电话:0755-23888828\r\n统一社会信用代码:91440300083884082F\r\n工商注册号:440306108353406\r\n组织机构代码:083884082\r\n注册资本:10000万元\r\n成立日期:2013-11-19\r\n登记机关:深圳市市场监督管理局', '2018-01-27 17:37:30', '2018-01-27 17:55:06'),
	(7, '团贷网', 'XIB', 0, '东莞', '东莞市南城街道莞太路111号民间金融大厦1号楼28楼', '创投 | C轮', 3, 12.00, '车贷 | 房贷 | 个人信用贷 | 中小企业贷', '30天后可转让', '担保&风险准备金', '备受风投追捧、发展快、产品丰富、收益不错、提现手续费高。平台发展比较快，也有坏账率提升的隐患。另外提现手续费略高，希望改进', '2012-07-15', '公司名称:东莞团贷网互联网科技服务有限公司\r\n统一社会信用代码:91441900MA4ULXKB38\r\n工商注册号:441900002895462\r\n组织机构代码:MA4ULXKB3\r\n注册资本:10293.3334万元人民币\r\n成立日期:2016-02-04\r\n登记机关:东莞市工商行政管理局', '2018-01-27 17:43:05', '2018-01-27 17:55:47'),
	(8, '积木盒子', 'CMSB', 0, '北京', '北京市朝阳区金桐西路10号1单元5层501', '创投 | C轮', 2, 8.00, '车贷 | 房贷 | 个人信用贷 | 其他', '直投项目持有30天之后可以转让，轻松投系列锁定期之后可以提前退出，都会收取一定的费用', '担保&风险准备金', '风投青睐、轻资产、体验好、一站式理财服务。平台资产主要来自担保公司，目前平台重心已经逐渐转向一站式理财服务，P2P 产品收益偏低', '2013-08-07', '公司名称:北京乐融多源信息技术有限公司\r\n电话:010-65818612\r\n统一社会信用代码:911101055923826701\r\n工商注册号:110105014684445\r\n组织机构代码:592382670\r\n注册资本:20000万元\r\n成立日期:2012-03-05\r\n登记机关:朝阳分局', '2018-01-27 17:45:59', '2018-01-27 17:49:25'),
	(9, '信而富', 'CMSB', 0, '上海', '上海市嘉定区南翔镇银翔路819弄1号7层705室', '创投 | C轮', 2, 9.00, '个人信用贷 | 其他', '直投项目持有30天之后可以转让，轻松投系列锁定期之后可以提前退出，都会收取一定的费用', '担保&风险准备金', '风投青睐、轻资产、体验好、一站式理财服务。平台资产主要来自担保公司，目前平台重心已经逐渐转向一站式理财服务，P2P 产品收益偏低', '2010-11-01', '公司名称:上海信而富企业管理有限公司\r\n电话:021-60325999\r\n统一社会信用代码:913100007776054479\r\n工商注册号:310000400432304\r\n组织机构代码:777605447\r\n注册资本:3790万美元\r\n成立日期:2005-07-15\r\n登记机关:上海市工商行政管理局', '2018-01-27 17:48:33', '2018-01-27 17:50:21'),
	(10, '余额宝', NULL, 0, NULL, NULL, '支付宝', 1, 3.80, NULL, '活期，2小时内到帐', NULL, NULL, NULL, NULL, '2018-01-27 17:51:44', '2018-02-01 09:26:02'),
	(11, '唐小僧', NULL, 0, '上海', '中国（上海）自由贸易试验区杨高南路799号1801室', '民营', 5, 8.00, '优选理财', '活期随时提现', '合作机构承诺回购', '活期、高收益、底层资产不明。小妞去实地考察过唐小僧，发现唐小僧募集的资金主要去向母公司资邦集团提供的项目，但底层资产并不清楚。投资人还需谨慎', '2015-05-05', '公司名称:资邦元达（上海）互联网金融有限公司\r\n电话:021-28900978\r\n统一社会信用代码:913101153208482031\r\n工商注册号:310115002473756\r\n组织机构代码:320848203\r\n注册资本:5000万人民币\r\n成立日期:2014-10-31\r\n登记机关:自贸区市场监督管理局', '2018-01-27 17:59:35', '2018-01-27 17:59:35'),
	(12, '饭饭金服', 'HFB', 0, '北京', '浙江省嘉兴市南湖区南江路1856号1号楼103室-11', '创投 | 盛大资本', 3, 11.00, '车贷 | 房贷', '活期随时退出', '足额抵押&风险准备金', '饭饭金服之前是爱钱帮旗下品牌，2017年5月更换公司主体，开始独立运营。资产还是由爱钱帮提供，专注做车辆质押和一线城市房产抵押业务。合规性和安全性相对较好，另外饭饭目前在活动期间，活期产品“饭盒”无论从收益还是体验都很不错，投资性价比很高', '2016-05-18', '饭饭投资管理（嘉兴）有限公司\r\n客服邮箱:ffkf@fanfanjf.cn\r\n统一社会信用代码:91330402MA28AML14X\r\n工商注册号:330402000243295\r\n组织机构代码:MA28AML14\r\n注册资本1000万人民币\r\n成立日期:2016-09-08\r\n登记机关:嘉兴市南湖区行政审批局', '2018-02-02 17:07:45', '2018-02-08 14:19:53'),
	(13, '人人贷', 'CMSB', 0, '北京', '北京市朝阳区曙光西里甲5号院16号楼5层503单元', '创投 | A轮', 2, 8.00, '车贷 | 房贷 | 个人信用贷 | 中小企业贷 | 其他', 'U计划提前退出收取加入金额2%，薪计划不能提前退出，散标持有90天之后可以债权转让但要收取成交金额0.5%的费用', '平台保证金（保证金名义上是由友众信业存管在民生银行）&合作机构提前垫付', '大品牌、体验优、资金存管、收益偏低。懒人选U计划，工薪族投薪计划，高手玩转让专区。人人贷总有一款产品适合你，不过收益就soso了', '2010-10-03', '公司名称:人人贷商务顾问（北京）有限公司\r\n电话:82152601\r\n统一社会信用代码:911101055548793445\r\n工商注册号:110105012817080\r\n组织机构代码:554879344\r\n注册资本:10000万人民币\r\n成立日期:2010-04-28\r\n登记机关:朝阳分局', '2018-02-03 11:59:44', '2018-02-03 11:59:44'),
	(14, '拍拍贷', 'CMBC', 0, '上海', '上海市 浦东新区 张江高科技园区 郭守敬路356号', '创投 | C轮', 3, 10.00, '其它', '部分项目支持转让', '散标不提供保障（赔标除外）', '中国第一家P2P平台，也是网贷行业内坚持个人对个人点对点借款的平台。平台逾期比较严重，除开彩虹计划和带有"赔"标示的项目外，都不承诺任何保障。投资人还需谨慎投资', '2007-06-18', '公司名称:上海拍拍贷金融信息服务有限公司\r\n电话:021-31186888\r\n统一社会信用代码:91310115568071645J\r\n工商注册号:310115001783417\r\n组织机构代码:568071645\r\n注册资本:10000万人民币\r\n成立日期:2011-01-18\r\n登记机关:自贸区市场监督管理局\r\n法定代表人:顾少丰\r\n客服电话:400-184-8888', '2018-02-03 12:04:13', '2018-02-03 12:04:13'),
	(15, '凤凰金融', 'BOB', 0, '北京', '北京市朝阳区酒仙桥路10号星科大厦A座6层6002', '上市公司系', 2, 7.00, '个人信用贷 | 中小企业贷 | 债权流转 | 优选理财', '30天后可转让', '合作方代偿&担保公司担保', '原凤凰金融平台已将网贷业务独立出来，叫做“凤凰智信”，凤凰金融平台则发展成为集基金、保险、网贷等为一体的综合理财平台。背靠凤凰卫视和凤凰金融这个金字招牌，凤凰智信仍然适合中产阶级大资金长期投资', '2014-08-22', '公司名称:北京凤凰理理它信息技术有限公司\r\n电话:186****6336\r\n统一社会信用代码:91110105306763860W\r\n工商注册号:110105017780223\r\n组织机构代码:306763860\r\n注册资本:1065.245588万人民币\r\n成立日期:2014-08-22\r\n登记机关:北京市工商行政管理局朝阳分局\r\n法定代表人:张震', '2018-02-03 12:07:27', '2018-02-03 12:07:27'),
	(16, '图腾贷', 'JXB', 0, '四川成都', '深圳市前海深港合作区临海大道59号海运中心口岸楼408E房', '创投 | A轮', 3, 13.00, '车贷', '持有30天以上可申请转让', '足值抵质押&第三方资产处理公司回购债权', '成都当地做车贷较出名的平台，A轮获得天鸽互动的2000万元投资，目前是西南地区最大的车贷平台。过去以质押车辆为主，且很多二押车，但现在已转型为以抵押为主，二押的比例也大大降低。车贷是地域性很强的一类资产，图腾贷在四川、重庆等地有较强的竞争力和风险把控能力。平台目前的收益属于行业中等偏上，可以适当投资', '2014-09-10', '公司名称:深圳前海图腾互联网金融服务有限公司\r\n电话:0755-27924139\r\n统一社会信用代码:91440300319478479E\r\n工商注册号:440301111535459\r\n组织机构代码:319478479\r\n注册资本:1278.1957万人民币\r\n成立日期:2014-10-28\r\n登记机关:深圳市市场监督管理局\r\n法定代表人:罗润超', '2018-02-03 12:10:55', '2018-02-03 12:10:55'),
	(17, '企额贷', 'HFB', 0, '上海', '上海浦东新区陆家嘴环路166号未来资产大厦31层', '民营系', 3, 12.00, '房贷 | 车贷 | 中小企业贷', '180天及以上的项目可以转让，等额本金车贷项目不支持债权转让', '强抵押物', '上海一家专注做抵押贷款的平台，主攻房抵贷和车抵贷。平台虽然不出名，但业务能力是过硬，外加有强抵押物做担保，相比其他平台资产更为优质。', '2014-10-27', '公司名称:上海相诚金融信息服务有限公司\r\n客服电话:4008-826-823\r\n微信号：13816674106\r\n电话:021-68670095\r\n统一社会信用代码:913101153208129212\r\n工商注册号:310115002468198\r\n组织机构代码:320812921\r\n法定代表人:颜永忠\r\n注册资本:5000万人民币\r\n成立日期2014-10-27\r\n登记机关:自贸区市场监督管理局', '2018-02-06 14:48:25', '2018-02-06 14:48:25'),
	(18, '四川信托', NULL, 0, '成都', '成都', NULL, 1, 6.00, NULL, NULL, NULL, NULL, NULL, NULL, '2018-02-07 17:29:22', '2018-02-07 17:29:30');
/*!40000 ALTER TABLE `investment_platform` ENABLE KEYS */;

-- 导出  表 invest.login_log 结构
CREATE TABLE IF NOT EXISTS `login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `ip` varchar(20) DEFAULT NULL COMMENT '编号',
  `host` varchar(40) DEFAULT NULL COMMENT '登录主机',
  `login_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `type` tinyint(4) DEFAULT NULL COMMENT '操作类型:登录/登出',
  `system_code` varchar(50) DEFAULT NULL COMMENT '系统编码',
  `system_name` varchar(50) DEFAULT NULL COMMENT '系统名称',
  `firm_code` varchar(50) DEFAULT NULL COMMENT '市场编码',
  `firm_name` varchar(50) DEFAULT NULL COMMENT '市场名称',
  `success` int(11) DEFAULT NULL COMMENT '是否成功',
  `msg` varchar(511) DEFAULT NULL COMMENT '失败原因',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='登录日志';

-- 正在导出表  invest.login_log 的数据：~0 rows (大约)
DELETE FROM `login_log`;
/*!40000 ALTER TABLE `login_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `login_log` ENABLE KEYS */;

-- 导出  表 invest.menu 结构
CREATE TABLE IF NOT EXISTS `menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `system_id` bigint(20) DEFAULT NULL COMMENT '所属系统',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级id',
  `order_number` int(11) DEFAULT NULL COMMENT '排序号',
  `url` varchar(127) DEFAULT NULL COMMENT '菜单url',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `description` varchar(40) DEFAULT NULL COMMENT '描述',
  `target` int(4) DEFAULT NULL COMMENT '打开链接的位置##{data:[{value:0, text:"当前窗口"},{value:1, text:"新开窗口"}]}',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `type` int(4) NOT NULL DEFAULT '0' COMMENT '类型##{data:[{value:0, text:"目录"},{value:1, text:"链接"}]}',
  `icon_cls` varchar(40) DEFAULT NULL COMMENT '菜单图标',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8 COMMENT='菜单';

-- 正在导出表  invest.menu 的数据：~28 rows (大约)
DELETE FROM `menu`;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` (`id`, `system_id`, `parent_id`, `order_number`, `url`, `name`, `description`, `target`, `created`, `modified`, `type`, `icon_cls`) VALUES
	(1, 2, NULL, 20, NULL, '权限管理', NULL, 0, '2018-05-22 17:04:42', '2018-06-11 10:19:52', 0, NULL),
	(2, 2, 1, 30, 'http://uap.artist.com/menu/index.html', '菜单管理', '菜单管理', 0, '2018-05-21 10:48:15', '2018-06-22 14:39:33', 1, NULL),
	(3, 2, NULL, 10, 'http://uap.artist.com/index/adminIndex.html', '首页', '首页', 0, '2018-05-22 14:58:16', '2018-06-22 14:39:35', 1, NULL),
	(4, 2, 79, 70, 'http://uap.artist.com/dataDictionary/index.html', '数据字典', '数据字典', 0, '2018-05-24 17:12:13', '2018-06-22 14:39:40', 1, NULL),
	(5, 2, 79, 80, 'http://uap.artist.com/systemConfig/index.html', '系统配置', '系统配置', 0, '2018-05-24 17:12:56', '2018-06-22 14:39:46', 1, NULL),
	(6, 2, 79, 60, 'http://uap.artist.com/department/index.html', '部门管理', '部门', 0, '2018-05-25 15:19:31', '2018-06-22 14:39:50', 1, NULL),
	(7, 2, 1, 20, 'http://uap.artist.com/role/index.html', '角色管理', '角色管理', 0, '2018-05-25 16:09:39', '2018-06-22 14:39:53', 1, NULL),
	(8, 2, 4, 10, 'http://uap.artist.com/dataDictionaryValue/list.html', '数据字典值', '数据字典值内链', 0, '2018-05-25 16:51:28', '2018-06-22 14:39:56', 2, NULL),
	(9, 2, 1, 10, 'http://uap.artist.com/user/index.html', '用户管理', '用户管理', 0, '2018-05-28 17:20:27', '2018-06-22 14:39:59', 1, NULL),
	(10, 2, NULL, 50, '', '日志管理', '日志管理', 0, '2018-05-29 14:19:49', '2018-06-11 10:19:52', 0, NULL),
	(11, 2, 10, 10, 'http://uap.artist.com/loginLog/index.html', '登录日志', '登录日志', 0, '2018-05-29 14:20:30', '2018-06-22 14:40:02', 1, NULL),
	(12, 2, 10, 12, 'http://uap.artist.com/systemExceptionLog/index.html', '异常日志', '异常日志', 0, '2018-05-29 14:21:15', '2018-06-22 14:40:05', 1, NULL),
	(13, 2, 1, 90, 'http://uap.artist.com/system/index.html', '系统管理', '系统管理', NULL, '2018-05-29 14:27:04', '2018-06-22 14:40:08', 1, NULL),
	(79, 2, NULL, 30, NULL, '配置管理', '配置管理', NULL, '2018-06-11 14:15:37', '2018-06-11 14:15:37', 0, NULL),
	(80, 2, 1, 91, 'http://uap.artist.com/user/onlineList.html', '在线用户', '在线用户列表', NULL, '2018-06-11 15:22:43', '2018-06-22 14:40:13', 1, NULL),
	(82, 3, 87, 20, 'http://invest.artist.com:8086/bankCard/index.html', '银行卡', NULL, NULL, '2018-06-22 15:16:13', '2018-06-22 15:49:35', 1, NULL),
	(83, 3, 87, 30, 'http://invest.artist.com:8086/investmentPlatform/index.html', '投资平台', NULL, NULL, '2018-06-22 15:16:29', '2018-06-22 15:49:39', 1, NULL),
	(84, 3, 87, 10, 'http://invest.artist.com:8086/investment/index.html', '投资记录', NULL, NULL, '2018-06-22 15:16:47', '2018-06-22 15:49:41', 1, NULL),
	(85, 3, 82, NULL, 'http://invest.artist.com:8086/selectDialog/user.html', '选择用户', '选择用户', NULL, '2018-06-22 15:17:20', '2018-06-22 15:49:44', 2, NULL),
	(86, 3, 84, NULL, 'http://invest.artist.com:8086/selectDialog/bankCard.html', '选择银行卡', '选择银行卡', NULL, '2018-06-22 15:17:54', '2018-06-22 15:49:49', 2, NULL),
	(87, 3, NULL, 10, NULL, '投资理财', NULL, NULL, '2018-06-22 15:18:59', '2018-06-22 15:18:59', 0, NULL),
	(88, 3, NULL, 20, NULL, '报表', NULL, NULL, '2018-06-22 15:19:27', '2018-06-22 15:19:27', 0, NULL),
	(89, 3, 88, 20, 'http://invest.artist.com:8086/investment/profitDistributionPieChart.html', '收益分布', NULL, NULL, '2018-06-22 15:19:54', '2018-06-22 15:49:55', 1, NULL),
	(90, 3, 88, 10, 'http://invest.artist.com:8086/investment/investmentStats.html', '投资统计', NULL, NULL, '2018-06-22 15:20:09', '2018-06-22 15:50:00', 1, NULL),
	(91, 3, 87, 15, 'http://invest.artist.com:8086/paymentRecord/index.html', '收支明细', NULL, NULL, '2018-06-22 15:20:53', '2018-06-22 15:50:04', 1, NULL),
	(92, 3, 87, 40, 'http://invest.artist.com:8086/investment/compute.html', '投资计算器', NULL, NULL, '2018-06-22 15:21:09', '2018-06-22 15:50:08', 1, NULL),
	(93, 3, 88, 30, 'http://invest.artist.com:8086/investment/investmentDistributionPieChart.html', '投资分布', NULL, NULL, '2018-06-22 15:21:38', '2018-06-22 15:50:12', 1, NULL),
	(94, 3, NULL, 1, 'http://invest.artist.com:8086/index.html', '首页', NULL, NULL, '2018-06-22 15:58:27', '2018-06-22 15:58:27', 1, NULL);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;

-- 导出  表 invest.payment_record 结构
CREATE TABLE IF NOT EXISTS `payment_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_name` varchar(20) DEFAULT NULL COMMENT '用户',
  `platform_name` varchar(20) DEFAULT NULL COMMENT '资金去向##投资平台或银行卡号',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `type` int(11) DEFAULT NULL COMMENT '类型##{provider:"paymentRecordTypePro                               vider", data:[{value:1,text:"收入"},{value:2, text:"支出"},{value:3, text:"应收"},{value:4, text:"应付"}]}',
  `adjust_amount` bigint(20) DEFAULT NULL COMMENT '调整金额',
  `balance` bigint(20) DEFAULT NULL COMMENT '余额',
  `notes` varchar(250) DEFAULT NULL COMMENT '备注信息',
  `created_name` varchar(20) DEFAULT NULL COMMENT '创建人',
  `created` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=177 DEFAULT CHARSET=utf8 COMMENT='收支记录';

-- 导出  表 invest.resource 结构
CREATE TABLE IF NOT EXISTS `resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `description` varchar(120) DEFAULT NULL COMMENT '描述',
  `menu_id` bigint(20) NOT NULL COMMENT '外键，关联menu表',
  `code` varchar(50) DEFAULT NULL COMMENT '编码##原resource URL',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 COMMENT='资源';

-- 正在导出表  invest.resource 的数据：~41 rows (大约)
DELETE FROM `resource`;
/*!40000 ALTER TABLE `resource` DISABLE KEYS */;
INSERT INTO `resource` (`id`, `name`, `description`, `menu_id`, `code`, `created`, `modified`) VALUES
	(1, '新增', '新增菜单、资源或内链', 2, 'addMenu', '2018-05-22 17:04:42', '2018-05-22 17:04:42'),
	(27, '新增角色', '新增角色信息', 7, 'insertRole', '2018-06-04 15:53:17', '2018-06-04 15:54:08'),
	(28, '修改角色', '修改角色信息', 7, 'updateRole', '2018-06-04 15:54:32', '2018-06-04 15:54:32'),
	(29, '删除角色', '删除角色信息', 7, 'deleteRole', '2018-06-04 15:55:10', '2018-06-04 15:55:10'),
	(30, '导出', '导出角色信息', 7, 'exportRole', '2018-06-04 15:55:49', '2018-06-04 15:58:03'),
	(31, '所属用户', '查看角色用户列表信息', 7, 'roleUsers', '2018-06-04 15:57:15', '2018-06-04 15:57:57'),
	(32, '解绑角色用户', '解绑角色用户', 7, 'unbindRoleUser', '2018-06-04 15:57:35', '2018-06-04 15:57:35'),
	(33, '角色权限', '设置角色的权限', 7, 'rolePermission', '2018-06-04 15:59:45', '2018-06-04 15:59:45'),
	(34, '新增用户', '新增用户', 9, 'insertUser', '2018-06-04 16:10:50', '2018-06-04 16:10:50'),
	(35, '修改用户', '修改用户信息', 9, 'updateUser', '2018-06-04 16:11:12', '2018-06-04 16:11:12'),
	(36, '删除用户', '删除用户信息', 9, 'deleteUser', '2018-06-04 16:11:32', '2018-06-04 16:11:32'),
	(37, '查看详情', '查看用户详细信息', 9, 'viewUser', '2018-06-04 16:12:07', '2018-06-04 16:12:07'),
	(38, '重置密码', '重置用户密码', 9, 'resetPass', '2018-06-04 16:12:34', '2018-06-04 16:12:34'),
	(39, '分配角色', '给用户分配角色', 9, 'editUserRole', '2018-06-04 16:13:21', '2018-06-04 16:13:21'),
	(40, '数据权限', '给用户分配数据权限', 9, 'editUserData', '2018-06-04 16:16:34', '2018-06-04 16:16:34'),
	(41, '导出', '导出用户数据', 9, 'exportUser', '2018-06-04 16:16:51', '2018-06-04 16:16:51'),
	(42, '启用', '启用用户', 9, 'enabledUser', '2018-06-04 16:18:11', '2018-06-04 16:18:11'),
	(43, '禁用', '禁用用户', 9, 'disabledUser', '2018-06-04 16:18:36', '2018-06-04 16:18:36'),
	(44, '导出', '登录日志数据导出', 11, 'exportLoginLog', '2018-06-05 10:23:05', '2018-06-05 10:23:05'),
	(45, '导出', '异常日志导出', 12, 'exportSystemExceptionLog', '2018-06-05 10:23:45', '2018-06-05 10:23:45'),
	(46, '新增', '新增系统配置', 5, 'insertSystemConfig', '2018-06-05 10:24:29', '2018-06-05 10:24:29'),
	(47, '修改', '修改系统配置', 5, 'updateSystemConfig', '2018-06-05 10:24:42', '2018-06-05 10:24:42'),
	(48, '删除', '删除系统配置', 5, 'deleteSystemConfig', '2018-06-05 10:24:56', '2018-06-05 10:24:56'),
	(49, '导出', '导出系统配置', 5, 'exportSystemConfig', '2018-06-05 10:25:17', '2018-06-05 10:25:17'),
	(50, '新增', '新增系统信息', 13, 'insertSystem', '2018-06-05 10:25:54', '2018-06-05 10:25:54'),
	(51, '修改', '修改系统信息', 13, 'updateSystem', '2018-06-05 10:26:06', '2018-06-05 10:26:06'),
	(52, '删除', '删除系统信息', 13, 'deleteSystem', '2018-06-05 10:26:17', '2018-06-05 10:26:17'),
	(53, '导出', '导出系统信息', 13, 'exportSystem', '2018-06-05 10:26:30', '2018-06-05 10:26:30'),
	(54, '新增', '新增部门信息', 6, 'insertDepartment', '2018-06-05 10:26:59', '2018-06-05 10:26:59'),
	(55, '修改', '修改部门信息', 6, 'updateDepartment', '2018-06-05 10:27:17', '2018-06-05 10:27:17'),
	(56, '删除', '删除部门信息', 6, 'deleteDepartment', '2018-06-05 10:27:37', '2018-06-05 10:27:37'),
	(57, '新增', '新增数据字典信息', 4, 'insertDataDictionary', '2018-06-05 10:28:20', '2018-06-05 10:28:20'),
	(58, '修改', '修改数据字典信息', 4, 'updateDataDictionary', '2018-06-05 10:28:30', '2018-06-05 10:28:30'),
	(59, '删除', '删除数据字典信息', 4, 'deleteDataDictionary', '2018-06-05 10:28:48', '2018-06-05 10:28:48'),
	(60, '新增', '新增数据字典值', 8, 'insertDataDictionaryValue', '2018-06-05 10:30:30', '2018-06-05 10:30:30'),
	(61, '修改', '修改数据字典值', 8, 'updateDataDictionaryValue', '2018-06-05 10:30:39', '2018-06-05 10:30:47'),
	(62, '删除', '删除数据字典值', 8, 'deleteDataDictionaryValue', '2018-06-05 10:31:00', '2018-06-05 10:31:00'),
	(63, '修改', '修改菜单、资源或内链', 2, 'updateMenu', '2018-06-05 11:27:16', '2018-06-05 11:27:16'),
	(64, '删除', '删除菜单、资源或内链', 2, 'deleteMenu', '2018-06-05 11:27:35', '2018-06-05 11:27:35'),
	(69, '解锁', '手动解锁用户', 9, 'unlockUser', '2018-06-07 18:44:03', '2018-06-07 18:44:03'),
	(70, '强制下线', '强制在线用户下线', 80, 'forcedOffline', '2018-06-11 15:23:55', '2018-06-11 15:23:55');
/*!40000 ALTER TABLE `resource` ENABLE KEYS */;

-- 导出  表 invest.role 结构
CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(255) NOT NULL COMMENT '角色名',
  `description` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `firm_code` varchar(50) DEFAULT NULL COMMENT '所属市场编码',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='角色';

-- 正在导出表  invest.role 的数据：~1 rows (大约)
DELETE FROM `role`;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`id`, `role_name`, `description`, `firm_code`, `created`, `modified`) VALUES
	(1, '系统管理员', '系统管理员', 'group', '2018-05-21 09:45:54', '2018-06-22 14:50:34');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;

-- 导出  表 invest.role_data_auth 结构
CREATE TABLE IF NOT EXISTS `role_data_auth` (
  `id` bigint(20) NOT NULL,
  `data_auth_id` bigint(20) NOT NULL COMMENT '数据权限表id',
  `role_id` bigint(20) NOT NULL COMMENT 'role_id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色数据权限关系';

-- 正在导出表  invest.role_data_auth 的数据：~0 rows (大约)
DELETE FROM `role_data_auth`;
/*!40000 ALTER TABLE `role_data_auth` DISABLE KEYS */;
/*!40000 ALTER TABLE `role_data_auth` ENABLE KEYS */;

-- 导出  表 invest.role_menu 结构
CREATE TABLE IF NOT EXISTS `role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID##外键',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=295 DEFAULT CHARSET=utf8 COMMENT='角色菜单关系';

-- 正在导出表  invest.role_menu 的数据：~28 rows (大约)
DELETE FROM `role_menu`;
/*!40000 ALTER TABLE `role_menu` DISABLE KEYS */;
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES
	(267, 1, 1),
	(268, 1, 2),
	(269, 1, 7),
	(270, 1, 9),
	(271, 1, 13),
	(272, 1, 80),
	(273, 1, 3),
	(274, 1, 10),
	(275, 1, 11),
	(276, 1, 12),
	(277, 1, 79),
	(278, 1, 4),
	(279, 1, 8),
	(280, 1, 5),
	(281, 1, 6),
	(282, 1, 87),
	(283, 1, 82),
	(284, 1, 85),
	(285, 1, 83),
	(286, 1, 84),
	(287, 1, 86),
	(288, 1, 91),
	(289, 1, 92),
	(290, 1, 88),
	(291, 1, 89),
	(292, 1, 90),
	(293, 1, 93),
	(294, 1, 94);
/*!40000 ALTER TABLE `role_menu` ENABLE KEYS */;

-- 导出  表 invest.role_resource 结构
CREATE TABLE IF NOT EXISTS `role_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID##外键',
  `resource_id` bigint(20) NOT NULL COMMENT '资源ID##外键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=507 DEFAULT CHARSET=utf8 COMMENT='角色资源关系';

-- 正在导出表  invest.role_resource 的数据：~123 rows (大约)
DELETE FROM `role_resource`;
/*!40000 ALTER TABLE `role_resource` DISABLE KEYS */;
INSERT INTO `role_resource` (`id`, `role_id`, `resource_id`) VALUES
	(343, 19, 1),
	(344, 19, 63),
	(345, 19, 64),
	(346, 19, 27),
	(347, 19, 28),
	(348, 19, 29),
	(349, 19, 30),
	(350, 19, 31),
	(351, 19, 32),
	(352, 19, 33),
	(353, 19, 34),
	(354, 19, 35),
	(355, 19, 36),
	(356, 19, 37),
	(357, 19, 38),
	(358, 19, 39),
	(359, 19, 40),
	(360, 19, 41),
	(361, 19, 42),
	(362, 19, 43),
	(363, 19, 69),
	(364, 19, 50),
	(365, 19, 51),
	(366, 19, 52),
	(367, 19, 53),
	(368, 19, 70),
	(369, 19, 44),
	(370, 19, 45),
	(371, 19, 60),
	(372, 19, 61),
	(373, 19, 62),
	(374, 19, 57),
	(375, 19, 58),
	(376, 19, 59),
	(377, 19, 46),
	(378, 19, 47),
	(379, 19, 48),
	(380, 19, 49),
	(381, 19, 54),
	(382, 19, 55),
	(383, 19, 56),
	(384, 20, 1),
	(385, 20, 63),
	(386, 20, 64),
	(387, 20, 27),
	(388, 20, 28),
	(389, 20, 29),
	(390, 20, 30),
	(391, 20, 31),
	(392, 20, 32),
	(393, 20, 33),
	(394, 20, 34),
	(395, 20, 35),
	(396, 20, 36),
	(397, 20, 37),
	(398, 20, 38),
	(399, 20, 39),
	(400, 20, 40),
	(401, 20, 41),
	(402, 20, 42),
	(403, 20, 43),
	(404, 20, 69),
	(405, 20, 50),
	(406, 20, 51),
	(407, 20, 52),
	(408, 20, 53),
	(409, 20, 70),
	(410, 20, 44),
	(411, 20, 45),
	(412, 20, 60),
	(413, 20, 61),
	(414, 20, 62),
	(415, 20, 57),
	(416, 20, 58),
	(417, 20, 59),
	(418, 20, 46),
	(419, 20, 47),
	(420, 20, 48),
	(421, 20, 49),
	(422, 20, 54),
	(423, 20, 55),
	(424, 20, 56),
	(466, 1, 1),
	(467, 1, 63),
	(468, 1, 64),
	(469, 1, 27),
	(470, 1, 28),
	(471, 1, 29),
	(472, 1, 30),
	(473, 1, 31),
	(474, 1, 32),
	(475, 1, 33),
	(476, 1, 34),
	(477, 1, 35),
	(478, 1, 36),
	(479, 1, 37),
	(480, 1, 38),
	(481, 1, 39),
	(482, 1, 40),
	(483, 1, 41),
	(484, 1, 42),
	(485, 1, 43),
	(486, 1, 69),
	(487, 1, 50),
	(488, 1, 51),
	(489, 1, 52),
	(490, 1, 53),
	(491, 1, 70),
	(492, 1, 44),
	(493, 1, 45),
	(494, 1, 60),
	(495, 1, 61),
	(496, 1, 62),
	(497, 1, 57),
	(498, 1, 58),
	(499, 1, 59),
	(500, 1, 46),
	(501, 1, 47),
	(502, 1, 48),
	(503, 1, 49),
	(504, 1, 54),
	(505, 1, 55),
	(506, 1, 56);
/*!40000 ALTER TABLE `role_resource` ENABLE KEYS */;

-- 导出  表 invest.schedule_job 结构
CREATE TABLE IF NOT EXISTS `schedule_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `job_name` varchar(40) DEFAULT NULL COMMENT '任务名',
  `job_group` varchar(40) DEFAULT NULL COMMENT '任务分组',
  `job_status` int(11) DEFAULT NULL COMMENT '是否启动任务##{provider:"jobStatusProvider", data:[{value:0, text:"无"},{value:1, text:"正常"},{value:2, text:"暂停"},{value:3, text:"完成"},{value:4, text:"错误"},{value:5, text:"阻塞"}]}',
  `job_data` varchar(1000) DEFAULT NULL COMMENT 'json',
  `cron_expression` varchar(40) DEFAULT NULL COMMENT 'cron表达式',
  `repeat_interval` int(11) DEFAULT NULL COMMENT '重复间隔(s)##简单调度，默认以秒为单位',
  `start_delay` int(11) DEFAULT NULL COMMENT '启动间隔(s)##启动调度器后，多少秒开始执行调度',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `bean_class` varchar(100) DEFAULT NULL COMMENT '任务调用类##任务执行时调用类的全名，用于反射',
  `spring_id` varchar(40) DEFAULT NULL COMMENT 'SpringBeanId##spring的beanId，直接从spring中获取',
  `url` varchar(100) DEFAULT NULL COMMENT 'url##支持远程调用restful url',
  `is_concurrent` int(11) DEFAULT NULL COMMENT '任务是否有状态##1：并发; 0:同步##{provider:"isConcurrentProvider", data:[{value:0, text:"同步"},{value:1, text:"并发"}]}',
  `method_name` varchar(40) DEFAULT NULL COMMENT '任务调用的方法名##bean_class和spring_id需要配置方法名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='任务调度';

-- 正在导出表  invest.schedule_job 的数据：~3 rows (大约)
DELETE FROM `schedule_job`;
/*!40000 ALTER TABLE `schedule_job` DISABLE KEYS */;
INSERT INTO `schedule_job` (`id`, `created`, `modified`, `job_name`, `job_group`, `job_status`, `job_data`, `cron_expression`, `repeat_interval`, `start_delay`, `description`, `bean_class`, `spring_id`, `url`, `is_concurrent`, `method_name`) VALUES
	(20, '2017-11-01 15:01:31', '2018-02-11 11:06:43', 'quartzRecycleJob', 'system', 1, NULL, NULL, 1800, 3, '调度器回收(勿动)', NULL, 'quartzRecycleJob', NULL, 0, 'scan'),
	(26, '2018-02-01 12:57:16', '2018-02-11 10:19:13', 'ScanExpiredInvestmentJob', 'investment', 1, NULL, '0 0 0 * * ?', 30, 5, '投资理财过期记录扫描，每天凌晨12点运行一次', NULL, 'scanExpiredInvestmentJob', NULL, 0, 'scan'),
	(27, '2018-06-20 17:17:57', '2018-06-20 17:18:24', 'ScanPayableInvestmentJob', 'investment', 1, NULL, '0 0 0 * * ?', NULL, NULL, '扫描等额本息和按月付息到帐的投资', NULL, 'scanPayableInvestmentJob', NULL, 0, 'scan');
/*!40000 ALTER TABLE `schedule_job` ENABLE KEYS */;

-- 导出  表 invest.system 结构
CREATE TABLE IF NOT EXISTS `system` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '系统标识',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级系统标识',
  `name` varchar(50) DEFAULT NULL COMMENT '系统名称',
  `code` varchar(50) DEFAULT NULL COMMENT '系统编码',
  `order_number` int(11) DEFAULT NULL COMMENT '排序号',
  `description` varchar(255) DEFAULT NULL COMMENT '系统描述',
  `url` varchar(255) DEFAULT NULL COMMENT '系统链接url，为空则跳到内部系统权限菜单，外部系统可定制',
  `index_url` varchar(255) DEFAULT NULL COMMENT '系统首页URL地址',
  `icon_url` varchar(255) DEFAULT NULL COMMENT '图标URL',
  `firm_code` varchar(50) DEFAULT NULL COMMENT '所属市场',
  `logo_url` varchar(255) DEFAULT NULL COMMENT 'LOGO URL',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `type` int(11) NOT NULL COMMENT '类型##1:内部系统,2:外部系统',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='系统';

-- 正在导出表  invest.system 的数据：~3 rows (大约)
DELETE FROM `system`;
/*!40000 ALTER TABLE `system` DISABLE KEYS */;
INSERT INTO `system` (`id`, `parent_id`, `name`, `code`, `order_number`, `description`, `url`, `index_url`, `icon_url`, `firm_code`, `logo_url`, `created`, `modified`, `type`) VALUES
	(1, NULL, '统一登录平台', 'UAP', 1, '统一登录平台', 'http://uap.artist.com/index/index.html', 'http://uap.artist.com/index/platform.html', NULL, NULL, NULL, '2018-06-06 09:51:22', '2018-06-22 14:37:12', 1),
	(2, NULL, '权限管理系统', 'ADMIN', 2, '园区管理系统', 'http://uap.artist.com/index/index.html?systemCode=ADMIN', 'http://uap.artist.com/index/adminIndex.html', 'http://uap.artist.com/resources/images/platform/sys-admin.png', '', NULL, '2018-05-21 17:30:57', '2018-06-25 17:02:01', 1),
	(3, NULL, '投资管理系统', 'INVEST', 3, '投资管理系统', 'http://uap.artist.com/index/index.html?systemCode=INVEST', 'http://invest.artist.com:8086/index.html', 'http://uap.artist.com/resources/images/platform/sys-crm.png', '', 'http://invest.artist.com:8086/resources/images/logo.png', '2018-05-24 10:50:23', '2018-06-25 11:13:35', 1);
/*!40000 ALTER TABLE `system` ENABLE KEYS */;

-- 导出  表 invest.system_config 结构
CREATE TABLE IF NOT EXISTS `system_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `code` varchar(100) DEFAULT NULL COMMENT '编码',
  `value` varchar(100) DEFAULT NULL COMMENT '值',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `system_code` varchar(50) DEFAULT NULL COMMENT '所属系统编码',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='配置系统变量';

-- 正在导出表  invest.system_config 的数据：~3 rows (大约)
DELETE FROM `system_config`;
/*!40000 ALTER TABLE `system_config` DISABLE KEYS */;
INSERT INTO `system_config` (`id`, `name`, `code`, `value`, `description`, `system_code`, `created`, `modified`) VALUES
	(3, '密码错误锁定次数', 'loginFailedTimes', '3', '密码错误多次后锁定用户', 'UAP', '2018-05-31 17:02:06', '2018-06-06 09:44:13'),
	(5, '锁定用户恢复时长', 'resumeDuration', '43200', '锁定用户恢复时长(秒)，默认12小时', 'UAP', '2018-05-24 17:29:48', '2018-05-25 10:53:54'),
	(19, '登录超时时长', 'loginTimeout', '30', '登录超时时长(单位分钟)，默认30分钟，修改并重启后生效', 'UAP', '2018-06-11 14:49:53', '2018-06-11 14:49:53');
/*!40000 ALTER TABLE `system_config` ENABLE KEYS */;

-- 导出  表 invest.system_exception_log 结构
CREATE TABLE IF NOT EXISTS `system_exception_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(255) DEFAULT NULL COMMENT '备注',
  `system_code` varchar(50) DEFAULT NULL COMMENT '系统编码',
  `system_name` varchar(50) DEFAULT NULL COMMENT '系统名称',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '功能模块##菜单id',
  `type` varchar(10) DEFAULT NULL COMMENT '异常类型##业务异常，系统异常或权限异常',
  `firm_code` varchar(50) DEFAULT NULL COMMENT '市场编码',
  `firm_name` varchar(50) DEFAULT NULL COMMENT '市场名称',
  `version` int(11) DEFAULT NULL COMMENT '版本号',
  `exception_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '异常时间',
  `msg` varchar(511) DEFAULT NULL COMMENT '异常信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统异常日志';

-- 正在导出表  invest.system_exception_log 的数据：~0 rows (大约)
DELETE FROM `system_exception_log`;
/*!40000 ALTER TABLE `system_exception_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_exception_log` ENABLE KEYS */;

-- 导出  表 invest.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `firm_code` varchar(50) DEFAULT NULL COMMENT '归属市场编码',
  `department_id` bigint(20) DEFAULT NULL COMMENT '归属部门',
  `position` varchar(20) DEFAULT NULL COMMENT '职位',
  `balance` bigint(20) NOT NULL DEFAULT '0' COMMENT '余额',
  `card_number` varchar(30) DEFAULT NULL COMMENT '卡号',
  `description` varchar(255) DEFAULT NULL COMMENT '备注',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `locked` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '锁定时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `state` int(4) NOT NULL DEFAULT '1' COMMENT '状态##状态:已禁用、正常、已锁定、未激活',
  `real_name` varchar(20) NOT NULL COMMENT '真实姓名',
  `serial_number` varchar(128) DEFAULT '000' COMMENT '用户编号',
  `cellphone` varchar(24) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='用户';

-- 正在导出表  invest.user 的数据：~3 rows (大约)
DELETE FROM `user`;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `user_name`, `password`, `firm_code`, `department_id`, `position`, `balance`, `card_number`, `description`, `created`, `locked`, `modified`, `state`, `real_name`, `serial_number`, `cellphone`, `email`) VALUES
	(1, 'admin', '178591FD5BB0C24851', 'group', 9, '超级管理员', 14006441, '123456789000', '超级管理员', '2018-05-21 09:44:29', '2018-06-22 10:42:59', '2018-06-25 16:45:50', 1, '超级管理员', '', '18111111111', 'admin@qq.com');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

-- 导出  表 invest.user_data_auth 结构
CREATE TABLE IF NOT EXISTS `user_data_auth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_auth_id` bigint(20) NOT NULL COMMENT '数据权限id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='用户数据权限关系';

-- 正在导出表  invest.user_data_auth 的数据：~1 rows (大约)
DELETE FROM `user_data_auth`;
/*!40000 ALTER TABLE `user_data_auth` DISABLE KEYS */;
INSERT INTO `user_data_auth` (`id`, `data_auth_id`, `user_id`) VALUES
	(1, 1, 1);
/*!40000 ALTER TABLE `user_data_auth` ENABLE KEYS */;

-- 导出  表 invest.user_department 结构
CREATE TABLE IF NOT EXISTS `user_department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `department_id` bigint(20) NOT NULL COMMENT '部门id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8 COMMENT='用户部门关系';

-- 正在导出表  invest.user_department 的数据：~0 rows (大约)
DELETE FROM `user_department`;
/*!40000 ALTER TABLE `user_department` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_department` ENABLE KEYS */;

-- 导出  表 invest.user_role 结构
CREATE TABLE IF NOT EXISTS `user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id##外键',
  `role_id` bigint(20) NOT NULL COMMENT '角色id##外键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='用户角色关系';

-- 正在导出表  invest.user_role 的数据：~0 rows (大约)
DELETE FROM `user_role`;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` (`id`, `user_id`, `role_id`) VALUES
	(18, 1, 1);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
