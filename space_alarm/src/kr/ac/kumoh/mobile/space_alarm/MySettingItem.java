package kr.ac.kumoh.mobile.space_alarm;

public class MySettingItem {
	String mTitle; // 제목
	String mSelected; // 선택된 아이템
	
	public MySettingItem(String title,String selected){
		this.mTitle = title;
		this.mSelected = selected;
	}
	
	public void setSelected(String selected){
		this.mSelected = selected;
	}
}
