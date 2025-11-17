package com.example.lab_week_10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.database.TotalObject
import com.example.lab_week_10.viewmodels.TotalViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TotalViewModel
    private lateinit var db: TotalDatabase

    companion object {
        const val ID = 1L
    }

    private fun getDate(): String {
        return SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
            .format(Date())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-db"
        ).allowMainThreadQueries().build()

        viewModel = ViewModelProvider(this).get(TotalViewModel::class.java)

        loadFromDatabase()
        observeLiveData()
        setupButton()
    }

    private fun loadFromDatabase() {
        val record = db.totalDao().getTotal(ID)
        if (record == null) {
            val newData = Total(ID, TotalObject(0, getDate()))
            db.totalDao().insert(newData)
            viewModel.setTotal(0)
            viewModel.setDate(newData.total.date)
        } else {
            viewModel.setTotal(record.total.value)
            viewModel.setDate(record.total.date)
        }
    }

    private fun observeLiveData() {
        viewModel.total.observe(this, Observer { total ->
            findViewById<TextView>(R.id.text_total).text =
                "Total: $total"

            // BONUS: Toast setiap perubahan
            Toast.makeText(this, "Total berubah: $total", Toast.LENGTH_SHORT).show()
        })

        viewModel.date.observe(this, Observer { date ->
            findViewById<TextView>(R.id.text_date).text =
                "Updated: $date"
        })
    }

    private fun setupButton() {
        findViewById<Button>(R.id.button_increment).setOnClickListener {
            val now = getDate()
            viewModel.incrementTotal(now)
        }
    }

    override fun onPause() {
        super.onPause()

        val total = viewModel.total.value ?: 0
        val date = viewModel.date.value ?: getDate()

        db.totalDao().update(
            Total(
                id = ID,
                total = TotalObject(total, date)
            )
        )
    }
}
