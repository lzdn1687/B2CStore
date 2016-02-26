package cn.mstar.store.activity;

import java.util.ArrayList;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.interfaces.MstarLocationListener;
import cn.mstar.store.adapter.StoreAdapter;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.entity.MstarGeoPoint;
import cn.mstar.store.entity.StoreInfo;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.L;
import cn.mstar.store.customviews.MarqueeText;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 附近门店
 * @author wenjundu 2015-7-16
 *
 */
public class NearStoreActivity extends BaseActivity implements OnClickListener{
	private LocationClient mLocationClient;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;//定位精度
	private MarqueeText locationTV; //显示位置文字
	private TextView refreshBtn;
	private MyApplication application;
	private ImageView titleBack;//返回
	private TextView titleName;//标题
	private ListView listView; //门店信息list
	private List<StoreInfo> list;
	private StoreAdapter adapter;//门店信息adapter
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby_store);
		Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
		Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
		application=((MyApplication)getApplication());
		application.addActivity(this);
		mLocationClient=application.mLocationClient;
		initView();
		initListener();
		initData();
		InitLocation();
		mLocationClient.start();
		
	}
	//初始化数据
	private void initData() {
		// TODO Auto-generated method stub
		list=new ArrayList<StoreInfo>();
		for(int i=0;i<4;i++){
			StoreInfo storeInfo=new StoreInfo("", "广东省深圳市罗湖区翠竹路直营店", "22.22km", "营业时间:06:00--22:00", "联系电话:0755-88888888",24.323235,28.888888);
			list.add(storeInfo);
		}
		adapter=new StoreAdapter(this, list);
		listView.setAdapter(adapter);
	}
	//初始化监听
	private void initListener() {
		// TODO Auto-generated method stub
		refreshBtn.setOnClickListener(this);
		titleBack.setOnClickListener(this);
		application.setMstarLocationListener(new MstarLocationListener() {
			
			@Override
			public void successed(MstarGeoPoint point) {
				// TODO Auto-generated method stub
				
				locationTV.setText(point.getAddress());
				mLocationClient.stop();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(NearStoreActivity.this,NavigationActivity.class);
				startActivity(intent);
			}
		});
	}
	private void initView() {
		// TODO Auto-generated method stub
		locationTV=(MarqueeText) findViewById(R.id.tv_location);
		refreshBtn=(TextView) findViewById(R.id.tv_refresh);
		titleBack=(ImageView) findViewById(R.id.title_back);
		titleName=(TextView) findViewById(R.id.title_name);
		listView=(ListView) findViewById(R.id.store_list);
		
		titleBack.setVisibility(View.VISIBLE);
		titleName.setText(getString(R.string.near_store));

	}
	//初始化定位参数
	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType("gcj02");//返回的定位结果是百度经纬度，默认值gcj02

		option.setScanSpan(1000);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyApplication.requestQueue.cancelAll(this);
		super.onDestroy();
		L.e("onDestroy");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_refresh://重新定位
			InitLocation();
			locationTV.setText(getString(R.string.locationing));
			mLocationClient.start();
			
			break;

		case R.id.title_back:
			finish();
			break;
		}
	}
	
}
