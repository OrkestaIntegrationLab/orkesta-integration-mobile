package com.norkut.orkesta_mobile.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.net.UnknownHostException
import java.util.Random

class UI {

    companion object{

        fun showToast(context: Context, message: String?, duration: Int = Toast.LENGTH_SHORT) {
            message?.let {
                if (it.trim().isNotEmpty()) Toast.makeText(context, message, duration).show()
            }
        }

        fun showToast(context: Context, resId: Int?, duration: Int = Toast.LENGTH_SHORT) {
            resId?.let { Toast.makeText(context, context.getString(it), duration).show() }
        }

        fun showSnackbar(
            view: View,
            message: String?,
            callback: (() -> Unit)?,
            duration: Int = BaseTransientBottomBar.LENGTH_LONG
        ) {
            if (message != null && message.trim().isNotEmpty()) {
                Snackbar.make(view, message, duration).apply {
                    callback?.let {
                        this.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                super.onDismissed(transientBottomBar, event)
                                it()
                            }
                        })
                    }
                    show()
                }
            }
        }


        fun showSnackbar(
            view: View,
            message: String,
            callback: (message: String) -> Unit,
            duration: Int = BaseTransientBottomBar.LENGTH_LONG
        ): Snackbar {
            return Snackbar.make(view, message, duration).apply {

                this.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        callback(message)
                    }
                })
                show()
            }
        }


        fun showSnackbarColor(
            view: View,
            message: String,
            color : Int,
            duration: Int = BaseTransientBottomBar.LENGTH_LONG
        ): Snackbar {
            return Snackbar.make(view, message, duration).apply {
                setBackgroundTint(color)
                this.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        //callback(message)
                    }
                })
                show()
            }
        }


        fun showDialog(
            activity: Activity?,
            title: String,
            message: String?,
            negativeButton: String? = null,
            positiveButton: String? = null,
        ) {
            if (message != null && message.trim().isNotEmpty()) {
                activity?.let {
                    val builder = MaterialAlertDialogBuilder(it)
                    builder.setTitle(title)
                    builder.setMessage(message)
                    builder.apply {
                        negativeButton?.let { name ->
                            setNegativeButton(name) { dialog, _ -> dialog.cancel() }
                        }
                        positiveButton?.let { name ->
                            setPositiveButton(name) { dialog, _ -> dialog.dismiss() }
                        }
                        show()
                    }
                } ?: throw IllegalStateException("BuilderAlertDialog: Activity no puede ser null")
            }
        }

        fun showBasicDialog(
            activity: Activity?,
            reason: Pair<String, String?>,
            buttonMessage: String
        ) {
            if (reason.second?.isNotEmpty() == true) {
                activity?.let {
                    val builder = MaterialAlertDialogBuilder(it)
                    builder.setTitle(reason.first)
                    builder.setMessage(reason.second)
                    builder.apply {
                        setPositiveButton(buttonMessage) { dialog, _ -> dialog.dismiss() }
                        show()
                    }
                } ?: throw IllegalStateException("BuilderAlertDialog: Activity no puede ser null")
            }
        }

        fun dialog(
            activity: Activity?,
            title: String,
            message: String,
            negativeButton: String? = null,
            positiveButton: String? = null,
        ): MaterialAlertDialogBuilder {
            activity?.let {
                val builder = MaterialAlertDialogBuilder(it)
                builder.setTitle(title)
                builder.setMessage(message)
                builder.apply {
                    negativeButton?.let { name ->
                        setNegativeButton(name) { dialog, _ -> dialog.cancel() }
                    }
                    positiveButton?.let { name ->
                        setPositiveButton(name) { dialog, _ -> dialog.dismiss() }
                    }
                }
                return builder
            } ?: throw IllegalStateException("BuilderAlertDialog: Activity no puede ser null")
        }

        fun dialogNoCancel(
            activity: Activity?,
            title: String,
            message: String,
            positiveButton: String? = null,
        ): MaterialAlertDialogBuilder {
            activity?.let {
                val builder = MaterialAlertDialogBuilder(it)
                builder.setTitle(title)
                builder.setMessage(message)
                builder.apply {
                    positiveButton?.let { name ->
                        setPositiveButton(name) { dialog, _ ->
                            //dialog.dismiss()
                        }
                    }
                }
                return builder
            } ?: throw IllegalStateException("BuilderAlertDialog: Activity no puede ser null")
        }


        fun Fragment.hideKeyboardFragment() {
            view?.let { activity?.hideKeyboard(it) }
        }

        fun Activity.hideKeyboard(view: View) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }




        fun setText(text: String): Editable {
            return Editable.Factory.getInstance().newEditable(text)
        }

        fun gallery(multiChoice: Boolean = true): Intent {
            return if (Build.VERSION.SDK_INT < 19) {
                Intent().apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiChoice)
                    action = Intent.ACTION_GET_CONTENT
                }
            } else {
                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiChoice)
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "image/*"
                }
            }
        }


        fun isConnected(context: Context): Int {
            var result = 0 // Returns connection type. 0: none; 1: mobile data; 2: wifi; 3: vpn ; 4: ethernet
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                                result = 2
                            }
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                                if (isConnectedToThisServer("192.168.15.140")) {
                                    println("PING SUSCEFUUUULL")
                                } else {
                                    println("PING FAILED")
                                }
                                result = 1
                            }
                        }
                    }
                }
            }
            return result
        }


        private fun isConnectedToThisServer(host: String): Boolean {
            val runtime = Runtime.getRuntime()
            try {
                val ipProcess = runtime.exec("/system/bin/ping -c 1 $host")
                val exitValue = ipProcess.waitFor()
                ipProcess.destroy()
                return exitValue == 0
            } catch (e: UnknownHostException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return false
        }
        fun startsWithPrefix(input: String, prefix: String): Boolean {
            return input.startsWith(prefix)
        }



        fun ImageView.setTextImageDrawable(nombre: String, useRandom: Boolean = false, fontSize: Float = 70f) {
            val iniciales = getInitials(nombre)

            val ancho = 100
            val alto = 100
            val bitmap = Bitmap.createBitmap(ancho, alto, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            val fondo = Paint().apply {
                color = if (useRandom) {
                    generarColorAleatorio()
                } else {
                    Color.parseColor("#808080") // Gris estilo Gmail
                }
                isAntiAlias = true
                style = Paint.Style.FILL
            }

            val texto = Paint().apply {
                color = Color.WHITE
                textSize = fontSize
                textAlign = Paint.Align.CENTER
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }

            // Fondo circular
            val radio = (ancho / 2).toFloat()
            canvas.drawCircle(radio, radio, radio, fondo)

            // Texto centrado
            val xPos = (canvas.width / 2).toFloat()
            val yPos = (canvas.height / 2 - (texto.descent() + texto.ascent()) / 2)
            canvas.drawText(iniciales, xPos, yPos, texto)

            this.setImageDrawable(BitmapDrawable(this.context.resources, bitmap))
        }

        fun getInitials(nombre: String): String {
            val partes = nombre.trim().split(" ")
            val primera = partes.getOrNull(0)?.firstOrNull()?.uppercaseChar() ?: ""
            val segunda = partes.getOrNull(1)?.firstOrNull()?.uppercaseChar() ?: ""
            return "$primera$segunda"
        }

        fun generarColorAleatorio(): Int {
            val random = Random()
            val r = random.nextInt(256)
            val g = random.nextInt(256)
            val b = random.nextInt(256)
            return Color.rgb(r, g, b)
        }





    }
}