# RxJava의 기본

<br />

## RxJava와 리액티브 프로그래밍

### 리액티브 프로그래밍

데이터가 통지될 때마다 관련 프로그램이 `반응(reaction)`해 데이터를 처리하는 프로그래밍 방식.

생성되는 데이터를 한 번에 보내지 않고 각각의 데이터가 생성될 때마다 순서대로 보낸다. 이러한 데이터 흐름을 `데이터 스트림(data stream)`이라 한다. 이것은 이미 생성된 데이터 집합인 리스트(list) 같은 컬렉션과는 다르게 앞으로 발생할 가능성이 있는 데이터까지도 포함한다. '버튼을 누른다.'와 같은 이벤트도 발생할 때마다 데이터를 전송하는 데이터 스트림으로 다룰 수 있다. 

리액티브 프로그래밍은 이러한 데이터 스트림으로 데이터를 전달받은 프로그램이 그때마다 적절히 처리할 수 있게 구성된다. 프로그램이 필요한 데이터를 직접 가져와 처리하는 것이 아니라 보내온 데이터를 받은 시점에 반응해 이를 처리하는 프로그램을 만드는 것이 리액티브 프로그래밍이다.

<br />

예를 들어, 상품 가격과 부가가치세 세율로 부가가치세를 계산하는 프로그램이 있다고 가정한다. 리액티브 프로그래밍이 아니라면 상품 가격과 부가가치세 세율을 얻는 것만으로는 어떤 일도 일어나지 않는다. 값을 얻은 후에 '부가가치세를 계산한다'라는 **행위**가 이루어져야 비로소 부가가치세를 계산한다. 또한 부가가치세를 계산한 후 상품 가격이 변경되더라도 다시 계산 처리 행위가 이루어지지 않는 한 부가가치세는 변경되지 않는다.

하지만 이를 리액티브 프로그래밍으로 구현하면 상품 가격이 변경될 때마다 부가가치세를 계산하는 프로그램에 변경된 가격이 전달되고, 프로그램이 다시 계산해 이 계산 결과를 부가가치세에 반영한다. 따라서 상품 가격이 바뀔 때마다 상품 가격이 통지되고 부가가치세가 자동으로 계산된다.

어떤 프로그램이 리액티브 프로그래밍인지는 무엇이 데이터의 처리를 수행하는가로 가릴 수 있다. 상품 가격이 변경될 때 리스터가 반응하면서 상품 가격에 해당하는 부가가치세를 다시 계산해 표시하는 것은 리액티브 프로그래밍이라고 할 수 없다.

이를 리스너가 반응하면서 부가가치세 항목에 새로운 데이터가 전달되고 부가가치세 항목에서 계산 프로그램을 실행해 결과를 부가가치세로 표시한다고 생각하면 이는 리액티브 프로그래밍이라고 할 수 있다.

<br />

리액티브 프로그래밍에서 데이터를 생산하는 측은 데이터를 전달하는 것까지 책임진다. 그러므로 데이터를 생산하는 측은 데이터를 소비하는 측이 전달받은 데이터로 무엇을 하는지는 몰라도 된다. 또한, 데이터를 생산하는 측은 데이터를 소비하는 측에서 무엇을 하든지 관계가 없으므로 소비하는 측의 처리를 기다릴 필요가 없다. 그러므로 데이터를 통지한 후 데이터를 소비하는 측에서 데이터를 처리하는 도중이라도 데이터를 생산하는 측은 바로 다음 데이터를 처리할 수 있다.

<br />

### RxJava의 특징

RxJava는 디자인 패턴인 `옵저버 패턴(Observer Pattern)`을 잘 확장했다. 옵저버 패턴은 감시 대상 객체의 상태가 변하면 이를 관찰하는 객체에 알려 주는 구조다. 이 패턴의 특징을 살리면 데이터를 생성하는 측과 데이터를 소비하는 측으로 나눌 수 있기 때문에 쉽게 데이터 스트림을 처리할 수 있다.

RxJava의 또 다른 특징으로 쉬운 비동기 처리를 들 수 있다. `Reactive Streams` 규칙의 근간이 되는 `Observable 규약`이라는 RxJava 가이드라인을 따른 구현이라면 직접 스레드를 관리하는 번거로움에서 벗어날 뿐만 아니라 구현도 간단하게 할 수 있다.

