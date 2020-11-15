import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;

public class ObservableSample {
    public static void main(String[] args) throws Exception {

        // 인사말을 통지하는 Observable 생성
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NotNull ObservableEmitter<String> emitter) throws Exception {
                // 통지 데이터
                String[] datas = { "Hello, World!", "안녕, RxJava!" };

                for (String data : datas) {
                    // 구독이 해지되면 처리를 중단한다.
                    if (emitter.isDisposed()) {
                        return;
                    }

                    // 데이터를 통지한다.
                    emitter.onNext(data);
                }

                // 완료를 통지한다.
                emitter.onComplete();
            }
        });

        observable
                // 소비하는 측의 처리를 개별 스레드로 실행한다.
                .observeOn(Schedulers.computation())
                // 구독한다.
                .subscribe(new Observer<String>() {
                    // subscribe 메서드 호출 시의 처리
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        // 아무것도 하지 않는다.
                    }

                    @Override
                    public void onNext(@NotNull String s) {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + ": " + s);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + ": 완료");
                    }
                });

        Thread.sleep(500L);
    }
}
