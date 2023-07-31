package com.easycalculattor

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import com.easycalculattor.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    var lastNumber = false
    var stateError = false
    var lastPoint = false

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    fun clearClick(view: View) {
        binding.enteredNumbers.text = ""
        lastNumber = false
    }

    fun deleteClick(view: View) {
        binding.enteredNumbers.text = binding.enteredNumbers.text.toString().dropLast(1)
        try{
            val lastChar = binding.enteredNumbers.text.toString().last()
            if(lastChar.isDigit()){
                getResult()
            }

        }
        catch (e:Exception){
            e.stackTrace
            binding.result.text = ""
            binding.result.visibility = View.GONE
        }
    }

    fun equalClick(view: View) {
        getResult()
        binding.enteredNumbers.text = binding.result.text.toString().drop(1)
    }

    fun acClick(view: View) {
        binding.enteredNumbers.text = ""
        binding.result.text = ""
        stateError = false
        lastPoint = false
        lastNumber = false
        binding.result.visibility = View.GONE
    }

    fun numberClick(view: View) {
        if(stateError){
            binding.enteredNumbers.text = (view as Button).text
            stateError = false
        }
        else{
            binding.enteredNumbers.append((view as Button).text)
        }
        lastNumber = true
        getResult()
    }

    fun operatorsClick(view: View) {
        if(!stateError && lastNumber){
             binding.enteredNumbers.append((view as Button).text)
            lastPoint = false
            lastNumber = false
            getResult()
        }
    }

    fun getResult(){
        if(lastNumber && !stateError){
            val txt = binding.enteredNumbers.text.toString()

            expression = ExpressionBuilder(txt).build()

            try {
                val res = expression.evaluate()

                binding.result.visibility = View.VISIBLE
                binding.result.text = "=$res"
            }catch (e:ArithmeticException){
                e.stackTrace
                binding.result.text = "Error"
                stateError = true
                lastNumber = false

            }
        }
    }
}