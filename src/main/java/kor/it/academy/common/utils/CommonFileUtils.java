package kor.it.academy.common.utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.*;

public class CommonFileUtils {

    public static List<Map<String, Object>> uploadFiles(List<MultipartFile> files, String path, String type){
        List<Map<String , Object>> resultList = new ArrayList<>();

        String filePath = path + File.separator + type + File.separator;

        for (MultipartFile file : files) {
            Map<String, Object> fileMap = new HashMap<>();

            try{
                String fileName = file.getOriginalFilename();
                String extension = fileName.substring(fileName.lastIndexOf(".")); //확장자 가져오기
                //네트워크 상에서 식별 ID
                String randStr = UUID.randomUUID().toString().replaceAll("-","").substring(0,16);
                String storedFileName = randStr + extension;

                //파일 객체 만들기
                String fullPath = filePath + storedFileName;
                File dest = new File(fullPath);
                //만약 경로가 없으면
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }

                //빈 파일 만들기
                dest.createNewFile();
                //복사
                file.transferTo(dest);
                fileMap.put("fileName", fileName);
                fileMap.put("storedFileName", storedFileName);
                fileMap.put("filePath", filePath);
                //리스트에 추가
                resultList.add(fileMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return resultList;
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

}
