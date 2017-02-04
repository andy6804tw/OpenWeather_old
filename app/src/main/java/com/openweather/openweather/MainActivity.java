package com.openweather.openweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.openweather.openweather.WeatherNow.WeatherNowActivity;
import com.openweather.openweather.WeatherNow.YahooWeatherAPI.City;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    class MyWeather {

        String description;
        String city;
        String region;
        String country;

        String windChill;
        String windDirection;
        String windSpeed;

        String sunrise;
        String sunset;

        String conditiontext;
        String conditiondate;
        String conditiontemp;

        String forecastdate0;
        String forecastday0;
        String forecasthigh0;
        String forecastlow0;
        String forecasttext0;

        String forecastdate1;
        String forecastday1;
        String forecasthigh1;
        String forecastlow1;
        String forecasttext1;

        public String getForecastday0() {
            return forecastday0;
        }

        public void setForecastday0(String forecastday0) {
            this.forecastday0 = forecastday0;
        }

        public String toString() {

            String s;

            s = description + " -\n\n" + "city: " + city + "\n"
                    + "region: " + region + "\n"
                    + "country: " + country + "\n\n"
                    + "Wind\n"
                    + "chill: " + windChill + "\n"
                    + "direction: " + windDirection + "\n"
                    + "speed: " + windSpeed + "\n\n"
                    + "Sunrise: " + sunrise + "\n"
                    + "Sunset: " + sunset + "\n\n"
                    + "Condition: " + conditiontext + "\n"
                    + "temp: " + conditiontemp + "\n"
                    + conditiondate + "\n"
                    + "日期: " + forecastdate0 + "\n"
                    + "day: " + forecastday0 + "\n"
                    + "high: " + forecasthigh0 + "\n"
                    + "low: " + forecastlow0 + "\n"
                    + "w: " + forecasttext0 + "\n"

            ;

            return s;
        }
    }

    private String prefName = "prefSet";
    private String default_cityName = "Yunlin";
    MyWeather myWeather;
    public static String mDay="",mDate="",mLocation="",mTemp="",mLowTemp="",mHightTemp="",mWeather="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // weatherApplication = (WeatherApplication) getApplicationContext();
        myWeather = new MyWeather();

        loadPrefCity();
        new MyQueryYahooWeatherTask("Taipei").execute();


    }

    private void loadPrefCity() {
        SharedPreferences preferences = getSharedPreferences(prefName, Context.MODE_PRIVATE);
        String cityName = preferences.getString("cityName", default_cityName);

        City city = new City();
        city.setCityName(cityName);
        default_cityName = cityName;


    }


    private class MyQueryYahooWeatherTask extends AsyncTask<Void, Void, Void> {

        String woeid;
        String weatherResult;
        String weatherString;

        MyQueryYahooWeatherTask(String w) {
            woeid = w;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            weatherString = QueryYahooWeather();
            Document weatherDoc = convertStringToDocument(weatherString);

            if (weatherDoc != null) {
                weatherResult = parseWeather(weatherDoc).toString();
            } else {
                weatherResult = "Cannot convertStringToDocument!";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //weather.setText(weatherResult);
            Log.d("data : ",weatherResult);
            mDay=myWeather.forecastday0;
            mDate=myWeather.forecastdate0;
            mLocation=myWeather.city;
            mTemp=myWeather.conditiontemp+" °C";
            mLowTemp=myWeather.forecastlow0 +" °C";
            mHightTemp=myWeather.forecasthigh0+" °C";
            mWeather=myWeather.forecasttext0;
            Toast.makeText(MainActivity.this,myWeather.forecasttext0+" ",Toast.LENGTH_SHORT).show();
            if(myWeather.forecastdate0==null)
                Toast.makeText(MainActivity.this,"連接網路好不",Toast.LENGTH_SHORT).show();
            else if(myWeather.forecasttext0.toString().equals("Partly Cloudy"))
                //imageView.setImageResource(R.drawable.c);
                Log.e("Data",myWeather.forecasttext1+" "+myWeather.forecastdate1);
            Log.e("Data",mDate+" | "+mDay+" | "+mTemp+" | "+mLocation+" | "+mLowTemp+" | "+mHightTemp+" | "+mWeather);
            super.onPostExecute(result);
        }

        private String QueryYahooWeather() {
            String qResult = "";
            String queryString = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + default_cityName + "%2C%20ak%22)&format=xml&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(queryString);

            try {
                HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

                if (httpEntity != null) {
                    InputStream inputStream = httpEntity.getContent();
                    Reader in = new InputStreamReader(inputStream);
                    BufferedReader bufferedreader = new BufferedReader(in);
                    StringBuilder stringBuilder = new StringBuilder();

                    String stringReadLine = null;

                    while ((stringReadLine = bufferedreader.readLine()) != null) {
                        stringBuilder.append(stringReadLine + "\n");
                    }

                    qResult = stringBuilder.toString();
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return qResult;
        }

        private Document convertStringToDocument(String src) {
            Document dest = null;

            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder parser;

            try {
                parser = dbFactory.newDocumentBuilder();
                dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
            } catch (ParserConfigurationException e1) {
                e1.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return dest;
        }

        private MyWeather parseWeather(Document srcDoc) {

            //   MyWeather myWeather = new MyWeather();

            //<description>Yahoo! Weather for New York, NY</description>
            NodeList descNodelist = srcDoc.getElementsByTagName("description");
            if (descNodelist != null && descNodelist.getLength() > 0) {
                myWeather.description = descNodelist.item(0).getTextContent();
            } else {
                myWeather.description = "EMPTY";
            }

            //<yweather:location city="New York" region="NY" country="United States"/>
            NodeList locationNodeList = srcDoc.getElementsByTagName("yweather:location");
            if (locationNodeList != null && locationNodeList.getLength() > 0) {
                Node locationNode = locationNodeList.item(0);
                NamedNodeMap locNamedNodeMap = locationNode.getAttributes();

                myWeather.city = locNamedNodeMap.getNamedItem("city").getNodeValue().toString();
                myWeather.region = locNamedNodeMap.getNamedItem("region").getNodeValue().toString();
                myWeather.country = locNamedNodeMap.getNamedItem("country").getNodeValue().toString();
            } else {
                myWeather.city = "EMPTY";
                myWeather.region = "EMPTY";
                myWeather.country = "EMPTY";
            }

            //<yweather:wind chill="60" direction="0" speed="0"/>
            NodeList windNodeList = srcDoc.getElementsByTagName("yweather:wind");
            if (windNodeList != null && windNodeList.getLength() > 0) {
                Node windNode = windNodeList.item(0);
                NamedNodeMap windNamedNodeMap = windNode.getAttributes();

                myWeather.windChill = windNamedNodeMap.getNamedItem("chill").getNodeValue().toString();
                myWeather.windDirection = windNamedNodeMap.getNamedItem("direction").getNodeValue().toString();
                myWeather.windSpeed = windNamedNodeMap.getNamedItem("speed").getNodeValue().toString();
            } else {
                myWeather.windChill = "EMPTY";
                myWeather.windDirection = "EMPTY";
                myWeather.windSpeed = "EMPTY";
            }

            //<yweather:astronomy sunrise="6:52 am" sunset="7:10 pm"/>
            NodeList astNodeList = srcDoc.getElementsByTagName("yweather:astronomy");
            if (astNodeList != null && astNodeList.getLength() > 0) {
                Node astNode = astNodeList.item(0);
                NamedNodeMap astNamedNodeMap = astNode.getAttributes();

                myWeather.sunrise = astNamedNodeMap.getNamedItem("sunrise").getNodeValue().toString();
                myWeather.sunset = astNamedNodeMap.getNamedItem("sunset").getNodeValue().toString();
            } else {
                myWeather.sunrise = "EMPTY";
                myWeather.sunset = "EMPTY";
            }

            //<yweather:condition text="Fair" code="33" temp="60" date="Fri, 23 Mar 2012 8:49 pm EDT"/>
            NodeList conditionNodeList = srcDoc.getElementsByTagName("yweather:condition");
            if (conditionNodeList != null && conditionNodeList.getLength() > 0) {
                Node conditionNode = conditionNodeList.item(0);
                NamedNodeMap conditionNamedNodeMap = conditionNode.getAttributes();

                myWeather.conditiontext = conditionNamedNodeMap.getNamedItem("text").getNodeValue().toString();
                myWeather.conditiondate = conditionNamedNodeMap.getNamedItem("date").getNodeValue().toString();

                myWeather.conditiontemp = conveterTemp(conditionNamedNodeMap.getNamedItem("temp").getNodeValue().toString());

            } else {
                myWeather.conditiontext = "EMPTY";
                myWeather.conditiondate = "EMPTY";
                myWeather.conditiontemp = "EMPTY";

            }
            //<yweather:forecast xmlns:yweather="http://xml.weather.yahoo.com/ns/rss/1.0" code="39" date="03 Jan 2017" day="Tue" high="72" low="67" text="Scattered Showers"/>

            NodeList weekConditionList = srcDoc.getElementsByTagName("yweather:forecast");
            if (weekConditionList != null && weekConditionList.getLength() > 0) {
                Node weeklyNode = weekConditionList.item(0);
                NamedNodeMap weeklyNameNodeMap = weeklyNode.getAttributes();
                myWeather.forecastdate0 = weeklyNameNodeMap.getNamedItem("date").getNodeValue().toString();
                myWeather.forecastday0 = weeklyNameNodeMap.getNamedItem("day").getNodeValue().toString();
                myWeather.forecasthigh0 = conveterTemp(weeklyNameNodeMap.getNamedItem("high").getNodeValue().toString());
                myWeather.forecastlow0 = conveterTemp(weeklyNameNodeMap.getNamedItem("low").getNodeValue().toString());
                myWeather.forecasttext0 = weeklyNameNodeMap.getNamedItem("text").getNodeValue().toString();

            }else{
                myWeather.forecastdate0 = "EMPTY";
                myWeather.forecastday0 = "EMPTY";
                myWeather.forecasthigh0 = "EMPTY";
                myWeather.forecastlow0 = "EMPTY";
                myWeather.forecasttext0 = "EMPTY";
            }



            return myWeather;
        }

        private String conveterTemp(String temp) {
            double tempF = Double.parseDouble(temp);
            double tempC = 0;
            tempC = (Double) ((tempF - 32) / 1.8);
            BigDecimal b = new BigDecimal(tempC);
            int f1 = b.setScale(0, RoundingMode.HALF_UP).intValue();

            // String result = String .format("%.2f");

            return String.valueOf(f1);
        }

    }

    public void WeatherNowClick(View view) {
        startActivity(new Intent(MainActivity.this,WeatherNowActivity.class));
    }
}
