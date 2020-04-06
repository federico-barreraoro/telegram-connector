package org.mule.extension.mule.telegram.api.errors;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

public enum SimpleError implements ErrorTypeDefinition<SimpleError> {
        BAD_REQUEST,
        NOT_FOUND,
        UNKNOWN_ERROR
}
