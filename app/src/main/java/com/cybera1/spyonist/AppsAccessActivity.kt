package com.cybera1.spyonist

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial

class AppsAccessActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    lateinit var adapter:AppsAdapter
    lateinit var lockScreen: SwitchMaterial
    var recyclerViewLayoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apps_access)

        recyclerView = findViewById(R.id.recycler_view) as RecyclerView

        recyclerViewLayoutManager = GridLayoutManager(this, 1)

        recyclerView!!.setLayoutManager(recyclerViewLayoutManager)

        lockScreen=findViewById(R.id.onlockscreen)

        var sharedPrefManager:Preferences = Preferences.getInstance(this)!!

        if(sharedPrefManager.getBoolean(this,"lockScreen",true)){
            lockScreen.isChecked=true
        }
        else{
            lockScreen.isChecked=false
        }

        lockScreen.setOnClickListener {
            if(lockScreen.isChecked){
                sharedPrefManager.setBoolean(this,"lockScreen",true)
            }
            else{
                sharedPrefManager.setBoolean(this,"lockScreen",false)
            }
        }

        adapter = AppsAdapter(
            this,
            ApkInfoExtractor(this).GetAllInstalledApkInfo()
        )

        recyclerView!!.setAdapter(adapter)
    }
}