package com.epam.izh.rd.online.repository;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SimpleFileRepository implements FileRepository {

    /**
     * Метод рекурсивно подсчитывает количество файлов в директории
     *
     * @param path путь до директори
     * @return файлов, в том числе скрытых
     */
    @Override
    public long countFilesInDirectory(String path) {
        long fileCount = 0;
        long hiddenFileCount = 0;

        Path originalPath = Paths.get(path);
        if (!originalPath.isAbsolute()) {
            URL url = getClass().getClassLoader().getResource(path);
            try {
                Path absolutePath = Paths.get(url.toURI());
                path = absolutePath.toString();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        File file = new File(path);
        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    if (file.isHidden()) {
                        hiddenFileCount += 1;
                    } else {
                        fileCount += 1;
                    }
                } else {
                    fileCount += countFilesInDirectory(files[i].getPath());
                }
            }
        }

        return fileCount + hiddenFileCount;
    }

    /**
     * Метод рекурсивно подсчитывает количество папок в директории, считая корень
     *
     * @param path путь до директории
     * @return число папок
     */
    @Override
    public long countDirsInDirectory(String path) {
        long catalogCount = 1;

        Path pathResult = Paths.get(path);

        if (!pathResult.isAbsolute()) {
            URL url = getClass().getClassLoader().getResource(path);
            try {
                Path absolutePath = Paths.get(url.toURI());
                path = absolutePath.toString();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        File folder = new File(path);
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                {
                    catalogCount += countDirsInDirectory(file.getAbsolutePath());
                }
            }
        }

        return catalogCount;
    }

    /**
     * Метод копирует все файлы с расширением .txt
     *
     * @param from путь откуда
     * @param to   путь куда
     */
    @Override
    public void copyTXTFiles(String from, String to) {
        File folder = new File(from);
        FilenameFilter filter = (f, name) -> name.endsWith(".txt");
        File[] filesInFolder = folder.listFiles(filter);
        if (filesInFolder != null) {
            for (File file : filesInFolder) {
                try {
                    Files.copy(file.toPath(), Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Метод создает файл на диске с расширением txt
     *
     * @param path путь до нового файла
     * @param name имя файла
     * @return был ли создан файл
     */
    @Override
    public boolean createFile(String path, String name) {
        Path originalPath = Paths.get(path);

        if (!originalPath.isAbsolute()) {
            try {
                Path absolutePath = Paths.get(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                path = absolutePath.toString() + "\\" + path;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        boolean result = false;

        File folder = new File(path);
        File file = new File(path + "\\" + name);

        try {
            folder.mkdir();
            file.createNewFile();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Метод считывает тело файла .txt из папки src/main/resources
     *
     * @param fileName имя файла
     * @return контент
     */
    @Override
    public String readFileFromResources(String fileName) {
        String body = null;
        try {
            body = Files.lines(Paths.get("src\\main\\resources\\" + fileName)).reduce("", String::concat);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }
}
