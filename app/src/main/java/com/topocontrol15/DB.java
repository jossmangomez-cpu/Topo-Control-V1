package com.topocontrol15;
import android.content.*; import android.database.sqlite.*; import java.util.*;
public class DB extends SQLiteOpenHelper {
 public DB(Context c){super(c,"topocontrol.db",null,1);}
 public void onCreate(SQLiteDatabase d){
  d.execSQL("CREATE TABLE crews(id INTEGER PRIMARY KEY, name TEXT, top TEXT, chain TEXT, site TEXT, active INTEGER)");
  d.execSQL("CREATE TABLE logs(id INTEGER PRIMARY KEY AUTOINCREMENT, crew INTEGER, ts INTEGER, lat REAL, lon REAL, acc REAL, type TEXT)");
  d.execSQL("CREATE TABLE shifts(id INTEGER PRIMARY KEY AUTOINCREMENT, crew INTEGER, start INTEGER, end INTEGER, approved INTEGER DEFAULT 0)");
  d.execSQL("CREATE TABLE worksites(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, lat REAL, lon REAL, radius REAL)");
  for(int i=1;i<=15;i++) d.execSQL("INSERT INTO crews VALUES(?,?,?,?,?,1)",new Object[]{i,"C-"+String.format("%03d",i),"—","—","Obra A"});
 }
 public void onUpgrade(SQLiteDatabase d,int o,int n){}
 public void log(int c,long t,double lat,double lon,float acc,String type){getWritableDatabase().execSQL("INSERT INTO logs(crew,ts,lat,lon,acc,type) VALUES(?,?,?,?,?,?)",new Object[]{c,t,lat,lon,acc,type});}
}
