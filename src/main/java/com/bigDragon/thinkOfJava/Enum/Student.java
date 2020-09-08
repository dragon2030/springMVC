package com.bigDragon.thinkOfJava.Enum;

public enum Student {
	JACK(1,"26"),
	Bob(2,"22"),
	Alice(3,"21");
	
	private int school_id;
	private String age;
	
	private Student(int school_id,String age){
		this.school_id=school_id;
		this.age=age;
	}
	
	public static String getStudentMessageById(int school_id){
		for(Student s:Student.values()){
			if(s.getSchool_id() == school_id){
				return "姓名："+s.toString()+" 学号:"+s.getSchool_id()+" 年龄:"+s.age;
			}
		}
		return null;
	}

	public int getSchool_id() {
		return school_id;
	}

	public void setSchool_id(int school_id) {
		this.school_id = school_id;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	
	
	
}
