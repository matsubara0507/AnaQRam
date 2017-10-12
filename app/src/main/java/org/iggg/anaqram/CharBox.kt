package org.iggg.anaqram

internal class CharBox(private val value: Char) {
    private var flag: Boolean = false

    init {
        flag = false
    }

    fun setFlag() {
        flag = true
    }

    fun resetFlag() {
        flag = false
    }

    override fun toString(): String {
        return (if (flag) value else defaultValue).toString()
    }

    companion object {
        private val defaultValue = '？'
    }
}
