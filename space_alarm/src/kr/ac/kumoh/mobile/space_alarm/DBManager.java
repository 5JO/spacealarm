package kr.ac.kumoh.mobile.space_alarm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DBManager {

Context mContext;
	
	SQLiteDatabase db;
	public DBManager(Context mContext) {
		super();
		this.mContext = mContext;
		db = mContext.openOrCreateDatabase("GAlarm.db", Context.MODE_PRIVATE, null);

		//list// �˶� ����Ʈ DB ���̺�
		//--------------------------------------------------------//
		//  title | ������| �Ÿ����� | �����ִ��� ����  | ����   | �浵    | �Ҹ�  | ��  |//
		//--------------------------------------------------------//
		db.execSQL("create table if not exists list(title text primary key, dst text, distance int, turn int, lat double, lng double, bell text, biv int)");
		//db.execSQL("INSERT INTO list(title, dst, distance, turn) VALUES('�츮��', '�뱸', '200', '0')");
		
		//���Ҹ� & ���� ���� DB ���̺�           ������ ������ �� 1 �ƴ� �� 0, ���Ҹ� ���� �ּ�
		db.execSQL("create table if not exists setinfo(num int primary key, bell text, biv int)");
		//db.execSQL("INSERT INTO setinfo(num, bell, biv) VALUES('1', 'fsf', '1')");
	}
	
	
	//����Ʈ ���̺� ������ �߰�
	public void appendList(String title, String dst, int distance, int turn, double lat, double lng, String bell, int biv) {
		String sql = "insert into list(title, dst, distance, turn, lat, lng, bell, biv) values('"+ title +"', '" + dst + "', '"+ distance + "', '" + turn +"','"+lat+"','"+lng+"','"+bell+"','"+biv+"')";
		db.execSQL(sql);
	}
	
	//����Ʈ ���̺��� ������ Ŀ���� �޾ƿ�
	public Cursor fetchAllLists() {
		Cursor cursor = null;
		cursor = db.rawQuery("select * from list", null);
		return cursor;
	}
	
	//����Ʈ ���̺� ������ ����
	public void deleteList(String title) {
		String sql = "delete from list where title = '"+title+"'";
		db.execSQL(sql);
	}
	
	public Cursor selectAlarm(String title){
		Cursor cursor = null;
		cursor = db.rawQuery("select * from list where title = '"+title+"'", null);
		return cursor;
	}
	
	public Cursor isbeing(){
		Cursor cursor = null;
		cursor = db.rawQuery("select turn from list", null);
		return cursor;
	}
	
	public void updateListTurn(String title, int turn){ //update list set turn = 1 where title = '������'
		String sql = "update list set turn = "+turn+" where title = '"+title+"'";
		db.execSQL(sql);
	}
	
		
	////////////////////////
	
	//����Ʈ ���̺��� ������ Ŀ���� �޾ƿ�
	public Cursor fetchSetinfo() {
		Cursor cursor = null;
		cursor = db.rawQuery("select * from setinfo", null);
		return cursor;
	}
	
	public void updateSetinfo(String bell, int biv){
		String sql = "update setinfo set bell = '"+bell+"', biv = "+biv+" where num = '1'";
		db.execSQL(sql);
	}
	

}
