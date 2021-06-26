package local.host.thon

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.StrictMode
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.eazegraph.lib.charts.BarChart
import org.eazegraph.lib.charts.ValueLineChart
import org.eazegraph.lib.models.BarModel
import org.eazegraph.lib.models.ValueLinePoint
import org.eazegraph.lib.models.ValueLineSeries
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.LinkedHashMap


class Summary : Fragment() {
    @SuppressLint("SetTextI18n", "SimpleDateFormat", "SetJavaScriptEnabled", "CutPasteId")
    private fun createGraph(days : Int,view : View){
        val pr = view.findViewById<LinearLayout>(R.id.graphCanvas)
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val ftt = Date()
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        val dt = dateFormat.format(cal.time);
        val c = Calendar.getInstance()
        c.add(Calendar.DATE,-1)
        c.time = ftt
        var dayOfWeek = c[Calendar.DAY_OF_WEEK]
        val week = arrayOf("","Sun","Mon","Tue","Wed","Thu","Fri","Sat");
        println("Day of week in number:$dayOfWeek")
        val dayWeekText = SimpleDateFormat("E").format(ftt)
        println("Day of week in text:$dayWeekText")
        val queue = Volley.newRequestQueue(view.context)
        val url = "${addr}/graph/${dt},${days}"
        view.findViewById<MaterialCardView>(R.id.graphCard).visibility = View.VISIBLE
        view.findViewById<ProgressBar>(R.id.grph_load).visibility = View.VISIBLE
        view.findViewById<ChipGroup>(R.id.graphMode).visibility = View.GONE
        when(days) {
            7 -> {
                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    { response ->
                        val mBarChart = BarChart(view.context)
                        val mLineChart = ValueLineChart(view.context)
                        val series = ValueLineSeries()
                        series.setColor(0xFF56B7F1.toInt());
                        val ll =
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 512)
                        var daydt = 0;
                        val arr = JSONArray(response)
                        for (i in 0 until days) {
                            dayOfWeek--;
                            if (dayOfWeek == 0) dayOfWeek = 7
                        }
                        for (i in 1 until arr.length()) {
                            val m = arr[i] as JSONObject;
                            val n = arr[i - 1] as JSONObject;
                            daydt = m["unit"].toString().toInt() - n["unit"].toString().toInt()
                            mBarChart.addBar(BarModel(week[dayOfWeek], daydt.toFloat(), -0xedcbaa))
                            series.addPoint(ValueLinePoint(week[dayOfWeek], daydt.toFloat()))
                            if (dayOfWeek == 7) dayOfWeek = 1;
                            else dayOfWeek++;
                        }
                        view.findViewById<ProgressBar>(R.id.grph_load).visibility = View.GONE
                        view.findViewById<ChipGroup>(R.id.graphMode).visibility = View.VISIBLE
                        mBarChart.layoutParams = ll
                        mLineChart.layoutParams = ll
                        mLineChart.visibility = View.GONE
                        mLineChart.addSeries(series);
                        pr.addView(mBarChart)
                        pr.addView(mLineChart)
                        mBarChart.startAnimation()
                        view.findViewById<Chip>(R.id.barMode).isChecked = true

                        view.findViewById<Chip>(R.id.barMode).setOnClickListener {
                            mLineChart.visibility = View.GONE
                            mBarChart.visibility = View.VISIBLE
                            mBarChart.startAnimation()
                            view.findViewById<Chip>(R.id.LineMode).isChecked = false
                        }
                        view.findViewById<Chip>(R.id.LineMode).setOnClickListener{
                            mBarChart.visibility = View.GONE
                            mLineChart.visibility = View.VISIBLE
                            mLineChart.startAnimation()
                            view.findViewById<Chip>(R.id.barMode).isChecked = false
                        }
                        Log.d("A", response)
                    }, {

                    })
                queue.add(stringRequest)

            }
            15 -> {
                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    { response ->
                        val mBarChart = BarChart(view.context)
                        val mLineChart = ValueLineChart(view.context)
                        val series = ValueLineSeries()
                        series.setColor(0xFF56B7F1.toInt());
                        val ll = LinearLayout.LayoutParams(2500, 512)
                        var daydt = 0;
                        val arr = JSONArray(response)
                        for (i in 0 until days) c.add(Calendar.DATE, -1)

                        for (i in 1 until arr.length()) {
                            val m = arr[i] as JSONObject;
                            val n = arr[i - 1] as JSONObject;
                            daydt = m["unit"].toString().toInt() - n["unit"].toString().toInt()
                            val dtF = SimpleDateFormat("d MMM")
                            mBarChart.addBar(
                                BarModel(
                                    dtF.format(c.time),
                                    daydt.toFloat(),
                                    -0xedcbaa
                                )
                            )
                            series.addPoint(ValueLinePoint(dtF.format(c.time), daydt.toFloat()))
                            c.add(Calendar.DATE, 1);
                        }
                        mBarChart.layoutParams = ll
                        view.findViewById<ProgressBar>(R.id.grph_load).visibility = View.GONE
                        view.findViewById<ChipGroup>(R.id.graphMode).visibility = View.VISIBLE
                        mLineChart.layoutParams = ll
                        mLineChart.visibility = View.GONE
                        mLineChart.addSeries(series);
                        pr.addView(mBarChart)
                        pr.addView(mLineChart)
                        mBarChart.startAnimation()
                        view.findViewById<Chip>(R.id.barMode).isChecked = true

                        view.findViewById<Chip>(R.id.barMode).setOnClickListener {
                            mLineChart.visibility = View.GONE
                            mBarChart.visibility = View.VISIBLE
                            mBarChart.startAnimation()
                            view.findViewById<Chip>(R.id.LineMode).isChecked = false
                        }
                        view.findViewById<Chip>(R.id.LineMode).setOnClickListener{
                            mBarChart.visibility = View.GONE
                            mLineChart.visibility = View.VISIBLE
                            mLineChart.startAnimation()
                            view.findViewById<Chip>(R.id.barMode).isChecked = false
                        }
                        Log.d("A", response)
                    }, {

                    })

