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
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    companion object {
        private const val MAX_COUNT = 6
    }
    var inputPin = StringBuilder()
    private var inPinConde = arrayOfNulls<ImageView>(6)
    private var arrayList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inPinConde[0] = ivMemberPin01
        inPinConde[1] = ivMemberPin02
        inPinConde[2] = ivMemberPin03
        inPinConde[3] = ivMemberPin04
        inPinConde[4] = ivMemberPin05
        inPinConde[5] = ivMemberPin06

        inputPin.setLength(0)

        setPinCode()

        shuffle()

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = KeypadItemAdapter(arrayList)

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

    inner class KeypadItemAdapter : RecyclerView.Adapter<KeypadItemAdapter.ViewHolder> {
        private var itemList = ArrayList<String>()

        constructor(itemList: ArrayList<String>) {
            this.itemList = itemList
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_keypad, parent,false))
        }

        override fun getItemCount(): Int = itemList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            when (position) {
                9 -> holder.btnKeypad.text = "재배열"
                11 -> holder.btnKeypad.text = "삭제"
                else -> holder.btnKeypad.text = itemList[position]
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val btnKeypad: Button = itemView.findViewById(R.id.btnKeypad)
            init {
                btnKeypad.setOnClickListener {
                    onItemClick(btnKeypad, adapterPosition)
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