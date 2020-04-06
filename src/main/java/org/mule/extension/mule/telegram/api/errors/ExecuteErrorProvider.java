package org.mule.extension.mule.telegram.api.errors;

import org.mule.runtime.extension.api.annotation.error.ErrorTypeProvider;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.HashSet;
import java.util.Set;

public class ExecuteErrorProvider implements ErrorTypeProvider {

    @Override
    public Set<ErrorTypeDefinition> getErrorTypes() {
        HashSet<ErrorTypeDefinition> errors = new HashSet<>();
        errors.add(SimpleError.NOT_FOUND);
        errors.add(SimpleError.BAD_REQUEST);
        errors.add(SimpleError.UNKNOWN_ERROR);
        return errors;
    }
}
