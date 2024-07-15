package com.github.ktj.katajaintellijplugin.file

import com.github.ktj.katajaintellijplugin.language.KatajaLanguage
import com.github.ktj.katajaintellijplugin.language.KatajaLanguageFileType
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class KatajaFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, KatajaLanguage.INSTANCE) {
    override fun getFileType(): FileType = KatajaLanguageFileType.INSTANCE

    override fun toString(): String = "Kataja File"

}