/*
 * Copyright 2015 General Developers. www.gendevs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gendevs.bedrock.service.storage;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

import java.io.File;
import java.io.InputStream;

public class StorageProvider {
	private static final String BASE_URL = "https://s3-us-west-2.amazonaws.com/";

	protected final String bucketName = getAWSBucket();
	private Region region = com.amazonaws.regions.Region.getRegion(Regions.US_WEST_2);
	private static AmazonS3Client s3Client = null;
	private TransferManager manager = null;
	protected File tempFile;

	private enum MediaType {
		IMAGE, VIDEO, VOICE;
	}

	private void initalizeS3() {
		AWSCredentials credentials = new BasicAWSCredentials(StorageConstants.ACCESS_KEY,
				StorageConstants.SECRET_KEY);

		if (s3Client == null) {
			s3Client = new AmazonS3Client(credentials);
			s3Client.setRegion(region);
			s3Client.setEndpoint("s3-us-west-2.amazonaws.com");
		}
		manager = new TransferManager(credentials);
	}

	// Updated method
	public String uploadImageFile(InputStream imageInputStream, String organisationId, String id, String type) {
		String fileName = uploadFile(imageInputStream, "image/jpeg", organisationId, id, type);
		return getPhotosUrl(fileName, type);
	}

	private String getAWSBucket() {
		return StorageConstants.BUCKET_NAME;
	}

	private String getPhotosUrl(String fileName, String type) {
		switch(type) {
		case "app":
			return String.format(StorageConstants.APP_PROFILE_PHOTOS_END_URL, BASE_URL, bucketName, fileName);
		case "user":
			return String.format(StorageConstants.USER_PROFILE_PHOTOS_END_URL, BASE_URL, bucketName, fileName);
		}
		return null;
	}
	
	private String uploadFile(InputStream imageInputStream, String contentType, String organisationId, String id, String type) {
		initalizeS3();
		String fileName = null;
		switch(type) {
		case "app":
			fileName = String.format(StorageConstants.APP_PROFILE_PHOTOS, organisationId, id);break;
		case "user":
			fileName = String.format(StorageConstants.USER_PROFILE_PHOTOS, organisationId, id);break;
		}
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(contentType);
		
		PutObjectRequest por = new PutObjectRequest(bucketName, fileName, imageInputStream, metadata);
		por.setCannedAcl(CannedAccessControlList.PublicRead);
		Upload upload = manager.upload(por);
		try {
			upload.waitForCompletion();
		} catch (AmazonServiceException e) {
			e.printStackTrace();
		} catch (AmazonClientException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return fileName;
	}
	
	public boolean deleteFile(String bucketName, String keyName) {
		initalizeS3();
		//AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());        
		s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
		return true;
	}
	
}
