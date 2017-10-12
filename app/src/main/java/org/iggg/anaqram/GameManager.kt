package org.iggg.anaqram

import android.os.Handler
import android.widget.TextView

import java.util.Locale
import java.util.Timer
import java.util.TimerTask

internal class GameManager(val answer: String, private val timerText: TextView) {
    private var clear = false
    val charBoxes: List<CharBox> = answer.toCharArray().map { c -> CharBox(c) }

    var isRunning = false
        private set
    private var timer: Timer? = null
    private val handler = Handler()
    private var count: Long = 0

    private val initTime = "00:00"

    init {
        timerText.text = initTime
    }

    fun displayChar(qrText: String): String {
        try {
            // 剰余を取って文字数未満の数字が出ても大丈夫にしている
            val index = Integer.valueOf(qrText)!! % charBoxes.size
            charBoxes[index].open()
            return "「" + charBoxes[index] + "」をみつけた！v(≧∇≦)v"
        } catch (e: NumberFormatException) {
            // qrText が数字以外の場合
            return "ちがうQRコードだよ！(*￣∀￣)\"b\" ﾁｯﾁｯﾁｯ"
        }

    }

    fun accept(solution: String): String? {
        if (solution == answer && !clear) {
            clear = true
            timer?.cancel()
            return "くりあ～～ ヽ(^◇^*)/"
        }
        return null
    }

    fun start() {
        timer?.cancel()
        timer = Timer()
        count = 0
        timerText.text = initTime
        timer?.schedule(object : TimerTask() {
            override fun run() {
                handler.post {
                    count++
                    val mm = count * 100 / 1000 / 60
                    val ss = count * 100 / 1000 % 60
                    timerText.text = String.format(Locale.US, "%1$02d:%2$02d", mm, ss)
                }
            }
        }, 0, 100)
        isRunning = true
    }

    fun reset() {
        timer?.cancel()
        timer = null
        timerText.text = initTime

        charBoxes.map { charBox -> charBox.reset() }

        isRunning = false
        clear = false
    }
}