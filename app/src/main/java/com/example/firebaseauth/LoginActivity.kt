package com.example.firebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    lateinit var mAuthStateListener : FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initAuthStateListener()

        //Kayıt ol butonunun işlevi için fonksiyon tanımlıyoruz.

        tvKayitOl.setOnClickListener{

            var intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
        //Giriş butonunun işlevi için fonksiyon tanımlıyoruz.

        btn_Giris.setOnClickListener {

            if (et_Mail.text.isNotEmpty() && et_Sifre.text.isNotEmpty()){

                progressBarGoster()

                // Firebase kütüphanesinden email ve password parametrelerine tanımladığımız edit textleri Stringe çeviriyoruz.

                FirebaseAuth.getInstance().signInWithEmailAndPassword(et_Mail.text.toString(),et_Sifre.text.toString())

                    .addOnCompleteListener(object:OnCompleteListener<AuthResult>{

                     // Kullanıcı başarılı giriş yaptığında bildirim oluşturuyoruz.

                        override fun onComplete(p0: Task<AuthResult>) {

                            if(p0.isSuccessful){
                                progressBarGizle()
                              //  Toast.makeText(this@LoginActivity,"Başarılı Giriş: "+FirebaseAuth.getInstance().currentUser?.email, Toast.LENGTH_SHORT).show()
                                FirebaseAuth.getInstance().signOut()

                            }else{

                                // Kullanıcı hatalı giriş yaptığında bildirim oluşturuyoruz.

                                progressBarGizle()
                                Toast.makeText(this@LoginActivity,"Hatalı Giriş: "+p0.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })

            }else{
                // Kullanıcı alanı boş bıraktığında bildirim oluşturuyoruz.

                Toast.makeText(this@LoginActivity,"Boş alanları doldurunuz", Toast.LENGTH_SHORT).show()
            }

        }
    }

    //Progress bar/yükleme ikonunu fonksiyonlar ile tanımlıyoruz
    //Buton işlemine bağlı goster/gizle şeklinde iki fonksiyon tanımluyoruz.

    private fun progressBarGoster(){
        progress_Login.visibility = View.VISIBLE
    }
    private fun progressBarGizle(){
        progress_Login.visibility = View.INVISIBLE
    }

    // Mail onaylama işlemlerinin bildirimlerini ve kontrolünü sağlıyoruz.

    private fun initAuthStateListener(){

        mAuthStateListener=object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var kullanici=p0.currentUser

                if (kullanici != null){

                    if(kullanici.isEmailVerified){
                        Toast.makeText(this@LoginActivity,"Mail onaylandı giriş yapabilirsiniz", Toast.LENGTH_SHORT).show()
                        var intent=Intent(this@LoginActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        Toast.makeText(this@LoginActivity,"Mail adresinizi onaylamadan giriş yapamazsınız", Toast.LENGTH_SHORT).show()


                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener {mAuthStateListener}
    }
}