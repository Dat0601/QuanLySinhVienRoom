package com.example.quanlysinhvienroom.data.database

import androidx.lifecycle.LiveData
import com.example.quanlysinhvienroom.data.dao.StudentDao
import com.example.quanlysinhvienroom.data.entity.Student

class DatabaseRepository(private val studentDao: StudentDao) {

    fun getAllStudents(): LiveData<List<Student>> = studentDao.getAllStudents()

    suspend fun getStudentById(id: Int): Student? = studentDao.getStudentById(id)

    suspend fun getStudentByCode(studentCode: String): Student? =
        studentDao.getStudentByCode(studentCode)

    fun searchStudentsByName(name: String): LiveData<List<Student>> =
        studentDao.searchStudentsByName(name)

    suspend fun insertStudent(student: Student): Long = studentDao.insertStudent(student)

    suspend fun updateStudent(student: Student) = studentDao.updateStudent(student)

    suspend fun deleteStudent(student: Student) = studentDao.deleteStudent(student)

    suspend fun deleteStudentById(id: Int) = studentDao.deleteStudentById(id)

    suspend fun getStudentCount(): Int = studentDao.getStudentCount()
}