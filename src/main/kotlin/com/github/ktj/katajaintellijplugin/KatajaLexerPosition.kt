package com.github.ktj.katajaintellijplugin

import com.intellij.lexer.LexerPosition

class KatajaLexerPosition(private val offset: Int, private val state: Int): LexerPosition {

    override fun getOffset(): Int = offset

    override fun getState(): Int = state

}