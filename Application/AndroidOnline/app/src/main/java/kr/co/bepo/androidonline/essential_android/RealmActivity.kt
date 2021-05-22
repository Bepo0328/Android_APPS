package kr.co.bepo.androidonline.essential_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration
import kr.co.bepo.androidonline.databinding.ActivityRealmBinding

class RealmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRealmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRealmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .deleteRealmIfMigrationNeeded()
            .build()
        val realm = Realm.getInstance(config)

        binding.saveButton.setOnClickListener {
            realm.executeTransaction {
                with(it.createObject(School::class.java)) {
                    this.name = "어딴 대학교"
                    this.location = "서울"
                }
            }
        }

        binding.loadButton.setOnClickListener {
            realm.executeTransaction {
                val data = it.where(School::class.java).findFirst()
                Log.d(TAG, "data: $data")
            }
        }

        binding.deleteButton.setOnClickListener {
            realm.executeTransaction {
                it.where(School::class.java).findAll().deleteAllFromRealm()
            }
        }
    }

    companion object {
        private const val TAG = "RealmActivity"
    }
}