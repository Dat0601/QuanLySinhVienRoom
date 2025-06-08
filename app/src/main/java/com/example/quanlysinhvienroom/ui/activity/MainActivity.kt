package com.example.quanlysinhvienroom.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quanlysinhvienroom.databinding.ActivityMainBinding
import com.example.quanlysinhvienroom.ui.adapter.StudentAdapter
import com.example.quanlysinhvienroom.ui.viewmodel.StudentViewModel
import com.example.quanlysinhvienroom.utils.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var studentViewModel: StudentViewModel
    private lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupViewModel() {
        studentViewModel = ViewModelProvider(this)[StudentViewModel::class.java]
    }

    private fun setupRecyclerView() {
        studentAdapter = StudentAdapter(
            onItemClick = { student ->
                val intent = Intent(this, AddEditStudentActivity::class.java).apply {
                    putExtra(Constants.EXTRA_STUDENT_ID, student.id)
                }
                startActivity(intent)
            },
            onDeleteClick = { student ->
                showDeleteConfirmDialog(student)
            }
        )

        binding.rvStudents.apply {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setupObservers() {
        studentViewModel.allStudents.observe(this) { students ->
            studentAdapter.submitList(students)
            binding.tvEmpty.visibility = if (students.isEmpty()) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }
        }

        studentViewModel.message.observe(this) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupListeners() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddEditStudentActivity::class.java))
        }

        binding.etSearch.addTextChangedListener { text ->
            val searchQuery = text.toString().trim()
            if (searchQuery.isNotEmpty()) {
                studentViewModel.searchStudents(searchQuery).observe(this) { students ->
                    studentAdapter.submitList(students)
                }
            } else {
                studentViewModel.allStudents.observe(this) { students ->
                    studentAdapter.submitList(students)
                }
            }
        }
    }

    private fun showDeleteConfirmDialog(student: com.example.quanlysinhvienroom.data.entity.Student) {
        AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa sinh viên ${student.fullName}?")
            .setPositiveButton("Xóa") { _, _ ->
                studentViewModel.deleteStudent(student)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}