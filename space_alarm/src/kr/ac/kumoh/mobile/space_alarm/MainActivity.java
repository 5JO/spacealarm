package kr.ac.kumoh.mobile.space_alarm;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity {

	private LocationManager locManager;
	private LocationListener locationListener;
	private NotificationManager mNM;
	private Notification mNoti;

	String title = null;
	float[] distance = new float[2];
	double lat, lng, area;
	int count, flag;
	int pos;

	private Button mAdd;	
	
	private ArrayList<Integer> turn;
	private ArrayList<String> item;
	private ArrayList<Double> Aitem;

	Context mContext;
	DBManager dbManager;
	AlertDialog.Builder alertDialog;

	ListView lv;
	CustomAdapter custom_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		

		mContext = getApplicationContext();

		lv = (ListView)findViewById(R.id.listView1);

		dbManager = new DBManager(this);

		reflashList(dbManager, lv, mContext);

		mAdd = (Button)findViewById(R.id.insert);
		mAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SettingActivity.class);
				startActivity(intent);
			}
		});
				
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		reflashList(dbManager, lv, mContext);
	}


	//�˶� ����Ʈ�� ����ȸ
	public void reflashList(DBManager manager, ListView lv, Context mContext) {
		Cursor cursor = manager.fetchAllLists();
		item = new ArrayList<String>();
		flag = 1;		

		if(cursor.moveToFirst()){
			do{
				item.add(cursor.getString(0));
			}while(cursor.moveToNext());
		}

		custom_adapter = new CustomAdapter(mContext, R.layout.chkbox, item);
		lv.setAdapter(custom_adapter);
	}



	class CustomAdapter extends BaseAdapter {
		ArrayList<String> adapter_item;
		Context context;
		int layout;
		ViewHolder holder;
		public boolean[] isChecked;
		AlertDialog dialog;
		int i;

		public CustomAdapter(Context mContext, int list, ArrayList<String> item) {
			context = mContext;
			layout = list;
			adapter_item = item;
		}



		@Override
		public int getCount() {
			return adapter_item.size();
		}

		@Override
		public Object getItem(int position) {
			return adapter_item.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}


		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				convertView = inflater.inflate(layout, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView)convertView.findViewById(R.id.textView1);
				holder.check = (CheckBox)convertView.findViewById(R.id.checkBox1);
				convertView.setTag(holder);

			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}

			pos = position;//
			
			holder.text.setText(adapter_item.get(position));

			isChecked = new boolean[adapter_item.size()];


			Cursor cursor = dbManager.isbeing();
			turn = new ArrayList<Integer>();
			if(cursor.moveToFirst()){
				do{
					turn.add(cursor.getInt(0));
				}while(cursor.moveToNext());
			}

			i = (int)getItemId(position);

			int state = turn.get(i);  //�ش� ����Ʈ�� üũ ���¸� ��Ÿ���� state

			if(state == 1)
				isChecked[i] = true;


			if(i == (turn.size()-1)){
				if(state == 1)  //�� ����Ʈ���� On ������ �� ������ ������ ���ϰ� �ϱ����ؼ�
					flag = 0;
				else
					flag = 3;
			}
  
			//üũ�� ������� �� ������
			holder.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
					title = getItem(position).toString();

					if(flag == 1) { //ó���� ������ �� üũ�� �Ǿ� �ִ��� �ƴ��� Ȯ�� �ϱ� ���� �÷���, üũ�� �ٲ��ֱ� ������ �ƹ� ���� ���� �ʰ� ������.
					}
					else { 
					/*///////////////////////*/
					if(flag == 0)
						flag = 3;
					else if(flag == 3){//

						if(checked){
							Cursor cursor = dbManager.selectAlarm(title);
							cursor.moveToFirst();
							Aitem = new ArrayList<Double>();

							Aitem.add(cursor.getDouble(2));
							Aitem.add(cursor.getDouble(4));
							Aitem.add(cursor.getDouble(5));

							area = Aitem.get(0);
							lat  = Aitem.get(1);
							lng  = Aitem.get(2);

							locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

							locationListener = new LocationListener(){

								@SuppressWarnings("deprecation")
								@Override
								public void onLocationChanged(Location location) {
									Location.distanceBetween(location.getLatitude(), location.getLongitude(), lat, lng, distance);             
									if(distance[0] < area) {
										
										String Suri;
										Cursor cursor = dbManager.selectAlarm(title);
										cursor.moveToFirst();
										Suri = cursor.getString(6);
										
										mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
										
										mNoti = new Notification(R.drawable.ic_launcher, "�ȳ�", System.currentTimeMillis());
										Context context1 = getApplicationContext();
										Intent intent = new Intent(getApplicationContext(), MainActivity.class);
										PendingIntent pendIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

										mNoti.setLatestEventInfo(context1, "hi", "�̰��� �׽�Ʈ�Դϴ�", pendIntent);
										
										//���� ���� Ȯ��
										if(cursor.getInt(7) == 1){
											long[] patterns = new long[] { 200, 400, 200};
											mNoti.vibrate = patterns;
										}
										
										//���Ҹ� ���� Ȯ��
										if(Suri != "����"){
											Uri soundURI = Uri.parse(Suri);
											mNoti.sound = soundURI;
										}
										
										//���Ҹ�&������ ����ڰ� �˸�â�� �Ӷ� ���� �︲
										mNoti.flags |= Notification.FLAG_INSISTENT;

										mNM.notify(7777, mNoti);

										locManager.removeUpdates(this);
										isChecked[position] = false;
										dbManager.updateListTurn(title, 0);
										reflashList(dbManager, lv, mContext);
									}

									//Toast.makeText(mContext, String.valueOf(distance[0]), Toast.LENGTH_LONG).show();
								}

								@Override
								public void onProviderDisabled(String provider) { }

								@Override
								public void onProviderEnabled(String provider) { }

								@Override
								public void onStatusChanged(String provider, int status, Bundle extras) { }

							};

							locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
							dbManager.updateListTurn(title, 1); //DB�� �ش� title�� turn�� on ���·� �ٲ㼭 ����
							Toast.makeText(context, title + " �����Ѵ�", Toast.LENGTH_LONG).show();

							isChecked[position] = true;
						}
						else {
							locManager.removeUpdates(locationListener);
							dbManager.updateListTurn(title, 0);
							Toast.makeText(context, title + " �����Ѵ�", Toast.LENGTH_LONG).show();
							isChecked[position] = false;
						}
					}
					/*///////////////////////*/
					}
				}
			});


			if(isChecked[position]) {
				holder.check.setChecked(true);
			}
			else {
				holder.check.setChecked(false);
			}


			holder.check.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {

					AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

					alertDialog.setTitle("����");
					alertDialog.setMessage("�����ҷ�?");
					alertDialog.setCancelable(true);

					alertDialog.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dbManager.deleteList(getItem(position).toString());
							reflashList(dbManager, lv, mContext);
						}
					});

					alertDialog.setNegativeButton(android.R.string.no,
							new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});

					alertDialog.setOnCancelListener(
							new DialogInterface.OnCancelListener() {

								@Override
								public void onCancel(DialogInterface dialog) {
									// TODO Auto-generated method stub

								}
							});

					alertDialog.show();

					return true;
				}
			});

			return convertView;
		}
	}

}
class ViewHolder {
	TextView text;
	CheckBox check;

}

