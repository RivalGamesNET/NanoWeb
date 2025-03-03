package net.rivalgames.nanoweb;

import net.rivalgames.nanoweb.authentication.HeaderAuthenticationHandler;
import net.rivalgames.nanoweb.controller.ExampleController;

/**
 * @date 2/16/2025
 * @author Moose1301
 */
public class ExampleNanoWeb {
    public static void main(String[] args) {
        NanoWeb web = new NanoWeb(8080);

        web.registerAuthenticationHandler(new HeaderAuthenticationHandler());
        web.registerControllers(new ExampleController());
        web.register();


        web.shutdown();
    }
}
