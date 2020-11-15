import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;

public class CompletableSample {
    public static void main(String[] args) throws Exception {
        // Completable 생성
        Completable completable = Completable.create(emitter -> {
            // 중략(업무 로직 처리)

            // 완료 통지
            emitter.onComplete();
        });

        completable
                // Completable을 비동기로 실행
                .subscribeOn(Schedulers.computation())
                // 구독
                .subscribe(new CompletableObserver() {

                    // 구독 준비가 되었을 때의 처리
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        // do nothing
                    }

                    // 완료 통지를 받을 때의 처리
                    @Override
                    public void onComplete() {
                        System.out.println("완료");
                    }

                    // 에러 통지를 받을 때의 처리
                    @Override
                    public void onError(@NotNull Throwable e) {
                        System.out.println("Error = " + e);
                    }
                });

        Thread.sleep(100L);
    }
}
