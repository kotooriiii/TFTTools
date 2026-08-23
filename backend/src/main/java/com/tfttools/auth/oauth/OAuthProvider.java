package com.tfttools.auth.oauth;

import com.tfttools.auth.exception.OAuthAuthenticationException;

/**
 * One implementation per external identity provider (Google, and any added later).
 * {@link com.tfttools.auth.service.AuthService} depends only on this interface, so adding
 * a provider means adding an implementation - no changes to the service, controller, or
 * {@link com.tfttools.auth.domain.User} schema (oauthProvider/oauthSubjectId are already
 * provider-agnostic).
 */
public interface OAuthProvider
{
    /**
     * @return the key this provider is addressed by, e.g. in {@code POST /auth/oauth/{provider}}
     * and in {@code User.oauthProvider}.
     */
    String getProviderKey();

    /**
     * Exchanges an authorization code for the signed-in user's identity.
     *
     * @throws OAuthAuthenticationException if the code is invalid/expired, the exchange with
     *                                       the provider fails, or the returned identity token
     *                                       cannot be verified
     */
    OAuthUserInfo exchangeCodeForUserInfo(String code);
}
