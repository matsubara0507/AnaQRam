package org.iggg.anaqram

import android.content.Context
import android.graphics.Color
import android.widget.Button
import android.widget.LinearLayout

import java.util.Collections
import java.util.HashMap

internal class CharBoxMapper(context: Context, charBoxes: List<CharBox>, clear: (String) -> Unit) {
    private val unUseButton: Button = Button(context)
    private var clickedButton: Button = unUseButton

    val buttons: List<Button> = charBoxes.map { Button(context) }
    private val charBoxMap: HashMap<Button, CharBox> = HashMap(charBoxes.size)

    val currentString: String
        get() = buttons.fold("", { acc, button -> acc + button.text })

    init {
        buttons
            .apply {
                zip(charBoxes).forEach { (button, index) -> charBoxMap.put(button, index) }
            }
            .forEach { button ->
                button
                    .apply { layoutParams = LinearLayout.LayoutParams(150, 150) }
                    .apply { textSize = 30f }
                    .apply {
                        setOnClickListener {
                            swapCharBox(it as Button)
                            clear(currentString)
                        }
                    }
            }
    }

    private fun swapCharBox(button: Button) =
        charBoxMap[clickedButton]?.let { clickedCharBox ->
            charBoxMap[button]?.let { charBox ->
                charBoxMap.put(button, clickedCharBox)
                charBoxMap.put(clickedButton, charBox)
                setClickedButton(unUseButton)
                updateChar()
            }
        } ?: setClickedButton(button)

    private fun setClickedButton(button: Button) {
        clickedButton.setTextColor(Color.BLACK)
        clickedButton = button.apply { setTextColor(Color.RED)}
    }

    fun updateChar() =
        buttons.forEach { button -> button.text = charBoxMap[button].toString() }

    fun shuffle() {
        val currentCharBoxes = charBoxMap.values.toTypedArray()
        buttons
            .mapIndexed { index, _ -> index }
            .also(Collections::shuffle)
            .zip(buttons)
            .forEach { (index, button) ->
                charBoxMap.put(button, currentCharBoxes[index])
            }
    }
}
