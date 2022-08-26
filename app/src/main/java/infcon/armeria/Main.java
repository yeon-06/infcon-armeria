package infcon.armeria;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.server.Server;

import java.util.concurrent.CompletableFuture;

public final class Main {

    public static void main(String[] args) {
        Backend foo = Backend.of("foo", 9000);
        foo.start();
        WebClient fooClient = WebClient.of("http://127.0.0.1:9000");

        // 생성자보다 builder 같은 static method 선호
        Server server = Server.builder()
                .http(8080)
                .service("/infcon", new MyService(fooClient))
                .build();

        CompletableFuture<Void> future = server.start();
        future.join();
    }
}
