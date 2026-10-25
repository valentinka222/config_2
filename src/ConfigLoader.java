import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ConfigLoader {
    private final static int REQUIRED_ARGS = 1;
    private final static String ROOT_TAG = "config";
    private final static List<String> PARAMS = List.of("package", "version", "repository",
            "test-mode", "output-file", "ascii-tree", "max-depth", "filter");

    /**
     * Проверяет, что путь к конфигурации и файл существуют
     * @param args аргументы командной строки
     * @return файл конфигурации
     * @throws ConfigException если путь или файл не найден
     */
    private static File findConfigFile(String[] args) throws ConfigException {
        if (args.length < REQUIRED_ARGS) {
            throw new ConfigException("Не указан путь к конфигурационному файлу");
        }
        File file = new File(args[0]);
        if (!file.isFile()) {
            throw new ConfigException("Файл не найден по пути: " + args[0]);
        }
        return file;
    }

    /**
     * Читает XML файл и возвращает его корневой элемент
     * @param file XML файл конфигурации
     * @return корневой элемент XML файла конфигурации
     * @throws ConfigException если файл не читается или это некорректный XML
     */
    private static Element parseXml(File file) throws ConfigException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newNSInstance().newDocumentBuilder();
            builder.setErrorHandler(null);
            return builder.parse(file).getDocumentElement();
        } catch (SAXException e) {
            throw new ConfigException("Некорректный XML: " + e.getMessage());
        } catch (IOException | ParserConfigurationException e) {
            throw new ConfigException("Не удалось прочитать файл: " + e.getMessage());
        }
    }

    /**
     * Читает конфигурацию и проверяет корректность названия корневого элемента
     * @param file файл конфигурации
     * @return корневой элемент конфигурации
     * @throws ConfigException если XML файл некорректный или корневой тег неверный
     */
    private static Element readRoot(File file) throws ConfigException {
        Element root = parseXml(file);
        if (!root.getTagName().equals(ROOT_TAG)) {
            throw new ConfigException("Корневой элемент должен называться <" + ROOT_TAG + ">, " +
                    "а найден <" + root.getTagName() + ">");
        }
        return root;
    }

    /**
     * Возвращает текст параметра конфигурации
     * @param root корневой элемент конфигурации
     * @param tag имя тега параметра
     * @return текст внутри тега
     * @throws ConfigException если такого тега нет
     */
    private static String readValue(Element root, String tag) throws ConfigException {
        Node node = root.getElementsByTagName(tag).item(0);
        if (node == null) {
            throw new ConfigException("Не найден параметр <" + tag + ">");
        }
        return node.getTextContent().trim();
    }

    /**
     * Читает все восемь параметров как текст
     * @param root корневой элемент конфигурации
     * @return параметры в порядке их описания: имя тега и текст
     * @throws ConfigException если параметра нет
     */
    private static Map<String, String> readValues(Element root) throws ConfigException {
        Map<String, String> values = new LinkedHashMap<>();
        for (String tag : PARAMS) {
            values.put(tag, readValue(root, tag));
        }
        return values;
    }

    /**
     * Загружает конфигурацию из файла, путь передан в аргументах
     * @param args аргументы командной строки
     * @return параметры конфигурации: имя тега и текст
     * @throws ConfigException если файл не найден, поврежден или нет параметра
     */
    public static Map<String, String> load(String[] args) throws ConfigException {
        Element root = readRoot(findConfigFile(args));
        return readValues(root);
    }
}
