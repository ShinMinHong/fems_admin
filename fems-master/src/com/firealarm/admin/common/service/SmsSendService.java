package com.firealarm.admin.common.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.firealarm.admin.appconfig.AppConfig;
import com.firealarm.admin.appconfig.AppConst;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.SmsRemainResultDT;
import com.firealarm.admin.common.vo.SmsSendDT;
import com.firealarm.admin.common.vo.SmsSendResultDT;

/**
 *
 * Aligo SMS 발송 API 호출(선불형 SMS)
 * remainSmsCNT : SMS, LMS, MMS 잔여 건수 조회
 * fireSmsSend : 문자 발송
 */
@Service
public class SmsSendService extends ServiceSupport {

	public static final String SMS_SEND_API_URL = "https://apis.aligo.in/send/";
	public static final String SMS_REMAIN_API_URL = "https://apis.aligo.in/remain/";
	public static final String SMS_ENCODE_TYPE = "UTF-8";
	public static final String SMS_BOUNDARY_TEXT = "____boundary____";

	/**
	 * SMS 발송 가능 건수 조회
	 */
	public SmsRemainResultDT checkRemainSmsCnt() {

		SmsRemainResultDT smsRemainResult = new SmsRemainResultDT();
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			/**************** 최근 전송 목록 ******************/
			/* "result_code":결과코드,"message":결과문구, */
			/** list : 전송된 목록 배열 ***/
			/******************** 인증정보 ********************/
			List<NameValuePair> smsParams = new ArrayList<>();
			//API 인증 정보
			smsParams.add(new BasicNameValuePair("user_id", AppConfig.getInstance().getSmsUserId())); // SMS 아이디
			smsParams.add(new BasicNameValuePair("key", AppConfig.getInstance().getSmsApiKey())); //인증키

			HttpPost httpPost = new HttpPost(SMS_REMAIN_API_URL);
			httpPost.setEntity(new UrlEncodedFormEntity(smsParams, AppConst.UTF8));
			CloseableHttpResponse response = client.execute(httpPost);
			String responseAsJsonString = "";
			try {
				responseAsJsonString = EntityUtils.toString(response.getEntity());
				logger.debug("Check SMS Remain Result. responseAsJsonString:{}", responseAsJsonString);

				//응답전문에 속성값중에 SMS_CNT, LMS_CNT, MMS_CNT을 소문자로 변환
				responseAsJsonString = StringUtils.replaceEach(responseAsJsonString, new String[]{"SMS_CNT", "LMS_CNT", "MMS_CNT"}, new String[]{"sms_cnt", "lms_cnt", "mms_cnt"});

				//msg_id => msgId와 같이 사용하기 위해, FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES 정책 사용.
				Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
				smsRemainResult = gson.fromJson(responseAsJsonString, SmsRemainResultDT.class);
			} catch(Exception e) {
				logger.warn("SMS Remain 결과 변환 중 오류 발생 : string:{}, error:{}", responseAsJsonString, e.getMessage() );
				smsRemainResult.setResultCode("-999");
				smsRemainResult.setMessage("SMS잔여 결과 변환 시 Exception 발생.");
			} finally {
				response.close();
			}
		} catch(Exception e){
			logger.warn("Check SMS Remain API Exception : {}", e.getMessage() );
			smsRemainResult.setResultCode("-999");
			smsRemainResult.setMessage("SMS잔여 결과 요청 시 Exception 발생.");
		}

