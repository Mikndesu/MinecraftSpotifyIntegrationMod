package com.github.mikndesu.spotify_integration.spotify;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

import com.github.mikndesu.spotify_integration.localserver.LocalServer;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

public class AuthorizationCodeUri {
    private static final String clientId = "f13e3abd0c1c4b7c8519fff8dda95dbf";
    private static final String clientSecret = "36fdebf1f7f6473a9d66ee7ccae0daa9";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8888/callback");

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder().setClientId(clientId).setClientSecret(clientSecret).setRedirectUri(redirectUri).build();
    private static final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri().build();
    public static String authorizationCodeUri_Async() {
        LocalServer localServer = LocalServer.getInstance();
        try {
            final CompletableFuture<URI> uriFuture = authorizationCodeUriRequest.executeAsync();
            final URI uri = uriFuture.join();
            System.out.println("URI: " + uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        localServer.stopServer();
        return localServer.getCode();
    }
}
