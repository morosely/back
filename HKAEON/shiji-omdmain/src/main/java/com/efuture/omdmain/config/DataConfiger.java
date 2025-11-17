package com.efuture.omdmain.config;

import com.efuture.omdmain.component.OrderCallNoServiceHisImpl;
import com.efuture.omdmain.component.*;
import com.efuture.omdmain.component.out.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.efuture.omdmain.common.sync.SyncSaleGoodsService;
import com.efuture.omdmain.component.hwg.Hwg_FulfillmentShopServiceImpl;
import com.efuture.omdmain.component.receipt.BrandInfoDeamonServiceImpl;
import com.efuture.omdmain.utils.QueryAdvanceField;
import com.product.config.BaseConfiger;
import com.product.storage.template.FMybatisTemplate;

@Configuration
@AutoConfigureAfter(Omdmain_BeanConfiger.class)
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
public class DataConfiger extends BaseConfiger {
	@Autowired
	@Qualifier("omdmain_StorageOperation")
	FMybatisTemplate mybatisTemplate;

	/**
	 * 自定义Bean 在外部使用如果调用：
	 * 请求地址：http://127.0.0.1:8115/rest?ent_id=0&method=testJdbc.onTesta 请求参数：{}
	 * 
	 * 说明:test是Bean名称，onTesta是请求方法 参数说明： onTesta支持两种参数模式: 第一种：public String
	 * onTesta(ServiceSession session,JSONObject paramsObject) {...} 第二种：public
	 * String onTesta(ServiceSession session,String paramsObject) {...}
	 * 
	 * @return
	 */
//    @Bean(name = "myuserservice")
//    public MyUserServiceImpl onMyUserServiceImpl(){
//    	String collectionName = "myuser";
//    	String keyfieldName   = "seqno";
//    	MyUserServiceImpl service = new MyUserServiceImpl(mybatisTemplate,collectionName,keyfieldName);
//        return service;
//    }

	//add by yihaitao 2024-09-02 套餐时段
	@Bean(name = "omdmain.cateringtime")
	public CateringTimeService onCateringTimeService(){
		return new CateringTimeService(mybatisTemplate, "cateringtime", "id");
	}

	//add by yihaitao 2024-07-11 同步前置架构升级
	@Bean(name = "omdmain.syncfront")
	public SyncFrontService onSyncFrontService(){
		return new SyncFrontService(mybatisTemplate, "", "");
	}

	//salegoods附属属性（售罄标识等）
	@Bean(name = "omdmain.salegoodsproperty")
	public SaleGoodsPropertyService onSaleGoodsPropertyService(){
		return new SaleGoodsPropertyService(mybatisTemplate, "salegoodsproperty", "id");
	}

	//套餐分类与门店档口套餐商品关系表
	@Bean(name = "omdmain.packageattshopgoodsref")
	public PackageAttShopGoodsRefService onPackageAttShopGoodsRefService(){
		return new PackageAttShopGoodsRefService(mybatisTemplate, "packageattshopgoodsref", "id");
	}

	//套餐分类与套餐商品关系表
//	@Bean(name = "omdmain.packageattgoodsref")
//	public PackageAttGoodsRefService onPackageAttGoodsRefService(){
//		return new PackageAttGoodsRefService(mybatisTemplate, "packageattgoodsref", "id");
//	}

	//套餐属性字典
	@Bean(name = "omdmain.packageattdict")
	public PackageAttDictService onPackageAttDict(){
		return new PackageAttDictService(mybatisTemplate, "packageattdict", "id");
	}

	//套餐属性类别
	@Bean(name = "omdmain.packageattcate")
	public PackageAttCateService onPackageAttCateService(){
		return new PackageAttCateService(mybatisTemplate, "packageattcate", "id");
	}

	//订单叫号历史
	@Bean(name = "omdmain.ordercallnohis")
	public OrderCallNoServiceHisImpl onOrderCallNoServiceHisImpl() {
		return new OrderCallNoServiceHisImpl(mybatisTemplate, "ordercallnohis", "id");
	}

