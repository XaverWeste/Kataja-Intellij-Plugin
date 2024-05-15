package com.github.ktj.katajaintellijplugin

import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerPosition
import com.intellij.psi.tree.IElementType

class KatajaLexer: Lexer() {
    
    private lateinit var buffer: CharSequence
    private var pos: Int = -1
    private var end: Int = -1

    /**
     * Prepare for lexing character data from {@code buffer} passed. Internal lexer state is supposed to be {@code initialState}. It is guaranteed
     * that the value of initialState is the same as returned by {@link #getState()} method of this lexer at condition {@code startOffset=getTokenStart()}.
     * This method is used to incrementally re-lex changed characters using lexing data acquired from this particular lexer sometime in the past.
     *
     * @param buffer       character data for lexing.
     * @param startOffset  offset to start lexing from
     * @param endOffset    offset to stop lexing at
     * @param initialState the initial state of the lexer.
     */
    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        pos = startOffset
        end = endOffset
    }

    override fun getState(): Int = 0

    /**
     * Returns the token at the current position of the lexer or {@code null} if lexing is finished.
     *
     * @return the current token.
     */
    override fun getTokenType(): IElementType? {
        TODO("Not yet implemented")
    }

    /**
     * Returns the start offset of the current token.
     *
     * @return the current token start offset.
     */
    override fun getTokenStart(): Int {
        TODO("Not yet implemented")
    }

    /**
     * Returns the end offset of the current token.
     *
     * @return the current token end offset.
     */
    override fun getTokenEnd(): Int {
        TODO("Not yet implemented")
    }

    /**
     * Advances the lexer to the next token.
     */
    override fun advance() {
        TODO("Not yet implemented")
    }

    override fun getCurrentPosition(): LexerPosition = KatajaLexerPosition(pos, state)

    override fun restore(position: LexerPosition) {
        pos = position.offset
    }

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = end
}

class KatajaLexerPosition(private val offset: Int, private val state: Int): LexerPosition{
    
    override fun getOffset(): Int = offset

    override fun getState(): Int = state

}