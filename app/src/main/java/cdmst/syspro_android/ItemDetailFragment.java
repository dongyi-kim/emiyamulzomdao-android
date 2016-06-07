package cdmst.syspro_android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cdmst.syspro_android.dummy.DummyContent;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */

public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }


    @BindView(R.id.chart_humidity) LineChart mChartHumidity;
    @BindView(R.id.chart_temperature) LineChart mChartTemperature;
    @BindView(R.id.chart_illumination) LineChart mChartIllumination;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        ButterKnife.bind(this, rootView);
        mChartHumidity = (LineChart) rootView.findViewById(R.id.chart_humidity);
        mChartTemperature = (LineChart) rootView.findViewById(R.id.chart_temperature);
        mChartIllumination = (LineChart) rootView.findViewById(R.id.chart_illumination);
        initCharts();
        requestData();
        return rootView;
    }

    public void initCharts(){
        setupChart(mChartHumidity);
        setupChart(mChartTemperature);
        setupChart(mChartIllumination);
    }
    private int[] mColors = new int[] {
            Color.rgb(137, 230, 81),
            Color.rgb(240, 240, 30),
            Color.rgb(89, 199, 250),
            Color.rgb(250, 104, 104)
    };

    public void setupChart(LineChart chart){
//        ((LineDataSet) data.getDataSetByIndex(0)).setCircleColor(color);

        // no description text
        chart.setDescription("");
        chart.setNoDataTextDescription("You need to provide data for the chart.");

        //
        // enable / disable grid background
//        chart.setDrawGridBackground(false);
//        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

//        chart.setBackgroundColor(color);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        chart.setViewPortOffsets(10, 0, 10, 0);

        // add data
//        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(true);
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisLeft().setSpaceTop(40);
        chart.getAxisLeft().setSpaceBottom(40);
        chart.getAxisRight().setEnabled(false);


        chart.getXAxis().setEnabled(true);
        chart.getXAxis().setDrawLabels(true);
        chart.getXAxis().setLabelsToSkip(3);

        chart.getAxisLeft().setDrawAxisLine(true);
        chart.getAxisLeft().setDrawLabels(true);
        chart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

//        chart.getAxisLeft().draw

        chart.getAxisRight().setDrawAxisLine(true);
        chart.getAxisRight().setDrawLabels(true);
        chart.setDrawBorders(true);

        // animate calls invalidate()...
        chart.animateX(2500);
    }

    public void requestData(){
        Thread threadRequest=  new Thread( new Runnable() {
            @Override
            public void run() {
                String dataUrl = "http://whispering-basin-50196.herokuapp.com/api/data?date=2016.05.17";
                String dataUrlParameters = "";
                URL url;
                HttpURLConnection connection = null;
                try{
                    url = new URL(dataUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
//                    connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//                    connection.setRequestProperty("Content-Length","" + Integer.toString(dataUrlParameters.getBytes().length));
//                    connection.setRequestProperty("Content-Language", "en-US");
//                    connection.setUseCaches(false);
//                    connection.setDoInput(true);
//                    connection.setDoOutput(true);
// Send request
                    int status = connection.getResponseCode();
//                    DataOutputStream wr = new DataOutputStream(
//                            connection.getOutputStream());
//                    wr.writeBytes(dataUrlParameters);
//                    wr.flush();
//                    wr.close();
// Get Response
                    InputStream is = connection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuffer response = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
//                        response.append('\r');
                    }
                    rd.close();
                    String responseStr = response.toString();
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("json", responseStr);
                    msg.setData(bundle);
                    dataHander.sendMessage(msg);
                }catch (Exception ex){
                    Log.e("error", ex.getMessage());
                    ex.printStackTrace();
                }

            }
        });
        threadRequest.start();
    }

    Handler dataHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            try{
                String json = msg.getData().getString("json");
                JSONObject data = new JSONObject(json);
                JSONArray array = data.getJSONArray("data");
                ArrayList<String> xVals = new ArrayList<String>();
                ArrayList<Entry> yValHumidity = new ArrayList<Entry>();
                ArrayList<Entry> yValSoilHumidity = new ArrayList<Entry>();
                ArrayList<Entry> yValTemperature = new ArrayList<Entry>();
                ArrayList<Entry> yValIllumination = new ArrayList<Entry>();
                for(int i = 0 ; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);
                    float soil_humidity = (float)object.getDouble("soil_humidity");
                    float humidity        = (float)object.getDouble("humidity");
                    float temperature   = (float)object.getDouble("temperature");
                    float illumination  = (float)object.getDouble("illumination");
                    String timestamp    = object.getString("created_at").substring(2,8);

                    xVals.add(timestamp);
                    yValSoilHumidity.add(new Entry(soil_humidity, i));
                    yValHumidity.add(new Entry(humidity, i));
                    yValTemperature.add(new Entry(temperature, i));
                    yValIllumination.add(new Entry(illumination, i));
                }

                ArrayList<ILineDataSet> dataHumidity = new ArrayList<>();
                LineDataSet dataSetHumidity = new LineDataSet(yValHumidity, "습도");
                LineDataSet dataSetSoilHumidity = new LineDataSet(yValSoilHumidity, "토양습도");

                dataSetHumidity.setDrawCubic(true);
                dataSetSoilHumidity.setDrawCubic(true);
                dataSetSoilHumidity.setColor(Color.rgb(160, 82,45));
                dataSetSoilHumidity.setCircleColor(Color.rgb(160, 82,45));

