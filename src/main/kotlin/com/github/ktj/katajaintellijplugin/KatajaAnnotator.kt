package com.github.ktj.katajaintellijplugin

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement

class KatajaAnnotator: Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder){
        println(element.javaClass)
    }
}