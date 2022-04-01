package com.binar.roomchapter6.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.binar.roomchapter6.R
import com.binar.roomchapter6.Student
import com.binar.roomchapter6.databinding.ActivityEditBinding
import com.binar.roomchapter6.room.StudentDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding

    var mDb: StudentDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDb = StudentDatabase.getInstance(this)

        val objectStudent = intent.getParcelableExtra<Student>("student")

        if (objectStudent != null) {
            binding.etNamaStudent.setText(objectStudent.nama)
            binding.etEmailStudent.setText(objectStudent.email)

            binding.btnSave.setOnClickListener {
                objectStudent.nama = binding.etNamaStudent.text.toString()
                objectStudent.email = binding.etEmailStudent.text.toString()

                lifecycleScope.launch(Dispatchers.IO) {
                    val result = mDb?.studentDao()?.updateStudent(objectStudent)

                    runBlocking(Dispatchers.Main) {
                        if (result != 0) {
                            Toast.makeText(
                                this@EditActivity,
                                "Sukses mengubah ${objectStudent.nama}",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this@EditActivity,
                                "Gagal mengubah ${objectStudent.nama}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        finish()
                    }
                }
            }
        }
    }
}