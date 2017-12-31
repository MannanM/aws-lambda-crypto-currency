package com.mannanlive.repository;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterDao {
    public void tweet(String message) {
        Configuration configuration = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(System.getenv("consumerKey"))
                .setOAuthConsumerSecret(System.getenv("consumerSecret"))
                .setOAuthAccessToken(System.getenv("accessToken"))
                .setOAuthAccessTokenSecret(System.getenv("accessTokenSecret"))
                .build();
        TwitterFactory tf = new TwitterFactory(configuration);
        Twitter twitter = tf.getInstance();
        try {
            twitter.updateStatus(message);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
