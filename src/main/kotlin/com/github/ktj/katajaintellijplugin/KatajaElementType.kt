package com.github.ktj.katajaintellijplugin

import com.github.ktj.katajaintellijplugin.language.KatajaLanguage
import com.intellij.psi.tree.IElementType

class KatajaElementType(debugName: String): IElementType(debugName, KatajaLanguage.INSTANCE)