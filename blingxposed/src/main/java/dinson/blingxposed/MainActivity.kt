package dinson.blingxposed

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit

class MainActivity : AppCompatActivity(), SensorEventListener {
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        //"--------- onSensorChanged ---------------".loge()
        event?.values?.forEach {
            //   "Sensor : $it".loge()
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_toast.setOnClickListener {
            Toast.makeText(this, toastMessage(), Toast.LENGTH_SHORT).show()
        }

        val manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepCount = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        manager.registerListener(this, stepCount, SensorManager.SENSOR_DELAY_FASTEST)
    }

    private fun toastMessage(): String {
        return "I am not hooked"
    }
}
