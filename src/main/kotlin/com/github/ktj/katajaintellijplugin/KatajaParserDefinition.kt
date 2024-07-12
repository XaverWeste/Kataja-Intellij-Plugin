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

class KatajaParserDefinition: ParserDefinition {

    override fun createLexer(project: Project?): Lexer = KatajaLexer()

    override fun createParser(project: Project?): PsiParser = KatajaPsiParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = TokenSet.create(KatajaTokenTypes.COMMENT)

    override fun getStringLiteralElements(): TokenSet = TokenSet.create(KatajaTokenTypes.STRING)

    override fun createElement(astNode: ASTNode?): PsiElement = PsiFactory.createElement(astNode)

    override fun createFile(fileViewProvider: FileViewProvider): PsiFile = KatajaFile(fileViewProvider)
}

val FILE: IFileElementType = IFileElementType(KatajaLanguage.INSTANCE)