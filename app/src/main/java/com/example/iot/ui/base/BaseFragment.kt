package com.example.iot.ui.base

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VBinding : ViewBinding>(@LayoutRes val layout: Int) : Fragment() {

    protected lateinit var binding: VBinding
    protected abstract fun getViewBinding(): VBinding
    lateinit var myContext: Context
    lateinit var myActivity: Activity

//    lateinit var networkConnectivity: NetworkConnectivity

//    fun isNetworkConnecting(): Boolean {
//        return networkConnectivity.isConnected()
//    }

    fun isDataConnected(): Boolean {
        return try {
            val cm = myActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            cm.activeNetworkInfo!!.isConnectedOrConnecting
        } catch (e: java.lang.Exception) {
            false
        }
    }
    protected abstract fun initView()
    abstract fun observeViewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myContext = context
        myActivity = context as Activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initView()
        onEventClick()
        observeViewModel()
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    open fun refresh() {

    }

    open fun navigate(action: Int) {
        view?.let { _view ->
            Navigation.findNavController(_view).navigate(action)
        }
    }

    open fun navigate(action: Int, bundle: Bundle) {
        view?.let { _view ->
            Navigation.findNavController(_view).navigate(action, bundle)
        }
    }

    open fun navigate(direction: NavDirections) {
        view?.let { _view ->
            Navigation.findNavController(_view).navigate(direction)
        }
    }

    open fun popBackStack() {
        findNavController().navigateUp()
    }

    open fun onEventClick() {}

    fun Fragment.findNavControllerSafely(id: Int): NavController? {
        return if (isAdded
            && lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
            && findNavController().currentDestination?.id == id
        ) {
            findNavController()
        } else {
            null
        }
    }
}