package com.example.studentrecordapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private var students: List<Student>,
    private val onItemClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStudentName: TextView = itemView.findViewById(R.id.tvStudentName)
        val tvClassInfo: TextView = itemView.findViewById(R.id.tvRollNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.tvStudentName.text = student.name
        holder.tvClassInfo.text = "Class: ${student.studentClass} | ${student.level}"
        holder.itemView.setOnClickListener { onItemClick(student) }
    }

    override fun getItemCount() = students.size

    fun updateData(newStudents: List<Student>) {
        students = newStudents
        notifyDataSetChanged()
    }
}