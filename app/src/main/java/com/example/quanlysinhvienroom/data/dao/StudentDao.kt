package com.example.quanlysinhvienroom.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.quanlysinhvienroom.data.entity.Student

@Dao
interface StudentDao {

    @Query("SELECT * FROM students ORDER BY full_name ASC")
    fun getAllStudents(): LiveData<List<Student>>

    @Query("SELECT * FROM students WHERE id = :id")
    suspend fun getStudentById(id: Int): Student?

    @Query("SELECT * FROM students WHERE student_code = :studentCode")
    suspend fun getStudentByCode(studentCode: String): Student?

    @Query("SELECT * FROM students WHERE full_name LIKE '%' || :name || '%'")
    fun searchStudentsByName(name: String): LiveData<List<Student>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student): Long

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("DELETE FROM students WHERE id = :id")
    suspend fun deleteStudentById(id: Int)

    @Query("SELECT COUNT(*) FROM students")
    suspend fun getStudentCount(): Int
}