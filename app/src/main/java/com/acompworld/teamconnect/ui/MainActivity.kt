package com.acompworld.teamconnect.ui

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.acompworld.teamconnect.R
import com.acompworld.teamconnect.databinding.ActivityMainBinding
import com.acompworld.teamconnect.databinding.ContentMainBinding
import com.acompworld.teamconnect.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    var  footer : CardView?=null
     var toolbar: Toolbar? = null
    var menuSearchItem: MenuItem? = null
    private val viewModel: MainViewModel by viewModels()
    private var shouldVisble: Boolean = false
    var notificationBadge: View? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //accelerating hardware
        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )

        setSupportActionBar(binding.appBarMain.toolbar)
        binding.appBarMain.toolbar.setNavigationIcon(R.drawable.ic_menu)
      toolbar =  binding.appBarMain.toolbar


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
      binding.appBarMain.contentMain.navHome.setOnClickListener {
        navController.popBackStack(R.id.nav_home,false)
      }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//
//        val item = menu.findItem(R.id.action_notification)
//        val actionView = item.actionView
//        notificationBadge = actionView.findViewById(R.id.view_badge)
//        notificationBadge?.isVisible = shouldVisble
//        actionView.setOnClickListener {
//            onOptionsItemSelected(item)
//            blinkNotification()
//        }
//
//        //search view
//
//        val menuItem = menu.findItem(R.id.search_menu)
//        this.menuSearchItem = menuItem
//        val searchView = menuItem?.actionView as SearchView?
//
//        searchView?.queryHint = "Search Here"
//
//        searchView?.isSubmitButtonEnabled = true
//
//        if (viewModel.pendingQuery.value?.isNotBlank() == true) {
//            menuItem.expandActionView()
//            searchView?.setQuery(viewModel.pendingQuery.value!!, false)
//        }
//
//        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                viewModel.pendingQuery.postValue(query.toString() + "")
//                viewModel.searchingFun(query.toString())
//                return true
//            }
//
//            var job: Job? = null
//            override fun onQueryTextChange(newText: String?): Boolean {
//                job?.cancel()
//                job = MainScope().launch {
//                    delay(Constants.SEARCH_TIME_DELAY)
//                    viewModel.pendingQuery.postValue(newText.toString() + "")
//                    viewModel.searchingFun(newText ?: "")
//                }
//                return true
//            }
//
//        })
//        return super.onCreateOptionsMenu(menu)
//    }

    fun blinkNotification() {
        Log.d("omega", "fun called")
        notificationBadge?.let {
            shouldVisble = !shouldVisble
            Log.d("omega", "success")
        }
        if (notificationBadge == null) Log.d("omega", "null badge")
        invalidateOptionsMenu()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }


}