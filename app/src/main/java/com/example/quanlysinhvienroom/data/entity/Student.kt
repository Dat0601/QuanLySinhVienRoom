package com.example.quanlysinhvienroom.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "student_code")
    val studentCode: String,

    @ColumnInfo(name = "full_name")
    val fullName: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "major")
    val major: String,

    @ColumnInfo(name = "gpa")
    val gpa: Double,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)