		return smsRemainResult;
	}

	/**
	 * SMS 발송 Aligo API 연동
	 * AppConfig이용 : user_id, key, sender, title
	 * DT 이용 :
	 * 		receiver : 수신번호 - 콤마(,)로 구분하여 최대 1000개까지 입력 가능. 번호 형식(01012345678,01012451247...
	 * 		destination : %고객명% 치환용 - 형식(01012345678ㅣ인왕떡집,01012451247ㅣKFC포방점...)
	 * 		msg : 메시지 내용 - 형식("%고객명%점포에 화재발생알림...")-(%고객명%)이 용어만 치환 됨
	 * 		testmode_yn: 연동테스트 여부 - 연동테스트시 Y 적용(Y 인경우 실제문자 전송X)
	 */
	public SmsSendResultDT sendSms(SmsSendDT smsSendDT) {

		SmsSendResultDT smsSendResult = new SmsSendResultDT();

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			/**************** 문자전송하기 예제 ******************/
			/* "result_code":결과코드,"message":결과문구, */
			/* "msg_id":메세지ID,"error_cnt":에러갯수,"success_cnt":성공갯수 */
			/* 동일내용 > 전송용 입니다.
			/******************** 인증정보 ********************/
			List<NameValuePair> smsParams = new ArrayList<>();

			//API 인증 정보
			smsParams.add(new BasicNameValuePair("user_id", AppConfig.getInstance().getSmsUserId())); // SMS 아이디
			smsParams.add(new BasicNameValuePair("key", AppConfig.getInstance().getSmsApiKey())); //인증키

			//전송정보
			smsParams.add(new BasicNameValuePair("sender", AppConfig.getInstance().getSmsSendNo())); // 발신번호
			smsParams.add(new BasicNameValuePair("receiver", smsSendDT.getReceiver())); // 수신번호
			smsParams.add(new BasicNameValuePair("destination", smsSendDT.getDestination())); // 수신인 %고객명% 치환
			smsParams.add(new BasicNameValuePair("title", smsSendDT.getTitle())); //  LMS, MMS 제목 (미입력시 본문중 44Byte 또는 엔터 구분자 첫라인)
			smsParams.add(new BasicNameValuePair("msg", smsSendDT.getMsg())); // 메세지 내용
			smsParams.add(new BasicNameValuePair("testmode_yn", smsSendDT.getTestmodeYn())); // Y 인경우 실제문자 전송X , 자동취소(환불) 처리

			HttpPost httpPost = new HttpPost(SMS_SEND_API_URL);
			httpPost.setEntity(new UrlEncodedFormEntity(smsParams, AppConst.UTF8));

			CloseableHttpResponse response = client.execute(httpPost);
			String responseAsJsonString = "";
			try {
				responseAsJsonString = EntityUtils.toString(response.getEntity());
				logger.debug("SMS Send Result. responseAsJsonString:{}", responseAsJsonString);
				//msg_id => msgId와 같이 사용하기 위해, FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES 정책 사용.
				Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
				smsSendResult = gson.fromJson(responseAsJsonString, SmsSendResultDT.class);
			} catch(Exception e) {
				logger.warn("SMS Remain 결과 변환 중 오류 발생 : string:{}, error:{}", responseAsJsonString, e.getMessage() );
				smsSendResult.setResultCode("-999");
				smsSendResult.setMessage("SMS발송 결과 변환 시 Exception 발생.");
			} finally {
				response.close();
			}
		} catch(Exception e){
			logger.warn("SMS SEND API Exception : {}", e.getMessage() );
			smsSendResult.setResultCode("-999");
			smsSendResult.setMessage("SMS발송 결과 요청 시 Exception 발생.");
		}

		return smsSendResult;
	}

	/**
	 * SMS 발송 가능 건수 조회
	 */
	public SmsRemainResultDT oldCheckRemainSmsCNT() {

		SmsRemainResultDT smsRemainResultDT = new SmsRemainResultDT();
		try{

			logger.debug("::::::::::::::::::::::::::: AppConfig.getInstance().getSmsUserId() : {}", AppConfig.getInstance().getSmsUserId() );
			logger.debug("::::::::::::::::::::::::::: AppConfig.getInstance().getSmsApiKey() : {}", AppConfig.getInstance().getSmsApiKey() );

			/**************** 최근 전송 목록 ******************/
			/* "result_code":결과코드,"message":결과문구, */
			/** list : 전송된 목록 배열 ***/
			/******************** 인증정보 ********************/
			String sms = "";
			sms += "user_id=" + AppConfig.getInstance().getSmsUserId(); // SMS 아이디
			sms += "&key=" + AppConfig.getInstance().getSmsApiKey(); //인증키
			/******************** 인증정보 ********************/

			URL url = new URL(SMS_REMAIN_API_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			OutputStream os = conn.getOutputStream();
			os.write(sms.getBytes());
			os.flush();
			os.close();

			String result = "";
			String buffer = null;
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while((buffer = in.readLine())!=null){
				result += buffer;
			}

			in.close();

			logger.debug("::::::::::::::::::::::::::: result : {}", result );
			//out.print(result);

			Gson gson = new Gson();
			smsRemainResultDT = gson.fromJson(result, new TypeToken<SmsSendResultDT>(){}.getType());
			logger.debug("::::::::::::::::::::::::::: smsSendResultDT : {}", smsRemainResultDT );

		}catch(MalformedURLException e1){
			//out.print(e1.getMessage());
			logger.debug("::::::::::::::::::::::::::: Exception : {}", e1.getMessage() );

			smsRemainResultDT.setResultCode("-999");
			smsRemainResultDT.setMessage("예기치않은 오류발생(MalformedURLException).");

		}catch(IOException e2){
			//out.print(e2.getMessage());
			logger.debug("::::::::::::::::::::::::::: Exception : {}", e2.getMessage() );

			smsRemainResultDT.setResultCode("-999");
			smsRemainResultDT.setMessage("예기치않은 오류발생(IOException).");
		}

		return smsRemainResultDT;

	}

	/**
	 * SMS 발송 Aligo API 연동
	 * AppConfig이용 : user_id, key, sender, title
	 * DT 이용 :
	 * 		receiver : 수신번호 - 콤마(,)로 구분하여 최대 1000개까지 입력 가능. 번호 형식(01012345678,01012451247...
	 * 		destination : %고객명% 치환용 - 형식(01012345678ㅣ인왕떡집,01012451247ㅣKFC포방점...)
	 * 		msg : 메시지 내용 - 형식("%고객명%점포에 화재발생알림...")-(%고객명%)이 용어만 치환 됨
	 * 		testmode_yn: 연동테스트 여부 - 연동테스트시 Y 적용(Y 인경우 실제문자 전송X)
	 */
	public SmsSendResultDT oldSendSms(SmsSendDT smsSendDT) {

		SmsSendResultDT smsSendResultDT = new SmsSendResultDT();
		CloseableHttpClient client = HttpClients.createDefault();

		try {
			/**************** 문자전송하기 예제 ******************/
			/* "result_code":결과코드,"message":결과문구, */
			/* "msg_id":메세지ID,"error_cnt":에러갯수,"success_cnt":성공갯수 */
			/* 동일내용 > 전송용 입니다.
			/******************** 인증정보 ********************/
			Map<String, String> smsParams = new HashMap<>();

			//API 인증 정보
			smsParams.put("user_id", AppConfig.getInstance().getSmsUserId()); // SMS 아이디
			smsParams.put("key", AppConfig.getInstance().getSmsApiKey()); //인증키

			//전송정보
			smsParams.put("sender", AppConfig.getInstance().getSmsSendNo()); // 발신번호
			smsParams.put("receiver", smsSendDT.getReceiver()); // 수신번호
			smsParams.put("destination", smsSendDT.getDestination()); // 수신인 %고객명% 치환
			smsParams.put("title", AppConfig.getInstance().getSmsSendTitle()); //  LMS, MMS 제목 (미입력시 본문중 44Byte 또는 엔터 구분자 첫라인)
			smsParams.put("msg", smsSendDT.getMsg()); // 메세지 내용
			smsParams.put("testmode_yn", smsSendDT.getTestmodeYn()); // Y 인경우 실제문자 전송X , 자동취소(환불) 처리
			//smsParams.put("rdate", ""); // 예약일자 - 20161004 : 2016-10-04일기준
			//smsParams.put("rtime", ""); // 예약시간 - 1930 : 오후 7시30분
			//String image = "";
			//image = "/tmp/pic_57f358af08cf7_sms_.jpg"; // MMS 이미지 파일 위치
			/******************** 전송정보 ********************/

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setBoundary(SMS_BOUNDARY_TEXT);
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.setCharset(Charset.forName(SMS_ENCODE_TYPE));

			for(Iterator<String> i = smsParams.keySet().iterator(); i.hasNext();){
				String key = i.next();
				builder.addTextBody(key, smsParams.get(key)
						, ContentType.create("Multipart/related", SMS_ENCODE_TYPE));
			}

			HttpEntity entity = builder.build();

			HttpPost post = new HttpPost(SMS_SEND_API_URL);
			post.setEntity(entity);

			HttpResponse res = client.execute(post);

			String result = "";
			if(res != null){
				BufferedReader in = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), SMS_ENCODE_TYPE));
				String buffer = null;
				while((buffer = in.readLine())!=null){
					result += buffer;
				}
				in.close();
			}

			logger.debug("::::::::::::::::::::::::::: result : {}", result );

			Gson gson = new Gson();
			smsSendResultDT = gson.fromJson(result, new TypeToken<SmsSendResultDT>(){}.getType());
			logger.debug("::::::::::::::::::::::::::: smsSendResultDT : {}", smsSendResultDT );

			client.close();
		} catch(Exception e){
			logger.debug("::::::::::::::::::::::::::: Exception : {}", e.getMessage() );
			smsSendResultDT.setResultCode("-999");
			smsSendResultDT.setMessage("예기치않은 오류발생(Exception).");
		}

		return smsSendResultDT;
	}
}