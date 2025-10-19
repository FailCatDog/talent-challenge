package cn.hgy.domain.project.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectStatusEnum {
    
    DRAFT("草稿", "draft"),
    PENDING_REVIEW("待审核", "pending_review"),
    REJECTED("已拒绝", "rejected"),
    PUBLISHED("已发布", "published"),
    IN_PROGRESS("进行中", "in_progress"),
    COMPLETED("已完成", "completed"),
    CLOSED("已关闭", "closed");
    
    private final String statusName;
    private final String statusCode;
    
    public static boolean isValidStatus(String status) {
        for (ProjectStatusEnum statusEnum : values()) {
            if (statusEnum.getStatusCode().equals(status) || statusEnum.getStatusName().equals(status)) {
                return true;
            }
        }
        return false;
    }
    
    public static ProjectStatusEnum getByCode(String code) {
        for (ProjectStatusEnum statusEnum : values()) {
            if (statusEnum.getStatusCode().equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }
}
