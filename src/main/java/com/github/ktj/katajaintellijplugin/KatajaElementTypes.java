package com.github.ktj.katajaintellijplugin;

import com.intellij.psi.tree.IElementType;

public interface KatajaElementTypes {
    IElementType USE = new KatajaElementType("use");

    IElementType USES = new KatajaElementType("uses");
    IElementType FROM = new KatajaElementType("from");
    IElementType AS = new KatajaElementType("as");


    IElementType DATA = new KatajaElementType("date");
    IElementType TYPE = new KatajaElementType("type");
    IElementType CLASS = new KatajaElementType("class");
    IElementType INTERFACE = new KatajaElementType("interface");

    IElementType NAME = new KatajaElementType("name");
    IElementType TYPE_VALUE = new KatajaElementType("type value");
    IElementType PARAMETER = new KatajaElementType("parameter");
    IElementType MODIFIER = new KatajaElementType("modifier");
    IElementType EXTENDS = new KatajaElementType("extends");
}
