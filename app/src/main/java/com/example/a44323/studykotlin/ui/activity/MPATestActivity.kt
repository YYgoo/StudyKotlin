package com.example.a44323.studykotlin.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import com.example.a44323.studykotlin.R
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import kotlinx.android.synthetic.main.activity_mpa_test.*

class MPATestActivity:BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mpa_test)

        chart_line.setDrawBorders(true)

        var list:MutableList<Entry> = mutableListOf<Entry>()
        for ( i in 1..10){

            var item:Entry = Entry(i.toFloat(),Math.random().toFloat())
            list.add(item)

        }
        var line1Set:LineDataSet = LineDataSet(list,"增长期望")

        var line1Data:LineData = LineData(line1Set)
        chart_line.data = line1Data
    }

}