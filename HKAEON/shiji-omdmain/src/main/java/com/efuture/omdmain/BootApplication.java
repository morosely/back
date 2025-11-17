package com.efuture.omdmain;

import com.product.util.SpringContext;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

//@EnableDiscoveryClient
//@SpringBootApplication
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@SpringBootApplication(scanBasePackages = {"com.efuture"})
@EnableDiscoveryClient(autoRegister = true)
@ServletComponentScan
public class BootApplication {
	@Bean
	@LoadBalanced
	public RestTemplate restTempldate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringContext.run(BootApplication.class, args);
		/*String data = "{'data':[{'shopCode':'002','goodsCode':'10000','goodsName':'电信-1','mainBarcodeFlag':1,'updateDate':'2023-07-11 10:00:01'}," +
				"{'shopCode':'002','goodsCode':'10000','goodsName':'电信-2','mainBarcodeFlag':1,'updateDate':'2023-07-11 10:00:02'},"+
				"{'shopCode':'002','goodsCode':'10000','goodsName':'电信-3','mainBarcodeFlag':0,'updateDate':'2023-07-11 10:00:03'},"+
				"{'shopCode':'001','goodsCode':'10086','goodsName':'移动-1','mainBarcodeFlag':1,'updateDate':'2023-07-11 10:00:01'}," +
				"{'shopCode':'001','goodsCode':'10086','goodsName':'移动-2','mainBarcodeFlag':1,'updateDate':'2023-07-11 10:00:02'}" +
				"{'shopCode':'001','goodsCode':'10086','goodsName':'移动-3','mainBarcodeFlag':0,'updateDate':'2023-07-11 10:00:03'}" +
				"]}";

		JSONObject json = JSON.parseObject(data);
		JSONArray array = json.getJSONArray("data");
		List<JSONObject> jsonList = new ArrayList<>();
		for (Object o : array) {
			jsonList.add((JSONObject) o);
		}
		Map<String,List<JSONObject>> group = jsonList.stream().collect(Collectors.groupingBy(e -> e.get("goodsCode")+ "|" +e.get("shopCode")));
		System.out.println(group);
		for (Map.Entry<String, List<JSONObject>> map : group.entrySet()) {
			map.getValue().sort(Comparator.comparing(obj -> ((JSONObject) obj).getInteger("mainBarcodeFlag"),Comparator.reverseOrder())
					.thenComparing(obj -> ((JSONObject) obj).getDate("updateDate"), Comparator.reverseOrder()));
		}
		System.out.println(group);*/
	}
}
