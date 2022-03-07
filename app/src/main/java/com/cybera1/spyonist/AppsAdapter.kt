package com.cybera1.spyonist

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial

class AppsAdapter(var context1: Context, var stringList: List<String>) :
    RecyclerView.Adapter<AppsAdapter.ViewHolder?>() {
    var sharedPrefManager:Preferences = Preferences.getInstance(context1)!!
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var cardView: CardView
        var imageView: ImageView
        var textView_App_Name: TextView
        var switchAcces:SwitchMaterial

        init {
            cardView = view.findViewById<View>(R.id.card_view) as CardView
            imageView = view.findViewById<View>(R.id.imageview) as ImageView
            textView_App_Name = view.findViewById<View>(R.id.Apk_Name) as TextView
            switchAcces=view.findViewById(R.id.switchAccess) as SwitchMaterial

        }
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val apkInfoExtractor = ApkInfoExtractor(context1)
        val ApplicationPackageName = stringList[position]
        val ApplicationLabelName = apkInfoExtractor.GetAppName(ApplicationPackageName)
        val drawable: Drawable = apkInfoExtractor.getAppIconByPackageName(ApplicationPackageName)
        viewHolder.textView_App_Name.setText(ApplicationLabelName)
//        viewHolder.textView_App_Package_Name.setText(ApplicationPackageName)
        viewHolder.imageView.setImageDrawable(drawable)
//
        if(sharedPrefManager.getappsAcces(ApplicationPackageName)){
            viewHolder.switchAcces.isChecked=false
        }

        //Adding click listener on CardView to open clicked application directly from here .
        viewHolder.switchAcces.setOnClickListener(View.OnClickListener {
            if(viewHolder.switchAcces.isChecked){
                sharedPrefManager.removeappsAccess(context1,ApplicationPackageName)

            }
            else{
                sharedPrefManager.setappsAccess(context1,ApplicationPackageName)
            }
        })
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view2: View =
            LayoutInflater.from(context1).inflate(R.layout.cardview_layout, parent, false)
        return ViewHolder(view2)
    }

    override fun getItemCount(): Int {
        return stringList.size
    }
}