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


-- 导出 investment 的数据库结构
CREATE DATABASE IF NOT EXISTS `investment` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `investment`;

-- 导出  表 investment.bank_card 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='银行卡信息';

-- 正在导出表  investment.bank_card 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `bank_card` DISABLE KEYS */;
INSERT INTO `bank_card` (`id`, `user_id`, `card_type`, `account_name`, `id_number`, `subbranch`, `card_number`, `bank`, `is_default`, `is_depository`, `add_time`) VALUES
	(1, 1, 1, '王宓', '510105198206122016', '成都市金沙支行', '6225881280735613', 'CMBC', 1, 0, '2018-01-19 15:59:12'),
	(2, 1, 1, '王宓', '510105198206122016', '金沙支行', '6226090282081336', 'CMBC', 0, 0, '2018-01-19 17:02:02');
/*!40000 ALTER TABLE `bank_card` ENABLE KEYS */;

-- 导出  表 investment.biz_number 结构
CREATE TABLE IF NOT EXISTS `biz_number` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL,
  `value` bigint(20) NOT NULL DEFAULT '1',
  `memo` varchar(100) DEFAULT NULL,
  `version` varchar(20) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='记录所有业务的编号\r\n如：\r\n回访编号:HF201712080001\r\n';

-- 正在导出表  investment.biz_number 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `biz_number` DISABLE KEYS */;
INSERT INTO `biz_number` (`id`, `type`, `value`, `memo`, `version`) VALUES
	(1, 'CUSTOMER_VISIT_CODE', 201712280050, '客户回访编号', '1');
/*!40000 ALTER TABLE `biz_number` ENABLE KEYS */;

-- 导出  表 investment.data_auth 结构
CREATE TABLE IF NOT EXISTS `data_auth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL COMMENT '数据权限分类##这个由具体的业务系统确定',
  `data_id` varchar(50) NOT NULL COMMENT '数据权限ID##业务编号',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `parent_data_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '父级数据ID##外键，关联父级数据权限',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据权限';

-- 正在导出表  investment.data_auth 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `data_auth` DISABLE KEYS */;
/*!40000 ALTER TABLE `data_auth` ENABLE KEYS */;

-- 导出  表 investment.data_dictionary 结构
CREATE TABLE IF NOT EXISTS `data_dictionary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `yn` int(4) DEFAULT '1' COMMENT '是否可用##{provider:"YnProvider"}',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='数据字典\r\n例:\r\n系统图片:IMAGE_CODE';

-- 正在导出表  investment.data_dictionary 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `data_dictionary` DISABLE KEYS */;
INSERT INTO `data_dictionary` (`id`, `code`, `name`, `notes`, `created`, `modified`, `yn`) VALUES
	(1, 'data_auth_type', '数据权限类型', '数据权限类型', '2017-10-27 10:02:42', '2017-11-13 11:18:40', 1),
	(2, 'system', '系统', '系统', '2017-11-13 11:23:05', '2018-01-19 15:54:51', 1),
	(3, 'priority', '优先级', '优先级', '2017-11-21 15:57:18', '2018-01-19 15:54:52', 1),
	(4, 'bank', '开户行', '开户行', '2018-01-19 14:47:53', '2018-01-19 15:54:53', 1),
	(5, 'security_level', '安全级别', '投资平台安全级别', '2018-01-19 20:10:11', '2018-01-19 20:14:41', 1);
/*!40000 ALTER TABLE `data_dictionary` ENABLE KEYS */;

-- 导出  表 investment.data_dictionary_value 结构
CREATE TABLE IF NOT EXISTS `data_dictionary_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级id',
  `dd_id` bigint(20) DEFAULT NULL COMMENT '数据字典id',
  `order_number` int(11) DEFAULT NULL COMMENT '排序号',
  `code` varchar(255) DEFAULT NULL COMMENT '编码',
  `value` varchar(255) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL COMMENT '备注',
  `period_begin` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '启用时间',
  `period_end` timestamp NULL DEFAULT NULL COMMENT '停用时间',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `yn` int(4) DEFAULT '1' COMMENT '是否可用##{provider:"YnProvider", data:[{value:0,text:"不可用"},{value:1, text:"可用"}]}',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=183 DEFAULT CHARSET=utf8 COMMENT='数据字典值';