그리고 RxJava는 함수형 프로그래밍의 영향을 받아 함수형 인터페이스를 인자로 전달 받는 메서드를 사용해 대부분의 처리를 구현한다. 이는 입력과 결과만 정해져 있다면 구체적인 처리는 개발자에게 맡길 수 있으므로 더욱 자유로운 구현이 가능함을 의미한다. 

<br />

<br />

## Reactive Streams

### Reactive Streams의 구성

`Reactive Streams`는 데이터를 만들어 통지하는 `Publisher(생산자)`와 통지된 데이터를 받아 처리하는 `Subscriber(소비자)`로 구성된다. Subscriber가 Pusblisher를 `구독(subscribe)`하면 Publisher가 통지한 데이터를 Subscriber가 받을 수 있다.

- **Publisher** : 데이터를 통지하는 생산자
- **Subscriber** : 데이터를 받아 처리하는 소비자

<br />

Publisher는 통지 준비가 끝나면 Subscriber에 `통지(onSubscribe)`한다. 해당 통지를 받은 Subscriber는 받고자 하는 데이터 개수를 요청한다. 이때 Subscriber가 자신이 통지받을 데이터 개수를 요청하지 않으면 Publisher는 통지해야 할 데이터 개수 요청을 기다리게 되므로 통지를 시작할 수 없다.

그 다음 Publisher는 데이터를 만들어 Subscriber에게 `통지(onNext)`한다. 이 데이터를 받은 Subscriber는 받은 데이터를 사용해 처리 작업을 수행한다. Publisher는 요청받은 만큼의 데이터를 통지한 뒤 Subscriber로부터 다음 요청이 올 때까지 데이터 통지를 중단한다. 이후 Subscriber가 처리 작업을 완료하면 다음에 받을 데티어 개수를 Publisher에 요청한다. 이 요청을 보내지 않으면 Publisher는 요청 대기 상태가 돼 Subscriber에 데이터를 통지할 수 없다.

Publisher는 Subscriber에 모든 데이터를 통지하고 마지막으로 데이터 전송이 완료돼 정상 종료됐다고 `통지(onComplete)`한다. 완료 통지를 하고 나면 Publisher는 이 구독 건에 대해 어턴 통지도 하지 않는다. 또한 Publisher는 처리 도중에 에러가 발생하면 Subscriber에 발생한 객체와 함께 에러를 `통지(onError)`한다.

<br />

Subscriber가 Publisher에 통지받을 데이터 개수를 요청하는 것은 Publisher가 통지하는 데이터 개수를 제어하기 위해서다. 예를 들어, Publisher와 Subscriber의 처리가 각각 다른 스레드에서 진행되는데 Publisher의 통지 속도가 빠르면 Subscriber가 소화할 수 없을 만큼 많은 처리 대기 데이터가 쌓인다. 이를 막기 위해 Publisher가 통지할 데이터 개수를 Subscriber가 처리할 수 있을 만큼으로 제어하는 수단이 필요하다.

<br />

#### Reactive Streams가 제공하는 프로토콜

- **onSubscribe** : 데이터 통지가 준비됐음을 통지
- **onNext** : 데이터 통지
- **onError** : 에러 통지
- **onComplete** : 완료 통지

<br />

#### Reactive Streams의 인터페이스

- **Publisher** : 데이터를 생성하고 통지하는 인터페이스

  ```java
  public interface Publisher<T> {
      // 데이터를 받는 Subscriber 등록
      public void subscribe(Subscribe <? super T> subscriber);
  }
  ```

- **Subscriber** : 통지된 데이터를 전달받아 처리하는 인터페이스

  ```java
  public interface Subscriber<T> {
      // 구독 시작 처리
      public void onSubscribe(Subscription subscription);
      
      // 데이터 통지 시 처리
      public void onNext(T item);
      
      // 에러 통지 처리
      public void onError(Throwable error);
      
      // 완료 통지 시 처리
      public void onComplete();
  }
  ```

