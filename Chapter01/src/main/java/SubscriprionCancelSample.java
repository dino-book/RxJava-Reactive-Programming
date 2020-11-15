import io.reactivex.Flowable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

// 구독을 중도 해지하는 예제
public class SubscriprionCancelSample {
    public static void main(String[] args) throws Exception {
        Flowable.interval(200L, TimeUnit.MILLISECONDS)
                .subscribe(new Subscriber<Long>() {
                    private Subscription subscription;
                    private long startTime;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        startTime = System.currentTimeMillis();
                        subscription.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        // 구독 시작부터 500밀리초가 지나면 구독을 해지하고 처리를 중지한다.
                        if (System.currentTimeMillis() - startTime > 500) {
                            subscription.cancel();
                            System.out.println("구독 해지");
                        } else {
                            System.out.println("data = " + aLong);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        Thread.sleep(2000L);
    }
}
