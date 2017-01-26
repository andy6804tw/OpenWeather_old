package com.openweather.openweather;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Toast.makeText(context,mPosition+" "+viewType+" ",Toast.LENGTH_SHORT).show();
        if (viewType == TYPE_HEADER) {
            return new HeaderHolder(LayoutInflater.from(context).inflate(R.layout.rv_header_weather_now, parent, false));
        }
         else if(viewType==1)
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item1, parent, false));
         else if(viewType==2)
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item2, parent, false));
        else if(viewType==9)
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item10, parent, false));
        else
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_weather_now, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }
}
