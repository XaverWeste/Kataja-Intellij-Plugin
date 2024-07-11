package com.github.ktj.katajaintellijplugin

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.lang.SyntaxTreeBuilder.Marker
import com.intellij.psi.tree.IElementType

class KatajaPsiParser: PsiParser {

    private lateinit var builder: PsiBuilder

    override fun parse(type: IElementType, builder: PsiBuilder): ASTNode {
        this.builder = builder

        val marker = builder.mark()
        while(builder.tokenType == KatajaTokenTypes.COMMENT || builder.tokenType == KatajaTokenTypes.WHITESPACE) builder.advanceLexer()

        do{
            if(builder.tokenType != KatajaTokenTypes.NEW_LINE){
                when(builder.tokenText){
                    "use" -> parseUse()
                    "public", "private", "protected", "const", "final", "synchronised", "abstract", "static" -> parseMod(true)
                    "int", "short", "long", "boolean", "byte", "char", "float", "double" -> parseMethodOrField(false)
                    "type" -> parseType(builder.mark())
                    "data" -> parseData(builder.mark())
                    "interface" -> parseInterface(builder.mark())
                    "class" -> parseClass(builder.mark())
                    null -> {
                        marker.done(type)
                        return builder.treeBuilt
                    }
                    else -> {
                        if(builder.tokenType == KatajaTokenTypes.IDENTIFIER) parseMethodOrField(false)
                        else builder.error("Illegal type")
                    }
                }
            }
            skipWhitespace()
            builder.advanceLexer()
        }while(true)
    }

