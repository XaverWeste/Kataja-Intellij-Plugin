package com.github.ktj.katajaintellijplugin;

import com.intellij.psi.tree.TokenSet;

public interface KatajaTokenSet{

    TokenSet IDENTIFIER       = TokenSet.create(new KatajaTokenType("Identifier"));
    TokenSet KEYWORD          = TokenSet.create(new KatajaTokenType("Keyword"));
    TokenSet SPECIAL          = TokenSet.create(new KatajaTokenType("Special"));
    TokenSet OPERATOR         = TokenSet.create(new KatajaTokenType("Operator"));
    TokenSet NUMBER           = TokenSet.create(new KatajaTokenType("Number"));
    TokenSet COMMENT          = TokenSet.create(new KatajaTokenType("Comment"));
    TokenSet WHITESPACE       = TokenSet.create(new KatajaTokenType("Whitespace"));
    TokenSet NEW_LINE         = TokenSet.create(new KatajaTokenType("New Line"));
    TokenSet END_OF_STATEMENT = TokenSet.create(new KatajaTokenType("End of Statement"));
    TokenSet STRING           = TokenSet.create(new KatajaTokenType("String"));
    TokenSet CHAR             = TokenSet.create(new KatajaTokenType("Char"));
    TokenSet BAD_CHARACTER    = TokenSet.create(new KatajaTokenType("Bad Character"));
}
