CREATE TABLE IF NOT EXISTS `cateringtime` (
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `entId` bigint(20) NOT NULL COMMENT '零售商ID',
    `erpCode` varchar(3) DEFAULT NULL COMMENT '经营公司编码',
    `cateringTimeCode` varchar(50) DEFAULT NULL COMMENT '餐饮时段编码',
    `cateringTimeName` varchar(64) DEFAULT NULL COMMENT '餐饮时段名称',
    `startTime` time DEFAULT NULL COMMENT '开始时间',
    `endTime` time DEFAULT NULL COMMENT '结束时间',
    `status` char(1) DEFAULT '1' COMMENT '状态1:有效,0:无效',
    `lang` varchar(10) DEFAULT 'CN' COMMENT '语言类型',
    `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
    `createDate` datetime DEFAULT NULL COMMENT '创建日期',
    `modifier` varchar(64) DEFAULT NULL COMMENT '修改人',
    `updateDate` datetime DEFAULT NULL COMMENT '修改日期',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `idx_cateringtime_code` (`cateringTimeCode`,`erpCode`,`entId`)
) ENGINE=InnoDB COMMENT='餐饮时段表';

CREATE TABLE IF NOT EXISTS `packageattcate` (
    `id` bigint(20) NOT NULL COMMENT 'ID',
    `entId` bigint(20) NOT NULL COMMENT '零售商ID',
    `erpCode` varchar(3) DEFAULT NULL COMMENT '经营公司编码',
    `pCode` varchar(32) NOT NULL COMMENT '属性类别编码',
    `pName` varchar(64) NOT NULL COMMENT '属性类别名称',
    `pEnName` varchar(100) DEFAULT NULL COMMENT '属性类别英文名称',
    `parentCode` varchar(32) NOT NULL COMMENT '上级代码',
    `status` smallint(6) DEFAULT NULL COMMENT '状态',
    `level` smallint(6) DEFAULT NULL COMMENT '层级',
    `leafFlag` tinyint(1) DEFAULT NULL COMMENT '是否叶子结点',
    `lang` varchar(10) DEFAULT NULL COMMENT '语言类型',
    `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
    `createDate` datetime DEFAULT NULL COMMENT '创建日期',
    `modifier` varchar(64) DEFAULT NULL COMMENT '修改人',
    `updateDate` datetime DEFAULT NULL COMMENT '修改日期',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_packageattcate_gee` (`pCode`,`erpCode`,`entId`) USING BTREE
) COMMENT='套餐属性类别';

CREATE TABLE IF NOT EXISTS `packageattdict` (
    `id` bigint(20) NOT NULL COMMENT 'ID',
    `entId` bigint(20) NOT NULL COMMENT '零售商ID',
    `erpCode` varchar(3) DEFAULT NULL COMMENT '经营公司编码',
    `pCode` varchar(32) DEFAULT NULL COMMENT '属性分类编码',
    `dCode` varchar(32) NOT NULL COMMENT '属性编码',
    `dName` varchar(64) NOT NULL COMMENT '属性名称',
    `dEnName` varchar(100) DEFAULT NULL COMMENT '属性英文名称',
    `status` smallint(6) DEFAULT NULL COMMENT '状态',
    `lang` varchar(10) DEFAULT NULL COMMENT '语言类型',
    `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
    `createDate` datetime DEFAULT NULL COMMENT '创建日期',
    `modifier` varchar(64) DEFAULT NULL COMMENT '修改人',
    `updateDate` datetime DEFAULT NULL COMMENT '修改日期',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_packageattdict_pdee` (`dCode`,`pCode`,`erpCode`,`entId`) USING BTREE
) COMMENT='套餐属性字典';

/*CREATE TABLE IF NOT EXISTS `packageattgoodsref` (
    `id` bigint(20) NOT NULL COMMENT '工业分类ID',
    `entId` bigint(20) NOT NULL COMMENT '零售商ID',
    `erpCode` varchar(3) DEFAULT NULL COMMENT '经营公司编码',
    `pCode` varchar(10) NOT NULL COMMENT '属性类别编码',
    `goodsCode` varchar(20) NOT NULL COMMENT '商品编码',
    `status` smallint(6) DEFAULT NULL COMMENT '状态',
    `lang` varchar(10) DEFAULT NULL COMMENT '语言类型',
    `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
    `createDate` datetime DEFAULT NULL COMMENT '创建日期',
    `modifier` varchar(64) DEFAULT NULL COMMENT '修改人',
    `updateDate` datetime DEFAULT NULL COMMENT '修改日期',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_packageattgoodsref_pgee` (`pCode`,`goodsCode`,`entId`,`erpCode`) USING BTREE
) COMMENT='套餐分类与套餐商品关系表';*/

CREATE TABLE IF NOT EXISTS `packageattshopgoodsref` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `entId` bigint(20) NOT NULL COMMENT '零售商ID',
  `erpCode` varchar(3) NOT NULL COMMENT '经营公司编码',
  `pCode` varchar(32) DEFAULT NULL COMMENT '属性类别编码',
  `cateringTimeCode` varchar(50) DEFAULT NULL COMMENT '餐饮时段编码',
  `shopCode` varchar(20) NOT NULL COMMENT '门店编码',
  `stallCode` varchar(20) NOT NULL COMMENT '档口编码',
  `goodsCode` varchar(20) NOT NULL COMMENT '商品编码',
  `barNo` varchar(64) NOT NULL COMMENT '条码',
  `status` smallint(6) DEFAULT NULL COMMENT '状态',
  `sGoodsCode` varchar(20) DEFAULT NULL COMMENT '套餐编码（此字段有值时goodsCode套餐明细的商品编码）',
  `lang` varchar(10) DEFAULT NULL COMMENT '语言类型',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `createDate` datetime DEFAULT NULL COMMENT '创建日期',
  `modifier` varchar(64) DEFAULT NULL COMMENT '修改人',
  `updateDate` datetime DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_packageattshopgoodsref_uniquekey` (`goodsCode`,`barNo`,`shopCode`,`stallCode`,`erpCode`,`entId`) USING BTREE
) COMMENT='套餐分类与门店档口套餐商品关系表';

