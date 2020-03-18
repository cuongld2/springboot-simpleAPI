package donald.apiwithspringboot.exceptions.graphql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import java.util.List;

public class InvalidCredentialsException extends RuntimeException implements GraphQLError {

    private String username;
    private String password;

    public InvalidCredentialsException(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getMessage() {
        return "Invalid credentials : with username is : " + username + " and password is : " + password;
    }

    @Override
    @JsonIgnore
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
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
