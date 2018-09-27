package com.file;

import java.io.File;
import java.io.FilenameFilter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SearchAllFiles {
    public static void main(String[] args) {
        File file = new File("D:\\");
        List<File> list = Arrays.asList(file.listFiles());
//        list.stream().forEach(System.out::println);
        Instant instant = Instant.now();
        List<File> list1 = getEndFile(new File("e:\\"));
        System.out.println(list1.size());
        Instant now = Instant.now();
        Duration duration = Duration.between(now,instant);
        System.out.println("时间：" + duration.getNano());
//        List<File> listSorted = list1.stream().sorted(Comparator.comparing(File::length)).collect(Collectors
//                .toList());
//        for(File f:listSorted){
//            System.out.println(f.length() + ", " + f.getName());
//        }

    }

    public static List<File>  getEndFile( File file){

        List<File> list = new ArrayList<>();
        if(null != file){
            if(file.isDirectory()){
//                System.out.println("file:  __  " + file);
                if(null != file.listFiles()) {
                    List<File> list1 = Arrays.asList(file.listFiles());
                    list1.parallelStream().forEach((f) -> list.addAll(getEndFile(f)));
                }
            }else{
                list.add(file);
            }
        }

        return list;
    }

}
