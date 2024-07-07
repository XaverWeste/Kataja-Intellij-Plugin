package com.github.ktj.katajaintellijplugin

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext

class KatajaCompletionContributor : CompletionContributor() {

    init{
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(),
            object : CompletionProvider<CompletionParameters?>() {
                override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, resultSet: CompletionResultSet){
                    for(s: String in KatajaLexer.keywords){
                        if(s.startsWith(parameters.position.text[0])){
                            resultSet.addElement(LookupElementBuilder.create(s))
                        }
                    }
                }
            })
    }
}