	//订单叫号
	@Bean(name = "omdmain.ordercallno")
	public OrderCallNoServiceImpl onOrderCallNoServiceImpl() {
		return new OrderCallNoServiceImpl(mybatisTemplate, "ordercallno", "id");
	}
	//EXT经营配置
	@Bean(name = "omdmain.extgoodshopref")
	public ExtGoodsShopRefServiceImpl onExtGoodsShopRefServiceImpl() {
		return new ExtGoodsShopRefServiceImpl(mybatisTemplate, "extgoodsshopref", "egsrid");
	}

	//EXT多条码
	@Bean(name = "omdmain.extaeonmorebarno")
	public ExtAeonMoreBarNoServiceImpl onExtAeonMoreBarNoServiceImpl() {
		return new ExtAeonMoreBarNoServiceImpl(mybatisTemplate, "extaeonmorebarno", "mbid");
	}

	//多条码
	@Bean(name = "omdmain.aeonmorebarno")
	public AeonMoreBarNoServiceImpl onAeonMoreBarNoService() {
		return new AeonMoreBarNoServiceImpl(mybatisTemplate, "aeonmorebarno", "mbid");
	}

	//商品API说明
	@Bean(name = "omdmain.aeoncitysalegoods")
	public AeonCitySaleGoodsService onAeonCitySaleGoodsService() {
		return new AeonCitySaleGoodsService(mybatisTemplate, "saleGoods", "ssgid");
	}


	// 档口商品销售表
	@Bean(name = "omdmain.stallsalegoods")
	public StallSaleGoodsServiceImpl onStallSaleGoodsServiceImpl() {
		return new StallSaleGoodsServiceImpl(mybatisTemplate, "saleGoods", "ssgid");
	}

	// 上传下载日志fileupdownlogs
	@Bean(name = "omdmain.fileupdownlogs")
	public FileUpDownLogsServiceImpl onFileUpDownLogs() {
		return new FileUpDownLogsServiceImpl(mybatisTemplate, "fileupdownlogs", "fid");
	}

	// 最低折扣表
	@Bean(name = "omdmain.mindiscount")
	public MinDiscountServiceImpl onMinDiscountServiceImpl() {
		return new MinDiscountServiceImpl(mybatisTemplate, "mindiscount", "minDisId");
	}

	// PartyTray商品属性表
	@Bean(name = "omdmain.partytraygoodsproperties")
	public PartyTrayGoodsPropertiesServiceImpl onPartyTrayGoodsPropertiesServiceImpl() {
		return new PartyTrayGoodsPropertiesServiceImpl(mybatisTemplate, "partytraygoodsproperties", "ptgpid");
	}

	// 游戏白对照表
	@Bean(name = "omdmain.aeonjdtoken")
	public AeonJDTokenServiceImpl onAeonJDTokenServiceImpl() {
		return new AeonJDTokenServiceImpl(mybatisTemplate, "aeon_jdtoken", "itemCode1");
	}

	// 特殊商品类型表：用于主数据同步
	@Bean(name = "omdmain.goodsspecial")
	public GoodsSpecialServiceImpl onGoodsSpecialServiceImpl() {
		return new GoodsSpecialServiceImpl(mybatisTemplate, "goodsspecial", "gsid");
	}

	// 接口数据解析日志
	@Bean(name = "omdmain.translog")
	public AeonTransLogServiceImpl onAeonTransLogServiceImpl() {
		return new AeonTransLogServiceImpl(mybatisTemplate, "aeon_trans_log", "id");
	}

	// 修改saleGoods
	@Bean(name = "omdmain.updateSalegoods")
	public UpdateSaleGoodsServiceImpl onUpdateSaleGoodsServiceImpl() {
		return new UpdateSaleGoodsServiceImpl(mybatisTemplate, "saleGoods", "ssgid");
	}

	// 商品基础表
	@Bean(name = "omdmain.goods")
	public GoodsServiceImpl onGoodsServiceImpl() {
		return new GoodsServiceImpl(mybatisTemplate, "goods", "sgid");
	}

