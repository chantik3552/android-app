package net.ivpn.core.v2.customdns

/*
 IVPN Android app
 https://github.com/ivpn/android-app

 Created by Oleksandr Mykhailenko.
 Copyright (c) 2020 Privatus Limited.

 This file is part of the IVPN Android app.

 The IVPN Android app is free software: you can redistribute it and/or
 modify it under the terms of the GNU General Public License as published by the Free
 Software Foundation, either version 3 of the License, or (at your option) any later version.

 The IVPN Android app is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details.

 You should have received a copy of the GNU General Public License
 along with the IVPN Android app. If not, see <https://www.gnu.org/licenses/>.
*/

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import net.ivpn.core.IVPNApplication
import net.ivpn.core.R
import net.ivpn.core.databinding.FragmentCustomDnsBinding
import net.ivpn.core.v2.dialog.DialogBuilder
import net.ivpn.core.v2.MainActivity
import org.slf4j.LoggerFactory
import javax.inject.Inject

class CustomDNSFragment : Fragment() {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(CustomDNSFragment::class.java)
    }

    lateinit var binding: FragmentCustomDnsBinding

    @Inject
    lateinit var viewModel: CustomDNSViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_custom_dns, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        IVPNApplication.appComponent.provideActivityComponent().create().inject(this)
        initViews()
        initToolbar()
    }

    override fun onStart() {
        super.onStart()
        activity?.let {
            if (it is MainActivity) {
                it.setContentSecure(false)
            }
        }
    }

    private fun initViews() {
        binding.contentLayout.viewmodel = viewModel

        binding.contentLayout.changeDnsButton.setOnClickListener {
            changeDNS()
        }
        binding.contentLayout.changeSecondaryDnsButton.setOnClickListener {
            changeSecondaryDNS()
        }
    }

    fun changeDNS() {
        DialogBuilder.createCustomDNSDialogue(
            context,
            CustomDNSViewModel.DNSType.PRIMARY
        ) { dns: String? -> viewModel.setDnsAs(dns) }
    }

    fun changeSecondaryDNS() {
        DialogBuilder.createCustomDNSDialogue(
            context,
            CustomDNSViewModel.DNSType.SECONDARY
        ) { dns: String? -> viewModel.setSecondaryDNSAs(dns) }
    }

    private fun initToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}