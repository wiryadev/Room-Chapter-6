package com.binar.roomchapter6.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.binar.roomchapter6.StudentAdapter
import com.binar.roomchapter6.databinding.ActivityMainBinding
import com.binar.roomchapter6.room.StudentDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mDB: StudentDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDB = StudentDatabase.getInstance(this)

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        fetchData()

        binding.fabAdd.setOnClickListener {
            val keActivityAdd = Intent(this, AddActivity::class.java)
            startActivity(keActivityAdd)
        }
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun fetchData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val listStudent = mDB?.studentDao()?.getAllStudent()

            runBlocking(Dispatchers.Main) {
                listStudent?.let {
                    val adapter = StudentAdapter(
                        listStudent = it,
                        onEditClickListener = { student ->
                            val intentKeEditActivity = Intent(
                                this@MainActivity,
                                EditActivity::class.java
                            )

                            intentKeEditActivity.putExtra("student", student)
                            startActivity(intentKeEditActivity)
                        },
                        onDeleteClickListener = { student ->
                            AlertDialog.Builder(this@MainActivity)
                                .setPositiveButton("Ya") { _, _ ->
                                    val mDb = StudentDatabase.getInstance(baseContext)

                                    lifecycleScope.launch(Dispatchers.IO) {
                                        val result = mDb?.studentDao()?.deleteStudent(student)

                                        runBlocking(Dispatchers.Main) {
                                            if (result != 0) {
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    "Data ${student.nama} berhasil dihapus",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            } else {
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    "Data ${student.nama} Gagal dihapus",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }

                                        fetchData()
                                    }
                                }
                                .setNegativeButton(
                                    "Tidak"
                                ) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .setMessage("Apakah Anda Yakin ingin menghapus data ${student.nama}")
                                .setTitle("Konfirmasi Hapus")
                                .create()
                                .show()
                        }
                    )
                    binding.recyclerView.adapter = adapter
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        StudentDatabase.destroyInstance()
    }
}