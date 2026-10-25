public class Main {
    /**
     * Загружает и проверяет конфигурацию, выводит параметры или сообщение об ошибке
     * @param args аргументы командной строки - путь к файлу
     */
    public static void main(String[] args) {
        try {
            Config config = ConfigValidator.validate(ConfigLoader.load(args));
            System.out.println(config);
        } catch (ConfigException e) {
            System.out.println("Ошибка конфигурации: " + e.getMessage());
        }
    }
}