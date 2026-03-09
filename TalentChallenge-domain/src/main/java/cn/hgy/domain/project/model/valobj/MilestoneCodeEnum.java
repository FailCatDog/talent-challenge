package cn.hgy.domain.project.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-13
 * @Description: 里程碑编码枚举（固定7个里程碑）
 */
@Getter
@AllArgsConstructor
public enum MilestoneCodeEnum {
    
    AGREEMENT_SIGNING("协议签订", "agreement_signing"),
    PROPOSAL_SUBMISSION("方案提交", "proposal_submission"),
    MID_TERM_DEFENSE("中期答辩", "mid_term_defense"),
    DELIVERABLE_SUBMISSION("成果提交", "deliverable_submission"),
    RESULT_REVIEW("成果评审", "result_review"),
    PROJECT_SETTLEMENT("项目结算", "project_settlement"),
    CLOSURE_ANNOUNCEMENT("结项公示", "closure_announcement");
    
    private final String milestoneName;
    private final String milestoneCode;
    
    public static boolean isValidCode(String code) {
        for (MilestoneCodeEnum codeEnum : values()) {
            if (codeEnum.getMilestoneCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
    
    public static MilestoneCodeEnum getByCode(String code) {
        for (MilestoneCodeEnum codeEnum : values()) {
            if (codeEnum.getMilestoneCode().equals(code)) {
                return codeEnum;
            }
        }
        return null;
    }
}

