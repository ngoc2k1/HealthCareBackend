package vn.healthcare.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
	private final Cloudinary cloudinary;

	public Map<?, ?> upload(MultipartFile multipartFile, String folder) {
		getResult(folder, multipartFile);
		return getResult(folder, multipartFile);
	}

	public String uploadAndGetUrl(String folder, MultipartFile multipartFile) {
		return (String) getResult(folder, multipartFile).get("url");
	}

	private Map<?, ?> getResult(String folder, MultipartFile multipartFile) {
		Map<?, ?> result = null;
		try {
			Map<String, String> option = new HashMap<>();
			option.put("resource_type", "auto");
			option.put("folder", folder);

			result = cloudinary.uploader().upload(multipartFile.getBytes(), option);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void delete(String publicId) {
		try {
			cloudinary.uploader().destroy(publicId, Collections.emptyMap());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void delete(List<String> publicIds) {
		try {
			cloudinary.api().deleteResources(publicIds, Collections.emptyMap());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
