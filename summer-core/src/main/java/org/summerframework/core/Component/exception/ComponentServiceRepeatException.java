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
package org.summerframework.core.Component.exception;

/**
 * 重复组件服务异常.
 *
 * @author CJSHEN
 * @since 1.0
 */
public class ComponentServiceRepeatException extends RuntimeException {
    private static final long serialVersionUID = -1767535947039306656L;

    public ComponentServiceRepeatException(){

    }

    public ComponentServiceRepeatException(String message){
        super(message);
    }


    public ComponentServiceRepeatException(String message, Throwable cause){
        super(message, cause);

    }


}
