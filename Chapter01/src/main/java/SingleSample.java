import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class SingleSample {
    public static void main(String[] args) throws Exception {
        // Single 생성
        Single<DayOfWeek> single = Single.create( emitter -> {
            emitter.onSuccess(LocalDate.now().getDayOfWeek());
        });

        // 구독
        single.subscribe(new SingleObserver<DayOfWeek>() {

            // 구독 준비가 됐을 때의 처리
            @Override
            public void onSubscribe(@NotNull Disposable d) {
                // do nothing
            }

            // 데이터 통지를 받았을 때의 처리
            @Override
            public void onSuccess(@NotNull DayOfWeek dayOfWeek) {
                System.out.println(dayOfWeek);
            }

            // 에러 통지를 받았을 때의 처리
            @Override
            public void onError(@NotNull Throwable e) {
                System.out.println("Error = " + e);
            }
        });
    }
}
