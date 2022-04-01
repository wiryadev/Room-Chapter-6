package com.binar.roomchapter6

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.binar.roomchapter6.databinding.StudentItemBinding

class StudentAdapter(
    private val listStudent: List<Student>,
    private val onEditClickListener: (Student) -> Unit,
    private val onDeleteClickListener: (Student) -> Unit,
) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    class ViewHolder(val binding: StudentItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StudentItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listStudent.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvID.text = listStudent[position].id.toString()
        holder.binding.tvNama.text = listStudent[position].nama
        holder.binding.tvEmail.text = listStudent[position].email

        holder.binding.ivEdit.setOnClickListener {
            onEditClickListener.invoke(listStudent[position])
        }

        holder.binding.ivDelete.setOnClickListener {
            onDeleteClickListener.invoke(listStudent[position])
        }
    }
}