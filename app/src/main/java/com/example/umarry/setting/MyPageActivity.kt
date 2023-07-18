package com.example.umarry.setting

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION_CODES.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.umarry.auth.UserDataModel
import com.example.umarry.utils.FirebaseAuthUtils
import com.example.umarry.utils.FirebaseRef
import com.example.umarry.databinding.ActivityMyPageBinding
import com.example.umarry.databinding.ToolbarBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MyPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageBinding
//    private lateinit var tbinding: ToolbarBinding
    private val uid = FirebaseAuthUtils.getUid()
    val PICK_IMAGE_REQUEST = 2020
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
//        tbinding = binding.tbPage
        setContentView(binding.root)

        val myName = binding.myName
        val myNickname = binding.myNickname
        val myAge = binding.myAge
        val myArea = binding.myArea
        val myGender = binding.myGender
        val myImg = binding.myImage
        val myJob = binding.myJob
        val myPassword = binding.myPassword
        val myPasswordCH = binding.mypasswordCh

        binding.backBtn.setOnClickListener {
            onBackPressed()
            overridePendingTransition(com.example.umarry.R.anim.slide_from_left, com.example.umarry.R.anim.slide_to_right)
        }
//        binding.tbTitle.setOnClickListener {
//            Log.e("xxxxxxxxxxxxxxxxxx","클릭했음")
//            onBackPressed()
//            Log.e("xxxxxxxxxxxxxxxxxx","클릭됐음")
//
//            overridePendingTransition(com.example.umarry.R.anim.slide_from_left, com.example.umarry.R.anim.slide_to_right)
//        }

        getMyData()
//        setMyData()
    }

    private fun chooseImage(){
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_REQUEST)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == PICK_IMAGE_REQUEST && resultCode != null){
//            filePath = data!!.data
//            try{
//                val bitmap:Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filePath)
//
//            }
//        }
//    }

    private fun setMyData(){
        val myName = binding.myName
        val myNickname = binding.myNickname
        val myAge = binding.myAge
        val myArea = binding.myArea
        val myImg = binding.myImage
        val myJob = binding.myJob
        val myPassword = binding.myPassword
        val myPasswordCH = binding.mypasswordCh
        binding.checkbtn.setOnClickListener {
            if(binding.checkbtn.text.equals("수정하기")) {
                myName.isEnabled = true
                myNickname.isEnabled = true
                myAge.isEnabled = true
                myArea.isEnabled = true
                myJob.isEnabled = true
                myPassword.isEnabled = true
                myPasswordCH.isEnabled = true
                myPassword.visibility = View.VISIBLE
                myPasswordCH.visibility = View.VISIBLE
                binding.checkbtn.text = "저장하기"
            }else{
                //todo db에 저장하기
                //todo editText isEnabled = false로 바꾸기
            }
        }
    }

    private fun getMyData(){

        val myName = binding.myName
        val myNickname = binding.myNickname
        val myAge = binding.myAge
        val myArea = binding.myArea
        val myGender = binding.myGender
        val myImg = binding.myImage
        val myJob = binding.myJob
        val myPassword = binding.myPassword
        val myPasswordCH = binding.mypasswordCh


        val postListener = object : ValueEventListener {
            @RequiresApi(O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("xxxxxx mypagedata", dataSnapshot.toString())
                val data = dataSnapshot.getValue(UserDataModel::class.java)
                var date = LocalDate.now()

                val formatter = DateTimeFormatter.ofPattern("yyyy")
                val formatted = date.format(formatter)
                val uid = FirebaseAuthUtils.getUid()

                myName.setText(data!!.name)
                myNickname.setText(data!!.nickname)
                myAge.setText((formatted.toInt()-data.birth!!.substring(0,4).toInt()).toString())
                myArea.setText(data!!.bigArea +" "+ data!!.smallArea)
                myGender.setText(data!!.gender)
                myJob.setText(data!!.job)



                // mypage여서 현재 사용자의 uid를 get
                val storageRef = Firebase.storage.reference.child(uid).child("01.png")
                storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                    if(task.isSuccessful){
                        Glide.with(baseContext)
                            .load(task.result)
                            .into(myImg)
                    }
                })



            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("xxxxxx mypagedata", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.memberRef.child(uid).addValueEventListener(postListener) // 데이터 가져올 경로.addValue~  , post = 가져올 곳 ??

    }
}