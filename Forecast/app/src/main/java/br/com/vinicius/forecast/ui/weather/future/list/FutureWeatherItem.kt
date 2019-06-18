package br.com.vinicius.forecast.ui.weather.future.list

import br.com.vinicius.forecast.R
import br.com.vinicius.forecast.data.db.unitlocalized.future.MetricSimpleFutureWeatherEntry
import br.com.vinicius.forecast.data.db.unitlocalized.future.UnitSpecificSimpleFutureWeatherEntry
import br.com.vinicius.forecast.internal.glide.GlideApp
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_future_weather.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class FutureWeatherItem( val weatherEntry: UnitSpecificSimpleFutureWeatherEntry) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textViewCondition.text = weatherEntry.conditionText
            updateDate()
            updateTemperature()
            updateConditionImage()
        }
    }

    override fun getLayout() = R.layout.item_future_weather

    private fun ViewHolder.updateDate() {
        val dtFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        textViewDate.text = weatherEntry.date.format(dtFormatter)
    }

    private fun ViewHolder.updateTemperature() {
        val unitAbbreviation = if(weatherEntry is MetricSimpleFutureWeatherEntry) "ºC" else "ºF"
        textViewTemperature.text = "${weatherEntry.avgTemperature}$unitAbbreviation"
    }

    private fun ViewHolder.updateConditionImage() {
        GlideApp.with(this.containerView)
            .load("http:"+weatherEntry.conditionIconUrl)
            .into(imageViewConditionIcon)
    }

}