package com.efuture.omdmain.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.util.FileProcessorUtils;
import com.product.util.UniqueID;

@Component
public class ExcelImportCommonUtil {

	private String[] tableName = new String[]{};
	
	private String[] beanName = new String[]{};
	
	private String[][] notNullFields = new String[][]{};
	
	private String[][] regex = new String[][]{};
	
	private String[][] cols = new String[][]{};   //明细表的col的第一个值必须是标识主数据的column
	
	private String[][] colsName = new String[][]{};
	
	private java.util.List<java.util.Map<String, Object>> appendValueList = new ArrayList<>();  
	
	private java.util.List<String> errMsg = new ArrayList<>();
	
	private JSONArray appendValue = new JSONArray();
	
	private ServiceSession session = null;
	
	private static String EXCEL_RELATION_SIGN = "excel_relation_sign";
	
	private int startRow = 1;
	
	private int endRow = 999;
	
	protected JSONObject excelData = new JSONObject();
	
	private final static String EXCEL_DATA_NAME = "excelData";
	
	@Autowired
	protected SpringContextUtil applicationContext; 
	
	public ExcelImportCommonUtil(){
	}
	
	public ExcelImportCommonUtil(ServiceSession session) {
		this.session = session;
	}

	public ServiceResponse onImportData4NotPersistented(ServiceSession session, String params, MultipartFile file) throws Exception{
		JSONObject jsonParams = JSONObject.parseObject(params);
		
		//0.初始化设置.
		ServiceResponse response = this.initCols(jsonParams);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			return response;
		}
		
