package graphqla;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import graphql.GraphQL;
import graphql.annotations.processor.GraphQLAnnotations;
import graphql.schema.GraphQLSchema;
import graphqla.mutation.Mutation;
import graphqla.query.Query;
import graphqla.subscription.OperationMessage;
import graphqla.subscription.Subscription;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static graphql.schema.GraphQLSchema.newSchema;
import static graphqla.subscription.OperationMessage.*;

@Component
public class SocketHandler extends TextWebSocketHandler implements SubProtocolCapable {
    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private HashMap<WebSocketSession, List<String>> subscriptionMap = new HashMap<>();
    private final AtomicReference<org.reactivestreams.Subscription> subscriptionRef = new AtomicReference<>();
    private Gson gson = new Gson();
    private final String[] subProtocols = {"graphql-ws"};

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
        OperationMessage operationMessage = gson.fromJson(message.getPayload(), OperationMessage.class);
        switch (operationMessage.getType()) {
            case GQL_CONNECTION_TERMINATE:
                unsubscribe(session, operationMessage.getId());
                session.close();
                break;
            case GQL_START:
                subscribe(session, operationMessage.getId());
                GraphQLSchema schema = getGraphQLSchema();
                GraphQL graphql = GraphQL.newGraphQL(schema).build();
                ExecutionInput executionInput = gson.fromJson(operationMessage.getPayload(), ExecutionInput.class);
                ExecutionResult result = graphql.execute(executionInput);
                if (result.getData() instanceof Publisher) handlePublisher(session, result, operationMessage.getId());
                break;
            case GQL_STOP:
                unsubscribe(session, operationMessage.getId());
                break;
            case GQL_CONNECTION_INIT:
                session.sendMessage(new TextMessage(gson.toJson(new OperationMessage(GQL_CONNECTION_ACK))));
                break;
        }
    }

    private void subscribe(WebSocketSession session, String id) {
        List<String> ids = subscriptionMap.get(session);
        if (ids != null && !ids.contains(id)) {
            ids.add(id);
        } else {
            ArrayList<String> idsList = new ArrayList<>();
            idsList.add(id);
            subscriptionMap.put(session, idsList);
        }
    }

    private void unsubscribe(WebSocketSession session, String id) {
        List<String> ids = subscriptionMap.get(session);
        if (ids != null) {
            if (ids.contains(id)) {
                ids.remove(id);
            }
        }
    }

    private void handlePublisher(WebSocketSession session, ExecutionResult result, String id) {
        Publisher<ExecutionResult> stream = result.getData();
        Subscriber<ExecutionResult> subscriber = new Subscriber<ExecutionResult>() {
            @Override
            public void onSubscribe(org.reactivestreams.Subscription s) {
                List<String> subscribingIds = subscriptionMap.get(session);
                if (subscribingIds != null && subscribingIds.contains(id)) {
                    subscriptionRef.set(s);
                    try {
                        session.sendMessage(new TextMessage(gson.toJson(new OperationMessage(new JsonObject(),id, GQL_DATA))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    request(1);
                }
            }

            @Override
            public void onNext(ExecutionResult executionResult) {
                List<String> subscribingIds = subscriptionMap.get(session);
                if (subscribingIds != null && subscribingIds.contains(id)) {
                    try {
                        if (executionResult.getErrors().isEmpty())
                            session.sendMessage(new TextMessage(gson.toJson(new OperationMessage((JsonObject) gson.toJsonTree(executionResult), id, GQL_DATA))));
                        else
                            session.sendMessage(new TextMessage(gson.toJson(new OperationMessage((JsonObject) gson.toJsonTree(executionResult), id, GQL_ERROR))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    request(1);
                }
            }

            @Override
            public void onError(Throwable t) {
                try {
                    session.sendMessage(new TextMessage(gson.toJson(new OperationMessage((JsonObject) gson.toJsonTree(t), id, GQL_ERROR))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onComplete() {
                try {
                    session.sendMessage(new TextMessage(gson.toJson(new OperationMessage(id, GQL_COMPLETE))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        stream.subscribe(subscriber);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
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

    @Override
    public List<String> getSubProtocols() {
        return Arrays.asList(subProtocols);
    }
}
