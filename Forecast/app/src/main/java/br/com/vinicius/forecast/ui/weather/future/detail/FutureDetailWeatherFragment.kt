package br.com.vinicius.forecast.ui.weather.future.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.vinicius.forecast.R

class FutureDetailWeatherFragment : Fragment() {

    private var mViewModel: FutureDetailWeatherViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_detail_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(FutureDetailWeatherViewModel::class.java)
        // TODO: Use the ViewModel
    }

    companion object {

        fun newInstance(): FutureDetailWeatherFragment {
            return FutureDetailWeatherFragment()
        }
    }

}
