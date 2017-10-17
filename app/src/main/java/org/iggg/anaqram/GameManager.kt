package org.iggg.anaqram

import android.os.Handler
import android.widget.TextView

import java.util.Locale
import java.util.Timer
import kotlin.concurrent.schedule

internal class GameManager(val answer: String, private val timerText: TextView) {
    val charBoxes: List<CharBox> = answer.toCharArray().map { c -> CharBox(c) }
    var isRunning = false
        private set
    private var timer: Timer? = null
    private var timeCount: Long = 0
    private var clear = false

    companion object {
        val handler = Handler()
        val initTime = "00:00"
    }

    init {
        timerText.text = initTime
    }

    fun displayChar(qrText: String): String =
        qrText.toIntOrNull()?.let {
            // 剰余を取って文字数未満の数字が出ても大丈夫にしている
            val index = Integer.valueOf(qrText)!! % charBoxes.size
            charBoxes[index].open()
            "「" + charBoxes[index] + "」をみつけた！v(≧∇≦)v"
        } ?: "ちがうQRコードだよ！(*￣∀￣)\"b\" ﾁｯﾁｯﾁｯ"

    fun accept(solution: String): String? =
        if (solution == answer && !clear) {
            clear = true
            timer?.cancel()
            "くりあ～～ ヽ(^◇^*)/"
        } else null

    fun start() {
        timer?.cancel()
        timer = Timer().apply { schedule(0, 100, {
            handler.post {
                timeCount++
                val mm = timeCount * 100 / 1000 / 60
                val ss = timeCount * 100 / 1000 % 60
                timerText.text = String.format(Locale.US, "%1$02d:%2$02d", mm, ss)
            }
        }) }
        timerText.text = initTime
        isRunning = true
        timeCount = 0
    }

    fun reset() {
        timer?.cancel()
        timer = null
        timerText.text = initTime
        charBoxes.forEach { charBox -> charBox.reset() }
        isRunning = false
        clear = false
    }

    fun second(): Int = (timeCount * 100 / 1000).toInt()
}
