package com.careyun.voiceassistant.map.Search.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.careyun.voiceassistant.R;

import java.util.List;


/**
 * Created by Huangmq on 2017/3/22.
 */

public class PoiAdpter extends ArrayAdapter<MyPoiResult> {

    private int resourceID;


    public PoiAdpter(Context context, int textViewResourceID, List<MyPoiResult> objects){
        super(context,textViewResourceID,objects);

        resourceID = textViewResourceID;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyPoiResult poiResult = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView ==null){
            view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.mNum = (TextView) view.findViewById(R.id.poi_num);
            viewHolder.mRes = (TextView) view.findViewById(R.id.poi_res);
            viewHolder.mAdress = (TextView) view.findViewById(R.id.poi_adress);
            viewHolder.mDistance = (TextView) view.findViewById(R.id.poi_distance);
            viewHolder.mMeasure = (TextView) view.findViewById(R.id.poi_measure);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();

        }

/*        if (convertView ==null){
            view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
        }
        else {
            view = convertView;
        }*/
//        View view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
/*        TextView mNum = (TextView) view.findViewById(R.id.poi_num);
        TextView mRes = (TextView) view.findViewById(R.id.poi_res);
        TextView mAdress = (TextView) view.findViewById(R.id.poi_adress);
        TextView mDistance = (TextView) view.findViewById(R.id.poi_distance);
        TextView mMeasure = (TextView) view.findViewById(R.id.poi_measure);*/

        viewHolder.mNum.setText(String.valueOf(position+1));
//        viewHolder.mNum.setText(poiResult.getmNum());
        viewHolder.mRes.setText(poiResult.getmResult());
        viewHolder.mAdress.setText(poiResult.getmAdress());
        viewHolder.mDistance.setText(poiResult.getmDistance());
        viewHolder.mMeasure.setText(poiResult.getmMeasure());
        return view;
    }

    class ViewHolder{
        TextView mNum;
        TextView mRes;
        TextView mAdress;
        TextView mDistance;
        TextView mMeasure;
    }

}
