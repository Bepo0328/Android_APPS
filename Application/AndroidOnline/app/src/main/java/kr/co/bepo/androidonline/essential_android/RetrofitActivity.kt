package kr.co.bepo.androidonline.essential_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.bepo.androidonline.R
import kr.co.bepo.androidonline.databinding.ActivityRetrofitBinding
import kr.co.bepo.androidonline.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRetrofitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRetrofitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // http://mellowcode.org/json/students
        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RetrofitService::class.java)
        service.getStudentList().enqueue(object : Callback<ArrayList<PersonForServer>> {
            override fun onResponse(
                call: Call<ArrayList<PersonForServer>>,
                response: Response<ArrayList<PersonForServer>>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG,"GET 성공")
                    val personList = response.body()
                    val studentAdapter = StudentAdapter(personList, layoutInflater)
                    binding.studentRecyclerView.adapter = studentAdapter
                    binding.studentRecyclerView.layoutManager = LinearLayoutManager(this@RetrofitActivity)
                }
            }

            override fun onFailure(call: Call<ArrayList<PersonForServer>>, t: Throwable) {
                Log.d(TAG, t.message ?: "")
            }
        })

//        val params = HashMap<String, Any>()
//        params["name"] = "테스트"
//        params["age"] = 20
//        params["intro"] = "테스트"
//        service.createStudent(params).enqueue(object : Callback<PersonForServer> {
//            override fun onResponse(
//                call: Call<PersonForServer>,
//                response: Response<PersonForServer>
//            ) {
//                if (response.isSuccessful) {
//                    Log.d(TAG,"POST 성공")
//                    val person = response.body()
//                    Log.d(TAG, "name: ${person?.name} ")
//                }
//            }
//
//            override fun onFailure(call: Call<PersonForServer>, t: Throwable) {
//                Log.d(TAG, t.message ?: "")
//            }
//        })

        val personServer = PersonForServer(name = "테스트123", age = 30, intro = "테스트123")
        service.createStudentEasy(personServer).enqueue(object : Callback<PersonForServer> {
            override fun onResponse(
                call: Call<PersonForServer>,
                response: Response<PersonForServer>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG,"POST 성공")
                    val person = response.body()
                    Log.d(TAG, "name: ${person?.name} ")
                }
            }

            override fun onFailure(call: Call<PersonForServer>, t: Throwable) {
                Log.d(TAG, t.message ?: "")
            }
        })
    }

    companion object {
        private const val TAG = "RetrofitActivity"
    }
}

class StudentAdapter(
    private val studentList: ArrayList<PersonForServer>?,
    private val inflater: LayoutInflater
): RecyclerView.Adapter<StudentAdapter.ViewHolder>() {
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
        holder.id.text = studentList?.get(position)?.id.toString()
        holder.name.text = studentList?.get(position)?.name.orEmpty()
        holder.age.text = studentList?.get(position)?.age.toString()
        holder.intro.text = studentList?.get(position)?.intro.orEmpty()
    }

    override fun getItemCount(): Int {
        return studentList?.size ?: 0
    }
}
