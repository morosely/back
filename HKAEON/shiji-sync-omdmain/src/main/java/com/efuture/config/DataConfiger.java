package com.efuture.config;

import com.efuture.component.*;
import com.efuture.component.event.component.ExtGoodsShopRefComponent;
import com.product.storage.template.FMybatisTemplate;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration 
@EnableAutoConfiguration
public class DataConfiger extends BaseConfig {
    @Autowired
    @Qualifier("sqlSessionTemplate")
    SqlSessionTemplate omdmainSqlSessionTemplate;

    @Autowired
    @Qualifier("stockSqlSessionTemplate")
    SqlSessionTemplate stockSqlSessionTemplate;

    @Autowired
    @Qualifier("crmadapterSqlSessionTemplate")
    SqlSessionTemplate crmadapterSqlSessionTemplate;

    @Bean("sync.extamcprofile")
    public ExtAmcProfileServiceImpl getExtAmcProfileService() {
        return new ExtAmcProfileServiceImpl("ExtAmcProfileModelMapper", "extamcprofile", "id",crmadapterSqlSessionTemplate);
    }

    @Bean("sync.amcprofile")
    public AmcProfileServiceImpl getAmcProfileService() {
        return new AmcProfileServiceImpl("AmcProfileModelMapper", "amcprofile", "id",crmadapterSqlSessionTemplate);
    }
    
    // 临时ext中（ERP）经营配置
    @Bean(name = "sync.ExtGoodsShopRef")
    public ExtGoodsShopRefComponent onExtGoodsShopRefComponent() {
    		return new ExtGoodsShopRefComponent(omdmainSqlSessionTemplate);
    }
    
    // 临时ext中（ERP）经营配置
    @Bean(name = "sync.tmpextgoodsshopref")
    public TmpExtGoodsShopRefServiceImpl onTmpExtGoodsShopRefServiceImpl() {
    	return new TmpExtGoodsShopRefServiceImpl("TmpExtGoodsShopRefModelMapper", "tmpextgoodsshopref", "egsrid", omdmainSqlSessionTemplate);
    }
    
    //ext中品牌
    @Bean(name = "sync.extbrandinfo")
    public ExtBrandInfoServiceImpl onExtBrandInfoServiceImpl() {
        return new ExtBrandInfoServiceImpl("ExtBrandInfoModelMapper","extbrandinfo", "ebrandId", omdmainSqlSessionTemplate);
    }
    
    //ext中品类
    @Bean(name = "sync.extcategory")
    public ExtCategoryServiceImpl onExtCategoryServiceImpl() {
        return new ExtCategoryServiceImpl("ExtCategoryModelMapper", "extcategory", "ecategoryId", omdmainSqlSessionTemplate);
    }
    
    // ext中（ERP）支付方式
    @Bean(name = "sync.exterppaymentmethod")
    public ExtERPPaymentMethodServiceImpl onExtERPPaymentMethodServiceImpl() {
    	return new ExtERPPaymentMethodServiceImpl("ExtErpPaymentMethodModelMapper", "exterppaymentmethod", "eepmid", omdmainSqlSessionTemplate);
    }
    
    //ext中商品
    @Bean(name = "sync.extgoods")
    public ExtGoodsServiceImpl onExtGoodsServiceImpl() {
        return new ExtGoodsServiceImpl("ExtGoodsModelMapper", "extgoods", "egid", omdmainSqlSessionTemplate);
    }
    
    // ext中（ERP）经营配置
    @Bean(name = "sync.extgoodsshopref")
    public ExtGoodsShopRefServiceImpl onExtGoodsShopRefServiceImpl() {
    	return new ExtGoodsShopRefServiceImpl("ExtGoodsShopRefModelMapper", "extgoodsshopref", "egsrid", omdmainSqlSessionTemplate);
    }
    
    // ext中（ERP）商品订货/配送规格
    @Bean(name = "sync.extgoodsspec")
    public ExtGoodsSpecServiceImpl onExtGoodsSpecServiceImpl() {
    	return new ExtGoodsSpecServiceImpl("ExtGoodsSpecModelMapper", "extgoodsspec", "egsid");
    }
    
    // ext中（ERP）供应商商品
    @Bean(name = "sync.extgoodsvenderref")
    public ExtGoodsVenderRefServiceImpl onExtGoodsVenderRefServiceImpl() {
    	return new ExtGoodsVenderRefServiceImpl("ExtGoodsVenderRefModelMapper", "extgoodsvenderref", "egvrid", omdmainSqlSessionTemplate);
    }
    
