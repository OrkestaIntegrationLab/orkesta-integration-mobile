package com.norkut.orkesta_mobile.view.main

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.norkut.orkesta_mobile.R
import com.norkut.orkesta_mobile.databinding.FragmentAboutBinding

class AboutDialog( context: Context,  layoutInflater: LayoutInflater)
       : MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialog_rounded)  {


           private var modalBinding = FragmentAboutBinding.inflate(layoutInflater)


    override fun create(): AlertDialog {
        setView(modalBinding.root)
        return super.create()
    }

   /* fun setConfigInfo(configInfo: ConfigurationEntity?) = this.also {
        configInfo?.let {
            modalBinding.tvCompany.text = it.companyName
            modalBinding.tvBranchOffice.text = it.branchOfficeName
            modalBinding.tvIp.text = it.localIp
        }
    }*/

    fun setVersion() {
        val info = context.packageManager.getPackageInfo(context.packageName, 0)
        modalBinding.tvVersion.text = info.versionName
    }

}