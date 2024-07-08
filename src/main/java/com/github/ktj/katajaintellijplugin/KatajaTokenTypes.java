package com.github.ktj.katajaintellijplugin;

import com.intellij.psi.tree.IElementType;

public interface KatajaTokenTypes {

    IElementType IDENTIFIER       = new KatajaTokenType("Identifier");
    IElementType KEYWORD          = new KatajaTokenType("Keyword");
    IElementType SPECIAL          = new KatajaTokenType("Special");
    IElementType OPERATOR         = new KatajaTokenType("Operator");
    IElementType NUMBER           = new KatajaTokenType("Number");
    IElementType COMMENT          = new KatajaTokenType("Comment");
    IElementType WHITESPACE       = new KatajaTokenType("Whitespace");
    IElementType NEW_LINE         = new KatajaTokenType("New Line");
    IElementType SINGLE           = new KatajaTokenType("SINGLE");
    IElementType STRING           = new KatajaTokenType("String");
    IElementType CHAR             = new KatajaTokenType("Char");
    IElementType BAD_CHARACTER    = new KatajaTokenType("Bad Character");
}
