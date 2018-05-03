package com.vcredit.global;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * 作者: 伍跃武
 * 时间： 2018/5/3
 * 描述：
 */

public class SampleApplication extends TinkerApplication{
    public SampleApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.vcredit.global.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
