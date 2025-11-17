//package com.efuture.omdmain;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//public class test {
//	
//
//	
//	public static void main(String[] args) throws InterruptedException, ExecutionException {
//		System.out.println("----程序开始运行----");  
//		   Date date1 = new Date();  
//		  
//		 int taskSize = 50;  
//		   // 创建一个线程池  
//		   ExecutorService pool = Executors.newFixedThreadPool(taskSize);  
//		   // 创建多个有返回值的任务  
//		   List<Future> list = new ArrayList<Future>();  
//		   
//		   for (int i = 0; i < taskSize; i++) {  
//			    Callable c = new MyCallable(i + " ");  
//			    // 执行任务并获取Future对象  
//			    Future f = pool.submit(c);  
//			    // System.out.println(">>>" + f.get().toString());  
//			    list.add(f);  
//			   }  
//			   // 关闭线程池  
//			   pool.shutdown();  
//			  
//			   Map<String,Integer> map = new HashMap<String,Integer>();
//			   // 获取所有并发任务的运行结果  
//			   for (Future f : list) {  
//			    // 从Future对象上获取任务的返回值，并输出到控制台  
//			    System.out.println(">>>" + f.get().toString());  
//			    String ids = f.get().toString();
//			    String idss[] = ids.split(";");
//				    for(String s : idss){
//				    	if(!s.isEmpty()){
//				    		if(!map.containsKey(s)){
//				    			map.put(s, 1);
//				    		}else{
//				    			int n = map.get(s)+1;
//				    			map.put(s, n);
//				    		}
//				    	}
//				    }
//			   }  
//			  
//			   for(Map.Entry<String, Integer> entry : map.entrySet()){
//				   String key = entry.getKey().toString();  
//				   Integer value = entry.getValue();  
////				   System.out.println("key=" + key + " value=" + value);
//				   if(value>1){
//					   System.out.println("==============>"+key);
//				   }
//			   }
//			   
//			   Date date2 = new Date();  
//			   System.out.println("----程序结束运行----，程序运行时间【"  
//			     + (date2.getTime() - date1.getTime()) + "毫秒】");  
//		
////		for(int i=1;i<10;i++){
////			getId(true);
////		}
//	}
//
//}
//
//class MyCallable implements Callable<Object> {  
//private String taskNum;  
//private static Integer number = new Integer(0);
//private static String hostId = "";
//  
//MyCallable(String taskNum) {  
//   this.taskNum = taskNum;  
//}  
//
//public static long getId(boolean isRandom){
//	long uid = 0;
//    Date dt = new Date();
//    
//    // 最大ID(7643726453097023999)
//    //dt.setYear(300);
//    //dt.setMonth(11);
//    //dt.setDate(31);
//    //dt.setHours(23);
//    //dt.setMinutes(59);
//    //dt.setSeconds(59);
//    //number = 999999;
//    //hostId = "999";
//    
//    // 时间去掉毫秒(32位二进制整数,可表示到2200/12/31 23:59:59年)
//    uid   = dt.getTime() / 1000;
////    System.out.println(uid);
//    uid <<= 20;
////    System.out.println(uid);
//	// 加上序号(20位二进制整数,最大可表示整数999999)
//	uid += number;
////	System.out.println(uid);
//	number = (number + 1) % 1000000;
////	System.out.println(number);
//	
//	// 加上机器号(3位10进制,最大整数999)
//	String currentHostID = hostId;
//	
//	// 随机生成HostID
//	if (isRandom) {
//		Random random    = new Random(); // 注意：如果Random给参数，产生重复的概率很大
//		int randomHostID = random.nextInt(999);
//		currentHostID    = String.format("%1$03d", randomHostID);
//	}
//	
//	long id = Long.parseLong(String.valueOf(uid)+currentHostID);
//	String s = Long.toString(id);
////	System.out.println(s.substring(s.length()-8));
//	return id;
//}
//  
//public Object call() throws Exception {  
//   System.out.println(">>>" + taskNum + "任务启动");  
////   Date dateTmp1 = new Date();  
////   Thread.sleep(1000);  
////   Date dateTmp2 = new Date();  
////   long time = dateTmp2.getTime() - dateTmp1.getTime();  
//   String s = "";
//   for(int i =1;i<100000;i++){
//	   long id = getId(true);
//	   s+= id+";";
//   }
//   
////   System.out.println(">>>" + taskNum + "任务终止");  
//   return  s;  
//}  
//}