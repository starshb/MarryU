package com.example.umarry.cardstack

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.umarry.R
import com.example.umarry.auth.UserDataModel
import com.example.umarry.utils.FirebaseAuthUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MatchingAdapter(val context: Context, val items: MutableList<UserDataModel>) :
    RecyclerView.Adapter<MatchingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchingAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view :View =  inflater.inflate(R.layout.itemcard, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder : MatchingAdapter.ViewHolder, position: Int) {
        holder.binding(items[position]) // 어댑터 ViewHolder의 binding 함수를 실행하여 UserDataModel item 홀딩함
    }

    override fun getItemCount(): Int {
        return items.size
    }

    //view와 data를 연결시켜주는 inner class
    // 각각의 View에는 어떤 정보가 맵핑되야하는지를 알려줌
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.itemimg)
        val nickname = itemView.findViewById<TextView>(R.id.itemNickname)
        val age = itemView.findViewById<TextView>(R.id.itemAge)
        val bigArea = itemView.findViewById<TextView>(R.id.itembigArea)
        val smallArea = itemView.findViewById<TextView>(R.id.itemsmallArea)
        @RequiresApi(Build.VERSION_CODES.O)
        var date = LocalDate.now()
        @RequiresApi(Build.VERSION_CODES.O)
        val formatter = DateTimeFormatter.ofPattern("yyyy")
        @RequiresApi(Build.VERSION_CODES.O)
        val formatted = date.format(formatter)
        val uid = FirebaseAuthUtils.getUid()


        //데이터와 요소 바인딩꼬부Rhqnrl
        // todo 나이 디테일한 설정 안됐음
        @RequiresApi(Build.VERSION_CODES.O)
        fun binding(data : UserDataModel) {

            // storage 에서 이미지 불러오기 의 child를 현재 접속이 아닌 datamodel의 uid를 끌어다 시도
            val storageRef = Firebase.storage.reference.child(data.uid!!).child("01.png")
            storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                if(task.isSuccessful){
                    Glide.with(context)
                        .load(task.result)
                        .into(image)
                }
            })
            nickname.text = data.nickname
            age.text =  (formatted.toInt()-data.birth!!.substring(0,4).toInt()).toString()
            bigArea.text  = data.bigArea
            smallArea.text  = data.smallArea
            Log.d("xxxxxdateformatted",formatted)
        }
    }

}