-- 正在导出表  investment.data_dictionary_value 的数据：~29 rows (大约)
/*!40000 ALTER TABLE `data_dictionary_value` DISABLE KEYS */;
INSERT INTO `data_dictionary_value` (`id`, `parent_id`, `dd_id`, `order_number`, `code`, `value`, `notes`, `period_begin`, `period_end`, `created`, `modified`, `yn`) VALUES
	(31, NULL, 3, 1, '最高', 'highest', '最高', '2018-01-19 15:55:14', NULL, '2017-11-21 17:14:17', '2018-01-19 15:55:14', 1),
	(32, NULL, 3, 2, '高', 'high', '高', '2018-01-19 15:55:16', NULL, '2017-11-21 17:14:47', '2018-01-19 15:55:16', 1),
	(33, NULL, 3, 3, '常规', 'normal', '常规', '2018-01-19 15:55:17', NULL, '2017-11-21 17:15:02', '2018-01-19 15:55:17', 1),
	(34, NULL, 3, 4, '低', 'low', '低', '2018-01-19 15:55:18', NULL, '2017-11-21 17:15:23', '2018-01-19 15:55:18', 1),
	(35, NULL, 2, 1, '投资理财管理系统', 'invest', '投资理财管理系统', '2018-01-19 16:34:02', NULL, '2017-11-13 14:40:31', '2018-01-19 16:34:02', 1),
	(159, NULL, 4, 2, '中国建设银行', 'CBC', '中国建设银行', '2018-01-19 15:55:52', NULL, '2018-01-19 14:49:10', '2018-01-19 15:55:52', 1),
	(160, NULL, 4, 1, '中国银行', 'BC', '中国银行', '2018-01-19 15:55:52', NULL, '2018-01-19 14:50:47', '2018-01-19 15:55:52', 1),
	(161, NULL, 4, 3, '中国农业银行', 'ABC', '中国农业银行', '2018-01-19 15:55:52', NULL, '2018-01-19 14:51:02', '2018-01-19 15:55:52', 1),
	(162, NULL, 4, 4, '中国工商银行', 'ICBC', '中国工商银行', '2018-01-19 15:55:52', NULL, '2018-01-19 14:51:19', '2018-01-19 15:55:52', 1),
	(163, NULL, 4, 5, '民生银行', 'CMSB', '民生银行', '2018-01-19 15:55:52', NULL, '2018-01-19 14:51:46', '2018-01-19 15:55:52', 1),
	(164, NULL, 4, 6, '招商银行', 'CMBC', '招商银行', '2018-01-19 15:55:52', NULL, '2018-01-19 14:52:07', '2018-01-19 15:55:52', 1),
	(165, NULL, 4, 7, '兴业银行', 'CIB', '兴业银行', '2018-01-19 15:55:52', NULL, '2018-01-19 14:52:37', '2018-01-19 15:55:52', 1),
	(166, NULL, 4, 8, '汇丰银行', 'HSBC', '汇丰银行', '2018-01-19 15:55:52', NULL, '2018-01-19 14:53:06', '2018-01-19 15:55:52', 1),
	(167, NULL, 4, 9, '国家开发银行', 'CDB', '国家开发银行', '2018-01-19 15:55:52', NULL, '2018-01-19 14:53:34', '2018-01-19 15:55:52', 1),
	(168, NULL, 4, 10, '北京市商业银行', 'BOB', '北京市商业银行', '2018-01-19 15:55:52', NULL, '2018-01-19 14:54:08', '2018-01-19 15:55:52', 1),
	(169, NULL, 4, 11, '中信银行', 'CCB', '中信银行', '2018-01-19 15:55:52', NULL, '2018-01-19 14:54:49', '2018-01-19 15:55:52', 1),
	(170, NULL, 4, 12, '中国光大银行', 'CEB', '中国光大银行', '2018-01-19 15:55:52', NULL, '2018-01-19 14:55:04', '2018-01-19 15:55:52', 1),
	(171, NULL, 4, 13, '上海浦东发展银行', 'SPDB', '上海浦东发展银行', '2018-01-19 15:55:52', NULL, '2018-01-19 14:55:27', '2018-01-19 15:55:52', 1),
	(172, NULL, 4, 14, '交通银行', 'BCM', '交通银行', '2018-01-19 15:55:52', NULL, '2018-01-19 14:55:46', '2018-01-19 15:55:52', 1),
	(173, NULL, 4, 15, '广东发展银行', 'GDB', '广东发展银行', '2018-01-19 15:55:52', NULL, '2018-01-19 14:56:13', '2018-01-19 15:55:52', 1),
	(174, NULL, 4, 16, '华夏银行', 'HXB', '华夏银行', '2018-01-19 15:55:52', NULL, '2018-01-19 14:57:51', '2018-01-19 15:55:52', 1),
	(175, NULL, 4, 17, '中国农村信用社', 'CUB', '中国农村信用社', '2018-01-19 15:55:52', NULL, '2018-01-19 14:58:14', '2018-01-19 15:55:52', 1),
	(176, NULL, 4, 18, '江西银行', 'JXB', '江西银行', '2018-01-19 15:55:52', NULL, '2018-01-19 14:58:53', '2018-01-19 15:55:52', 1),
	(177, NULL, 5, 1, '保守型', '1', '保守型', '2018-01-19 20:14:50', NULL, '2018-01-19 20:10:45', '2018-01-19 20:14:50', 1),
	(178, NULL, 5, 2, '稳健型', '2', '稳健型', '2018-01-19 20:14:56', NULL, '2018-01-19 20:11:00', '2018-01-19 20:14:56', 1),
	(179, NULL, 5, 3, '进取型', '3', '进取型', '2018-01-19 20:14:59', NULL, '2018-01-19 20:11:23', '2018-01-19 20:14:59', 1),
	(180, NULL, 5, 4, '激进型', '4', '激进型', '2018-01-19 20:15:03', NULL, '2018-01-19 20:11:59', '2018-01-19 20:15:03', 1),
	(181, NULL, 5, 5, '需警惕', '5', '需警惕', '2018-01-19 20:15:05', NULL, '2018-01-19 20:12:15', '2018-01-19 20:15:05', 1),
	(182, NULL, 4, 19, '厦门国际银行', 'XIB', '厦门国际银行', '2018-01-23 17:08:55', NULL, '2018-01-23 17:08:56', '2018-01-23 17:08:56', 1);
