package com.efuture.controller;

import com.alibaba.fastjson.JSON;
import com.efuture.executor.jobhandler.*;
import com.product.controller.ProductController;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DataController extends ProductController {

	@Autowired
	private ConvertHandler convertHandler;
	@Autowired
	private StockHandler stockHandler;
	@Autowired
	private GoodsStockHandler goodsHandler;
	@Autowired
	private GoodsCategoryStockHandler goodsCategoryStockHandler;
	@Autowired
	private DeleteSaleGoodsHandler deleteSaleGoodsHandler;
	@Autowired
	DeleteGoodsHandler deleteGoodsHandler;

    @RequestMapping("/")
    public String onRoot(HttpServletRequest request){
    	return request.getRequestURI() + " success";
    }
	
    @RequestMapping("/rest")
    public String onRest(@RequestParam(value="method",required=false) String method, 
			    		 @RequestParam(value="session",required=false) String session, 
			    		 @RequestParam(value="ent_id",required=false) String ent_id, 
			    		 @RequestParam(value="user_id",required=false) String user_id, 
			    		 @RequestParam(value="user_name",required=false) String user_name,
			    		 @RequestParam(value="locale",required=false) String locale,
			    		 @RequestBody String param) {
    	return this.onRestService(method, session, ent_id, user_id, user_name, locale, param);
    }
	
	/**
	 * HTTP: HTTP://127.0.0.1:8115/DataAdapter/ServiceSuccess/rest?ent_id=1&method=a.x
	 * POST: {"a":"test"}
	 * 【OK】
	 * {"returncode":"0","data":{"ent_id":"1","method":"a.x","params":"{}"}}
	 */
	@RequestMapping(value={"/DataAdapter/ServiceSuccess/rest"},method=RequestMethod.POST)
	public ServiceResponse onServiceSuccess(@RequestParam(value="ent_id"     ,required=false) long   entID,
			                                @RequestParam(value="method"     ,required=false) String method,
							                @RequestBody String params) {
		Map<String,String> resposneMap = new HashMap<String,String>();
		resposneMap.put("ent_id", Long.toString(entID));
		resposneMap.put("method", method);
		resposneMap.put("params", params);
		ServiceResponse response = ServiceResponse.buildSuccess(resposneMap);
    	System.out.println("onServiceSuccess:"+JSON.toJSONString(response));
		return response;
	}

	/**
	 * HTTP: HTTP://127.0.0.1:8115/DataAdapter/ServiceMessage/rest?ent_id=1&method=a.x
	 * POST: {"a":"test"}
	 * 【OK】
	 * {"returncode":"50000","data":"Error:{\"ent_id\":\"1\",\"method\":\"a.x\",\"params\":\"{}\"}"}
	 */
	@RequestMapping(value={"/DataAdapter/ServiceMessage/rest"},method=RequestMethod.POST)
	public ServiceResponse onServiceMessage(@RequestParam(value="ent_id"     ,required=false) long   entID,
			                                @RequestParam(value="method"     ,required=false) String method,
							                @RequestBody String params) {
		ServiceSession session = new ServiceSession();
		session.setEnt_id(entID);

		Map<String,String> resposneMap = new HashMap<String,String>();
		resposneMap.put("ent_id", Long.toString(entID));
		resposneMap.put("method", method);
		resposneMap.put("params", params);
		ServiceResponse response = ServiceResponse.buildFailure(session, ResponseCode.EXCEPTION, "Error:{0}", JSON.toJSONString(resposneMap));
    	System.out.println("onServiceMessage:"+JSON.toJSONString(response));
		return response;
	}

	/**
	 * HTTP: HTTP://127.0.0.1:8115/DataAdapter/ServiceFaiure/rest?ent_id=1&method=a.x
	 * POST: {"a":"test"}
	 * 【OK】
	 * {"method":"a.x","params":"{}","ent_id":"1"} 429 -> TOO_MANY_REQUESTS
	 */
	@RequestMapping(value={"/DataAdapter/ServiceFaiure/rest"},method=RequestMethod.POST)
	public ResponseEntity<Object> onServiceFaiure(@RequestParam(value="ent_id"     ,required=false) long   entID,
			                                      @RequestParam(value="method"     ,required=false) String method,
							                      @RequestBody String params) {
		ServiceSession session = new ServiceSession();
		session.setEnt_id(entID);

		Map<String,String> resposneMap = new HashMap<String,String>();
		resposneMap.put("ent_id", Long.toString(entID));
		resposneMap.put("method", method);
		resposneMap.put("params", params);
		
		HttpHeaders responseHeaders = new HttpHeaders();  
    	responseHeaders.setContentType(MediaType.TEXT_PLAIN);  		
    	ResponseEntity<Object> response = new ResponseEntity<Object>(JSON.toJSON(resposneMap),responseHeaders, HttpStatus.TOO_MANY_REQUESTS);
    	System.out.println("onServiceFaiure:"+JSON.toJSONString(response));
    	return response;
	}

	@RequestMapping(value = "/aeonmorebarno", method = RequestMethod.GET)
	public void aeonmorebarno() {
		String json = "{\"sourceTable\": \"aeonmorebarno\",\"extTable\":\"extaeonmorebarno\",\"pageSize\":100}";
		try {
			convertHandler.execute(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/goodsCategoryStockHandler", method = RequestMethod.GET)
	public void goodsCategoryStockHandler() {
		try {
			goodsCategoryStockHandler.execute("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/brandinfo", method = RequestMethod.GET)
	public void brandinfo() {
		String json = "{\"sourceTable\": \"brandinfo\",\"extTable\":\"extbrandinfo\",\"pageSize\":3000 }";
		try {
			convertHandler.execute(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/goodsdesc", method = RequestMethod.GET)
	public void goodsDesc() {
		String json = "{\"sourceTable\": \"goodsdesc\",\"extTable\":\"extgoodsdesc\",\"pageSize\":3000 }";
		try {
			convertHandler.execute(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/goodsmorebarcode", method = RequestMethod.GET)
	public void goodsMoreBarcode() {
		String json = "{\"sourceTable\": \"goodsmorebarcode\",\"extTable\":\"extgoodsmorebarcode\",\"pageSize\":3000 }";
		try {
			convertHandler.execute(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/vender", method = RequestMethod.GET)
	public void vender() {
		String json = "{\"sourceTable\": \"vender\",\"extTable\":\"extvender\",\"pageSize\":3000 }";
		try {
			convertHandler.execute(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/shop", method = RequestMethod.GET)
	public void shop() {
		String json = "{\"sourceTable\": \"shop\",\"extTable\":\"extshop\",\"pageSize\":3000 }";
		try {
			convertHandler.execute(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/goodsshopref", method = RequestMethod.GET)
	public void goodsshopref() {
		String json = "{\"sourceTable\": \"goodsshopref\",\"extTable\":\"extgoodsshopref\",\"pageSize\":3000 }";
		try {
			convertHandler.execute(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/erppaymentmethod", method = RequestMethod.GET)
	public void erpPaymentMethod() {
		String json = "{\"sourceTable\": \"erppaymentmethod\",\"extTable\":\"exterppaymentmethod\",\"pageSize\":3000 }";
		try {
			convertHandler.execute(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/category", method = RequestMethod.GET)
	public void category() {
		String json = "{\"sourceTable\": \"category\",\"extTable\":\"extcategory\",\"pageSize\":3000}";
		try {
			convertHandler.execute(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/goods", method = RequestMethod.GET)
	public void goods() {
		String json = "{\"sourceTable\": \"goods\",\"extTable\":\"extgoods\",\"pageSize\":10}";
		try {
			convertHandler.execute(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/categoryStock", method = RequestMethod.GET)
	public void categoryStock() {
		try {
			stockHandler.execute("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/goodsStock", method = RequestMethod.GET)
	public void goodsStock() {
		try {
			goodsHandler.execute("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/amcprofile", method = RequestMethod.GET)
	public void amcprofile() {
		String json = "{\"sourceTable\": \"amcprofile\",\"extTable\":\"extamcprofile\",\"pageSize\":3000 }";
		try {
			convertHandler.execute(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/deleteSaleGoods", method = RequestMethod.GET)
	public void deleteSaleGoods() {
		String json = "";
		try {
			deleteSaleGoodsHandler.execute(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//设置停止删除操作控制标志位
	//服务器执行脚本 curl -X GET  'http://127.0.0.1:8214/resetStopFlag/true'
	@RequestMapping(value = "/resetStopFlag/{stopFlag}", method = RequestMethod.GET)
	public void resetStopFlag(@PathVariable Boolean stopFlag) {
		try {
			deleteSaleGoodsHandler.setStopFlag(stopFlag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/deleteGoods", method = RequestMethod.GET)
	public void deleteGoods() {
		String json = "{'totalCount':100,'pageSize':3,'sleepTime':5,'stopFlag':true}";
		try {
			deleteGoodsHandler.execute(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Autowired
	private MaxGoodsSalePriceHandler maxGoodsSalePriceHandler;
	@RequestMapping(value = "/maxGoodsSalePriceHandler", method = RequestMethod.GET)
	public void maxGoodsSalePriceHandler() {
		try {
			maxGoodsSalePriceHandler.execute(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Autowired
	private ShellHandler shellHandler;
	@RequestMapping(value = "/shell", method = RequestMethod.GET)
	public void shell(@RequestParam("param") String param) throws Exception {
		shellHandler.execute(param);
	}

	@Autowired
	DeleteOrderCallNoHandler deleteOrderCallNoHandler;
	@RequestMapping(value = "/deleteOrderCallNo", method = RequestMethod.GET)
	public void deleteOrderCallNo() throws Exception {
		deleteOrderCallNoHandler.execute(null);
	}
}
