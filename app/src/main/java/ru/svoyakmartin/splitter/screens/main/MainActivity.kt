package ru.svoyakmartin.splitter.screens.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import ru.svoyakmartin.splitter.*
import ru.svoyakmartin.splitter.databinding.ActivityMainBinding
import ru.svoyakmartin.splitter.screens.add.WedgeEditActivity
import ru.svoyakmartin.splitter.screens.main.list.WedgeListFragment
import ru.svoyakmartin.splitter.screens.main.statistic.StatisticFragment
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var backPressed: Long = 0
    private val listFragment = WedgeListFragment.getInstance()
    private val statisticFragment = StatisticFragment.getInstance()
    private var currentFragmentTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        setContent(listFragment)

        binding.apply {
            floatingActionButtonAdd.setOnClickListener {
                startActivity(Intent(this@MainActivity, WedgeEditActivity::class.java))
            }

            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.bottom_menu_item_wedge_list -> {
                        floatingActionButtonAdd.visibility = View.VISIBLE
                        setContent(listFragment)
                    }
                    R.id.bottom_menu_item_wedge_statistic -> {
                        floatingActionButtonAdd.visibility = View.GONE
                        setContent(statisticFragment)
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
                        "Нажмите для выхода ещё раз",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                backPressed = System.currentTimeMillis()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun setContent(fragment: Fragment) {
        val destFragmentTag = fragment::class.java.name

        if (currentFragmentTag == destFragmentTag) return

        val transaction = supportFragmentManager.beginTransaction()
        val currentFragment = supportFragmentManager.findFragmentByTag(currentFragmentTag)
        var destFragment = supportFragmentManager.findFragmentByTag(destFragmentTag)

        if (destFragment == null){
            transaction.add(R.id.place_holder, fragment, destFragmentTag)
            destFragment = fragment
        }

        transaction.apply {
                if (currentFragment != null){
                    hide(currentFragment)
                }
                show(destFragment)
                commit()
            }

        currentFragmentTag = destFragmentTag
    }
}