    // ext中（ERP）调价单
    @Bean(name = "sync.extpricechangebill")
    public ExtPriceChangeBillServiceImpl onExtPriceChangeBillServiceImpl() {
    	return new ExtPriceChangeBillServiceImpl("ExtPriceChangeBillModelMapper", "extpricechangebill", "epcbid", omdmainSqlSessionTemplate);
    }
    
    // ext中（ERP）调价单明细
    @Bean(name = "sync.extpricechangebilldetail")
    public ExtPriceChangeBillDetailServiceImpl onExtPriceChangeBillDetailServiceImpl() {
    	return new ExtPriceChangeBillDetailServiceImpl("ExtPriceChangeBillDetailModelMapper", "extpricechangebilldetail", "epcbdid", omdmainSqlSessionTemplate);
    }
    
    // ext中（ERP）柜组信息
    @Bean(name = "sync.extsaleorg")
    public ExtSaleOrgServiceImpl onExtSaleOrgServiceImpl() {
    	return new ExtSaleOrgServiceImpl("ExtSaleOrgModelMapper", "extsaleorg", "saleOrgId", omdmainSqlSessionTemplate);
    }
    
    // ext中（ERP）门店信息
    @Bean(name = "sync.extshop")
    public ExtShopServiceImpl onExtShopServiceImpl() {
    	return new ExtShopServiceImpl("ExtShopModelMapper","extshop", "eshopId", omdmainSqlSessionTemplate);
    }
    
    //ext（ERP）供应商
    @Bean(name = "sync.extvender")
    public ExtVenderServiceImpl onExtVenderServiceImpl() {
    	return new ExtVenderServiceImpl("ExtVenderModelMapper", "extvender", "evid", omdmainSqlSessionTemplate);
    }
    
    //（ERP）多条码价格
    @Bean(name = "sync.extgoodsmorebarcode")
    public ExtGoodsMoreBarcodeServiceImpl extGoodsMoreBarcodeService() {
        return new ExtGoodsMoreBarcodeServiceImpl("ExtGoodsMoreBarcodeModelMapper", "extgoodsmorebarcode", "egsid", omdmainSqlSessionTemplate);
    }
    
    //（ERP）多条码价格
    @Bean(name = "sync.extgoodsspecprice")
    public ExtGoodsSpecPriceServiceImpl extGoodsSpecPriceService() {
    	return new ExtGoodsSpecPriceServiceImpl("ExtGoodsSpecPriceModelMapper", "extgoodsspecprice", "egspid", omdmainSqlSessionTemplate);
    }
    
    //（ERP）商品描述
    @Bean(name = "sync.extgoodsdesc")
    public ExtGoodsDescServiceImpl extGoodsDescServiceImpl() {
    	return new ExtGoodsDescServiceImpl("ExtGoodsDescModelMapper", "extgoodsdesc", "gdid");
    }

    @Bean(name = "sync.extaeonmorebarno")
    public ExtAeonMoreBarNoServiceImpl extAeonMoreBarNoService() {
        return new ExtAeonMoreBarNoServiceImpl("ExtAeonMoreBarNoModelMapper", "extaeonmorebarno", "mbid", omdmainSqlSessionTemplate);
    }

    // (ERP)组包码商品
    @Bean(name = "sync.extsalegoodsitems")
    public ExtSaleGoodsItemsServiceImpl extSaleGoodsItemsService() {
        return new ExtSaleGoodsItemsServiceImpl("ExtSaleGoodsItemsModelMapper", "extsalegoodsitems", "sgiid", omdmainSqlSessionTemplate);
    }
    
    //商品描述
    @Bean(name = "sync.goodsdesc")
    public GoodsSpecServiceImpl goodsSpecServiceImpl() {
    	return new GoodsSpecServiceImpl("GoodsDescModelMapper", "goodsdesc", "gdid");
    }
    
    //门店
    @Bean(name = "sync.shop")
    public ShopServiceImpl onShopServiceImpl() {
        return new ShopServiceImpl("ShopModelMapper", "shop", "shopId", omdmainSqlSessionTemplate);
    }

    //品类
    @Bean(name = "sync.category")
    public CategoryServiceImpl onCategoryServiceImpl() {
        return new CategoryServiceImpl("CategoryModelMapper", "category", "categoryId", omdmainSqlSessionTemplate);
    }

    //支付方式
    @Bean(name = "sync.erppaymentmethod")
    public ERPPaymentMethodServiceImpl onERPPaymentMethodServiceImpl() {
        return new ERPPaymentMethodServiceImpl("ERPPaymentMethodModelMapper", "erppaymentmethod", "epmid", omdmainSqlSessionTemplate);
    }

