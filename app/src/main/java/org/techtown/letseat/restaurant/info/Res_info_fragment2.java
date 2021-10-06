package org.techtown.letseat.restaurant.info;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.AppHelper;
import org.techtown.letseat.R;

import java.util.List;

public class Res_info_fragment2 extends Fragment implements OnMapReadyCallback {

    int resId;
    private GoogleMap map;
    private TextView place,resinfo,resName,opening_hours,phonenum;
    String location,s_resinfo,s_resName,s_opening_hours,s_phonenum;;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.res_info_fragment2, container, false);

        place = view.findViewById(R.id.place);
        resinfo = view.findViewById(R.id.resinfo);
        resName = view.findViewById(R.id.resName);
        opening_hours = view.findViewById(R.id.opening_hours);
        phonenum = view.findViewById(R.id.phonenum);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.shop_map);
        mapFragment.getMapAsync(this);
        get_Restaurant();

        Bundle extra = this.getArguments();
        if (extra != null) {
            resId = extra.getInt("ap");
        }

        return view;
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        String searchPlace = place.getText().toString();
        Geocoder geocoder = new Geocoder(getActivity().getBaseContext());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocationName(searchPlace, 3);
            if (addresses != null && !addresses.equals(" ")) {
                oneMarker(addresses);
                ;
            }
        } catch (Exception e) {
        }
    }


    public void oneMarker(List<Address> addresses) {

        Address address = addresses.get(0);

        // 텍스트로 부터 받아온 위치 설정
        LatLng place = new LatLng(address.getLatitude(), address.getLongitude());


        // 구글맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions
                .position(place)
                .title("지도 우측하단의 버튼을 통해 구글 맵에서 보실 수 있습니다.")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .alpha(0.5f); // 알파는 좌표의 투명도

        // 마커를 생성한다
        map.addMarker(markerOptions); // .showInfoWindow() 를 쓰면 처음부터 마커에 상세정보가 뜬다

        // 정보창 클릭리스너
        map.setOnInfoWindowClickListener(infoWindowClickListener);

        // 마커 클릭 리스너
        map.setOnMarkerClickListener(markerClickListener);

        // 카메라를 여의도 위치로 옮겨준다
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 16));

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
    }

    // 정보창 클릭 리스너
    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            String markerId = marker.getId();
        }
    };

    // 마커 클릭 리스너
    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            String markerId = marker.getId();
            // 선택한 타겟의 위치
            LatLng location = marker.getPosition();
            return false;
        }
    };

    void get_Restaurant() {
        String url = "http://125.132.62.150:8000/letseat/store/findAll";



        JSONArray getData = new JSONArray();

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                getData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                                JSONObject jsonObject = (JSONObject) response.get(resId);
                                location = jsonObject.getString("location");
                                s_resinfo = jsonObject.getString("resIntro");
                                s_resName = jsonObject.getString("resName");
                                s_opening_hours = jsonObject.getString("openTime");
                                s_phonenum = jsonObject.getString("phoneNumber");

                                place.setText(location);
                                resinfo.setText(s_resinfo);
                                resName.setText(s_resName);
                                opening_hours.setText(s_opening_hours);
                                phonenum.setText(s_phonenum);

                                onMapReady(map);

                            Log.d("응답", response.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("에러", error.toString());
                    }
                }
        );
        request.setShouldCache(false); // 이전 결과 있어도 새로 요청해 응답을 보내줌
        AppHelper.requestQueue = Volley.newRequestQueue(getActivity()); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }
}

/*
    JSONObject getD = new JSONObject();
    JsonObjectRequest requ = new JsonObjectRequest(
            Request.Method.GET,
            url,
            getData,
            new Response.Listener<JSONObject>() {
                @Override // 응답 잘 받았을 때
                public void onResponse(JSONObject response) {
                    try {
                        sample = response.get("resIntro").toString();
                        sample_text.setText(sample);
                        Log.d("응답", String.valueOf(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override // 에러 발생 시
                public void onErrorResponse(VolleyError error) {
                    Log.d("에러",error.toString());
                }
            }
    );
        request.setShouldCache(false); // 이전 결과 있어도 새로 요청해 응답을 보내줌
                AppHelper.requestQueue = Volley.newRequestQueue(getActivity()); // requsetQueue 초기화
                AppHelper.requestQueue.add(request);
                }
                }
*/