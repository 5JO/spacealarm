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

		//list// 알람 리스트 DB 테이블
		//--------------------------------------------------------//
		//  title | 목적지| 거리설정 | 켜져있는지 상태  | 위도   | 경도    | 소리  | 벨  |//
		//--------------------------------------------------------//
		db.execSQL("create table if not exists list(title text primary key, dst text, distance int, turn int, lat double, lng double, bell text, biv int)");
		//db.execSQL("INSERT INTO list(title, dst, distance, turn) VALUES('우리집', '대구', '200', '0')");
		
		//벨소리 & 진동 설정 DB 테이블           진동이 켜졌을 땐 1 아닐 땐 0, 벨소리 저장 주소
		db.execSQL("create table if not exists setinfo(num int primary key, bell text, biv int)");
		//db.execSQL("INSERT INTO setinfo(num, bell, biv) VALUES('1', 'fsf', '1')");
	}
	
	
	//리스트 테이블에 데이터 추가
	public void appendList(String title, String dst, int distance, int turn, double lat, double lng, String bell, int biv) {
		String sql = "insert into list(title, dst, distance, turn, lat, lng, bell, biv) values('"+ title +"', '" + dst + "', '"+ distance + "', '" + turn +"','"+lat+"','"+lng+"','"+bell+"','"+biv+"')";
		db.execSQL(sql);
	}
	
	//리스트 테이블의 내용을 커서에 받아옴
	public Cursor fetchAllLists() {
		Cursor cursor = null;
		cursor = db.rawQuery("select * from list", null);
		return cursor;
	}
	
	//리스트 테이블에 데이터 삭제
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
	
	public void updateListTurn(String title, int turn){ //update list set turn = 1 where title = 'ㄱㅅㅈ'
		String sql = "update list set turn = "+turn+" where title = '"+title+"'";
		db.execSQL(sql);
	}
	
		
	////////////////////////
	
	//리스트 테이블의 내용을 커서에 받아옴
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
