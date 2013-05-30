package kr.ac.kumoh.mobile.space_alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Vibrator;
import android.widget.Toast;

public class LocationReceiver extends BroadcastReceiver {

	Vibrator vibrator;
	@Override
	public void onReceive(Context context, Intent intent) {
	    boolean isEntering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
	    if(isEntering){
	    	Toast.makeText(context, "��ǥ ������ ������..", Toast.LENGTH_LONG).show();
	    	vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
	    	vibrator.vibrate(1000);
	    }
	    else
	    	Toast.makeText(context, "��ǥ �������� ����ϴ�.", Toast.LENGTH_LONG).show();
	}

}
