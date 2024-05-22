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
    override fun createLexer(project: Project?): Lexer = KatajaLexer()

    override fun createParser(project: Project?): PsiParser = KatajaPsiParser()

    override fun getFileNodeType(): IFileElementType {
        TODO("Not yet implemented")
    }

    override fun getCommentTokens(): TokenSet {
        TODO("Not yet implemented")
    }

    override fun getStringLiteralElements(): TokenSet {
        TODO("Not yet implemented")
    }

    override fun createElement(astNode: ASTNode?): PsiElement {
        TODO("Not yet implemented")
    }

    override fun createFile(fileViewProvider: FileViewProvider): PsiFile {
        TODO("Not yet implemented")
    }
}