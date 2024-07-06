package com.github.ktj.katajaintellijplugin.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

object PsiFactory {
    fun createElement(node: ASTNode?): PsiElement = KtjPsiElement(node)
}