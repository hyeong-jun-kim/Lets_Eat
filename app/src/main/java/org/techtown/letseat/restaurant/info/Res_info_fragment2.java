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
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.R;

import java.util.List;

public class Res_info_fragment2 extends Fragment implements OnMapReadyCallback {

    int resId, getResId;
    private GoogleMap map;
    private TextView place,resinfo,resName,opening_hours,phonenum;
    String location,s_resinfo,s_resName,s_opening_hours,s_phonenum, text, url;





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

        Bundle extra = this.getArguments();
        if (extra != null) {
            text = extra.getString("text");
            resId = extra.getInt("send_resId");
            Log.d("ds","ds");
            get_Restaurant();
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

        // ???????????? ?????? ????????? ?????? ??????
        LatLng place = new LatLng(address.getLatitude(), address.getLongitude());


        // ???????????? ????????? ????????? ?????? ?????? ??????
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions
                .position(place)
                .title("?????? ??????????????? ????????? ?????? ?????? ????????? ?????? ??? ????????????.")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .alpha(0.5f); // ????????? ????????? ?????????

        // ????????? ????????????
        map.addMarker(markerOptions); // .showInfoWindow() ??? ?????? ???????????? ????????? ??????????????? ??????

        // ????????? ???????????????
        map.setOnInfoWindowClickListener(infoWindowClickListener);

        // ?????? ?????? ?????????
        map.setOnMarkerClickListener(markerClickListener);

        // ???????????? ????????? ????????? ????????????
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 16));

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
    }

    // ????????? ?????? ?????????
    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            String markerId = marker.getId();
        }
    };

    // ?????? ?????? ?????????
    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            String markerId = marker.getId();
            // ????????? ????????? ??????
            LatLng location = marker.getPosition();
            return false;
        }
    };

    void get_Restaurant() {

        if(text.equals("chineseFood")){
            url = "http://125.132.62.150:8000/letseat/store/findRestaurant?restype=chineseFood";
        }
        else if(text.equals("koreanFood")){
            url = "http://125.132.62.150:8000/letseat/store/findRestaurant?restype=koreanFood";
        }
        else if(text.equals("japaneseFood")){
            url = "http://125.132.62.150:8000/letseat/store/findRestaurant?restype=japaneseFood";
        }
        else if(text.equals("westernFood")){
            url = "http://125.132.62.150:8000/letseat/store/findRestaurant?restype=westernFood";
        }
        else if(text.equals("onemanFood")){
            url = "http://125.132.62.150:8000/letseat/store/find/aloneAble";
        }
        else if(text.equals("All")){
            url = "http://125.132.62.150:8000/letseat/store/findAll";
        }
        else if(text !=null){
            url = "http://125.132.62.150:8000/letseat/store/searchRestaurant?name="+text;
        }

        JSONArray getData = new JSONArray();

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                getData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int res;

                            for(int i = 0; i<response.length(); i++){
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                location = jsonObject.getString("location");
                                s_resinfo = jsonObject.getString("resIntro");
                                s_resName = jsonObject.getString("resName");
                                s_opening_hours = jsonObject.getString("openTime");
                                s_phonenum = jsonObject.getString("phoneNumber");
                                getResId = jsonObject.getInt("resId");

                                if(resId == getResId){
                                    place.setText(location);
                                    resinfo.setText(s_resinfo);
                                    resName.setText(s_resName);
                                    opening_hours.setText(s_opening_hours);
                                    phonenum.setText(s_phonenum);

                                    onMapReady(map);


                                    Log.d("??????", response.toString());
                                 }
                            }


                        } catch (JSONException e) {
                            Log.d("??????",e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("??????", error.toString());
                    }
                }
        );
        request.setShouldCache(false); // ?????? ?????? ????????? ?????? ????????? ????????? ?????????
        AppHelper.requestQueue = Volley.newRequestQueue(getActivity()); // requsetQueue ?????????
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
                @Override // ?????? ??? ????????? ???
                public void onResponse(JSONObject response) {
                    try {
                        sample = response.get("resIntro").toString();
                        sample_text.setText(sample);
                        Log.d("??????", String.valueOf(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override // ?????? ?????? ???
                public void onErrorResponse(VolleyError error) {
                    Log.d("??????",error.toString());
                }
            }
    );
        request.setShouldCache(false); // ?????? ?????? ????????? ?????? ????????? ????????? ?????????
                AppHelper.requestQueue = Volley.newRequestQueue(getActivity()); // requsetQueue ?????????
                AppHelper.requestQueue.add(request);
                }
                }
*/