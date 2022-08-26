package infcon.armeria;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpResponse;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

class BackendTest {

    @Test
    void backend() throws InterruptedException {
        Backend foo = Backend.of("foo", 9000);
        foo.start();

        WebClient webClient = WebClient.of("http://127.0.0.1:9000");

        /* 비동기 통신임을 기억하자.
         * /foo에서는 3초 뒤에 response 값을 받는다.
         * 그 전까지는 httpResponse 껍데기만 존재한다.
         */
        HttpResponse httpResponse = webClient.get("/foo"); // post, get, put, delete, ... 의 get

        /*
         * httpResponse를 이용해 또 다른 껍데기를 만들었다.
         * 이를 이용해 콜백을 해보겠다.
         */
        CompletableFuture<AggregatedHttpResponse> future = httpResponse.aggregate();
        future.thenAccept(aggregatedHttpResponse -> {
            System.out.println(Thread.currentThread().getName());
            sendBackToTheOriginalClient(aggregatedHttpResponse);
        });

        Thread.sleep(Long.MAX_VALUE);
    }

    private void sendBackToTheOriginalClient(AggregatedHttpResponse aggregatedHttpResponse) {
    }
}
