import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class FlowableSample {
    public static void main(String[] args) throws Exception {

        // 인사말을 통지하는 Flowable을 생성한다.
        Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@org.jetbrains.annotations.NotNull FlowableEmitter<String> emitter) throws Exception {
                String[] datas = { "Hello, World", "안녕, RxJava!" };

                for (String data : datas) {
                    // 구독이 해지되면 처리를 중단한다.
                    if (emitter.isCancelled()) {
                        return;
                    }

                    // 데이터를 통지한다.
                    emitter.onNext(data);
                }

                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER);

        flowable
                // Subscriber 처리를 개별 스레드에서 실행한다.
                .observeOn(Schedulers.computation())
                // 구독한다.
                .subscribe(new Subscriber<String>() {
                    private Subscription subscription;

                    // 구독 시작 시 처리.
                    @Override
                    public void onSubscribe(Subscription s) {
                        // Subscription을 Subscriber에 보관한다.
                        subscription = s;
                        // 받을 데이터 개수를 요청한다.
                        subscription.request(1L);
                    }

                    // 데이터를 받을 때 처리.
                    @Override
                    public void onNext(String s) {
                        // 실행 중인 스레드 이름을 얻는다.
                        String threadName = Thread.currentThread().getName();
                        // 받은 데이터를 출력한다.
                        System.out.println(threadName + ": " + s);
                        // 다음에 받을 데이터 개수를 요청한다.
                        subscription.request(1L);
                    }

                    // 에러 통지 시 처리.
                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    // 완료 통지 시 처리.
                    @Override
                    public void onComplete() {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + ": 완료");
                    }
                });

        Thread.sleep(1000L);
    }
}
