package ru.svoyakmartin.splitter.screens.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import ru.svoyakmartin.splitter.*
import ru.svoyakmartin.splitter.databinding.ActivityMainBinding
import ru.svoyakmartin.splitter.model.Wedge
import ru.svoyakmartin.splitter.screens.main.list.WedgeListFragmentDirections
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    var visibilityFAB = MutableLiveData<Boolean>()
    private var backPressed: Long = 0
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main)
            .apply {
                activity = this@MainActivity
                lifecycleOwner = this@MainActivity
            }

        init()
    }

    override fun onResume() {
        super.onResume()
        setVisibilityFAB()
    }

    private fun init() {
//        Util.startWorker(applicationContext)
        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        onBackPressedDispatcher.addCallback(this, callback)
    }

    fun goToNewWedge() {
        navController.navigate(
            WedgeListFragmentDirections.actionWedgeListFragmentToWedgeEditActivity(
                Wedge()
            )
        )
    }

    fun setFragment(menuItem: MenuItem): Boolean {
        with(menuItem.itemId) {
            if (binding.bottomNavigationView.selectedItemId != this) {
                setVisibilityFAB(this)

                when (this) {
                    R.id.bottom_menu_item_wedge_list -> {
                        navController.navigate(R.id.list_fragment)
                    }
                    R.id.bottom_menu_item_wedge_statistic -> {
                        navController.navigate(R.id.statistic_fragment)
                    }
                }
            }
        }

        return true
    }

    private fun setVisibilityFAB(itemId: Int = binding.bottomNavigationView.selectedItemId) {
        visibilityFAB.value = (itemId == R.id.bottom_menu_item_wedge_list)
    }

    fun backPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            finishAffinity()
            exitProcess(0)
        } else {
            Toast.makeText(
                this,
                getString(R.string.back_pressed_toast_text),
                Toast.LENGTH_SHORT
            ).show()

            backPressed = System.currentTimeMillis()
        }
    }
}