	// 用于高级字段动态查询
	@Bean(name = "omdmain.advance")
	public QueryAdvanceField onQueryAdvanceField() {
		return new QueryAdvanceField(mybatisTemplate, "", "");
	}

	// 商品销售表
	@Bean(name = "omdmain.salegoods")
	public SaleGoodsServiceImpl onSaleGoodsServiceImpl() {
		return new SaleGoodsServiceImpl(mybatisTemplate, "saleGoods", "ssgid");
	}

	// 供应商商品
	@Bean(name = "omdmain.goodsVenderRef")
	public GoodsVenderRefServiceImpl onGoodsVenderRefServiceImpl() {
		return new GoodsVenderRefServiceImpl(mybatisTemplate, "GoodsVenderRef", "gvrid");
	}

	// 商品描述
	@Bean(name = "omdmain.goodsDesc")
	public GoodsDescServiceImpl onGoodsDescServiceImpl() {
		return new GoodsDescServiceImpl(mybatisTemplate, "goodsdesc", "gdid");
	}

	// 字典
	@Bean(name = "omdmain.dictionary")
	public DictionaryServiceImpl onDictionaryServiceImpl() {
		return new DictionaryServiceImpl(mybatisTemplate, "dictionary", "dictId");
	}

	// 字典数据
	@Bean(name = "omdmain.dictionarydata")
	public DictionaryDataServiceImpl onDictionaryDataServiceImpl() {
		return new DictionaryDataServiceImpl(mybatisTemplate, "dictionarydata", "dictDataId");
	}

	// 渠道定义
	@Bean(name = "omdmain.channelinfo")
	public ChannelInfoServiceImpl onChannelInfoServiceImpl() {
		return new ChannelInfoServiceImpl(mybatisTemplate, "channelInfo", "channelId");
	}

	// 行政区域设置
	@Bean(name = "omdmain.regionInfo")
	public RegionInfoServiceImpl onRegionInfoServiceImpl() {
		return new RegionInfoServiceImpl(mybatisTemplate, "regionInfo", "regionId");
	}

	// 支付方式设置
	@Bean(name = "omdmain.paymentMethod")
	public PaymentMethodServiceImpl onPaymentMethodServiceImpl() {
		return new PaymentMethodServiceImpl(mybatisTemplate, "paymentMethod", "pmid");
	}

	// 展示分类
	@Bean(name = "omdmain.show.category")
	public ShowCategoryServiceImpl showCategoryServiceImpl() {
		return new ShowCategoryServiceImpl(mybatisTemplate, "showcategory", "showCategoryId");
	}

	// 工业分类
	@Bean(name = "omdmain.category")
	public CategoryServiceImpl categoryServiceImpl() {
		return new CategoryServiceImpl(mybatisTemplate, "category", "categoryId");
	}

	// 工业分类(redis)
	@Bean(name = "omdmain.category.redis")
	public CategoryRedisServiceImpl onCategoryRedisServiceRedisImpl() {
		return new CategoryRedisServiceImpl("category", "categoryId");
	}

	// 品牌
	@Bean(name = "omdmain.brandinfo")
	public BrandsServiceImpl brandsServiceImpl() {
		return new BrandsServiceImpl(mybatisTemplate, "brandInfo", "brandId");
	}

	// 品牌接收
	@Bean(name = "deamon.omd.brandinfo")
	public BrandInfoDeamonServiceImpl brandInfoDeamonServiceImpl() {
		return new BrandInfoDeamonServiceImpl(mybatisTemplate, "brandInfo", "brandId");
	}

	// 商品项
	@Bean(name = "omdmain.goods.items")
	public SaleGoodsItemsServiceImpl saleGoodsItemsServiceImpl() {
		return new SaleGoodsItemsServiceImpl(mybatisTemplate, "saleGoodsItems", "sgiid");
	}

	// 虚拟母品
	@Bean(name = "omdmain.virtual")
	public VirtualSaleGoodsServiceImpl onVirtualSaleGoodsServiceImpl() {
		return new VirtualSaleGoodsServiceImpl(mybatisTemplate, "saleGoods", "ssgid");
	}

