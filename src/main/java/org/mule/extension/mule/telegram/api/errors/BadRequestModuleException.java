package org.mule.extension.mule.telegram.api.errors;

import org.mule.runtime.extension.api.exception.ModuleException;

public class BadRequestModuleException extends ModuleException {

    public BadRequestModuleException(Exception cause) {
        super(SimpleError.BAD_REQUEST, cause);
    }

    public BadRequestModuleException(String message) {
        super(SimpleError.BAD_REQUEST, new Exception(message));
    }
}
