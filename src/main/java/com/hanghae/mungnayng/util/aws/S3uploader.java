package com.hanghae.mungnayng.util.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

@RequiredArgsConstructor
@Service
public class S3uploader {

    @Value("${cloud.aws.s3.bucket}") // 내 S3 버켓 이름
    private String bucketName;
    private final AmazonS3Client amazonS3Client;

    public String Uploader(MultipartFile multipartFile) throws IOException {

        String fileName = CommonUtils.buildFileName(multipartFile.getOriginalFilename()); /* 파일이름을 만들어 가져옴 */
        long size = multipartFile.getSize(); /* 파일 크기(byte 단위) */

        ObjectMetadata objectMetadata = new ObjectMetadata();
        /* ContentType을 가져와서 클라이언트에 전송할 데이터 종류(MIME-TYPE) 지정 - ex) image/jpg */
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(size); /* getSize()로 알아낸 파일 사이즈를 클라이언트에게 전송 */
        /* 받아온 multipartFile 파라미터 값에서 실제 데이터를 읽는다(가져온다) - InputStream을 통해서는 Byte만 가져옴 */
        InputStream inputStream = multipartFile.getInputStream();
        /* objectMetadata를 통해 실제 파일에 대한 정보를 받아오는 것 */
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));  /* S3 저장 및 권한설정(publicRead 권한으로 업로드) */
        String imageUrl = amazonS3Client.getUrl(bucketName, fileName).toString();
        imageUrl = URLDecoder.decode(imageUrl, "UTF-8");
        return imageUrl; /* URL 변환시 한글깨짐 방지용 decode */
    }

}
