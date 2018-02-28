package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;

import tdt4140.gr1816.app.api.types.SigninPayload;
import tdt4140.gr1816.app.api.types.User;

/* Because SigninPayload data class contains a complex
 * (non-scalar) object User, it needs a companion resolver class */
public class SigninResolver implements GraphQLResolver<SigninPayload> {

    public User user(SigninPayload payload) {
        return payload.getUser();
    }
}