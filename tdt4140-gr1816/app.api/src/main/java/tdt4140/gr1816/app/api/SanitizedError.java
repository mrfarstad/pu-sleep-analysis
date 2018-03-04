package tdt4140.gr1816.app.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graphql.ExceptionWhileDataFetching;

// Error wrapper for better exception handling
public class SanitizedError extends ExceptionWhileDataFetching {
    
    public SanitizedError(ExceptionWhileDataFetching inner) {
        super(inner.getException());
    }

    @Override
    @JsonIgnore
    public Throwable getException() {
        return super.getException();
    }
}