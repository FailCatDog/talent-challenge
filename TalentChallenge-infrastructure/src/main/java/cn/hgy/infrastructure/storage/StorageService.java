package cn.hgy.infrastructure.storage;

import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 简单本地存储服务（兼容 Windows / Linux）
 * Windows：默认存到 {user.dir}/data/milestones
 * Linux：默认存到 /data/milestones
 */
@Slf4j
@Service
public class StorageService {

    @Value("${file.upload.path.milestones}")
    private String milestonesBasePath;

    @Value("${file.upload.path.qualifications}")
    private String qualificationsBasePath;

    @Value("${file.upload.path.bids}")
    private String bidsBasePath;

    @Value("${file.upload.allowed-types}")
    private String allowedTypesConfig;

    private static final String STATIC_PREFIX = "/static/milestones";

    /**
     * 存储方案提交文件，返回可访问的相对 URL
     */
    public String storeProposalFile(String projectId, String milestoneId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件为空");
        }
        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.trim().isEmpty()) {
            originalName = generateDefaultName(file.getName());
        }

        // 验证文件类型
        validateFileType(originalName);

        String sanitizedName = sanitizeFileName(originalName);

        Path targetDir = Paths.get(milestonesBasePath, projectId, milestoneId);
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }
        Path targetFile = targetDir.resolve(sanitizedName);
        Files.copy(file.getInputStream(), targetFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        String url = String.format("%s/%s/%s/%s", STATIC_PREFIX, projectId, milestoneId, sanitizedName);
        log.info("文件已保存 path={}, url={}", targetFile, url);
        return url;
    }

    /**
     * 存储企业资质文件，返回可访问的相对 URL
     */
    public String storeQualificationFile(String userId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件为空");
        }
        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.trim().isEmpty()) {
            originalName = generateDefaultName(file.getName());
        }

        // 验证文件类型
        validateFileType(originalName);

        String sanitizedName = sanitizeFileName(originalName);

        Path targetDir = Paths.get(qualificationsBasePath, userId);
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }
        Path targetFile = targetDir.resolve(sanitizedName);
        Files.copy(file.getInputStream(), targetFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        String url = String.format("/static/qualifications/%s/%s", userId, sanitizedName);
        log.info("资质文件已保存 path={}, url={}", targetFile, url);
        return url;
    }

    /**
     * 存储揭榜申请附件，返回可访问的相对 URL
     */
    public String storeBidAttachment(String bidId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件为空");
        }
        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.trim().isEmpty()) {
            originalName = generateDefaultName(file.getName());
        }

        // 验证文件类型
        validateFileType(originalName);

        String sanitizedName = sanitizeFileName(originalName);

        Path targetDir = Paths.get(bidsBasePath, bidId);
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }
        Path targetFile = targetDir.resolve(sanitizedName);
        Files.copy(file.getInputStream(), targetFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        String url = String.format("/static/bids/%s/%s", bidId, sanitizedName);
        log.info("揭榜附件已保存 path={}, url={}", targetFile, url);
        return url;
    }

    /**
     * 存储里程碑参考文件（企业上传的要求文档、规范文件等），返回可访问的相对 URL
     */
    public String storeMilestoneReferenceFile(String projectId, String milestoneId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件为空");
        }
        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.trim().isEmpty()) {
            originalName = generateDefaultName(file.getName());
        }

        validateFileType(originalName);
        String sanitizedName = sanitizeFileName(originalName);

        Path targetDir = Paths.get(milestonesBasePath, "references", projectId, milestoneId);
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }
        Path targetFile = targetDir.resolve(sanitizedName);
        Files.copy(file.getInputStream(), targetFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        String url = String.format("/static/milestones/references/%s/%s/%s", projectId, milestoneId, sanitizedName);
        log.info("里程碑参考文件已保存 path={}, url={}", targetFile, url);
        return url;
    }

    private String generateDefaultName(String seed) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "upload_" + time + "_" + Objects.toString(seed, "file");
    }

    private String sanitizeFileName(String name) {
        // 去除路径分隔符，防止目录穿越
        String replaced = name.replace("\\", "_").replace("/", "_");
        return replaced.trim();
    }

    /**
     * 验证文件类型
     */
    private void validateFileType(String fileName) {
        String extension = getFileExtension(fileName);

        if (StrUtil.isBlank(allowedTypesConfig)) {
            throw new AppException(ResponseCode.UN_ERROR.getCode(), "文件类型配置错误");
        }

        List<String> allowedTypes = Arrays.asList(allowedTypesConfig.split(","));

        if (!allowedTypes.contains(extension.toLowerCase())) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(),
                String.format("不支持的文件类型 .%s，仅支持：%s", extension, allowedTypesConfig));
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}