/*!40000 ALTER TABLE `data_dictionary_value` ENABLE KEYS */;

-- 导出  表 investment.department 结构
CREATE TABLE IF NOT EXISTS `department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL COMMENT '部门名',
  `code` varchar(20) DEFAULT NULL COMMENT '编号',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作员id',
  `notes` varchar(255) DEFAULT NULL COMMENT '备注',
  `parent_id` bigint(20) NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='部门';

-- 正在导出表  investment.department 的数据：~8 rows (大约)
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` (`id`, `name`, `code`, `operator_id`, `notes`, `parent_id`, `created`, `modified`) VALUES
	(1, '前台信息部', NULL, NULL, '前台信息部', -1, '2017-09-07 11:54:35', '2017-10-20 09:55:14'),
	(2, '测试部', NULL, NULL, '测试部', -1, '2017-09-07 14:27:36', '2017-10-20 09:55:09'),
	(3, '产品部', NULL, NULL, '产品部', -1, '2017-09-07 14:29:02', '2017-10-20 09:55:05'),
	(4, '运维部', NULL, NULL, '运维部', -1, '2017-10-30 14:15:36', '2017-10-30 14:15:36'),
	(17, '后台信息部', NULL, NULL, '后台信息部', -1, '2017-11-06 13:57:25', '2017-11-06 13:57:25'),
	(18, '移动部', NULL, NULL, '移动部', -1, '2017-11-08 13:01:54', '2017-11-08 13:01:54'),
	(19, '人力资源部', NULL, NULL, '人力资源部', -1, '2017-11-08 14:15:35', '2017-11-08 14:15:35'),
	(20, 'CRM项目组', NULL, NULL, 'CRM项目组', 1, '2017-12-05 12:55:41', '2017-12-05 12:55:41');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;

-- 导出  过程 investment.getChildDepartments 结构
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

-- 导出  函数 investment.getParentMenus 结构
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

