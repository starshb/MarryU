package com.example.umarry

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.umarry.auth.UserDataModel
import com.example.umarry.cardstack.MatchingActivity
import com.example.umarry.databinding.ActivityMainBinding
import com.example.umarry.databinding.ToolbarBinding
import com.example.umarry.recycler.RecyclerView01Adapter
import com.example.umarry.recycler.RecyclerView02Adapter
import com.example.umarry.recycler.RecyclerView03Adapter
import com.example.umarry.setting.SettingActivity
import com.example.umarry.utils.FirebaseAuthUtils
import com.example.umarry.utils.FirebaseRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

//전화번호, 이메일 유니크
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
//    private lateinit var tbinding : ToolbarBinding  //include Layout 바인딩
    private var isExpanded = false
    lateinit var rcv01Adapter : RecyclerView01Adapter
    lateinit var rcv02Adapter : RecyclerView02Adapter
    lateinit var rcv03Adapter : RecyclerView03Adapter
    private val userDataList01 = mutableListOf<UserDataModel>()
    private val userDataList02 = mutableListOf<UserDataModel>()
    private val userDataList03 = mutableListOf<UserDataModel>()
    private val uid = FirebaseAuthUtils.getUid()
    private lateinit var currentUserGender: String
    private lateinit var currentUserArea: String


    private val fromBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_bottom_fab)
    }
    private val toBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom_fab)
    }
    private val rotateClockWiseFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_clock_wise)
    }
    private val rotateAntiClockWiseFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_anti_clock_wise)
    }
    private val fromBottomBgAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim)
    }
    private val toBottomBgAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
//        tbinding = binding.tbMain
        setContentView(binding.root)
        supportActionBar?.setTitle("") // 제목없음설정
//        actionBar?.show()
        val user = Firebase.auth.currentUser
        Log.d("MAINxxxxx uid",uid)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("TOKENFaild", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            Log.e("TOKEN",token )
        })

        binding.tbMypage.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }

        rcv01Adapter = RecyclerView01Adapter(this, userDataList01)
        binding.rcv01.adapter = rcv01Adapter
        rcv02Adapter = RecyclerView02Adapter(this, userDataList02)
        binding.rcv02.adapter = rcv02Adapter
        rcv03Adapter = RecyclerView03Adapter(this, userDataList03)
        binding.rcv03.adapter = rcv03Adapter



        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.ppl_01))
        imageList.add(SlideModel(R.drawable.ppl_02))
        imageList.add(SlideModel(R.drawable.ppl_03))
        imageList.add(SlideModel(R.drawable.ppl_04))
        imageList.add(SlideModel(R.drawable.ppl_05))

        binding.imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)

        binding.mainFabBtn.setOnClickListener {
            if (isExpanded) {
                shrinkFab()
            } else {
                expandFab()
            }
        }

        binding.chatbotFabBtn.setOnClickListener {
            onchatbotClicked()
        }

        binding.chatbotTv.setOnClickListener {
            onchatbotClicked()
        }

        binding.bottomNavBar.setItemSelected(R.id.nav_home,true)
        getMyData()
        setUpTabBar()

    }

    /*-------------recyclerView*/
    private fun getUserDataList(currentUserGender: String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val user = dataModel.getValue(UserDataModel::class.java)
                    if (user!!.gender.toString().equals(currentUserGender)){

                    } else{
                        userDataList01.add(user!!)
                    }
                }
                rcv01Adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("xxxxxx userdata", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.memberRef.addValueEventListener(postListener) // 데이터 가져올 경로.addValue~  , post = 가져올 곳 ??
    }

    private fun getUserDataListArea(currentUserArea: String,currentUserGender: String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val user = dataModel.getValue(UserDataModel::class.java)
                    if(user!!.gender.toString().equals(currentUserGender)){

                    } else{
                        if (user!!.bigArea.toString().equals(currentUserArea)){
                            userDataList02.add(user!!)
                        } else{
                            userDataList03.add(user!!)
                        }
                    }

                }
                if(userDataList02 == null){
                    val textV = findViewById<TextView>(R.id.rcv02_nickname)
                    textV.text = "가까운 회원이 없습니다"
                    // todo 같은 동네 친구가 검색 되지 않을 경우 처리
                }
                rcv02Adapter.notifyDataSetChanged()
                rcv03Adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("xxxxxx userdata", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.memberRef.addValueEventListener(postListener) // 데이터 가져올 경로.addValue~  , post = 가져올 곳 ??
    }

//    private fun getUserDataListALl(currentUserGender: String){
//        val postListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for(dataModel in dataSnapshot.children){
//                    val user = dataModel.getValue(UserDataModel::class.java)
//                    userDataList01.add(user!!)
//                }
//                rcv03Adapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//                Log.w("xxxxxx userdata", "loadPost:onCancelled", databaseError.toException())
//            }
//        }
//        FirebaseRef.memberRef.addValueEventListener(postListener) // 데이터 가져올 경로.addValue~  , post = 가져올 곳 ??
//    }

    private fun getMyData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue(UserDataModel::class.java)
                Log.d("xxxxxx matching gender", data?.gender.toString())

                currentUserGender = data?.gender.toString()
                currentUserArea = data?.bigArea.toString()
                getUserDataList(currentUserGender)
                getUserDataListArea(currentUserArea,currentUserGender)
//                getUserDataListALl()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("xxxxxx mypagedata", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.memberRef.child(uid).addValueEventListener(postListener)
    }


    /*-------------floating button*/
    private fun onchatbotClicked() {
        Toast.makeText(this, "Gallery Clicked", Toast.LENGTH_SHORT).show()
    }

    private fun shrinkFab() {
        binding.mainFabBtn.startAnimation(rotateAntiClockWiseFabAnim)
        binding.chatbotFabBtn.startAnimation(toBottomFabAnim)
        binding.settingFabBtn.startAnimation(toBottomFabAnim)
        binding.callFabBtn.startAnimation(toBottomFabAnim)
        binding.chatbotTv.startAnimation(toBottomFabAnim)
        binding.settingTv.startAnimation(toBottomFabAnim)
        binding.callTv.startAnimation(toBottomFabAnim)

        isExpanded = !isExpanded
    }

    private fun expandFab() {
        binding.mainFabBtn.startAnimation(rotateClockWiseFabAnim)
        binding.chatbotFabBtn.startAnimation(fromBottomFabAnim)
        binding.settingFabBtn.startAnimation(fromBottomFabAnim)
        binding.callFabBtn.startAnimation(fromBottomFabAnim)
        binding.chatbotTv.startAnimation(fromBottomFabAnim)
        binding.settingTv.startAnimation(fromBottomFabAnim)
        binding.callTv.startAnimation(fromBottomFabAnim)

        isExpanded = !isExpanded
    }

    override fun onBackPressed() {
        if (isExpanded) {
            shrinkFab()
        } else {
            super.onBackPressed()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            if (isExpanded) {
                val outRect = Rect()
                binding.fabConstraint.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    shrinkFab()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun setUpTabBar() {
        binding.bottomNavBar.setOnItemSelectedListener {
            when (it) {
                R.id.nav_news ->{
//                    binding.textMain.text = "Near"
                }
                R.id.nav_home -> {
                    startActivity(Intent(this,MainActivity::class.java))
//                    binding.textMain.text = "Chat"
                }

                R.id.nav_matching -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this,MatchingActivity::class.java))
                    },250)

//                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
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