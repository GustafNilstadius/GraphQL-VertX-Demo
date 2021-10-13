package com.redpilllinpro.techtalk;

import com.redpilllinpro.techtalk.service.author.AuthorService;
import com.redpilllinpro.techtalk.service.author.AuthorServiceVeritcle;
import com.redpilllinpro.techtalk.service.book.BookService;
import com.redpilllinpro.techtalk.service.book.BookServiceVerticle;
import com.redpilllinpro.techtalk.service.graphql.GraphQLVerticle;
import com.redpilllinpro.techtalk.service.quote.QuoteService;
import com.redpilllinpro.techtalk.service.quote.QuoteServiceVerticle;

import io.vertx.core.*;

import java.util.LinkedList;
import java.util.List;


public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    List<Future> futureList = new LinkedList<>();

    Promise<String> startBookServicePromise = Promise.promise();
    vertx.deployVerticle(BookServiceVerticle.class.getName(),
            new DeploymentOptions().setInstances(3).setConfig(config()), startBookServicePromise);
    futureList.add(startBookServicePromise.future());


    Promise<String> startQuoteServicePromise = Promise.promise();
    vertx.deployVerticle(QuoteServiceVerticle.class.getName(),
            new DeploymentOptions().setInstances(3).setConfig(config()), startQuoteServicePromise);
    futureList.add(startQuoteServicePromise.future());

    Promise<String> startAuthorServicePromise = Promise.promise();
    vertx.deployVerticle(AuthorServiceVeritcle.class.getName(),
            new DeploymentOptions().setInstances(3).setConfig(config()), startAuthorServicePromise);
    futureList.add(startAuthorServicePromise.future());


    Promise<String> startGraphQLVerticlePromise = Promise.promise();
    vertx.deployVerticle(GraphQLVerticle.class.getName(),
            new DeploymentOptions().setInstances(1).setConfig(config()), startGraphQLVerticlePromise);
    futureList.add(startGraphQLVerticlePromise.future());

    CompositeFuture.join(futureList)
            .onSuccess(v -> startPromise.complete())
            .onFailure(t -> {
              t.printStackTrace();
              startPromise.fail(t);
            });
  }
}
