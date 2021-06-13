package com.example.weather;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class NorthChina extends Fragment implements Runnable{
    SimpleAdapter simpleAdapter;
    private static final String TAG = "NorthChina";
    Handler handler;
    private ArrayList<HashMap<String, String>> rateList;
    private int msgWhat=6;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread t = new Thread((Runnable) this);
        t.start();

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == msgWhat) {
                    List<HashMap<String, String>> list2 = (List<HashMap<String, String>>) msg.obj;
                    ListView listView = getView().findViewById(R.id.my_list);
                    simpleAdapter = new SimpleAdapter(getActivity(), list2, R.layout.list, new String[]{"place", "temp"}, new int[]{R.id.place, R.id.temp});
                    listView.setAdapter(simpleAdapter);
                }
                super.handleMessage(msg);

            }


        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_main, container, false);

    }


    @Override
    public void run() {
        Log.i(TAG, "run:..........");
        boolean marker=false;
        URL url = null;
        try {
            rateList = new ArrayList<HashMap<String, String>>();
            Thread.sleep(3000);
            Document doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Log.i(TAG, "run:time=" + doc.title());//获取时间

            Element publicTime = doc.getElementsByClass("time").first();
            Log.i(TAG, "run:time=" + publicTime.html());

            Element tables = doc.getElementsByTag("table").first();
            Log.i(TAG, "run:" + tables);
            Elements trs = tables.getElementsByTag("tr");
            Log.i(TAG, "sss" + trs);
            for (Element td : trs) {
                Elements tds = td.getElementsByTag("td");
                if (tds.size() > 0) {
                    String str = tds.first().text();
                    Log.i(TAG, "run:td=" + str);

                    String val = tds.get(5).text();
                    Log.i(TAG, "run:rate=" + val);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("place", str);
                    map.put("temp", val);
                    rateList.add(map);
                    Log.i(TAG, "run:str" + "=>" + rateList);

                }
                marker=true;
//                    RateManager rateManager=new RateManager(MyPerfectProject.this);
//                    rateManager.deleteAll();
//                    Log.i("db","删除所有记录");
//                    rateManager.addAll(rateList);
//                    Log.i("db","添加新记录集");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Message msg=handler.obtainMessage();
        msg.what=msgWhat;



        if(marker){
            msg.arg1=1;

        }else{
            msg.arg1=0;
        }

        msg.obj=rateList;
        handler.sendMessage(msg);
    }
}



