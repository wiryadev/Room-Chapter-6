package com.binar.roomchapter6.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.binar.roomchapter6.Student
import com.binar.roomchapter6.databinding.ActivityAddBinding
import com.binar.roomchapter6.room.StudentDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding

    var mDb: StudentDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDb = StudentDatabase.getInstance(this)

        binding.btnSave.setOnClickListener {
            val objectStudent = Student(
                null,
                binding.etNamaStudent.text.toString(),
                binding.etEmailStudent.text.toString()
            )

            lifecycleScope.launch(Dispatchers.IO) {
                val result = mDb?.studentDao()?.insertStudent(objectStudent)
                runBlocking(Dispatchers.Main) {
                    if (result != 0.toLong()) {
                        //sukses
                        Toast.makeText(
                            this@AddActivity,
                            "Sukses menambahkan ${objectStudent.nama}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        //gagal
                        Toast.makeText(
                            this@AddActivity,
                            "Gagal menambahkan ${objectStudent.nama}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    finish()
                }
            }
        }
    }
}