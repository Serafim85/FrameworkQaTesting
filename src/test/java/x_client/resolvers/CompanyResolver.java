package x_client.resolvers;

import x_client.helpers.AuthHelper;
import x_client.helpers.CompanyHelper;
import x_client.helpers.servise.AuthUserNameAndPassword;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.helpers.AnnotationHelper;

public class CompanyResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(CompanyHelper.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        AuthUserNameAndPassword annotation = AnnotationHelper
                .findAnnotation(parameterContext.getAnnotatedElement(), AuthUserNameAndPassword.class);
        String userName = annotation.userName();
        String password = annotation.password();

        AuthHelper authHelper = new AuthHelper(userName, password);
        String token = authHelper.authorization().orElse("");

        if (!token.equals(""))
            return new CompanyHelper(userName, token);
        else
            return new CompanyHelper();
    }
}