		response = this.initTableName();
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			return response;
		}
		
		//1.从Excel中获取数据
		this.excelData = this.getDataFromExcel(jsonParams, file);
		
		response = this.addTransferData(session, excelData, jsonParams);
		if(response != null && !ResponseCode.SUCCESS.equals(response.getReturncode())){
			response.setData(this.getErrMsg2Str());
			return response;
		}
		
		return ServiceResponse.buildSuccess(this.excelData);
	}
	
	/**
	 * 处理Excel表格中需要转化的数据，并将转化之后的数据添加到excelData中去
	 * 有转化Excel表格数据需求的，请在子类中重写该方法;
	 */
	public ServiceResponse addTransferData(ServiceSession session, JSONObject excelData, JSONObject jsonParams){
		return null;
	}
	
	public ServiceResponse onImportData(ServiceSession session, String params, MultipartFile file)throws Exception{
		//0.初始化数据
		JSONObject jsonParams = JSONObject.parseObject(params);
		
		ServiceResponse response = this.initBeanName(jsonParams);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			return response;
		}

		response = this.initTableName();
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			return response;
		}
		
		response = this.initCols(jsonParams);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			return response;
		}
		
		this.initColsName(jsonParams);
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
		
		//4.添加前段传递过来的附加值
		this.addAppendValue(excelData, jsonParams);
		
		//6.持久化操作
		return persistent2DB(excelData);
	}
	
	protected JSONObject getDataFromExcel(JSONObject jsonParams, MultipartFile file) throws Exception {
		String fileType = jsonParams.getString("type");
		JSONObject result = new JSONObject();
		if ("excel".equals(fileType)) {
			if (file == null) {
				throw new RuntimeException("上传文件不能为空.");
			}

			for (int index = 0; index < tableName.length; index++) {
				jsonParams.put("sheetIndex", index);
				String colsStr = "";
				for(int j=0; j<cols[index].length; j++) {
					colsStr += cols[index][j] + ",";
				}
				if(colsStr.length() > 0) colsStr = (String) colsStr.substring(0, colsStr.length() - 1);
				jsonParams.put("cols", colsStr);
				result.put(tableName[index], FileProcessorUtils.parseFile(jsonParams, file.getInputStream()));
			}
		}

		return result;
	}

	protected boolean isValidateOK(JSONObject excelData){
		boolean isOK = true;
		//1.按照是否允许为空校验数据
		if(this.notNullFields != null && this.notNullFields.length > 0){
			for(int i=0; i<tableName.length; i++){
				JSONArray tmpSheetData = excelData.getJSONArray(tableName[i]);
				String[] tmpSheetCols = this.cols[i];
				String[] notNullFields4Sheet = notNullFields[i];
				String[] tmpSheetColsName = this.colsName[i];
				
				for(int j=0; j<tmpSheetData.size(); j++){
					JSONObject tmpRowData = tmpSheetData.getJSONObject(j);
					
					for(int k=0; k<notNullFields4Sheet.length; k++){
						if(!tmpRowData.containsKey(notNullFields4Sheet[k]) || StringUtils.isEmpty(tmpRowData.getString(notNullFields4Sheet[k]))){
							isOK = false;
							this.errMsg.add("表格序号：" + (i+1) + ",第" + (j+1) + "行,列名称为 " + tmpSheetColsName[k] + "不允许为空.");
						}
					}
				}
			}
		}
		
	    //2.按照正则表达式检验数据的正确性
		//  Excel中的所有单元格的数据均是按照字符串格式进行校验
		if(regex == null || regex.length <= 0) return isOK;
		if(tableName.length != regex.length)
			throw new RuntimeException("数据校验正则表达式的数组长度必须与中Excel中sheet的数量一致.");
		
		for(int i=0; i<tableName.length; i++){
			JSONArray tmpSheetData = excelData.getJSONArray(tableName[i]);
			String[] tmpSheetCols = this.cols[i];
			String[] tmpSheetColsName = this.colsName[i];
			String[] tmpRegex = this.regex[i];
			if(tmpSheetCols.length != tmpRegex.length){
				throw new RuntimeException("表格" + i + 1 + "数据校验正则表达式的长度必须与该表格的列数量一致.");
			}
			
			for(int j=0; j<tmpSheetData.size(); j++){
				JSONObject tmpRowData = tmpSheetData.getJSONObject(j);
				
				for(int k=0; k<tmpSheetCols.length; k++){
					String cellValue = tmpRowData.getString(tmpSheetCols[k]);
					if(cellValue == null) cellValue = "-";
					if(StringUtils.isEmpty(tmpRegex[k])) continue;
					
					if(!this.regexValidate(tmpRegex[k], cellValue)){
						isOK = true;
						this.errMsg.add("表格序号：" + i+1 + ",第" + j+1 + "行,列名称为 " + tmpSheetColsName[k] + "数据格式不正确.");
					}
				}
			}
		}
		
		return isOK;
	}

	/**
	 * Excel sheet 里面每一列对应的Column
	 * {
	 * 	   ...
	 *     cols:{
	 * 	     'processRecipe':{
	 *     
	 *       },
	 *       'processRecipeDetail':{
	 *     	
	 *       }
	 *     },
	 *     ...
	 * }
	 */
	public String[][] setCols(String[][] cols){
		this.cols = cols;
		return this.cols;
	}
	
	public String[][] setColsName(String[][] colsName){
		this.colsName = colsName;
		return this.colsName;
	}
	
	/**
	 * 设置数据校验规则，正则表达式
	 * */
	public void setRegex(String[][] regex){
		this.regex = regex;
	}
	
	public void setBeanName(String[] beanName){
		this.beanName = beanName;
	}
	
	public void setNotNullFields(String[][] notNullFields){
		this.notNullFields = notNullFields;
	}
	
	public void setSession(ServiceSession session){
		this.session = session;
	}
	
	public List<String> getErrMsg(){
		return this.errMsg;
	}
	
	protected ServiceResponse initTableName(){
		if(beanName == null || beanName.length <= 0){
			tableName = new String[]{EXCEL_DATA_NAME};
			return ServiceResponse.buildSuccess("设置tableName成功.");
		}
		
		tableName = new String[beanName.length];
		for(int i=0; i<beanName.length; i++){
			JDBCCompomentServiceImpl component = (JDBCCompomentServiceImpl)this.applicationContext.getBean(beanName[i]);
			tableName[i] = component.getCollectionName();
		}
		
		return ServiceResponse.buildSuccess("设置tableName成功.");
	}
	
	/**
	 * @Title: addAppendValue
	 * @Description: TODO
	 * @param:
	 * @param excelData
	 * @param jsonParams 
	 * @return: void 
	 * @throws
	 */
	protected void addAppendValue(JSONObject excelData, JSONObject jsonParams) {
		if(appendValue == null || appendValue.size() <= 0) return;
		for (int i = 0; i < tableName.length; i++) {
			JSONArray array = excelData.getJSONArray(tableName[i]);
			JSONObject appendValue4SingleSheet = appendValue.getJSONObject(i);
			if(appendValue4SingleSheet.isEmpty()) continue;
			
			java.util.Set<String> appendKeySet = appendValue4SingleSheet.keySet();
			// Excel sheet 数据
			for (int j = 0; j < array.size(); j++) {
				JSONObject tmp = array.getJSONObject(j); // Excel 行数据
				Iterator<String> it = appendKeySet.iterator();
				while (it.hasNext()) {
					String key = it.next();
					Object value = appendValue4SingleSheet.get(key);
					tmp.put(key, value);
				}
			}
			
			excelData.put(tableName[i], array);
		}
	}

	protected void initExcelParseParams(JSONObject paramsObject){
		if(paramsObject == null) return;
		
		if(paramsObject.containsKey("startRow") && StringUtils.isNotEmpty(paramsObject.getString("startRow")))
			startRow = Integer.parseInt(paramsObject.getString("startRow"));
		if(paramsObject.containsKey("endRow") && StringUtils.isNotEmpty(paramsObject.getString("endRow")))
			endRow = Integer.parseInt(paramsObject.getString("endRow"));
	}
	
	protected ServiceResponse initCols(JSONObject paramsObject) {
		if(this.cols == null || this.cols.length <= 0){
			if(paramsObject.containsKey("cols") && StringUtils.isNotEmpty(paramsObject.getString("cols"))){
				if (String.class.isInstance(paramsObject.get("cols")) || Integer.class.isInstance(paramsObject.get("cols"))) {
					cols = new String[1][];
					cols[0] = paramsObject.getString("cols").split(",");
				}else{
					JSONObject colsObj = paramsObject.getJSONObject("cols");
					cols = new String[tableName.length][];
					for(int i=0; i<tableName.length; i++){
						JSONArray array = colsObj.getJSONArray(tableName[i]);
						if(array == null || array.size() <= 0){
							String errMsg = "Excel中sheet序号为"+ (i+1) + " 的表格没有设置cols";
							return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, errMsg);
						}
						cols[i] = new String[array.size()];
						for(int j=0; j<array.size(); j++){
							cols[i][j] = array.get(j).toString();
						}
					}
				}
			}
		}
		
		return ServiceResponse.buildSuccess("设置cols成功.");
	}
	
	/**
	 * 指定表格单元不能为空的列名称
	 * {
	 * 	 ...
	 *   'notNullFields':{
	 *   	'processRecipe':[
	 *   	  'goodsCode','categoryCode'
	 *      ],
	 *      'processDetail':[
	 *        'purPriceAmount', 'goodsSpec', 'weigth'
	 *      ]
	 *   }
	 *   ...
	 * }
	 */
	protected String[][] initNotNullFields(JSONObject paramsObject) {
		if(this.notNullFields == null || this.notNullFields.length <= 0){
			if(paramsObject.containsKey("notNullFields") && StringUtils.isNotEmpty(paramsObject.getString("notNullFields"))){
				if (String.class.isInstance(paramsObject.get("notNullFields")) || Integer.class.isInstance(paramsObject.get("notNullFields"))) {
					notNullFields = new String[1][];
					notNullFields[0] = paramsObject.getString("notNullFields").split(",");
				}else{
					notNullFields = new String[tableName.length][];
					JSONObject fieldsObj = paramsObject.getJSONObject("notNullFields");
					for(int i=0; i<tableName.length; i++){
						JSONArray array = fieldsObj.getJSONArray(tableName[i]);
						if(array == null || array.size() <= 0) continue;
						notNullFields[i] = new String[array.size()];
						for(int j=0; j<array.size(); j++){
							notNullFields[i][j] = array.getString(j);
						}
					}
				}
			}
		}
		return notNullFields;
	}

	protected String[][] initRegex(){
		if(regex == null)
			regex = new String[0][];
		return regex;
	}
	
	/**
	 * 指定需要插入数据的数据库表名称 数组中的库表名称的顺序跟Excel表中的Sheet的顺序保持一致
	 * {
	 * 	  ....
	 *    "tableName":"processRecipe,processRecipeDetail",
	 *    ....
	 * }
	 */
	protected ServiceResponse initBeanName(JSONObject paramsObject) {
		if(beanName == null || beanName.length <= 0){
			if(paramsObject.containsKey("beanName") && StringUtils.isNotEmpty(paramsObject.getString("beanName"))){
				beanName = paramsObject.getString("beanName").split(",");
			}else{
				return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "没有设置beanName");
			}
		}	
		return ServiceResponse.buildSuccess("设置beanName成功.");
	}
	
	private String[][] initColsName(JSONObject paramsObject){
		if(this.colsName == null || colsName.length <= 0){
			if(paramsObject.containsKey("colsName") && StringUtils.isNotEmpty(paramsObject.getString("colsName"))){
				if (String.class.isInstance(paramsObject.get("colsName")) || Integer.class.isInstance(paramsObject.get("colsName"))) {
					colsName = new String[1][];
					colsName[0] = paramsObject.getString("colsName").split(",");
				}else{
					JSONObject colsNameObj = paramsObject.getJSONObject("colsName");
					for(int i=0; i<tableName.length; i++){
						JSONArray array = colsNameObj.getJSONArray(tableName[i]);
						for(int j=0; j<array.size(); j++){
							colsName[i][j] = array.getString(j);
						}
					}
				}
			}
		}
		
		return this.colsName;
	}
	
	/**
	 * {
	 * 	 .....
	 *   colsName:{
	 *    'processRecipe':['商品编码','商品名称'],
	 *    'processRecipeDetail':['重量','配方比例']
	 *   }
	 * }
	 * 
	 * */
	protected JSONArray initAppendValue(JSONObject jsonParams){
		if(JSONArray.class.isInstance(jsonParams.get("appendValue"))){
			this.appendValue = jsonParams.getJSONArray("appendValue");
		}else{
			JSONObject tmp = jsonParams.getJSONObject("appendValue");
			this.appendValue = new JSONArray();
			this.appendValue.add(tmp);
		}
		return this.appendValue;
	}
	
	protected boolean regexValidate(String regex, String value){
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(value);
		return match.matches();
	}
	
	protected String getErrMsg2Str(){
		String result = "";
		if(CollectionUtils.isEmpty(this.errMsg)){
			return result;
		}
		
		for(int i=0; i<errMsg.size(); i++){
			result += errMsg.get(i) + "\r\n";
		}
		
		return result;
	}
	
	@Transactional
	protected ServiceResponse persistent2DB(JSONObject excelData){
		JSONArray entityArray = excelData.getJSONArray(tableName[0]);
		for(int i = 0; i < entityArray.size(); i++){
			Long primaryKeyValue = UniqueID.getUniqueID();
			JSONObject entityObj = entityArray.getJSONObject(i);
			JDBCCompomentServiceImpl entityService = (JDBCCompomentServiceImpl) applicationContext.getBean(beanName[0]);
			entityObj.put(entityService.getKeyfieldName(), primaryKeyValue);
			
			ServiceResponse response = entityService.onInsert(session, entityObj);
			if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
				return response;
			}
			
			for(int j=1; j<tableName.length; j++){
				String signColumn = cols[j][cols[j].length-1];
				JSONArray detail = this.getDetailDataBySign(excelData, j, i + startRow,
						signColumn, entityService.getKeyfieldName(), primaryKeyValue);
				JDBCCompomentServiceImpl detailService = (JDBCCompomentServiceImpl) applicationContext.getBean(beanName[j]);
				JSONObject tmp = new JSONObject();
				tmp.put(detailService.getCollectionName(), detail);
				response = detailService.onInsert(session, tmp);
				if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
					return response;
				}
			}
		}
		
		return ServiceResponse.buildSuccess("导入成功.");
	}
	
	private JSONArray getDetailDataBySign(JSONObject excelData, int tableIndex, int entityRowNo,
			String signColumn, String entityKeyName, Long entityKey){
		JSONArray result = new JSONArray();
		JSONArray sheetData = excelData.getJSONArray(tableName[tableIndex]);
		if(sheetData == null || sheetData.isEmpty()){
			return result;
		}
		
		String relationSign = "-";
		if(!EXCEL_RELATION_SIGN.equals(signColumn)){
			JSONArray entityArray = excelData.getJSONArray(tableName[0]);
			JSONObject entity = (JSONObject) entityArray.get(entityRowNo - startRow);
			relationSign = entity.getString(cols[tableIndex][(int) Long.parseLong(signColumn)]);   					//主表对应明细表格的具体值
		}
		
		for(int i=0; i<sheetData.size(); i++){
			JSONObject tmp = sheetData.getJSONObject(i);
			if(!EXCEL_RELATION_SIGN.equals(signColumn) && tmp.getString(signColumn.toString()) == null){
				throw new RuntimeException("sheet 序号：" + (tableIndex + 1)+ "数据缺少关系标识.");
			}
			if((!EXCEL_RELATION_SIGN.equals(signColumn) && tmp.getString(signColumn.toString()).equals(relationSign))                     //按照具有业务意义的字段区分
					||(EXCEL_RELATION_SIGN.equals(signColumn) && Long.parseLong(tmp.getString(signColumn)) == entityRowNo)){   //按照主表表格的行号区分
				tmp.remove(signColumn);
				tmp.put(entityKeyName, entityKey);
				result.add(tmp);
				sheetData.remove(i);
				i--;
			}
		}
		
		return result;
	}
	
	public List<Map<String, Object>> getExcelCellValue(String tableName, String[] column){
		List<Map<String, Object>> result = new ArrayList<>();
		JSONArray sheetData = this.excelData.getJSONArray(tableName);
		if(sheetData == null || sheetData.isEmpty() 
				|| column == null || column.length <= 0) 
			return result;
		
		for(int i=0; i<sheetData.size(); i++){
			JSONObject obj = sheetData.getJSONObject(i);
			Map<String, Object> map = new HashMap<>();
			for(String s: column){
				map.put(s, obj.get(s));
			}
			result.add(map);
		}
		
		return result;
	}
	
	public String[] getTableName(){
		return this.tableName;
	}
}
