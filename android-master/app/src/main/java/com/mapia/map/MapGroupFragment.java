package com.mapia.map;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mapia.R;
import com.mapia.network.RestRequestHelper;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapGroupFragment extends MapFragment {

	int currentGroupNumber = 0; // currentGroupNumber 수정

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.type_num = 4;
		LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout relLayout = (RelativeLayout)inflater.inflate(R.layout.overview_map_group, null);
		RelativeLayout.LayoutParams paramrel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
		getActivity().getWindow().addContentView(relLayout, paramrel);
		Toast.makeText(getActivity().getApplicationContext(), "GroupFragment", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(MapActivity.txtMapName!=null) MapActivity.txtMapName.setText("Group");
		super.mapType="group";
		getMarker();
		drawMarker(markerDatas);
	}
}
