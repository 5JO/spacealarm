package kr.ac.kumoh.mobile.space_alarm;

public class MySettingItem {
	String mTitle; // ����
	String mSelected; // ���õ� ������
	
	public MySettingItem(String title,String selected){
		this.mTitle = title;
		this.mSelected = selected;
	}
	
	public void setSelected(String selected){
		this.mSelected = selected;
	}
}
