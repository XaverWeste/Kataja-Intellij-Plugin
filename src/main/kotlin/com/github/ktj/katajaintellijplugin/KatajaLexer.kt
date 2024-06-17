package com.github.ktj.katajaintellijplugin

import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerPosition
import com.intellij.psi.tree.IElementType

class KatajaLexer : Lexer() {
    
    private lateinit var buffer: CharSequence
    private var state = 0
    private var pos: Int = -2
    private var end: Int = 0
    private var startToken: Int = 0
    private var endToken: Int = 0
    private val keywords: Set<String> = setOf("if", "else", "while", "return", "true", "false", "null", "this", "main", "class", "interface", "type", "data", "public", "private", "protected", "static", "synchronised", "abstract", "void", "short", "int", "long", "boolean", "double", "float", "byte", "char", "use", "from", "as", "const")

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        pos = startOffset - 1
        end = endOffset - 1
        state = initialState
    }

    override fun getState(): Int = state

    override fun getTokenType(): IElementType? {
        if(pos == -2 || pos > end) return null

        advance()
        pos = startToken - 1

        return when(state){
            1 -> KatajaTokenSet.IDENTIFIER.types[0]
            2 -> KatajaTokenSet.SPECIAL.types[0]
            3 -> KatajaTokenSet.OPERATOR.types[0]
            4 -> KatajaTokenSet.NUMBER.types[0]
            5 -> KatajaTokenSet.KEYWORD.types[0]
            6 -> KatajaTokenSet.COMMENT.types[0]
            7 -> KatajaTokenSet.WHITESPACE.types[0]
            8 -> KatajaTokenSet.NEW_LINE.types[0]
            9 -> KatajaTokenSet.END_OF_STATEMENT.types[0]
            10 -> KatajaTokenSet.STRING.types[0]
            11 -> KatajaTokenSet.CHAR.types[0]
            12 -> KatajaTokenSet.BAD_CHARACTER.types[0]
            else -> null
        }
    }

    override fun getTokenStart(): Int = startToken

    override fun getTokenEnd(): Int = endToken +1

    override fun advance() {
        if(pos < end){
            pos++
            startToken = pos

            val current: Char = buffer[pos]

            if(current == '\n') state = 8
            else if(current == ';') state = 9
            else if(current == '\''){
                if(pos + 2 > end || buffer[pos + 2] != '\'') state = 12
                else{
                    state = 11
                    pos += 2
                }
            }else if(current == '"'){
                if(pos == end) state = 12
                else {
                    pos++
                    while (pos < end && !setOf('"', '\n').contains(buffer[pos])) pos++

                    state = if(buffer[pos] == '"') 10
                    else 12
                }
            }else if(setOf(' ', '\t').contains(current)){
                state = 7
                while(pos < end && setOf(' ', '\t').contains(buffer[pos + 1])) pos++
            }else if(current == '#'){
                state = 6
                while(pos < end && !setOf('#', '\n').contains(buffer[pos + 1])) pos++
            }else if(current > ('0' - 1) && current < ('9' + 1)){
                state = 4
                while(pos < end && buffer[pos + 1] > ('0' - 1) && buffer[pos + 1] < ('9' + 1)) pos++

                if(pos < end && buffer[pos + 1] == '.'){
                    pos++
                    while(pos < end && buffer[pos + 1] > ('0' - 1) && buffer[pos + 1] < ('9' + 1)) pos++

                    if(pos < end && setOf('i', 'd', 'l', 's', 'f').contains(buffer[pos + 1])) pos++
                }
            }else if(setOf('+', '-', '*', '/', '^', '!', '%', '&', '=', '|', '<', '>', '~', '\\').contains(current)){
                state = 3
                while(pos < end && setOf('+', '-', '*', '/', '^', '!', '%', '&', '=', '|', '<', '>', '~', '\\').contains(buffer[pos + 1])) pos++
            }else if((current > ('a' - 1) && current < ('z' + 1)) || (current > ('A' - 1) && current < ('Z' + 1))){
                state = 1
                while(pos < end && ((buffer[pos + 1] == '_') || (buffer[pos + 1] > ('a' - 1) && buffer[pos + 1] < ('z' + 1)) || (buffer[pos + 1] > ('A' - 1) && buffer[pos + 1] < ('Z' + 1)) || (buffer[pos + 1] > ('0' - 1) && buffer[pos + 1] < ('9' + 1)))) pos++

                if(keywords.contains(buffer.subSequence(startToken, pos + 1).toString())) state = 5
            }else state = 2

            endToken = pos
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