- **Subscription** : 데이터 개수를 요청하고 구독을 해지하는 인터페이스

  ```java
  public interface Subscription {
      // 통지받을 데이터 개수 요청
      public void request(long num);
      
      // 구독 해지
      public void cancel();
  }
  ```

- **Processor** : Publisher와 Subscriber의 기능이 모두 있는 인터페이스

  ```java
  public abstract interface Processor<T, R> extends Subscriber<T>, Publisher<R> {
      
  }
  ```

<br />

### Reactive Streams의 규칙

- 구독 시작 통지(onSubscribe)는 해당 구독에서 한 번만 발생한다.
- 통지는 순차적으로 이루어진다.
- null을 통지하지 않는다. 
- Publisher의 처리는 완료(onComplete) 또는 에러(onError)를 통지해 종료한다.
- 데이터 개수 요청에 `Long.MAX_VALUE`를 설정하면 데이터 개수에 의한 통지 제한은 없어진다.
- Subscription의 메서드는 동기화된 상태로 호출해야 한다.

또한 요청받은 데이터 개수가 남은 상태에서 추가로 데이터 개수를 요청받으면 새로 요청받은 데이터 개수가 기존 개수에 더해져 통지 가능한 데이터 개수가 증가한다. 그래서 이 더해진 결과가 Long.MAX_VALUE에 도달하면 통지 가능한 데이터 개수 제한이 없어진다.

RxJava를 사용할 때는 각 통지 메서드와 Subscription의 메서드를 호출할 때 동기화가 이뤄지므로 처리 자체가 스레드 안전한지를 특히 신경써야 한다.

<br />

<br />

## RxJava의 기본 구조

### 기본 구조

RxJava는 데이터를 만들고 통지하는 생산자와 통지된 생산자를 받아 처리하는 소비자로 구성된다. 이 생산자를 소비자가 구독해 생산자가 통지한 데이터를 소비자가 받게 된다. RxJava에서 이 생산자와 소비자의 관계는 크게 두 가지로 나뉜다.

- **Reactive Streams 지원** : Flowable(생산자) / Subscriber(소비자)
- **Reactive Streams 미지원** : Observable(생산자) / Observer(소비자)

<br />

`Flowable`은 Reactive Streams의 생산자인 `Publisher`를 구현한 클래스고, `Subscriber`는 Reactive Streams의 클래스다. 때문에 기본적인 메커니즘은 동일하다. 생산자인 Flowable로 구독 시작(onSubscribe), 데이터 통지(onNext), 에러 통지(onError), 완료 통지(onComplete)를 하고 각 통지를 받은 시점에 소비자인 Subscriber로 처리한다. 그리고 `Subscription`으로 데이터 개수 요청과 구독 해지를 한다.

반면 `Observable`과 `Observer` 구성은 Reactive Streams를 구현하지 않아서 Reactive Streams 인터페이스를 사용하지 않는다. 하지만 기본적인 메커니즘은 거의 같다. 생산자인 Observable에서 구독 시작(onSubscribe), 데이터 통지(onNext), 에러 통지(onError), 완료 통지(onComplete)를 하면 Observer에서 이 통지를 받는다.

다만 Observable과 Observer 구성은 통지하는 데이터 개수를 제어하는 배압 기능이 없기 때문에 데이터 개수를 요청하지 않는다. 그래서 Subscription을 사용하지 않고, `Disposable`이라는 구독해지 메서드가 있는 인터페이스를 사용한다. 이 Disposable은 구독을 시작하는 시점에 onSubscribe 메서드의 인자로 Observer에 전달된다. 그래서 Observable과 Observer 간에 데이터를 교환할 때 Flowable과 Subscriber처럼 데이터 개수 요청은 하지 않고 데이터가 생성되자마자 Observer에 통지된다.

<br />

### 연산자

RxJava에서 통지하는 데이터를 생성하거나 필터링 또는 변환하는 메서드를 연산자라고 한다. 그리고 이 연산자를 연결해나감으로써 최종 통지 데이터의 단순한 처리를 단계적으로 설정할 수 있다.

<br />

```java
public static void main(String[] args) {
    Flowable<Integer> flowable = 
        Flowable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .filter(data -> data % 2 == 0)
        .map(data -> data * 100);
    
    flowable.subscribe(data -> Syetem.out.println("data=" + data));
}
```

