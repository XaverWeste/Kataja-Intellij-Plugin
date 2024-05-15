package com.github.ktj.katajaintellijplugin;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class KatajaLanguageFileType extends LanguageFileType {

    public static final KatajaLanguageFileType INSTANCE = new KatajaLanguageFileType();

    protected KatajaLanguageFileType() {
        super(KatajaLanguage.INSTANCE);
    }

    @Override
    public @NonNls @NotNull String getName() {
        return "Kataja File";
    }

    @Override
    public @NlsContexts.Label @NotNull String getDescription() {
        return "Kataja language file";
    }

    @Override
    public @NlsSafe @NotNull String getDefaultExtension() {
        return "ktj";
    }

    @Override
    public Icon getIcon() {
        Icon icon = KatajaIcons.FILE;
        return KatajaIcons.FILE;
    }
}
