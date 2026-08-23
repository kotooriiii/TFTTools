package com.tfttools.auth.oauth;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.tfttools.auth.exception.OAuthAuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
public class GoogleOAuthProvider implements OAuthProvider
{
    private static final String PROVIDER_KEY = "google";

    /**
     * Google's reserved redirect_uri value for the JS popup authorization-code flow
     * (google.accounts.oauth2.initCodeClient / useGoogleLogin({flow: 'auth-code'})) - the code
     * is returned to the popup opener via postMessage rather than an actual HTTP redirect, so
     * no real URI needs to be registered in the Google Cloud console for this flow.
     */
    private static final String POPUP_REDIRECT_URI = "postmessage";

    private final String clientId;
    private final String clientSecret;
    private final HttpTransport httpTransport;
    private final GsonFactory jsonFactory;
    private final GoogleIdTokenVerifier idTokenVerifier;

    public GoogleOAuthProvider(@Value("${google.oauth.client-id}") String clientId,
                                @Value("${google.oauth.client-secret}") String clientSecret)
    {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.jsonFactory = GsonFactory.getDefaultInstance();
        try
        {
            this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        }
        catch (GeneralSecurityException | IOException e)
        {
            throw new IllegalStateException("Failed to initialize Google HTTP transport", e);
        }
        this.idTokenVerifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    @Override
    public String getProviderKey()
    {
        return PROVIDER_KEY;
    }

    @Override
    public OAuthUserInfo exchangeCodeForUserInfo(String code)
    {
        GoogleTokenResponse tokenResponse;
        try
        {
            tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    httpTransport,
                    jsonFactory,
                    clientId,
                    clientSecret,
                    code,
                    POPUP_REDIRECT_URI
            ).execute();
        }
        catch (IOException e)
        {
            throw new OAuthAuthenticationException("Failed to exchange authorization code with Google");
        }

        GoogleIdToken idToken;
        try
        {
            idToken = tokenResponse.parseIdToken();
        }
        catch (IOException e)
        {
            throw new OAuthAuthenticationException("Failed to parse Google ID token");
        }

        if (idToken == null || !verify(idToken))
        {
            throw new OAuthAuthenticationException("Google ID token failed verification");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        return new OAuthUserInfo(
                payload.getSubject(),
                payload.getEmail(),
                Boolean.TRUE.equals(payload.getEmailVerified()),
                (String) payload.get("name")
        );
    }

    private boolean verify(GoogleIdToken idToken)
    {
        try
        {
            return idTokenVerifier.verify(idToken);
        }
        catch (GeneralSecurityException | IOException e)
        {
            throw new OAuthAuthenticationException("Failed to verify Google ID token");
        }
    }
}
