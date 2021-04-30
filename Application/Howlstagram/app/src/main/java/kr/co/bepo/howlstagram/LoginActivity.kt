package kr.co.bepo.howlstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    // Firebase Authentication 관리 클래스
    private var auth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null

    // GoogleLogin 관리 클래스
    private var googleSignInClient: GoogleSignInClient? = null

    // Facebook 로그인 처리 결과 관리 클래스
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //구글 로그인 옵션
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        callbackManager = CallbackManager.Factory.create()
    }

    override fun onStart() {
        super.onStart()
        currentUser = auth?.currentUser
        moveMainPage(currentUser)
    }

    init {
        // Firebase 로그인 통합 관리하는 Object 만들기
        auth = Firebase.auth
    }

    // 이메일 로그인
    fun signInAndSignUp(view: View) {
        if (emailEditText.text.isNullOrBlank() || passwordEditText.text.isNullOrBlank()) {
            return
        }
        progressBar.visibility = View.VISIBLE
        auth?.createUserWithEmailAndPassword(
            emailEditText.text.toString(),
            passwordEditText.text.toString()
        )
            ?.addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    // 아이디 생성이 성공했을 경우
                    currentUser = auth?.currentUser
                    moveMainPage(currentUser)
                } else {
                    // 아이디 생성이 안됬을 경우 로그인
                    signInEmail()
                }
            }
    }

    // 로그인 함수
    private fun signInEmail() {
        auth?.signInWithEmailAndPassword(
            emailEditText.text.toString(),
            passwordEditText.text.toString()
        )
            ?.addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    // 로그인 성공
                    currentUser = auth?.currentUser
                    moveMainPage(currentUser)
                } else {
                    // 로그인 실패
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    // 익명 로그인
    fun anonymousLogin(view: View) {
        progressBar.visibility = View.VISIBLE
        auth?.signInAnonymously()
            ?.addOnCompleteListener() { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    currentUser = auth?.currentUser
                    moveMainPage(currentUser)
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    // 구글 로그인
    fun googleLogin(view: View) {
        progressBar.visibility = View.VISIBLE
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    currentUser = auth?.currentUser
                    moveMainPage(currentUser)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    // 페이스북 로그인
    fun facebookLogin(view: View) {
        progressBar.visibility = View.VISIBLE
        LoginManager.getInstance()
            .logInWithReadPermissions(this, listOf("public_profile", "email"))
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    Log.d(TAG, "facebook:onSuccess:$result")
                    handleFacebookAccessToken(result?.accessToken!!)
                }

                override fun onCancel() {
                    Log.d(TAG, "facebook:onCancel")
                    progressBar.visibility = View.GONE
                }

                override fun onError(error: FacebookException?) {
                    Log.d(TAG, "facebook:onError", error)
                    progressBar.visibility = View.GONE
                }
            })
    }

    // Facebook 토큰을 Firebase 로 넘겨주는 코드
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    currentUser = auth?.currentUser
                    moveMainPage(currentUser)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Facebook SDK 로 값 넘겨주기
        callbackManager?.onActivityResult(requestCode, resultCode, data)

        // 구글에서 승인된 정보를 가지고 오기
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "firebaseAuthWithGoogle:" + account?.id)
                firebaseAuthWithGoogle(account?.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            showLogAuth()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun showLogAuth() {
        Log.d(TAG, "displayName: ${currentUser?.displayName}")
        Log.d(TAG, "email: ${currentUser?.email}")
        Log.d(TAG, "photoUrl: ${currentUser?.photoUrl}")
        Log.d(TAG, "phoneNumber: ${currentUser?.phoneNumber}")
        Log.d(TAG, "isAnonymous: ${currentUser?.isAnonymous}")
        Log.d(TAG, "metadata: ${currentUser?.metadata}")
        Log.d(TAG, "providerId: ${currentUser?.providerId}")
        Log.d(TAG, "isEmailVerified: ${currentUser?.isEmailVerified}")
        Log.d(TAG, "uid: ${currentUser?.uid}")
    }

    companion object {
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 9001 //GoogleLogin Intent Request ID
    }

}