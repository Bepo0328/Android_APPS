package kr.co.bepo.androidonline.essential_android

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.bepo.androidonline.R

class RecyclerViewActivity : AppCompatActivity() {
    private val recyclerView: RecyclerView by lazy {
        findViewById(R.id.recyclerView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        with(recyclerView) {
            this.adapter = PhoneBookRecyclerViewAdapter(
                phoneBookList = createFakePhoneBook(fakeNumber = 30),
                inflater = LayoutInflater.from(this@RecyclerViewActivity),
                activity = this@RecyclerViewActivity
            )
            this.layoutManager = LinearLayoutManager(this@RecyclerViewActivity)
        }
    }

    private fun createFakePhoneBook(
        fakeNumber: Int = 10,
        phoneBook: PhoneBook = PhoneBook()
    ): PhoneBook {
        for (i in 0 until fakeNumber) {
            phoneBook.addPerson(
                PhonePerson(name = "$i 번째 사람", number = "$i 번째 사람의 전화 번호")
            )
        }
        return phoneBook
    }
}

class PhonePerson(val name: String, var number: String)

class PhoneBook() {
    val personList = ArrayList<PhonePerson>()

    fun addPerson(phonePerson: PhonePerson) {
        personList.add(phonePerson)
    }
}

class PhoneBookRecyclerViewAdapter(
    private val phoneBookList: PhoneBook,
    private val inflater: LayoutInflater,
    private val activity: Activity
) : RecyclerView.Adapter<PhoneBookRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val personName: TextView = itemView.findViewById(R.id.personNameTextView)

        init {
            itemView.setOnClickListener {
                val intent = Intent(activity, PhoneBookDetailActivity::class.java)
                intent.putExtra("name", phoneBookList.personList[adapterPosition].name)
                intent.putExtra("number", phoneBookList.personList[adapterPosition].number)
                activity.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.phonebook_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.personName.text = phoneBookList.personList[position].name
    }

    override fun getItemCount(): Int {
        return phoneBookList.personList.size
    }
}