package com.example.umarry.cardstack

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umarry.MainActivity
import com.example.umarry.R
import com.example.umarry.auth.UserDataModel
import com.example.umarry.databinding.ActivityMatchingBinding
import com.example.umarry.utils.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class MatchingActivity:AppCompatActivity() {
    private lateinit var binding: ActivityMatchingBinding
    lateinit var matchingAdapter: MatchingAdapter
    lateinit var manager: CardStackLayoutManager
    private val userDataList = mutableListOf<UserDataModel>()
    private var userCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setTitle("매칭")
        val cardStackView = binding.cardstackview


//        binding.bottomNavBar.bringToFront()

        manager = CardStackLayoutManager(baseContext, object : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {

                if(direction == Direction.Left){
                    Toast.makeText(this@MatchingActivity,"left",Toast.LENGTH_SHORT).show()
                }

                if(direction == Direction.Right){
                    Toast.makeText(this@MatchingActivity,"right",Toast.LENGTH_SHORT).show()
                }
                userCount++

                if(userCount == userDataList.count()){
                    getUserDataList()
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

        matchingAdapter = MatchingAdapter(baseContext, userDataList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = matchingAdapter

        binding.bottomNavBar.setItemSelected(R.id.nav_matching,true)
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
                matchingAdapter.notifyDataSetChanged()

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
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this, MainActivity::class.java))
                    },300)

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