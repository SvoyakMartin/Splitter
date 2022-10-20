package ru.svoyakmartin.splitter.screens.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import ru.svoyakmartin.splitter.*
import ru.svoyakmartin.splitter.databinding.ActivityMainBinding
import ru.svoyakmartin.splitter.model.Wedge
import ru.svoyakmartin.splitter.screens.main.list.WedgeListFragmentDirections
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var backPressed: Long = 0
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
//        Util.startWorker(applicationContext)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.apply {
            floatingActionButtonAdd.setOnClickListener {
//                startActivity(Intent(this@MainActivity, WedgeEditActivity::class.java))
                val action =
                    WedgeListFragmentDirections.actionWedgeListFragmentToWedgeEditActivity(Wedge())
                findNavController(R.id.nav_host_fragment).navigate(action)
            }

            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.bottom_menu_item_wedge_list -> {
                        floatingActionButtonAdd.visibility = View.VISIBLE
                        findNavController(R.id.nav_host_fragment).navigate(R.id.wedgeListFragment)
                    }
                    R.id.bottom_menu_item_wedge_statistic -> {
                        floatingActionButtonAdd.visibility = View.GONE
                        findNavController(R.id.nav_host_fragment).navigate(R.id.statisticFragment)
                    }
                }
                true
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressed + 2000 > System.currentTimeMillis()) {
                    finishAffinity()
                    exitProcess(0)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.back_pressed_toast_text),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                backPressed = System.currentTimeMillis()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)
    }
}