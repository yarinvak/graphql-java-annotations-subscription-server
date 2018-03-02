package graphql;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DogUpdatePublisher {
    private final Flowable<DogUpdate> publisher;

    public DogUpdatePublisher() {
        Observable<DogUpdate> stockPriceUpdateObservable = Observable.create(emitter -> {

            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(newTick(emitter), 0, 2, TimeUnit.SECONDS);

        });

        ConnectableObservable<DogUpdate> connectableObservable = stockPriceUpdateObservable.share().publish();
        connectableObservable.connect();

        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    private Runnable newTick(ObservableEmitter<DogUpdate> emitter) {
        return () -> {
            List<DogUpdate> stockPriceUpdates = new ArrayList<>();
            stockPriceUpdates.add(new DogUpdate("na1"));
            stockPriceUpdates.add(new DogUpdate("na2"));
            stockPriceUpdates.add(new DogUpdate("na3"));
            if (stockPriceUpdates != null) {
                emitUpdates(emitter, stockPriceUpdates);
            }
        };
    }

    private void emitUpdates(ObservableEmitter<DogUpdate> emitter, List<DogUpdate> stockPriceUpdates) {
        for (DogUpdate stockPriceUpdate : stockPriceUpdates) {
            try {
                emitter.onNext(stockPriceUpdate);
            } catch (RuntimeException rte) {
                rte.printStackTrace();
            }
        }
    }

    public Flowable<DogUpdate> getPublisher() {
        return publisher;
    }
}