                queue.add(stringRequest)
            }
            30 -> {
                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    { response ->
                        val mBarChart = BarChart(view.context)
                        val mLineChart = ValueLineChart(view.context)
                        val series = ValueLineSeries()
                        series.setColor(0xFF56B7F1.toInt());
                        val ll = LinearLayout.LayoutParams(4000, 512)
                        var daydt = 0;
                        val arr = JSONArray(response)
                        for (i in 0 until days) c.add(Calendar.DATE, -1)

                        for (i in 1 until arr.length()) {
                            val m = arr[i] as JSONObject;
                            val n = arr[i - 1] as JSONObject;
                            daydt = m["unit"].toString().toInt() - n["unit"].toString().toInt()
                            val dtF = SimpleDateFormat("dd-MM")
                            mBarChart.addBar(
                                BarModel(
                                    dtF.format(c.time),
                                    daydt.toFloat(),
                                    -0xedcbaa
                                )
                            )
                            series.addPoint(ValueLinePoint(dtF.format(c.time), daydt.toFloat()))

                            c.add(Calendar.DATE, 1);
                        }
                        mBarChart.layoutParams = ll
                        view.findViewById<ProgressBar>(R.id.grph_load).visibility = View.GONE
                        view.findViewById<ChipGroup>(R.id.graphMode).visibility = View.VISIBLE
                        mLineChart.layoutParams = ll
                        mLineChart.visibility = View.GONE
                        mLineChart.addSeries(series);
                        pr.addView(mBarChart)
                        pr.addView(mLineChart)
                        mBarChart.startAnimation()
                        view.findViewById<Chip>(R.id.barMode).isChecked = true

                        view.findViewById<Chip>(R.id.barMode).setOnClickListener {
                            mLineChart.visibility = View.GONE
                            mBarChart.visibility = View.VISIBLE
                            mBarChart.startAnimation()
                            view.findViewById<Chip>(R.id.LineMode).isChecked = false
                        }
                        view.findViewById<Chip>(R.id.LineMode).setOnClickListener{
                            mBarChart.visibility = View.GONE
                            mLineChart.visibility = View.VISIBLE
                            mLineChart.startAnimation()
                            view.findViewById<Chip>(R.id.barMode).isChecked = false
                        }
                        Log.d("A", response)
                    }, {

                    })
                queue.add(stringRequest)
            }
        }

    }

    @SuppressLint("SimpleDateFormat", "ResourceType", "CutPasteId")
    private fun evaluateCustomGraph(view : View){
        val stR = arrayOf<MaterialButton>(view.findViewById(R.id.strart_range),view.findViewById(R.id.end_range))
        val range = arrayOf(Date(),Date())
        val one = view.findViewById<RadioGroup>(R.id.one)
        one.check(R.id.daily);
        stR[0].setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(view.context, { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                val df = SimpleDateFormat("dd-MM-yyyy")

                stR[0].text = df.format(Date("$year/${monthOfYear+1}/${dayOfMonth}"));
                val cc = Calendar.getInstance()
                cc.time = Date("$year/${monthOfYear+1}/${dayOfMonth}")
                cc.add(Calendar.DATE,-1)
                range[0] = cc.time;
            }, year, month, day)
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.DATE,-1)
        dpd.datePicker.maxDate = cal.time.time
            dpd.datePicker.minDate = Date("2020/01/01").time;
            dpd.show()
        }
        stR[1].setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(view.context, { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox00
                val df = SimpleDateFormat("dd-MM-yyyy")

                stR[1].text = df.format(Date("$year/${monthOfYear+1}/${dayOfMonth}"));
                range[1] = Date("$year/${monthOfYear+1}/${dayOfMonth}")
            }, year, month, day)
            val cal = Calendar.getInstance()
            cal.time = Date()
            cal.add(Calendar.DATE,-1)
            dpd.datePicker.maxDate = cal.time.time
            dpd.datePicker.minDate = Date("2020/01/01").time;
            dpd.show()
        }
        val displayMetrics = DisplayMetrics()
        this.requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val btn = view.findViewById<MaterialButton>(R.id.cust_graph_btn)
        val queue = Volley.newRequestQueue(view.context)
        val pr = view.findViewById<LinearLayout>(R.id.graphCanvas)
        btn.setOnClickListener {
            pr.removeAllViews()
            view.findViewById<ChipGroup>(R.id.graphMode).visibility = View.GONE
            val dfm = SimpleDateFormat("yyyy-MM-dd")
            val mn = arrayOf(SimpleDateFormat("dd-MM"),SimpleDateFormat("MMM"),SimpleDateFormat("yyyy"))
            val dt1 = dfm.format(range[0]);
            val dt2 = dfm.format(range[1]);
            val url = "${addr}/bet/${dt1},${dt2}"
            val month = LinkedHashMap<String,Int>()
            val daily = LinkedHashMap<String,Int>()
            val yearly = LinkedHashMap<String,Int>()
            if(range[0].time<range[1].time){
                view.findViewById<MaterialCardView>(R.id.graphCard).visibility = View.VISIBLE
                view.findViewById<ProgressBar>(R.id.grph_load).visibility = View.VISIBLE
                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    { response ->
                        val ck = Calendar.getInstance()
                        ck.time = range[0];
                        val mBarChart = BarChart(view.context)
                        val mLineChart = ValueLineChart(view.context)
                        val sdf = SimpleDateFormat("dd-MM")
                       val k = JSONArray(response)
                        var len = 0L
                        for(i in 1 until k.length()){
                            ck.add(Calendar.DATE,1)
                            val n = k[i] as JSONObject
                            val m = k[i-1] as JSONObject
                            val diff = n["unit"].toString().toInt() - m["unit"].toString().toInt();
                            daily[mn[0].format(ck.time)] = diff
                            if(!month.containsKey(mn[1].format(ck.time))) month[mn[1].format(ck.time)] = 0
                            month[mn[1].format(ck.time)] = month[mn[1].format(ck.time)]!!.plus(diff)
                            if(!yearly.containsKey(mn[2].format(ck.time))) yearly[mn[2].format(ck.time)] = 0
                            yearly[mn[2].format(ck.time)] = yearly[mn[2].format(ck.time)]!!.plus(diff)
                        }
                        val series = ValueLineSeries()
                        series.setColor(0xFF56B7F1.toInt());
                        when(one.checkedRadioButtonId){
                            R.id.daily -> {
                                for(i in daily){
                                    len+=135;
                                    mBarChart.addBar(BarModel(i.key,i.value.toFloat(),-0xedcbaa))
                                    series.addPoint(ValueLinePoint(i.key,i.value.toFloat()))
                                }
                            }
                            R.id.monthly -> {
                                for(i in month){
                                    len+=135;
                                    mBarChart.addBar(BarModel(i.key,i.value.toFloat(),-0xedcbaa))
                                    series.addPoint(ValueLinePoint(i.key,i.value.toFloat()))
                                }
                            }
                            R.id.yearly -> {
                                for(i in yearly){
                                    len+=135;
                                    mBarChart.addBar(BarModel(i.key,i.value.toFloat(),-0xedcbaa))
                                    series.addPoint(ValueLinePoint(i.key,i.value.toFloat()))
                                }
                            }
                        }
                        var ll = LinearLayout.LayoutParams(len.toInt(), 512)
                        if(len.toInt()<=width) ll = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 512)
                        mBarChart.layoutParams = ll
                        mLineChart.layoutParams = ll
                        mLineChart.visibility = View.GONE
                        mLineChart.addSeries(series);
                        pr.addView(mBarChart)
                        pr.addView(mLineChart)
                        mBarChart.startAnimation()

                        view.findViewById<Chip>(R.id.barMode).isChecked = true

                                view.findViewById<Chip>(R.id.barMode).setOnClickListener {
                                    mLineChart.visibility = View.GONE
                                    mBarChart.visibility = View.VISIBLE
                                    mBarChart.startAnimation()
                                    view.findViewById<Chip>(R.id.LineMode).isChecked = false
                                }
                                view.findViewById<Chip>(R.id.LineMode).setOnClickListener{
                                    mBarChart.visibility = View.GONE
                                    mLineChart.visibility = View.VISIBLE
                                    mLineChart.startAnimation()
                                    view.findViewById<Chip>(R.id.barMode).isChecked = false
                                }
                        view.findViewById<ProgressBar>(R.id.grph_load).visibility = View.GONE
                        view.findViewById<ChipGroup>(R.id.graphMode).visibility = View.VISIBLE
                       Log.d("SIZE",k.length().toString())
                    },{})
                queue.add(stringRequest)
            }else{
                Snackbar.make(view,"Reversed Range Found",Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        super.onCreate(savedInstanceState)
        val items = arrayOf("Last 7 days", "Last 15 days", "Last 30 days","Custom Range")
        val view =  inflater.inflate(R.layout.summary, container, false)
        val pBtn = view.findViewById<MaterialCardView>(R.id.pSelectBtn)

        pBtn.setOnClickListener {
            MaterialAlertDialogBuilder(view.context)
                .setTitle("Select Period")
                .setItems(items) { dialog, which ->
                    view.findViewById<MaterialCardView>(R.id.graphCard).visibility = View.GONE
                    view.findViewById<ChipGroup>(R.id.graphMode).visibility = View.GONE
                    if(which==items.size-1){
                        view.findViewById<MaterialCardView>(R.id.custom_range_card).visibility = View.VISIBLE
                    }else{
                        view.findViewById<MaterialCardView>(R.id.custom_range_card).visibility = View.GONE
                    }
                    val pr = view.findViewById<LinearLayout>(R.id.graphCanvas)
                    when(which){
                        0->{
                            pr.removeAllViews()
                           createGraph(7,view);
                           }
                        1->{
                            pr.removeAllViews()
                            createGraph(15,view);
                        }
                        2->{
                            pr.removeAllViews()
                            createGraph(30,view);
                        }
                        3->{
                            pr.removeAllViews()
                            evaluateCustomGraph(view)
                        }
                    }
                    view.findViewById<TextView>(R.id.period_txt).text = items[which];
                }
                .show()
        }

//        val mBarChart = view.findViewById(R.id.barchart) as BarChart
//        val mBarChart1 = view.findViewById(R.id.barchart1) as BarChart
//        mBarChart.addBar(BarModel("A",0f, -0xedcbaa))
//        mBarChart.addBar(BarModel("A",2f, -0xcbcbaa))
//        mBarChart.addBar(BarModel("A",3.3f, -0xa9cbaa))
//        mBarChart.addBar(BarModel("A",1.1f, -0x78c0aa))
//        mBarChart.addBar(BarModel("A",2.7f, -0xa9480f))
//        mBarChart.addBar(BarModel("A",2f, -0xcbcbaa))
//        mBarChart.addBar(BarModel("A",0.4f, -0xe00b54))
//        mBarChart.addBar(BarModel("A",4f, -0xe45b1a))
//        mBarChart.addBar(BarModel("A",4f, -0xe45b1a))
//        mBarChart.addBar(BarModel("A",2.3f, -0xedcbaa))
//        mBarChart.addBar(BarModel("A",2f, -0xcbcbaa))
//        mBarChart.addBar(BarModel("A",3.3f, -0xa9cbaa))
//        mBarChart1.addBar(BarModel("A",1.1f, -0x78c0aa))
//        mBarChart1.addBar(BarModel("A",2.7f, -0xa9480f))
//        mBarChart1.addBar(BarModel("A",2f, -0xcbcbaa))
//        mBarChart1.addBar(BarModel("A",0.4f, -0xe00b54))
//        mBarChart1.addBar(BarModel("A",4f, -0xe45b1a))
//        mBarChart1.addBar(BarModel("A",1.1f, -0x78c0aa))
//        mBarChart1.addBar(BarModel("A",2.7f, -0xa9480f))
//        mBarChart1.addBar(BarModel("A",2f, -0xcbcbaa))
//        mBarChart.isShowValues = true
//        mBarChart.startAnimation()
//        mBarChart1.startAnimation()
        return view;
    }
}