	// 组包码
	@Bean(name = "omdmain.packing")
	public PackingSaleGoodsServiceImpl onPackingSaleGoodsServiceImpl() {
		return new PackingSaleGoodsServiceImpl(mybatisTemplate, "saleGoods", "ssgid");
	}

	// 经营公司
	@Bean(name = "omdmain.businesscompany")
	public BusinessCompanyServiceImpl onBusinessCompanyServiceImpl() {
		return new BusinessCompanyServiceImpl(mybatisTemplate, "businesscompany", "bcid");
	}

	// 支付方式
	@Bean(name = "omdmain.paymentmethodref")
	public PaymentMethodRefServiceImpl onPaymentMethodRefServiceImpl() {
		return new PaymentMethodRefServiceImpl(mybatisTemplate, "paymentmethodref", "pmrid");
	}

	// ERP支付方式
	@Bean(name = "omdmain.erppaymentmethod")
	public ERPPaymentMethodServiceImpl onERPPaymentMethodServiceImpl() {
		return new ERPPaymentMethodServiceImpl(mybatisTemplate, "erppaymentmethod", "epmid");
	}

	// 字典分类
	@Bean(name = "omdmain.dictionarytype")
	public DictionaryTypeServiceImpl onDictionaryTypeServiceImpl() {
		return new DictionaryTypeServiceImpl(mybatisTemplate, "dictionarytype", "dtid");
	}

	// 品类属性模板
	@Bean(name = "omdmain.categoryproperty")
	public CategoryPropertyServiceImpl onCategoryPropertyServiceImpl() {
		return new CategoryPropertyServiceImpl(mybatisTemplate, "categoryproperty", "cpmid");
	}

	// 商品扩展属性表
	@Bean(name = "omdmain.goodspropertyext")
	public GoodsPropertyExtServiceImpl onGoodsPropertyExtServiceImpl() {
		return new GoodsPropertyExtServiceImpl(mybatisTemplate, "goodspropertyextvalue", "gpevid");
	}

	// 经营配置表
	@Bean(name = "omdmain.goodsshopref")
	public GoodsShopRefServiceImpl onGoodsShopRefServiceImpl() {
		return new GoodsShopRefServiceImpl(mybatisTemplate, "goodsShopRef", "gsrid");
	}

	// 冷藏商品设置
	@Bean(name = "omdmain.coldtrans")
	public ColdTransSaleGoodsServiceImpl onColdTransSaleGoodsServiceImpl() {
		return new ColdTransSaleGoodsServiceImpl(mybatisTemplate, "goods", "sgid");
	}

	// 档口加工方法定义
	@Bean(name = "omdmain.goodsprocess")
	public GoodsProcessServiceImpl onGoodsProcessServiceImpl() {
		return new GoodsProcessServiceImpl(mybatisTemplate, "goodsprocess", "ssgid");
	}

	// 档口信息
	@Bean(name = "omdmain.stallinfo")
	public StallInfoServiceImpl onStallInfoServiceImpl() {
		return new StallInfoServiceImpl(mybatisTemplate, "stallinfo", "siid");
	}

	// 档口经营商品
	@Bean(name = "omdmain.stallgoodsref")
	public StallGoodsRefServiceImpl onStallGoodsRefServiceImpl() {
		return new StallGoodsRefServiceImpl(mybatisTemplate, "saleGoods", "ssgid");
	}

	// 门店渠道关联
	@Bean(name = "omdmain.shopChannelRef")
	public ShopChannelRefServiceImpl onShopChannelRefServiceImpl() {
		return new ShopChannelRefServiceImpl(mybatisTemplate, "shopChannelRef", "ocrid");
	}

	// 渠道商品上下架
	@Bean(name = "omdmain.goodsUpAndDown")
	public GoodsUpAndDownServiceImpl onGoodsUpAndDownServiceImpl() {
		return new GoodsUpAndDownServiceImpl(mybatisTemplate, "goodsUpAndDown", "guadid");
	}

