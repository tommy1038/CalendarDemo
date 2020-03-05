package jp.noir.tomiyama.calendardemo

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.calendar_cell.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CalendarAdapter(var context: Context) : BaseAdapter() {

    private var dateArray: List<Date> = ArrayList()
    var mDateManager: DateManager = DateManager()

    //カスタムセルを拡張したらここでWigetを定義
    class ViewHolder(view: View) {
        var dateText: TextView = view.dateText
    }

    init {
        dateArray = mDateManager.getDays()!!
    }

    override fun getCount(): Int {
        return dateArray.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

        val (viewHolder, convertView) = if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            val convertView = inflater.inflate(R.layout.calendar_cell, null)
            val viewHolder = ViewHolder(convertView)

            convertView.tag = viewHolder
            Pair(viewHolder, convertView)
        } else {
            Pair(convertView.tag, convertView)
        }

        // セルのサイズの指定
        val dp = context.resources.displayMetrics.density
        val params = AbsListView.LayoutParams(
            parent.width / 7 - dp.toInt(),
            (parent.height - dp.toInt() * mDateManager.getWeeks()) / mDateManager.getWeeks()
        )
        convertView.layoutParams = params
        // 日付のみ表示させる
        val dateFormat = SimpleDateFormat("d", Locale.JAPAN)

        (viewHolder as ViewHolder).dateText.text = dateFormat.format(dateArray[position])

        //当月以外のセルをグレーアウト
        if (mDateManager.isCurrentMonth(dateArray[position])) {
            convertView.setBackgroundColor(Color.WHITE)
        } else {
            convertView.setBackgroundColor(Color.LTGRAY)
        }

        //日曜日を赤、土曜日を青に
        val colorId: Int = when (mDateManager.getDayOfWeek(dateArray[position])) {
            1 -> Color.RED
            7 -> Color.BLUE
            else -> Color.BLACK
        }
        (viewHolder as ViewHolder).dateText.setTextColor(colorId)

        return convertView

    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    //表示月を取得
    fun getTitle(): String? {
        val format = SimpleDateFormat("yyyy.MM", Locale.US)
        return format.format(mDateManager.mCalendar.time)
    }

    //翌月表示
    fun nextMonth() {
        mDateManager.nextMonth()
        dateArray = mDateManager.getDays()!!
        notifyDataSetChanged()
    }

    //前月表示
    fun prevMonth() {
        mDateManager.prevMonth()
        dateArray = mDateManager.getDays()!!
        notifyDataSetChanged()
    }
}