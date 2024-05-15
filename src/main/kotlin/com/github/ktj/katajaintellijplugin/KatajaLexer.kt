package com.github.ktj.katajaintellijplugin

import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerPosition
import com.intellij.psi.tree.IElementType

class KatajaLexer: Lexer() {
    
    private lateinit var buffer: CharSequence
    private var state = 0
    private var pos: Int = -1
    private var end: Int = -1
    private var start: Int = -1

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        pos = startOffset
        end = endOffset
        state = initialState
    }

    override fun getState(): Int = state

    override fun getTokenType(): IElementType? {
        if(pos == -1 || pos > end) return null

        when(state){
            1 -> KatajaElementType("Identifier")
            2 -> KatajaElementType("Special")
            3 -> KatajaElementType("Operator")
            4 -> KatajaElementType("Number")
            5 -> KatajaElementType("Decimal Number")
            6 -> KatajaElementType("Comment")
            7 -> KatajaElementType("Whitespace")
            8 -> KatajaElementType("New Line")
            9 -> KatajaElementType("End of Statement")
        }

        return null
    }

    override fun getTokenStart(): Int = start

    override fun getTokenEnd(): Int = pos

    override fun advance() {
        if(pos > end){
            val current: Char = buffer[pos++]

            if(current == '\n') state = 8
            else if(current == ';') state = 9
            else if(setOf(' ', '\t').contains(current)){
                state = 7
                while(pos < end && setOf(' ', '\t').contains(buffer[pos + 1])) pos++
            }else if(current == '#'){
                state = 6
                while(pos < end && !setOf('#', '\n').contains(buffer[pos + 1])) pos++
            }else if(current > ('0' - 1) && current < ('9' + 1)){
                state = 4
                while(pos < end && buffer[pos + 1] > ('0' - 1) && buffer[pos + 1] < ('9' + 1)) pos++

                if(pos < end && buffer[pos + 1] == '.'){
                    state = 5
                    while(pos < end && buffer[pos + 1] > ('0' - 1) && buffer[pos + 1] < ('9' + 1)) pos++

                    if(pos < end && setOf('i', 'd', 'l', 's', 'f').contains(buffer[pos + 1])) pos++
                }
            }else if(setOf('+', '-', '*', '/', '^', '!', '%', '&', '=', '|', '<', '>', '~', '\\').contains(current)){
                state = 3
                while(pos < end && setOf('+', '-', '*', '/', '^', '!', '%', '&', '=', '|', '<', '>', '~', '\\').contains(buffer[pos + 1])) pos++
            }else if((current > ('a' - 1) && current < ('z' + 1)) || (current > ('A' - 1) && current < ('Z' + 1))){
                state = 1
                while(pos < end && ((buffer[pos + 1] == '_') || (buffer[pos + 1] > ('a' - 1) && buffer[pos + 1] < ('z' + 1)) || (buffer[pos + 1] > ('A' - 1) && buffer[pos + 1] < ('Z' + 1)) || (buffer[pos + 1] > ('0' - 1) && buffer[pos + 1] < ('9' + 1)))) pos++
            }else state = 2
        }else state = 0
    }

    override fun getCurrentPosition(): LexerPosition = KatajaLexerPosition(pos, state)

    override fun restore(position: LexerPosition) {
        pos = position.offset
        state = position.state
    }

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = end
}