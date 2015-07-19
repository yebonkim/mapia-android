package com.mapia.setting;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapia.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProfSettingActivity extends Activity{

    private ListView lvProfSetting1, lvProfSetting2, lvProfSetting3;
    private ProfSettingLvAdapter adapterProfSetting1, adapterProfSetting2, adapterProfSetting3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_setting);
        lvProfSetting1 = (ListView) findViewById(R.id.lvProfSetting1);
        lvProfSetting2 = (ListView) findViewById(R.id.lvProfSetting2);
        lvProfSetting3 = (ListView) findViewById(R.id.lvProfSetting3);
        adapterProfSetting1 = new ProfSettingLvAdapter(this);
        adapterProfSetting2 = new ProfSettingLvAdapter(this);
        adapterProfSetting3 = new ProfSettingLvAdapter(this);
        lvProfSetting1.setAdapter(adapterProfSetting1);
        lvProfSetting2.setAdapter(adapterProfSetting2);
        lvProfSetting3.setAdapter(adapterProfSetting3);

        adapterProfSetting1.addItem("별명", "후꾸선장");
        adapterProfSetting1.addItem("아이디", "hookoorookoo");
        adapterProfSetting1.addItem("소개","히으힣으힝흥히읗이");

        adapterProfSetting2.addItem("이메일","caphuku@gmail.com");
        adapterProfSetting2.addItem("핸드폰", "01047147870");

        adapterProfSetting3.addItem("비밀번호 변경", "");
        adapterProfSetting3.addItem("로그아웃", "");

        lvProfSetting1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProfSettingListData mData = adapterProfSetting1.mListData.get(position);
                Toast.makeText(ProfSettingActivity.this, mData.txtTitle, Toast.LENGTH_SHORT).show();
            }
        });

        lvProfSetting2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProfSettingListData mData = adapterProfSetting2.mListData.get(position);
                Toast.makeText(ProfSettingActivity.this, mData.txtTitle, Toast.LENGTH_SHORT).show();
            }
        });

        lvProfSetting3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProfSettingListData mData = adapterProfSetting3.mListData.get(position);
                Toast.makeText(ProfSettingActivity.this, mData.txtTitle, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private class ViewHolder{
        public TextView titleText, mainText;
    }

    private class ProfSettingLvAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ProfSettingListData> mListData = new ArrayList<ProfSettingListData>();

        public ProfSettingLvAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(String txtTitle, String txtMain){
            ProfSettingListData addData = new ProfSettingListData();
            addData.txtMain = txtMain;
            addData.txtTitle = txtTitle;
            mListData.add(addData);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if(mListData.get(position).txtMain!="") {
                    convertView = inflater.inflate(R.layout.item_listview_profile_setting, null);
                }
                else convertView = inflater.inflate(R.layout.item_listview_profile_setting1,null);

                holder.mainText = (TextView) convertView.findViewById(R.id.itemTxtMain);
                holder.titleText = (TextView) convertView.findViewById(R.id.itemTxtTitle);

                convertView.setTag(holder);

            ProfSettingListData mData = mListData.get(position);
            holder.mainText.setText(mData.txtMain);
            holder.titleText.setText(mData.txtTitle);
            return convertView;
        }
    }
}