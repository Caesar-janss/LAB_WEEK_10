package com.example.lab_week_10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.database.TotalObject
import com.example.lab_week_10.viewmodels.TotalViewModel
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TotalViewModel
    private lateinit var db: TotalDatabase

    companion object {
        const val ID: Long = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init database
        db = Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        ).allowMainThreadQueries().build()

        viewModel = ViewModelProvider(this).get(TotalViewModel::class.java)

        initializeValueFromDatabase()
        prepareViewModel()
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel() {
        viewModel.total.observe(this, Observer { total ->
            updateText(total)
        })

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    private fun initializeValueFromDatabase() {
        val record = db.totalDao().getTotal(ID)

        if (record == null) {
            // pertama kali install
            db.totalDao().insert(
                Total(
                    id = ID,
                    total = TotalObject(
                        value = 0,
                        date = Date().toString()
                    )
                )
            )
            viewModel.setTotal(0)
        } else {
            viewModel.setTotal(record.total.value)
        }
    }

    override fun onPause() {
        super.onPause()
        val curValue = viewModel.total.value ?: 0

        db.totalDao().update(
            Total(
                id = ID,
                total = TotalObject(
                    value = curValue,
                    date = Date().toString()
                )
            )
        )
    }
}
