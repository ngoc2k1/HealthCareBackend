package vn.healthcare.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.healthcare.dto.BaseResponse;
import vn.healthcare.service.CloudinaryService;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class FileController {
    private final CloudinaryService cloudinaryService;

    @PostMapping("/file/upload")
    public BaseResponse uploadFile(@RequestPart MultipartFile file) {
        String url = cloudinaryService.uploadAndGetUrl("healthcare", file);

        return new BaseResponse(200, "Upload thành công", url);
    }
}
