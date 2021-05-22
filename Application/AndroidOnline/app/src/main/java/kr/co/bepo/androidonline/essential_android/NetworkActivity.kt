package kr.co.bepo.androidonline.essential_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.co.bepo.androidonline.R
import kr.co.bepo.androidonline.databinding.ActivityNetworkBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NetworkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNetworkBinding
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNetworkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        networkTaskStart()
    }

    private fun networkTaskStart() {
        if (job == null) {
            job = CoroutineScope(IO).launch(IO) {
                val urlString: String = "http://mellowcode.org/json/students/"
                val url: URL = URL(urlString)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"
                connection.setRequestProperty("Content-Type", "application/json")

                var buffer = ""
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream, "UTF-8"))
                    buffer = reader.readLine()
                }
                val data = Gson().fromJson(buffer, Array<PersonForServer>::class.java)
                launch(Main) {
                    val adapter = PersonAdapter(data, layoutInflater)
                    binding.personRecyclerView.adapter = adapter
                    binding.personRecyclerView.layoutManager = LinearLayoutManager(this@NetworkActivity)
                }
            }
        }
    }

    companion object {
        private const val TAG = "NetworkActivity"
    }
}

class PersonAdapter(
    private val personList: Array<PersonForServer>,
    private val inflater: LayoutInflater
): RecyclerView.Adapter<PersonAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.personIdTextView)
        val name: TextView = itemView.findViewById(R.id.personNameTextView)
        val age: TextView = itemView.findViewById(R.id.personAgeTextView)
        val intro: TextView = itemView.findViewById(R.id.personIntroTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_peron_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.id.text = personList[position].id.toString() ?: ""
        holder.name.text = personList[position].name ?: ""
        holder.age.text = personList[position].age.toString() ?: ""
        holder.intro.text = personList[position].intro ?: ""

    }

    override fun getItemCount(): Int {
        return personList.size
    }
}