package com.mrluo.cloud.common.defs;

import com.baomidou.mybatisplus.core.enums.IEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RemoteInvokeFailed implements IEnum<String> {
    GENERAL("GENERAL_REMOTE_INVOKE_FAILED");

    private final String value;
}
