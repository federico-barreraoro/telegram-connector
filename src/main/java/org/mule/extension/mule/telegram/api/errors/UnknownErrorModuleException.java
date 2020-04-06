package org.mule.extension.mule.telegram.api.errors;

import org.mule.runtime.extension.api.exception.ModuleException;

public class UnknownErrorModuleException extends ModuleException {

    public UnknownErrorModuleException(Throwable cause) {
        super(SimpleError.UNKNOWN_ERROR, cause);
    }
}
