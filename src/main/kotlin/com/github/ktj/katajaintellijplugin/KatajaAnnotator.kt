package com.github.ktj.katajaintellijplugin

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType

class KatajaAnnotator: Annotator {

    private lateinit var uses: java.util.HashMap<String, String>
    private lateinit var holder: AnnotationHolder

    override fun annotate(element: PsiElement, holder: AnnotationHolder){
        if(element.node.elementType == FILE){
            this.holder = holder
            annotateFile(element)
        }
    }

    private fun annotateFile(element: PsiElement){
        uses = hashMapOf("String" to "java.lang.String", "Object" to "java.lang.Object")
    }

    private fun filterChildren(arr: Array<PsiElement>, type: IElementType): MutableList<PsiElement>{
        val list = mutableListOf<PsiElement>()
        for(element: PsiElement in arr) if(element.node.elementType == type) list.add(element)
        return list
    }
}