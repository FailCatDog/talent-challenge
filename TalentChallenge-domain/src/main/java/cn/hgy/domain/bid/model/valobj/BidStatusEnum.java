package cn.hgy.domain.bid.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BidStatusEnum {

    APPLIED("APPLIED"),
    SHORTLISTED("SHORTLISTED"),
    CONFIRMED("CONFIRMED"),
    REJECTED("REJECTED"),
    CANCELLED("CANCELLED"),
    EXPIRED("EXPIRED");

    private final String code;
}

