package study.security_nopassword.config.secret;

public class Secret {
    public static String AT_JWT_KEY = "doroseeLxAccessToken";
    public static String RT_JWT_KEY = "doroseeLxRefreshToken";
    public static Long AT_EXPRIRATION_TIME = 1000L * 60 * 60 * 24 * 7;
    public static Long RT_EXPRIRATION_TIME = 1000L * 60 * 60 * 24 * 30;
    public static String TOKEN_PREFIX = "Bearer ";
    public static String HEADER_STRING = "Authorization";
    public static String AT_HEADER_STRING = "ACCESS_TOKEN";
    public static String RT_HEADER_STRING = "REFRESH_TOKEN";

}
