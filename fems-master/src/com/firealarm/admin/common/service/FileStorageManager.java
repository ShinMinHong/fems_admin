package com.firealarm.admin.common.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.firealarm.admin.appconfig.AppConst;
import com.firealarm.admin.appconfig.CodeMap.UPLOAD_SERVICE_TYPE;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.UploadFileVO;

import framework.exception.AbleIOException;
import framework.exception.AbleRuntimeException;
import framework.util.AbleDateUtil;
import framework.util.AbleUtil;


@Service(value="fileStorageManager")
public class FileStorageManager extends ServiceSupport {

	/** 업로드 최상위 Root경로 */
	public String getUploadRootPath(boolean isPrivate) {
		//서버 환경별 Nas 경로
		if ( isPrivate ) {
			return this.appConfig.getNasPrivatePath();
		} else {
			return this.appConfig.getNasPath();
		}
	}

	/** 업로드 최상위 URL경로 */
	public String getUploadRootUrl() {
		//서버 환경별 Nas Url
		return this.appConfig.getNasUrl();
	}

	/** 서비스별 업로드 하위 경로 (업로드 Root 내의) */
	public String getServiceUploadPath(UPLOAD_SERVICE_TYPE serviceType) {
		switch (serviceType) {
			case FIREDETECTOR: //사전점검-안전서류점검
				return "/firedetector/firedetectormng";
			case EXCEL_UPLOAD: // 엑셀 업로드
				return "/excelupload";
			default:
				throw new AbleRuntimeException("업로드처리를 위한 서비스타입을 확인 할 수 없습니다.");
		}
	}

	/** 경로가 없는 경우 생성 */
	public void ensureFolder(String dirPath) {
		try {
			File dir = new File(dirPath);
			if(!dir.exists()) {
				FileUtils.forceMkdir(dir);
			}
			if(dir.isFile()) {
				throw new AbleRuntimeException("업로드 대상경로와 같은 파일명이 존재합니다. " + dirPath);
			}
		} catch (IOException e) {
			logger.debug("FileStorageManager.ensureFolder FAILED. - EXCEPTION: {} - {}", e.getClass().getName(), e.getMessage(), logger.isTraceEnabled()?e:null);
			throw new AbleRuntimeException("업로드 대상경로 생성에 실패하였습니다. " + dirPath);
		}
	}

	protected String getUniqueFileName(){
		return AbleDateUtil.yyyyMMddHHmmss()+UUID.randomUUID().toString().replace("-", "").substring(0, 4);
	}

	/** 파일명에 특수 문자가 들어가있는지 체크 */
	public boolean hasSpecialCharacter(String fileName) {
		fileName = fileName.trim().replace(" ","");
		Pattern regex = Pattern.compile("[\\/:*?\"<>|]");
	    Matcher matcher = regex.matcher(fileName);
	    return !fileName.isEmpty() && matcher.find();
	}

	/**
	 * 업로드 파일 저장 처리
	 * @param serviceType 서비스타입
	 * @param uploadedFile 업로드파일객체
	 * @param optionalFilenamePrefix 파일저장시
	 * @return
	 */
	public UploadFileVO saveUploadedFile(UPLOAD_SERVICE_TYPE serviceType, MultipartFile uploadedFile, String optionalFilenamePrefix, boolean isPrivate) {
		// 파일이 없을떄
		if(uploadedFile == null || uploadedFile.getSize() == 0) {
			return null;
		}

		// 서버에 저장할 경로 및 파일명 설정
		String root = getUploadRootPath(isPrivate);
		String uploadPath = getServiceUploadPath(serviceType);
		String uploadFullPath = root + uploadPath;

		String filename = uploadedFile.getOriginalFilename();
		String fileExtendtion = FilenameUtils.getExtension(filename);

		// 폴더 체크 - 없을시 생성
		ensureFolder(uploadFullPath);

		String orginalFileName = uploadedFile.getOriginalFilename();

		//저장파일명
		String fileNameToSave = this.getUniqueFileName()+"."+fileExtendtion; //+ "." + orginalFileName;

		// optionalFilenamePrefix
		if(!StringUtils.isEmpty(optionalFilenamePrefix)) {
			fileNameToSave = optionalFilenamePrefix + "." + fileNameToSave;
		}

		String filePathToSaveAtUploadRoot = uploadPath + "/" + fileNameToSave;
		String fullFilePathToSave = root + filePathToSaveAtUploadRoot;
		File fileToSave = new File(fullFilePathToSave);

		try {
			uploadedFile.transferTo(fileToSave);
		} catch (Exception e) {
			logger.debug("FileStorageManager.saveUploadedFile FAILED. - EXCEPTION: {} - {}", e.getClass().getName(), e.getMessage(), logger.isTraceEnabled()?e:null);
			throw new AbleRuntimeException(String.format("파일 저장에 실패 하였습니다.(파일명:%s)", orginalFileName));
		}

		//저장결과 반환
		UploadFileVO result = new UploadFileVO(serviceType, orginalFileName, fileNameToSave, filePathToSaveAtUploadRoot, uploadedFile.getSize());
		logger.info("결과"+result.toString());

		return result;
	}

