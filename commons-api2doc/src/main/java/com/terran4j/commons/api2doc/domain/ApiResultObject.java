package com.terran4j.commons.api2doc.domain;

import com.terran4j.commons.api2doc.annotations.Api2Doc;
import com.terran4j.commons.api2doc.annotations.ApiComment;
import com.terran4j.commons.api2doc.impl.Api2DocUtils;
import com.terran4j.commons.api2doc.impl.ApiCommentUtils;
import com.terran4j.commons.api2doc.other.utils.Arrays;
import com.terran4j.commons.api2doc.other.utils.Classes;
import com.terran4j.commons.api2doc.other.utils.KeyedList;
import org.apache.commons.beanutils.PropertyUtils;
import org.mockito.internal.util.collections.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.*;

/**
 * 记录所有的结果字段，它是一个复合型
 */
public class ApiResultObject extends ApiObject {

    private static final Logger log = LoggerFactory.getLogger(ApiResultObject.class);

    private ApiDataType dataType;

    /**
     * 如果类型是数组类型，此类是里面元素的类型
     * 否则是这个类型本身。
     */
    private Class<?> sourceType;

    private String typeName = "";

    private String refGroupId = null;

    private String groupId = null;

    private String groupName = null;

    private final List<ApiResultObject> children = new ArrayList<>();

    public Class<?> getSourceType() {
        return sourceType;
    }

    public void setSourceType(Class<?> sourceType) {
        this.sourceType = sourceType;
    }

    public String getRefGroupId() {
        return refGroupId;
    }

    public void setRefGroupId(String refGroupId) {
        this.refGroupId = refGroupId;
    }

    public final ApiDataType getDataType() {
        return dataType;
    }

