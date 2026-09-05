package com.topocontrol15;
import android.app.*; import android.content.*; import android.os.*; import android.location.*; import java.util.*;
public class LocationService extends Service implements LocationListener {
 DB db; int crew=1; LocationManager lm;
 public int onStartCommand(Intent i,int f,int id){ if(i!=null) crew=i.getIntExtra("crew",1); db=new DB(this); lm=(LocationManager)getSystemService(LOCATION_SERVICE);
  startForeground(7,notification()); try{lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,3600000L,0,this);}catch(SecurityException e){} return START_STICKY; }
 Notification notification(){String ch="gps"; NotificationManager n=(NotificationManager)getSystemService(NOTIFICATION_SERVICE); if(Build.VERSION.SDK_INT>=26)n.createNotificationChannel(new NotificationChannel(ch,"Ubicación",NotificationManager.IMPORTANCE_LOW));
  return new Notification.Builder(this,ch).setContentTitle("TopoControl").setContentText("Ubicación de cuadrilla activa cada hora").setSmallIcon(android.R.drawable.ic_menu_mylocation).build();}
 public void onLocationChanged(Location l){db.log(crew,System.currentTimeMillis(),l.getLatitude(),l.getLongitude(),l.getAccuracy(),"AUTO_HORA");}
 public void onProviderEnabled(String s){} public void onProviderDisabled(String s){} public void onStatusChanged(String s,int a,Bundle b){}
 public IBinder onBind(Intent i){return null;}
}
