/*
 Copyright (c) 2022 Mikndesu

 Permission is hereby granted, free of charge, to any person obtaining a copy of
 this software and associated documentation files (the "Software"), to deal in
 the Software without restriction, including without limitation the rights to
 use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 the Software, and to permit persons to whom the Software is furnished to do so,
 subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
