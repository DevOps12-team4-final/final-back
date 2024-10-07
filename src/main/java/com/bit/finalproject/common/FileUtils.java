package com.bit.finalproject.common;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bit.finalproject.config.NaverConfiguration;
import com.bit.finalproject.dto.BoardFileDto;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

// 클라우드 기반 객체 스토리지
// 이 코드는 AWS S3를 사용하여 파일을 업로드하고 삭제하는 유틸리티 클래스입니다.
@Component
public class FileUtils {
    private final AmazonS3 s3;

    // NaverConfiguration 클래스로부터 Naver Cloud Platform(NCP)의
    // accessKey, secretKey, regionName, endPoint 값을 받아 AWS S3와 통신할 수 있는 클라이언트를 생성합니다.
    public FileUtils(NaverConfiguration naverConfiguration) {
        s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                naverConfiguration.getEndPoint(),
                                naverConfiguration.getRegionName()
                        )
                )
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(
                                        naverConfiguration.getAccessKey(),
                                        naverConfiguration.getSecretKey()
                                )
                        )
                )
                .build();
    }


    public BoardFileDto parserFileInfo(MultipartFile multipartFile, String directory) {
        String bucketName = "bobaesj";

        BoardFileDto boardFileDto = new BoardFileDto();

        // 중복값을 제외 하기위한 고유식별자 UUID 사용
        UUID uuid = UUID.randomUUID();

        String fileName =  uuid.toString() + "_" +  multipartFile.getOriginalFilename();

        // Object Storage에 파일 업로드
        // InputStream을 통해 파일 데이터를 가져온다.
        // Java에서 데이터를 읽어들이기 위한 기본적인 Stream class이다.
        try(InputStream fileInputStream = multipartFile.getInputStream()) {

            // ObjectMetadata를 사용해 파일 속성(메타데이터)을 설정한다.
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 업로드할 파일의 MIME 타입을 설정합니다.
            objectMetadata.setContentType(multipartFile.getContentType());

            // PutObjectRequest객체에 파일 데이터, 파일 속성(메타데이터)를 담는다.
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName,
                    directory + fileName,
                    fileInputStream,
                    objectMetadata
            ).withCannedAcl(CannedAccessControlList.PublicRead); // 권한 설정

            // s3에 저장
            s3.putObject(putObjectRequest);
            System.out.println("File uploaded to S3: " + fileName);
            //이 전체 과정은 파일을 S3에 저장하기 위해 AWS SDK에서 제공하는 표준 방식입니다.
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // 경로와 이름이 담긴 파일 객체 생성
        File uploadFile = new File(directory + fileName);

        // MIME(Multipurpose Internet Mail Extensions)는 인터넷에서 데이터의 형식(타입)을 정의하는 표준 방식입니다.
        // MIME 타입을 담기위한 변수
        String type = "";

        try {
            // 이 메서드는 파일의 MIME 타입을 자동으로 감지하고, 그 결과를 문자열로 반환합니다.
            type = Files.probeContentType(uploadFile.toPath());
        } catch(IOException ie) {
            System.out.println(ie.getMessage());
        }

        // MIME 타입이 빈 문자열이 아닌지 확인합니다.
        if(!type.equals("")) {
            // 타입확인후 image로 설정 그외는 etc로 설정한다
            if(type.startsWith("image")) {
                boardFileDto.setFiletype("image");
            } else {
                boardFileDto.setFiletype("etc");
            }
        } else {
            boardFileDto.setFiletype("etc");
        }

        // 파일명
        boardFileDto.setFilename(fileName);
        // 원본 파일명
        boardFileDto.setFileoriginname(multipartFile.getOriginalFilename());
        // 파일 경로
        boardFileDto.setFilepath(directory);

        return boardFileDto;
    }

    // 경로의 파일명 파일 삭제한다.
    public void deleteFile(String directory, String fileName) {
        String bucketName = "bobaesj";

        s3.deleteObject(new DeleteObjectRequest(bucketName, directory + fileName));
    }
}

