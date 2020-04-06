package org.mule.extension.mule.telegram.api.errors;

import org.mule.runtime.extension.api.exception.ModuleException;

public class NotFoundModuleException extends ModuleException {

    public NotFoundModuleException(Exception cause) {
        super(SimpleError.NOT_FOUND, cause);
    }

    public NotFoundModuleException(String cause) {
        super(SimpleError.NOT_FOUND, new Exception(cause));
    }

}
