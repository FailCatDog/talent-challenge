package cn.hgy.domain.bid.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BidStatusEnum {

    APPLIED("applied"),
    SHORTLISTED("shortlisted"),
    CONFIRMED("confirmed"),
    REJECTED("rejected"),
    CANCELLED("cancelled"),
    EXPIRED("expired");

    private final String code;

    public static BidStatusEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (BidStatusEnum statusEnum : values()) {
            if (statusEnum.getCode().equalsIgnoreCase(code)) {
                return statusEnum;
            }
        }
        return null;
    }
}

