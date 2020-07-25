package com.example.firebaseauth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        //Şifrenin aynı olduğunu kontrol için fonksiyon tanımlıyoruz.

        btn_Kayit.setOnClickListener {
            if(et_Mail.text.isNotEmpty() && et_Sifre.text.isNotEmpty() && et_Sifre_Tekrar.text.isNotEmpty()){
                if (et_Sifre.text.toString().equals(et_Sifre_Tekrar.text.toString())){

                    kullaniciKayit(et_Mail.text.toString(), et_Sifre.text.toString())

                }else{
                    Toast.makeText(this,"Şifreler aynı değil", Toast.LENGTH_SHORT).show()
                }

            }else{

                Toast.makeText(this,"Boş alanları doldurunuz", Toast.LENGTH_SHORT).show()
            }
        }
    }
        //Yeni kullanıcı kayıt konrolü için fonksiyon tanımlıyoruz.

    private fun kullaniciKayit(mail: String, sifre: String) {
        progressBarGoster()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail,sifre)
            .addOnCompleteListener(object:OnCompleteListener<AuthResult>{
                override fun onComplete(p0: Task<AuthResult>) {

                    if(p0.isSuccessful){
                        Toast.makeText(this@RegisterActivity,"Üye kaydedildi:"+FirebaseAuth.getInstance().currentUser?.email, Toast.LENGTH_SHORT).show()
                        mailGonder()
                        FirebaseAuth.getInstance().signOut()
                    }else{
                        Toast.makeText(this@RegisterActivity,"Üye kaydedilirken sorun oluştu:"+p0.exception?.message, Toast.LENGTH_SHORT).show()
                    }




                }

            })
        progressBarGizle()

    }

    //Mail onaylama işlemleri için bildirimleri/onaylama sürecini oluşturuyoruz.

    private fun mailGonder(){

        var kullanici=FirebaseAuth.getInstance().currentUser
        if (kullanici != null){

            kullanici.sendEmailVerification()
                .addOnCompleteListener(object : OnCompleteListener<Void>{
                    override fun onComplete(p0: Task<Void>) {
                        if(p0.isSuccessful){
                            Toast.makeText(this@RegisterActivity,"Mailinizi kontrol edin, mailinizi onaylayın", Toast.LENGTH_SHORT).show()

                        }else{

                            Toast.makeText(this@RegisterActivity,"Mail gönderilirken sorun oluştu "+p0.exception?.message, Toast.LENGTH_SHORT).show()

                        }
                    }

                })
        }
    }

    private fun progressBarGoster(){
        progressBar.visibility = View.VISIBLE
    }
    private fun progressBarGizle(){
        progressBar.visibility = View.INVISIBLE
    }
}