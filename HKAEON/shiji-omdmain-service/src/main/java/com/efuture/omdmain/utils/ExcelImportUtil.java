package com.efuture.omdmain.utils;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public abstract class ExcelImportUtil extends ExcelImportCommonUtil{

	public ExcelImportUtil(){
	}
	
	public ExcelImportUtil(ServiceSession session) {
		super(session);
	}

	public ServiceResponse onImportData(ServiceSession session, String params, MultipartFile file)throws Exception{
		//0.初始化数据
		JSONObject jsonParams = JSONObject.parseObject(params);
		ServiceResponse response = this.initBeanName(jsonParams);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			return response;
		}
		this.initTableName();
		response = this.initCols(jsonParams);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			return response;
		}
		
		this.initExcelParseParams(jsonParams);
		this.initNotNullFields(jsonParams);
		this.initAppendValue(jsonParams);
		this.initRegex();
		
		//1.从Excel中获取数据
		this.excelData = this.getDataFromExcel(jsonParams, file);
		if(excelData.isEmpty()){
			return ServiceResponse.buildSuccess("导入成功.");
		}
		
		//2.校验
		if(!this.isValidateOK(excelData))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.NOT_EXIST_MATCHED, this.getErrMsg2Str());
		
		//3.用户自定义校验
		response = this.selfDefinedValidate();
		if(response != null && !ResponseCode.SUCCESS.equals(response.getReturncode())){
			return response;
		}
		
		//4.添加前段传递过来的附加值
		this.addAppendValue(excelData, jsonParams);
		
		//5.添加需要数据转化的值,比如Excel表格里面只有goodsCode、barCode,程序需要解析goodsCode、barCode
		//  得到goodsName、goodsDisplayName这些信息，并与Excel表格数据一起添加到数据库
		response = this.addTransferData(session, excelData, jsonParams);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, this.getErrMsg2Str());
		}
		
		//6.持久化操作
		response = this.persitent(jsonParams);
		if(response != null){
			return response;
		}
		
		return persistent2DB(excelData);
	}
	
	/**
	 * 处理Excel表格中需要转化的数据，并将转化之后的数据添加到excelData中去
	 * 
	 */
	public abstract ServiceResponse addTransferData(ServiceSession session, JSONObject excelData, JSONObject jsonParams);
	
	/**
	 * 用户自定义数据持久化操作;
	 * 假如不进行自定义数据持久化操作，则在方法体中返回null;
	 * */
	public abstract ServiceResponse persitent(JSONObject excelData);
	
	
	/**
	 * @Title: 			selfDefinedValidate
	 * @Description: 	用户自定义校验
	 * 					如果检验失败，则returncode != 0;校验成功，则returncode == 0;
	 * @param: 			@return   
	 * @return: 		ServiceResponse   
	 * @throws
	 */
	public abstract ServiceResponse selfDefinedValidate();
}
