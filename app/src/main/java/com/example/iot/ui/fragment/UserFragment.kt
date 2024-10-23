package com.example.iot.ui.fragment

import android.text.Html
import android.text.method.LinkMovementMethod
import com.example.iot.R
import com.example.iot.databinding.FragmentUserBinding
import com.example.iot.ui.base.BaseFragment

class UserFragment : BaseFragment<FragmentUserBinding>(R.layout.fragment_user) {

    override fun getViewBinding(): FragmentUserBinding {
        return FragmentUserBinding.inflate(layoutInflater)
    }

    override fun initView() {
        val htmlGithubApp = "Click <a href='https://github.com/GiangPT-Apero/IOT'>Github App</a>"
        val htmlGithubServer = "Click <a href='https://github.com/GiangPT-Apero/IOT'>Github Server</a>"

        binding.txtGitApp.text = Html.fromHtml(htmlGithubApp, Html.FROM_HTML_MODE_LEGACY)
        binding.txtGitServer.text = Html.fromHtml(htmlGithubServer, Html.FROM_HTML_MODE_LEGACY)


        binding.txtGitApp.movementMethod = LinkMovementMethod.getInstance()
        binding.txtGitServer.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun observeViewModel() {
    }

}