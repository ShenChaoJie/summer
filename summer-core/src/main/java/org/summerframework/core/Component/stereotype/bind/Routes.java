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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.summerframework.commons.entity.BaseEntity;
import org.summerframework.commons.support.logging.Logger;
import org.summerframework.commons.support.logging.LoggerFactory;
import org.summerframework.commons.util.AntPathMatcher;
import org.summerframework.commons.util.Assert;
import org.summerframework.commons.util.CollectionUtils;
import org.summerframework.commons.util.PathMatcher;
import org.summerframework.core.Component.exception.ComponentServiceRepeatException;
import sun.misc.Request;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * 路由.
 *
 * @author CJSHEN
 * @since 1.0
 */
public class Routes {

    private static final Logger LOGGER = LoggerFactory.getLogger(Routes.class);

    private static final Routes INSTANCE = new Routes();

    private final Map<String, Map<RequestMethod, RequestMapper>> mappers = Maps.newLinkedHashMap();

    private final PathMatcher pathMatcher = new AntPathMatcher();

    private final UrlPathHelper urlPathHelper = new UrlPathHelper();

    private Routes(){

    }

    public static Routes route(){
        return INSTANCE;
    }


    public Map<String, Map<RequestMethod, RequestMapper>> matchers(final Object instance, final Method[] methods,
              final Class<? extends RequestMapping> annotated, final String url){
        if (ArrayUtils.isEmpty(methods)){
            return Collections.emptyMap();
        }

        final Map<String, Map<RequestMethod, RequestMapper>> routes = Maps.newHashMap();
        Arrays.asList(methods).stream().filter(method -> filterMethod(method, annotated)).map(method -> routeDefine(instance, method, annotated, url))
                .forEach(routeRes -> routeDefine0(routeRes, routes));

        return routes;
    }

    protected boolean filterMethod (final Method method, final Class<? extends RequestMapping> annotated){
        if (method.isAnnotationPresent(annotated)){
            final RequestMapping mapping = method.getAnnotation(annotated);
            if (mapping != null && StringUtils.isNotBlank(mapping.value())){
                return true;
            }
        }
        return false;
    }


    protected Route routeDefine(final Object instance, final Method method,
              final  Class<? extends RequestMapping> annotated, final String url) {
        final RequestMapping mapping =  method.getAnnotation(annotated);
        final RequestMapper mapper = RequestMapper.create().setInstance(instance).setCls(instance.getClass()).setMethod(method)
                .setRequestMethods(mapping.method());
        final Map<RequestMethod, RequestMapper> mappers = Maps.newHashMap();
        final RequestMethod[] requestMethods = mapper.getRequestMethods();
        for (final RequestMethod requestMethod : requestMethods){
            mappers.put(requestMethod, mapper);
        }

        final String route = (url + mapping.value());
        final String newRoute = execRoutePath(route);
        LOGGER.debug("Route define : {}.{}:{} {}", instance.getClass(), method.getName(), newRoute, Lists.newArrayList(requestMethods));
        return new Route(newRoute, mappers);
    }


    protected void routeDefine0(final Route route, final Map<String, Map<RequestMethod, RequestMapper>> routes){

        final  String routeUrl = route.getRoute();
        if (!CollectionUtils.isEmpty(route.getMappers()) && routes.containsKey(routeUrl)) {
            final Set<RequestMethod> before = route.getMappers().keySet();
            final Set<RequestMethod> after = routes.get(routeUrl).keySet();
            if (!isIntersectionRequestMethod(before, after)) {
                putRoute(route, routes);
            } else {
                throw new ComponentServiceRepeatException(routeUrl);
            }

        } else {
            putRoute(route, routes);
        }


    }

    private void putRoute(final Route route, final Map<String, Map<RequestMethod, RequestMapper>> routes){
        final String url = route.getRoute();
        final Map<RequestMethod, RequestMapper> mapper = route.getMappers();
        final Map<RequestMethod, RequestMapper> mappers = routes.get(url);
        if (mappers == null) {
            routes.put(url, mapper);
        } else {
            mappers.putAll(mapper);
            routes.put(url, mappers);
        }
    }


    private boolean isIntersectionRequestMethod(final Set<RequestMethod> before, final Set<RequestMethod> after) {
        Assert.notEmpty(before);
        Assert.notEmpty(after);
        for (final RequestMethod bf : before) {
            for (final RequestMethod af : after) {
                if (bf == af) {
                    return true;
                }
            }

        }
        return false;

    }



    protected String execRoutePath(final String route){
        final String[] rtks = route.split("/");
        final StringBuilder routeBuilder = new StringBuilder();

        for (final String rtk : rtks){
            if (StringUtils.isEmpty(rtk)){
                continue;
            }

            if (rtk.startsWith("{") && rtk.endsWith("}")) {
                routeBuilder.append('/');

                final int idx = rtk.indexOf(':');

                if (idx > 0) {
                    routeBuilder.append(StringUtils.lowerCase(rtk.substring(0, idx)));
                    routeBuilder.append(rtk.substring(idx));
                } else {
                    routeBuilder.append(rtk);
                }
            } else if ((rtk.startsWith("{") && !rtk.endsWith("}")) || (!rtk.startsWith("{") && rtk.endsWith("}"))){
                throw new IllegalArgumentException("Invalid route definition: "+ route);
            } else {
                routeBuilder.append('/');
                routeBuilder.append(StringUtils.lowerCase(rtk));
            }
        }
        return routeBuilder.toString();
    }




    protected  static class Route extends BaseEntity{

        private static final long serialVersionUID = 588326727953833067L;

        private String route;
        private Map<RequestMethod, RequestMapper> mappers;

        public Route(final String route, final Map<RequestMethod, RequestMapper> mappers){
            this.route = route;
            this.mappers = mappers;
        }


        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
        }

        public Map<RequestMethod, RequestMapper> getMappers() {
            return mappers;
        }

        public void setMappers(Map<RequestMethod, RequestMapper> mappers) {
            this.mappers = mappers;
        }
    }


}
