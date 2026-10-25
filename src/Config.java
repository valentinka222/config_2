public class Config {
    private String packageName;
    private String version;
    private String repository;
    private boolean testMode;
    private String outputFile;
    private boolean asciiTree;
    private int maxDepth;
    private String filter;

    /**
     * Возвращает параметры в формате "ключ-значение"
     * @return текст со всеми параметрами
     */
    @Override
    public String toString() {
        return String.join(System.lineSeparator(),
                "package = " + packageName,
                "version = " + version,
                "repository = " + repository,
                "test-mode = " + testMode,
                "output-file = " + outputFile,
                "ascii-tree = " + asciiTree,
                "max-depth = " + maxDepth,
                "filter = " + filter);
    }

    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    public String getRepository() {
        return repository;
    }
    public void setRepository(String repository) {
        this.repository = repository;
    }

    public boolean isTestMode() {
        return testMode;
    }
    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public String getOutputFile() {
        return outputFile;
    }
    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public boolean isAsciiTree() {
        return asciiTree;
    }
    public void setAsciiTree(boolean asciiTree) {
        this.asciiTree = asciiTree;
    }

    public String getFilter() {
        return filter;
    }
    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getMaxDepth() {
        return maxDepth;
    }
    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }
}
