package donald.apiwithspringboot.exceptions.graphql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserInfoExistedException extends RuntimeException implements GraphQLError {

    private String username;

    public UserInfoExistedException(String username) {
        this.username = username;
    }

    @Override
    public String getMessage() {
        return "User already existed :" + username;
    }

    @Override
    @JsonIgnore
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    public Map<String, Object> getExtensions() {
        return Collections.singletonMap("username", username);
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.ValidationError;
    }


}
