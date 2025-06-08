package com.example.quanlysinhvienroom.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.quanlysinhvienroom.data.entity.Student
import com.example.quanlysinhvienroom.databinding.ActivityAddEditStudentBinding
import com.example.quanlysinhvienroom.ui.viewmodel.StudentViewModel
import com.example.quanlysinhvienroom.utils.Constants
import kotlinx.coroutines.launch

class AddEditStudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditStudentBinding
    private lateinit var studentViewModel: StudentViewModel
    private var studentId: Int = -1
    private var currentStudent: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupUI()
        setupListeners()
    }

    private fun setupViewModel() {
        studentViewModel = ViewModelProvider(this)[StudentViewModel::class.java]
    }

    private fun setupUI() {
        studentId = intent.getIntExtra(Constants.EXTRA_STUDENT_ID, -1)

        if (studentId != -1) {
            // Edit mode
            supportActionBar?.title = "Sửa sinh viên"
            loadStudentData()
        } else {
            // Add mode
            supportActionBar?.title = "Thêm sinh viên"
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun loadStudentData() {
        lifecycleScope.launch {
            currentStudent = studentViewModel.getStudentById(studentId)
            currentStudent?.let { student ->
                binding.apply {
                    etStudentCode.setText(student.studentCode)
                    etFullName.setText(student.fullName)
                    etEmail.setText(student.email)
                    etPhone.setText(student.phone)
                    etMajor.setText(student.major)
                    etGpa.setText(student.gpa.toString())
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            saveStudent()
        }

        studentViewModel.message.observe(this) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                if (message.contains("thành công")) {
                    finish()
                }
            }
        }
    }

    private fun saveStudent() {
        val studentCode = binding.etStudentCode.text.toString().trim()
        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val major = binding.etMajor.text.toString().trim()
        val gpaText = binding.etGpa.text.toString().trim()

        // Validation
        if (studentCode.isEmpty()) {
            binding.tilStudentCode.error = "Mã sinh viên không được để trống"
            return
        }

        if (fullName.isEmpty()) {
            binding.tilFullName.error = "Họ tên không được để trống"
            return
        }

        if (email.isEmpty()) {
            binding.tilEmail.error = "Email không được để trống"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Email không hợp lệ"
            return
        }

        if (phone.isEmpty()) {
            binding.tilPhone.error = "Số điện thoại không được để trống"
            return
        }

        if (major.isEmpty()) {
            binding.tilMajor.error = "Ngành học không được để trống"
            return
        }

        val gpa = try {
            gpaText.toDouble()
        } catch (e: NumberFormatException) {
            binding.tilGpa.error = "GPA phải là số"
            return
        }

        if (gpa < 0.0 || gpa > 4.0) {
            binding.tilGpa.error = "GPA phải từ 0.0 đến 4.0"
            return
        }

        // Clear errors
        binding.tilStudentCode.error = null
        binding.tilFullName.error = null
        binding.tilEmail.error = null
        binding.tilPhone.error = null
        binding.tilMajor.error = null
        binding.tilGpa.error = null

        val student = if (currentStudent != null) {
            // Update existing student
            currentStudent!!.copy(
                studentCode = studentCode,
                fullName = fullName,
                email = email,
                phone = phone,
                major = major,
                gpa = gpa
            )
        } else {
            // Create new student
            Student(
                studentCode = studentCode,
                fullName = fullName,
                email = email,
                phone = phone,
                major = major,
                gpa = gpa
            )
        }

        if (currentStudent != null) {
            studentViewModel.updateStudent(student)
        } else {
            studentViewModel.insertStudent(student)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}