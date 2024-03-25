package com.tune_fun.v1.common.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.springframework.security.core.GrantedAuthority;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.UUID;

import static java.util.concurrent.ThreadLocalRandom.current;
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
        return RandomStringUtils.random(count, 0, 0, letters, numbers, null, secureRandom());
    }

    private String generateRandomString(final int count, char... chars) throws NoSuchAlgorithmException {
        return RandomStringUtils.random(count, 0, 0, true, true, chars, secureRandom());
    }

    public static String generateRandomNickname() throws NoSuchAlgorithmException {
        int animalIndex = secureRandom().nextInt(ANIMAL_NAMES.length);
        int prefixIndex = secureRandom().nextInt(PREFIX_NAMES.length);

        String animalName = ANIMAL_NAMES[animalIndex];
        String prefix = PREFIX_NAMES[prefixIndex];

        return prefix + animalName;
    }

    public String uuid() {
        return UUID.randomUUID().toString();
    }

    public boolean hasText(String text) {
        return org.springframework.util.StringUtils.hasText(text);
    }

    public String removeBearerPrefix(String accessTokenFromRequest) {
        return matches("^Bearer .*", accessTokenFromRequest) ? accessTokenFromRequest.substring(7) : null;
    }

    public static String getFlattenAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(","));
    }

    private static final String[] PREFIX_NAMES = {
            "행복한", "멋진", "멋있는", "귀여운", "예쁜", "잘생긴", "똑똑한", "졸린", "피곤한", "배고픈", "궁금한", "친절한", "평범한",
            "활발한", "조용한", "빠른", "느린", "재빠른", "강한", "영리한", "우아한", "독특한", "재밌는", "발랄한", "따뜻한", "눈부신",
            "명랑한", "빛나는", "깜찍한", "차분한", "이상한", "행운의"
    };

    private static final String[] ANIMAL_NAMES = {
            "사자", "호랑이", "표범", "기린", "코끼리", "코뿔소", "하마", "펭귄", "독수리", "타조", "캥거루", "고래", "칠면조", "직박구리",
            "청설모", "메추라기", "앵무새", "스라소니", "판다", "오소리", "오리", "거위", "백조", "두루미", "고슴도치", "두더지", "우파루파",
            "너구리", "카멜레온", "이구아나", "노루", "제비", "까치", "고라니", "수달", "당나귀", "순록", "염소", "공작", "바다표범", "들소",
            "참새", "물개", "바다사자", "얼룩말", "산양", "카피바라", "북극곰", "퓨마", "코요테", "라마", "딱따구리", "돌고래", "까마귀",
            "낙타", "여우", "사슴", "늑대", "재규어", "알파카", "다람쥐", "담비", "사막여우", "북극여우", "꽃사슴", "해달", "강아지",
            "고양이", "햄스터", "기니피그", "왈라비", "마못", "물범", "토끼", "미어캣", "북극곰", "코알라", "디프만"
    };

}
