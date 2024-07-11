package com.github.ktj.katajaintellijplugin;

import com.intellij.psi.tree.IElementType;

public interface KatajaElementTypes {
    IElementType USE = new KatajaElementType("use");
    IElementType DATA = new KatajaElementType("date");
    IElementType TYPE = new KatajaElementType("type");
    IElementType CLASS = new KatajaElementType("class");
    IElementType INTERFACE = new KatajaElementType("interface");
}