	// 门店信息定义
	@Bean(name = "omdmain.shop")
	public ShopServiceImpl onShopServiceImpl() {
		return new ShopServiceImpl(mybatisTemplate, "shop", "shopId");
	}

	// 门店信息定义(reidis)
	@Bean(name = "omdmain.shop.redis")
	public ShopServiceRedisImpl onShopServiceRedisImpl() {
		return new ShopServiceRedisImpl("shop", "shopId");
	}

	// 柜组信息
	@Bean(name = "omdmain.saleorg")
	public SaleOrgServiceImpl onSaleOrgServiceImpl() {
		return new SaleOrgServiceImpl(mybatisTemplate, "saleorg", "saleOrgId");
	}

	// 柜组信息(redis)
	@Bean(name = "omdmain.saleorg.redis")
	public SaleOrgRedisServiceImpl onSaleOrgServiceRedisImpl() {
		return new SaleOrgRedisServiceImpl("saleOrg", "saleOrgId");
	}

	// 商品图片
	@Bean(name = "omdmain.goodsImage")
	public SaleGoodsImageRefServiceImpl onSaleGoodsImageRefServiceImpl() {
		return new SaleGoodsImageRefServiceImpl(mybatisTemplate, "saleGoodsImageRef", "sgirid");
	}

	// 商品2
	@Bean(name = "omdmain.saleGoods2")
	public SaleGoodsService2Impl onSaleGoodsService2Impl() {
		return new SaleGoodsService2Impl(mybatisTemplate, "saleGoods", "ssgid");
	}

	@Bean(name = "omdmain.categorylabelref")
	public CategoryLabelRefServiceImpl onCategoryLabelRefServiceImpl() {
		return new CategoryLabelRefServiceImpl(mybatisTemplate, "categorylabelref", "clrid");
	}

	@Bean(name = "omdmain.categorylabel")
	public CategoryLabelServiceImpl onCategoryLabelServiceImpl() {
		return new CategoryLabelServiceImpl(mybatisTemplate, "categorylabel", "clid");
	}

	@Bean(name = "omdmain.processRecipe")
	public ProcessRecipeServiceImpl onProcessRecipeServiceImpl() {
		return new ProcessRecipeServiceImpl(mybatisTemplate, "processRecipe", "prid");
	}

	@Bean(name = "omdmain.processRecipeDetail")
	public ProcessRecipeDetailServiceImpl onProcessRecipeDetailServiceImpl() {
		return new ProcessRecipeDetailServiceImpl(mybatisTemplate, "processRecipeDetail", "prdid");
	}

	// 分解配方单
	@Bean(name = "omdmain.decomposeRecipe")
	public DecomposeRecipeServiceImpl onDecomposeRecipeServiceImpl() {
		return new DecomposeRecipeServiceImpl(mybatisTemplate, "decomposeRecipe", "drid");
	}

	// 分解配方单明细
	@Bean(name = "omdmain.decomposeRecipeDetail")
	public DecomposeRecipeDetailServiceImpl onDecomposeRecipeDetailServiceImpl() {
		return new DecomposeRecipeDetailServiceImpl(mybatisTemplate, "decomposeRecipeDetail", "drdid");
	}

	// 供应商信息
	@Bean(name = "omdmain.vender")
	public VenderServiceImpl onVenderServiceImpl() {
		return new VenderServiceImpl(mybatisTemplate, "vender", "vid");
	}

	// ext中供应商
	@Bean(name = "omdmain.extSupplierInformation")
	public ExtSupplierInformationServiceImpl onSupplierInformationServiceImpl() {
		return new ExtSupplierInformationServiceImpl(mybatisTemplate, "extVender", "evid");
	}

	// ext中供应商商品
	@Bean(name = "omdmain.extSupplierGoods")
	public ExtSupplierGoodsServiceImpl onSupplierGoodsServiceImpl() {
		return new ExtSupplierGoodsServiceImpl(mybatisTemplate, "extGoodsVenderRef", "egvrid");
	}