이처럼 데이터를 메서드 체인에 통과하게 해 원본 Flowable/Observable이 통지하는 데이터를 최종으로 어떤 데이터로 통지할 것인지를 제어할 수 있다. 빌더 패턴과 유사하며, 연산자를 설정한 시점에서 그 처리가 실행되는 것이 아니라 통지 처리가 실행되고 통지를 받는 시점에 설정한 처리가 실행된다. 하지만 RxJava의 메서드 체인은 빌더 패턴과 달리 연산자를 설정한 순서가 실행하는 작업에 영향을 미치기 때문에 주의해야 한다.

<br />

RxJava는 함수형 프로그래밍의 영향을 많이 받아 메서드 대부분이 함수형 인터페이스를 인자로 받는다. 그래서 이 함수형 인터페이스의 구현은 함수형 프로그래밍의 원칙에 따라 같은 입력을 받으면 매번 같은 결과를 반환하며, 기본으로 그 입력값과 처리는 외부 객체나 환경에 의해 변화가 일어나지 않아야 한다. 부가 작용(side effect)의 발생을 피해야 하는 것이다. 외부의 상태 변경이 데이터 통지 처리에 영향을 주는 것을 피하지 않으면 책임 범위가 넓어져 단순한 처리라도 관리가 어렵다.

함수형 프로그래밍은 데이터 처리 시점에도 영향을 미친다. 인자로 데이터를 직접 작성하면 지정한 시점에 평가된 값을 받는다. 인자로 수식을 지정하더라도 메서드는 지정한 시점에 평가된 수식을 결과로 전달받는다. 즉, 메서드가 실행되기 전에 이미 값이 결정된다.

```java
/**
* - 값을 인자로 받는 just 메서드
* Flowable은 생성된 시점의 시스템 시각을 통지한다.
* 
* 여러 번 구독해도 같은 값을 통지한다.
*/ 
Flowable<Long> flowable = Flowable.just(System.currentTimeMills());

/**
* - 함수형 인터페이스를 메서드의 인자로 지정
* 해당 메서드가 처리를 수행하는 시점에 정의된 처리를 실행해 값을 가져온다.
* Flowable은 구독한 시점에서 함수를 호출해 호출한 시점의 시스템 시각을 통지한다.
*
* 구독할 때마다 다른 값을 통지한다.
*/ 
Floawble flowable = Flowable.fromCallable(() -> System.currentTimeMills());
```

<br />

### 비동기 처리

RxJava에서는 개발자가 직접 스레드를 관리하지 않게 각 처리 목적에 맞춰 스레드를 관리하는 스케줄러를 제공한다. 이 스케줄러를 설정하면 어떤 스레드에서 무엇을 처리할지 제어할 수 있다.

<br />

스케줄러는 데이터를 생성해 통지하는 부분과 데이터를 받아 처리하는 부분에 지정할 수 있다. 전자는 데이터 생성의 기본이 되는 생산자(Flowable, Observable)를 말하는 것으로, 생성 메서드에서 생성한 Flowable/Observable이 데이터 통지를 어떤 스케줄러에서 처리할지를 제어한다. 후자는 데이터의 필터나 변환을 하는 메서드와 소비자(Subscriber/Observer) 등이 데이터의 수신 처리를 어느 스케줄러에서 할지를 제어한다. 데이터를 통지하는 측과 받는 측은 데이터 통지 시에만 데이터를 주고 받아야 하며, 그 이외의 요인으로 서로의 행동에 영향을 주지 않아야 한다.

```java
// 외부 영향을 받는 예제

// 계산 방법을 나타내는 enum 객체
private enum State {
    ADD, MULTIPLY;
};

// 계산 방법
private static State calcMethod;

public static void main(String[] args) throws Exception {
    // 계산 방법을 덧셈으로 설정한다.
    calcMethod = State.ADD;
    
    Flowable<Long> flowable =
        // 300밀리초마다 데이터를 통지하는 flowable을 생성한다.
        Flowable.interval(300L, TimeUnit.MILLISECONDS)
        // 7건까지 통지한다.
        .take(7)
        // 각 데이터를 계산한다.
        .scan((sum, data) -> {
            if (calcMethod == State.ADD) {
                return sum + data;
            } else {
                return sum * data;
            }
        });
    
    // 구독하고 받은 데이터를 출력한다.
    flowable.subscribe(data -> System.out.println("data=" + data));
    
    // 잠시 기다렸다가 계산 방법을 곱셈으로 변경한다.
    Thread.sleep(1000);
    System.out.println("계산 방법 변경");
    calcMethod = State.MULTIPLY;
    
    // 잠시 기다린다.
    Thread.sleep(2000);
}
```

