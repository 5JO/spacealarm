package kr.ac.kumoh.mobile.space_alarm;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingActivity extends Activity {
	DBManager dbManager;
	String mDst = "����"; // ������
	String mLabel; // ����
	String mBell = "����";
	double mDstLat;
	double mDstLng;
	int mArea = 500;
	int mTurn = 0; // ������ �˶� �۵�
	int mBiv=0;
	private int brightness = 50;
	Context context = this;

	private SeekBar seekArea;
	private ListView list;
	private Button saveBtn, cancelBtn;

	private ArrayList<MySettingItem> setlist;
	MyAdapter adapter = new MyAdapter(this, R.layout.setting);
	final String[] items = { "���Ҹ�", "����" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);

		dbManager = new DBManager(this);

		saveBtn = (Button) findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(listener);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(listener);

		seekArea = (SeekBar) findViewById(R.id.seekBar);
		seekArea.setProgress(brightness);
		seekArea.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				mArea = (progress * 10) + 1;
				printSelected(mArea);

			}

			public void onStartTrackingTouch(SeekBar arg0) {
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

		});

		setlist = new ArrayList<MySettingItem>();
		setlist.add(new MySettingItem("������ ����", ""));
		setlist.add(new MySettingItem("�˶� ���� ����", ""));
		setlist.add(new MySettingItem("�˶��� ����", ""));
		setlist.add(new MySettingItem("�� ", ""));

		list = (ListView) findViewById(R.id.list);
		
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				if (position == 0) {
					Intent mapintent = new Intent(context, MapActivity.class);
					startActivityForResult(mapintent, 1);
				}

				else if (position == 1) {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							SettingActivity.this);

					alertDialog.setTitle("�˶� ���� ����");
					alertDialog.setSingleChoiceItems(items, -1,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									if(items[which] == "����"){
										mBiv = 1;
									}
									else mBiv = 0;
									
									
									setlist.set(1, new MySettingItem(
											"�˶� ���� ����", items[which]));
									adapter.notifyDataSetChanged();
									dialog.dismiss();
								}

							});

					alertDialog.show();

				}

				else if (position == 2) {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							SettingActivity.this);
					alertDialog.setTitle("�˶��� ����");
					Intent intent = new Intent(
							RingtoneManager.ACTION_RINGTONE_PICKER);
					// Set Title
					intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
							"�˶��� ����");
					// Set Silent Option
					intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT,
							true);
					// Set Default Ringtone Option
					intent.putExtra(
							RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
					// Ringtone Type
					intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
							RingtoneManager.TYPE_ALL);
					startActivityForResult(intent, 2);

				} else if (position == 3) {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							SettingActivity.this);
					alertDialog.setTitle("��");
					alertDialog.setMessage("���� �Է��ϼ���");
					final EditText inputlabel = new EditText(
							SettingActivity.this);
					alertDialog.setView(inputlabel);
					alertDialog.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String input = inputlabel.getText()
											.toString();
									setlist.set(3, new MySettingItem("��",
											input));
									adapter.notifyDataSetChanged();
									mLabel = input;
								}
							});

					alertDialog.setNegativeButton(android.R.string.no,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
								}
							});

					alertDialog
							.setOnCancelListener(new DialogInterface.OnCancelListener() {

								@Override
								public void onCancel(DialogInterface dialog) {
									// TODO Auto-generated method stub

								}
							});

					alertDialog.show();
				}
			}
		});
		list.setAdapter(adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				mDst = data.getStringExtra("dst");
				setlist.set(0, new MySettingItem("������ ����", mDst));
				adapter.notifyDataSetChanged();
				mDstLat = data.getDoubleExtra("dstLat", 0);
				mDstLng = data.getDoubleExtra("dstLng", 0);
				break;
			case 2:
				Uri pickedUri = data
						.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

				if (pickedUri != null) {
					Ringtone ringtone = RingtoneManager.getRingtone(this,
							pickedUri);
					mBell = pickedUri.toString();
					setlist.set(2,new MySettingItem("�˶��� ����", ringtone.getTitle(this)));
					adapter.notifyDataSetChanged();
					ringtone.play();
					ringtone.stop();

				}
			}
		}

	}

	public void printSelected(int value) {
		TextView tv = (TextView) findViewById(R.id.distanceText);
		tv.setText(String.valueOf(value) + "M");
	}

	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.saveBtn:
				dbManager.appendList(mLabel, mDst, mArea, mTurn, mDstLat, mDstLng, mBell, mBiv);
				finish();
				break;
			case R.id.cancelBtn:
				finish();
				break;
			}

		}
	};

	class MyAdapter extends BaseAdapter {

		public MyAdapter(Context context, int layoutRes) {

		}

		@Override
		// �����ϰ� �ִ� �������� ����
		public int getCount() {

			// TODO Auto-generated method stub
			return setlist.size();
		}

		@Override
		// �����Ͱ�
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return setlist.get(position);
		}

		@Override
		// �ε�����
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		// ������ �������� ������ �並 ����
		public View getView(int position, View converView, ViewGroup parent) {
			final int index = position;
			settinglist layout = new settinglist(getApplicationContext());

			layout.setTitle(setlist.get(index).mTitle);
			layout.setSelected(setlist.get(index).mSelected);

			return layout;

		}

	}

}
