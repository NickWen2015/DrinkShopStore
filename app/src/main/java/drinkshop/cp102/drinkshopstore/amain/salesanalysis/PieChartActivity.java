package drinkshop.cp102.drinkshopstore.amain.salesanalysis;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import drinkshop.cp102.drinkshopstore.R;


public class PieChartActivity extends AppCompatActivity {
    private static final String TAG = "PieChartActivity";
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        pieChart = findViewById(R.id.pieChart);

        /* 設定可否旋轉 */
        pieChart.setRotationEnabled(true);

        /* 設定圓心文字 */
        pieChart.setCenterText("八月");
        /* 設定圓心文字大小 */
        pieChart.setCenterTextSize(25);

        Description description = new Description();
        description.setText("飲料銷售分析");
        description.setTextSize(25);
        pieChart.setDescription(description);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                Log.d(TAG, "entry: " + entry.toString() + "; highlight: " + highlight.toString());
                PieEntry pieEntry = (PieEntry) entry;
                String text = pieEntry.getLabel() + "\n" + pieEntry.getValue();
                Toast.makeText(PieChartActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        /* 取得各商品單月銷售量資料 */
        List<PieEntry> pieEntries = getSalesEntries();

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Product Sales");
        pieDataSet.setValueTextColor(Color.BLUE);
        pieDataSet.setValueTextSize(20);
        pieDataSet.setSliceSpace(2);

        /* 使用官訂顏色範本，顏色不能超過5種，否則官定範本要加顏色 */
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();//重繪
    }

    private List<PieEntry> getSalesEntries() {
        List<PieEntry> salesEntries = new ArrayList<>();
        salesEntries.add(new PieEntry(375, "翠芽青茶"));
        salesEntries.add(new PieEntry(345, "蕎麥青茶"));
        salesEntries.add(new PieEntry(293, "英國錫蘭奶茶"));
        salesEntries.add(new PieEntry(285, "烏龍清茶"));
//        salesEntries.add(new PieEntry(2204, "M-Benz"));
        return salesEntries;
    }

}
