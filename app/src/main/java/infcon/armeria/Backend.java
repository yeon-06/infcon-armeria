package infcon.armeria;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Server;

import java.time.Duration;

public final class Backend {

    private final Server server;

    public static Backend of(String name, int port) {
        return new Backend(name, port);
    }

    private Backend(String name, int port) {
        server = Server.builder()
                .http(port)
                .service("/foo", (ctx, req) -> {
                    HttpResponse response = HttpResponse.of("response from: " + name);
                    return HttpResponse.delayed(response, Duration.ofSeconds(3));
                })
                .build();
    }

    public void start() {
        server.start().join();
    }
}
