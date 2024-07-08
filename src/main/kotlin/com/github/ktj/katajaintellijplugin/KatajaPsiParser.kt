package com.github.ktj.katajaintellijplugin

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

class KatajaPsiParser: PsiParser {

    private lateinit var builder: PsiBuilder

    override fun parse(type: IElementType, builder: PsiBuilder): ASTNode {
        this.builder = builder

        val marker = builder.mark()
        var eof = false

        while (!eof){
            when(builder.tokenText){
                "use" -> parseUse()
                "type" -> parseType()
            }

            if(builder.eof()) eof = true
            else builder.advanceLexer()
        }

        marker.done(type)

        return builder.treeBuilt
    }

    private fun parseUse(){
        assert(KatajaTokenTypes.IDENTIFIER)

        var mult = false
        next()

        while(builder.tokenText.equals(",")){
            mult = true
            assert(KatajaTokenTypes.IDENTIFIER)
            next()
        }

        if(mult){
            if(!builder.tokenText.equals("from")) error("Expected from")

            assert(KatajaTokenTypes.IDENTIFIER)
            next()

            while(builder.tokenText.equals("/")){
                assert(KatajaTokenTypes.IDENTIFIER)
                if(hasNext()) next()
            }
        }else{
            if(builder.tokenText.equals("from")){
                assert(KatajaTokenTypes.IDENTIFIER)
                next()
            }

            while(builder.tokenText.equals("/")){
                assert(KatajaTokenTypes.IDENTIFIER)
                if(hasNext()) next()
            }

            builder.tokenText.equals("as")
            assert(KatajaTokenTypes.IDENTIFIER)
        }

        if(hasNext()) builder.error("Expected new line")
    }

    private fun parseType(){
        assert(KatajaTokenTypes.IDENTIFIER)
        assert("=")
        assert(KatajaTokenTypes.IDENTIFIER)

        while(hasNext()){
            assert("|")
            assert(KatajaTokenTypes.IDENTIFIER)
        }
    }

    private fun hasNext(): Boolean{
        while(!builder.eof() && (builder.lookAhead(1) == KatajaTokenTypes.WHITESPACE || builder.lookAhead(1) == KatajaTokenTypes.COMMENT)){
            builder.advanceLexer()
        }

        if(builder.eof()) return false
        if(builder.lookAhead(1) == KatajaTokenTypes.NEW_LINE) builder.advanceLexer()

        return builder.tokenType != KatajaTokenTypes.NEW_LINE
    }

    private fun assert(s: String){
        next()

        if(!builder.tokenText.equals(s)) builder.error("Expected $s")
    }

    private fun assert(type: IElementType){
        next()

        if(builder.tokenType != type) builder.error("Expected $type")
    }

    private fun isNext(type: IElementType): Boolean{
        next()

        if(builder.eof()) return false

        return builder.lookAhead(1) == type
    }

    private fun next(){
        while(!builder.eof() && (builder.lookAhead(1) == KatajaTokenTypes.WHITESPACE || builder.lookAhead(1) == KatajaTokenTypes.COMMENT)){
            builder.advanceLexer()
        }

        if(!builder.eof()) builder.advanceLexer()
    }
}