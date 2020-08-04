package dinson.customview.fragment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.Navigation
import com.dinson.blingbase.kotlin.click
import dinson.customview.R
import kotlinx.android.synthetic.main.fragment_028_first.*

class _028FirstFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_028_first, container, false)
    }

    private var startAge = 20

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnJump1.click {
            val bundle = _028SecondFragmentArgs.Builder().setUserAge(startAge).setUserName("Dinson")
                .build().toBundle()
            startAge++
            Navigation.findNavController(it).navigate(R.id.action028ToSecondFragment, bundle)
        }
        btnJump2.click {
            val CHANNEL_ID = "channelId"
            val TAG = "这是通知标签"
            val id = (1..Int.MAX_VALUE).random()
            if (activity == null) return@click
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, "channelName", importance)
                channel.description = "description"
                val notification = requireActivity().getSystemService(NotificationManager::class.java)
                notification.createNotificationChannel(channel)
            }

            val builder = NotificationCompat.Builder(requireActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_arrows_down)
                .setContentTitle("DeepLinkDemo")
                .setContentText("content text")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(getPendingIntent())
                .setAutoCancel(true).build()
            NotificationManagerCompat.from(requireActivity()).notify(TAG, id, builder)
        }
    }

    private fun getPendingIntent(): PendingIntent? {
        return null
        /*if (activity == null) return null
        val bundle = Bundle()
        bundle.putString("params", "Paramsfromnoti fication Hellomichae")
        return Navigation.findNavController(requireActivity(), R.id.sendnotification)
            .createDeepLink()
            .setGraph(R.navigation.graph_deep_link_activity)
            .setdestination(R.id.deeplinksettingsfragment)
            .setarguments(bundle)
            .creatependingintent()*/
    }


}