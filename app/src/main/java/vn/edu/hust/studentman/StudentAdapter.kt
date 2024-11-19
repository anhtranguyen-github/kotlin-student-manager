package vn.edu.hust.studentman

import android.app.AlertDialog
import android.app.Dialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlin.collections.addAll
import kotlin.text.clear

class StudentAdapter(val students: List<StudentModel>): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
  class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
    val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
    val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
    val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_student_item,
       parent, false)
    return StudentViewHolder(itemView)
  }

  override fun getItemCount(): Int = students.size

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    val student = students[position]

    holder.textStudentName.text = student.studentName
    holder.textStudentId.text = student.studentId
    holder.imageEdit.setOnClickListener {
      val dialog = Dialog(holder.itemView.context)
      dialog.setContentView(R.layout.edit_dia)
      setupeditDialog(dialog, holder, position)
      dialog.setCancelable(true)
      dialog.setCanceledOnTouchOutside(true)
      dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
      )
      dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
      dialog.window?.setGravity(Gravity.CENTER)
      dialog.show()
    }
    holder.imageRemove.setOnClickListener {
      val builder = AlertDialog.Builder(holder.itemView.context)
      builder.setTitle("Xac nhan xoa")
      builder.setMessage("Ban co that su muon xoa khong?")
      builder.setPositiveButton("Yes") { dialog, which ->
        val removedStudent = students[position]
        val updatedStudents = students.filterIndexed { index, _ -> index != position }
        (holder.itemView.context as? MainActivity)?.updateStudentList(updatedStudents)

        Snackbar.make(
          (holder.itemView.context as? MainActivity)?.findViewById(android.R.id.content)!!,
          "Student removed",
          Snackbar.LENGTH_LONG
        )
          .setAction("Undo") {
            (holder.itemView.context as? MainActivity)?.restoreStudent(removedStudent, position)
          }
          .show()

      }
      builder.setNegativeButton("No") { dialog, which ->
        dialog.dismiss()
      }
      val dialog = builder.create()
      dialog.show()
    }
  }

  private fun setupeditDialog(dialog: Dialog,holder: StudentViewHolder, position: Int) {
    val changeButton = dialog.findViewById<Button>(R.id.btnchange)
    if (changeButton != null) {
      changeButton?.setOnClickListener {
        val name: String = dialog.findViewById<EditText>(R.id.editTextText_1).text.toString()
        val mssv: String = dialog.findViewById<EditText>(R.id.editTextText_2).text.toString()
        val student = students[position]
        student.studentName = name
        student.studentId = mssv
        holder.textStudentName.text = student.studentName
        holder.textStudentId.text = student.studentId
        dialog.dismiss() }
    }
  }


}