	/**
	 * 파일 list 업로드
	 * @param serviceType 서비스타입
	 * @param uploadedFile 업로드파일객체
	 * @param optionalFilenamePrefix 파일저장시
	 * @return upload 된 파일들 정보
	 */
	public List<UploadFileVO> saveUploadFiles (List<MultipartFile> uploadFiles , UPLOAD_SERVICE_TYPE boardType, boolean isPrivate) {

		List<UploadFileVO> fileVOs = new ArrayList<UploadFileVO>();

		for(MultipartFile file:uploadFiles) {

			if (!file.getOriginalFilename().isEmpty()) {
				// 원본파일명 특수문자 체크
				// 해당문자 제외(\, /, :, *, ?, ", <, >, |)
				String fileName = file.getOriginalFilename();
				if(this.hasSpecialCharacter(fileName)) {
					throw new AbleRuntimeException("파일명에는 특수 문자가 들어올 수 없습니다.");
				}

				Long fileSize = file.getSize();

				// 파일별 용량 체크
				if( fileSize > AppConst.ALLOWED_BOARDFILE_SIZE){
					throw new AbleRuntimeException("첨부파일 최대크기를 초과하였습니다. 최대사이즈 " + AppConst.ALLOWED_BOARDFILE_SIZE+ " 입니다.");
				}

				UploadFileVO fileVO = this.saveUploadedFile(boardType, file, null, isPrivate);

				if ( fileVO != null ){
					fileVOs.add(fileVO);
					logger.info("::::파일 ::"+fileVO.toString());
				}
			}
		}
		return fileVOs;
	}

	/**
	 * 업로드 파일 삭제
	 * @param filePathToSaveAtUploadRoot 업로드 Root내의 파일경로
	 */
	public void deleteUploadedFile(String filePathToSaveAtUploadRoot, boolean isPrivate) {
		if(StringUtils.isEmpty(filePathToSaveAtUploadRoot)) {
			return;
		}

		String root = getUploadRootPath(isPrivate);
		logger.info("::::fullpath{}::::",root + filePathToSaveAtUploadRoot);

		File fileToDelete = new File(root + filePathToSaveAtUploadRoot);

		if(fileToDelete.exists()) {
			FileUtil.deleteContents(fileToDelete);
		}
	}

	public void download(String DBPath, String originalFileName,boolean isPrivate, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String root = this.getUploadRootPath(isPrivate);
		/* 실제 파일 저장되어 있는 위치 */
		String absolutePath = root+DBPath;
		String fileName = originalFileName;
		try {
			this.startDownload(fileName, absolutePath, request, response, true);
		} catch(RuntimeException e) {
			throw new AbleIOException("파일을 다운로드 할 수 없습니다.");
		} catch(Exception e) {
			throw new AbleIOException("파일을 다운로드 할 수 없습니다.");
		}
	}

	/**
	 * 업로드 파일 다운로드
	 * @param filePathToSaveAtUploadRoot 업로드 Root내의 파일경로
	 */
	public void startDownload(String fileName, String filePathAtUploadRoot, HttpServletRequest request, HttpServletResponse response, boolean forceDownload) throws IOException {
		// Write file
		OutputStream out = response.getOutputStream();
		FileInputStream fis = null;
		try {
			File file = new File(filePathAtUploadRoot);
			fis = new FileInputStream(file);
			String mimeType = getFileMimeType(file);
			response.setContentType(mimeType);
			response.setContentLength((int) file.length());
			if (forceDownload) {
				// File download response header
				// fileName = URLEncoder.encode(fileName, "UTF-8");
				String browser = request.getHeader("User-Agent");
				// 파일 인코딩
				if (browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")) {
					fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
				} else {
					fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
				}

				// fileName = new String(fileName.getBytes("euc-kr"), "8859_1");
				response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\";");
				response.setHeader("Content-Transfer-Encoding", "binary");
				// jQuery FileDownload Cookie
				response.setHeader("Set-Cookie", "fileDownload=true; path=/"); // the cookie to indicate if a file																		// download has occured
			}

			FileCopyUtils.copy(fis, out);
			out.flush();
		} catch (Exception e) {
			logger.warn(e.getMessage());
			// http://johnculviner.com/jquery-file-download-plugin-for-ajax-like-feature-rich-file-downloads/
			if (AbleUtil.isIFrameTransportRequest(request)) {
				response.setContentType("text/html");
				response.setCharacterEncoding("utf-8");
				PrintWriter pw = new PrintWriter(out);
				pw.append("요청된 파일을 다운로드 할 수 없습니다.");
				pw.flush();
				pw.close();
			} else {
				throw new AbleIOException("파일을 다운로드 할 수 없습니다.");
			}
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e2) {
					logger.warn(e2.getMessage());
				}
			}
		}
	}

	private String getFileMimeType(File file) {
		//http://stackoverflow.com/questions/51438/getting-a-files-mime-type-in-java
		return URLConnection.guessContentTypeFromName(file.getName());
	}

}
