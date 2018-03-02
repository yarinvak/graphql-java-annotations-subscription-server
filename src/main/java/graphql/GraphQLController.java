package graphql;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import graphql.annotations.processor.GraphQLAnnotations;
import graphql.schema.GraphQLSchema;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import static graphql.schema.GraphQLSchema.newSchema;

@Controller
public class GraphQLController {
    private GraphQLSchema schema = getGraphQLSchema();
    @Autowired
    private SimpMessagingTemplate template;
    private final AtomicReference<org.reactivestreams.Subscription> subscriptionRef = new AtomicReference<>();

    @RequestMapping(value = "/graphql", method = RequestMethod.POST)
    public Object index(@RequestBody GraphQLRequest graphQLRequest) {
        GraphQL graphql = GraphQL.newGraphQL(schema).build();
        ExecutionResult result = graphql.execute(graphQLRequest.getQuery(), this, new HashMap<>());

        if (result.getData() instanceof Publisher) handlePublisher(result);


        return result;
    }

    @MessageMapping("/subscription")
    @SendTo("/topic/messages")
    public JsonElement send(String message)
            throws Exception {
//        GraphQL graphql = GraphQL.newGraphQL(schema).build();
//        ExecutionResult result = graphql.execute(query, this, new HashMap<>());
//
//        if (result.getData() instanceof Publisher) handlePublisher(result);


        return new Gson().toJsonTree(message);
    }


    private void handlePublisher(ExecutionResult result) {
        Publisher<ExecutionResult> stockPriceStream = result.getData();

        stockPriceStream.subscribe(new Subscriber<ExecutionResult>() {

            @Override
            public void onSubscribe(org.reactivestreams.Subscription s) {
                subscriptionRef.set(s);
                request(1);
            }

            @Override
            public void onNext(ExecutionResult er) {
                Object update = er.getData();
                template.convertAndSend("/topic/messages", new Gson().toJsonTree(update));
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

    private void request(int n) {
        org.reactivestreams.Subscription subscription = subscriptionRef.get();
        if (subscription != null) {
            subscription.request(n);
        }
    }


//    @Scheduled(fixedRate = 5000)
//    public void subTask() {
//        System.out.println("scheduled");
//        this.template.convertAndSend("/topic/greetings", "Hello");
//    }


    private static GraphQLSchema getGraphQLSchema() {
        return newSchema().query(GraphQLAnnotations.object(Query.class))
                .mutation(GraphQLAnnotations.object(Mutation.class))
                .subscription(GraphQLAnnotations.object(Subscription.class))
                .build();
    }

}