    //品牌
    @Bean(name = "sync.brandinfo")
    public BrandInfoServiceImpl onBrandInfoServiceImpl() {
        return new BrandInfoServiceImpl("BrandInfoModelMapper","brandinfo", "brandId", omdmainSqlSessionTemplate);
    }

    //供应商
    @Bean(name = "sync.vender")
    public VenderServiceImpl onVenderServiceImpl() {
        return new VenderServiceImpl("VenderModelMapper", "vender", "vid", omdmainSqlSessionTemplate);
    }

    //柜组
    @Bean(name = "sync.saleorg")
    public SaleOrgServiceImpl onSaleOrgServiceImpl() {
        return new SaleOrgServiceImpl("SaleOrgModelMapper", "saleorg", "saleOrgId", omdmainSqlSessionTemplate);
    }

    //商品
    @Bean(name = "sync.goods")
    public GoodsServiceImpl onGoodsServiceImpl() {
        return new GoodsServiceImpl( "GoodsModelMapper", "goods", "sgid", omdmainSqlSessionTemplate);
    }

    //经营配置
    @Bean(name = "sync.goodsshopref")
    public GoodsShopRefServiceImpl onGoodsShopRefServiceImpl() {
        return new GoodsShopRefServiceImpl("GoodsShopRefModelMapper", "goodsshopref", "gsrid", omdmainSqlSessionTemplate);
    }
 
    //商品订货/配送规格
    @Bean(name = "sync.goodsspec")
    public GoodsSpecServiceImpl onGoodsSpecServiceImpl() {
        return new GoodsSpecServiceImpl("GoodsSpecModelMapper", "goodsspec", "gsid");
    }

    //供应商商品
    @Bean(name = "sync.goodsvenderref")
    public GoodsVenderRefServiceImpl onGoodsVenderRefServiceImpl() {
        return new GoodsVenderRefServiceImpl("GoodsVenderRefModelMapper", "goodsvenderref", "gvrid", omdmainSqlSessionTemplate);
    }

    //调价单明细
    @Bean(name = "sync.pricechangebilldetail")
    public PriceChangeBillDetailServiceImpl onPriceChangeBillDetailServiceImpl() {
        return new PriceChangeBillDetailServiceImpl("PriceChangeBillDetailModelMapper", "pricechangebilldetail", "pcbdid", omdmainSqlSessionTemplate);
    }

    //调价单
    @Bean(name = "sync.pricechangebill")
    public PriceChangeBillServiceImpl onPriceChangeBillServiceImpl() {
        return new PriceChangeBillServiceImpl("PriceChangeBillModelMapper", "pricechangebill", "pcbid", omdmainSqlSessionTemplate);
    }

    //多条码
    @Bean(name = "sync.goodsmorebarcode")
    public GoodsMoreBarcodeServiceImpl goodsMoreBarcodeService() {
        return new GoodsMoreBarcodeServiceImpl("GoodsMoreBarcodeModelMapper", "goodsmorebarcode", "gsid", omdmainSqlSessionTemplate);
    }
    
    //多条码价格
    @Bean(name = "sync.goodsspecprice")
    public GoodsSpecPriceServiceImpl goodsSpecPriceService() {
    	return new GoodsSpecPriceServiceImpl("GoodsSpecPriceModelMapper", "goodsspecprice", "gspid", omdmainSqlSessionTemplate);
    }

    // (ERP)组包码商品
    @Bean(name = "sync.salegoodsitems")
    public SaleGoodsItemsServiceImpl saleGoodsItemsService() {
        return new SaleGoodsItemsServiceImpl("SaleGoodsItemsModelMapper", "salegoodsitems", "sgiid", omdmainSqlSessionTemplate);
    }
    
    @Bean(name = "sync.errorlog")
    public ErrorLogServiceImpl errorLogServiceImpl() {
    	return new ErrorLogServiceImpl();
    }


    @Bean(name = "sync.extshelfgoodsref")
    public ExtShelfGoodsRefService extShelfGoodsRefService() {
        return new ExtShelfGoodsRefService("ExtShelfGoodsRefModelMapper", "extshelfgoodsref", "sgridt", stockSqlSessionTemplate);
    }

    @Bean(name = "sync.aeonmorebarno")
    public AeonMoreBarNoServiceImpl aeonMoreBarNoService() {
        return new AeonMoreBarNoServiceImpl("AeonMoreBarNoModelMapper", "aeonmorebarno", "mbid", omdmainSqlSessionTemplate);
    }

}
