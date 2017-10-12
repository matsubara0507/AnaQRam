package org.iggg.anaqram

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

import java.util.ArrayList
import java.util.Collections
import java.util.HashMap

internal class CharBoxMapper(context: Context, cbs: Array<CharBox?>, listener: View.OnClickListener) {
    private var clickedButton: Button? = null

    val buttons: Array<Button?>
    private val charBoxes: HashMap<Button, CharBox>

    private val currentCharBoxes: Array<CharBox?>
        get() {
            val current = arrayOfNulls<CharBox>(buttons.size)
            for (i in buttons.indices)
                current[i] = charBoxes[buttons[i]]
            return current
        }

    val currentString: String
        get() {
            var str = ""
            for (button in buttons)
                str += button?.text
            return str
        }

    init {
        buttons = arrayOfNulls(cbs.size)
        for (i in cbs.indices) {
            val button = Button(context)
            buttons[i] = button
        }

        charBoxes = HashMap(buttons.size)
        for (i in buttons.indices)
            buttons[i]?.let { charBoxes.put(it, cbs[i]!!) }  // charBoxes.put(buttons.get(i), cbs[i])

        for (button in buttons) {
            val buttonLayoutParams = LinearLayout.LayoutParams(150, 150)
            button?.layoutParams = buttonLayoutParams
            button?.textSize = 30f
            button?.setOnClickListener(listener)
        }
    }

    fun swapCharBox(button: Button) {
        if (clickedButton == null) {
            button.setTextColor(Color.RED)
            clickedButton = button
        } else {
            val temp = charBoxes[button]
            charBoxes.put(button, charBoxes[clickedButton!!]!!)
            charBoxes.put(clickedButton!!, temp!!)
            clickedButton!!.setTextColor(Color.BLACK)
            updateChar()
            clickedButton = null
        }
    }

    fun updateChar() {
        for (button in buttons)
            button?.text = charBoxes[button].toString()
    }

    fun shuffle() {
        val indexes = ArrayList<Int>(buttons.size)
        for (i in buttons.indices)
            indexes.add(i)
        Collections.shuffle(indexes)

        val current = currentCharBoxes
        for (i in buttons.indices)
            charBoxes.put(buttons[i]!!, current[indexes[i]]!!)
    }
}
