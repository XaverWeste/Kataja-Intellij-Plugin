package com.github.ktj.katajaintellijplugin;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class KatajaSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey identifier = createTextAttributesKey("Kataja_Identifier", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey keyword = createTextAttributesKey("Kataja_Keyword", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey number = createTextAttributesKey("Kataja_Number", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey comment = createTextAttributesKey("Kataja_Comment", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey semicolon = createTextAttributesKey("Kataja_Semicolon", DefaultLanguageHighlighterColors.SEMICOLON);
    public static final TextAttributesKey string = createTextAttributesKey("Kataja_String", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey operator = createTextAttributesKey("Kataja_Operator", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey bad_character = createTextAttributesKey("Kataja_BAD_CHARACTER", DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE);
    public static final TextAttributesKey character = createTextAttributesKey("Kataja_Char", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey special = createTextAttributesKey("Kataja_Special", DefaultLanguageHighlighterColors.IDENTIFIER);

    private static final TextAttributesKey[] Identifier = new TextAttributesKey[]{identifier};
    private static final TextAttributesKey[] Keyword = new TextAttributesKey[]{keyword};
    private static final TextAttributesKey[] Number = new TextAttributesKey[]{number};
    private static final TextAttributesKey[] Comment = new TextAttributesKey[]{comment};
    private static final TextAttributesKey[] Single = new TextAttributesKey[]{semicolon};
    private static final TextAttributesKey[] String = new TextAttributesKey[]{string};
    private static final TextAttributesKey[] Operator = new TextAttributesKey[]{operator};
    private static final TextAttributesKey[] BAD_CHARACTER = new TextAttributesKey[]{bad_character};
    private static final TextAttributesKey[] Char = new TextAttributesKey[]{character};
    private static final TextAttributesKey[] Special = new TextAttributesKey[]{special};

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new KatajaLexer();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType type) {
        if(type != null) {
            if(type.equals(KatajaTokenSet.IDENTIFIER.getTypes()[0])) return Identifier;
            if(type.equals(KatajaTokenSet.SPECIAL.getTypes()[0])) return Special;
            if(type.equals(KatajaTokenSet.KEYWORD.getTypes()[0])) return Keyword;
            if(type.equals(KatajaTokenSet.NUMBER.getTypes()[0])) return Number;
            if(type.equals(KatajaTokenSet.COMMENT.getTypes()[0])) return Comment;
            if(type.equals(KatajaTokenSet.SINGLE.getTypes()[0])) return Single;
            if(type.equals(KatajaTokenSet.STRING.getTypes()[0])) return String;
            if(type.equals(KatajaTokenSet.CHAR.getTypes()[0])) return Char;
            if(type.equals(KatajaTokenSet.OPERATOR.getTypes()[0])) return Operator;
            if(type.equals(KatajaTokenSet.BAD_CHARACTER.getTypes()[0])) return BAD_CHARACTER;
        }
        return new TextAttributesKey[0];
    }
}
