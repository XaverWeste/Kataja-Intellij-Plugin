package com.github.ktj.katajaintellijplugin.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

class KtjPsiElement(node: ASTNode?) : ASTWrapperPsiElement(node!!), PsiElement {
}