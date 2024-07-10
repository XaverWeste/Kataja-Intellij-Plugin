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
        while(builder.tokenType == KatajaTokenTypes.COMMENT || builder.tokenType == KatajaTokenTypes.WHITESPACE) builder.advanceLexer()

        do{
            if(!(builder.tokenType == KatajaTokenTypes.NEW_LINE || builder.tokenText.equals(";"))){
                when(builder.tokenText){
                    "use" -> parseUse()
                    "public", "private", "protected", "const", "final", "synchronised", "abstract", "static" -> parseMod(true)
                    "type" -> parseType()
                    "class" -> parseClass()
                    null -> {
                        marker.done(type)
                        return builder.treeBuilt
                    }
                    else -> builder.error("Illegal statement")
                }
            }
            skipWhitespace()
            builder.advanceLexer()
        }while(true)
    }

    private fun parseUse(){
        assert(KatajaTokenTypes.IDENTIFIER)

        if(isNext(KatajaTokenTypes.SINGLE)){
            while(isNext(KatajaTokenTypes.SINGLE)){
                builder.advanceLexer()
                when(builder.tokenText){
                    "," -> {}
                    ";" -> return
                    else -> builder.error("Expected ,")
                }
                assert(KatajaTokenTypes.IDENTIFIER)
            }

            assert("from")
            assert(KatajaTokenTypes.IDENTIFIER)

            while(isNext(KatajaTokenTypes.OPERATOR)){
                assert("/")
                assert(KatajaTokenTypes.IDENTIFIER)
            }
        }else{
            if(isNext(KatajaTokenTypes.KEYWORD)){
                assert("from")
                assert(KatajaTokenTypes.IDENTIFIER)
            }

            while(isNext(KatajaTokenTypes.OPERATOR)){
                assert("/")
                assert(KatajaTokenTypes.IDENTIFIER)
            }

            if(isNext(KatajaTokenTypes.KEYWORD)){
                assert("as")
                assert(KatajaTokenTypes.IDENTIFIER)
            }
        }

        assertEnd()
    }

    private fun parseMod(allowClass: Boolean){
        while(isNext(KatajaTokenTypes.KEYWORD)){
            assert(KatajaTokenTypes.KEYWORD)
            when(builder.tokenText){
                "const", "final", "synchronised", "abstract", "static" -> {}
                "type" -> {
                    if(!allowClass) builder.error("Illegal statement")
                    parseType()
                    return
                }
                "class" -> {
                    if(!allowClass) builder.error("Illegal statement")
                    parseClass()
                    return
                }
                "int", "short", "long", "boolean", "byte", "char", "float", "double" -> parseMethodOrField(false)
                else -> builder.error("Expected modifier")
            }
        }

        parseMethodOrField(true)
    }

    private fun parseType(){
        assert(KatajaTokenTypes.IDENTIFIER)
        assert("=")
        assert(KatajaTokenTypes.IDENTIFIER)
        while(isNext(KatajaTokenTypes.OPERATOR)){
            assert("|")
            assert(KatajaTokenTypes.IDENTIFIER)
        }
        assertEnd()
    }

    private fun parseClass(){
        assert(KatajaTokenTypes.IDENTIFIER)
        if(isNext(KatajaTokenTypes.KEYWORD)){
            assert("extends")
            assert(KatajaTokenTypes.IDENTIFIER)
            while(isNext(KatajaTokenTypes.SINGLE)){
                assert(",")
                assert(KatajaTokenTypes.IDENTIFIER)
            }
        }
        assert("{")
        while(!isNext(KatajaTokenTypes.SPECIAL)){
            if(!hasNext()){
                builder.error("Expected }")
                return
            }
            if(isNext(KatajaTokenTypes.NEW_LINE)) next()
            else if(isNext(KatajaTokenTypes.SINGLE)) assert(";")
            else parseMethodOrField(true)
        }
        assert("}")
        assertEnd()
    }

    private fun parseMethodOrField(parseType: Boolean){
        var method = false

        if(parseType){
            if (isNext(KatajaTokenTypes.KEYWORD)){
                assert(KatajaTokenTypes.KEYWORD)
                if(!setOf("int", "double", "short", "long", "float", "char", "byte", "boolean").contains(builder.tokenText))
                assert(KatajaTokenTypes.IDENTIFIER)
            }else{
                assert(KatajaTokenTypes.IDENTIFIER)
                if (isNext(KatajaTokenTypes.IDENTIFIER)) assert(KatajaTokenTypes.IDENTIFIER)
                else method = true
            }
        }else assert(KatajaTokenTypes.IDENTIFIER)

        if(isNext(KatajaTokenTypes.SPECIAL) || method){
            assert(KatajaTokenTypes.SPECIAL)
            when(builder.tokenText){
                "(" -> {
                    if(isNext(KatajaTokenTypes.SPECIAL)) assert(")")
                    else{
                        do{
                            if(isNext(KatajaTokenTypes.KEYWORD)){
                                assert(KatajaTokenTypes.KEYWORD)
                                if(!setOf("int", "double", "short", "long", "float", "char", "byte", "boolean").contains(builder.tokenText)) builder.error("Expected type")
                            }else assert(KatajaTokenTypes.IDENTIFIER)

                            assert(KatajaTokenTypes.IDENTIFIER)

                            if(isNext(KatajaTokenTypes.SPECIAL)){
                                assert(KatajaTokenTypes.SPECIAL)
                                if(builder.tokenText.equals(")")) break
                                else if(!builder.tokenText.equals(",") || builder.eof()) builder.error("Expected )")
                            }
                        }while(true)
                    }
                }
                "=" -> {}
                else -> builder.error("Expected (")
            }
        }

        assertEnd()
    }

    private fun assertEnd(){
        if(!hasNext()) return

        builder.advanceLexer()
        if(builder.tokenType != KatajaTokenTypes.NEW_LINE && !builder.tokenText.equals(";") && !builder.eof()){
            builder.error("Expected end of statement")
        }
    }

    private fun assert(s: String){
        if(!hasNext()){
            builder.advanceLexer()
            builder.error("Expected $s")
            return
        }

        builder.advanceLexer()

        if(!builder.tokenText.equals(s)){
            builder.error("Expected $s")
        }
    }

    private fun assert(type: IElementType){
        if(!isNext(type)){
            builder.advanceLexer()
            builder.error("Expected $type")
        }else builder.advanceLexer()
    }

    private fun isNext(type: IElementType): Boolean{
        if(!hasNext()) return false
        return builder.lookAhead(1) == type
    }

    private fun next(){
        if(!hasNext()) builder.error("Expected next")
        builder.advanceLexer()
    }

    private fun hasNext(): Boolean{
        skipWhitespace()
        return !builder.eof()
    }

    private fun skipWhitespace(){
        while(!builder.eof() && (builder.lookAhead(1) == KatajaTokenTypes.WHITESPACE || builder.lookAhead(1) == KatajaTokenTypes.COMMENT)){
            builder.advanceLexer()
        }
    }
}