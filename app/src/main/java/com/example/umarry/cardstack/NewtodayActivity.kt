package com.example.umarry.cardstack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.umarry.MainActivity
import com.example.umarry.R
import com.example.umarry.auth.UserDataModel
import com.example.umarry.databinding.ActivityMainBinding
import com.example.umarry.databinding.ActivityNewtodayBinding
import com.example.umarry.utils.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class NewtodayActivity:AppCompatActivity() {
    private lateinit var binding: ActivityNewtodayBinding
    lateinit var newtodayAdapter: NewtodayAdapter
    lateinit var manager: CardStackLayoutManager
    private val userDataList = mutableListOf<UserDataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewtodayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setTitle("매칭")
        val cardStackView = binding.cardstackview


//        binding.bottomNavBar.bringToFront()

        manager = CardStackLayoutManager(baseContext, object : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {

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



        newtodayAdapter = NewtodayAdapter(baseContext, userDataList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = newtodayAdapter

        getUserDataList()
        setUpTabBar()
    }

    private fun getUserDataList(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

//
                for(dataModel in dataSnapshot.children){
                    val user = dataModel.getValue(UserDataModel::class.java)
                    userDataList.add(user!!)

                }
                newtodayAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("xxxxxx userdata", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.memberRef.addValueEventListener(postListener) // 데이터 가져올 경로.addValue~  , post = 가져올 곳 ??

    }

    private fun setUpTabBar() {
        binding.bottomNavBar.setOnItemSelectedListener {
            when (it) {
                R.id.nav_news -> {
//                    binding.textMain.text = "Near"
                }
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
//                    binding.textMain.text = "Chat"
                }
                R.id.nav_matching -> {
                    startActivity(Intent(this,NewtodayActivity::class.java))
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