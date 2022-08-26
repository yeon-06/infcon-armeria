package infcon.armeria;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Server;

import java.util.concurrent.CompletableFuture;

public final class Main {

    public static void main(String[] args) {
        // 생성자보다 builder 같은 static method 선호
        Server server = Server.builder()
                .http(8080)
                .service("/infcon", (context, request) -> {
                    return HttpResponse.of("Hello, Armeria!");
                })
                .build();

        CompletableFuture<Void> future = server.start();
        future.join();
    }
}
