package kr.ac.kumoh.mobile.space_alarm;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class settinglist extends LinearLayout {

	private TextView text;
	private TextView text2;
	private ImageView mIcon;
	
	public settinglist(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}
	
	public settinglist(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.settinglist, this, true);
		
		text = (TextView) findViewById(R.id.text);
		text2 = (TextView) findViewById(R.id.text2);
		mIcon = (ImageView) findViewById(R.id.image);

		
	}
	
	public void setTitle(String data){
		text.setText(data);
	}
	public void setSelected(String data){
		text2.setText(data);
	}
	public void setIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
    }
}
