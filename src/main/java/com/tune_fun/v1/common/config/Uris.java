package com.tune_fun.v1.common.config;

public class Uris {

    public static final String API_V1_ROOT = "/v1";

    public static final String REST_NAME_ID = "/{id}";
    public static final String REST_NAME_UUID = "/{uuid}";

    public static final String CHECK_USERNAME_DUPLICATE = API_V1_ROOT + "/check-username-duplicate";
    public static final String CHECK_EMAIL_DUPLICATE = API_V1_ROOT + "/check-email-duplicate";
    public static final String FIND_USERNAME = API_V1_ROOT + "/find-username";
    public static final String REGISTER = API_V1_ROOT + "/register";
    public static final String LOGIN = API_V1_ROOT + "/login";
    public static final String LOGIN_GOOGLE = API_V1_ROOT + "/login/google";
    public static final String LOGIN_APPLE = API_V1_ROOT + "/login/apple";
    public static final String LOGIN_INSTAGRAM = API_V1_ROOT + "/login/instagram";
    public static final String LOGOUT = API_V1_ROOT + "/logout";
    public static final String REFRESH = API_V1_ROOT + "/refresh";
    public static final String SET_NEW_PASSWORD = API_V1_ROOT + "/set-new-password";
    public static final String UPDATE_NICKNAME = API_V1_ROOT + "/update-nickname";

    public static final String FORGOT_PASSWORD_SEND_OTP = API_V1_ROOT + "/forgot-password/send-otp";

    public static final String CHECK_EMAIL_VERIFIED = API_V1_ROOT + "/check-email-verified";

    public static final String VERIFY_OTP = API_V1_ROOT + "/otp/verify";
    public static final String VERIFY_OTP_WITH_TOKEN = API_V1_ROOT + "/otp/verify-with-token";
    public static final String RESEND_OTP = API_V1_ROOT + "/otp/resend";

    public static final String REGISTER_VOTE = API_V1_ROOT + "/vote/register";

    public static final String SWAGGER_UI_ROOT = "/swagger-ui";
    public static final String SWAGGER_UI = "/swagger-ui.html";
    public static final String SWAGGER_DOCS = "/docs/com.tune_fun-open-api-3.0.1.json";

    public static final String SWAGGER_INDEX_HTML = SWAGGER_UI_ROOT + "/index.html";
    public static final String SWAGGER_INDEX_CSS = SWAGGER_UI_ROOT + "/index.css";
    public static final String SWAGGER_BUNDLE_JS = SWAGGER_UI_ROOT + "/swagger-ui-bundle.js";
    public static final String SWAGGER_CSS = SWAGGER_UI_ROOT + "/swagger-ui.css";
    public static final String SWAGGER_STANDALONE_PRESET_JS = SWAGGER_UI_ROOT + "/swagger-ui-standalone-preset.js";
    public static final String SWAGGER_INITIALIZER_JS = SWAGGER_UI_ROOT + "/swagger-initializer.js";
    public static final String SWAGGER_FAVICON = SWAGGER_UI_ROOT + "/favicon-32x32.png";

    public static final String SWAGGER_API_DOCS = "/v3/api-docs";
    public static final String SWAGGER_API_DOCS_SWAGGER_CONFIG = SWAGGER_API_DOCS + "/swagger-config";
    public static final String SWAGGER_API_DOCS_JSON = "/docs/com.tune_fun-open-api-3.0.1.json";

    public static final String API_DOC_HTML = "/static/docs/index.html";
    public static final String FAVICON = "/favicon.ico";
    public static final String HEALTH_CHECK = "/health-check";

    public static final String[] NOT_LOGGING_URIS = {
            SWAGGER_UI,
            SWAGGER_INDEX_HTML,
            SWAGGER_INDEX_CSS,
            SWAGGER_BUNDLE_JS,
            SWAGGER_CSS,
            SWAGGER_STANDALONE_PRESET_JS,
            SWAGGER_INITIALIZER_JS,
            SWAGGER_API_DOCS,
            SWAGGER_API_DOCS_SWAGGER_CONFIG,
            SWAGGER_FAVICON,
            SWAGGER_API_DOCS_JSON,
            API_DOC_HTML,
            FAVICON,
            HEALTH_CHECK
    };
    public static final String[] PERMIT_ALL_URIS = {
            CHECK_USERNAME_DUPLICATE,
            CHECK_EMAIL_DUPLICATE,
            FIND_USERNAME,
            REGISTER,
            LOGIN,
            REFRESH,
            FORGOT_PASSWORD_SEND_OTP,
            RESEND_OTP,
            VERIFY_OTP,
            SWAGGER_UI,
            SWAGGER_INDEX_HTML,
            SWAGGER_DOCS,
            SWAGGER_INDEX_CSS,
            SWAGGER_BUNDLE_JS,
            SWAGGER_CSS,
            SWAGGER_STANDALONE_PRESET_JS,
            SWAGGER_INITIALIZER_JS,
            SWAGGER_API_DOCS,
            SWAGGER_API_DOCS_SWAGGER_CONFIG,
            SWAGGER_FAVICON,
            SWAGGER_API_DOCS_JSON,
            API_DOC_HTML,
            FAVICON,
            HEALTH_CHECK
    };
    public static final String[] ADMIN_URIS = {

    };


}
