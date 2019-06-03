/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.summerframework.core.Component.stereotype.bind;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.summerframework.commons.entity.BaseEntity;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 组件映射类, 存储实例化对象、对象类、方法.
 *
 * @author CJSHEN
 * @since 1.0
 */
public class RequestMapper extends BaseEntity{

    private static final long serialVersionUID = 3957179891123456818L;

    private Object instance;
    private Class<?> cls;
    private Method method;

    private RequestMethod[] requestMethods = new RequestMethod[] { RequestMethod.GET, RequestMethod.POST};
    private Map<String, String> param;

    private RequestMapper(){

    }

    public static RequestMapper create(){
        return new RequestMapper();
    }

    public Object getInstance(){
        return instance;
    }

    public RequestMapper setInstance(final Object instance){
        this.instance = instance;
        return this;
    }

    public Class<?> getCls(){
        return cls;
    }

    public RequestMapper setCls(final Class<?> cls){
        this.cls = cls;
        return this;
    }

    public Method getMethod(){
        return method;
    }

    public RequestMapper setMethod(final Method method){
        this.method = method;
        return this;
    }

    public RequestMethod[] getRequestMethods(){
        return requestMethods;
    }

    public RequestMapper setRequestMethods(final RequestMethod[] requestMethods){
        if(ArrayUtils.isNotEmpty(requestMethods)){
            this.requestMethods = requestMethods;
        }
        return this;
    }


    public String[] getRequestMethodStrs(){
        if(ArrayUtils.isNotEmpty(requestMethods)){
            String[] strings = new String[requestMethods.length];
            int idx = 0;
            for (RequestMethod mtd : requestMethods){
                strings[idx] = mtd.name();
                idx++;
            }

            return strings;
        }

        return ArrayUtils.EMPTY_STRING_ARRAY;
    }


    public Map<String, String> getParam(){
        return param;
    }

    public RequestMapper setParam(final Map<String, String> param){
        this.param = param;
        return this;
    }


}