	// ext中品牌
	@Bean(name = "omdmain.extBrandInfo")
	public ExtBrandInfoServiceImpl onExtBrandInfoServiceImpl() {
		return new ExtBrandInfoServiceImpl(mybatisTemplate, "extBrandInfo", "ebrandId");
	}

	// ext中品类
	@Bean(name = "omdmain.extCategory")
	public ExtCategoryServiceImpl onExtCategoryServiceImpl() {
		return new ExtCategoryServiceImpl(mybatisTemplate, "extCategory", "ecategoryId");
	}

	// ext中商品
	@Bean(name = "omdmain.extGoods")
	public ExtGoodsServiceImpl onExtGoodsServiceImpl() {
		return new ExtGoodsServiceImpl(mybatisTemplate, "extGoods", "egid");
	}

	// 线上商品展示分类设置
	@Bean(name = "omdmain.shopChannelCategoryGoodsRef")
	public ShopChannelCategoryGoodsRefServiceImpl onShopChannelCategoryGoodsRefServiceImpl() {
		return new ShopChannelCategoryGoodsRefServiceImpl(mybatisTemplate, "shopChannelCategoryGoodsRef", "sccgrid");
	}

	// 商品销售规格
	@Bean(name = "omdmain.goodsSpec")
	public GoodsSpecServiceImpl onGoodsSpecServiceImpl() {
		return new GoodsSpecServiceImpl(mybatisTemplate, "goodsSpec", "gsid");
	}

	@Bean(name = "omdmain.goodsMoreBarCode")
	public GoodsMoreBarCodeServiceImpl onGoodsMoreBarCodeServiceImpl() {
		return new GoodsMoreBarCodeServiceImpl(mybatisTemplate, "goodsMoreBarCode", "gsid");
	}

	@Bean(name = "omdmain.goodsSpecPrice")
	public GoodsSpecPriceServiceImpl onGoodsSpecPriceServiceImpl() {
		return new GoodsSpecPriceServiceImpl(mybatisTemplate, "goodsSpecPrice", "gspid");
	}

	// 对外接口 - 查询经营公司
	@Bean(name = "deamon.omd.business")
	public BusinessCompanyDeamonServiceImpl onBusinessCompanyDeamonServiceImpl() {
		return new BusinessCompanyDeamonServiceImpl(mybatisTemplate, "businessCompany", "bcid");
	}

	// 对外接口 - 查询品牌
	@Bean(name = "deamon.omd.brand")
	public BrandInfoDeamonOutServiceImpl onBrandInfoDeamonOutServiceImpl() {
		return new BrandInfoDeamonOutServiceImpl(mybatisTemplate, "brandInfo", "brandId");
	}

	// 对外接口 - 查询品类
	@Bean(name = "deamon.omd.category")
	public CategoryDeamonOutServiceImpl onCategoryDeamonOutServiceImpl() {
		return new CategoryDeamonOutServiceImpl(mybatisTemplate, "category", "categoryId");
	}

	// 对外接口 - 供应商查询
	@Bean(name = "deamon.omd.vendor")
	public VenderDeamonOutServiceImpl onVenderDeamonOutServiceImpl() {
		return new VenderDeamonOutServiceImpl(mybatisTemplate, "vender", "vid");
	}

	// 对外接口 - 经营配置查询
	@Bean(name = "deamon.omd.jypz")
	public GoodsShopRefDeamonOutServiceImpl onGoodsShopRefDeamonOutServiceImpl() {
		return new GoodsShopRefDeamonOutServiceImpl(mybatisTemplate, "saleGoods", "ssgid");
	}

	// 对外接口 - 组织机构查询
	@Bean(name = "deamon.omd.shop")
	public ShopDeamonOutServiceImpl onShopDeamonOutServiceImpl() {
		return new ShopDeamonOutServiceImpl(mybatisTemplate, "shop", "shopId");
	}

	// 对外接口 - 店内组织查询
	@Bean(name = "deamon.omd.org")
	public SaleOrgDeamonOutServiceImpl onSaleOrgDeamonOutServiceImpl() {
		return new SaleOrgDeamonOutServiceImpl(mybatisTemplate, "saleOrg", "saleOrgId");
	}

