package com.hanghae.mungnayng.util.aws;

// 업로드한 파일명 생성자
public class CommonUtils {

    private static final String FILE_EXTENSION_SEPARATOR = ".";

    public static String buildFileName(String originalFileName){
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR); /* 파일 확장자 구분선(파일 이름에서 몇번째 위치에 .이 있는지) */
        String fileExtension = originalFileName.substring(fileExtensionIndex);  /* 파일 확장자 (substring으로 자름) */
        String fileName = originalFileName.substring(0, fileExtensionIndex); /* 파일 이름(.확장자를 제외한) */
        String now = String.valueOf(System.currentTimeMillis()); /* 파일 업로드 시간(String 타입으로 변환) */


        return fileName  + now + fileExtension;
    }


}
