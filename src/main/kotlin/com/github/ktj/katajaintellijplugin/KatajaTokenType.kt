package com.github.ktj.katajaintellijplugin

import com.intellij.psi.tree.IElementType

class KatajaTokenType(debugName: String): IElementType(debugName, KatajaLanguage.INSTANCE) {

    override fun toString(): String = "KatajaTokenType." + super.toString()
}