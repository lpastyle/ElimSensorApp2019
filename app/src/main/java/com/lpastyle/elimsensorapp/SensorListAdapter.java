package com.lpastyle.elimsensorapp;

import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SensorListAdapter extends BaseAdapter {

    private ViewHolder mViewHolder;
    private List<Sensor> mSensors;
    private Context mContext;


    public SensorListAdapter(Context context, List<Sensor> sensors) {
        mContext = context;
        mSensors = sensors;
    }

    @Override
    public int getCount() {
        return mSensors.size();
    }

    @Override
    public Object getItem(int position) {
        return mSensors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.sensor_list_item, viewGroup, false);
            mViewHolder = new ViewHolder(view);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        Sensor sensor = mSensors.get(position);
        mViewHolder.nameTv.setText(sensor.getName());
        mViewHolder.typeTv.setText(sensor.getStringType());
        mViewHolder.resolutionTv.setText(String.valueOf(sensor.getResolution()));
        mViewHolder.maxRangeTv.setText(String.valueOf(sensor.getMaximumRange()));


        return view;
    }

    private class ViewHolder {

        TextView nameTv;
        TextView typeTv;
        TextView resolutionTv;
        TextView maxRangeTv;

        public ViewHolder(View v) {

            nameTv = v.findViewById(R.id.nameTv);
            typeTv = v.findViewById(R.id.typeTv);
            resolutionTv = v.findViewById(R.id.resolutionTv);
            maxRangeTv = v.findViewById(R.id.maxRangeTv);

        }
    }


}
