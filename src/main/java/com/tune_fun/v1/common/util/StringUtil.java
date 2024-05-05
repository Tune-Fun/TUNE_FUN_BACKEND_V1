package com.tune_fun.v1.common.util;

import com.github.f4b6a3.ulid.UlidCreator;
import com.tune_fun.v1.common.constant.Constants;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.UUID;

import static java.lang.String.join;
import static java.util.regex.Pattern.matches;
import static java.util.stream.Collectors.joining;

@UtilityClass
public class StringUtil {

    private static SecureRandom secureRandom() throws NoSuchAlgorithmException {
        return SecureRandom.getInstanceStrong();
    }

    private static UniformRandomProvider randomProvider() {
        return RandomSource.SFC_64.create();
    }

    public String randomNumeric(final int count) throws NoSuchAlgorithmException {
        String characters = "123456789";
        return generateRandomString(count, characters.toCharArray());
    }

    public String randomNumeric(final int minLength, final int maxLength) throws NoSuchAlgorithmException {
        return randomNumeric(rangedRandom(minLength, maxLength));
    }

    public String randomAlphabetic(final int count) throws NoSuchAlgorithmException {
        return generateRandomString(count, true, false);
    }

    public String randomAlphabetic(final int minLength, final int maxLength) throws NoSuchAlgorithmException {
        return randomAlphabetic(rangedRandom(minLength, maxLength));
    }

    public String randomAlphanumeric(final int count) throws NoSuchAlgorithmException {
        return generateRandomString(count, true, true);
    }

    public String randomAlphanumeric(final int minLength, final int maxLength) throws NoSuchAlgorithmException {
        return randomAlphanumeric(rangedRandom(minLength, maxLength));
    }

    public String randomAlphaNumericSymbol(final int count) throws NoSuchAlgorithmException {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@$%^&*";
        return generateRandomString(count, characters.toCharArray());
    }

    public String randomAlphaNumericSymbol(final int minLength, final int maxLength) throws NoSuchAlgorithmException {
        return randomAlphaNumericSymbol(rangedRandom(minLength, maxLength));
    }

    private static int rangedRandom(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomProvider().nextInt(minLengthInclusive, maxLengthExclusive);
    }

    private String generateRandomString(final int count, final boolean letters, final boolean numbers) throws NoSuchAlgorithmException {
        return RandomStringUtils.random(count, 0, 0, letters, numbers, (char[]) Constants.NULL_OBJECT, secureRandom());
    }

    private String generateRandomString(final int count, char... chars) throws NoSuchAlgorithmException {
        return RandomStringUtils.random(count, 0, 0, true, true, chars, secureRandom());
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String ulid() {
        return UlidCreator.getMonotonicUlid().toString();
    }

    public boolean hasText(String text) {
        return StringUtils.hasText(text);
    }

    public static String concatWithColon(String... strings) {
        return join(Constants.COLON, strings);
    }

    public static String removeBearerPrefix(String accessTokenFromRequest) {
        return matches("^Bearer .*", accessTokenFromRequest) ?
                accessTokenFromRequest.substring(7) : Constants.NULL_STRING;
    }

    public static String getFlattenAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(Constants.COMMA));
    }

    public static String generateRandomNickname() throws NoSuchAlgorithmException {
        int animalIndex = secureRandom().nextInt(Constants.NicknameFragment.ANIMAL_NAMES.length);
        int prefixIndex = secureRandom().nextInt(Constants.NicknameFragment.PREFIX_NAMES.length);

        String animalName = Constants.NicknameFragment.ANIMAL_NAMES[animalIndex];
        String prefix = Constants.NicknameFragment.PREFIX_NAMES[prefixIndex];

        return prefix + animalName;
    }

}