<br />

비동기로 처리할 때는 생산자에서 소비자까지의 처리가 노출되지 않게 폐쇄적으로 개발하면 어느 정도 위험을 줄일 수 있다. 기본적으로는 생산자가 외부에서 데이터를 받아 데이터를 생성할 때와 소비자가 받은 데이터를 처리하고  외부에 반영할 때만 외부 데이터를 참조하는 것이 좋다.

<br />

### Cold 생산자와 Hot 생산자

`Cold 생산자`는 1개의 소비자와 구독 관계를 맺지만, `Hot 생산자`는 여러 소비자와 구독 관계를 맺을 수 있다. Cold 생산자가 통지하는 데이터의 타임라인은 구독할 때마다 생성되지만, Hot 생산자는 이미 생성한 통지 데이터의 타임라인에 나중에 소비자가 참가하는 것을 허용한다.

그래서 Cold 생산자를 구독하면 생산자 처리가 시작되지만, Hot 생산자는 구독해도 생산자 처리가 시작되지 않을 수 있다. 또한, Hot 생산자의 경우 이미 처리를 시작한 생산자를 구독하면 소비자는 구독한 시점부터 데이터를 받게 되고, 같은 데이터를 여러 소비자가 받을 수도 있다.

RxJava에서 생성 메서드로 생성된 생산자는 기본적으로 Cold 생산자이다. Hot 생산자를 생성하려면 Cold 생산자에서 Hot 생산자로 변환하는 메서드를 호출하거나 `Process`나 `Subject`를 생성해야 한다.

<br />

### ConnectableFlowable/ConnectableObservable

`ConnectableFlowable/ConnectableObservable`은 Hot Flowable/Observable이며, 여러 Subscriber/Observer에서 동시에 구독할 수 있다. 또한, Cold와 달리 `subscribe()` 메서드를 호출해도 처리를 시작하지 않고 `connect()` 메서드를 호출해야 처리를 시작한다. 그래서 처리를 시작하기 전에 여러 Subscriber/Observer에서 구독하게 하고 그 후 처리를 시작해 처음부터 동시에 여러 구독자에게 데이터를 통지할 수 있다.

<br />

#### refCount

`refCount()` 메서드는 ConnectableFlowable/ConnectableObservable에서 새로운 Flowable/Observable을 생성한다. 이 Flowable/Observable은 이미 다른 소비자가 구독하고 있다면 도중에 구독하더라도 같은 타임라인에서 생성되는 데이터를 통지한다. refCount() 메서드에서 생성한 Flowable/Observable은 더 이상 ConnectableFlowable/ConnectableObservable이 아니므로 connect() 메서드가 없다. 그러므로 아직 구독 상태가 아니라면 subscribe() 메서드가 호출될 때 처리를 시작한다. 또한, refCount() 메서드로 생성한 Flowable/Observable은 처리가 완료된 뒤 또는 모든 구독이 해지된 뒤에는 다시 subscribe 메서드를 호출해도 처리가 다시 시작되지 않는다.

<br />

#### autoConnect

`autoConnect()` 메서드는 ConnectableFlowable/ConnectableObservable에서 지정한 개수의 구독이 시작된 시점에 처리를 시작하는 Flowable/Observable을 생성한다. autoConnect() 메서드에서 인자 없이 생성한다면 처음 subscribe() 메서드가 호출된 시점에 처리를 시작하고, autoConnect() 메서드에서 인자로 구독 개수를 지정한다면 지정한 개수에 도달한 시점에서 처리를 시작한다. autoConnect() 메서드로 생성한 Flowable/Observable은 처리가 완료된 뒤 또는 모든 구독이 해지된 뒤에는 다시 subscribe 메서드를 호출해도 처리가 다시 시작되지 않는다.

