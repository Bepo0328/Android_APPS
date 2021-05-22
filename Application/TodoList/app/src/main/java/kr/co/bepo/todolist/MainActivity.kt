package kr.co.bepo.todolist

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.co.bepo.todolist.databinding.ActivityMainBinding
import kr.co.bepo.todolist.databinding.ItemTodoBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    private val viewModel: MainViewModel by viewModels()
    private val auth: FirebaseAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginActivityResult()
        val currentUser = auth.currentUser
        if(currentUser == null){
            login()
        } else {
            Log.d(TAG, "currentUser: $currentUser")
        }

        setupListener()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.actionLogout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loginActivityResult() {
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult  ->
                if (result.resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "Login: Success!")
                    viewModel.fetchData()
                } else {
                    Log.d(TAG, "Login: Fail!")
                    finish()
                }
            }
    }

    private fun login() {
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

        startForResult.launch(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build())
    }

    private fun logout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "logout: Success!")
                    login()
                } else {
                    Log.d(TAG, "logout: Fail!")
                }
            }
    }

    private fun setupListener() {
        binding.recyclerView.apply {
            adapter = TodoAdapter(
                emptyList(),
                onClickDeleteIcon = {
                    viewModel.deleteTodo(it)
                },
                onClickItem = {
                    viewModel.toggleTodo(it)
                }
            )
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        binding.addButton.setOnClickListener {
            val todo = Todo(binding.editText.text.toString())
            viewModel.addTodo(todo)
        }

        viewModel.todoLiveData.observe(this, Observer {
            (binding.recyclerView.adapter as TodoAdapter).setData(it)
        })
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

data class Todo(
    val text: String,
    var isDone: Boolean = false
)

class TodoAdapter(
    private var todoList: List<DocumentSnapshot>,
    private val onClickDeleteIcon: (todo: DocumentSnapshot) -> Unit,
    private val onClickItem: (todo: DocumentSnapshot) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(ItemTodoBinding.bind(view))
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todoList[position]
        holder.binding.todoTextView.text = todo.getString("text") ?: ""
        holder.binding.deleteImageView.setOnClickListener {
            onClickDeleteIcon.invoke(todo)
        }

        holder.binding.root.setOnClickListener {
            onClickItem.invoke(todo)
        }

        if (todo.getBoolean("isDone") == true) {
            holder.binding.todoTextView.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTypeface(null, Typeface.ITALIC)
            }
        } else {
            holder.binding.todoTextView.apply {
                paintFlags = 0
                setTypeface(null, Typeface.NORMAL)
            }
        }
    }

    override fun getItemCount(): Int = todoList.size

    fun setData(newData: List<DocumentSnapshot>) {
        todoList = newData
        notifyDataSetChanged()
    }
}

class MainViewModel : ViewModel() {
    val todoLiveData = MutableLiveData<List<DocumentSnapshot>>()
    private val db  = Firebase.firestore
    private val user = Firebase.auth.currentUser

    init {
        fetchData()
    }

    fun fetchData() {
        if (user != null) {
            db.collection(user.uid)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    }

                    if (value != null) {
                        todoLiveData.value = value.documents
                    }
                }
        }
    }

    fun addTodo(todo: Todo) {
        user?.let {
            db.collection(user.uid).add(todo)
        }
    }

    fun deleteTodo(todo: DocumentSnapshot) {
        user?.let {
            db.collection(user.uid).document(todo.id).delete()
        }
    }

    fun toggleTodo(todo: DocumentSnapshot) {
        user?.let {
            val isDone = todo.getBoolean("isDone") ?: false
            db.collection(user.uid).document(todo.id)
                .update("isDone", !isDone)
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}