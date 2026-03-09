package com.example.studentrecordapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StudentListActivity : AppCompatActivity() {

    private lateinit var adapter: StudentAdapter
    private lateinit var etSearch: EditText
    private lateinit var chipGroupLevels: ChipGroup
    private lateinit var chipGroupClasses: ChipGroup
    
    private var currentLevelFilter: String = "All Levels"
    private var currentClassFilter: String = "All Classes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        etSearch = findViewById(R.id.etSearch)
        chipGroupLevels = findViewById(R.id.chipGroupLevels)
        chipGroupClasses = findViewById(R.id.chipGroupClasses)
        val rvStudents = findViewById<RecyclerView>(R.id.rvStudents)
        rvStudents.layoutManager = LinearLayoutManager(this)
        
        // Ensure sample students are added if the list is empty
        StudentManager.addSampleStudents()
        
        adapter = StudentAdapter(StudentManager.students) { student ->
            val intent = Intent(this, StudentDetailsActivity::class.java)
            intent.putExtra("STUDENT", student)
            startActivity(intent)
        }
        rvStudents.adapter = adapter

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                applyFilters()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        chipGroupLevels.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            currentLevelFilter = chip?.text?.toString() ?: "All Levels"
            applyFilters()
        }

        chipGroupClasses.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            currentClassFilter = chip?.text?.toString() ?: "All Classes"
            applyFilters()
        }

        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            val intent = Intent(this, AddEditStudentActivity::class.java)
            startActivity(intent)
        }
        
        updateClassChips()
    }

    private fun updateClassChips() {
        // Remove all chips except the "All Classes" chip
        val childCount = chipGroupClasses.childCount
        for (i in childCount - 1 downTo 1) {
            chipGroupClasses.removeViewAt(i)
        }

        val uniqueClasses = StudentManager.students.map { it.studentClass }.distinct().filter { it.isNotEmpty() }.sorted()
        
        for (className in uniqueClasses) {
            val chip = layoutInflater.inflate(R.layout.layout_chip_filter, chipGroupClasses, false) as Chip
            chip.text = className
            chip.id = View.generateViewId()
            chipGroupClasses.addView(chip)
        }
    }

    private fun applyFilters() {
        val query = etSearch.text.toString()
        val filteredList = StudentManager.students.filter { student ->
            val matchesQuery = query.isEmpty() || 
                    student.name.contains(query, ignoreCase = true) ||
                    student.studentClass.contains(query, ignoreCase = true)
            
            val matchesLevel = currentLevelFilter == "All Levels" || student.level == currentLevelFilter
            val matchesClass = currentClassFilter == "All Classes" || student.studentClass == currentClassFilter
            
            matchesQuery && matchesLevel && matchesClass
        }
        adapter.updateData(filteredList)
    }

    override fun onResume() {
        super.onResume()
        updateClassChips()
        applyFilters()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_reports -> {
                startActivity(Intent(this, ReportsActivity::class.java))
                true
            }
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}