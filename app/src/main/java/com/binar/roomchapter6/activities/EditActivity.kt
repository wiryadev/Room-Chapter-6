package com.binar.roomchapter6.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.binar.roomchapter6.R
import com.binar.roomchapter6.Student
import com.binar.roomchapter6.room.StudentDatabase
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class EditActivity : AppCompatActivity() {
    var mDb: StudentDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        mDb = StudentDatabase.getInstance(this)

        val objectStudent = intent.getParcelableExtra<Student>("student")

        etNamaStudent.setText(objectStudent.nama)
        etEmailStudent.setText(objectStudent.email)

        btnSave.setOnClickListener {
            objectStudent.nama = etNamaStudent.text.toString()
            objectStudent.email = etEmailStudent.text.toString()

            GlobalScope.async {
                val result = mDb?.studentDao()?.updateStudent(objectStudent)

                runOnUiThread {
                    if(result!=0){
                        Toast.makeText(this@EditActivity,"Sukses mengubah ${objectStudent.nama}", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@EditActivity,"Gagal mengubah ${objectStudent.nama}", Toast.LENGTH_LONG).show()
                    }

                    finish()
                }
            }
        }
    }
}