//                dataSetHumidity.setLineWidth(1.75f);
//                dataSetHumidity.setCircleRadius(5f);
//                dataSetHumidity.setColor(Color.WHITE);
//                dataSetHumidity.setCircleColorHole(Color.WHITE);
//                dataSetHumidity.setHighLightColor(Color.WHITE);
//                dataSetHumidity.setDrawValues(false);

                dataHumidity.add(dataSetHumidity);
                dataHumidity.add(dataSetSoilHumidity);


//                dataHumidity.add(new LineDataSet(yValSoilHumidity, "토양습도"));

                ArrayList<ILineDataSet> dataTemperature = new ArrayList<>();
                LineDataSet dataSetTemperature =new LineDataSet(yValTemperature, "기온");
                dataSetTemperature.setColor(Color.RED);
                dataSetTemperature.setCircleColor(Color.RED);
                dataSetTemperature.setDrawCubic(true);
                dataSetTemperature.setDrawFilled(true);
                dataSetTemperature.setFillColor(Color.RED);
                dataTemperature.add(dataSetTemperature);

                ArrayList<ILineDataSet> dataIllumination = new ArrayList<>();
                LineDataSet dataSetIllumination =  new LineDataSet(yValIllumination, "조도");
                dataSetIllumination.setDrawCubic(true);
                dataSetIllumination.setDrawFilled(true);
                dataSetIllumination.setFillColor(Color.YELLOW);
                dataSetIllumination.setColor(Color.YELLOW);
                dataSetIllumination.setCircleColor(Color.YELLOW);

                dataIllumination.add(dataSetIllumination);

                LineData lineDataHumidity = new LineData(xVals, dataHumidity);
                lineDataHumidity.setDrawValues(false);


                LineData lineDataTemperature = new LineData(xVals, dataTemperature);
                lineDataTemperature.setDrawValues(false);

                LineData lineDataIllumination = new LineData(xVals, dataIllumination);
                lineDataIllumination.setDrawValues(false);


                mChartHumidity.setData(lineDataHumidity);
                mChartTemperature.setData(lineDataTemperature);
                mChartIllumination.setData(lineDataIllumination);

                mChartHumidity.invalidate();
                mChartTemperature.invalidate();
                mChartIllumination.invalidate();
            }catch (Exception ex){

            }

        }

    };


}
