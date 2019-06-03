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
package org.summerframework.core.Component;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.summerframework.commons.loader.LoaderException;
import org.summerframework.commons.loader.PropertiesLoader;
import org.summerframework.commons.support.logging.Logger;
import org.summerframework.commons.support.logging.LoggerFactory;
import org.summerframework.commons.util.CollectionUtils;
import org.summerframework.commons.util.StringUtils;
import org.summerframework.core.Component.scan.ClassScanner;
import org.summerframework.core.Component.stereotype.Component;
import org.summerframework.core.Component.stereotype.bind.RequestMapper;
import org.summerframework.core.Component.stereotype.bind.RequestMapping;
import org.summerframework.core.Component.stereotype.bind.RequestMethod;
import org.summerframework.core.Component.stereotype.bind.Routes;
import org.summerframework.core.context.ApplicationContext;
import org.summerframework.core.globals.Globals;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * 组件操作累.
 *
 * @author CJSHEN
 * @since 1.0
 */
public class Components {

    private static final Logger LOGGER = LoggerFactory.getLogger(Components.class);

    private static boolean isLoaded;

    private Components(){

    }

    /**
     * 加载组件服务, 并且装载至组件服务映射表中.
     * @throws LoaderException
     * @throws IOException
     */
    public static final void load() throws LoaderException, IOException{
        if (isLoaded) {
            throw new LoaderException("Component已经加载,这里不再进行重复加载,如需重新加载请调用reload方法!");
        }

        PropertiesLoader.PROPERTIES.values().stream()
                .filter(item -> StringUtils.isNotBlank(item.getProperty(ApplicationContext.COMPONENT_BASE_PACKAGE)))
                .forEach(item -> {
                    final String[] packageNames= item.getProperty(ApplicationContext.COMPONENT_BASE_PACKAGE).split(",");
                    Arrays.asList(packageNames).forEach(packageName -> ClassScanner.scan(packageName));
                });

        final Set<Class<?>> classes = ClassScanner.filter(Component.class);
        LOGGER.info("Component size: {}", classes.size());
        if (CollectionUtils.isEmpty(classes)) {
            final Injector injector = Globals.get(Injector.class);
            for(final Class<?> cls : classes){
                LOGGER.info("Inject Component Class: {}", cls.getName());
                final Object instance = injector.getInstance(cls);
                final Method[] methods = cls.getMethods();
                final String mapping = cls.isAnnotationPresent(RequestMapping.class) ? cls.getAnnotation(RequestMapping.class).value() : "";
                final Map<String, Map<RequestMethod, RequestMapper>> mappers = Routes.route().matchers(instance, methods, RequestMapping.class, mapping);

                //mappers.forEach((url, mapper) -> Routes.route().register(url, mapper));
                //写到这里  register 方法


            }


        }

    }



}