<br />

### Flowable/Observable을 Cold에서 Hot으로 변환하는 연산자

#### publish

`publish()` 메서드는 원래 Cold인 Flowable/Observable에서 ConnectableFlowable/ConnectableObservable을 생성하는 연산자다. 이 메서드로 생성한 ConnectableFlowable/ConnectableObservable은 처리를 시작한 뒤에 구독하면 구독한 이후에 생성된 데이터부터 새로운 소비자에게 통지한다.

<br />

#### replay

`replay()` 메서드는 publish()처럼 원래 Cold인 Flowable/Observable에서 ConnectableFlowable/ConnectableObservable을 생성한다. 이 메서드로 생성한 ConnectableFlowable/ConnectableObservable은 통지한 데이터를 캐시하고, 처리를 시작한 뒤에 구독하면 캐시된 데이터를 먼저 새로 구독한 소비자에게 통지한다. 그 뒤에는 모든 소비자에게 같은 데이터를 통지한다. replay() 메서드는 인자가 없으면 모든 데이터를 캐시하고, 인자가 있으면 인자로 지정한 시간 동안 지정한 개수만큼의 데이터를 캐시한다.

<br />

#### share

`share()` 메서드는 여러 소비자가 구독할 수 있는 Flowable/Observable을 생성한다. 다른 메서드와 달리 ConnectableFlowable/ConnectableObservable을 생성하지 않는다. 이 share() 메서드에서 생성한 Flowable/Observable은 구독하는 소비자가 있는 동안은 도중에 새로 구독해도 같은 타임라인에서 생성되는 데이터를 통지한다. 즉, 실질적으로는 `flowable.publish().refCount()`와 같다.

<br />

<br />

## Rxjava의 전체 구성

### RxJava의 기본 구성

RxJava는 소비자(Subscriber/Observer)가 생산자(Flowable, Observable)를 구독하는 형태다. 이 생산자와 소비자의 관계는 Reactive Streams 사용을 지원하는 Flowable/Subscriber 구성과 지원하지 않는 Observable/Observer 구성으로 나눌 수 있다. 그리고 각각의 구성에는 통지하는 데이터의 생산자인 Flowable과 Observable, 통지받은 데이터의 소비자인 Subscriber와 Observer, 생산자와 소비자 사이에서 공유되는 Subscription과 Disposable이 있다.

<br />

#### Flowable/Observable

Flowable과 Observable은 데이터를 생성하고 통지하는 클래스이며, Flowable에는 배압 기능이 있고 Observable에는 없다는 차이가 있다. Flowable과 Observable은 기본적으로 Reactive Streams의 규칙과 그 규칙에 영향을 준 Observable 규약을 따르지 않으면 데이터가 문제 없이 통지되는 것을 보장받지 못 한다.

<br />

##### 통지 시 규칙

- null을 통지하면 안 된다.
- 데이터 통지는 해도 되고, 안 해도 된다.
- Flowable/Observable의 처리를 끝낼 때는 완료나 에러 통지를 해야 하며, 둘 다 통지하지는 않는다.
- 완료나 에러 통지를 한 뒤에는 다른 통지를 해서는 안 된다.
- 통지할 때는 1건씩 순차적으로 통지하며, 동시에 통지하면 안 된다.

<br />

#### Subscriber/Observer

Subscriber와 Observer는 통지된 데이터를 전달받아 이 데이터를 처리하는 인터페이스다. Subscriber와 Observer의 메서드가 호출되는 정상적인 순서는 다음과 같다.

- onSubscribe() 메서드
- onNext() 메서드
- onComplete() 메서드

먼저 Subscriber/Observer가 구독한 Flowable/Observable의 통지가 준비되면 `onSubscribe()` 메서드가 호출되는데, onSubscribe() 메서드는 1건의 구독에서 한 번만 호출된다.

다음으로 데이터가 통지될 때마다 `onNext()` 메서드가 호출된다. 여러 데이터가 통지되면 그만큼 onNext() 메서드가 호출되며 1건도 데이터가 통지되지 않으면 onNext() 메서드는 호출되지 않는다.

