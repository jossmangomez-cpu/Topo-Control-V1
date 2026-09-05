package com.topocontrol15;
import android.Manifest; import android.app.*; import android.os.*; import android.content.*; import android.content.pm.PackageManager; import android.location.*; import android.graphics.Color; import android.view.*; import android.widget.*; import java.text.*; import java.util.*;
public class MainActivity extends Activity {
 DB db; LinearLayout list; TextView stats;
 ArrayList<Crew> crews=new ArrayList<>();
 static class Crew{int id;String name,top,chain,site;Crew(int i,String n,String t,String c,String s){id=i;name=n;top=t;chain=c;site=s;}}
 TextView tv(String s,int z){TextView t=new TextView(this);t.setText(s);t.setTextSize(z);t.setPadding(22,16,22,16);return t;}
 public void onCreate(Bundle b){super.onCreate(b);db=new DB(this);load();request();ui();}
 void request(){if(Build.VERSION.SDK_INT>=23&&checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},10);}
 void load(){android.database.Cursor c=db.getReadableDatabase().rawQuery("SELECT id,name,top,chain,site FROM crews WHERE active=1 ORDER BY id",null);while(c.moveToNext())crews.add(new Crew(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4)));c.close();}
 void ui(){LinearLayout r=new LinearLayout(this);r.setOrientation(LinearLayout.VERTICAL);r.setPadding(10,10,10,10);
  TextView h=tv("TOPOCONTROL",25);h.setTextColor(Color.rgb(21,101,192));r.addView(h);stats=tv("",15);r.addView(stats);
  Button map=new Button(this);map.setText("MAPA / UBICACIONES");r.addView(map);map.setOnClickListener(v->mapDialog());
  LinearLayout bar=new LinearLayout(this); Button add=new Button(this);add.setText("+ CUADRILLA");Button pay=new Button(this);pay.setText("NÓMINA");bar.addView(add,new LinearLayout.LayoutParams(0,-2,1));bar.addView(pay,new LinearLayout.LayoutParams(0,-2,1));r.addView(bar);
  add.setOnClickListener(v->addCrew());pay.setOnClickListener(v->payroll());
  ScrollView s=new ScrollView(this);list=new LinearLayout(this);list.setOrientation(LinearLayout.VERTICAL);s.addView(list);r.addView(s,new LinearLayout.LayoutParams(-1,0,1));setContentView(r);refresh();}
 void refresh(){list.removeAllViews();stats.setText("Cuadrillas: "+crews.size()+" | 30 trabajadores iniciales | Jornada 07:00–16:00 | GPS cada 60 min | Radio 2 km");
  for(Crew c:crews){TextView x=tv(c.name+"  🟢\n"+c.site+" | "+c.top+" + "+c.chain+"\nEntrada 07:00 · HE: después de 16:00",16);x.setBackgroundColor(0xFFF4F7FA);list.addView(x);x.setOnClickListener(v->crew(c));}}
 void crew(Crew c){new AlertDialog.Builder(this).setTitle(c.name).setMessage("Topógrafo: "+c.top+"\nCadenero: "+c.chain+"\nObra: "+c.site+"\nGeocerca: 2 km\nGPS: cada hora\n\nTarifas: Topógrafo $45/día + $7 HE\nCadenero $28/día + $4.50 HE\nRenta: 10%")
  .setPositiveButton("INICIAR GPS",(d,w)->startGps(c.id)).setNegativeButton("Cerrar",null).show();}
 void startGps(int id){if(Build.VERSION.SDK_INT>=23&&checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){request();return;}Intent i=new Intent(this,LocationService.class);i.putExtra("crew",id);if(Build.VERSION.SDK_INT>=26)startForegroundService(i);else startService(i);Toast.makeText(this,"GPS activo: registro cada hora",Toast.LENGTH_SHORT).show();}
 void mapDialog(){new AlertDialog.Builder(this).setTitle("Mapa de cuadrillas").setMessage("La V2 almacena la última coordenada de cada cuadrilla y valida la geocerca de 2 km. Para mostrar mapa cartográfico en producción se puede conectar un proveedor de mapas (Google Maps u OpenStreetMap).").setPositiveButton("OK",null).show();}
 void addCrew(){final EditText e=new EditText(this);e.setHint("Nombre, ej. C-016");new AlertDialog.Builder(this).setTitle("Nueva cuadrilla").setView(e).setPositiveButton("Agregar",(d,w)->{int id=crews.size()+1;String n=e.getText().toString();if(n.trim().isEmpty())n="C-"+String.format("%03d",id);db.getWritableDatabase().execSQL("INSERT INTO crews VALUES(?,?,?,?,?,1)",new Object[]{id,n,"—","—","Obra A"});crews.add(new Crew(id,n,"—","—","Obra A"));refresh();}).setNegativeButton("Cancelar",null).show();}
 void payroll(){double daily=73*crews.size();double net=daily*.90;new AlertDialog.Builder(this).setTitle("Nómina base diaria").setMessage(String.format(Locale.US,"Bruto: $%.2f\nRenta 10%%: $%.2f\nNeto: $%.2f\n\nTopógrafo: $45 + $7/h HE\nCadenero: $28 + $4.50/h HE\n\nEl módulo quincenal sumará jornadas y horas extras registradas.",daily,daily*.10,net)).setPositiveButton("OK",null).show();}
}
