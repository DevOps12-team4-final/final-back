package com.bit.finalproject.livestation.service;

import com.bit.finalproject.dto.ResponseDto;
import com.bit.finalproject.livestation.dto.CdnDto;
import com.bit.finalproject.livestation.dto.LiveStationInfoDto;
import com.bit.finalproject.livestation.dto.LiveStationRequestDto;
import com.bit.finalproject.livestation.dto.LiveStationResponseDto;
import com.bit.finalproject.livestation.dto.LiveStationUrlDto;
import com.bit.finalproject.livestation.dto.RecordDto;
import com.bit.finalproject.livestation.dto.RecordInfoDto;
import com.bit.finalproject.livestation.dto.RecordResponseDto;
import com.bit.finalproject.livestation.dto.RecordVodDto;
import com.bit.finalproject.livestation.dto.ServiceUrlDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class LiveStationService {
    @Value("630CEFD3678E43849068")
    String accessKey;
    @Value("CE8BF1ECC97ADC629E30D846DB364D2CF41DECE5")
    String secretKey;
    @Value("bitcamp-149")
    String bucket;

    String liveStationUrl = "https://livestation.apigw.ntruss.com/api/v2/channels";

    public String makeSignature(String timestamp, String method, String signUrl) {
        try {
            String space = " ";
            String newLine = "\n";

            String message = new StringBuilder().append(method).append(space).append(signUrl).append(newLine)
                    .append(timestamp).append(newLine).append(accessKey).toString();

            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            String encodeBase64String = Base64.encodeBase64String(rawHmac);

            return encodeBase64String;
        } catch (Exception e) {
//            throw new MakeSignatureException(); ??
            throw new RuntimeException();
        }
    }

    public String createChannel(String name) {
        try {
            String title = name.replaceAll(" ", "");
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(liveStationUrl);
            String url = urlBuilder.toString();
            String signUrl = url.substring(url.indexOf(".com") + 4);

            //요청 바디 만들기
            CdnDto cdnDTO = CdnDto.builder().createCdn(true).cdnType("CDN_PLUS").build();
            RecordDto recordDTO = RecordDto.builder().type("AUTO_UPLOAD").format("MP4").bucketName(bucket).filePath("/")
                    .accessControl("PUBLIC_READ").build();
            LiveStationRequestDto requestDTO = LiveStationRequestDto.builder().channelName(title).cdn(cdnDTO)
                    .qualitySetId(3).useDvr(true).immediateOnAir(true).timemachineMin(360).record(recordDTO)
                    .isStreamFailOver(false).build();

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(requestDTO);

            String timestamp = String.valueOf(System.currentTimeMillis());
            String method = "POST";
            String sig = makeSignature(timestamp, method, signUrl);

            //요청 헤더 만들기
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-ncp-apigw-timestamp", timestamp);
            headers.set("x-ncp-iam-access-key", accessKey);
            headers.set("x-ncp-apigw-signature-v2", sig);
            headers.set("x-ncp-region_code", "KR");

            HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

            ResponseEntity<LiveStationResponseDto> response = restTemplate.exchange(new URI(url), HttpMethod.POST, body,
                    LiveStationResponseDto.class);

            return response.getBody().getContent().getChannelId();  // channelId 반환
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public LiveStationInfoDto getChannelInfo(String channelID) {
        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(liveStationUrl);
            urlBuilder.append("/");
            urlBuilder.append(channelID);
            String url = urlBuilder.toString();

            String signUrl = url.substring(url.indexOf(".com") + 4);

            String timestamp = String.valueOf(System.currentTimeMillis());
            String method = "GET";
            String sig = makeSignature(timestamp, method, signUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-ncp-apigw-timestamp", timestamp);
            headers.set("x-ncp-iam-access-key", accessKey);
            headers.set("x-ncp-apigw-signature-v2", sig);
            headers.set("x-ncp-region_code", "KR");

            HttpEntity<LiveStationResponseDto> httpEntity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<LiveStationResponseDto> response = restTemplate.exchange(new URI(url), HttpMethod.GET,
                    httpEntity, LiveStationResponseDto.class);

            System.out.println(response);

            //보내줄 데이터 가공
            LiveStationInfoDto dto = LiveStationInfoDto.builder().channelId(channelID)
                    .channelName(response.getBody().getContent().getChannelName())
                    .channelStatus(response.getBody().getContent().getChannelStatus())
                    .cdnInstanceNo(response.getBody().getContent().getCdn().getInstanceNo())
                    .cdnStatus(response.getBody().getContent().getCdn().getStatusName())
                    .publishUrl(response.getBody().getContent().getPublishUrl())
                    .streamKey(response.getBody().getContent().getStreamKey()).build();
            return dto;

        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<LiveStationUrlDto> getServiceURL(String channelID, String urlType) {
        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(liveStationUrl);
            urlBuilder.append("/");
            urlBuilder.append(channelID);
            urlBuilder.append("/serviceUrls?serviceUrlType=");
            urlBuilder.append(urlType);
            String url = urlBuilder.toString();

            String signUrl = url.substring(url.indexOf(".com") + 4);

            String timestamp = String.valueOf(System.currentTimeMillis());
            String method = "GET";
            String sig = makeSignature(timestamp, method, signUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-ncp-apigw-timestamp", timestamp);
            headers.set("x-ncp-iam-access-key", accessKey);
            headers.set("x-ncp-apigw-signature-v2", sig);
            headers.set("x-ncp-region_code", "KR");

            HttpEntity<ServiceUrlDto> httpEntity = new HttpEntity<>(headers);

            // HTTP 요청을 보내기 위해 RestTemplate 객체 생성
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<ServiceUrlDto> response = restTemplate.exchange(new URI(url), HttpMethod.GET, httpEntity,
                    ServiceUrlDto.class);
            System.out.println(response);
            //보내줄 데이터 가공

            List<LiveStationUrlDto> dtoList = response.getBody().getContents().stream()
                    .map(contentDTO -> LiveStationUrlDto.builder().channelId(channelID).name(contentDTO.getName())
                            .url(contentDTO.getUrl()).build()).collect(Collectors.toList());

            return dtoList;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public ResponseEntity<?> deleteChannel(String channelID) {
        ResponseDto<LiveStationInfoDto> ResponseDto = new ResponseDto<>();
        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(liveStationUrl);
            urlBuilder.append("/");
            urlBuilder.append(channelID);
            String url = urlBuilder.toString();

            String signUrl = url.substring(url.indexOf(".com") + 4);

            String timestamp = String.valueOf(System.currentTimeMillis());
            String method = "DELETE";
            String sig = makeSignature(timestamp, method, signUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-ncp-apigw-timestamp", timestamp);
            headers.set("x-ncp-iam-access-key", accessKey);
            headers.set("x-ncp-apigw-signature-v2", sig);
            headers.set("x-ncp-region_code", "KR");

            HttpEntity<String> httpEntity = new HttpEntity<>(headers);

            // HTTP 요청을 보내기 위해 RestTemplate 객체 생성
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<LiveStationResponseDto> response = restTemplate.exchange(new URI(url), HttpMethod.DELETE,
                    httpEntity, LiveStationResponseDto.class);

            LiveStationInfoDto dto = LiveStationInfoDto.builder().channelId(channelID)
                    .channelName(response.getBody().getContent().getChannelName())
                    .cdnInstanceNo(response.getBody().getContent().getCdn().getInstanceNo()).build();

            ResponseDto.setItem(dto);
            ResponseDto.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(ResponseDto);
        } catch (URISyntaxException e) {
            ResponseDto.setStatusMessage(e.getMessage());
            ResponseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());

            return ResponseEntity.badRequest().body(ResponseDto);
        }
    }

    public RecordVodDto getRecord(String channelID) {
        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(liveStationUrl);
            urlBuilder.append("/");
            urlBuilder.append(channelID);
            urlBuilder.append("/records");

            String url = urlBuilder.toString();

            String signUrl = url.substring(url.indexOf(".com") + 4);

            String timestamp = String.valueOf(System.currentTimeMillis());
            String method = "GET";
            String sig = makeSignature(timestamp, method, signUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-ncp-apigw-timestamp", timestamp);
            headers.set("x-ncp-iam-access-key", accessKey);
            headers.set("x-ncp-apigw-signature-v2", sig);
            headers.set("x-ncp-region_code", "KR");

            HttpEntity<String> httpEntity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<RecordResponseDto> response = restTemplate.exchange(new URI(url), HttpMethod.GET, httpEntity,
                    RecordResponseDto.class);
            RecordVodDto dto = null;
            System.out.println(response.getBody().getContentDTO().getRecordList());

            for (String key : response.getBody().getContentDTO().getRecordList().keySet()) {
                RecordInfoDto recordInfoDTO = response.getBody().getContentDTO().getRecordList().get(key);
                if (recordInfoDTO.getRecordType().equals("MP4")) {
                    dto = RecordVodDto.builder().channelId(recordInfoDTO.getChannelId())
                            .fileName(recordInfoDTO.getFileName()).uploadPath(recordInfoDTO.getUploadPath())
                            .recordType(recordInfoDTO.getRecordType()).build();
                }
            }

            return dto;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}