    public final void setDataType(ApiDataType dataType) {
        this.dataType = dataType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public final List<ApiResultObject> getChildren() {
        return children;
    }

    public final ApiResultObject getChild(String fieldName) {
        if (children == null || StringUtils.isEmpty(fieldName)) {
            return null;
        }

        for (ApiResultObject child : children) {
            if (fieldName.equals(child.getId())) {
                return child;
            }
        }

        return null;
    }

    public final void addChild(ApiResultObject child) {
        this.children.add(child);
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final String getEnumComment(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        if (!clazz.isEnum()) {
            return null;
        }

        StringBuffer sb = new StringBuffer("\n可选值为：");
        Class<Enum<?>> enumClass = (Class<Enum<?>>) clazz;
        Enum[] enums = enumClass.getEnumConstants();
        for (Enum e : enums) {
            String name = e.name();
            Field field = null;
            try {
                field = enumClass.getDeclaredField(name);
            } catch (NoSuchFieldException | SecurityException e1) {
                log.error("Can't get field \"" + name + "\" from Enum: " + clazz.getName(), e1);
                continue;
            }
            ApiComment comment = field.getAnnotation(ApiComment.class);
            String value = ApiCommentUtils.getComment(
                    comment, null, field.getName());
            if (value == null) {
                value = "";
            }

            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(name).append(": ").append(value).append("; ");
        }

        return sb.toString();
    }

    private static final String getTypeName(Class<?> clazz, ApiDataType dataType) {
        if (clazz.isEnum()) {
            return ApiDataType.STRING.name().toLowerCase() + "(枚举值)";
        } else if (dataType != null && dataType.isSimpleType()) {
            return dataType.name().toLowerCase();
        } else {
            return clazz.getSimpleName();
        }
    }

    /**
     * 找到一个方法返回类型中字段，收集它的 Api2Doc 信息。
     *
     * @param method
     * @param totalResults
     *
     * @return
     */
    public static final ApiResultObject parseResultType(
            Method method, KeyedList<String, ApiResultObject> totalResults) {

        if (method == null) {
            return null;
        }

        if (totalResults == null) {
            totalResults = new KeyedList<>();
        }

        final Class<?> clazz = method.getReturnType();
        final ApiDataType dataType = ApiDataType.toDataType(clazz);
        if (dataType == null) {
            return null;
        }
        String typeName = getTypeName(clazz, dataType);

        // 基本类型，直接处理。
        if (dataType.isSimpleType()) {
            return createSimple(clazz, clazz, dataType, typeName);
        }

        // 子类型。
        Class<?> elementType = null;

        // 数组类型，找到它的元素的具体类型，然后处理具体类型。
        if (dataType.isArrayType()) {
            elementType = Api2DocUtils.getArrayElementClass(method);
            if (elementType == null) {
                log.warn("Can't find element class by method: {}", method);
                return null;
            }

            ApiDataType elementDataType = ApiDataType.toDataType(elementType);
            typeName = getTypeName(elementType, elementDataType) + "[]";

            // 数组类型，但元素是基本类型的，也直接处理。
            if (elementDataType != null && elementDataType.isSimpleType()) {
                return createSimple(elementType, clazz,
                        dataType, typeName);
            }
        }

        // 复杂类型的情况。
        ApiResultObject result = new ApiResultObject();
        result.setDataType(dataType);
        result.setSourceType(clazz);
        result.setTypeName(typeName);
        result.setId("");

        if (dataType.isObjectType()) {
            elementType = method.getReturnType();
        }

        // 没有子类型，直接返回。
        // TODO:  暂时不解析 Map 内部的类型。
        if (elementType == null || Map.class.equals(elementType)) {
            return result;
        }

        result.setSourceType(elementType);

        // 没有子类型，直接返回。
        PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(elementType);

        if (props == null || props.length == 0) {
            return result;
        }
        //判断是否有泛型
        Type genericReturnType = method.getGenericReturnType();
        Map<Class<?>, PropertyDescriptor[]> propMap = new HashMap<>();
        propMap.put(elementType, props);
        //解析泛型
        parseParameterizedType(genericReturnType,propMap);
        ApiComment annotation = method.getAnnotation(ApiComment.class);
        if (annotation != null) {
            for (Class<?> resultClass : annotation.resultClass()) {
                propMap.put(resultClass, PropertyUtils.getPropertyDescriptors(resultClass));
            }
        }


        // 根据类型生成字段集的 id 和 name 。
        String groupId = getGroupId(elementType);
        result.setGroupId(groupId);
        String groupName = elementType.getSimpleName();
        result.setGroupName(groupName);

        // 加入到结果字段集中。
        if (totalResults.containsKey(groupId)) {
            return result;
        } else {
            totalResults.add(groupId, result);
        }
        for (Map.Entry<Class<?>, PropertyDescriptor[]> propsEntry : propMap.entrySet()) {

            // 有子类型，补充子类型信息。
            for (PropertyDescriptor prop : propsEntry.getValue()) {
                if (Api2DocUtils.isFilter(prop, propsEntry.getKey())) {
                    continue;
                }

                String fieldName = prop.getName();
                Method subMethod = prop.getReadMethod();
                Type genericReturnType1 = subMethod.getGenericReturnType();

                if (genericReturnType1 instanceof ParameterizedType) {
                    System.out.println(((ParameterizedType) genericReturnType1).getActualTypeArguments());
                }
                // 处理子类型。
                ApiResultObject childPropResult;
                try {
                    childPropResult = parseResultType(subMethod, totalResults);
                } catch (Exception e) {
                    String msg = String.format("解析类[ %s ]的属性[ %s ]出错： %s",
                            propsEntry.getKey().getName(), fieldName, e.getMessage());
                    throw new RuntimeException(msg);
                }

                // 补充子类型信息。
                if (childPropResult != null) {

                    // 补充到当前节点中。
                    result.addChild(childPropResult);

                    String id = prop.getName();
                    childPropResult.setId(id);
                    childPropResult.setName(id);

                    Class<?> childPropClass = subMethod.getReturnType();
                    ApiDataType childPropDataType = ApiDataType.toDataType(childPropClass);
                    childPropResult.setDataType(childPropDataType);

                    Api2Doc childApi2Doc;
                    ApiComment childApiComment;
                    String childName;
                    Field field = Classes.getField(id, propsEntry.getKey());
                    if (field != null) {
                        childApiComment = field.getAnnotation(ApiComment.class);
                        childApi2Doc = field.getAnnotation(Api2Doc.class);
                        childName = field.getName();
                    } else {
                        childApiComment = subMethod.getAnnotation(ApiComment.class);
                        childApi2Doc = subMethod.getAnnotation(Api2Doc.class);
                        childName = subMethod.getName();
                    }

                    ApiComment elementApiComment = propsEntry.getKey()
                            .getAnnotation(ApiComment.class);
                    Class<?> defaultSeeClass = ApiCommentUtils
                            .getDefaultSeeClass(elementApiComment, null);

                    String comment = ApiCommentUtils.getComment(
                            childApiComment, defaultSeeClass, childName);
                    if (comment == null) {
                        comment = "";
                    }
                    childPropResult.insertComment(comment);

                    String sample = ApiCommentUtils.getSample(
                            childApiComment, defaultSeeClass, childName);
                    if (sample == null) {
                        sample = "";
                    }
                    childPropResult.setSample(sample);

                    if (childApi2Doc != null) {
                        childPropResult.setOrder(childApi2Doc.order());
                    }

                    // 记录所引用的类型。
                    Class<?> childSubType = null;
                    if (childPropDataType != null) {
                        if (childPropDataType.isArrayType()) {
                            childSubType = Api2DocUtils.getArrayElementClass(subMethod);
                        } else if (childPropDataType.isObjectType()) {
                            childSubType = subMethod.getReturnType();
                        }
                    }
                    if (childSubType != null) {
                        String refGroupId = getGroupId(childSubType);
                        childPropResult.setRefGroupId(refGroupId);
                    }
                }
            }
        }
        Collections.sort(result.getChildren());

        return result;
    }

    /**
     * @param type :
     *
     * @Author : GXL
     * @Desc : 解析泛型
     * @Date : 2019/5/2
     * @Return : void
     */
    public static void parseParameterizedType(Type type,Map<Class<?>, PropertyDescriptor[]> propMap) {

        if (type instanceof ParameterizedType) {
            ParameterizedType type1 = (ParameterizedType) type;
            //存储实际类型
            Class<?> rawType = (Class<?>) type1.getRawType();
            if (rawType != null&&!rawType.isAssignableFrom(List.class)&&!rawType.isAssignableFrom(Map.class)) {
                PropertyDescriptor[] temp1 = PropertyUtils.getPropertyDescriptors(rawType);
                if (temp1 != null && temp1.length != 0) {
                    //存储
                    propMap.put((Class<?>)type1.getRawType(), temp1);
                }
            }

            //继续解析泛型
            Type[] actualTypeArguments = type1.getActualTypeArguments();
            for (Type type2 : actualTypeArguments) {
                parseParameterizedType(type2, propMap);
            }
        }else if(type instanceof GenericArrayType){
            //泛型数组,获取泛型继续解析
           parseParameterizedType(((GenericArrayType) type).getGenericComponentType(),propMap);
        } else if (type instanceof Class) {
            ApiDataType dataType = ApiDataType.toDataType((Class<?>)type);
            if (!dataType.isSimpleType()) {
                propMap.put((Class<?>)type, PropertyUtils.getPropertyDescriptors((Class<?>)type));
            }

        }
    }

    public static final String getGroupId(Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException();
        }
        String groupId = ApiDocUtils.getId(clazz);
        return groupId;
    }

    private static ApiResultObject createSimple(Class<?> sourceType,
                                                Class<?> clazz, ApiDataType dataType,
                                                String typeName) {
        ApiResultObject result = new ApiResultObject();
        result.setSourceType(sourceType);
        result.setDataType(dataType);
        result.setTypeName(typeName);
        result.insertComment(getEnumComment(clazz));
        result.setId("");
        return result;
    }


}
