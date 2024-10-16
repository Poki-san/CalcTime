package com.example.timecalculator

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var timeOne: EditText
    private lateinit var timeTwo:EditText
    private lateinit var plusBtn:Button
    private lateinit var minusBtn: Button
    private lateinit var resultTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbarMain)
        setSupportActionBar(toolbar)
        title = "Калькулятор времени"
        toolbar.subtitle = "Версия 1.1"
        toolbar.setLogo(R.drawable.baseline_alarm_24)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        timeOne=findViewById(R.id.timeOne)
        timeTwo=findViewById(R.id.timeTwo)
        plusBtn=findViewById(R.id.plusBtn)
        minusBtn=findViewById(R.id.minusBtn)
        resultTxt=findViewById(R.id.resultTxt)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.resetMenuBtn->{
                timeOne.text.clear()
                timeTwo.text.clear()
                resultTxt.setTextColor(Color.parseColor("#000000"))
                resultTxt.text = "Результат"
                Toast.makeText(
                    applicationContext,
                    "Данные очищены",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.exitMenuBtn -> {
                Toast.makeText(
                    applicationContext,
                    "Приложение закрыто",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    fun onPlus(view: View) {
        val timeRegex = Regex("^(?!\\s*\$)(?:[0-9]+[hms])+\$")
        val one = timeOne.text.toString()
        val two = timeTwo.text.toString()
        if (!timeRegex.matches(one) || !timeRegex.matches(two)){
            resultTxt.text = "Ошибка ввода!"
            resultTxt.setTextColor(Color.parseColor("red"))
            return
        }
        resultTxt.text = action(one, two, '+')
    }
    @SuppressLint("SetTextI18n")
    fun onMinus(view: View) {
        val timeRegex = Regex("^(?!\\s*\$)(?:[0-9]+[hms])+\$")
        val one = timeOne.text.toString()
        val two = timeTwo.text.toString()
        if (!timeRegex.matches(one) || !timeRegex.matches(two)){
            resultTxt.text = "Ошибка ввода!"
            resultTxt.setTextColor(Color.parseColor("red"))
            return
        }
        resultTxt.text = action(one, two, '-')
    }

    private fun convertStoHMS(sec:Int):String{
        if (sec < 0) return "Ошибка! отрицательное значение"

        val h = sec / 3600
        val m = (sec % 3600)/60
        val s = sec % 60

        var result =""
        if (h>0) result += "${h}h"
        if (m>0) result += "${m}m"
        if (s>0) result += "${s}s"
        Toast.makeText(
            applicationContext,
            "Результат: $result",
            Toast.LENGTH_LONG
        ).show()
        resultTxt.setTextColor(Color.parseColor("#8B0000"))
        return result
    }
    private fun timeToMap(time:String):Int {
        val timeRegex = Regex("[0-9]+[hms]")
        val hmsRegex = Regex("[hms]")

        val res = timeRegex.findAll(time).map {
            val value = it.value
            Pair(value[value.lastIndex], hmsRegex.replace(value,"").toInt())
        }.toMap()

        val allSec = res.map {
            when(it.key){
                'h' -> it.value * 3600
                'm' -> it.value * 60
                's' -> it.value
                else -> 0
            }
        }.sum()

        return allSec
    }
    private fun action(one:String, two:String, compute:Char):String{
        val oneNumber = timeToMap(one)
        val twoNumber = timeToMap(two)
        val result = when(compute){
            '+'->oneNumber+twoNumber
            '-'->oneNumber-twoNumber
            else -> 0
        }
        return convertStoHMS(result)
    }
}