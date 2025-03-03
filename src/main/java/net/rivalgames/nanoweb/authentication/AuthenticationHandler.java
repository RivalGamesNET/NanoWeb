package net.rivalgames.nanoweb.authentication;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

/**
 * @date 2/16/2025
 * @author Moose1301
 */
public interface AuthenticationHandler extends Comparable<AuthenticationHandler> {

    /**
     * @param context The request context
     *
     * @return
     * True = Request is allowed through
     * False = Request is blocked
     * Null = Request should be handled by next auth handler
     */
    Boolean handle(Context context);

    /**
     * @return The priority of this Authentication Handler (Lower = Last To Call, Higher = First to Call)
     */
    int getPriority();

    @Override
    default int compareTo(@NotNull AuthenticationHandler o) {
        return Integer.compare(o.getPriority(), this.getPriority());
    }

}
