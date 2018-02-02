package io.spring.oauth2.common.config;

/**
 * OAuth 2 defines four grant types, each of which is useful in different cases
 * <ul>
 * <li>Authorization: Code for apps running on a web server</li>
 * <li>Implicit: for browser-based or mobile apps</li>
 * <li>Password: for logging in with a username and password</li>
 * <li>Client credentials: for application access</li>
 * </ul>
 */
public class AuthorizedGrantTypes {

    /**
     * Web server apps are the most common type of application you encounter when dealing with OAuth servers. Web apps
     * are written in a server-side language and run on a server where the source code of the application is not
     * available to the public.
     * <p>
     * Create a "Log In" link sending the user to:<br/>
     * https://oauth2server.com/auth?response_type=code&client_id=CLIENT_ID&redirect_uri=REDIRECT_URI&scope=photos
     * <p>
     * The service redirects the user back to your site with an auth code:<br/>
     * https://oauth2client.com/cb?code=AUTH_CODE_HERE
     * <p>
     * Your server exchanges the auth code for an access token<br/>
     * POST https://api.oauth2server.com/token<br/>
     * grant_type=authorization_code&<br/>
     * code=AUTH_CODE_HERE&<br/>
     * redirect_uri=REDIRECT_URI&<br/>
     * client_id=CLIENT_ID&<br/>
     * client_secret=CLIENT_SECRET<br/>
     * <p>
     * The server replies with an access token
     * <p>
     * {"access_token":"RsT5OjbzRn430zqMLgV3Ia"}<br/>
     * or if there was an error<br/>
     * {"error":"invalid_request" }
     * <p>
     * Security: Note that the service must require apps to pre-register their redirect URIs.
     */
    public static final String AUTHORIZATION_CODE = "authorization_code";

    /**
     * Refresh Token grant type, will generate a refresh_token to be able to refresh the validity of your token by
     * providing a new token
     * <p>
     * call goes as follows :
     * <p>
     * POST https://oauthServer/oauth/token Authorization:"Basic ZXNzYmFja2VuZDpteVNlY3JldE9BdXRoU2VjcmV0" Content-Type:"application/x-www-form-urlencoded" Accept:"application/json"
     * <p>
     * refresh_token=a0c8ecb7-198f-4a55-bdbb-f18f376c55bc&<br/>
     * grant_type=refresh_token&<br/>
     * scope=read%20write&<br/>
     * client_secret=mySecretOAuthSecret&<br/>
     * client_id=essbackend& <br/>
     */
    public static final String REFRESH_TOKEN = "refresh_token";

    /**
     * Obtain an access token via a username, password login
     * <p>
     * POST localhost:8080/oauth/token Content-Type:"application/x-www-form-urlencoded" Accept:"application/json" Authorization:"Basic ZXNzYmFja2VuZDpteVNlY3JldE9BdXRoU2VjcmV0"<br/>
     * username=admin&<br/>
     * password=admin&<br/>
     * grant_type=password&<br/>
     * scope=read%20write&<br/>
     * client_secret=mySecretOAuthSecret&<br/>
     * client_id=essbackend&<br/>
     */
    public static final String PASSWORD = "password";

    /**
     * Implicit: used with Mobile Apps or Web Applications (applications that run on the user's device)
     */
    public static final String IMPLICIT = "implicit";

    public static final String CLIENT_CREDENTIALS = "client-credentials";

    private AuthorizedGrantTypes() {
    }
}
