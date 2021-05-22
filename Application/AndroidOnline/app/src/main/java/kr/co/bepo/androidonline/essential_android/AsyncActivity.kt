package kr.co.bepo.androidonline.essential_android

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kr.co.bepo.androidonline.databinding.ActivityAsyncBinding

class AsyncActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAsyncBinding
    private var task: BackgroundAsyncTask? = null
    private var job: Job? = null
    private var percent: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAsyncBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener {
            jobStart()
        }

        binding.cancelButton.setOnClickListener {
            jobCancel()
        }

//        binding.startButton.setOnClickListener {
//            task = BackgroundAsyncTask(
//                progressBar = binding.progressBar,
//                progressText = binding.commentTextView
//            )
//            task?.execute()
//        }
//
//        binding.cancelButton.setOnClickListener {
//            task?.cancel(true)
//        }
    }

    override fun onPause() {
        super.onPause()
        jobCancel()
//        task?.cancel(true)
    }

    private fun jobStart() {
        percent = 0
        binding.progressBar.progress = percent
        if (job == null) {
            job = CoroutineScope(Main).launch {
                while (isActive) {
                    percent++
                    if (percent > 100) {
                        binding.commentTextView.text = "작업이 완료되었습니다."
                        break
                    } else {
                        binding.progressBar.progress = percent
                        binding.commentTextView.text = "퍼센트: $percent"
                    }
                    try {
                        delay(100)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun jobCancel() {
        job?.cancel()
        job = null
        binding.commentTextView.text = "작업이 취소되었습니다."
    }
}

@Suppress("DEPRECATION")
@SuppressLint("SetTextI18n")
class BackgroundAsyncTask(
    val progressBar: ProgressBar,
    val progressText: TextView
) : AsyncTask<Int, Int, Int>() {
    var percent: Int = 0

    override fun onPreExecute() {
        super.onPreExecute()
        percent = 0
        progressBar.progress = percent
    }

    override fun doInBackground(vararg params: Int?): Int {
        while (!isCancelled) {
            percent++
            if (percent > 100) {
                break
            } else {
                publishProgress(percent)
            }

            try {
                Thread.sleep(100)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return percent
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        progressBar.progress = values[0] ?: 0
        progressText.text = "퍼센트: ${values[0]}"
    }

    override fun onPostExecute(result: Int?) {
        super.onPostExecute(result)
        progressText.text = "작업이 완료되었습니다."
    }

    override fun onCancelled() {
        super.onCancelled()
        progressBar.progress = 0
        progressText.text = "작업이 취소되었습니다."
    }
}
