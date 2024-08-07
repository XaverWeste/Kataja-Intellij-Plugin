package com.github.ktj.katajaintellijplugin

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType

class KatajaAnnotator: Annotator {

    private lateinit var uses: java.util.HashMap<String, String>
    private lateinit var holder: AnnotationHolder

    override fun annotate(element: PsiElement, holder: AnnotationHolder){
        if(element.node.elementType == FILE){
            this.holder = holder
            annotateFile(element)
        }
    }

    private fun annotateFile(element: PsiElement){
        uses = hashMapOf("String" to "java.lang.String", "Object" to "java.lang.Object")

        for(e: PsiElement in filterChildren(element.children, KatajaElementTypes.TYPE)) validateName(e, "_TYPE")
        for(e: PsiElement in filterChildren(element.children, KatajaElementTypes.DATA)) validateName(e, "_DATA")
        for(e: PsiElement in filterChildren(element.children, KatajaElementTypes.INTERFACE)) validateName(e, "_INTERFACE")
        for(e: PsiElement in filterChildren(element.children, KatajaElementTypes.CLASS)) validateName(e, "_CLASS")
        for(e: PsiElement in filterChildren(element.children, KatajaElementTypes.USE)) annotateUse(e)

        for(e: PsiElement in filterChildren(element.children, KatajaElementTypes.TYPE)) annotateType(e)
        for(e: PsiElement in filterChildren(element.children, KatajaElementTypes.DATA)) annotateData(e)
        for(e: PsiElement in filterChildren(element.children, KatajaElementTypes.INTERFACE)) annotateInterface(e)
        for(e: PsiElement in filterChildren(element.children, KatajaElementTypes.CLASS)) annotateClass(e)
    }

    private fun annotateUse(element: PsiElement){
        val use = filterChildren(element.children, KatajaElementTypes.USES)
        val from = filterChildren(element.children, KatajaElementTypes.FROM)
        val alias = filterChildren(element.children, KatajaElementTypes.AS)

        val path = deleteWhiteSpace(from[0].text).replace("/", ".")

        if(use.isEmpty()){
            if(!classExist(path)) holder.newAnnotation(HighlightSeverity.ERROR, "Unable to find Class $path").range(from[0].lastChild).create()
            else if(uses.containsValue(path)) holder.newAnnotation(HighlightSeverity.WEAK_WARNING, "Class $path is already used").range(from[0]).create()

            if(alias.isNotEmpty()){
                if(uses.containsKey(alias[0].text.trim())) holder.newAnnotation(HighlightSeverity.ERROR, alias[0].text.trim()+" is already defined").range(alias[0]).create()
                else uses[alias[0].text.trim()] = path
            }else{
                val name = path.split(".")[path.split(".").size - 1]

                if(uses.containsKey(name)) holder.newAnnotation(HighlightSeverity.ERROR, "$name is already defined").range(from[0].lastChild).create()
                else uses[name] = name
            }
        }else{
            for(u in use){
                if(!classExist(path+"."+u.text.trim())) holder.newAnnotation(HighlightSeverity.ERROR, "Unable to find Class "+path+"."+u.text).range(u).create()
                else if(uses.containsValue(path+"."+u.text.trim())) holder.newAnnotation(HighlightSeverity.WEAK_WARNING, "Class "+path+"."+u.text+" is already used").range(u).create()

                if(alias.isEmpty()){
                    if(uses.containsKey(u.text.trim())) holder.newAnnotation(HighlightSeverity.ERROR, u.text.trim()+" is already defined").range(u).create()
                    else uses[u.text.trim()] = path+"."+u.text.trim()
                }
            }

            if(alias.isNotEmpty()){
                if(uses.containsKey(alias[0].text.trim())) holder.newAnnotation(HighlightSeverity.ERROR, alias[0].text.trim()+" is already defined").range(alias[0]).create()
                else uses[alias[0].text.trim()] = path+"."+use[0].text.trim()
            }
        }
    }

