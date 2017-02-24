package de.web.f_reissmann.connection;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility-Class used for creating a {@link StringEntity} from the given username/password.
 * <p>
 * Which then can be used for HTTP-POST requests.
 *
 * @author Fabian Reißmann
 * @since 19.02.2017
 */
final class SaveTvCredentialsFormatter {

    private static final Charset USED_CHARSET = StandardCharsets.UTF_8;
    private static final String SAVE_TV_USER_PARAMETER = "sUsername";
    private static final String SAVE_TV_PW_PARAMETER = "sPassword";

    private SaveTvCredentialsFormatter() {
        throw new UnsupportedOperationException("Utility-Class should not be instantiated.");
    }

    /**
     * Creates a {@link StringEntity} from the given credentials.
     *
     * @param username the username
     * @param password the password
     * @return a {@link StringEntity}
     */
    static StringEntity toFormEntity(String username, String password) {
        List<NameValuePair> formValues = new ArrayList<>();

        formValues.add(new BasicNameValuePair(SAVE_TV_USER_PARAMETER, username));
        formValues.add(new BasicNameValuePair(SAVE_TV_PW_PARAMETER, password));

        return new UrlEncodedFormEntity(formValues, USED_CHARSET);
    }
}