    private fun parseUse(){
        val marker = builder.mark()
        if(isNext(KatajaTokenTypes.SINGLE)) assert("$")
        assert(KatajaTokenTypes.IDENTIFIER)

        if(isNext(KatajaTokenTypes.SINGLE)){
            while(isNext(KatajaTokenTypes.SINGLE)){
                assert(",")
                if(isNext(KatajaTokenTypes.SINGLE)) assert("$")
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
        marker.done(KatajaElementTypes.USE)
    }

    private fun parseMod(allowClass: Boolean){
        val marker = builder.mark()
        while(isNext(KatajaTokenTypes.KEYWORD)){
            assert(KatajaTokenTypes.KEYWORD)
            when(builder.tokenText){
                "const", "final", "synchronised", "abstract", "static" -> {}
                "type" -> {
                    if(!allowClass) builder.error("Illegal statement")
                    parseType(marker)
                    return
                }
                "data" -> {
                    if(!allowClass) builder.error("Illegal statement")
                    parseData(marker)
                    return
                }
                "interface" -> {
                    if(!allowClass) builder.error("Illegal statement")
                    parseInterface(marker)
                    return
                }
                "class" -> {
                    if(!allowClass) builder.error("Illegal statement")
                    parseClass(marker)
                    return
                }
                "int", "short", "long", "boolean", "byte", "char", "float", "double" -> parseMethodOrField(false)
                else -> builder.error("Expected modifier")
            }
        }

        parseMethodOrField(true)
    }

    private fun parseType(marker: Marker){
        assert(KatajaTokenTypes.IDENTIFIER)
        assert("=")
        assert(KatajaTokenTypes.IDENTIFIER)
        while(isNext(KatajaTokenTypes.OPERATOR)){
            assert("|")
            assert(KatajaTokenTypes.IDENTIFIER)
        }
        assertEnd()
        marker.done(KatajaElementTypes.TYPE)
    }

    private fun parseData(marker: Marker){
        assert(KatajaTokenTypes.IDENTIFIER)
        assert("=")
        assert("(")
        if(!isNext(KatajaTokenTypes.SPECIAL)){
            do {
                parseDataType()
                assert(KatajaTokenTypes.IDENTIFIER)

                if (isNext(KatajaTokenTypes.SINGLE)) {
                    assert(",")
                } else break
            } while (true)
        }
        assert(")")
        assertEnd()
        marker.done(KatajaElementTypes.DATA)
    }

    private fun parseInterface(marker: Marker){
        assert(KatajaTokenTypes.IDENTIFIER)
        assert("{")
        while(!isNext(KatajaTokenTypes.SPECIAL)){
            if(isNext(KatajaTokenTypes.NEW_LINE)) next()
            else{
                if(!hasNext()){
                    builder.error("Expected }")
                    return
                }
                parseMod(false)
            }
        }
        assert("}")
        assertEnd()
        marker.done(KatajaElementTypes.INTERFACE)
    }

    private fun parseClass(marker: Marker){
        builder.mark()
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
            if(isNext(KatajaTokenTypes.NEW_LINE)) next()
            else{
                if(!hasNext()){
                    builder.error("Expected }")
                    return
                }
                parseMod(false)
            }
        }
        assert("}")
        assertEnd()
        marker.done(KatajaElementTypes.CLASS)
    }

    private fun parseMethodOrField(parseType: Boolean){
        var method = false

        if(parseType){
            if(isNext(KatajaTokenTypes.KEYWORD)){
                parseDataType()
                assert(KatajaTokenTypes.IDENTIFIER)
            }else{
                assert(KatajaTokenTypes.IDENTIFIER)
                if(isNext(KatajaTokenTypes.IDENTIFIER)) assert(KatajaTokenTypes.IDENTIFIER)
                else method = true
            }
        }else assert(KatajaTokenTypes.IDENTIFIER)

        if(isNext(KatajaTokenTypes.SPECIAL) || method){
            method = true
            assert("(")

            if(!isNext(KatajaTokenTypes.SPECIAL)) {
                do {
                    parseDataType()
                    assert(KatajaTokenTypes.IDENTIFIER)

                    if (isNext(KatajaTokenTypes.SINGLE)) {
                        assert(",")
                    } else break
                } while (true)
            }

            assert(")")
        }

        if(hasNext()){
            if(isNext(KatajaTokenTypes.SPECIAL)) parseContent()
            else{
                assert(KatajaTokenTypes.OPERATOR)
                when(builder.tokenText){
                    "=" -> parseCalc()
                    "->" -> {
                        if(!method) builder.error("Expected =")
                        else parseStatement()
                    }
                    else -> builder.error("Illegal argument")
                }
            }
        }

        assertEnd()
        return
    }

    private fun parseStatement(){
        if(isNext(KatajaTokenTypes.IDENTIFIER)) assert(KatajaTokenTypes.IDENTIFIER)
        else assert(KatajaTokenTypes.KEYWORD)

        when(builder.tokenText){
            "return", "throw" -> parseCalc()
            "while" -> parseWhile()
            "if" -> parseIf()
            "switch" -> parseSwitch()
            else -> {
                if(builder.tokenType == KatajaTokenTypes.KEYWORD){
                    if (!setOf("int", "short", "long", "double", "float", "boolean", "char", "byte").contains(builder.tokenText)) builder.error("Expected type")
                    while(isNext(KatajaTokenTypes.SPECIAL)){
                        assert("[")
                        assert("]")
                    }
                    assert(KatajaTokenTypes.IDENTIFIER)
                    assert("=")
                    parseCalc()
                }else{
                    if(isNext(KatajaTokenTypes.SPECIAL)){
                        assert(KatajaTokenTypes.SPECIAL)
                        if(builder.tokenText.equals("(")){
                            if(isNext(KatajaTokenTypes.SPECIAL)) assert(")")
                            else{
                                parseCalc()
                                while (isNext(KatajaTokenTypes.SINGLE)) {
                                    assert(",")
                                    parseCalc()
                                }
                                assert(")")
                            }
                            while(builder.tokenText.equals("[")) {
                                parseCalc()
                                assert("]")
                                if(isNext(KatajaTokenTypes.SPECIAL)) assert(KatajaTokenTypes.SPECIAL)
                            }
                            if(builder.tokenText.equals(".")){
                                assert(KatajaTokenTypes.IDENTIFIER)
                                parseCall()
                            }else if(!builder.tokenText.equals(")") && !builder.tokenText.equals("]")) builder.error("Illegal argument")
                        }else if(builder.tokenText.equals(".")){
                            assert(KatajaTokenTypes.IDENTIFIER)
                            parseCall()
                        }else if(builder.tokenText.equals("[")){
                            if(isNext(KatajaTokenTypes.SPECIAL)){
                                assert(KatajaTokenTypes.SPECIAL)
                                if(builder.tokenText.equals("]")){
                                    while(isNext(KatajaTokenTypes.SPECIAL)){
                                        assert("[")
                                        assert("]")
                                    }
                                    assert(KatajaTokenTypes.IDENTIFIER)
                                    assert("=")
                                    parseCall()
                                    assertEnd()
                                    return
                                }else if(builder.tokenText.equals("(")){
                                    parseCalc()
                                    assert(")")
                                    if(isNext(KatajaTokenTypes.OPERATOR)){
                                        assert(KatajaTokenTypes.OPERATOR)
                                        parseCalc()
                                    }
                                    assert("]")
                                    while(isNext(KatajaTokenTypes.SPECIAL)){
                                        assert(KatajaTokenTypes.SPECIAL)
                                        if(builder.tokenText.equals("[")){
                                            parseCalc()
                                            assert("]")
                                        }else if (builder.tokenText.equals(".")){
                                            assert(KatajaTokenTypes.IDENTIFIER)
                                            parseCall()
                                            break
                                        }else{
                                            builder.error("Illegal argument")
                                            break
                                        }
                                    }
                                }else builder.error("Illegal argument")
                            }else{
                                while(builder.tokenText.equals("[")) {
                                    parseCalc()
                                    assert("]")
                                    if(isNext(KatajaTokenTypes.SPECIAL)) assert(KatajaTokenTypes.SPECIAL)
                                }
                                if(builder.tokenText.equals(".")){
                                    assert(KatajaTokenTypes.IDENTIFIER)
                                    parseCall()
                                }else if(!builder.tokenText.equals("]")) builder.error("Illegal argument")
                            }
                        }else{
                            builder.error("Illegal argument")
                            assertEnd()
                            return
                        }

                        if(hasNext()){
                            assert("=")
                            parseCalc()
                        }
                    }else{
                        if(isNext(KatajaTokenTypes.IDENTIFIER)){
                            assert(KatajaTokenTypes.IDENTIFIER)
                            assert("=")
                            parseCalc()
                        }else{
                            assert("=")
                            parseCalc()
                        }
                    }
                }
            }
        }

        assertEnd()
    }

    private fun parseWhile(){
        parseCalc()
        if(builder.tokenText.equals("->")){
            parseStatement()
        }else{
            assert("{")
            while(!isNext(KatajaTokenTypes.SPECIAL)){
                if(isNext(KatajaTokenTypes.NEW_LINE)) next()
                else{
                    if(!hasNext()){
                        builder.error("Expected }")
                        return
                    }
                    parseStatement()
                }
            }
            assert("}")
        }
    }

    private fun parseIf(){
        parseCalc()
        if(builder.tokenText.equals("->")) parseStatement()
        else parseContent()

        if(hasNext()){
            assert("else")
            if(isNext(KatajaTokenTypes.IDENTIFIER)) assert("if")
            parseIf()
        }
    }

    private fun parseSwitch(){
        parseCalc()
        assert("{")
        while(!isNext(KatajaTokenTypes.SPECIAL)){
            if(isNext(KatajaTokenTypes.NEW_LINE)) next()
            else{
                if(!hasNext()){
                    builder.error("Expected }")
                    return
                }
                assert(KatajaTokenTypes.KEYWORD)
                if(builder.tokenText.equals("case")) parseCalcArg()
                else if(!builder.tokenText.equals("default")) builder.error("Illegal argument")

                if(isNext(KatajaTokenTypes.OPERATOR)){
                    assert("->")
                    parseStatement()
                }else parseContent()
            }
        }
        assert("}")
    }

    private fun parseCalc(){
        parseCalcArg()

        while(isNext(KatajaTokenTypes.OPERATOR)){
            assert(KatajaTokenTypes.OPERATOR)
            if(builder.tokenText.equals("->")) return
            parseCalcArg()
        }
    }

    private fun parseCalcArg(){
        if(isNext(KatajaTokenTypes.SPECIAL)){
            assert("(")
            parseCalc()
            assert(")")
        }else{
            if(isNext(KatajaTokenTypes.OPERATOR)) assert(KatajaTokenTypes.OPERATOR)
            next()
            when(builder.tokenType){
                KatajaTokenTypes.NUMBER, KatajaTokenTypes.CHAR, KatajaTokenTypes.STRING -> {}
                KatajaTokenTypes.KEYWORD -> {
                    if(!setOf("true", "false", "null").contains(builder.tokenText)) builder.error("Illegal argument")
                }
                KatajaTokenTypes.IDENTIFIER -> parseCall()
                else -> builder.error("Illegal argument")
            }
        }
    }

    private fun parseCall(){
        while(isNext(KatajaTokenTypes.SPECIAL)){
            assert(KatajaTokenTypes.SPECIAL)
            if(builder.tokenText.equals("(")){
                if(isNext(KatajaTokenTypes.SPECIAL)) assert(")")
                else{
                    parseCalc()
                    while(isNext(KatajaTokenTypes.SINGLE)){
                        assert(",")
                        parseCalc()
                    }
                    assert(")")
                    if(isNext(KatajaTokenTypes.SPECIAL)) assert(KatajaTokenTypes.SPECIAL)
                }
            }
            while(builder.tokenText.equals("[")) {
                parseCalc()
                assert("]")
                if(isNext(KatajaTokenTypes.SPECIAL)) assert(KatajaTokenTypes.SPECIAL)
            }

            if(builder.tokenText.equals(".")){
                assert(KatajaTokenTypes.IDENTIFIER)
            }else if(!builder.tokenText.equals(")") && !builder.tokenText.equals("]")) builder.error("Illegal argument")
        }
    }

    private fun parseDataType(){
        if(isNext(KatajaTokenTypes.KEYWORD)){
            assert(KatajaTokenTypes.KEYWORD)
            if (!setOf("int", "short", "long", "double", "float", "boolean", "char", "byte").contains(builder.tokenText)) builder.error("Expected type")
        }else assert(KatajaTokenTypes.IDENTIFIER)

        while(isNext(KatajaTokenTypes.SPECIAL)){
            assert("[")
            assert("]")
        }
    }

    private fun parseContent(){
        assert("{")
        while(!isNext(KatajaTokenTypes.SPECIAL)){
            if(isNext(KatajaTokenTypes.NEW_LINE)) next()
            else{
                if(!hasNext()){
                    builder.error("Expected }")
                    return
                }
                parseStatement()
            }
        }
        assert("}")
    }

    private fun assertEnd(){
        if(!hasNext()) return

        builder.advanceLexer()
        if(builder.tokenType != KatajaTokenTypes.NEW_LINE /*&& !builder.tokenText.equals(";")*/ && !builder.eof()){
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
        skipWhitespace()
        return builder.lookAhead(1) == type
    }

    private fun next(){
        skipWhitespace()
        builder.advanceLexer()
        if(builder.eof()) builder.error("Expected next")
    }

    private fun hasNext(): Boolean{
        skipWhitespace()
        return !builder.eof() && builder.lookAhead(1) != KatajaTokenTypes.NEW_LINE && builder.lookAhead(1) != null
    }

    private fun skipWhitespace(){
        while(!builder.eof() && (builder.lookAhead(1) == KatajaTokenTypes.WHITESPACE || builder.lookAhead(1) == KatajaTokenTypes.COMMENT)){
            builder.advanceLexer()
        }
    }
}