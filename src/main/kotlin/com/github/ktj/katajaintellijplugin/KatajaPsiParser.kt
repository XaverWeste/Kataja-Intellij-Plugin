package com.github.ktj.katajaintellijplugin

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

class KatajaPsiParser: PsiParser {
    override fun parse(type: IElementType, builder: PsiBuilder): ASTNode {
        val marker = builder.mark()

        while (!builder.eof()){
            builder.advanceLexer()
        }

        marker.done(type)

        return builder.treeBuilt
    }
}