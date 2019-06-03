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

import com.google.common.base.Charsets;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * TODO.
 *
 * @author CJSHEN
 * @since 1.0
 */
public class UrlPathHelper {

    public Map<String, String> decodePathVariables(final Map<String, String> vars){
        Map<String, String> decodedVars = new LinkedHashMap<>(vars.size());
        for(Map.Entry<String, String> entry : vars.entrySet()){
            decodedVars.put(entry.getKey(), entry.getValue());
        }
        return decodedVars;
    }


    private String decodeInternal(final String source){
        try {
            return URLDecoder.decode(source, Charsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
