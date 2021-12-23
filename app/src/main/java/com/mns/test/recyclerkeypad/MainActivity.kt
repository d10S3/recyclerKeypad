package com.mns.test.recyclerkeypad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mns.test.recyclerkeypad.databinding.ActivityMainBinding
import com.mns.test.recyclerkeypad.databinding.ItemKeypadBinding
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    companion object {
        private const val MAX_COUNT = 6
    }
    private lateinit var binding: ActivityMainBinding
    private var inputPin = StringBuilder()
    private var inPinConde = arrayOfNulls<ImageView>(6)
    private var arrayList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inPinConde[0] = binding.ivMemberPin01
        inPinConde[1] = binding.ivMemberPin02
        inPinConde[2] = binding.ivMemberPin03
        inPinConde[3] = binding.ivMemberPin04
        inPinConde[4] = binding.ivMemberPin05
        inPinConde[5] = binding.ivMemberPin06

        inputPin.setLength(0)

        setPinCode()

        shuffle()

        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.adapter = KeypadItemAdapter(arrayList)

    }

    private fun setPinCode() {
        for (i in 0..5) {
            if (i < inputPin.length) {
                inPinConde[i]!!.visibility = View.VISIBLE
            } else {
                inPinConde[i]!!.visibility = View.INVISIBLE
            }
        }
    }

    private fun shuffle() {
        arrayList.clear()

        for (i in 0..9) {
            arrayList.add(i.toString())
        }

        arrayList.shuffle()
        arrayList.add(9, "")
        arrayList.add("")
    }

    inner class KeypadItemAdapter(private val itemList: ArrayList<String>) :
        RecyclerView.Adapter<KeypadItemAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_keypad, parent, false)
            return ViewHolder(ItemKeypadBinding.bind(view))
        }

        override fun getItemCount(): Int = itemList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            when (position) {
                9 -> holder.binding.btnKeypad.text = "재배열"
                11 -> holder.binding.btnKeypad.text = "삭제"
                else -> holder.binding.btnKeypad.text = itemList[position]
            }
        }

        inner class ViewHolder(val binding: ItemKeypadBinding) : RecyclerView.ViewHolder(binding.root) {
            init {
                binding.btnKeypad.setOnClickListener {
                    onItemClick(binding.btnKeypad, adapterPosition)
                }
            }
        }

        private fun onItemClick(btnKeypad: Button, adapterPosition: Int) {
            when (adapterPosition) {
                11 -> {
                    if (inputPin.isNotEmpty()) {
                        inputPin.setLength(inputPin.length - 1)
                        setPinCode()
                    }
                    return
                }
                9 -> {
                    shuffle()
                    notifyDataSetChanged()
                    return
                }
            }

            if (inputPin.length >= MAX_COUNT)
                return

            inputPin.append(btnKeypad.text.toString())
            setPinCode()

            if (inputPin.length == MAX_COUNT) {
                Toast.makeText(baseContext, inputPin, Toast.LENGTH_SHORT).show()
            }
        }
    }
}