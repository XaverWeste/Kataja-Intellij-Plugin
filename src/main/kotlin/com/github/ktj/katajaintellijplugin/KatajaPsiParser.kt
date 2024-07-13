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
            if(builder.tokenType != KatajaTokenTypes.NEW_LINE){
                when(builder.tokenText){
                    "use" -> parseUse()
                    "public", "private", "protected", "const", "final", "synchronised", "abstract", "static" -> parseMod(true)
                    "type" -> parseType(builder.mark())
                    "data" -> parseData(builder.mark())
                    "interface" -> parseInterface(builder.mark())
                    "class" -> parseClass(builder.mark())
                    null -> {
                        marker.done(type)
                        return builder.treeBuilt
                    }
                    else -> {
                        while(!builder.eof() && !builder.tokenText.equals("{") && builder.tokenType != KatajaTokenTypes.NEW_LINE) builder.advanceLexer()
                        if(builder.tokenText.equals("{")){
                            parseContent()
                            assertEnd()
                        }
                    }
                }
            }
            skipWhitespace()
            builder.advanceLexer()
        }while(true)
    }

    private fun parseUse(){
        val useMarker = builder.mark()
        var marker: PsiBuilder.Marker
        if(isNext(KatajaTokenTypes.SINGLE)){
            assert("$")
            marker = builder.mark()
            assert(KatajaTokenTypes.IDENTIFIER)
        }else{
            assert(KatajaTokenTypes.IDENTIFIER)
            marker = builder.mark()
        }

        if(isNext(KatajaTokenTypes.SINGLE)){
            while(isNext(KatajaTokenTypes.SINGLE)){
                assert(",")
                marker.done(KatajaElementTypes.USES)
                if(isNext(KatajaTokenTypes.SINGLE)){
                    assert("$")
                    marker = builder.mark()
                    assert(KatajaTokenTypes.IDENTIFIER)
                }else{
                    assert(KatajaTokenTypes.IDENTIFIER)
                    marker = builder.mark()
                }
            }

            assert("from")
            marker.done(KatajaElementTypes.USES)
            assert(KatajaTokenTypes.IDENTIFIER)
            marker = builder.mark()

            while(isNext(KatajaTokenTypes.OPERATOR)){
                assert("/")
                assert(KatajaTokenTypes.IDENTIFIER)
            }

            builder.advanceLexer()
            marker.done(KatajaElementTypes.FROM)
        }else{
            if(isNext(KatajaTokenTypes.KEYWORD)){
                assert("from")
                marker.done(KatajaElementTypes.USES)
                assert(KatajaTokenTypes.IDENTIFIER)
                marker = builder.mark()
            }

            while(isNext(KatajaTokenTypes.OPERATOR)){
                assert("/")
                assert(KatajaTokenTypes.IDENTIFIER)
            }

            if(isNext(KatajaTokenTypes.KEYWORD)){
                assert("as")
                marker.done(KatajaElementTypes.FROM)
                assert(KatajaTokenTypes.IDENTIFIER)
                marker = builder.mark()
                builder.advanceLexer()
                marker.done(KatajaElementTypes.AS)
            }else{
                builder.advanceLexer()
                marker.done(KatajaElementTypes.FROM)
            }
        }

        if(builder.tokenType != KatajaTokenTypes.NEW_LINE) assertEnd()
        useMarker.done(KatajaElementTypes.USE)
    }

    private fun parseMod(allowClass: Boolean){
        val marker = builder.mark()
        val mod = builder.mark()

        var acc = false
        if(setOf("public", "private", "protected").contains(builder.tokenText)) acc = true

        while(isNext(KatajaTokenTypes.KEYWORD)){
            assert(KatajaTokenTypes.KEYWORD)
            when(builder.tokenText){
                "const", "final", "synchronised", "abstract", "static" -> {}
                "type" -> {
                    mod.done(KatajaElementTypes.MODIFIER)
                    if(!allowClass) builder.error("Illegal statement")
                    parseType(marker)
                    return
                }
                "data" -> {
                    mod.done(KatajaElementTypes.MODIFIER)
                    if(!allowClass) builder.error("Illegal statement")
                    parseData(marker)
                    return
                }
                "interface" -> {
                    mod.done(KatajaElementTypes.MODIFIER)
                    if(!allowClass) builder.error("Illegal statement")
                    parseInterface(marker)
                    return
                }
                "class" -> {
                    mod.done(KatajaElementTypes.MODIFIER)
                    if(!allowClass) builder.error("Illegal statement")
                    parseClass(marker)
                    return
                }
                "public", "private", "protected" -> {
                    if(acc) builder.error("Expected modifier")
                    acc = true
                }
                "main" -> break
                else -> builder.error("Expected modifier")
            }
        }
        builder.advanceLexer()
        mod.done(KatajaElementTypes.MODIFIER)

        while(!builder.eof() && !builder.tokenText.equals("{") && builder.tokenType != KatajaTokenTypes.NEW_LINE) builder.advanceLexer()
        if(builder.tokenText.equals("{")){
            parseContent()
            assertEnd()
        }
        marker.done(KatajaElementTypes.NAME)
    }

    private fun parseType(classMarker: PsiBuilder.Marker){
        assert(KatajaTokenTypes.IDENTIFIER)
        var marker = builder.mark()
        assert("=")
        marker.done(KatajaElementTypes.NAME)
        assert(KatajaTokenTypes.IDENTIFIER)
        marker = builder.mark()
        while(isNext(KatajaTokenTypes.OPERATOR)){
            assert("|")
            marker.done(KatajaElementTypes.TYPE_VALUE)
            assert(KatajaTokenTypes.IDENTIFIER)
            marker = builder.mark()
        }
        assertEnd()
        marker.done(KatajaElementTypes.TYPE_VALUE)
        classMarker.done(KatajaElementTypes.TYPE)
    }

    private fun parseData(classMarker: PsiBuilder.Marker){
        assert(KatajaTokenTypes.IDENTIFIER)
        var marker = builder.mark()
        assert("=")
        marker.done(KatajaElementTypes.NAME)
        assert("(")
        if(!isNext(KatajaTokenTypes.SPECIAL)){
            do {
                marker = parseDataType()
                assert(KatajaTokenTypes.IDENTIFIER)

                if (isNext(KatajaTokenTypes.SINGLE)) {
                    assert(",")
                    marker.done(KatajaElementTypes.PARAMETER)
                }else{
                    builder.advanceLexer()
                    marker.done(KatajaElementTypes.PARAMETER)
                    break
                }
            } while (true)
        }
        if(!builder.tokenText.equals(")")) assert(")")
        assertEnd()
        classMarker.done(KatajaElementTypes.DATA)
    }

    private fun parseInterface(classMarker: PsiBuilder.Marker){
        assert(KatajaTokenTypes.IDENTIFIER)
        assert("{")
        parseContent()
        assertEnd()
        classMarker.done(KatajaElementTypes.NAME)
    }

    private fun parseClass(classMarker: PsiBuilder.Marker){
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
        parseContent()
        assertEnd()
        classMarker.done(KatajaElementTypes.NAME)
    }

    private fun parseDataType(): PsiBuilder.Marker{
        val marker: PsiBuilder.Marker
        if(isNext(KatajaTokenTypes.KEYWORD)){
            assert(KatajaTokenTypes.KEYWORD)
            marker = builder.mark()
            //if(builder.tokenText.equals("const")){
            //    if(isNext(KatajaTokenTypes.KEYWORD)) {
            //        assert(KatajaTokenTypes.KEYWORD)
            //        if (!setOf("void", "int", "short", "long", "double", "float", "boolean", "char", "byte").contains(builder.tokenText)) builder.error("Expected type")
            //    }else assert(KatajaTokenTypes.IDENTIFIER)
            /*}else*/ if (!setOf("void", "int", "short", "long", "double", "float", "boolean", "char", "byte").contains(builder.tokenText)) builder.error("Expected type")
        }else{
            assert(KatajaTokenTypes.IDENTIFIER)
            marker = builder.mark()
        }

        while(isNext(KatajaTokenTypes.SPECIAL)){
            assert("[")
            assert("]")
        }

        return marker
    }

    private fun parseContent(){
        var i = 1
        while(i > 0){
            if(builder.eof()){
                builder.error("Expected {")
                return
            }else if(isNext(KatajaTokenTypes.SPECIAL)){
                assert(KatajaTokenTypes.SPECIAL)
                if(builder.tokenText.equals("{")) i++
                else if(builder.tokenText.equals("}")) i--
            }else next()
        }
    }

    private fun toEndOfLine(){
        while(hasNext() && !isNext(KatajaTokenTypes.NEW_LINE)) next()
    }

    private fun assertEnd(){
        if(!hasNext()){
            builder.advanceLexer()
            return
        }

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