-- 导出  表 investment.investment 结构
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
  `created_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  `modified_id` bigint(20) DEFAULT NULL COMMENT '修改人',
  `created` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='投资理财记录';

-- 正在导出表  investment.investment 的数据：~6 rows (大约)
/*!40000 ALTER TABLE `investment` DISABLE KEYS */;
INSERT INTO `investment` (`id`, `investor_id`, `platform_id`, `project_name`, `start_date`, `end_date`, `project_duration`, `project_duration_unit`, `profit_ratio`, `deducted`, `interest_coupon`, `cash_coupon`, `investment`, `is_remind`, `bank_card_id`, `notes`, `created_id`, `modified_id`, `created`, `modified`) VALUES
	(1, 1, 1, 'test', '2018-01-23', '2019-01-23', 1, 5, 8.50, 5000, 1.50, 1000, 999000, 0, 1, '备注', NULL, 1, '2018-01-22 11:36:46', '2018-01-24 18:10:39'),
	(2, 1, 2, 'test2', '2018-01-17', '2018-02-16', 1, 3, 15.00, 0, 0.00, 0, 1000000, 0, 2, NULL, 1, 1, '2018-01-22 16:31:35', '2018-01-25 09:56:54'),
	(3, 2, 2, 'test3', '2018-01-24', '2018-07-23', 6, 3, 12.00, 0, 0.00, 0, 2000000, 0, 2, '备注', 1, 1, '2018-01-24 18:00:38', '2018-01-25 09:52:20'),
	(4, 2, 1, 'test4', '2018-01-24', '2019-01-23', 1, 5, 8.50, 0, 0.00, 0, 5000000, 0, 2, NULL, 1, 1, '2018-01-24 18:01:35', '2018-01-25 09:39:46'),
	(5, 2, 1, 'test5', '2018-01-25', '2018-02-24', 1, 3, 6.00, 0, 0.00, 0, 1500000, 0, 1, NULL, 1, 1, '2018-01-25 09:37:40', '2018-01-25 09:39:47'),
	(6, 1, 1, 'test6', '2017-11-21', '2017-12-20', 1, 3, 6.50, 0, 0.00, 0, 1000000, 0, 1, '备注', 1, 1, '2018-01-25 09:45:36', '2018-01-25 09:45:54');
/*!40000 ALTER TABLE `investment` ENABLE KEYS */;

-- 导出  表 investment.investment_platform 结构
CREATE TABLE IF NOT EXISTS `investment_platform` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `supervision_bank` varchar(20) DEFAULT NULL COMMENT '资金监管银行',
  `is_record` int(4) DEFAULT NULL COMMENT '是否备案',
  `registry_place` varchar(10) DEFAULT NULL COMMENT '注册地',
  `business_place` varchar(10) DEFAULT NULL COMMENT '经营地',
  `platform_background` varchar(120) DEFAULT NULL COMMENT '平台背景',
  `security_level` int(4) DEFAULT NULL COMMENT '安全级别',
  `avg_profit_ratio` int(4) DEFAULT NULL COMMENT '平均收益率',
  `business_mode` varchar(120) DEFAULT NULL COMMENT '业务模式',
  `flexible` varchar(120) DEFAULT NULL COMMENT '灵活度',
  `guarantee` varchar(60) DEFAULT NULL COMMENT '保障',
  `introduction` varchar(250) DEFAULT NULL COMMENT '平台介绍',
  `uptime` date DEFAULT NULL COMMENT '上线时间',
  `notes` varchar(250) DEFAULT NULL COMMENT '备注信息',
  `created` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='投资平台';

-- 正在导出表  investment.investment_platform 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `investment_platform` DISABLE KEYS */;
INSERT INTO `investment_platform` (`id`, `name`, `supervision_bank`, `is_record`, `registry_place`, `business_place`, `platform_background`, `security_level`, `avg_profit_ratio`, `business_mode`, `flexible`, `guarantee`, `introduction`, `uptime`, `notes`, `created`, `modified`) VALUES
	(1, '宜人贷', '广东发展银行', 0, '北京', '北京', '上市公司', 2, 8, '宜人贷创新实现P2P从客户获取、风险控制、交易达成到客户服务全流程线上操作。宜人贷专注于具有信用维护意识的城市白领人群，这一群体具有较好的教育水平、信用意识以及稳定的还款能力。', '宜定盈可提前退出，但要收取1%～2%的紧急退出费，且会扣除相应的优惠券、宜人币（如有使用）；精英标持有90天之后可以转让，收取成交金额的2‰ ', '质保服务专款，存管在广发银行', '中国首家在美国纽交所上市的P2P公司，投宜人贷在安全性上还是很有保障的，但收益很一般', '2012-01-01', '中国首家在美国纽交所上市的P2P公司，投宜人贷在安全性上还是很有保障的，但收益很一般', '2018-01-19 17:53:28', '2018-01-22 09:56:48'),
	(2, '达人贷', 'XIB', 0, '深圳', '深圳', '民营系', 3, 12, '达人贷只做小额信贷业务，其中超80%是小微企业主借贷，借款金额在5-7万。剩余20%不到是薪金贷，给有稳定工资收入的人群放贷，平均借款金额也在7万左右。', '微信宝可转让', 'IPC技术。目前平台的不良率在2.6%左右，待收近1亿，风险准备金330万，数据上看是可以覆盖住风险的。', '深圳一家做信贷的小平台，业务规模不大，但总体做的还算比较扎实。团队经验丰富，且比较稳定，平台行业口碑尚可，投资性价比还不错', '2013-12-01', '业务方面平台是完全合规的，所有项目都遵循小额分散。\r\n12个月内（包含12个月）投资安全都有一定保障。\r\n每天10:00和18:00放标。\r\n', '2018-01-23 17:03:25', '2018-01-23 17:13:15');
/*!40000 ALTER TABLE `investment_platform` ENABLE KEYS */;

-- 导出  表 investment.menu 结构
CREATE TABLE IF NOT EXISTS `menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级菜单id',
  `order_number` int(11) DEFAULT NULL COMMENT '排序号',
  `menu_url` varchar(255) DEFAULT NULL COMMENT '菜单url',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `description` varchar(255) NOT NULL COMMENT '描述',
  `target` int(4) DEFAULT NULL COMMENT '打开链接的位置##{data:[{value:0, text:"当前窗口"},{value:1, text:"新开窗口"}]}',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `yn` int(4) NOT NULL DEFAULT '1' COMMENT '有效性##数据有效性，用于逻辑删除##{data:[{value:0, text:"无效"},{value:1, text:"有效"}]}',
  `type` int(4) NOT NULL DEFAULT '0' COMMENT '类型##{data:[{value:0, text:"目录"},{value:1, text:"链接"}]}',
  `icon_cls` varchar(40) DEFAULT NULL COMMENT '菜单图标',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=176 DEFAULT CHARSET=utf8 COMMENT='菜单';

