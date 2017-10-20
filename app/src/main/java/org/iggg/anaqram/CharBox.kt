package org.iggg.anaqram

internal class CharBox(private val value: Char = defaultValue) {
    private var opened: Boolean = false

    fun open() {
        opened = true
    }

    fun reset() {
        opened = false
    }

    override fun toString(): String =
            (if (opened) value else defaultValue).toString()

    companion object {
        private val defaultValue = '？'
    }
}
