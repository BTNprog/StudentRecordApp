package com.example.studentrecordapp

import java.util.UUID

object StudentManager {
    val students = mutableListOf<Student>()

    fun addSampleStudents() {
        val namesToAdd = listOf("Alice Johnson", "Bob Smith", "Charlie Brown", "Diana Prince", "Ethan Hunt")
        var addedAny = false
        
        for (name in namesToAdd) {
            // Only add if a student with this name doesn't already exist to avoid duplicates
            if (students.none { it.name == name }) {
                val student = Student(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    studentClass = "Ba1a",
                    email = "${name.lowercase().replace(" ", ".")}@example.com",
                    gender = if (name == "Alice Johnson" || name == "Diana Prince") "Female" else "Male",
                    dateOfBirth = 946684800000L, // Jan 1, 2000
                    phoneNumber = "1234567890",
                    address = "123 Student St",
                    guardianName = "Guardian of $name",
                    guardianContact = "0987654321",
                    level = "Level 1"
                )
                students.add(student)
                addedAny = true
            }
        }
        
        if (addedAny) {
            App.instance.saveData()
        }
    }

    fun addStudent(student: Student) {
        students.add(student)
        App.instance.saveData()
    }

    fun updateStudent(updatedStudent: Student) {
        val index = students.indexOfFirst { it.id == updatedStudent.id }
        if (index != -1) {
            students[index] = updatedStudent
            App.instance.saveData()
        }
    }

    fun deleteStudent(id: String) {
        students.removeIf { it.id == id }
        App.instance.saveData()
    }
}