    private fun annotateType(element: PsiElement){
        val modifier = filterChildren(element.children, KatajaElementTypes.MODIFIER)
        if(modifier.size > 0){
            var const = false
            var final = false
            for(mod in modifier){
                when(mod.text.trim()){
                    "const" -> {
                        if(const) holder.newAnnotation(HighlightSeverity.ERROR, "Is already constant").range(mod).create()
                        else const = true
                    }
                    "final" -> {
                        if(final) holder.newAnnotation(HighlightSeverity.ERROR, "Is already final").range(mod).create()
                        else final = true
                    }
                    "abstract", "static", "synchronised" -> holder.newAnnotation(HighlightSeverity.ERROR, "Modifier is not allowed").range(mod).create()
                }
            }
        }

        val types: MutableList<String> = mutableListOf()
        for(e in filterChildren(element.children, KatajaElementTypes.TYPE_VALUE)){
            if(types.contains(e.text.trim())) holder.newAnnotation(HighlightSeverity.ERROR, e.text.trim()+" is already defined").range(e).create()
            else types.add(e.text.trim())
        }
    }

    private fun annotateData(element: PsiElement){
        val modifier = filterChildren(element.children, KatajaElementTypes.MODIFIER)
        if(modifier.size > 0){
            var const = false
            var final = false
            for(mod in modifier){
                when(mod.text.trim()){
                    "const" -> {
                        if(const) holder.newAnnotation(HighlightSeverity.ERROR, "Is already constant").range(mod).create()
                        else const = true
                    }
                    "final" -> {
                        if(final) holder.newAnnotation(HighlightSeverity.ERROR, "Is already final").range(mod).create()
                        else final = true
                    }
                    "abstract", "static", "synchronised" -> holder.newAnnotation(HighlightSeverity.ERROR, "Modifier is not allowed").range(mod).create()
                }
            }
        }

        val parameter: MutableList<String> = mutableListOf()
        for(e in filterChildren(element.children, KatajaElementTypes.PARAMETER)){
            if(!validateType(e.firstChild.text.trim())) holder.newAnnotation(HighlightSeverity.ERROR, "Unknown type").range(e.firstChild).create()
            if(parameter.contains(e.lastChild.text.trim())) holder.newAnnotation(HighlightSeverity.ERROR, e.text.trim()+" is already defined").range(e.lastChild).create()
            else parameter.add(e.lastChild.text.trim())
        }
    }

    private fun annotateInterface(element: PsiElement){

    }

    private fun annotateClass(element: PsiElement){
        var first = true
        val extends: MutableList<String> = mutableListOf()

        for(e in filterChildren(element.children, KatajaElementTypes.EXTENDS)){
            if(first){
                if(!validateType(e.text.trim())) holder.newAnnotation(HighlightSeverity.ERROR, "Unknown type").range(e).create()
                extends.add(e.text.trim())
                first = false
            }else{
                if(!uses.containsKey(e.text.trim())) holder.newAnnotation(HighlightSeverity.ERROR, "Unknown type").range(e).create()
                else if(extends.contains(e.text.trim())) holder.newAnnotation(HighlightSeverity.ERROR, "Class is already extended").range(e).create()
                else{
                    if(uses[e.text.trim()]!!.startsWith("_")){
                        if(!uses[e.text.trim()].equals("_INTERFACE")) holder.newAnnotation(HighlightSeverity.ERROR, "Expected Interface").range(e).create()
                    }else{
                        try{
                            if(!Class.forName(uses[e.text.trim()]).isInterface) holder.newAnnotation(HighlightSeverity.ERROR, "Expected Interface").range(e).create()
                        }catch(ignored: ClassNotFoundException){}
                    }
                    extends.add(e.text.trim())
                }
            }
        }
    }

    private fun validateType(type: String): Boolean{
        if(type.contains("[")) return validateType(type.split("[")[0])
        else{
            if(setOf("int", "short", "long", "double", "float", "char", "boolean", "byte").contains(type)) return true
            return uses.containsKey(type)
        }
    }

    private fun validateName(element: PsiElement, type: String){
        val name = filterChildren(element.children, KatajaElementTypes.NAME)

        if(uses.containsKey(name[0].text.trim())) holder.newAnnotation(HighlightSeverity.ERROR, "Class "+name[0].text.trim()+" is already defined").range(name[0]).create()
        else uses[name[0].text.trim()] = type
    }

    private fun classExist(path: String): Boolean{
        try{
            Class.forName(path)
            return true
        }catch(ignored: ClassNotFoundException){}
        return false
    }

    private fun filterChildren(arr: Array<PsiElement>, type: IElementType): MutableList<PsiElement>{
        val list = mutableListOf<PsiElement>()
        for(element: PsiElement in arr) if(element.node.elementType == type) list.add(element)
        return list
    }

    private fun deleteWhiteSpace(s: String): String{
        val sb = StringBuilder()

        for(c in s.toCharArray()) if(!setOf(' ', '\t').contains(c)) sb.append(c)

        return sb.toString()
    }
}