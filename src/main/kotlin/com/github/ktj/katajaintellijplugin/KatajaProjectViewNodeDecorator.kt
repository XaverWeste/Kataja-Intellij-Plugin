package com.github.ktj.katajaintellijplugin

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ProjectViewNodeDecorator

class KatajaProjectViewNodeDecorator: ProjectViewNodeDecorator {

    override fun decorate(node: ProjectViewNode<*>?, data: PresentationData?) {
        if(node == null || data == null) return

        if(node.value is KatajaFile) data.presentableText = (node.value as KatajaFile).virtualFile.nameWithoutExtension
    }
}