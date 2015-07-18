package com.mapia.map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mapia.MainApplication;
import com.mapia.R;
import com.mapia.network.RestRequestHelper;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapPublicFragment extends MapFragment {
	
	Button btnLocCurrent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		((MainApplication)getActivity().getApplication()).setMapPublicFragment(this);
		this.type_num = 2;
		LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout relLayout = (RelativeLayout)inflater.inflate(R.layout.overview_map_public, null);
		RelativeLayout.LayoutParams paramrel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
		getActivity().getWindow().addContentView(relLayout, paramrel);
		Toast.makeText(getActivity().getApplicationContext(), "PublicFragment", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(MapActivity.txtMapName!=null) MapActivity.txtMapName.setText("Public");
		for(int i=0;i<markerDatas.size();i++){
			markerDatas.get(i).marker.remove();
		}
		markerDatas.clear();
		getMarker();
		drawMarker(markerDatas);
	}

	public void getMarker() {
		final ArrayList<MarkerData> markerList = new ArrayList<MarkerData>();
		try {
			RestRequestHelper requestHelper = RestRequestHelper.newInstance();

			requestHelper.getPosts("public", MapActivity.cameraLatlng.latitude, MapActivity.cameraLatlng.longitude, MapActivity.cameraZoom,
					new Callback<JsonObject>() {
						@Override
						public void success(JsonObject jO, Response response) {
							JsonArray jsonArray = jO.get("posts").getAsJsonArray();
							Toast.makeText(getActivity(), "Public 글 읽어오기 성공".toString(), Toast.LENGTH_LONG).show();
							for (int i = 0; i < jsonArray.size(); i++) {
								JsonObject jsonObject = (JsonObject) jsonArray.get(i);
								MarkerData markerData;
								if(jsonObject.has("image")){

									markerData = new MarkerData(new LatLng(jsonObject.get("lat").getAsDouble(),
											jsonObject.get("lng").getAsDouble()),
											jsonObject.get("content").getAsString(),
											jsonObject.get("username").getAsString());
									//add image to parameter
								}
								else {
									markerData = new MarkerData(new LatLng(jsonObject.get("lat").getAsDouble(),
											jsonObject.get("lng").getAsDouble()),
											jsonObject.get("content").getAsString(),
											jsonObject.get("username").getAsString());
								}
								markerList.add(markerData);
							}
							markerDatas = markerList;
							drawMarker(markerDatas);
						}

						@Override
						public void failure(RetrofitError error) {
							Toast.makeText(getActivity(), "글 읽어오기실패".toString(), Toast.LENGTH_LONG).show();
							error.printStackTrace();
						}
					});
		}  catch (Exception e) {
			e.printStackTrace();
		}
	}
}
