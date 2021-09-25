package org.techtown.letseat.map;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.R;
import org.techtown.letseat.map.Background;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Naver_map extends AppCompatActivity {

    Background task;
    Background1 Gu_task;
    Background2 Gun_task;

    JSONArray jsonArray;

    JSONObject city;
    JSONObject gu;
    JSONObject n_x,n_y;

    ArrayList<String> arrayList, arrayList2, arrayList3;
    ArrayAdapter<String> arrayAdapter, arrayAdapter2, arrayAdapter3;

    public static int TO_GRID = 0;
    public static int TO_GPS = 1;
    LatXLngY tmp_locate;

    class LatXLngY{
        public double lat;
        public double lng;

        public double x;
        public double y;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naver_map);

        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup)findViewById(R.id.MapView);
        mapViewContainer.addView(mapView);

        Button btn1 = (Button) findViewById(R.id.button);
        final String[] locate = new String[1];

        mapView.setDaumMapApiKey("ddaa91d6b27135fb6cb250e27690c59d");

        final Spinner sp1 = (Spinner) findViewById(R.id.spinner);
        final Spinner sp2 = (Spinner) findViewById(R.id.spinner2);
        final Spinner sp3 = (Spinner) findViewById(R.id.spinner3);

        final String url = "https://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt";

        city = new JSONObject();
        gu = new JSONObject();

        task = new Background();

        try {
            jsonArray = task.execute(url).get();
            arrayList = new ArrayList<>();
            arrayList.add("선택");

            for(int i = 0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrayList.add(jsonObject.getString("value"));

                city.put(jsonObject.getString("value"),jsonObject.getString("code"));
            }
            arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_spinner_dropdown_item, arrayList);

            sp1.setAdapter(arrayAdapter);
        }
        catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        final int[] now1 = new int[1];
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            JSONArray Gu_jsonArray;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = parent.getItemAtPosition(position).toString();
                now1[0] = position;
                System.out.println("####"+s);
                Gu_task = new Background1();
                try {
                    Gu_jsonArray = Gu_task.execute("https://www.kma.go.kr/DFSROOT/POINT/DATA/mdl."+city.getString(s)+".json.txt").get();
                    arrayList2 = new ArrayList<>();
                    arrayList2.add("선택");

                    for(int i = 0; i<Gu_jsonArray.length(); i++){
                        JSONObject jsonObject = Gu_jsonArray.getJSONObject(i);
                        arrayList2.add(jsonObject.getString("value"));

                        gu.put(jsonObject.getString("value"),jsonObject.getString("code"));
                    }

                    arrayAdapter2 = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, arrayList2);

                    sp2.setAdapter(arrayAdapter2);
                }
                catch (InterruptedException | ExecutionException e){
                    e.printStackTrace();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final int[] now2 = new int[1];
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            JSONArray Gun_jsonArray;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = parent.getItemAtPosition(position).toString();

                now2[0] = position;

                Gun_task = new Background2();

                n_x = new JSONObject();
                n_y = new JSONObject();

                try {
                    Gun_jsonArray = Gun_task.execute("https://www.kma.go.kr/DFSROOT/POINT/DATA/leaf."+gu.getString(s)+".json.txt").get();
                    arrayList3 = new ArrayList<>();
                    arrayList3.add("선택");

                    for(int i = 0; i<Gun_jsonArray.length(); i++){
                        JSONObject jsonObject = Gun_jsonArray.getJSONObject(i);
                        arrayList3.add(jsonObject.getString("value"));

                        n_x.put(jsonObject.getString("value"),jsonObject.getString("x"));
                        n_y.put(jsonObject.getString("value"),jsonObject.getString("y"));
                    }

                    arrayAdapter3 = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, arrayList3);

                    sp3.setAdapter(arrayAdapter3);
                }
                catch (InterruptedException | ExecutionException e){
                    e.printStackTrace();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final int[] now3 = new int[1];
        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locate[0] = parent.getItemAtPosition(position).toString();
                now3[0] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((now1[0] == 0)||(now2[0] == 0)|| now3[0] == 0){
                    Toast.makeText(getApplicationContext(),"모두 입력해주세요.",Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        tmp_locate = convertGRID_GPS(TO_GPS, n_x.getDouble(locate[0]), n_y.getDouble(locate[0]));

                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(tmp_locate.lat, tmp_locate.lng);

                        mapView.setMapCenterPoint(mapPoint,true);

                        MapPOIItem marker = new MapPOIItem();
                        marker.setMapPoint(mapPoint);
                        marker.setItemName("현재위치");
                        marker.setTag(0);

                        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                        mapView.addPOIItem(marker);
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y){
        double RE = 6371.00877;
        double GRID = 5.0;
        double SLAT1 = 30.0;
        double SLAT2 = 60.0;
        double OLON = 126.0;
        double OLAT = 38.0;
        double XO = 43;
        double YO = 136;

        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf,sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro,sn);
        LatXLngY rs = new LatXLngY();

        if(mode == TO_GRID){
            rs.lat = lat_X;
            rs.lng = lng_Y;
            double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra,sn);
            double theta = lng_Y * DEGRAD -olon;
            if( theta > Math.PI)theta -= 2.0 * Math.PI;
            if( theta < -Math.PI)theta += 2.0 * Math.PI;
            theta *= sn;
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        }
        else {
            rs.lat = lat_X;
            rs.lng = lng_Y;
            double xn = lat_X - XO;
            double yn = ro - lng_Y + YO;
            double ra = Math.sqrt(xn * xn + yn * yn);
            if(sn < 0.0){
                ra = -ra;
            }
            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta = 0.0;
            if(Math.abs(xn) <= 0.0){
                theta = 0.0;
            }
            else {
                if(Math.abs(yn) <= 0.0){
                    theta = Math.PI * 0.5;
                    if(xn < 0.0){
                        theta = -theta;
                    }
                }
                else theta = Math.atan2(xn,yn);
            }
            double alon = theta / sn + olon;
            rs.lat = alat * RADDEG;
            rs.lng = alon * RADDEG;
        }
        return rs;
    }
}

