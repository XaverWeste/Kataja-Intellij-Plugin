package com.github.ktj.katajaintellijplugin

import com.github.ktj.katajaintellijplugin.psi.PsiFactory
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class KatajaParser : ParserDefinition {
    private val FILE: IFileElementType = IFileElementType(KatajaLanguage.INSTANCE)

    override fun createLexer(p0: Project?): Lexer = KatajaLexer()

    override fun createParser(p0: Project?): PsiParser = KatajaPsiParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = KatajaTokenSet.COMMENT

    override fun getStringLiteralElements(): TokenSet = KatajaTokenSet.STRING

    override fun createElement(p0: ASTNode?): PsiElement = PsiFactory.createElement(p0)

    override fun createFile(p0: FileViewProvider): PsiFile = KatajaFile(p0)
}