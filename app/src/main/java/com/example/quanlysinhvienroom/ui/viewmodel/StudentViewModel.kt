package com.example.quanlysinhvienroom.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quanlysinhvienroom.data.database.DatabaseRepository
import com.example.quanlysinhvienroom.data.database.StudentDatabase
import com.example.quanlysinhvienroom.data.entity.Student
import kotlinx.coroutines.launch

class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DatabaseRepository
    val allStudents: LiveData<List<Student>>

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    init {
        val studentDao = StudentDatabase.getDatabase(application).studentDao()
        repository = DatabaseRepository(studentDao)
        allStudents = repository.getAllStudents()
    }

    fun insertStudent(student: Student) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val existingStudent = repository.getStudentByCode(student.studentCode)
                if (existingStudent != null) {
                    _message.value = "Mã sinh viên đã tồn tại!"
                } else {
                    repository.insertStudent(student)
                    _message.value = "Thêm sinh viên thành công!"
                }
            } catch (e: Exception) {
                _message.value = "Có lỗi xảy ra: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.updateStudent(student)
                _message.value = "Cập nhật sinh viên thành công!"
            } catch (e: Exception) {
                _message.value = "Có lỗi xảy ra: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.deleteStudent(student)
                _message.value = "Xóa sinh viên thành công!"
            } catch (e: Exception) {
                _message.value = "Có lỗi xảy ra: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchStudents(name: String): LiveData<List<Student>> {
        return repository.searchStudentsByName(name)
    }

    suspend fun getStudentById(id: Int): Student? {
        return repository.getStudentById(id)
    }
}