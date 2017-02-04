package com.openweather.openweather.WeatherNow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.openweather.openweather.MainActivity;
import com.openweather.openweather.R;

/**
 * Created by andy6804tw on 2017/1/25.
 */

public class WeatherNowRVA extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private static final int ITEM_COUNT = 10;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    int mPosition=0;

    public WeatherNowRVA(Context context) {
        this.context = context;
    }


    class ViewHolder extends RecyclerView.ViewHolder{



        public ViewHolder(View itemView,int viewType) {
            super(itemView);
            //if(viewType==0){

           // }
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView tv_temp;
        public HeaderHolder(View itemView) {
            super(itemView);
            tv_temp=(TextView)itemView.findViewById(R.id.tv_temp);
            tv_temp.setText(MainActivity.mTemp);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Toast.makeText(context,mPosition+" "+viewType+" ",Toast.LENGTH_SHORT).show();
        if (viewType == TYPE_HEADER) {
            return new HeaderHolder(LayoutInflater.from(context).inflate(R.layout.rv_header_weather_now, parent, false));
        }
         else if(viewType==1)
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item1, parent, false),viewType);
         else if(viewType==2)
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item2, parent, false),viewType);
        else if(viewType==9)
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item10, parent, false),viewType);
        else
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_weather_now, parent, false),viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        mPosition=position;

    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return position;
        }

    }
}
