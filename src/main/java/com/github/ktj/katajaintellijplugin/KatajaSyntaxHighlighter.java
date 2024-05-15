package com.github.ktj.katajaintellijplugin;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class KatajaSyntaxHighlighter extends SyntaxHighlighterBase {

    private static final TextAttributesKey[] Identifier = new TextAttributesKey[]{createTextAttributesKey("Kataja_Identifier", DefaultLanguageHighlighterColors.IDENTIFIER)};
    private static final TextAttributesKey[] Keyword = new TextAttributesKey[]{createTextAttributesKey("Kataja_Keyword", DefaultLanguageHighlighterColors.KEYWORD)};
    private static final TextAttributesKey[] Number = new TextAttributesKey[]{createTextAttributesKey("Kataja_Number", DefaultLanguageHighlighterColors.NUMBER)};
    private static final TextAttributesKey[] Comment = new TextAttributesKey[]{createTextAttributesKey("Kataja_Comment", DefaultLanguageHighlighterColors.LINE_COMMENT)};
    private static final TextAttributesKey[] Semicolon = new TextAttributesKey[]{createTextAttributesKey("Kataja_Semicolon", DefaultLanguageHighlighterColors.SEMICOLON)};
    private static final TextAttributesKey[] String = new TextAttributesKey[]{createTextAttributesKey("Kataja_String", DefaultLanguageHighlighterColors.STRING)};
    private static final TextAttributesKey[] Operator = new TextAttributesKey[]{createTextAttributesKey("Kataja_Operator", DefaultLanguageHighlighterColors.OPERATION_SIGN)};
    private static final TextAttributesKey[] BAD_CHARACTER = new TextAttributesKey[]{createTextAttributesKey("Kataja_BAD_CHARACTER", DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE)};

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new KatajaLexer();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType type) {
        if(type != null) {
            if(type.equals(KatajaTokenSet.IDENTIFIER) || type.equals(KatajaTokenSet.SPECIAL)) return Identifier;
            if(type.equals(KatajaTokenSet.KEYWORD)) return Keyword;
            if(type.equals(KatajaTokenSet.NUMBER)) return Number;
            if(type.equals(KatajaTokenSet.COMMENT)) return Comment;
            if(type.equals(KatajaTokenSet.END_OF_STATEMENT)) return Semicolon;
            if(type.equals(KatajaTokenSet.STRING) || type.equals(KatajaTokenSet.CHAR)) return String;
            if(type.equals(KatajaTokenSet.OPERATOR)) return Operator;
            if(type.equals(KatajaTokenSet.BAD_CHARACTER)) return BAD_CHARACTER;
        }
        return new TextAttributesKey[0];
    }
}