그리고 모든 데이터를 통지한 뒤에 `onComplete()` 메서드를 호출해 완료를 통지한다. 이것은 데이터 스트림을 처리할 때 중요한데, 이벤트 처리의 구독을 시작한 시점에서는 해당 Flowable/Observable이 끝나지 않고 계속해서 데이터를 통지하는지 판단할 수 없다. 그래서 모든 데이터를 통지한 뒤에 더 이상 데이터가 없음을 알리고자 완료를 통지하며 이때 완료를 통지할 때의 처리를 할 수 있다. onComplete() 메서드도 1건의 구독에서 한 번만 실행되며, onComplete() 메서드가 실행되면 해당 구독은 그대로 종료된다.

마지막으로 Flowable/Observable 처리 중에 에러가 발생하면 `onError()` 메서드가 호출돼 바로 처리가 종료된다.

<br />

원래 각 메서드는 하나씩 순서대로 실행되지만, Subscriber의 onSubscribe() 메서드만 예외적으로 처리 도중에 onNext() 메서드나 onComplete() 메서드가 실행된다. 이것은 onSubscribe() 메서드에서 Subscription의 request() 메서드를 호출하면 Flowable이 데이터 통지를 시작하기 때문이다.

<br />

#### Subscription

Subscription은 Reactive Streams에 정의된 인터페이스로, 통지 데이터 개수를 요청하는 request() 메서드와 처리 도중에도 구독을 해지하는 cancel() 메서드를 포함한다. Subscription은 Subscriber의 onSubscribe() 메서드의 인자로 전달되는 객체며, 설정한 수만큼 데이터를 통지하게 Flowable에 요청하거나 구독을 해지하는 기능이 있다.

```java
public interface Subscription {
    public void request(long n);
    public void cancel();
}
```

<br />

#### Disposable

Disposable은 구독을 해지하는 메서드를 포함한 인터페이스다. Observable과 Observer 간 구독에서 Observable이 구독 준비가 되면 onSubscribe() 메서드를 통해 Observer에 전달되는 객체며, 구독 처리 중에도 구독을 해지하는 기능이 있다.

```java
public interface Disposable {
    void dispose();
    boolean isDisposed();
}
```

<br />

또한 Disposable은 구독 해지뿐만 아니라 자원을 해제하는 등의 처리에도 활용할 수 있다. 따라서 처리가 끝날 때 자원을 해제해야 한다면 해제 처리를 Disposable의 dispose() 메서드에 구현한다. 그리고 FlowableEmitter/ObservableEmitter의 setDisposable 메서드에 이 Disposable을 설정하면 완료나 에러, 구독 해지 시에 Disposable의 dispose() 메서드가 호출된다. ResourceSubscriber/ResourceObserver의 add() 메서드로 Disposable을 추가하면 해당 ResourceSubscriber/ResourceObserver의 dispose() 호출 시 Disposable의 dispose() 메서드가 호출된다.

<br />

#### FlowableProcessor/Subject

Processor는 Reactive Streams에 정의된 생산자와 소비자의 기능이 모두 있는 인터페이스다. 이 인터페이스는 Publisher와 Subscriber를 모두 상속받으며, 다른 세머드는 가지고 있지 않다.

```java
public abstract interface Processor<T, R> extends Subscriber<T>, Publisher<R> { }
```

<br />

RxJava는 이 Reactive Streams의 Processor 구현 클래스로 FlowableProcessor를 제공한다. 그리고 Observable과 Observer의 구성에서 Processor와 같은 역할을 하는 Subject 인터페이스를  제공한다.

<br />

#### DisposableSubscriber/DisposableObserver

DisposableSubscriber/DisposableObserver는 Disposable을 구현한 Subscriber/Observer의 구현 클래스로, 외부에서 비동기로 구독 해지 메서드를 호출해도 안전하게 구독 해지를 하게 한다. 이 클래스에는 onSubscribe() 메서드가 final 메서드로 구현되어 있으며, onSubscribe() 메서드로 전달되는 Subscription/Disposable은 직접 접근하지 못하게 은닉돼 있다.

<br />

##### DisposableSubscriber의 Subsciprion 메서드를 호출하는 메서드

