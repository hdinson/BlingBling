package dinson.customview.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dinson.customview.utils.toast
import com.dinson.blingbase.rxcache.rxCache
import com.dinson.blingbase.rxcache.stategy.CacheStrategy
import com.dinson.blingbase.utils.DateUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dinson.customview.R
import dinson.customview.api.DaysMatterApi
import dinson.customview.entity.countdown.IDailyNews
import dinson.customview.entity.countdown.OnTheDay
import dinson.customview.holder._025DailyTodayViewHolder
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.logi
import dinson.customview.utils.MMKVUtils
import kotlinx.android.synthetic.main.fragment_025_on_the_days.*

class _025OnTheDaysFragment : ViewPagerLazyFragment() {

    private val mApi by lazy { HttpHelper.create(DaysMatterApi::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_025_on_the_days, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val date = DateUtils.getDateOfDay(0, "yyyy-MM-dd")
        tvDate1.text = date[0].toString()
        tvDate2.text = date[1].toString()
        tvDate3.text = date[2].toString()
        tvDate4.text = date[3].toString()
        tvDate5.text = date[5].toString()
        tvDate6.text = date[6].toString()
        tvDate7.text = date[8].toString()
        tvDate8.text = date[9].toString()
        logi { "日期设置完毕" }
    }

    override fun lazyInit() {
        val date = DateUtils.getDateOfDay(0, "yyyy-MM-dd")
        mApi.loadDailyNews()
            .rxCache("daily_banner", CacheStrategy.firstCache())
            .compose(RxSchedulers.io_main())
            .subscribe({
                initBanner(it.data)
            }, {
                it.toString().toast()
            }).addToManaged()

        val todayListStr = MMKVUtils.getOnTheDay(date)
        if (todayListStr.isEmpty()) {
            mApi.loadOnThisDays().compose(RxSchedulers.io_main())
                .subscribe({
                    if (it.isNotEmpty()) {
                        if (it.containsKey(date)) {
                            initTodayList(it[date])
                        } else {
                            initTodayList(it.iterator().next().value)
                        }
                        MMKVUtils.setOnTheDay(it)
                    }
                }, { it.toString().toast() }).addToManaged()
        } else {
            val type = object : TypeToken<ArrayList<OnTheDay>>() {}.type
            val list = Gson().fromJson<ArrayList<OnTheDay>>(todayListStr, type)
            if (list != null && list.isNotEmpty()) {
                initTodayList(list)
            }
        }
    }

    private val mData = ArrayList<IDailyNews>()
    private fun initBanner(data: ArrayList<IDailyNews>) {
        if (data.isEmpty()) return
        mData.clear()
        mData.addAll(data.take(10))
        dailyBanner.setPages(mData, _025DailyTodayViewHolder())
    }

    private fun initTodayList(list: ArrayList<OnTheDay>?) {
        if (list == null) return
        list.forEach {
            val view = layoutInflater.inflate(R.layout.item_025_daily_today, null)
            view.findViewById<TextView>(R.id.tvTodayTitle).text = it.year
            view.findViewById<TextView>(R.id.tvTodayDesc).text = it.content
            llDailyContainer.addView(view)
        }
    }
}
