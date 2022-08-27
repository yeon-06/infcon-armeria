package infcon.armeria;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.ServiceRequestContext;

import java.util.concurrent.CompletableFuture;

public final class MyService implements HttpService {

    private final WebClient fooClient;
    private final WebClient barClient;

    public MyService(WebClient fooClient, WebClient barClient) {
        this.fooClient = fooClient;
        this.barClient = barClient;
    }

    @Override
    public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
        CompletableFuture<HttpResponse> future = new CompletableFuture<>();

        fooClient.get("/foo")
                .aggregate()
                .thenAccept(fooResponse -> {
                    // foo가 완료된 후 bar를 호출
                    barClient.get("/bar").aggregate()
                            .thenAccept(barResponse -> {
                                HttpResponse response = HttpResponse.of(fooResponse.contentUtf8() + '\n' + barResponse.contentUtf8());
                                future.complete(response);
                            });
                });
        return HttpResponse.from(future);
    }
}
