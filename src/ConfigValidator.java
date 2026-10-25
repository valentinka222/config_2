import java.io.File;
import java.util.Map;

public class ConfigValidator {
    private final static int MIN_DEPTH = 1;
    private final static String PACKAGE_PATTERN = "[a-z0-9][a-z0-9.+-]+";
    private final static String TEST_PACKAGE_PATTERN = "[A-Z]+";
    private final static String VERSION_PATTERN = "(\\d+:)?[0-9][A-Za-z0-9.+~-]*";
    private final static String FILTER_PATTERN = "\\S*";
    private final static String OUTPUT_PATTERN = "[A-Za-z0-9._-]+\\.png";
    private final static String URL_PATTERN = "https?://\\S+";

    /**
     * Превращает текст в логическое значение
     * @param name имя параметра
     * @param text текст из конфигурации
     * @return true/false
     * @throws ConfigException если текст не true/false
     */
    static boolean parseBoolean(String name, String text) throws ConfigException {
        if (text.equals("true")) {
            return true;
        }
        if (text.equals("false")) {
            return false;
        }
        throw new ConfigException("Параметр <" + name + "> должен быть true/false, а не '" + text + "'");
    }

    /**
     * Превращает текст в максимальную глубину
     * @param text текст из конфигурации
     * @return глубина анализа
     * @throws ConfigException если это не целое число или меньше MIN_DEPTH
     */
    static int parseDepth(String text) throws ConfigException {
        int depth;
        try {
            depth = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new ConfigException("Параметр <max-depth> должен быть целым числом, а не '" +
                    text + "'");
        }
        if (depth < MIN_DEPTH) {
            throw new ConfigException("Параметр <max-depth> должен быть не меньше " + MIN_DEPTH +
                    ", а не '" + text + "'");
        }
        return depth;
    }

    /**
     * Проверяет, что текст подходит под шаблон
     * @param name имя параметра
     * @param text текст из конфигурации
     * @param pattern шаблон - регулярное выражение
     * @param rule правило для сообщения об ошибке
     * @return текст, если он подходит
     * @throws ConfigException если текст не подходит под шаблон
     */
    private static String requirePattern(String name, String text, String pattern, String rule)
            throws ConfigException {
        if (!text.matches(pattern)) {
            throw new ConfigException("Параметр <" + name + "> " + rule + ", а не '" + text + "'");
        }
        return text;
    }

    /**
     * Проверяет имя пакета
     * @param text текст из конфигурации
     * @param testMode true - включен режим работы с тестовым репозиторием
     * @return имя пакета
     * @throws ConfigException если имя пустое или содержит недопустимые символы
     */
    static String parsePackage(String text, boolean testMode) throws ConfigException {
        if (testMode) {
            return requirePattern("package", text, TEST_PACKAGE_PATTERN,
                    "в тестовом режиме должен состоять из больших латинских букв");
        }
        return requirePattern("package", text, PACKAGE_PATTERN,
                "должен состоять из строчных латинских букв, цифр и символов . + -, " +
                        "начинаться с буквы или цифры и быть не короче двух символов");
    }

    /**
     * Проверяет версию пакета
     * @param text текст из конфигурации
     * @return версия пакета
     * @throws ConfigException если версия пустая или содержит недопустимые символы
     */
    static String parseVersion(String text) throws ConfigException {
        return requirePattern("version", text, VERSION_PATTERN,
                "должен иметь вид [эпоха:]версия[-ревизия], начинаться с цифры и " +
                        "содержать только латинские буквы, цифры и символы . + - ~");
    }

    /**
     * Проверяет подстроку для фильтрации пакетов. Пустая - допустима
     * @param text текст из конфигурации
     * @return подстрока фильтрации
     * @throws ConfigException если в подстроке есть пробельные символы
     */
    static String parseFilter(String text) throws ConfigException {
        return requirePattern("filter", text, FILTER_PATTERN,
                "должен быть без пробелов");
    }

    /**
     * Проверяет имя файла с изображением графа
     * @param text текст из конфигурации
     * @return имя файла
     * @throws ConfigException если имя не подходит под правила
     */
    static String parseOutputFile(String text) throws ConfigException {
        return requirePattern("output-file", text, OUTPUT_PATTERN,
                "должен быть именем файла из латинских букв, цифр и символов . _ - " +
                        "с расширением .png");
    }

    /**
     * Проверяет адрес репозитория. Обычный режим: URL, тестовый: путь к
     * существующему файлу с описанием графа
     * @param text текст из конфигурации
     * @param testMode true - включен режим работы с тестовым репозиторием
     * @return адрес репозитория или путь к файлу
     * @throws ConfigException если значение не подходит для выбранного режима
     */
    static String parseRepository(String text, boolean testMode) throws ConfigException {
        if (!testMode) {
            return requirePattern("repository", text, URL_PATTERN,
                    "в обычном режиме должен быть URL с http:// или https://");
        }
        if (!new File(text).isFile()) {
            throw new ConfigException("Параметр <repository> в тестовом режиме должен быть " +
                    "путем к существующему файлу, а не '" + text + "'");
        }
        return text;
    }

    /**
     * Проверяет все параметры и собирает объект конфигурации
     * @param values тексты параметров: имя тега и значение
     * @return конфигурация с проверенными значениями
     * @throws ConfigException если какое-то значение неверно
     */
    public static Config validate(Map<String, String> values) throws ConfigException {
        Config config = new Config();
        config.setTestMode(parseBoolean("test-mode", values.get("test-mode")));
        config.setPackageName(parsePackage(values.get("package"), config.isTestMode()));
        config.setVersion(parseVersion(values.get("version")));
        config.setRepository(parseRepository(values.get("repository"), config.isTestMode()));
        config.setOutputFile(parseOutputFile(values.get("output-file")));
        config.setAsciiTree(parseBoolean("ascii-tree", values.get("ascii-tree")));
        config.setMaxDepth(parseDepth(values.get("max-depth")));
        config.setFilter(parseFilter(values.get("filter")));
        return config;
    }
}
