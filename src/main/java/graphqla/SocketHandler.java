package graphqla;

import com.google.gson.Gson;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.annotations.processor.GraphQLAnnotations;
import graphql.schema.GraphQLSchema;
import graphqla.mutation.Mutation;
import graphqla.query.Query;
import graphqla.subscription.Subscription;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static graphql.schema.GraphQLSchema.newSchema;

@Component
public class SocketHandler extends TextWebSocketHandler {
    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final AtomicReference<org.reactivestreams.Subscription> subscriptionRef = new AtomicReference<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
        GraphQLSchema schema = getGraphQLSchema();
        GraphQL graphql = GraphQL.newGraphQL(schema).build();
        ExecutionResult result = graphql.execute(message.getPayload(), this, new HashMap<>());
        if (result.getData() instanceof Publisher) handlePublisher(session, result);

        session.sendMessage(new TextMessage("Hello"));
    }

    private void handlePublisher(WebSocketSession session, ExecutionResult result) {
        Publisher<ExecutionResult> stream = result.getData();
        stream.subscribe(new Subscriber<ExecutionResult>() {

            @Override
            public void onSubscribe(org.reactivestreams.Subscription s) {
                subscriptionRef.set(s);
                request(1);
            }

            @Override
            public void onNext(ExecutionResult executionResult) {
                Object update = executionResult.getData();
                try {
                    session.sendMessage(new TextMessage(new Gson().toJson(update)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                request(1);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //the messages will be broadcasted to all users.
        sessions.add(session);
    }

    private GraphQLSchema getGraphQLSchema() {
        return newSchema().query(GraphQLAnnotations.object(Query.class))
                .mutation(GraphQLAnnotations.object(Mutation.class))
                .subscription(GraphQLAnnotations.object(Subscription.class))
                .build();
    }


    private void request(int n) {
        org.reactivestreams.Subscription subscription = subscriptionRef.get();
        if (subscription != null) {
            subscription.request(n);
        }
    }
}
