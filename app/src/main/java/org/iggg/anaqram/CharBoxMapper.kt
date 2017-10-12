package org.iggg.anaqram

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

import java.util.ArrayList
import java.util.Collections
import java.util.HashMap

internal class CharBoxMapper(context: Context, charBoxes: List<CharBox>, listener: View.OnClickListener) {
    private val unUseButton: Button = Button(context)
    private var clickedButton: Button = unUseButton

    val buttons: List<Button> = charBoxes.map { Button(context) }
    private val charBoxMap: HashMap<Button, CharBox> = HashMap(charBoxes.size)

    val currentString: String
        get() = buttons.fold("", { acc, button -> acc + button.text })

    init {
        buttons.zip(charBoxes).forEach {
            (button, index) -> charBoxMap.put(button, index)
        }
        for (button in buttons) {
            val buttonLayoutParams = LinearLayout.LayoutParams(150, 150)
            button.layoutParams = buttonLayoutParams
            button.textSize = 30f
            button.setOnClickListener(listener)
        }
    }

    fun swapCharBox(button: Button) {
        charBoxMap[clickedButton]?.let {
            val temp = it
            charBoxMap[button]?.let {
                charBoxMap.put(button, temp)
                charBoxMap.put(clickedButton, it)
                clickedButton.setTextColor(Color.BLACK)
                clickedButton = unUseButton
                updateChar()
            }
            return
        }
        button.setTextColor(Color.RED)
        clickedButton = button
    }

    fun updateChar() =
        buttons.forEach { button -> button.text = charBoxMap[button].toString() }

    fun shuffle() {
        val currentCharBoxes = charBoxMap.values.toTypedArray()
        val indexes = buttons.mapIndexed { index, _ -> index  }
        Collections.shuffle(indexes)
        buttons.zip(indexes).forEach { (button, index) ->
            charBoxMap.put(button, currentCharBoxes[index])
        }
    }
}