- **request(long)** : Subscription의 request() 메서드 호출
- **dispose()** : Subscription의 dispose() 메서드 호출

##### DisposableObserver의 Dispose 메서드를 호출하는 메서드

- **dispose()** : Dispose의 dispose() 메서드 호출
- **isDisposed()** : Dispose의 isDisposed() 메서드 호출 

<br />

구독 시작 시점에 원하는 처리를 실행하려면 onSubscribe() 메서드 내에서 호출되는 onStart() 메서드를 오버라이드 해 구현하면 된다.

<br />

#### ResourceSubscriber/ResourceObserver

ResourceSubscriber/ResourceObserver도 DisposableSubscriber/DisposableObserver와 마찬가지로 Disposable을 구현한 Subscriber/Observer의 구현 클래스이며, 외부에서 비동기로 구독 해지 메서드를 호출하더라도 안전하게 구독 해지를 하게 한다. 그리고 DisposableSubscriber/DisposableObserver와 같은 기능 외에도 다른 Disposable을 보관할 수도 있게 add() 메서드를 제공한다. add() 메서드로 보관된 Disposable의 dispose() 메서드는 ResourceSubscriber/ResourceObserver의 dispose() 메서드가 호출되면 함께 호출된다. 하지만 완료나 에러가 발생할 때는 자동으로 호출되지 않으니 주의해야 한다.

<br />

#### subscribe/subscribeWith

subscribe()는 소비자가 생산자를 구독하는 메서드로, 이 메서드를 호출하면 생산자가 데이터를 통지할 소비자를 등록한다. 생산자가 Cold일 때 subscibe 메서드를 호출하면 생산자는 바로 통지 처리를 시작한다. 구독 과정에서 통지 처리는 보통 다음 세 단계로 이루어진다.

1. 구독 시작 시 초기화 처리(onSubscribe)
2. 데이터 통지 처리(onNext)
3. 통지 종료 처리(onComplete/onError)

이러한 순서는 Subscriber/Observer 내에서 동기화돼야 하며, 순서대로 실행된다는 전제가 있다. 따라서 생산자의 처리와 소비자의 처리가 비동기로 이루어졌다고 해도 여러 데이터를 동시에 실행하는 일은 없다.

<br />

subscribeWith() 메서드는 Subscriber/Observer를 넘겨 주면 내부에서 이 Subscriber/Observer를 subscribe() 메서드에 전달해 실행하고 인자로 전달받은 Subscriber/Observer를 반환한다.

```java
public final <E extends Subscriber<? super T>> E subscribeWith(E subscriber) {
    subscribe(subscriber);
    return subscriber;
}
```

<br />

#### CompositeDisposable

CompositeDisposable은 여러 Disposable을 모아 CompositeDisposable의 dispose() 메서드를 호출함으로써 가지고 있는 모든 Disposable의 dispose() 메서드를 호출할 수 있는 클래스다.

```java
public static void main(String[] args) throws Exception {
        // Disposable을 합친다.
        CompositeDisposable compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(
                Flowable.range(1, 3)
                        .doOnCancel(() -> System.out.println("No.1 canceled"))
                        .observeOn(Schedulers.computation())
                .subscribe(data -> {
                    Thread.sleep(100L);
                    System.out.println("No.1 : " + data);
                })
        );

        compositeDisposable.add(
                Flowable.range(1, 3)
                .doOnCancel(() -> System.out.println("No.2 canceled"))
                .observeOn(Schedulers.computation())
                .subscribe(data -> {
                    Thread.sleep(100L);
                    System.out.println("No.2 : " + data);
                })
        );

        Thread.sleep(150L);

        // 한번에 구독을 해지한다.
        compositeDisposable.dispose();
    }
```

<br />

### Single/Maybe/Completable

Single은 통지할 데이터가 반드시 1건 있을 때, Maybe는 데이터가 없거나 있다면 반드시 1건일 때, Completable은 데이터는 통지하지 않고 완료만 통지한다. Maybe는 데이터가 있으면 해당 데이터를 통지하고 처리를 종료하며, 데이터가 없으면 완료를 통지하고 처리를 종료한다. 또한, 이 클래스들은 처리 중에 에러가 발생하면 에러를 통지한다.