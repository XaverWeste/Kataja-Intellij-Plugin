package com.github.ktj.katajaintellijplugin

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class KatajaColorSettingsPage: ColorSettingsPage {

    private val descriptors: Array<AttributesDescriptor> = arrayOf(
            AttributesDescriptor("Identifier", KatajaSyntaxHighlighter.identifier),
            AttributesDescriptor("Number", KatajaSyntaxHighlighter.number),
            AttributesDescriptor("String", KatajaSyntaxHighlighter.string),
            AttributesDescriptor("Comment", KatajaSyntaxHighlighter.comment),
            AttributesDescriptor("Keyword", KatajaSyntaxHighlighter.keyword),
            AttributesDescriptor("Bad_character", KatajaSyntaxHighlighter.bad_character),
            AttributesDescriptor("Operator", KatajaSyntaxHighlighter.operator),
            AttributesDescriptor("Semicolon", KatajaSyntaxHighlighter.semicolon),
            AttributesDescriptor("CHAR", KatajaSyntaxHighlighter.character),
            AttributesDescriptor("SPECIAL", KatajaSyntaxHighlighter.special)
    )

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = descriptors

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = "Kataja"

    override fun getIcon(): Icon? = KatajaIcons.FILE

    override fun getHighlighter(): SyntaxHighlighter = KatajaSyntaxHighlighter()

    override fun getDemoText(): String = "class Test{\n\t#Comment\n\tString +(int i){\n\t\tfloat f = 2.124f; char c = 'a' + 'b\n\t\treturn \"string\"\n\t}\n}"

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? = null
}