	// 对外接口 - 商品查询
	@Bean(name = "deamon.omd.goods")
	public SaleGoodsDeamonOutServiceImpl onSaleGoodsDeamonOutServiceImpl() {
		return new SaleGoodsDeamonOutServiceImpl(mybatisTemplate, "goods", "sgid");
	}

	// 对外接口 - 商品查询 先解决不能注入的问题
	@Bean(name = "deamon.omd.goodsNew")
	public GoodsDeamonOutServiceImpl onGoodsDeamonOutServiceImpl() {
		return new GoodsDeamonOutServiceImpl(mybatisTemplate, "goods", "sgid");
	}

	// 对外接口 - qudao 查询
	@Bean(name = "deamon.omd.channel")
	public ChannelInfoDeamonOutServiceImpl onChannelInfoDeamonOutServiceImpl() {
		return new ChannelInfoDeamonOutServiceImpl(mybatisTemplate, "channelInfo", "channelId");
	}

	// 对外接口 - 支付方式查询(旧方式)
	@Bean(name = "deamon.omd.old.pay")
	public PaymentDeamonOutServiceImpl onPaymentDeamonOutServiceImpl() {
		return new PaymentDeamonOutServiceImpl(mybatisTemplate, "paymentMethodRef", "pmid");
	}
	//修改提供营销的支付方式查询（新方式） add by yihaitao 2022-11-22
	@Bean(name = "deamon.omd.pay")
	public PaymentMethodOutServiceImpl onPaymentMethodOutServiceImpl() {
		return new PaymentMethodOutServiceImpl(mybatisTemplate, "paymentmethod", "pmid");
	}

	// 对外接口-电子秤码管理
	@Bean(name = "deamon.omd.possalegoods")
	public PosSaleGoodsDeamonOutServiceImpl onPosSaleGoodsDeamonOutServiceImpl() {
		return new PosSaleGoodsDeamonOutServiceImpl(mybatisTemplate, "salegoods", "ssgid");
	}

	// 商品表+经营配置表同步可售商品表
	@Bean(name = "omdmain.syncsalegoods")
	public SyncSaleGoodsService onSyncSaleGoodsService() {
		return new SyncSaleGoodsService(mybatisTemplate, "salegoods", "ssgid");
	}

	// 错误日志
	@Bean(name = "omdmain.errorlog")
	public ErrorLogService onErrorLogService() {
		return new ErrorLogService(mybatisTemplate, "errorlog", "errorId");
	}

	// 同步时间记录表
	@Bean(name = "omdmain.syncdatatime")
	public SyncDataTimeService onSyncDataTimeService() {
		return new SyncDataTimeService(mybatisTemplate, "syncdatatime", "syncId");
	}

	// 管理类别设置
	@Bean(name = "omdmain.categoryManageLevel")
	public CategoryManageLevelServiceImpl onCategoryManageLevelServiceImpl() {
		return new CategoryManageLevelServiceImpl(mybatisTemplate, "categoryManageLevel", "cml");
	}

	// 门店商品资料修改
	@Bean(name = "omdmain.shopGoodsInfoUpdate")
	public ShopGoodsInfoUpdateServiceImpl onShopGoodsInfoUpdateServiceImpl() {
		return new ShopGoodsInfoUpdateServiceImpl(mybatisTemplate, "shopGoodsInfoUpdate", "shopGoodsInfoId");
	}

	// 导出
	@Bean(name = "omdmain.export")
	public ExportExcelServiceImpl onMDMGoodsServiceImpl() {
		return new ExportExcelServiceImpl(mybatisTemplate, "", "");
	}

	// 海外购履约门店设置
	@Bean(name = "omdmain.hwg_fulfillmentshop")
	public Hwg_FulfillmentShopServiceImpl onHwg_FulfillmentShopServiceImpl() {
		return new Hwg_FulfillmentShopServiceImpl(mybatisTemplate, "hwg_fulfillmentshop", "hfsid");
	}

}
