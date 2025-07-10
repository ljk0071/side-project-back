package com.side.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class KoreanJosaUtil {

    private static final int HANGUL_BASE = 0xAC00;
    private static final int HANGUL_LAST = 0xD7A3;
    private static final int JONGSUNG_COUNT = 28;

    // 한글 이외의 문자에 대한 받침 규칙
    private static final Set<Character> FINAL_CONSONANT_CHARS = Set.of(
            'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    );

    // 영어 알파벳에 대한 받침 규칙
    private static final Set<Character> ENGLISH_FINAL_CONSONANTS = Set.of(
            'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z',
            'B', 'C', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Z'
    );

    // 숫자에 대한 받침 규칙
    private static final Set<Character> NUMBER_FINAL_CONSONANTS = Set.of(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    );

    // 일반적인 조사 패턴
    private static final Map<String, List<String>> COMMON_JOSA_PATTERNS = Map.of(
            "은/는", List.of("은", "는"),
            "이/가", List.of("이", "가"),
            "을/를", List.of("을", "를"),
            "과/와", List.of("과", "와"),
            "아/야", List.of("아", "야"),
            "으로/로", List.of("으로", "로"),
            "이다/다", List.of("이다", "다"),
            "이었/였", List.of("이었", "였"),
            "이야/야", List.of("이야", "야")
    );

    /**
     * 단어와 조사 후보 문자열을 받아 적절한 조사를 붙여 반환하는 메서드.
     *
     * @param word          검사할 단어
     * @param josaCandidate 조사 후보 문자열 (예: "이/가")
     * @return 단어 + 올바른 조사
     */
    public static String attachJosa(String word, String josaCandidate) {
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("단어를 입력해주세요.");
        }

        if (josaCandidate == null || josaCandidate.trim().isEmpty()) {
            throw new IllegalArgumentException("조사 후보를 입력해주세요.");
        }

        JosaPattern pattern = parseJosaPattern(josaCandidate);
        String selectedJosa = selectJosa(word, pattern);

        return word + selectedJosa;
    }

    /**
     * 단어에 대해 받침 여부를 확인하는 메서드.
     *
     * @param word 검사할 단어
     * @return 받침이 있으면 true, 그렇지 않으면 false
     */
    public static boolean hasFinalConsonant(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }

        char lastChar = word.charAt(word.length() - 1);
        return hasFinalConsonant(lastChar);
    }

    /**
     * 조사 패턴을 파싱하는 메서드.
     *
     * @param josaCandidate 조사 후보 문자열
     * @return 파싱된 조사 패턴
     */
    private static JosaPattern parseJosaPattern(String josaCandidate) {
        String trimmedCandidate = josaCandidate.trim();

        // 일반적인 조사 패턴 확인
        if (COMMON_JOSA_PATTERNS.containsKey(trimmedCandidate)) {
            List<String> josaList = COMMON_JOSA_PATTERNS.get(trimmedCandidate);
            return new JosaPattern(josaList.get(0), josaList.get(1));
        }

        // 사용자 정의 패턴 파싱
        if (!trimmedCandidate.contains("/")) {
            throw new IllegalArgumentException("조사 후보는 'A/B' 형식이어야 합니다.");
        }

        String[] candidates = trimmedCandidate.split("/");
        if (candidates.length != 2) {
            throw new IllegalArgumentException("조사 후보는 'A/B' 형식이어야 합니다.");
        }

        return new JosaPattern(candidates[0].trim(), candidates[1].trim());
    }

    /**
     * 단어와 조사 패턴을 기반으로 적절한 조사를 선택하는 메서드.
     *
     * @param word    검사할 단어
     * @param pattern 조사 패턴
     * @return 선택된 조사
     */
    private static String selectJosa(String word, JosaPattern pattern) {
        boolean hasConsonant = hasFinalConsonant(word.charAt(word.length() - 1));
        return hasConsonant ? pattern.withConsonant() : pattern.withoutConsonant();
    }

    /**
     * 주어진 문자가 받침이 있는지 판별하는 메서드.
     *
     * @param ch 판별할 문자
     * @return 받침이 있으면 true, 그렇지 않으면 false
     */
    private static boolean hasFinalConsonant(char ch) {
        return isHangulWithFinalConsonant(ch) ||
                FINAL_CONSONANT_CHARS.contains(ch) ||
                ENGLISH_FINAL_CONSONANTS.contains(ch) ||
                isNumberWithFinalConsonant(ch);
    }

    /**
     * 한글 음절에 받침이 있는지 판별하는 메서드.
     *
     * @param ch 판별할 문자
     * @return 받침이 있으면 true, 그렇지 않으면 false
     */
    private static boolean isHangulWithFinalConsonant(char ch) {
        if (ch < HANGUL_BASE || ch > HANGUL_LAST) {
            return false;
        }
        int offset = ch - HANGUL_BASE;
        int jong = offset % JONGSUNG_COUNT;
        return jong != 0;
    }

    /**
     * 숫자에 받침이 있는지 판별하는 메서드.
     *
     * @param ch 판별할 문자
     * @return 받침이 있으면 true, 그렇지 않으면 false
     */
    private static boolean isNumberWithFinalConsonant(char ch) {
        return switch (ch) {
            case '0', '1', '2', '6' -> true;  // 공, 일, 이, 육
            case '3', '4', '5', '7', '8', '9' -> false;  // 삼, 사, 오, 칠, 팔, 구
            default -> NUMBER_FINAL_CONSONANTS.contains(ch);
        };
    }

    /**
     * 조사 패턴을 나타내는 레코드.
     */
    private record JosaPattern(String withConsonant, String withoutConsonant) {
    }

    /**
     * 다양한 조사 패턴을 위한 빌더 클래스.
     */
    public static class JosaBuilder {
        private final String word;

        private JosaBuilder(String word) {
            this.word = word;
        }

        public static JosaBuilder of(String word) {
            return new JosaBuilder(word);
        }

        public String eun() {
            return attachJosa(word, "은/는");
        }

        public String i() {
            return attachJosa(word, "이/가");
        }

        public String eul() {
            return attachJosa(word, "을/를");
        }

        public String gwa() {
            return attachJosa(word, "과/와");
        }

        public String euro() {
            return attachJosa(word, "으로/로");
        }

        public String a() {
            return attachJosa(word, "아/야");
        }

        public String ida() {
            return attachJosa(word, "이다/다");
        }

        public String ieot() {
            return attachJosa(word, "이었/였");
        }

        public String custom(String josaPattern) {
            return attachJosa(word, josaPattern);
        }
    }
}
