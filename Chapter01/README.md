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

어떤 프로그램이 리액티브 프로그래밍인지는 무엇이 데이터를 처리를 수행하는가로 가릴 수 있다. 상품 가격이 변경될 때 리스터가 반응하면서 상품 가격에 해당하는 부가가치세를 다시 계산해 표시하는 것은 리액티브 프로그래밍이라고 할 수 없다.

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