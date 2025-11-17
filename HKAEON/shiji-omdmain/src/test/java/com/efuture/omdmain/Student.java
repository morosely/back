package com.efuture.omdmain;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Student {
	public static void main(String[] args) {
//		String goodsType = "1";
//		String regex = "^[0-9]{1}$";
//		if (!goodsType.matches(regex)) {
//			System.out.println("商品类型值必须为整数");
//		}else{
//			System.out.println(11);
//		}
		
		List<Student> list = new ArrayList<Student>();
		for(int i =1;i<=1001;i++){
			Random r = new Random();
			String str = ""+(char)(Math.random()*26+'A');
			Student stuA = new Student(i, str, str, r.nextInt(3));
			list.add(stuA);
		}
//		Student stuA = new Student(1, "A", "M", 184);
//		Student stuB = new Student(2, "B", "G", 163);
//		Student stuC = new Student(3, "C", "M", 175);
//		Student stuD = new Student(4, "D", "G", 158);
//		Student stuE = new Student(5, "E", "M", 170);
//		
//		list.add(stuA);
//		list.add(stuB);
//		list.add(stuC);
//		list.add(stuD);
//		list.add(stuE);
		
		/*
		java 7 
		*/
		System.out.println(new Date().getTime());
		Iterator<Student> iterator = list.iterator();
		while(iterator.hasNext()) {
		    Student stu = iterator.next();
		    if (stu.getSex().equals("G")) {

		        System.out.println(stu.toString());
		    }
		}
		System.out.println(new Date().getTime());
		System.out.println("*****************");
		System.out.println(new Date().getTime());
		list.stream()
	    .filter(student -> student.getSex().equals("G"))
	    .forEach(student -> System.out.println(student.toString()));
		System.out.println(new Date().getTime());
	}
	
	  	int no;
	    String name;
	    String sex;
	    int height;
	    public Student(int no, String name, String sex, int height) {
	        this.no = no;
	        this.name = name;
	        this.sex = sex;
	        this.height = height;
	    }

		public int getNo() {
			return no;
		}

		public void setNo(int no) {
			this.no = no;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public float getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}
		
		public String toString(){
			return "no:"+getNo()+","+"name"+getName()+","+"sex:"+getSex()+","+"height:"+getHeight()+",";
		}
	    
}