-- 正在导出表  investment.menu 的数据：~24 rows (大约)
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` (`id`, `parent_id`, `order_number`, `menu_url`, `name`, `description`, `target`, `created`, `modified`, `yn`, `type`, `icon_cls`) VALUES
	(1, 4, 1, 'http://investadm.artist.com:9999/user/index.html', '用户管理', '', 0, '2017-07-18 01:51:17', '2018-01-18 16:12:25', 1, 1, '1'),
	(2, 4, 4, 'http://investadm.artist.com:9999/menu/index.html', '菜单管理', '菜单管理', 0, '2017-07-18 19:37:48', '2018-01-18 16:12:28', 1, 1, '1'),
	(4, -1, 50, '', '权限管理', '', NULL, '2017-07-19 22:52:45', '2018-01-18 17:14:43', 1, 0, NULL),
	(5, 4, 2, 'http://investadm.artist.com:9999/role/index.html', '角色管理', '', NULL, '2017-07-20 00:27:10', '2018-01-18 16:12:29', 1, 1, NULL),
	(6, 4, 3, 'http://investadm.artist.com:9999/user/onlineList.html', '在线用户', '', NULL, '2017-07-20 00:31:12', '2018-01-18 16:12:32', 1, 1, NULL),
	(60, -1, 40, '', '系统设置', '', NULL, '2017-09-07 10:21:05', '2018-01-18 17:14:39', 1, 0, NULL),
	(72, 60, 2, 'http://invest.artist.com/scheduleJob/index.html', '调度器', '', NULL, '2017-10-24 09:24:28', '2018-01-18 16:12:39', 1, 1, NULL),
	(74, 60, 1, 'http://invest.artist.com/dataDictionary/index.html', '数据字典', '', NULL, '2017-10-25 11:33:55', '2018-01-18 16:12:42', 1, 1, NULL),
	(75, 4, 5, 'http://investadm.artist.com:9999/department/index.html', '部门管理', '', NULL, '2017-10-25 11:34:11', '2018-01-18 16:12:44', 1, 1, NULL),
	(97, 1, 1, 'http://investadm.artist.com:9999/dataAuth/editUserDataAuth.html', '用户数据权限', '', NULL, '2017-10-31 10:40:45', '2018-01-18 16:12:47', 1, 2, NULL),
	(98, 5, 1, 'http://investadm.artist.com:9999/role/roleMenuAndResource.html', '功能权限', '', NULL, '2017-10-31 10:44:14', '2018-01-18 16:12:49', 1, 2, NULL),
	(99, 5, 1, 'http://investadm.artist.com:9999/dataAuth/editRoleDataAuth.html', '数据权限', '', NULL, '2017-10-31 11:09:20', '2018-01-18 16:12:57', 1, 2, NULL),
	(120, -1, 30, '', '报表', '', NULL, '2017-11-30 18:01:49', '2018-01-18 17:14:35', 1, 0, NULL),
	(126, -1, 1, 'http://invest.artist.com/index.html', '首页', '', NULL, '2017-12-01 14:17:55', '2018-01-18 16:13:00', 1, 1, NULL),
	(130, 60, 3, 'http://invest.artist.com/systemConfig/index.html', '系统配置', '', NULL, '2017-12-05 14:27:02', '2018-01-19 10:54:38', 1, 1, NULL),
	(143, 120, 10, 'http://invest.artist.com/investment/investmentDistributionPieChart.html', '投资分布', '投资分布', NULL, '2017-12-14 17:39:10', '2018-01-25 15:48:12', 1, 1, NULL),
	(168, -1, 20, '', '投资理财', '', NULL, '2018-01-18 17:13:53', '2018-01-18 17:14:30', 1, 0, NULL),
	(169, 168, 10, 'http://invest.artist.com/bankCard/index.html', '银行卡', '', NULL, '2018-01-18 17:15:26', '2018-01-18 17:17:39', 1, 1, NULL),
	(170, 168, 20, 'http://invest.artist.com/investmentPlatform/index.html', '投资平台', '', NULL, '2018-01-18 17:15:57', '2018-01-18 17:17:43', 1, 1, NULL),
	(171, 168, 30, 'http://invest.artist.com/investment/index.html', '投资记录', '', NULL, '2018-01-18 17:16:22', '2018-01-18 17:17:47', 1, 1, NULL),
	(172, 169, 1, 'http://invest.artist.com/selectDialog/user.html', '选择用户', '选择用户', NULL, '2018-01-19 11:34:55', '2018-01-19 11:36:03', 1, 2, NULL),
	(173, 171, 1, 'http://invest.artist.com/selectDialog/bankCard.html', '选择银行卡', '选择银行卡', NULL, '2018-01-22 17:02:10', '2018-01-22 17:02:19', 1, 2, NULL),
	(174, 120, 20, 'http://invest.artist.com/investment/profitDistributionPieChart.html', '投资收益分布', '投资收益分布', NULL, '2018-01-25 15:48:05', '2018-01-25 15:48:41', 1, 1, NULL),
	(175, 120, 30, 'http://invest.artist.com/investment/investmentStats.html', '投资统计', '投资统计', NULL, '2018-01-25 16:54:04', '2018-01-25 16:54:35', 1, 1, NULL);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;

-- 导出  表 investment.resource 结构
CREATE TABLE IF NOT EXISTS `resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `menu_id` bigint(20) NOT NULL COMMENT '外键，关联menu表',
  `code` varchar(255) DEFAULT NULL COMMENT '编码##原resource URL',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态##{data:[{value:1, text:"启用"},{value:0, text:"停用"}]}',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `yn` int(4) NOT NULL DEFAULT '1' COMMENT '有效性##数据有效性，用于逻辑删除##{data:[{value:0, text:"无效"},{value:1, text:"有效"}]}',
  PRIMARY KEY (`id`),
  KEY `FK_RESOURCE_NAVBAR` (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8 COMMENT='资源';

-- 正在导出表  investment.resource 的数据：~26 rows (大约)
/*!40000 ALTER TABLE `resource` DISABLE KEYS */;
INSERT INTO `resource` (`id`, `name`, `description`, `menu_id`, `code`, `status`, `created`, `modified`, `yn`) VALUES
	(1, '新增用户', '新增用户', 1, 'addUser', 1, '2017-07-21 02:14:53', '2017-12-18 16:48:44', 1),
	(2, '删除用户', '', 1, 'removeUser', 1, '2017-07-21 02:14:53', '2017-08-08 11:00:14', 1),
	(3, '修改用户', '', 1, 'editUser', 1, '2017-07-21 02:14:53', '2017-08-08 11:00:13', 1),
	(4, '禁用用户', '', 1, 'disableUser', 1, '2017-07-20 02:36:12', '2017-08-08 11:00:12', 1),
	(5, '启用用户', '', 1, 'enableUser', 1, '2017-07-21 02:14:53', '2017-08-08 10:32:25', 1),
	(6, '查看用户详情', '', 1, 'viewUserDetail', 1, '2017-07-21 02:14:53', '2017-08-08 10:58:01', 1),
	(7, '查看用户权限', '', 1, 'viewUserDataAuth', 1, '2017-07-21 02:14:53', '2017-07-31 10:15:00', 1),
	(8, '保存用户', '', 1, 'saveUser', 1, '2017-07-20 02:36:12', '2017-08-08 10:32:27', 1),
	(9, '取消保存用户', '', 1, 'cancelSaveUser', 1, '2017-07-21 02:14:53', '2017-07-31 10:13:16', 1),
	(10, '新增菜单', '', 2, 'addMenu', 1, '2017-07-28 00:22:43', '2017-08-08 11:00:19', 1),
	(11, '编辑菜单', '', 2, 'editMenu', 1, '2017-07-28 00:26:36', '2017-08-08 11:00:04', 1),
	(12, '删除菜单', '', 2, 'deleteMenu', 1, '2017-07-28 00:29:41', '2017-07-31 17:21:27', 1),
	(15, '分配用户角色', '', 1, 'grantRoleToUser', 1, '2017-07-21 02:14:53', '2017-07-31 10:14:59', 1),
	(20, '新增角色', '', 5, 'addRole', 1, '2017-07-21 02:14:53', '2017-08-04 15:41:01', 1),
	(21, '删除角色', '', 5, 'removeRole', 1, '2017-07-21 02:14:53', '2017-08-08 10:52:16', 1),
	(22, '修改角色', '', 5, 'editRole', 1, '2017-07-21 02:14:53', '2017-08-08 10:52:14', 1),
	(23, '查看角色权限', '', 5, 'viewRoleAuth', 1, '2017-07-21 02:14:53', '2017-08-08 10:52:13', 1),
	(24, '查看角色用户列表', '', 5, 'viewRoleUserList', 1, '2017-07-21 02:14:53', '2017-08-08 10:52:12', 1),
	(25, '保存角色', '', 5, 'saveRole', 1, '2017-07-20 02:36:12', '2017-08-08 10:52:11', 1),
	(26, '取消保存角色', '', 5, 'cancelSaveRole', 1, '2017-07-21 02:14:53', '2017-08-08 10:52:10', 1),
	(27, '删除角色用户关联', '', 5, 'unBindRoleUser', 1, '2017-07-21 02:14:53', '2017-08-08 10:52:09', 1),
	(31, '强制下线', '', 6, 'kickUserOffLine', 1, '2017-07-21 02:14:53', '2017-07-28 19:04:34', 1),
	(32, '新增部门', NULL, 75, 'addDept', 1, '2017-09-07 11:49:14', '2017-10-25 14:06:09', 1),
	(33, '编辑部门', NULL, 75, 'editDept', 1, '2017-09-07 11:49:50', '2017-10-25 14:06:11', 1),
	(34, '删除部门', NULL, 75, 'deleteDept', 1, '2017-09-07 11:50:02', '2017-10-25 14:06:13', 1),
	(67, '访问首页', NULL, 126, 'viewIndexPage', 1, '2017-12-18 16:23:17', '2017-12-18 16:23:17', 1);
/*!40000 ALTER TABLE `resource` ENABLE KEYS */;

-- 导出  表 investment.role 结构
CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(255) NOT NULL COMMENT '角色名',
  `description` varchar(255) NOT NULL COMMENT '角色描述',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='角色';

-- 正在导出表  investment.role 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`id`, `role_name`, `description`, `created`, `modified`) VALUES
	(1, '超级管理员', '超级管理员', '2017-07-13 19:37:52', '2017-07-19 18:02:31'),
	(2, '普通用户', '普通用户', '2017-11-06 09:21:18', '2017-12-25 09:37:13');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;