CREATE TABLE IF NOT EXISTS `salegoodsproperty` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `entId` bigint(20) NOT NULL COMMENT '零售商ID',
  `erpCode` varchar(3) NOT NULL COMMENT '经营公司编码',
  `shopCode` varchar(20) NOT NULL COMMENT '门店编码',
  `stallCode` varchar(20) NOT NULL COMMENT '档口编码',
  `goodsCode` varchar(20) NOT NULL COMMENT '商品编码',
  `barNo` varchar(64) NOT NULL COMMENT '条码',
  `sellout` char(1) DEFAULT NULL COMMENT '售罄标识(1:售罄,0:未售罄)',
  `lang` varchar(10) DEFAULT NULL COMMENT '语言类型',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `createDate` datetime DEFAULT NULL COMMENT '创建日期',
  `modifier` varchar(64) DEFAULT NULL COMMENT '修改人',
  `updateDate` datetime DEFAULT NULL COMMENT '修改日期',
  `back01` varchar(20) DEFAULT NULL COMMENT '备用字段01',
  `back02` varchar(20) DEFAULT NULL COMMENT '备用字段02',
  `back03` varchar(20) DEFAULT NULL COMMENT '备用字段03',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_salegoodsproperty_uniquekey` (`shopCode`,`stallCode`,`goodsCode`,`barNo`) USING BTREE
) COMMENT='可售商品—附属表';

###################### IDC执行的前置机脚本 Start #############################
sh orderCallNo.sh "CREATE TABLE IF NOT EXISTS cateringtime (
    id bigint(20) NOT NULL COMMENT '主键ID',
    entId bigint(20) NOT NULL COMMENT '零售商ID',
    erpCode varchar(3) DEFAULT NULL COMMENT '经营公司编码',
    cateringTimeCode varchar(50) DEFAULT NULL COMMENT '餐饮时段编码',
    cateringTimeName varchar(64) DEFAULT NULL COMMENT '餐饮时段名称',
    startTime time DEFAULT NULL COMMENT '开始时间',
    endTime time DEFAULT NULL COMMENT '结束时间',
    status char(1) DEFAULT '1' COMMENT '状态1:有效,0:无效',
    lang varchar(10) DEFAULT 'CN' COMMENT '语言类型',
    creator varchar(64) DEFAULT NULL COMMENT '创建人',
    createDate datetime DEFAULT NULL COMMENT '创建日期',
    modifier varchar(64) DEFAULT NULL COMMENT '修改人',
    updateDate datetime DEFAULT NULL COMMENT '修改日期',
    PRIMARY KEY (id) USING BTREE,
    UNIQUE KEY idx_cateringtime_code (cateringTimeCode,erpCode,entId)
) ENGINE=InnoDB COMMENT='餐饮时段表';"

sh orderCallNo.sh "CREATE TABLE IF NOT EXISTS packageattcate (
    id bigint(20) NOT NULL COMMENT 'ID',
    entId bigint(20) NOT NULL COMMENT '零售商ID',
    erpCode varchar(3) DEFAULT NULL COMMENT '经营公司编码',
    pCode varchar(32) NOT NULL COMMENT '属性类别编码',
    pName varchar(64) NOT NULL COMMENT '属性类别名称',
    pEnName varchar(100) DEFAULT NULL COMMENT '属性类别英文名称',
    parentCode varchar(32) NOT NULL COMMENT '上级代码',
    status smallint(6) DEFAULT NULL COMMENT '状态',
    level smallint(6) DEFAULT NULL COMMENT '层级',
    leafFlag tinyint(1) DEFAULT NULL COMMENT '是否叶子结点',
    lang varchar(10) DEFAULT NULL COMMENT '语言类型',
    creator varchar(64) DEFAULT NULL COMMENT '创建人',
    createDate datetime DEFAULT NULL COMMENT '创建日期',
    modifier varchar(64) DEFAULT NULL COMMENT '修改人',
    updateDate datetime DEFAULT NULL COMMENT '修改日期',
    PRIMARY KEY (id),
    UNIQUE KEY idx_packageattcate_gee (pCode,erpCode,entId) USING BTREE
) COMMENT='套餐属性类别';"

sh orderCallNo.sh "CREATE TABLE IF NOT EXISTS packageattdict (
    id bigint(20) NOT NULL COMMENT 'ID',
    entId bigint(20) NOT NULL COMMENT '零售商ID',
    erpCode varchar(3) DEFAULT NULL COMMENT '经营公司编码',
    pCode varchar(32) DEFAULT NULL COMMENT '属性分类编码',
    dCode varchar(32) NOT NULL COMMENT '属性编码',
    dName varchar(64) NOT NULL COMMENT '属性名称',
    dEnName varchar(100) DEFAULT NULL COMMENT '属性英文名称',
    status smallint(6) DEFAULT NULL COMMENT '状态',
    lang varchar(10) DEFAULT NULL COMMENT '语言类型',
    creator varchar(64) DEFAULT NULL COMMENT '创建人',
    createDate datetime DEFAULT NULL COMMENT '创建日期',
    modifier varchar(64) DEFAULT NULL COMMENT '修改人',
    updateDate datetime DEFAULT NULL COMMENT '修改日期',
    PRIMARY KEY (id),
    UNIQUE KEY idx_packageattdict_pdee (dCode,pCode,erpCode,entId) USING BTREE
) COMMENT='套餐属性字典';"

sh orderCallNo.sh "CREATE TABLE IF NOT EXISTS packageattshopgoodsref (
  id bigint(20) NOT NULL COMMENT 'ID',
  entId bigint(20) NOT NULL COMMENT '零售商ID',
  erpCode varchar(3) NOT NULL COMMENT '经营公司编码',
  pCode varchar(32) DEFAULT NULL COMMENT '属性类别编码',
  cateringTimeCode varchar(50) DEFAULT NULL COMMENT '餐饮时段编码',
  shopCode varchar(20) NOT NULL COMMENT '门店编码',
  stallCode varchar(20) NOT NULL COMMENT '档口编码',
  goodsCode varchar(20) NOT NULL COMMENT '商品编码',
  barNo varchar(64) NOT NULL COMMENT '条码',
  status smallint(6) DEFAULT NULL COMMENT '状态',
  sGoodsCode varchar(20) DEFAULT NULL COMMENT '套餐编码（此字段有值时goodsCode套餐明细的商品编码）',
  lang varchar(10) DEFAULT NULL COMMENT '语言类型',
  creator varchar(64) DEFAULT NULL COMMENT '创建人',
  createDate datetime DEFAULT NULL COMMENT '创建日期',
  modifier varchar(64) DEFAULT NULL COMMENT '修改人',
  updateDate datetime DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (id),
  UNIQUE KEY idx_packageattshopgoodsref_uniquekey (goodsCode,barNo,shopCode,stallCode,erpCode,entId) USING BTREE
) COMMENT='套餐分类与门店档口套餐商品关系表';"

sh orderCallNo.sh "CREATE TABLE IF NOT EXISTS salegoodsproperty (
  id bigint(20) NOT NULL COMMENT '主键ID',
  entId bigint(20) NOT NULL COMMENT '零售商ID',
  erpCode varchar(3) NOT NULL COMMENT '经营公司编码',
  shopCode varchar(20) NOT NULL COMMENT '门店编码',
  stallCode varchar(20) NOT NULL COMMENT '档口编码',
  goodsCode varchar(20) NOT NULL COMMENT '商品编码',
  barNo varchar(64) NOT NULL COMMENT '条码',
  sellout char(1) DEFAULT NULL COMMENT '售罄标识(1:售罄,0:未售罄)',
  lang varchar(10) DEFAULT NULL COMMENT '语言类型',
  creator varchar(64) DEFAULT NULL COMMENT '创建人',
  createDate datetime DEFAULT NULL COMMENT '创建日期',
  modifier varchar(64) DEFAULT NULL COMMENT '修改人',
  updateDate datetime DEFAULT NULL COMMENT '修改日期',
  back01 varchar(20) DEFAULT NULL COMMENT '备用字段01',
  back02 varchar(20) DEFAULT NULL COMMENT '备用字段02',
  back03 varchar(20) DEFAULT NULL COMMENT '备用字段03',
  PRIMARY KEY (id) USING BTREE,
  UNIQUE KEY idx_salegoodsproperty_uniquekey (shopCode,stallCode,goodsCode,barNo) USING BTREE
) COMMENT='可售商品—附属表';"
######################### IDC执行的前置机脚本 End ##########################

####################################################### IDC 的表和触发器（前置机不需要）Start #####################################
CREATE TABLE IF NOT EXISTS `syncfrontaddress` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `entId` bigint(20) NOT NULL COMMENT '零售商ID',
    `erpCode` varchar(3) DEFAULT NULL COMMENT '经营公司编码',
    `shopCode` varchar(32) DEFAULT NULL COMMENT '属性分类编码',
    `ip` varchar(500) NOT NULL COMMENT 'IP地址',
    `status` char(1) DEFAULT '0' COMMENT '状态:0:不启用,1:启用',
    `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
    `createDate` datetime DEFAULT NULL COMMENT '创建日期',
    `modifier` varchar(64) DEFAULT NULL COMMENT '修改人',
    `updateDate` datetime DEFAULT NULL COMMENT '修改日期',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_syncfrontaddress_shopcode` (`shopCode`,`erpCode`,`entId`) USING BTREE
) COMMENT='IDC同步前置配置表';
INSERT INTO `omdmain`.`syncfrontaddress` (`id`, `entId`, `erpCode`, `shopCode`, `ip`, `status`, `creator`, `createDate`, `modifier`, `updateDate`) VALUES (1, 0, '002', '001', 'http://192.4.25.232:8088/', '1', 'yihaitao', '2024-07-04 10:10:10', NULL, NULL);
INSERT INTO `omdmain`.`syncfrontaddress` (`id`, `entId`, `erpCode`, `shopCode`, `ip`, `status`, `creator`, `createDate`, `modifier`, `updateDate`) VALUES (2, 0, '002', '002', 'http://192.4.36.232:8088/', '1', 'yihaitao', '2024-07-04 10:10:10', NULL, NULL);
INSERT INTO `omdmain`.`syncfrontaddress` (`id`, `entId`, `erpCode`, `shopCode`, `ip`, `status`, `creator`, `createDate`, `modifier`, `updateDate`) VALUES (3, 0, '002', '004', 'http://192.4.24.232:8088/', '1', 'yihaitao', '2024-07-04 10:10:10', NULL, NULL);
INSERT INTO `omdmain`.`syncfrontaddress` (`id`, `entId`, `erpCode`, `shopCode`, `ip`, `status`, `creator`, `createDate`, `modifier`, `updateDate`) VALUES (4, 0, '002', '005', 'http://192.4.32.232:8088/', '1', 'yihaitao', '2024-07-04 10:10:10', NULL, NULL);
INSERT INTO `omdmain`.`syncfrontaddress` (`id`, `entId`, `erpCode`, `shopCode`, `ip`, `status`, `creator`, `createDate`, `modifier`, `updateDate`) VALUES (5, 0, '002', '006', 'http://192.4.23.232:8088/', '1', 'yihaitao', '2024-07-04 10:10:10', NULL, NULL);
INSERT INTO `omdmain`.`syncfrontaddress` (`id`, `entId`, `erpCode`, `shopCode`, `ip`, `status`, `creator`, `createDate`, `modifier`, `updateDate`) VALUES (6, 0, '002', '007', 'http://192.4.29.232:8088/', '1', 'yihaitao', '2024-07-04 10:10:10', NULL, NULL);
INSERT INTO `omdmain`.`syncfrontaddress` (`id`, `entId`, `erpCode`, `shopCode`, `ip`, `status`, `creator`, `createDate`, `modifier`, `updateDate`) VALUES (7, 0, '002', '008', 'http://192.4.30.232:8088/', '1', 'yihaitao', '2024-07-04 10:10:10', NULL, NULL);
INSERT INTO `omdmain`.`syncfrontaddress` (`id`, `entId`, `erpCode`, `shopCode`, `ip`, `status`, `creator`, `createDate`, `modifier`, `updateDate`) VALUES (8, 0, '002', '009', 'http://192.4.31.232:8088/', '1', 'yihaitao', '2024-07-04 10:10:10', NULL, NULL);
INSERT INTO `omdmain`.`syncfrontaddress` (`id`, `entId`, `erpCode`, `shopCode`, `ip`, `status`, `creator`, `createDate`, `modifier`, `updateDate`) VALUES (9, 0, '002', '010', 'http://192.4.34.232:8088/', '1', 'yihaitao', '2024-07-04 10:10:10', NULL, NULL);
INSERT INTO `omdmain`.`syncfrontaddress` (`id`, `entId`, `erpCode`, `shopCode`, `ip`, `status`, `creator`, `createDate`, `modifier`, `updateDate`) VALUES (10, 0, '002', '026', 'http://192.4.22.232:8088/', '1', 'yihaitao', '2024-07-04 10:10:10', NULL, NULL);
INSERT INTO `omdmain`.`syncfrontaddress` (`id`, `entId`, `erpCode`, `shopCode`, `ip`, `status`, `creator`, `createDate`, `modifier`, `updateDate`) VALUES (11, 0, '002', '203', 'http://192.4.39.232:8088/', '1', 'yihaitao', '2024-07-04 10:10:10', NULL, NULL);
INSERT INTO `omdmain`.`syncfrontaddress` (`id`, `entId`, `erpCode`, `shopCode`, `ip`, `status`, `creator`, `createDate`, `modifier`, `updateDate`) VALUES (12, 0, '002', '210', 'http://192.4.40.232:8088/', '1', 'yihaitao', '2024-07-04 10:10:10', NULL, NULL);

###### packageattcate 增量表 ######
CREATE TABLE IF NOT EXISTS `packageattcate_change` (
    `trigger_change_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `entId` bigint(20) NOT NULL COMMENT '零售商ID',
    `erpCode` varchar(3) NOT NULL COMMENT '经营公司编码',
    `pCode` varchar(32) NOT NULL COMMENT '属性类别编码',
    `updateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `doFlag` char(1) NOT NULL DEFAULT '0' COMMENT '0:未处理,1:已处理',
    PRIMARY KEY (`trigger_change_id`),
    UNIQUE KEY `idx_packageattcate_change_uniquekey` (`pCode`,`erpCode`,`entId`) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT=0 COMMENT='套餐属性类别_增量表';

###### packageattcate 触发器 ######
create trigger insert_packageattcate
    after insert on packageattcate for each row
begin
    replace into packageattcate_change(entId,erpCode,pCode) values (NEW.entId,NEW.erpCode,NEW.pCode);
end;

create trigger update_packageattcate
    after update on packageattcate for each row
begin
   if(OLD.entId = NEW.entId and OLD.erpCode = NEW.erpCode and OLD.pCode = NEW.pCode ) then
       replace into packageattcate_change(entId,erpCode,pCode) values (NEW.entId,NEW.erpCode,NEW.pCode);
   else
       replace into packageattcate_change(entId,erpCode,pCode) values (OLD.entId,OLD.erpCode,OLD.pCode);
       replace into packageattcate_change(entId,erpCode,pCode) values (NEW.entId,NEW.erpCode,NEW.pCode);
   end if;
end;

create trigger delete_packageattcate
    after delete on packageattcate for each row
begin
    replace into packageattcate_change(entId,erpCode,pCode) values (OLD.entId,OLD.erpCode,OLD.pCode);
end;

###### packageattdict 增量表 ######
CREATE TABLE IF NOT EXISTS `packageattdict_change` (
    `trigger_change_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `entId` bigint(20) NOT NULL COMMENT '零售商ID',
    `erpCode` varchar(3) NOT NULL COMMENT '经营公司编码',
    `pCode` varchar(32) NOT NULL COMMENT '属性分类编码',
    `dCode` varchar(32) NOT NULL COMMENT '属性编码',
    `updateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `doFlag` char(1) NOT NULL DEFAULT '0' COMMENT '0:未处理,1:已处理',
    PRIMARY KEY (`trigger_change_id`),
    UNIQUE KEY `idx_packageattdict_change_uniquekey` (`dCode`,`pCode`,`erpCode`,`entId`) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT=0 COMMENT='套餐属性字典_增量表';

###### packageattdict 触发器 ######
create trigger insert_packageattdict
    after insert on packageattdict for each row
begin
    replace into packageattdict_change(entId,erpCode,pCode,dCode) values (NEW.entId,NEW.erpCode,NEW.pCode,NEW.dCode);
end;

create trigger update_packageattdict
    after update on packageattdict for each row
begin
    if(OLD.entId = NEW.entId and OLD.erpCode = NEW.erpCode and OLD.pCode = NEW.pCode and OLD.dCode = NEW.dCode ) then
       replace into packageattdict_change(entId,erpCode,pCode,dCode) values (NEW.entId,NEW.erpCode,NEW.pCode,NEW.dCode);
    else
       replace into packageattdict_change(entId,erpCode,pCode,dCode) values (OLD.entId,OLD.erpCode,OLD.pCode,OLD.dCode);
       replace into packageattdict_change(entId,erpCode,pCode,dCode) values (NEW.entId,NEW.erpCode,NEW.pCode,NEW.dCode);
end if;
end;

create trigger delete_packageattdict
    after delete on packageattdict for each row
begin
    replace into packageattdict_change(entId,erpCode,pCode,dCode) values (OLD.entId,OLD.erpCode,OLD.pCode,OLD.dCode);
end;

###### packageattshopgoodsref 增量表 ######
CREATE TABLE IF NOT EXISTS `packageattshopgoodsref_change` (
    `trigger_change_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `entId` bigint(20) NOT NULL COMMENT '零售商ID',
    `erpCode` varchar(3) NOT NULL COMMENT '经营公司编码',
    `shopCode` varchar(20) NOT NULL COMMENT '门店编码',
    `stallCode` varchar(20) NOT NULL COMMENT '档口编码',
    `goodsCode` varchar(20) NOT NULL COMMENT '商品编码',
    `barNo` varchar(64) NOT NULL COMMENT '条码',
    `updateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `doFlag` char(1) NOT NULL DEFAULT '0' COMMENT '0:未处理,1:已处理',
    PRIMARY KEY (`trigger_change_id`),
    UNIQUE KEY `idx_packageattshopgoodsref_uniquekey` (`goodsCode`,`barNo`,`shopCode`,`stallCode`,`erpCode`,`entId`) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT=0 COMMENT='套餐分类与门店档口套餐商品关系表_增量表';

###### packageattshopgoodsref 触发器 ######
create trigger insert_packageattshopgoodsref
    after insert on packageattshopgoodsref for each row
begin
    replace into packageattshopgoodsref_change(entId,erpCode,shopCode,stallCode,goodsCode,barNo) values (NEW.entId,NEW.erpCode,NEW.shopCode,NEW.stallCode,NEW.goodsCode,NEW.barNo);
end;

create trigger update_packageattshopgoodsref
    after update on packageattshopgoodsref for each row
begin
    if(OLD.entId = NEW.entId and OLD.erpCode = NEW.erpCode and OLD.shopCode = NEW.shopCode and OLD.stallCode = NEW.stallCode and OLD.goodsCode = NEW.goodsCode and OLD.barNo = NEW.barNo) then
       replace into packageattshopgoodsref_change(entId,erpCode,shopCode,stallCode,goodsCode,barNo) values (NEW.entId,NEW.erpCode,NEW.shopCode,NEW.stallCode,NEW.goodsCode,NEW.barNo);
    else
       replace into packageattshopgoodsref_change(entId,erpCode,shopCode,stallCode,goodsCode,barNo) values (OLD.entId,OLD.erpCode,OLD.shopCode,OLD.stallCode,OLD.goodsCode,OLD.barNo);
       replace into packageattshopgoodsref_change(entId,erpCode,shopCode,stallCode,goodsCode,barNo) values (NEW.entId,NEW.erpCode,NEW.shopCode,NEW.stallCode,NEW.goodsCode,NEW.barNo);
end if;
end;

create trigger delete_packageattshopgoodsref
    after delete on packageattshopgoodsref for each row
begin
    replace into packageattshopgoodsref_change(entId,erpCode,shopCode,stallCode,goodsCode,barNo) values (OLD.entId,OLD.erpCode,OLD.shopCode,OLD.stallCode,OLD.goodsCode,OLD.barNo);
end;

###### salegoodsproperty 增量表 ######
CREATE TABLE IF NOT EXISTS `salegoodsproperty_change` (
    `trigger_change_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `shopCode` varchar(20) NOT NULL COMMENT '门店编码',
    `stallCode` varchar(20) NOT NULL COMMENT '档口编码',
    `goodsCode` varchar(20) NOT NULL COMMENT '商品编码',
    `barNo` varchar(64) NOT NULL COMMENT '条码',
    `updateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `doFlag` char(1) NOT NULL DEFAULT '0' COMMENT '0:未处理,1:已处理',
    PRIMARY KEY (`trigger_change_id`) USING BTREE,
    UNIQUE KEY `idx_salegoodsproperty_change_uniquekey` (`shopCode`,`stallCode`,`goodsCode`,`barNo`) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT=0 COMMENT='可售商品—附属表_增量表';

###### salegoodsproperty 触发器 ######
create trigger insert_salegoodsproperty
    after insert on salegoodsproperty for each row
begin
    replace into salegoodsproperty_change(shopCode,stallCode,goodsCode,barNo) values (NEW.shopCode,NEW.stallCode,NEW.goodsCode,NEW.barNo);
end;

create trigger update_salegoodsproperty
    after update on salegoodsproperty for each row
begin
    if(OLD.shopCode = NEW.shopCode and OLD.stallCode = NEW.stallCode and OLD.goodsCode = NEW.goodsCode and OLD.barNo = NEW.barNo) then
       replace into salegoodsproperty_change(shopCode,stallCode,goodsCode,barNo) values (NEW.shopCode,NEW.stallCode,NEW.goodsCode,NEW.barNo);
    else
       replace into salegoodsproperty_change(shopCode,stallCode,goodsCode,barNo) values (OLD.shopCode,OLD.stallCode,OLD.goodsCode,OLD.barNo);
       replace into salegoodsproperty_change(shopCode,stallCode,goodsCode,barNo) values (NEW.shopCode,NEW.stallCode,NEW.goodsCode,NEW.barNo);
end if;
end;

create trigger delete_salegoodsproperty
    after delete on salegoodsproperty for each row
begin
    replace into salegoodsproperty_change(shopCode,stallCode,goodsCode,barNo) values (OLD.shopCode,OLD.stallCode,OLD.goodsCode,OLD.barNo);
end;

###### cateringtime 餐饮时段表 ######
CREATE TABLE IF NOT EXISTS `cateringtime_change` (
    `trigger_change_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `entId` bigint(20) NOT NULL COMMENT '零售商ID',
    `erpCode` varchar(3) NOT NULL COMMENT '经营公司编码',
    `cateringTimeCode` varchar(50) NOT NULL COMMENT '餐饮时段编码',
    `updateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `doFlag` char(1) NOT NULL DEFAULT '0' COMMENT '0:未处理,1:已处理',
    PRIMARY KEY (`trigger_change_id`),
    UNIQUE KEY `idx_cateringtime_change_uniquekey` (`cateringTimeCode`,`erpCode`,`entId`) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT=0 COMMENT='餐饮时段表_增量表';

###### cateringtime 触发器 ######
create trigger insert_cateringtime
    after insert on cateringtime for each row
begin
    replace into cateringtime_change(entId,erpCode,cateringTimeCode) values (NEW.entId,NEW.erpCode,NEW.cateringTimeCode);
end;

create trigger update_cateringtime
    after update on cateringtime for each row
begin
    if(OLD.entId = NEW.entId and OLD.erpCode = NEW.erpCode and OLD.cateringTimeCode = NEW.cateringTimeCode ) then
       replace into cateringtime_change(entId,erpCode,cateringTimeCode) values (NEW.entId,NEW.erpCode,NEW.cateringTimeCode);
    else
       replace into cateringtime_change(entId,erpCode,cateringTimeCode) values (OLD.entId,OLD.erpCode,OLD.cateringTimeCode);
       replace into cateringtime_change(entId,erpCode,cateringTimeCode) values (NEW.entId,NEW.erpCode,NEW.cateringTimeCode);
end if;
end;

create trigger delete_cateringtime
    after delete on cateringtime for each row
begin
    replace into cateringtime_change(entId,erpCode,cateringTimeCode) values (OLD.entId,OLD.erpCode,OLD.cateringTimeCode);
end;
####################################################### IDC 的表和触发器（前置机不需要）End #####################################

##### 更新脚本
ALTER TABLE `omdmain`.`salegoods` ADD INDEX `idx_salegoods_shopstallcode` (`shopCode`,`stallCode`);
update salegoods set salePrice = 0,lang = '20240624',updateDate = now() where goodsType = 20 and left(goodsCode,1) = 9 and siid is not null;
update salegoods t1 join setmealdetail t2 on t1.ssgid = t2.goodsId  set t1.parentGoodsCode = t2.sgoodsCode,t1.lang = '20240627',t1.updateDate = now() where t1.goodsType = 20;
INSERT INTO `omdmain`.`packageattcate` (`id`, `entId`, `erpCode`, `pCode`, `pName`, `pEnName`, `parentCode`, `status`, `level`, `leafFlag`, `lang`, `creator`, `createDate`, `modifier`, `updateDate`) VALUES (1716883179076000080, 0, '002', 'all', '属性分类', 'category', '0', 1, 1, 0, 'CN', 'yihaitao', '2024-06-06 12:28:00', 'yihaitao', '2024-06-20 12:06:27');

##### 增加档口打印机地址长度(IDC 和前置机)
ALTER TABLE omdmain.stallinfo MODIFY COLUMN printAddress varchar(256);
update stallinfo set printName = 2 where pattern = 2;
update stallinfo set printName = 1 where pattern != 2;

##### 增加档口打印机地址长度(前置机) 登陆机器192.3.28.80
sh /root/yihaitao/orderCallNo.sh "ALTER TABLE omdmain.stallinfo MODIFY COLUMN printAddress varchar(256)"

##### (前置机)检查SQL是否更新成功
sh /root/yihaitao/orderCallNo.sh "SELECT COLUMN_NAME, CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE TABLE_NAME = 'stallinfo' AND COLUMN_NAME = 'printAddress' AND TABLE_SCHEMA = 'omdmain'"

ALTER TABLE `omdmain`.`packageattcate` ADD COLUMN `requiredCount` int NOT NULL DEFAULT '0' COMMENT '必选数量' AFTER `pName`;

####################################################### 20241212 套餐 #######################################################
ALTER TABLE `omdmain`.`packageattdict` ADD COLUMN `orderNum` int NOT NULL DEFAULT '0' COMMENT '排序号' AFTER `status`;

ALTER TABLE `omdmain`.`packageattshopgoodsref`
    ADD COLUMN `selfNotShow` char(1) NULL DEFAULT 1 COMMENT '自助不显示 0:否(显示),1:是(不显示)' AFTER `cateringTimeCode`;