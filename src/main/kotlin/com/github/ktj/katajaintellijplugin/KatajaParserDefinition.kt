package com.github.ktj.katajaintellijplugin

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

class KatajaParserDefinition: ParserDefinition {

    private val file: IFileElementType = IFileElementType(KatajaLanguage.INSTANCE)

    override fun createLexer(project: Project?): Lexer = KatajaLexer()

    override fun createParser(project: Project?): PsiParser = KatajaPsiParser()

    override fun getFileNodeType(): IFileElementType = file

    override fun getCommentTokens(): TokenSet = KatajaTokenSet.COMMENT

    override fun getStringLiteralElements(): TokenSet = KatajaTokenSet.STRING

    override fun createElement(astNode: ASTNode?): PsiElement{
        TODO()
    }

    override fun createFile(fileViewProvider: FileViewProvider): PsiFile = KatajaFile(fileViewProvider)
}