package com.dinson.blingbase.utils



object JsoupUtils{

    /*//传入待解析的字符串与编写好的实体类
    fun <T> fromHTML(html: String, clazz: Class<T>): T? {
        var t: T? = null
        val pickClazz: Pick
        try {
            //先用Jsoup实例化待解析的字符串
            val rootDocument = Jsoup.parse(html)
            //获取实体类的的注解
            pickClazz = clazz.getAnnotation(Pick::class.java!!)
            //构建一个实体类的无参构造方法并生成实例
            t = clazz.getConstructor().newInstance()
            //获取注解的一些参数
            val clazzAttr = pickClazz.attr()
            val clazzValue = pickClazz.value()
            //用Jsoup选择到待解析的节点
            val rootNode = getRootNode(rootDocument, clazzValue)
            //获取实体类的所有成员变量
            val fields = clazz.declaredFields
            //遍历并解析这些成员变量
            for (field in fields) {
                dealFieldType(field, rootNode, t)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return t
    }

    @Throws(Exception::class)
    private fun dealFieldType(field: Field, rootNode: Element, t: Any): Field? {
        //设置成员变量为可修改的
        field.setAccessible(true)
        val pickField = field.getAnnotation(Pick::class.java) ?: return null
        val fieldValue = pickField.value()
        val fieldAttr = pickField.attr()
        //获取field的类型
        val type = field.getType()
        //目前此工具类只能解析两种类型的成员变量,一种是String的,另一种是带泛型参数的List,泛型参数必须是自定义
        //子实体类,或者String,自定义子实体类如果是内部类,必须用public static修饰
        if (type == String::class.java) {
            val nodeValue = getStringNode(rootNode, fieldAttr, fieldValue)
            val filterField = field.getAnnotation(Filter::class.java)
            if (filterField != null) {
                val filter = filterField!!.filter()
                val isFilter = filterField!!.isFilter()
                val isMatcher = RegexUtils.getRegexBoolean(nodeValue, filter)
                if (isFilter && isMatcher) {
                    field.set(t, nodeValue)
                } else {
                    return null
                }
            } else {
                field.set(t, nodeValue)
            }
        } else if (type == List<*>::class.java) {
            val elements = getListNode(rootNode, fieldValue)
            field.set(t, ArrayList())
            val fieldList = field.get(t) as List<Any>
            for (ele in elements) {
                val genericType = field.getGenericType()
                if (genericType is ParameterizedType) {
                    val args = (genericType as ParameterizedType).getActualTypeArguments()
                    val aClass = Class.forName((args[0] as Class<*>).name)
                    val `object` = aClass.newInstance()
                    val childFields = aClass.declaredFields
                    for (childField in childFields) {
                        dealFieldType(childField, ele, `object`)
                    }
                    fieldList.add(`object`)
                }
            }
            field.set(t, fieldList)
        }
        return field
    }

    *//**
     * 获取一个Elements对象
     *//*
    private fun getListNode(rootNode: Element, fieldValue: String): Elements {
        return rootNode.select(fieldValue)
    }

    *//**
     * 获取返回值为String的节点
     *
     * 由于Jsoup不支持JQuery的一些语法结构,例如  :first  :last,所以这里手动处理了下,自己可参考JQuery选择器
     * 扩展其功能
     *//*
    private fun getStringNode(rootNode: Element, fieldAttr: String, fieldValue: String): String {
        var fieldValue = fieldValue
        if (fieldValue.contains(":first")) {
            fieldValue = fieldValue.replace(":first", "")
            return if (Attrs.TEXT.equals(fieldAttr)) rootNode.select(fieldValue).first().text() else rootNode.select(fieldValue).first().attr(fieldAttr)
        } else if (fieldValue.contains(":last")) {
            fieldValue = fieldValue.replace(":last", "")
            return if (Attrs.TEXT.equals(fieldAttr)) rootNode.select(fieldValue).last().text() else rootNode.select(fieldValue).last().attr(fieldAttr)
        } else {
            return if (Attrs.TEXT.equals(fieldAttr)) rootNode.select(fieldValue).text() else rootNode.select(fieldValue).attr(fieldAttr)
        }
    }

    *//**
     * 获取根节点,通常在类的注解上使用
     *//*
    private fun getRootNode(rootDocument: Document, value: String): Element {
        return rootDocument.selectFirst(value)
    }*/


}