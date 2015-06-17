package com.zeroapp.parking.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import com.zeroapp.parking.R;
import com.zeroapp.parking.locator.Locator;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 * 
 */
public class LocationActivity extends Activity {

	MapView mMapView;
	BaiduMap mBaiduMap;
    private Button btnStartParking;
	// UI相关
	OnCheckedChangeListener radioButtonListener;
	Button requestLocButton;
	boolean isFirstLoc = true;// 是否首次定位
    private BDLocation l;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);

        btnStartParking = (Button) findViewById(R.id.start_parking);
        btnStartParking.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Locator.getInstance(getApplicationContext()).rememberLocation(l);
                finish();
            }
        });
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
        // 设置模式和图标
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(LocationMode.NORMAL, true, null));
        // 位置信息初始化
        l = Locator.getInstance(this).getLocation();
        MyLocationData locData = new MyLocationData.Builder().accuracy(l.getRadius()).direction(l.getDirection()).latitude(l.getLatitude()).longitude(l.getLongitude()).build();
        // 设置位置信息
        mBaiduMap.setMyLocationData(locData);
        // 定位到当前位置
        LatLng ll = new LatLng(l.getLatitude(), l.getLongitude());
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(u);
    }

    public void onNewLocation(BDLocation l) {

    }
	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

}
