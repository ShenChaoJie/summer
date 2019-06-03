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
package org.summerframework.core.Component.scan;

import com.google.common.collect.Sets;
import org.summerframework.commons.support.logging.Logger;
import org.summerframework.commons.support.logging.LoggerFactory;
import org.summerframework.commons.util.Assert;
import org.summerframework.commons.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

/**
 * 扫描组件，并返回符合要求的集合.
 *
 * @author CJSHEN
 * @since 1.0
 */
public class ClassScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassScanner.class);

    private static Set<Class<?>> classes;


    public static Set<Class<?>> filter(final Class<? extends Annotation> annotationClasss){
        if (classes == null) {
            return Collections.emptySet();
        }
        if (classes.size() > 0) {
            final Set<Class<?>> annClasses = Sets.newLinkedHashSet();
            classes.stream().filter(clz -> clz.isAnnotationPresent(annotationClasss)).forEach(clz -> annClasses.add(clz));
        }
        return Collections.emptySet();
    }



    public static void scan(String packageName){
        if (StringUtils.isEmpty(packageName)) {
            LOGGER.warn("没有设置packageName, 跳过扫描");
            return;
        }

        if (classes == null) {
            classes = Sets.newHashSet();
        }

        classes.addAll(getClasses(packageName));
    }


    private static Set<Class<?>> getClasses(String packageName){
        return getClasses(new ResolverUtil.IsA(Object.class), packageName);
    }


    private static Set<Class<?>> getClasses(ResolverUtil.Test test, String packageName){
        Assert.notNull(test, "Parameter 'test' must not be null");
        Assert.notNull(packageName, "Parameter 'packageName' must not be null");

        return new ResolverUtil<Object>().find(test, packageName).getClasses();
    }


}
