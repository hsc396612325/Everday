package com.example.heshu.everyday.zhihu;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heshu.everyday.R;
import com.example.heshu.everyday.common.MainActivity;
import com.example.heshu.everyday.weather.db.City;
import com.example.heshu.everyday.weather.db.County;
import com.example.heshu.everyday.weather.db.Province;
import com.example.heshu.everyday.weather.Gson.BasicList;
import com.example.heshu.everyday.common.util.App;
import com.example.heshu.everyday.common.util.HttpUtil;
import com.example.heshu.everyday.common.util.Utility;
import com.example.heshu.everyday.weather.WeatherFragment;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by heshu on 2017/11/14.
 */

public class ChooseAreaFragment extends Fragment implements TextView.OnEditorActionListener {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    public static final int LEVEL_SEARCH= 3;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<BasicList.Basic> mBasicList;
    private List<County> countyList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;
    private EditText mEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_area, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        mEditText = view.findViewById(R.id.city_edit);
        mEditText.setOnEditorActionListener(this);

        titleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setVisibility(View.VISIBLE);
                titleText.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            titleText.setVisibility(View.VISIBLE);
            mEditText.setVisibility(View.INVISIBLE);
            if (mEditText.getText().toString() != null) {
                gainLocationId(mEditText.getText().toString());
            }
            return true;
        }
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                } else if (currentLevel == LEVEL_COUNTY) {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.getContext()).edit();
                    editor.putString("WeatherId",countyList.get(position).getWeatherId());
                    Log.d("aaaa",""+countyList.get(position).getWeatherId());
                    editor.apply();
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.replaceFragment(new WeatherFragment());
                }else if(currentLevel == LEVEL_SEARCH){
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.getContext()).edit();
                    editor.putString("WeatherId",mBasicList.get(position).WeatherId);
                    Log.d("aaaa",""+mBasicList.get(position).WeatherId);
                    editor.apply();
//                    MainActivity mainActivity = (MainActivity)getActivity();
//                    mainActivity.replaceFragment(new WeatherFragment());
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                } else if (currentLevel == LEVEL_SEARCH){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    //遍历全国所有的省，优先在数据库中查询，如果没有再去服务器上面查询
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    //查询选中省的所有市，优先从数据库查询，如果没有再去服务器上查询
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }

    }

    //查询选中市的所有县，优先从数据库查询，如果没有再去服务器查询。
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }

    //根据输入的地址和类型从服务器上查询省市县的数据
    private void queryFromServer(String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOKHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //通过runOnUiThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override//响应后
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText, selectedCity.getId());
                }

                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    private void queryCounties2(List<BasicList.Basic> basicList) {
        titleText.setText("搜索结果");
        backButton.setVisibility(View.VISIBLE);

        dataList.clear();
        for (BasicList.Basic basic : basicList) {
            dataList.add(basic.cityName);
        }

        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        currentLevel = LEVEL_SEARCH;
    }

    public void gainLocationId(String location) {
        showProgressDialog();
        String key = "baccef9080f24fb895c350b8cdd72c6a";
        String weatherUrl = "https://search.heweather.com/find?location=" +
                location + "&key=" + key + "&group=cn" + "&number=30";

        HttpUtil.sendOKHttpRequest(weatherUrl, new Callback() {
            @Override//失败
            public void onFailure(Call call, IOException e) {
                closeProgressDialog();
                Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
            }

            @Override//成功
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();

                final BasicList basicList = Utility.handleBasicListResponse(responseText);
                mBasicList =  basicList.asicList;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        queryCounties2(basicList.asicList);
                    }
                });

            }
        });
    }

    //显示进度对话框
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    //关闭进度对话框
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


}

