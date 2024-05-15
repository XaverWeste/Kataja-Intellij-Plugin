package com.github.ktj.katajaintellijplugin

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

class KatajaSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        TODO("Not yet implemented")
    }

    override fun getTokenHighlights(element: IElementType?): Array<TextAttributesKey> {
        TODO("Not yet implemented")
    }
}