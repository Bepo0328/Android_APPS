package kr.co.bepo.howlstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    // Firebase Authentication 관리 클래스
    private var auth: FirebaseAuth? = null

    // GoogleLogin 관리 클래스
    private var googleSignInClient: GoogleSignInClient? = null

    // Facebook 로그인 처리 결과 관리 클래스
    private var callbackManager: CallbackManager? = null

    //GoogleLogin
    private val GOOGLE_LOGIN_CODE = 9001 // Intent Request ID

    //TwitterLogin
    private var twitterAuthClient: TwitterAuthClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressBar_login.visibility = View.GONE


        // Firebase 로그인 통합 관리하는 Object 만들기
        auth = FirebaseAuth.getInstance()

        /*
        //구글 로그인 옵션
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //구글 로그인 클래스를 만듬
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        callbackManager = CallbackManager.Factory.create()

        //트위터 세팅
        twitterAuthClient = TwitterAuthClient()
        */

        //이메일 로그인 세팅
        button_email_login.setOnClickListener { emailLogin() }

        //구글 로그인 버튼 세팅
        button_google_login.setOnClickListener { googleLogin() }

        //페이스북 로그인 세팅
        button_facebook_login.setOnClickListener { facebookLogin() }

        //트위터 로그인 세팅
        button_twitter_login.setOnClickListener { twitterLogin() }


    }

    private fun emailLogin() {
        if (editText_email.text.toString().isEmpty() || editText_password.text.toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.signout_fail_null), Toast.LENGTH_SHORT).show()
        } else {
            progressBar_login.visibility = View.VISIBLE
            createAndLoginEmail()
        }
    }

    private fun googleLogin() {

    }

    private fun facebookLogin() {

    }

    private fun twitterLogin() {

    }

    //이메일 회원가입 및 로그인 메소드
    private fun createAndLoginEmail() {
        Log.d("LoginEmail","로그인 시작")
        auth?.createUserWithEmailAndPassword(editText_email.text.toString(), editText_password.text.toString())
            ?.addOnCompleteListener { task ->
                progressBar_login.visibility = View.GONE
                if (task.isSuccessful) {
                    //아이디 생성이 성공했을 경우
                    Toast.makeText(this,
                        getString(R.string.signup_complete), Toast.LENGTH_SHORT).show()
                    //다음페이지 호출
                    moveMainPage(auth?.currentUser)
                } else if (task.exception?.message.isNullOrEmpty()) {
                    //회원가입 에러가 발생했을 경우
                    Toast.makeText(this,
                        task.exception!!.message, Toast.LENGTH_SHORT).show()
                } else {
                    //아이디 생성도 안되고 에러도 발생되지 않았을 경우 로그인
                    signinEmail()
                }
            }
    }

    //로그인 메소드
    private fun signinEmail() {
        auth?.signInWithEmailAndPassword(editText_email.text.toString(), editText_password.text.toString())
            ?.addOnCompleteListener { task ->
                progressBar_login.visibility = View.GONE

                if (task.isSuccessful) {
                    //로그인 성공 및 다음페이지 호출
                    moveMainPage(auth?.currentUser)
                } else {
                    //로그인 실패
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun moveMainPage(user: FirebaseUser?) {
        // User is signed in
        if (user != null) {
            Toast.makeText(this, getString(R.string.signin_complete), Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
