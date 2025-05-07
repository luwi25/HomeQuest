package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SubmitTaskActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_task)

        val chk = findViewById<CheckBox>(R.id.chk)

        val button_submit = findViewById<Button>(R.id.button_submit)
        button_submit.setOnClickListener {
            if(chk.isChecked)
            {
                Toast.makeText(this, "Task Finished!", Toast.LENGTH_SHORT).show()
            }
        }

        val button_back = findViewById<ImageButton>(R.id.button_back)
        button_back.setOnClickListener {
            startActivity(
                Intent(this, ToDoTaskActivity::class.java)
            )
        }

    }
}