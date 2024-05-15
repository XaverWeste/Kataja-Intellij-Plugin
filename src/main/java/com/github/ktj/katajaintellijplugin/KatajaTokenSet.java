package com.github.ktj.katajaintellijplugin;

public interface KatajaTokenSet{

    KatajaTokenType IDENTIFIER = new KatajaTokenType("Identifier");
    KatajaTokenType KEYWORD = new KatajaTokenType("Keyword");
    KatajaTokenType SPECIAL = new KatajaTokenType("Special");
    KatajaTokenType OPERATOR = new KatajaTokenType("Operator");
    KatajaTokenType NUMBER = new KatajaTokenType("Number");
    KatajaTokenType COMMENT = new KatajaTokenType("Comment");
    KatajaTokenType WHITESPACE = new KatajaTokenType("Whitespace");
    KatajaTokenType NEW_LINE = new KatajaTokenType("New Line");
    KatajaTokenType END_OF_STATEMENT = new KatajaTokenType("End of Statement");
    KatajaTokenType STRING = new KatajaTokenType("End of Statement");
    KatajaTokenType CHAR = new KatajaTokenType("End of Statement");
    KatajaTokenType BAD_CHARACTER = new KatajaTokenType("End of Statement");
}
