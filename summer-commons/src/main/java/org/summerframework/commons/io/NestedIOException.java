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
package org.summerframework.commons.io;

import java.io.IOException;

/**
 * Subclass of IOException that properly handles a root cause,
 * exposing the root cause just like NestedChecked/RuntimeException does.
 *
 * <p>The similarity between this class and the NestedChecked/RuntimeException
 * class is unavoidable, as this class needs to derive from IOException
 * and cannot derive from NestedCheckedException.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see #getMessage
 * @see #printStackTrace
 */
public class NestedIOException extends IOException {
    private static final long serialVersionUID = -713470029719907448L;

    /**
     * Construct a <code>NestedIOException</code> with the specified detail message.
     * @param msg the detail message
     */
    public NestedIOException(String msg) {
        super(msg);
    }

    /**
     * Construct a <code>NestedIOException</code> with the specified detail message
     * and nested exception.
     * @param msg the detail message
     * @param cause the nested exception
     */
    public NestedIOException(String msg, Throwable cause) {
        super(msg);
        initCause(cause);
    }

    /**
     * Return the detail message, including the message from the nested exception
     * if there is one.
     */
    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
    }

}
