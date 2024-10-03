package com.example.timecalculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var timeOne: EditText
    private lateinit var timeTwo:EditText
    private lateinit var plusBtn:Button
    private lateinit var minusBtn: Button
    private lateinit var resultTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
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

    @SuppressLint("SetTextI18n")
    fun onPlus(view: View) {
        val one = timeOne.text.toString()
        val two = timeTwo.text.toString()
        resultTxt.text = action(one, two, '+')
    }
    @SuppressLint("SetTextI18n")
    fun onMinus(view: View) {
        val one = timeOne.text.toString()
        val two = timeTwo.text.toString()
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