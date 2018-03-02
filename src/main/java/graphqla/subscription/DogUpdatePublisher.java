package graphqla.subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.observables.ConnectableObservable;
import org.springframework.stereotype.Component;

@Component
public class DogUpdatePublisher {
    private final Flowable<DogUpdate> publisher;

    public static ObservableList<DogUpdate> observableList = new ObservableList<>();

    private static final DogUpdatePublisher instance = new DogUpdatePublisher();

    public static DogUpdatePublisher getInstance(){
        return instance;
    }

    private DogUpdatePublisher(){
        ConnectableObservable<DogUpdate> connectableObservable = observableList.getObservable().share().publish();
        connectableObservable.connect();

        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    public Flowable<DogUpdate> getPublisher() {
        return publisher;
    }
}
