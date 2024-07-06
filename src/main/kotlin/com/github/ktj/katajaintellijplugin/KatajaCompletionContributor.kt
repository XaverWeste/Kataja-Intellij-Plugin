package com.github.ktj.katajaintellijplugin

import com.intellij.codeInsight.completion.*
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext


class KatajaCompletionContributor : CompletionContributor() {

    init {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(),
            object : CompletionProvider<CompletionParameters?>() {
                override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, resultSet: CompletionResultSet){
                    println(parameters.position.text)
                }
            })
    }
}