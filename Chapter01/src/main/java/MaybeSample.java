import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class MaybeSample {
    public static void main(String[] args) {
        // Maybe 생성
        Maybe<DayOfWeek> maybe = Maybe.create(emitter -> {
            emitter.onSuccess(LocalDate.now().getDayOfWeek());
        });

        // 구독
        maybe.subscribe(new MaybeObserver<DayOfWeek>() {

            // 구독 준비가 됐을 때의 처리
            @Override
            public void onSubscribe(@NotNull Disposable d) {
                // do nothing
            }

            // 데이터 통지를 받을 때의 처리
            @Override
            public void onSuccess(@NotNull DayOfWeek dayOfWeek) {
                System.out.println(dayOfWeek);
            }

            @Override
            public void onError(@NotNull Throwable e) {
                System.out.println("Error = " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("완료");
            }
        });
    }
}
