package com.example.umarry.cardstack

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.example.umarry.MainActivity
import com.example.umarry.R
import com.example.umarry.auth.UserDataModel
import com.example.umarry.databinding.ActivityMatchingBinding
import com.example.umarry.databinding.ToolbarBinding
import com.example.umarry.setting.SettingActivity
import com.example.umarry.utils.FirebaseAuthUtils
import com.example.umarry.utils.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MatchingActivity:AppCompatActivity() {
    private lateinit var binding: ActivityMatchingBinding
    private lateinit var tbinding: ToolbarBinding
    lateinit var matchingAdapter: MatchingAdapter
    lateinit var manager: CardStackLayoutManager
    private val userDataList = mutableListOf<UserDataModel>()
    private var userCount = 0
    private val uid = FirebaseAuthUtils.getUid()
    private lateinit var currentUserGender: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchingBinding.inflate(layoutInflater)
        tbinding = binding.tbMatching
        setContentView(binding.root)

        tbinding.tbTitle.text = "매칭"
        val cardStackView = binding.cardstackview

        manager = CardStackLayoutManager(baseContext, object : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {
                if(direction == Direction.Left){
                    Log.d("xxxxxxI LIKe U",userDataList[userCount].uid.toString())
                    userLike(uid,userDataList[userCount].uid.toString())
                }

                if(direction == Direction.Right){
//                    Toast.makeText(this@MatchingActivity,"right",Toast.LENGTH_SHORT).show()
                }
                userCount++

                if(userCount == userDataList.count()){
                    getUserDataList(currentUserGender)
                    Toast.makeText(this@MatchingActivity,"user update",Toast.LENGTH_SHORT).show()
                }

            }

            override fun onCardRewound() {

            }

            override fun onCardCanceled() {

            }

            override fun onCardAppeared(view: View?, position: Int) {

            }

            override fun onCardDisappeared(view: View?, position: Int) {

            }

        })

        tbinding.tbMypage.setOnClickListener {
            startActivity(Intent(this,SettingActivity::class.java))
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }

        matchingAdapter = MatchingAdapter(baseContext, userDataList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = matchingAdapter

        binding.bottomNavBar.setItemSelected(R.id.nav_matching,true)
//        getUserDataList(currentUserGender)
        setUpTabBar()
        getMyData()
    }

    private fun userLike(myUid :String, otherUid:String){
        FirebaseRef.memberLikeRef.child(myUid).child(otherUid).setValue("true")
        getOtherLikeList(otherUid)
    }


    private fun getOtherLikeList(otherUid: String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for(dataModel in dataSnapshot.children){
                    val likeUserKey = dataModel.key.toString()
                    if (likeUserKey.equals(uid)){
                        Toast.makeText(this@MatchingActivity,"매칭 완료",Toast.LENGTH_SHORT).show()
//                        createNotificationChannel()
//                        sendNotification()
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("xxxxxx userdata", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.memberLikeRef.child(otherUid).addValueEventListener(postListener)

    }
    private fun getUserDataList(currentUserGender: String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val user = dataModel.getValue(UserDataModel::class.java)
                    if (user!!.gender.toString().equals(currentUserGender)){

                    } else{
                        userDataList.add(user!!)
                    }
                }
                matchingAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("xxxxxx userdata", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.memberRef.addValueEventListener(postListener) // 데이터 가져올 경로.addValue~  , post = 가져올 곳 ??
    }

    private fun getMyData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue(UserDataModel::class.java)
                Log.d("xxxxxx matching gender", data?.gender.toString())

                currentUserGender = data?.gender.toString()
                getUserDataList(currentUserGender)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("xxxxxx mypagedata", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.memberRef.child(uid).addValueEventListener(postListener)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            //channel id 작성
            val channel = NotificationChannel("Test", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(){
        var builder = NotificationCompat.Builder(this, "Test") //이전에 작성한 channel id를 사용
            .setSmallIcon(R.drawable.near_m_icon)
            .setContentTitle("title")
            .setContentText("content")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(123,builder.build())
        }
    }

    private fun setUpTabBar() {
        binding.bottomNavBar.setOnItemSelectedListener {
            when (it) {
                R.id.nav_news -> {
//                    binding.textMain.text = "Near"
                }
                R.id.nav_home -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this, MainActivity::class.java))
                    },250)

//                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
//                    binding.textMain.text = "Chat"
                }
                R.id.nav_matching -> {
                    startActivity(Intent(this,MatchingActivity::class.java))
//                    binding.textMain.text = "Profile"
//                     binding.bottomNavBar.showBadge(R.id.nav_settings)
                }

                R.id.nav_store -> {
//                    binding.textMain.text = "Settings"
//                     binding.bottomNavBar.dismissBadge(R.id.nav_settings)
                }
            }
        }
    }
}