-- 导出  表 investment.role_data_auth 结构
CREATE TABLE IF NOT EXISTS `role_data_auth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `data_auth_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  investment.role_data_auth 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `role_data_auth` DISABLE KEYS */;
/*!40000 ALTER TABLE `role_data_auth` ENABLE KEYS */;

-- 导出  表 investment.role_menu 结构
CREATE TABLE IF NOT EXISTS `role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID##外键',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID##外键',
  PRIMARY KEY (`id`),
  KEY `FK_ROLEMENU_ROLE` (`role_id`) USING BTREE,
  KEY `FK_MENUROLE_MENU` (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3905 DEFAULT CHARSET=utf8 COMMENT='角色菜单关系';

-- 正在导出表  investment.role_menu 的数据：~25 rows (大约)
/*!40000 ALTER TABLE `role_menu` DISABLE KEYS */;
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES
	(3703, 2, 126),
	(3881, 1, 4),
	(3882, 1, 1),
	(3883, 1, 97),
	(3884, 1, 2),
	(3885, 1, 5),
	(3886, 1, 98),
	(3887, 1, 99),
	(3888, 1, 6),
	(3889, 1, 75),
	(3890, 1, 60),
	(3891, 1, 72),
	(3892, 1, 74),
	(3893, 1, 130),
	(3894, 1, 120),
	(3895, 1, 143),
	(3896, 1, 174),
	(3897, 1, 175),
	(3898, 1, 126),
	(3899, 1, 168),
	(3900, 1, 169),
	(3901, 1, 172),
	(3902, 1, 170),
	(3903, 1, 171),
	(3904, 1, 173);
/*!40000 ALTER TABLE `role_menu` ENABLE KEYS */;

-- 导出  表 investment.role_resource 结构
CREATE TABLE IF NOT EXISTS `role_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID##外键',
  `resource_id` bigint(20) NOT NULL COMMENT '资源ID##外键',
  PRIMARY KEY (`id`),
  KEY `FK_ROLEPERMISSION_ROLE` (`role_id`) USING BTREE,
  KEY `FK_ROLEPERMISSION_RESOURCE` (`resource_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5059 DEFAULT CHARSET=utf8 COMMENT='角色资源关系';

-- 正在导出表  investment.role_resource 的数据：~27 rows (大约)
/*!40000 ALTER TABLE `role_resource` DISABLE KEYS */;
INSERT INTO `role_resource` (`id`, `role_id`, `resource_id`) VALUES
	(4824, 2, 67),
	(5033, 1, 1),
	(5034, 1, 2),
	(5035, 1, 3),
	(5036, 1, 4),
	(5037, 1, 5),
	(5038, 1, 6),
	(5039, 1, 7),
	(5040, 1, 8),
	(5041, 1, 9),
	(5042, 1, 15),
	(5043, 1, 10),
	(5044, 1, 11),
	(5045, 1, 12),
	(5046, 1, 20),
	(5047, 1, 21),
	(5048, 1, 22),
	(5049, 1, 23),
	(5050, 1, 24),
	(5051, 1, 25),
	(5052, 1, 26),
	(5053, 1, 27),
	(5054, 1, 31),
	(5055, 1, 32),
	(5056, 1, 33),
	(5057, 1, 34),
	(5058, 1, 67);
/*!40000 ALTER TABLE `role_resource` ENABLE KEYS */;

-- 导出  表 investment.schedule_job 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8 COMMENT='任务调度';

-- 正在导出表  investment.schedule_job 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `schedule_job` DISABLE KEYS */;
INSERT INTO `schedule_job` (`id`, `created`, `modified`, `job_name`, `job_group`, `job_status`, `job_data`, `cron_expression`, `repeat_interval`, `start_delay`, `description`, `bean_class`, `spring_id`, `url`, `is_concurrent`, `method_name`) VALUES
	(20, '2017-11-01 15:01:31', '2018-01-18 16:52:23', 'quartzRecycleJob', 'system', 1, '{"code":"test-001","created":"2017-11-08 08:57:51","docUrl":"redmine","emailNotice":1,"git":"git","id":45,"market":"HD","projectId":103,"projectPhase":"Design","publishMemberId":42,"releaseTime":"2017-11-08 08:57:51","version":"0.0.1"}', NULL, 1800, 3, '调度器回收(勿动)', NULL, 'quartzRecycleJob', NULL, 0, 'scan');
/*!40000 ALTER TABLE `schedule_job` ENABLE KEYS */;

-- 导出  表 investment.system_config 结构
CREATE TABLE IF NOT EXISTS `system_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `code` varchar(100) DEFAULT NULL,
  `value` varchar(100) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `yn` int(4) DEFAULT NULL COMMENT '是否可用##{provider:"ynProvider"}',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='配置系统变量,';

-- 正在导出表  investment.system_config 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `system_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_config` ENABLE KEYS */;

-- 导出  表 investment.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `last_login_ip` varchar(20) DEFAULT NULL COMMENT '最后登录ip',
  `last_login_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后登录时间',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态##状态##{data:[{value:1,text:"启用"},{value:0,text:"停用"}]}',
  `yn` int(4) NOT NULL COMMENT '有效性##数据有效性，用于逻辑删除##{data:[{value:0, text:"无效"},{value:1, text:"有效"}]}',
  `real_name` varchar(64) NOT NULL DEFAULT 'system_default' COMMENT '真实姓名',
  `serial_number` varchar(128) NOT NULL DEFAULT '000' COMMENT '用户编号',
  `fixed_line_telephone` varchar(24) DEFAULT NULL COMMENT '固定电话',
  `cellphone` varchar(24) NOT NULL COMMENT '手机号码',
  `email` varchar(64) NOT NULL COMMENT '邮箱',
  `valid_time_begin` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '有效时间开始点',
  `valid_time_end` timestamp NULL DEFAULT NULL COMMENT '有效时间结束点',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=181 DEFAULT CHARSET=utf8 COMMENT='用户';

-- 正在导出表  investment.user 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `user_name`, `password`, `last_login_ip`, `last_login_time`, `created`, `modified`, `status`, `yn`, `real_name`, `serial_number`, `fixed_line_telephone`, `cellphone`, `email`, `valid_time_begin`, `valid_time_end`) VALUES
	(1, 'admin', '178591FD5BB0C24851', '127.0.0.1', '2018-01-25 17:19:57', '2017-07-11 14:13:04', '2017-12-14 19:54:53', 1, 1, '王宓', '001', '0281234567', '13500000000', 'yangfuchun@diligrp.com', '2017-07-11 14:13:04', '2018-07-12 00:00:00'),
	(2, 'tiantian', '3949BA59ABBE56E057', '127.0.0.1', '2018-01-25 09:39:12', '2018-01-24 17:58:57', '2018-01-25 09:39:12', 1, 1, '田恬', '002', NULL, '18108087571', '11266133@qq.com', '2018-01-24 17:58:57', NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

-- 导出  表 investment.user_data_auth 结构
CREATE TABLE IF NOT EXISTS `user_data_auth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_auth_id` bigint(20) NOT NULL COMMENT '数据权限表id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=141 DEFAULT CHARSET=utf8 COMMENT='数据权限';

-- 正在导出表  investment.user_data_auth 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `user_data_auth` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_data_auth` ENABLE KEYS */;

-- 导出  表 investment.user_department 结构
CREATE TABLE IF NOT EXISTS `user_department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `department_id` bigint(20) NOT NULL COMMENT '部门id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35271 DEFAULT CHARSET=utf8 COMMENT='用户部门关系';

-- 正在导出表  investment.user_department 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `user_department` DISABLE KEYS */;
INSERT INTO `user_department` (`id`, `department_id`, `user_id`) VALUES
	(35269, 1, 2),
	(35270, 1, 1);
/*!40000 ALTER TABLE `user_department` ENABLE KEYS */;

-- 导出  表 investment.user_platform 结构
CREATE TABLE IF NOT EXISTS `user_platform` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL COMMENT '投资人id',
  `platform_id` bigint(20) DEFAULT NULL COMMENT '投资平台id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户平台关系';

-- 正在导出表  investment.user_platform 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `user_platform` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_platform` ENABLE KEYS */;

-- 导出  表 investment.user_role 结构
CREATE TABLE IF NOT EXISTS `user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id##外键',
  `role_id` bigint(20) NOT NULL COMMENT '角色id##外键',
  PRIMARY KEY (`id`),
  KEY `FK_USERROLE_USER` (`user_id`) USING BTREE,
  KEY `FK_USERROLE_ROLE` (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=278 DEFAULT CHARSET=utf8 COMMENT='用户角色关系';

-- 正在导出表  investment.user_role 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` (`id`, `user_id`, `role_id`) VALUES
	(276, 2, 1),
	(277, 1, 1);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
