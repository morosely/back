CREATE TABLE IF NOT EXISTS  `ordercallnosshconfig` (
    `host` varchar(20) NOT NULL COMMENT '主机IP',
    `port` int(11) NOT NULL COMMENT '主机端口',
    `user` varchar(20) NOT NULL COMMENT '用户',
    `password` varchar(64) NOT NULL COMMENT '密码'
    )COMMENT='订单叫餐SSH配置表';

CREATE TABLE IF NOT EXISTS ordercallno (
    id bigint(20) NOT NULL COMMENT '主键ID',
    erpCode varchar(3) NOT NULL COMMENT '经营公司编码',
    shopCode varchar(20) NOT NULL COMMENT '门店编码',
    stallCode varchar(20) NOT NULL COMMENT '档口编码',
    stallName varchar(64) NOT NULL COMMENT '档口名称',
    callNo varchar(20) NOT NULL COMMENT '叫餐号',
    status smallint(6) NOT NULL COMMENT '订单叫号状态 0-已取消/1-制作中/2-待取餐/3-已取餐',
    orderId varchar(20) DEFAULT NULL COMMENT '订单号',
    entId bigint(20) DEFAULT NULL COMMENT '零售商ID',
    lang varchar(10) DEFAULT 'CN' COMMENT '语言类型',
    creator varchar(64) DEFAULT NULL COMMENT '创建人(收银机号)',
    createDate datetime DEFAULT NULL COMMENT '创建日期',
    modifier varchar(64) DEFAULT NULL COMMENT '修改人(收银机号)',
    updateDate datetime DEFAULT NULL COMMENT '修改日期',
    PRIMARY KEY (id),
    UNIQUE KEY idx_callno_shopstallno (shopCode,stallCode,callNo) USING BTREE
    ) COMMENT='订单叫号状态表';

CREATE TABLE IF NOT EXISTS ordercallnohis (
    id bigint(20) NOT NULL COMMENT '主键ID',
    erpCode varchar(3) NOT NULL COMMENT '经营公司编码',
    shopCode varchar(20) NOT NULL COMMENT '门店编码',
    stallCode varchar(20) NOT NULL COMMENT '档口编码',
    stallName varchar(64) NOT NULL COMMENT '档口名称',
    callNo varchar(20) NOT NULL COMMENT '叫餐号',
    status smallint(6) NOT NULL COMMENT '订单叫号状态 0-已取消/1-制作中/2-待取餐/3-已取餐',
    orderId varchar(20) DEFAULT NULL COMMENT '订单号',
    entId bigint(20) DEFAULT NULL COMMENT '零售商ID',
    lang varchar(10) DEFAULT 'CN' COMMENT '语言类型',
    creator varchar(64) DEFAULT NULL COMMENT '创建人(收银机号)',
    createDate datetime DEFAULT NULL COMMENT '创建日期',
    modifier varchar(64) DEFAULT NULL COMMENT '修改人(收银机号)',
    updateDate datetime DEFAULT NULL COMMENT '修改日期',
    KEY idx_callnohis_shopstallno (shopCode,stallCode,callNo),
    KEY idx_callnohis_createdate (createDate),
    KEY idx_callnohis_updatedate (updateDate),
    KEY idx_callnohis_orderid (orderId)
    )COMMENT='订单叫号状态历史表';

sh orderCallNo.sh "CREATE TABLE IF NOT EXISTS ordercallno (
  id bigint(20) NOT NULL COMMENT '主键ID',
  erpCode varchar(3) NOT NULL COMMENT '经营公司编码',
  shopCode varchar(20) NOT NULL COMMENT '门店编码',
  stallCode varchar(20) NOT NULL COMMENT '档口编码',
  stallName varchar(64) NOT NULL COMMENT '档口名称',
  callNo varchar(20) NOT NULL COMMENT '叫餐号',
  status smallint(6) NOT NULL COMMENT '订单叫号状态 0-已取消/1-制作中/2-待取餐/3-已取餐',
  orderId varchar(20) DEFAULT NULL COMMENT '订单号',
  entId bigint(20) DEFAULT NULL COMMENT '零售商ID',
  lang varchar(10) DEFAULT 'CN' COMMENT '语言类型',
  creator varchar(64) DEFAULT NULL COMMENT '创建人(收银机号)',
  createDate datetime DEFAULT NULL COMMENT '创建日期',
  modifier varchar(64) DEFAULT NULL COMMENT '修改人(收银机号)',
  updateDate datetime DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (id),
  UNIQUE KEY idx_callno_shopstallno (shopCode,stallCode,callNo) USING BTREE
) COMMENT='订单叫号状态表';"

sh orderCallNo.sh "CREATE TABLE IF NOT EXISTS ordercallnohis (
  id bigint(20) NOT NULL COMMENT '主键ID',
  erpCode varchar(3) NOT NULL COMMENT '经营公司编码',
  shopCode varchar(20) NOT NULL COMMENT '门店编码',
  stallCode varchar(20) NOT NULL COMMENT '档口编码',
  stallName varchar(64) NOT NULL COMMENT '档口名称',
  callNo varchar(20) NOT NULL COMMENT '叫餐号',
  status smallint(6) NOT NULL COMMENT '订单叫号状态 0-已取消/1-制作中/2-待取餐/3-已取餐',
  orderId varchar(20) DEFAULT NULL COMMENT '订单号',
  entId bigint(20) DEFAULT NULL COMMENT '零售商ID',
  lang varchar(10) DEFAULT 'CN' COMMENT '语言类型',
  creator varchar(64) DEFAULT NULL COMMENT '创建人(收银机号)',
  createDate datetime DEFAULT NULL COMMENT '创建日期',
  modifier varchar(64) DEFAULT NULL COMMENT '修改人(收银机号)',
  updateDate datetime DEFAULT NULL COMMENT '修改日期',
  KEY idx_callnohis_shopstallno (shopCode,stallCode,callNo),
  KEY idx_callnohis_createdate (createDate),
  KEY idx_callnohis_updatedate (updateDate),
  KEY idx_callnohis_orderid (orderId)
)COMMENT='订单叫号状态历史表';"