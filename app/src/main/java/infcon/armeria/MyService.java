package infcon.armeria;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.ServiceRequestContext;

import java.util.concurrent.CompletableFuture;

public final class MyService implements HttpService {

    private final WebClient fooClient;

    public MyService(WebClient fooClient) {
        this.fooClient = fooClient;
    }

    @Override
    public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
        CompletableFuture<HttpResponse> future = new CompletableFuture<>();

        fooClient.get("/foo")
                .aggregate()
                .thenAccept(aggregatedHttpResponse -> {
                    System.out.println(aggregatedHttpResponse.contentUtf8()); // 로그 찍기
                    // 콜백에서는 바로 return할 수 없으니 future라는 껍데기를 이용했다.
                    future.complete(aggregatedHttpResponse.toHttpResponse()); // 껍데기에 알맹이 넣기
                });
        return HttpResponse.from(future);
    }
}
