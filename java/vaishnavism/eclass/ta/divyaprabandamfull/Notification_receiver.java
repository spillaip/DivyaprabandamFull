package vaishnavism.eclass.ta.divyaprabandamfull;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

import static vaishnavism.eclass.ta.divyaprabandamfull.NotificationActivity.NotifyAmPm;
import static vaishnavism.eclass.ta.divyaprabandamfull.NotificationActivity.NotifyEnabled;


/**
 * Created by spillaip on 3/15/2018.
 */

public class Notification_receiver extends BroadcastReceiver {

    public static final String MyPREFERENCES = "VeCPref" ;
    SharedPreferences shared;

    @Override
    public void onReceive(Context context, Intent intent) {
        shared = context.getSharedPreferences(MyPREFERENCES, 0);


        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            final String prefNotifyEnabled=shared.getString("NotifyEnabled","N");
            final String prefNotifyAmPm=shared.getString("NotifyAmPm","AM");
            final String prefNotifyHour=shared.getString("NotifyHour","06");
            final String prefNotifyMinutes=shared.getString("NotifyMinutes","00");

            NotificationActivity.setNotification(context,prefNotifyEnabled,Integer.parseInt(prefNotifyHour), Integer.parseInt(prefNotifyMinutes) );
            //NotificationActivity.setNotification(context,prefNotifyEnabled,10, 40 );
            //Toast.makeText(context, "திவ்யப்பரந்தம் பரிமாரப்படும் after boot for "+prefNotifyHour+":"+prefNotifyMinutes, Toast.LENGTH_SHORT).show();

            //buildNotification(context);
       }
       else
        {
            buildNotification(context);
            //Toast.makeText(context, "திவ்யப்பரந்தம் பரிமாரப்பட்டது in normal mode", Toast.LENGTH_SHORT).show();
        }


    }

    private void buildNotification(Context context) {
        // To prepare and populate ListView with Prabandam
        String[] fillList = context.getResources().getStringArray(R.array.prabandam);

        int listSize = fillList.length;
        Random rand = new Random();
        int showMe = rand.nextInt(listSize - 1);
        String showText = fillList[showMe].replace("\n", "\r");


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Intent trigerIntent = new Intent(context, MainActivity.class);

        trigerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, trigerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        final Notification.Builder builder = new Notification.Builder(context);
        builder.setStyle(new Notification.BigTextStyle(builder)
                .bigText(showText)
                .setBigContentTitle("நாள் தோறும் நாலாயிரம்")
                .setSummaryText("vaishnavism.eclass@gmail.com"))
                .setContentTitle("திவ்யபிரபந்தம்")
                .setContentText(showText)
                .setSmallIcon(R.drawable.vishnu_transparent);

        // open MainActivity on click of the notification
        builder.setContentIntent(pendingIntent);

        notificationManager.notify(100, builder.build());
        //NotificationActivity



        ComponentName receiver = new ComponentName(context, Notification_receiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        //Toast.makeText(context, "தினம் ஒரு திவ்யப்பரந்தம் பரிமாரப்பட்டது", Toast.LENGTH_SHORT).show();


        //Play sound
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        Intent repeatingIntent = new Intent(context, MainActivity.class);

        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        pendingIntent = PendingIntent.getActivity(context, 100, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //final Notification.Builder builder = new Notification.Builder(context);
        builder.setStyle(new Notification.BigTextStyle(builder)
                .bigText(showText)
                .setBigContentTitle("நாள் தோறும் நாலாயிரம்")
                .setSummaryText("vaishnavism.eclass@gmail.com"))
                .setContentTitle("திவ்யபிரபந்தம்")
                .setContentText(showText)
                .setSmallIcon(R.drawable.vishnu_transparent);

        notificationManager.notify(100, builder.build());
        */
    }
}




