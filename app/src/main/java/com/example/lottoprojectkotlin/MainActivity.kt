package com.example.lottoprojectkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.contains
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val numberPicker: NumberPicker by lazy {
        findViewById(R.id.NPicker)
    }
    private val addButton: Button by lazy {
        findViewById(R.id.addButton)
    }
    private val clearButton: Button by lazy {
        findViewById(R.id.clearButton)
    }
    private val runButton: Button by lazy {
        findViewById(R.id.runButton)
    }

    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.firstN),
            findViewById(R.id.secondN),
            findViewById(R.id.thirdN),
            findViewById(R.id.fourthN),
            findViewById(R.id.fifthN),
            findViewById(R.id.sixthN)
        )
    }

    private var didRun = false //자동 생성 및 6개 이상 선택시 추가 불가능

    private val pickNumberSet = HashSet<Int>() //숫자 중복 방지

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNumberPicker()
        initAddButton()
        initClearButton()
        initRunButton()
    }

    //NumberPicker 최대, 최소값 지정 1~45
    private fun initNumberPicker() {
        numberPicker.minValue = 1
        numberPicker.maxValue = 45
    }

    private fun initAddButton() {
        addButton.setOnClickListener {
            if (didRun) {
                Toast.makeText(this, "초기화 후 사용해주세요..", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.contains(numberPicker.value)) {
                Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pickNumberSet.size >= 6) {
                Toast.makeText(this, "최대 선택 횟수 입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = numberTextViewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = numberPicker.value.toString()

            getNumberBackground(numberPicker.value, textView)

            pickNumberSet.add(numberPicker.value)
        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNumberSet.clear()
            numberTextViewList.forEach {
                it.isVisible = false
            }
            didRun = false
        }
    }

    private fun initRunButton() {
        runButton.setOnClickListener {
            val list = getRandomNumber()
            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                getNumberBackground(number, textView)
            }
            didRun = true

        }
    }

    private fun getRandomNumber(): List<Int> {
        val numberList = mutableListOf<Int>()
            .apply {
                for (i in 1..45) {
                    if (pickNumberSet.contains(i)) {
                        continue
                    }
                    this.add(i)
                }
            }

        numberList.shuffle()
        val newList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)

        return newList.sorted()
    }


    private fun getNumberBackground(number: Int, textView: TextView) {
        when (number) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 11..20 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 21..30 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 31..40 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_green)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
        }
    }
}