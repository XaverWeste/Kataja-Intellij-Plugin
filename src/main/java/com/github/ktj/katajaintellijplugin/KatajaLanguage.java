package com.github.ktj.katajaintellijplugin;

import com.intellij.lang.Language;

public class KatajaLanguage extends Language {

    public static final KatajaLanguage INSTANCE = new KatajaLanguage();

    protected KatajaLanguage() {
        super("Kataja");
    }
}
