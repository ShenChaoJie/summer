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
package org.summerframework.commons.util;

import java.util.Comparator;
import java.util.Map;

/**
 * TODO.
 *
 * @author CJSHEN
 * @since 1.0
 */
public interface PathMatcher {

    boolean isPattern(String path);

    boolean match(String pattern, String path);

    boolean matchStart(String pattern, String path);

    String extractPathWithinPattern(String pattern, String path);

    Map<String, String> extractUriTemplateVariables(String pattern, String path);

    Comparator<String> getPatternComparator(String path);

    String combine(String pattern1, String pattern2);

}
