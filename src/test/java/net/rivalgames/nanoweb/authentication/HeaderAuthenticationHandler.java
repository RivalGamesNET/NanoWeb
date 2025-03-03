package net.rivalgames.nanoweb.authentication;

import io.javalin.http.Context;

/**
 * @date 3/3/2025
 * @author Moose1301
 */
public class HeaderAuthenticationHandler implements AuthenticationHandler {
    @Override
    public Boolean handle(Context context) {
        String header = context.header("Authorization");

        return header != null && header.equalsIgnoreCase("123456");
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
