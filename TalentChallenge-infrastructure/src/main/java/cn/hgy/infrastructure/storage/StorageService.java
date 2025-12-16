package cn.hgy.infrastructure.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 简单本地存储服务（兼容 Windows / Linux）
 * Windows：默认存到 {user.dir}/data/milestones
 * Linux：默认存到 /data/milestones
 */
@Slf4j
@Service
public class StorageService {

    private static final String WINDOWS_PREFIX = "win";
    private static final String STATIC_PREFIX = "/static/milestones";

    /**
     * 存储方案提交文件，返回可访问的相对 URL
     */
    public String storeProposalFile(String projectId, String milestoneId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件为空");
        }
        String baseDir = resolveBaseDir();
        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.trim().isEmpty()) {
            originalName = generateDefaultName(file.getName());
        }
        String sanitizedName = sanitizeFileName(originalName);

        Path targetDir = Paths.get(baseDir, projectId, milestoneId);
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }
        Path targetFile = targetDir.resolve(sanitizedName);
        Files.copy(file.getInputStream(), targetFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        String url = String.format("%s/%s/%s/%s", STATIC_PREFIX, projectId, milestoneId, sanitizedName);
        log.info("文件已保存 path={}, url={}", targetFile, url);
        return url;
    }

    private String resolveBaseDir() {
        String osName = System.getProperty("os.name", "").toLowerCase();
        boolean isWindows = osName.startsWith(WINDOWS_PREFIX);
        if (isWindows) {
            return Paths.get(System.getProperty("user.dir"), "data", "milestones").toString();
        }
        return Paths.get("/data", "milestones").toString();
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
}

