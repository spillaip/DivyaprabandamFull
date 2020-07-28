package vaishnavism.eclass.ta.divyaprabandamfull;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


public class NotificationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String MyPREFERENCES = "VeCPref" ;
    public static final String NotifyEnabled = "N";
    public static final String NotifyHour = "6";
    public static final String NotifyMinutes = "0";
    public static final String NotifyAmPm = "AM";
     String prefNotifyEnabled = "N";
     String prefNotifyHour = "06";
     String prefNotifyMinutes = "00";
     String prefNotifyAmPm = "AM";
    String cb_value="N";

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String currentNotificationTime;
    CheckBox checkBox;
    String hourSet;
    String minutesSet;

    TimePicker timePicker;
    TextView btnTime;

    public static final int DAILY_REMINDER_REQUEST_CODE = 100;
    public static final String TAG = "NotificationScheduler";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);



        sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES,getApplicationContext().MODE_PRIVATE);
        editor = sharedpreferences.edit();


        //pref data store
        if(sharedpreferences.contains("NotifyEnabled")) {
            prefNotifyEnabled = sharedpreferences.getString("NotifyEnabled", "N");
        }
        if(sharedpreferences.contains("NotifyHour")) {
            prefNotifyHour = sharedpreferences.getString("NotifyHour", "06");
        }
        if(sharedpreferences.contains("NotifyMinutes")) {
            prefNotifyMinutes = sharedpreferences.getString("NotifyMinutes", "00");
        }
        if(sharedpreferences.contains("NotifyAmPm")) {
            prefNotifyAmPm = sharedpreferences.getString("NotifyAmPm", "AM");
        }

       // Toast.makeText(getApplicationContext(),"Time stored is "+prefNotifyHour+":"+prefNotifyMinutes+" "+prefNotifyAmPm+"  Notification enabled "+prefNotifyEnabled,Toast.LENGTH_SHORT).show();
        currentNotificationTime = prefNotifyHour+":"+prefNotifyMinutes;

        btnTime = (TextView) findViewById(R.id.btnCurrentTime);
        btnTime.setText(currentNotificationTime);


        timePicker = (TimePicker) findViewById(R.id.timePicker);

        timePicker.setCurrentHour(Integer.valueOf(prefNotifyHour));
        timePicker.setCurrentMinute(Integer.valueOf(prefNotifyMinutes));
        timePicker.setIs24HourView(true);

        checkBox = (CheckBox) findViewById(R.id.cb_enable);

        if (prefNotifyEnabled.equals("Y")){
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        checkBox.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()){
                    cb_value="Y";
                    editor.putString("NotifyEnabled", "Y");
                    editor.commit();

                }
                else{
                    cb_value="N";
                    editor.putString("NotifyEnabled", "N");
                    editor.commit();
                }
            }
        });

        Button btnSave = (Button) findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 23) {
                    editor.putString("NotifyEnabled", cb_value);
                    editor.putString("NotifyHour", String.valueOf(timePicker.getHour()));
                    editor.putString("NotifyMinutes", String.valueOf(timePicker.getMinute()));
                    //editor.putString(NotifyAmPm, spinnerAmPm.getSelectedItem().toString());
                    editor.commit();
                     hourSet = sharedpreferences.getString("NotifyHour","06");
                     minutesSet =  sharedpreferences.getString("NotifyMinutes","00");

                    //Toast.makeText(getApplicationContext(),"Time in the Clock is "+timePicker.getHour()+":"+timePicker.getMinute(),Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(),"Time in the SharedPref is "+sharedpreferences.getString("NotifyHour","06")+":"+sharedpreferences.getString("NotifyMinutes","00"),Toast.LENGTH_LONG).show();

                }
                else {

                        editor.putString("NotifyEnabled", cb_value);
                        editor.putString("NotifyHour", String.valueOf(timePicker.getCurrentHour()));
                        editor.putString("NotifyMinutes", String.valueOf(timePicker.getCurrentMinute()));
                        editor.commit();
                      //  Toast.makeText(getApplicationContext(),"Time in the Clock is "+timePicker.getCurrentHour()+":"+timePicker.getCurrentMinute(),Toast.LENGTH_LONG).show();
                       // Toast.makeText(getApplicationContext(),"Time in the SharedPref is "+sharedpreferences.getString("NotifyHour","06")+":"+sharedpreferences.getString("NotifyMinutes","00"),Toast.LENGTH_LONG).show();
                        hourSet = sharedpreferences.getString("NotifyHour","06");
                        minutesSet =  sharedpreferences.getString("NotifyMinutes","00");
                        }




                String enabledY = "N";

                if (checkBox.isChecked()) {
                    enabledY = "Y";
                    editor.putString("NotifyEnabled", "Y");
                    editor.commit();
                }
                else {
                    enabledY="N";
                    editor.putString("NotifyEnabled", "N");
                    editor.commit();
                }


                currentNotificationTime = hourSet+":"+minutesSet;
                if (checkBox.isChecked()) {
                    setNotification(getApplicationContext(),enabledY,Integer.parseInt(hourSet), Integer.parseInt(minutesSet));
                } else
                {
                    cancelReminder(getApplicationContext(), Notification_receiver.class);
                }
            }
        });


    }

    public static void setNotification(Context context, String enabledY,int hour, int minute ) {


        String enabled=enabledY;

        Calendar calendar = Calendar.getInstance();

        if (Build.VERSION.SDK_INT >= 23) {
            calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    hour,
                    minute,
                    0
            );
        } else {
            calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    hour,
                    minute,
                    0
            );
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Notification_receiver.class );
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,  0, intent,0);

        alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);


        Toast.makeText(context,"தினம் ஒரு திவ்யப்பரந்தம் "+ hour + ":"+ minute +" மணிக்கு பரிமாரப்படும்",Toast.LENGTH_LONG).show();
    }


    public static void cancelReminder(Context context, Class<?> cls) {
        // Disable a receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();

        Toast.makeText(context,"Notification is cancelled",Toast.LENGTH_LONG).show();
    }
/*
    public static void showNotification(Context context, Class<?> cls, String title, String content) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setStyle(new Notification.BigTextStyle(builder));
        Notification notification = builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent).build();


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification);

        Toast.makeText(context,"Notification is set repeating",Toast.LENGTH_LONG).show();
    }
*/
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}