package com.example.umarry.auth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.umarry.MainActivity
import com.example.umarry.databinding.ActivityRegister03Binding
import com.example.umarry.utils.FirebaseAuthUtils
import com.example.umarry.utils.FirebaseRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class RegisterActivity03 : AppCompatActivity() {
    private lateinit var binding: ActivityRegister03Binding
    lateinit var auth:FirebaseAuth
    lateinit var storage:FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister03Binding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding.root)

        // 핸드폰에서 이미지 불러오기
        val getAction01 = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                binding.img01.setImageURI(uri)
            }
        )
        val getAction02 = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                binding.img02.setImageURI(uri)
            }
        )
        val getAction03 = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                binding.img03.setImageURI(uri)
            }
        )

        binding.img01.setOnClickListener {
            getAction01.launch("image/*")
        }
        binding.img02.setOnClickListener {
            getAction02.launch("image/*")
        }
        binding.img03.setOnClickListener {
            getAction03.launch("image/*")
        }



        binding.signUpButton.setOnClickListener {
            // binding
            var img01 = binding.img01
            var img02 = binding.img02
            var img03 = binding.img03

            var name = intent.getStringExtra("name")
            var nickname = intent.getStringExtra("nickname")
            var gender = intent.getStringExtra("gender")
            var phone = intent.getStringExtra("phone")
            var birth = intent.getStringExtra("birth")
            val email = intent.getStringExtra("email")
            var password = intent.getStringExtra("password")
            var job = intent.getStringExtra("job")
            var ability = intent.getStringExtra("ability")
            var stature = intent.getStringExtra("stature")
            var bigArea = intent.getStringExtra("bigArea")
            var smallArea = intent.getStringExtra("smallArea")
            var yStature = intent.getStringExtra("yStature")
            var yAbility = intent.getStringExtra("yAbility")

            if(email!!.isNotEmpty() && password!!.isNotEmpty()) {
//            if(img01.)
                // [START create_user_with_email]
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information

                            val uid = FirebaseAuthUtils.getUid()

                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                OnCompleteListener { task ->
                                    if (!task.isSuccessful) {
                                        Log.e("TOKENFaild", "Fetching FCM registration token failed", task.exception)
                                        return@OnCompleteListener
                                    }
                                    // Get new FCM registration token
                                    val token = task.result
                                    val userModel = UserDataModel(
                                        uid,
                                        name,
                                        nickname,
                                        gender,
                                        phone,
                                        birth,
                                        email,
                                        password,
                                        job,
                                        ability,
                                        stature,
                                        bigArea,
                                        smallArea,
                                        yStature,
                                        yAbility,
                                        token
                                    )
                                    FirebaseRef.memberRef.child(uid).setValue(userModel)
                                    uploadImage01(uid)
                                    uploadImage02(uid)
                                    uploadImage03(uid)
                                    Toast.makeText(this, "회원가입에 성공했습니다!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, MainActivity::class.java)) // activity 이동
                                    finish()

                                Log.e("TOKEN",token )
                            })

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                this,
                                "회원가입에 실패했습니다. 다시 한 번 시도해주세요.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
            // [END create_user_with_email]
        }

        binding.registerHeader.setOnClickListener {
            onBackPressed()
        }

    }

    private fun uploadImage01(uid:String){
        storage = Firebase.storage
        val storageRef = storage.reference.child(uid).child("01.png")
         // Get the data from an ImageView as bytes
        binding.img01.isDrawingCacheEnabled = true
        binding.img01.buildDrawingCache()
        val bitmap = (binding.img01.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

    }
    private fun uploadImage02(uid:String){
        storage = Firebase.storage
        val storageRef = storage.reference.child(uid).child("02.png")
         // Get the data from an ImageView as bytes
        binding.img02.isDrawingCacheEnabled = true
        binding.img02.buildDrawingCache()
        val bitmap = (binding.img02.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

    }
    private fun uploadImage03(uid:String){
        storage = Firebase.storage
        val storageRef = storage.reference.child(uid).child("03.png")
         // Get the data from an ImageView as bytes
        binding.img03.isDrawingCacheEnabled = true
        binding.img03.buildDrawingCache()
        val bitmap = (binding.img03.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

    }
}